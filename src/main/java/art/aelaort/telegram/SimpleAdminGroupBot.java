package art.aelaort.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

public interface SimpleAdminGroupBot extends SimpleLongPollingBot {
	long getAdminId();

	void consumeAdmin(Update update);

	Set<Long> groupsIds();

	@Override
	default void consume(Update update) {
		if (update.hasMessage()) {
			Long chatId = update.getMessage().getChatId();
			if (chatId == getAdminId() || groupsIds().contains(chatId)) {
				consumeAdmin(update);
			}
		} else if (update.hasCallbackQuery()) {
			Long chatId = update.getCallbackQuery().getMessage().getChatId();
			if (chatId == getAdminId() || groupsIds().contains(chatId)) {
				consumeAdmin(update);
			}
		}
	}
}
