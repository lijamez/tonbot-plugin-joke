package net.tonbot.plugin.jokes;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

import net.tonbot.plugin.jokes.sequencer.EventListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

public class FuzzyMessageMatcher implements EventListener<MessageReceivedEvent> {

	private final IChannel matchingChannel;
	private final List<String> matches;

	public FuzzyMessageMatcher(IChannel matchingChannel, List<String> matches) {
		this.matchingChannel = Preconditions.checkNotNull(matchingChannel, "matchingChannel must be non-null.");
		this.matches = Preconditions.checkNotNull(matches, "matches must be non-null.");
	}

	@Override
	public Optional<MessageReceivedEvent> listen(Object event) {
		if (!(event instanceof MessageReceivedEvent)) {
			return Optional.empty();
		}

		MessageReceivedEvent mrEvent = (MessageReceivedEvent) event;

		if (mrEvent.getChannel().getLongID() != matchingChannel.getLongID()) {
			return Optional.empty();
		}

		IMessage message = mrEvent.getMessage();
		String messageStr = message.getContent();

		if (isMatched(messageStr)) {
			return Optional.of(mrEvent);
		}

		return Optional.empty();
	}

	private boolean isMatched(String messageStr) {
		String normalizedInput = normalize(messageStr);

		for (String candidate : matches) {
			String normalizedCandidate = normalize(candidate);

			if (StringUtils.equalsIgnoreCase(normalizedInput, normalizedCandidate)) {
				return true;
			}
		}

		return false;
	}

	private String normalize(String phrase) {
		String normalized = phrase.trim();

		// Multi-space characters should be replaced with a single space
		normalized = normalized.replaceAll("\\s+", " ");

		// Punctuation should be removed.
		normalized = normalized.replaceAll("\\p{Punct}", "");

		return normalized;
	}

}
