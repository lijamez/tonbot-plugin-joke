package net.tonbot.plugin.jokes.sequencer;

@FunctionalInterface
public interface Action {

	/**
	 * Performs an action.
	 */
	abstract void perform();
}
