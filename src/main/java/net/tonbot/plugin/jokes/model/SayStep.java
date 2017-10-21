package net.tonbot.plugin.jokes.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import lombok.Data;

@Data
public class SayStep implements JokeStep {

	private final String message;

	@JsonCreator
	public SayStep(@JsonProperty("message") String message) {
		this.message = Preconditions.checkNotNull(message, "message must be non-null.");
	}

}
