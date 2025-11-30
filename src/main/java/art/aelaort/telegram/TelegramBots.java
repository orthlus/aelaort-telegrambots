package art.aelaort.telegram;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.TelegramUrl;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

public class TelegramBots {
	public static TelegramBotsLongPollingApplication application() {
		return new TelegramBotsLongPollingApplication(
				ObjectMapper::new,
				() -> new OkHttpClient.Builder()
						.callTimeout(Duration.ofMinutes(2))
						.connectTimeout(Duration.ofMinutes(2))
						.readTimeout(Duration.ofMinutes(2))
						.writeTimeout(Duration.ofMinutes(2))
						.build()
		);
	}

	public static TelegramInit createTelegramInit(List<SimpleLongPollingBot> bots,
												  Supplier<TelegramUrl> telegramUrlSupplier) {
		return new TelegramInit(application(), bots, telegramUrlSupplier);
	}

	public static TelegramInit createTelegramInit(List<SimpleLongPollingBot> bots) {
		return new TelegramInit(application(), bots, () -> TelegramUrl.DEFAULT_URL);
	}

	public static Supplier<TelegramUrl> telegramUrlSupplier(String url) {
		return () -> buildTelegramUrl(url);
	}

	public static Supplier<TelegramUrl> telegramUrlSupplier(String schema, String host, int port) {
		return () -> TelegramUrl.builder().schema(schema).host(host).port(port).build();
	}

	public static TelegramUrl buildTelegramUrl(String url) {
		URI uri = URI.create(url);
		return TelegramUrl.builder()
				.schema(uri.getScheme())
				.host(uri.getHost())
				.port(uri.getPort())
				.build();
	}
}
