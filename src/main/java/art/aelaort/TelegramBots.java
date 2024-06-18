package art.aelaort;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.TelegramUrl;

import java.net.URI;
import java.util.List;
import java.util.function.Supplier;

public class TelegramBots {
	public static TelegramBotsLongPollingApplication telegramBotsApplication() {
		return new TelegramBotsLongPollingApplication();
	}

	public static TelegramInit createTelegramInit(List<SpringLongPollingBot> bots, Supplier<TelegramUrl> telegramUrlSupplier) {
		return new TelegramInit(telegramBotsApplication(), bots, telegramUrlSupplier);
	}

	public static TelegramInit createTelegramInit(List<SpringLongPollingBot> bots) {
		return new TelegramInit(telegramBotsApplication(), bots, () -> TelegramUrl.DEFAULT_URL);
	}

	public static Supplier<TelegramUrl> telegramUrlSupplier(String url) {
		return () -> buildTelegramUrl(url);
	}

	public static Supplier<TelegramUrl> telegramUrlSupplier(String schema, String host, int port) {
		return () -> new TelegramUrl(schema, host, port);
	}

	public static TelegramUrl buildTelegramUrl(String url) {
		URI uri = URI.create(url);

		String host = uri.getHost();
		String scheme = uri.getScheme();
		int port = uri.getPort();

		return new TelegramUrl(scheme, host, port);
	}
}
