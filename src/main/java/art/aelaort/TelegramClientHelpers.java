package art.aelaort;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramClientHelpers {
	public static Message execute(SendMessage sendMessage, TelegramClient telegramClient) {
		try {
			return telegramClient.execute(sendMessage);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	public static void execute(BotApiMethod<?> botApiMethod, TelegramClient telegramClient) {
		try {
			telegramClient.execute(botApiMethod);
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}
}
