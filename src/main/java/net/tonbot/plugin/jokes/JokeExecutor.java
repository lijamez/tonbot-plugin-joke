package net.tonbot.plugin.jokes;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import net.tonbot.common.BotUtils;
import net.tonbot.common.TonbotTechnicalFault;
import net.tonbot.plugin.jokes.model.Joke;
import net.tonbot.plugin.jokes.model.JokeStep;
import net.tonbot.plugin.jokes.model.SayStep;
import net.tonbot.plugin.jokes.model.WaitStep;
import net.tonbot.plugin.jokes.sequencer.Sequence;
import net.tonbot.plugin.jokes.sequencer.SequenceBuilder;
import net.tonbot.plugin.jokes.sequencer.SequenceExecutor;
import sx.blah.discord.handle.obj.IChannel;

class JokeExecutor {

	private final SequenceExecutor sequenceExecutor;

	@Inject
	public JokeExecutor(SequenceExecutor sequenceExecutor) {
		this.sequenceExecutor = Preconditions.checkNotNull(sequenceExecutor, "sequenceExecutor must be non-null.");
	}

	public void execute(Joke joke, IChannel channel, BotUtils botUtils) {
		Preconditions.checkNotNull(joke, "joke must be non-null.");

		List<JokeStep> steps = joke.getSequence();

		SequenceBuilder sb = Sequence.builder();

		for (JokeStep jokeStep : steps) {
			if (jokeStep instanceof SayStep) {
				SayStep sayStep = (SayStep) jokeStep;

				sb.perform(() -> botUtils.sendMessageSync(channel, sayStep.getMessage()));
			} else if (jokeStep instanceof WaitStep) {
				WaitStep waitStep = (WaitStep) jokeStep;
				
				sb.perform(() -> {
					try {
						channel.setTypingStatus(true);
						Thread.sleep(waitStep.getMillis());
					} catch (InterruptedException e) {
						throw new TonbotTechnicalFault("Wait was interrupted.", e);
					} finally {
						channel.setTypingStatus(false);
					}
				});
			}
		}

		Sequence sequence = sb.build();

		sequenceExecutor.execute(sequence);
	}
}
