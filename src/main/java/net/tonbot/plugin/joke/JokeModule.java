package net.tonbot.plugin.joke;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import net.tonbot.common.Activity;
import net.tonbot.common.BotUtils;
import net.tonbot.plugin.joke.model.JokesBundle;
import net.tonbot.plugin.joke.sequencer.SequenceExecutor;

class JokeModule extends AbstractModule {

	private final BotUtils botUtils;
	private final File jokesFile;

	public JokeModule(BotUtils botUtils, File jokesFile) {
		this.botUtils = Preconditions.checkNotNull(botUtils, "botUtils must be non-null.");
		this.jokesFile = Preconditions.checkNotNull(jokesFile, "jokesFile must be non-null.");
	}

	@Override
	protected void configure() {
		bind(BotUtils.class).toInstance(botUtils);
	}

	@Provides
	@Singleton
	Set<Activity> activities(TellMeAJokeActivity tellMeAJokeActivity) {
		return ImmutableSet.of(tellMeAJokeActivity);
	}

	@Provides
	@Singleton
	SequenceExecutor sequenceExecutor() {
		return new SequenceExecutor(Executors.newFixedThreadPool(5));
	}

	@Provides
	@Singleton
	JokesBundle jokesBundle() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.enable(Feature.ALLOW_COMMENTS);

		JokesBundle jokesBundle = objMapper.readValue(jokesFile, JokesBundle.class);
		return jokesBundle;
	}
}
