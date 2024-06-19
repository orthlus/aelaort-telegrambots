package art.aelaort;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramClientHelpers {
	public static void execute(BotApiMethod<?> botApiMethod, TelegramClient telegramClient) {
		try {
			telegramClient.execute(botApiMethod);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}
}
