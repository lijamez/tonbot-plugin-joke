package net.tonbot.plugin.jokes;

import java.util.List;
import java.util.Random;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import net.tonbot.common.Activity;
import net.tonbot.common.ActivityDescriptor;
import net.tonbot.common.BotUtils;
import net.tonbot.plugin.jokes.model.Joke;
import net.tonbot.plugin.jokes.model.JokesBundle;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class TellMeAJokeActivity implements Activity {

	private static final ActivityDescriptor ACTIVITY_DESCRIPTOR = ActivityDescriptor.builder()
			.route("tell me a joke")
			.description("I tell you a joke.")
			.build();

	private final BotUtils botUtils;
	private final JokesBundle jokesBundle;
	private final JokeExecutor jokeExecutor;

	@Inject
	public TellMeAJokeActivity(BotUtils botUtils, JokesBundle jokesBundle, JokeExecutor jokeExecutor) {
		this.botUtils = Preconditions.checkNotNull(botUtils, "botUtils must be non-null.");
		this.jokesBundle = Preconditions.checkNotNull(jokesBundle, "jokesBundle must be non-null.");
		this.jokeExecutor = Preconditions.checkNotNull(jokeExecutor, "jokeExecutor must be non-null.");
	}

	@Override
	public void enact(MessageReceivedEvent event, String args) {
		List<Joke> jokes = jokesBundle.getJokes();

		Joke joke;
		try {
			joke = jokes.get(Integer.parseInt(args));
		} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
			joke = jokes.get(new Random().nextInt(jokes.size()));
		}

		jokeExecutor.execute(joke, event.getChannel(), botUtils);
	}

	@Override
	public ActivityDescriptor getDescriptor() {
		return ACTIVITY_DESCRIPTOR;
	}

}
