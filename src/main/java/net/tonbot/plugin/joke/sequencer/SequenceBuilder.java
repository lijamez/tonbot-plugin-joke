package net.tonbot.plugin.joke.sequencer;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

public class SequenceBuilder {

	private final List<Object> steps;

	public SequenceBuilder() {
		this.steps = new ArrayList<>();
	}

	public SequenceBuilder perform(Action action) {
		Preconditions.checkNotNull(action, "action must be non-null");

		this.steps.add(action);

		return this;
	}

	public <T> SequenceBuilder react(EventListener<T> eventListener, Reaction<T> reaction) {
		Preconditions.checkNotNull(eventListener, "eventListener must be non-null");
		Preconditions.checkNotNull(reaction, "reaction must be non-null");

		this.steps.add(new ReactionStep<T>(eventListener, reaction));

		return this;
	}

	public Sequence build() {
		return new Sequence(steps);
	}
}
