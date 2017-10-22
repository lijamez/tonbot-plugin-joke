package net.tonbot.plugin.jokes.sequencer;

@FunctionalInterface
public interface Action extends Step {

	/**
	 * Performs an action.
	 */
	abstract void perform();
}
