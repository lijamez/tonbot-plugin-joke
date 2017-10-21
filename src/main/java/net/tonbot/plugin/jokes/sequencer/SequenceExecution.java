package net.tonbot.plugin.jokes.sequencer;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import lombok.Data;
import net.tonbot.common.TonbotTechnicalFault;

@Data
public class SequenceExecution implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(SequenceExecution.class);

	private final Sequence sequence;

	private boolean isDone = false;
	private boolean isWaitingForInput = false;
	private ArrayBlockingQueue<Object> nextInputs = new ArrayBlockingQueue<Object>(100);

	public SequenceExecution(Sequence sequence) {
		this.sequence = Preconditions.checkNotNull(sequence, "sequence must be non-null.");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run() {
		this.isDone = false;

		for (Object step : sequence.getSteps()) {
			if (step instanceof Action) {
				Action action = (Action) step;
				action.perform();
			} else if (step instanceof ReactionStep) {
				ReactionStep reaction = (ReactionStep) step;

				isWaitingForInput = true;

				while (true) {
					try {
						Object nextInput = nextInputs.take();
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
				LOG.warn("Unknown step type: {}", step.getClass().getName());
			}
		}

		this.isDone = true;

	}

	/**
	 * 
	 * @param input
	 *            Non-null.
	 */
	public void takeInput(Object input) {
		Preconditions.checkNotNull(input, "input must be non-null.");

		if (isWaitingForInput) {
			if (!nextInputs.offer(input)) {
				LOG.warn("SequenceExecution's input buffer has been exceeded.");
			}
		}
	}
}
