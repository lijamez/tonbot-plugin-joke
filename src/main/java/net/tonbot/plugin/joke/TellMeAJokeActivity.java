package net.tonbot.plugin.joke;

import java.util.List;
import java.util.Random;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import net.tonbot.common.Activity;
import net.tonbot.common.ActivityDescriptor;
import net.tonbot.common.BotUtils;
import net.tonbot.plugin.joke.sequencer.Sequence;
import net.tonbot.plugin.joke.sequencer.SequenceExecutor;
import net.tonbot.plugin.joke.sequencer.Wait;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class TellMeAJokeActivity implements Activity {

	private static final ActivityDescriptor ACTIVITY_DESCRIPTOR = ActivityDescriptor.builder()
			.route("tell me a joke")
			.description("Tell you a joke.")
			.build();

	private final BotUtils botUtils;
	private final SequenceExecutor sequenceExecutor;

	@Inject
	public TellMeAJokeActivity(BotUtils botUtils, SequenceExecutor sequenceExecutor) {
		this.botUtils = Preconditions.checkNotNull(botUtils, "botUtils must be non-null.");
		this.sequenceExecutor = Preconditions.checkNotNull(sequenceExecutor, "sequenceExecutor must be non-null.");
	}

	@Override
	public void enact(MessageReceivedEvent event, String args) {
		List<Sequence> jokes = ImmutableList.of(
				Sequence.builder()
						.perform(() -> botUtils.sendMessage(event.getChannel(), "What can think the unthinkable?"))
						.perform(Wait.forMillis(3000))
						.perform(() -> botUtils.sendMessage(event.getChannel(), "An itheberg."))
						.build(),
				Sequence.builder()
						.perform(() -> botUtils.sendMessage(event.getChannel(),
								"What's the difference between a hippo and a zippo?"))
						.perform(Wait.forMillis(3000))
						.perform(() -> botUtils.sendMessage(event.getChannel(),
								"One's really heavy, the other's a little lighter"))
						.build(),
				Sequence.builder()
						.perform(() -> botUtils.sendMessage(event.getChannel(),
								"What's the stupidest animal in the jungle?"))
						.perform(Wait.forMillis(3000))
						.perform(() -> botUtils.sendMessage(event.getChannel(), "A polar bear."))
						.build(),
				Sequence.builder()
						.perform(() -> botUtils.sendMessage(event.getChannel(),
								"A sandwich walks into a bar. The bartender says \"sorry we don't serve food here\"."))
						.build());

		Sequence randomJoke = jokes.get(new Random().nextInt(jokes.size()));

		sequenceExecutor.execute(randomJoke);
	}

	@Override
	public ActivityDescriptor getDescriptor() {
		return ACTIVITY_DESCRIPTOR;
	}

}
