package net.tonbot.plugin.jokes.sequencer;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import lombok.Data;
import lombok.Getter;
import net.tonbot.common.TonbotTechnicalFault;

public class SequenceExecution implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(SequenceExecution.class);
	private static final long REACTION_TIMEOUT_MS = 30_000;

	private final Sequence sequence;
	private final SequenceExecutor seqExecutor;

	private Long executionStartTime;

	@Getter
	private boolean isWaitingForInput = false;

	private ArrayBlockingQueue<Object> nextInputs = new ArrayBlockingQueue<Object>(100);

	private volatile State currentExecutionState;
	private volatile long currentReactionWaitTimeNs = 0;

	/**
	 * Constructor.
	 * 
	 * @param sequence
	 *            The {@link Sequence} to be executed. Non-null
	 * @param seqExecutor
	 *            The {@link SequenceExecutor} which started executing this
	 *            sequence. The provided {@link SequenceExecutor} will be notified
	 *            when this sequence is done. Non-null.
	 */
	public SequenceExecution(Sequence sequence, SequenceExecutor seqExecutor) {
		this.sequence = Preconditions.checkNotNull(sequence, "sequence must be non-null.");
		this.seqExecutor = Preconditions.checkNotNull(seqExecutor, "seqExecutor must be non-null.");
	}

	/**
	 * Gets the execution start time in milliseconds.
	 * 
	 * @return The execution start time in milliseconds. Empty if execution has not
	 *         yet started.
	 */
	public Optional<Long> getExecutionStartTime() {
		return Optional.ofNullable(executionStartTime);
	}

	/**
	 * Get the currently executing state info.
	 * 
	 * @return The currently executing state info. Empty if execution has not yet
	 *         started or is finished.
	 */
	public Optional<State> getCurrentExecutionState() {
		return Optional.ofNullable(currentExecutionState);
	}

	/**
	 * Runs the sequence. Will call the {@link SequenceExecutor#notifyIsDone} when
	 * this function completes, regardless of whether an exception is thrown or not.
	 * 
	 * @throws TonbotTechnicalFault
	 *             if any of {@link Sequence}'s step is not supported.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run() {
		executionStartTime = System.currentTimeMillis();

		try {
			for (Step step : sequence.getSteps()) {
				LOG.debug("Running sequence step {}", step);
				this.currentExecutionState = new State(step, System.currentTimeMillis());

				if (step instanceof Action) {
					Action action = (Action) step;
					action.perform();
				} else if (step instanceof ReactionStep) {
					ReactionStep reaction = (ReactionStep) step;

					isWaitingForInput = true;
					currentReactionWaitTimeNs = 0;

					while (true) {
						try {
							long remainingWaitTimeNs = (REACTION_TIMEOUT_MS * 1_000_000) - currentReactionWaitTimeNs;
							long startWaitTimeNs = System.nanoTime();
							Object nextInput = nextInputs.poll(remainingWaitTimeNs, TimeUnit.NANOSECONDS);
							if (nextInput == null) {
								// Timed out.
								throw new StepTimeoutException(
										"ReactionStep has timed out after " + REACTION_TIMEOUT_MS
												+ " ms to wait for an accepted input.");
							}
							currentReactionWaitTimeNs += System.nanoTime() - startWaitTimeNs;
							Optional<Object> response = reaction.getEventListener().listen(nextInput);
							if (response.isPresent()) {
								reaction.getReaction().perform(response.get());
								break;
							}
						} catch (InterruptedException e) {
							throw new TonbotTechnicalFault("Wait for input was interrupted.", e);
						}
					}

					isWaitingForInput = false;
					nextInputs.clear();
				} else {
					throw new TonbotTechnicalFault("Unknown step type: " + step.getClass().getName());
				}
			}
		} catch (StepTimeoutException e) {
			LOG.info(e.getMessage());
		} finally {
			this.currentExecutionState = null;
			seqExecutor.notifyIsDone(this);
		}
	}

	/**
	 * Provide an input to this execution. Inputs will be buffered. However, this
	 * method is a no-op if this execution isn't waiting for any input.
	 * 
	 * @param input
	 *            The input object. Non-null.
	 */
	public void takeInput(Object input) {
		Preconditions.checkNotNull(input, "input must be non-null.");

		if (isWaitingForInput) {
			if (!nextInputs.offer(input)) {
				LOG.warn("SequenceExecution's input buffer has been exceeded.");
			}
		}
	}

	@Data
	public static class State {
		private final Step step;
		private final long stepStartTimestamp;
	}
}
