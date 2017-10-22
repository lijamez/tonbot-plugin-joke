package net.tonbot.plugin.jokes.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import lombok.Data;

@Data
public class WaitForMessageStep implements JokeStep {

	private List<String> matches;

	public WaitForMessageStep(@JsonProperty("matches") List<String> matches) {
		Preconditions.checkNotNull(matches, "matches must be non-null.");

		this.matches = ImmutableList.copyOf(matches);
	}
}
