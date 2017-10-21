package net.tonbot.plugin.joke.sequencer;

@FunctionalInterface
public interface Action {

	/**
	 * Performs an action.
	 */
	abstract void perform();
}
