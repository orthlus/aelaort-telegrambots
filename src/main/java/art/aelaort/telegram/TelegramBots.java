package art.aelaort.telegram;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.TelegramUrl;

import java.net.URI;
import java.util.List;
import java.util.function.Supplier;

public class TelegramBots {
	public static TelegramBotsLongPollingApplication application() {
		return new TelegramBotsLongPollingApplication();
	}

	public static TelegramInit createTelegramInit(List<SimpleLongPollingBot> bots,
												  Supplier<TelegramUrl> telegramUrlSupplier,
												  String botsUrl) {
		return new TelegramInit(application(), bots, telegramUrlSupplier, botsUrl);
	}

	public static TelegramInit createTelegramInit(List<SimpleLongPollingBot> bots, String botsUrl) {
		return new TelegramInit(application(), bots, () -> TelegramUrl.DEFAULT_URL, botsUrl);
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
