package net.tonbot.plugin.jokes.sequencer;

import lombok.Data;
import lombok.NonNull;

@Data
class ReactionStep<T> implements Step {

	@NonNull
	private final EventListener<T> eventListener;

	@NonNull
	private final Reaction<T> reaction;
}
