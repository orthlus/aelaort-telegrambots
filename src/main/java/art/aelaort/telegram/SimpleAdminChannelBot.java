package art.aelaort.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

public interface SimpleAdminChannelBot extends SimpleLongPollingBot {
	long getAdminId();

	void consumeAdmin(Update update);

	Set<Long> channelsIds();

	@Override
	default void consume(Update update) {
		if (update.hasMessage()) {
			Long chatId = update.getMessage().getChatId();
			if (chatId == getAdminId()) {
				consumeAdmin(update);
			}
		} else if (update.hasChannelPost()) {
			Long chatId = update.getChannelPost().getChatId();
			if (channelsIds().contains(chatId)) {
				consumeAdmin(update);
			}
		} else if (update.hasCallbackQuery()) {
			Long chatId = update.getCallbackQuery().getMessage().getChatId();
			if (chatId == getAdminId() || channelsIds().contains(chatId)) {
				consumeAdmin(update);
			}
		}
	}
}
