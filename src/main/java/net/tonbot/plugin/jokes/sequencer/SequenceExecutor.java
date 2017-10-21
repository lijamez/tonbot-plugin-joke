package net.tonbot.plugin.jokes.sequencer;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import com.google.common.base.Preconditions;

public class SequenceExecutor {

	private final ExecutorService execService;
	private final CopyOnWriteArrayList<SequenceExecution> executions;

	public SequenceExecutor(ExecutorService execService) {
		this.execService = Preconditions.checkNotNull(execService, "execService must be non-null.");
		this.executions = new CopyOnWriteArrayList<>();
	}

	/**
	 * Takes input and sends it to all sequences that are awaiting an event.
	 * 
	 * @param in
	 */
	public void takeInput(Object in) {

		for (SequenceExecution exec : executions) {
			exec.takeInput(in);
		}
	}

	public void execute(Sequence sequence) {
		execService.submit(new SequenceExecution(sequence));
	}
}
