package art.aelaort;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramClientHelpers {
	public static void execute(SendMediaGroup.SendMediaGroupBuilder<?, ?> sendMediaGroupBuilder, TelegramClient telegramClient) {
		try {
			telegramClient.execute(sendMediaGroupBuilder.build());
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	public static void execute(SendPhoto.SendPhotoBuilder<?, ?> sendPhotoBuilder, TelegramClient telegramClient) {
		try {
			telegramClient.execute(sendPhotoBuilder.build());
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	public static void execute(SendVideo.SendVideoBuilder<?, ?> sendVideoBuilder, TelegramClient telegramClient) {
		try {
			telegramClient.execute(sendVideoBuilder.build());
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

	public static void execute(SendMessage.SendMessageBuilder<?, ?> sendMessageBuilder, TelegramClient telegramClient) {
		try {
			telegramClient.execute(sendMessageBuilder.build());
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

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
