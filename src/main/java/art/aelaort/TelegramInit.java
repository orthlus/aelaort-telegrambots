package art.aelaort;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator;
import org.telegram.telegrambots.meta.TelegramUrl;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TelegramInit implements InitializingBean {
	private final TelegramBotsLongPollingApplication telegramBotsApplication;
	private final SpringLongPollingBot bot;
	private final Supplier<TelegramUrl> telegramUrlSupplier;

	@Override
	public void afterPropertiesSet() throws Exception {
		String token = bot.getBotToken();
		LongPollingUpdateConsumer updateConsumer = bot.getUpdatesConsumer();
		DefaultGetUpdatesGenerator generator = new DefaultGetUpdatesGenerator();

		telegramBotsApplication.registerBot(token, telegramUrlSupplier, generator, updateConsumer);
	}
}
