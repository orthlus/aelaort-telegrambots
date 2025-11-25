package art.aelaort.telegram.client;

import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.OkHttpClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.Duration;
import java.util.function.Supplier;

@Setter
@Accessors(fluent = true)
public class TelegramClientBuilder {
	private String token;
	private Supplier<TelegramUrl> telegramUrlSupplier;
	private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
			.callTimeout(Duration.ofMinutes(2))
			.connectTimeout(Duration.ofMinutes(2))
			.readTimeout(Duration.ofMinutes(2))
			.writeTimeout(Duration.ofMinutes(2))
			.build();

	TelegramClientBuilder() {
	}

	public static TelegramClientBuilder builder() {
		return new TelegramClientBuilder();
	}

	public TelegramClient build() {
		if (telegramUrlSupplier == null) {
			return new OkHttpTelegramClient(okHttpClient, token, TelegramUrl.DEFAULT_URL);
		} else {
			return new OkHttpTelegramClient(okHttpClient, token, telegramUrlSupplier.get());
		}
	}
}
