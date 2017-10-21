package net.tonbot.plugin.joke.sequencer;

import com.google.common.base.Preconditions;

import net.tonbot.common.TonbotTechnicalFault;

public class Wait implements Action {

	private final long millis;

	private Wait(long millis) {
		Preconditions.checkArgument(millis >= 0, "millis must be non-negative.");
		this.millis = millis;
	}

	public static Wait forMillis(long millis) {
		return new Wait(millis);
	}

	@Override
	public void perform() {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new TonbotTechnicalFault("Wait was interrupted.", e);
		}
	}

}
