package net.tonbot.plugin.jokes.sequencer;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class SequenceExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(SequenceExecutor.class);

	private final ExecutorService execService;
	private final CopyOnWriteArrayList<SequenceExecution> executions;

	public SequenceExecutor(ExecutorService execService) {
		this.execService = Preconditions.checkNotNull(execService, "execService must be non-null.");
		this.executions = new CopyOnWriteArrayList<>();
	}

	/**
	 * Takes input and sends it to all sequences that are awaiting an event.
	 * 
	 * @param inputObj
	 *            The input object. Non-null.
	 */
	public void takeInput(Object inputObj) {
		Preconditions.checkNotNull(inputObj, "inputObj must be non-null.");

		for (SequenceExecution exec : executions) {
			exec.takeInput(inputObj);
		}
	}

	/**
	 * Executes a {@link Sequence}
	 * 
	 * @param sequence
	 *            The {@link Sequence} to execute. Non-null.
	 */
	public void execute(Sequence sequence) {
		Preconditions.checkNotNull(sequence, "sequence must be non-null.");

		SequenceExecution seqExec = new SequenceExecution(sequence, this);

		// Optimistically add the execution to the list.
		// The add occurs first just in case notifyIsDone is called sooner than the add.
		executions.add(seqExec);
		try {
			execService.submit(seqExec);
		} catch (RejectedExecutionException e) {
			LOG.warn("Unable to execute sequence.", e);
			executions.remove(seqExec);
		}
	}

	/**
	 * A callback function which {@link SequenceExecution} will call when they are
	 * done executing.
	 * 
	 * @param sequence
	 *            {@link Sequence} which finished execution.
	 */
	public void notifyIsDone(SequenceExecution sequenceExecution) {
		executions.remove(sequenceExecution);
	}

}
