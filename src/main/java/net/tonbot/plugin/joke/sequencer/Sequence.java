package net.tonbot.plugin.joke.sequencer;

import java.util.List;

import com.google.common.base.Preconditions;

import lombok.Data;

@Data
public class Sequence {

	private final List<Object> steps;

	Sequence(List<Object> steps) {
		this.steps = Preconditions.checkNotNull(steps, "steps must be non-null.");
	}

	public static SequenceBuilder builder() {
		return new SequenceBuilder();
	}
}
