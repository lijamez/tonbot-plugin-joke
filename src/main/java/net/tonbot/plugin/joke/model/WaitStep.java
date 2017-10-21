package net.tonbot.plugin.joke.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import lombok.Data;

@Data
public class WaitStep implements JokeStep {

	private final long millis;

	@JsonCreator
	private WaitStep(
			@JsonProperty("time") long millis) {
		Preconditions.checkArgument(millis >= 0, "millis must be non-negative.");
		this.millis = millis;
	}

	public static WaitStep forMillis(long millis) {
		return new WaitStep(millis);
	}

}
