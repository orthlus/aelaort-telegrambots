package art.aelaort.telegram;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class TelegramInit {
	private final TelegramBotsLongPollingApplication telegramBotsApplication;
	private final List<SimpleLongPollingBot> bots;
	private final Supplier<TelegramUrl> telegramUrlSupplier;

	@PostConstruct
	private void init() throws TelegramApiException {
		for (SimpleLongPollingBot bot : bots) {
			String token = bot.getBotToken();
			LongPollingUpdateConsumer updateConsumer = bot.getUpdatesConsumer();
			DefaultGetUpdatesGenerator generator = new DefaultGetUpdatesGenerator();

			telegramBotsApplication.registerBot(token, telegramUrlSupplier, generator, updateConsumer);
			BotCommands.setCommands(bot);
		}
	}
}
