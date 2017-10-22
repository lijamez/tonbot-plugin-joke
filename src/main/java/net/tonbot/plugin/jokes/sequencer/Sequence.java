package net.tonbot.plugin.jokes.sequencer;

import java.util.List;

import com.google.common.base.Preconditions;

import lombok.Data;

@Data
public class Sequence {

	private final List<Step> steps;

	Sequence(List<Step> steps) {
		this.steps = Preconditions.checkNotNull(steps, "steps must be non-null.");
	}

	public static SequenceBuilder builder() {
		return new SequenceBuilder();
	}
}
