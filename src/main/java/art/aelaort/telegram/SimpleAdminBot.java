package art.aelaort.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface SimpleAdminBot extends SimpleLongPollingBot {
	long getAdminId();

	void consumeAdmin(Update update);

	@Override
	default void consume(Update update) {
		if (update.hasMessage()) {
			if (update.getMessage().getChatId() == getAdminId()) {
				consumeAdmin(update);
			}
		} else if (update.hasCallbackQuery()) {
			if (update.getCallbackQuery().getMessage().getChatId() == getAdminId()) {
				consumeAdmin(update);
			}
		}
	}
}
