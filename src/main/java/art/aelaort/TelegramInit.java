package art.aelaort;

import art.aelaort.ya.func.helper.FuncParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator;
import org.telegram.telegrambots.meta.TelegramUrl;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TelegramInit implements InitializingBean {
	private final TelegramBotsLongPollingApplication telegramBotsApplication;
	private final List<SpringLongPollingBot> bots;
	private final Supplier<TelegramUrl> telegramUrlSupplier;
	private final FuncParams func;

	@Override
	public void afterPropertiesSet() throws Exception {
		for (SpringLongPollingBot bot : bots) {
			String token = bot.getBotToken();
			LongPollingUpdateConsumer updateConsumer = bot.getUpdatesConsumer();
			DefaultGetUpdatesGenerator generator = new DefaultGetUpdatesGenerator();

			telegramBotsApplication.registerBot(token, telegramUrlSupplier, generator, updateConsumer);
		}
		addBotsToStorage();
	}

	private void addBotsToStorage() {
		String list = bots.stream()
				.map(bot -> bot.getName() + ":" + bot.getDescription())
				.collect(Collectors.joining("\n"));
		HttpEntity<String> entity = new HttpEntity<>(func.secret() + "\n" + list);
		new RestTemplate().postForObject(
				func.uri(),
				entity,
				String.class
		);
	}
}
