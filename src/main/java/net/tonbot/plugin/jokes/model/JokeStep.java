package net.tonbot.plugin.jokes.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
		@Type(value = SayStep.class, name = "say"),
		@Type(value = WaitStep.class, name = "wait"),
		@Type(value = WaitForMessageStep.class, name = "wait_for_message") })
public interface JokeStep {

}
