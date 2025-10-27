package art.aelaort.telegram.client;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.function.Supplier;

@Setter
@Accessors(fluent = true)
public class TelegramClientBuilder {
	private String token;
	private Supplier<TelegramUrl> telegramUrlSupplier;

	TelegramClientBuilder() {
	}

	public static TelegramClientBuilder builder() {
		return new TelegramClientBuilder();
	}

	public TelegramClient build() {
		if (telegramUrlSupplier == null) {
			return new OkHttpTelegramClient(token, TelegramUrl.DEFAULT_URL);
		} else {
			return new OkHttpTelegramClient(token, telegramUrlSupplier.get());
		}
	}
}
