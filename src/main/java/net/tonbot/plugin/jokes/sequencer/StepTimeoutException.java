package net.tonbot.plugin.jokes.sequencer;

@SuppressWarnings("serial")
public class StepTimeoutException extends RuntimeException {

	public StepTimeoutException(String msg) {
		super(msg);
	}
}
