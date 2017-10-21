package net.tonbot.plugin.joke.sequencer;

import java.util.Optional;

@FunctionalInterface
public interface EventListener<T> {

	/**
	 * Consumes an object, and return a <T> if the event was applicable and
	 * consumed.
	 * 
	 * @param thing
	 * @return A present <T> if the input was applicable and consumed. Empty
	 *         otherwise.
	 */
	Optional<T> listen(Object thing);
}
