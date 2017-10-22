package net.tonbot.plugin.jokes;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import net.tonbot.plugin.jokes.sequencer.SequenceExecutor;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

class MessageListener {

	private final SequenceExecutor sequenceExecutor;

	@Inject
	public MessageListener(SequenceExecutor sequenceExecutor) {
		this.sequenceExecutor = Preconditions.checkNotNull(sequenceExecutor, "sequenceExecutor must be non-null.");
	}

	@EventSubscriber
	public void onMessageReceived(MessageReceivedEvent event) {
		sequenceExecutor.takeInput(event);
	}
}
