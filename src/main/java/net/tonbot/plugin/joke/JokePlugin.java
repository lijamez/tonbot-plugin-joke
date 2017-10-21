package net.tonbot.plugin.joke;

import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import net.tonbot.common.Activity;
import net.tonbot.common.TonbotPlugin;
import net.tonbot.common.TonbotPluginArgs;

public class JokePlugin extends TonbotPlugin {

	private final Injector injector;

	public JokePlugin(TonbotPluginArgs pluginArgs) {
		super(pluginArgs);

		this.injector = Guice.createInjector(new JokeModule(pluginArgs.getBotUtils()));
	}

	@Override
	public Set<Activity> getActivities() {
		return injector.getInstance(Key.get(new TypeLiteral<Set<Activity>>() {
		}));
	}

	@Override
	public String getActionDescription() {
		return "Tell Jokes";
	}

	@Override
	public String getFriendlyName() {
		return "Joke";
	}

}
