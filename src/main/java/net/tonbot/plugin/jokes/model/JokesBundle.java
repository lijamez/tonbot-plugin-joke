package net.tonbot.plugin.jokes.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import lombok.Data;

@Data
public class JokesBundle {

	private final List<Joke> jokes;

	@JsonCreator
	public JokesBundle(@JsonProperty("jokes") List<Joke> jokes) {
		Preconditions.checkNotNull(jokes, "jokes must be non-null.");
		this.jokes = ImmutableList.copyOf(jokes);
	}
}
