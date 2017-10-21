package net.tonbot.plugin.jokes.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import lombok.Data;

@Data
public class Joke {

	private final List<JokeStep> sequence;

	@JsonCreator
	public Joke(@JsonProperty("sequence") List<JokeStep> sequence) {
		Preconditions.checkNotNull(sequence, "sequence must be non-null.");
		this.sequence = ImmutableList.copyOf(sequence);
	}
}
