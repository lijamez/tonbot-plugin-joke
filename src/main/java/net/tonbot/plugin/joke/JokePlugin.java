package net.tonbot.plugin.joke;

import java.io.File;
import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import net.tonbot.common.Activity;
import net.tonbot.common.PluginSetupException;
import net.tonbot.common.TonbotPlugin;
import net.tonbot.common.TonbotPluginArgs;

public class JokePlugin extends TonbotPlugin {

	private final Injector injector;

	public JokePlugin(TonbotPluginArgs pluginArgs) {
		super(pluginArgs);

		File jokesBundleFile = new File(pluginArgs.getPluginDataDir(), "jokes.json");

		if (!jokesBundleFile.exists()) {
			throw new PluginSetupException("Jokes file does not exist at: " + jokesBundleFile.getAbsolutePath());
		}

		this.injector = Guice.createInjector(new JokeModule(pluginArgs.getBotUtils(), jokesBundleFile));
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
