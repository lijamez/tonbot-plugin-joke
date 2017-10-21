package net.tonbot.plugin.joke;

import java.util.Set;
import java.util.concurrent.Executors;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import net.tonbot.common.Activity;
import net.tonbot.common.BotUtils;
import net.tonbot.plugin.joke.sequencer.SequenceExecutor;

class JokeModule extends AbstractModule {

	private final BotUtils botUtils;

	public JokeModule(BotUtils botUtils) {
		this.botUtils = Preconditions.checkNotNull(botUtils, "botUtils must be non-null.");
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
}
