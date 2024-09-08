package art.aelaort;

import art.aelaort.ya.func.helper.FuncParams;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.api.methods.GetMe;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TelegramInit implements InitializingBean {
	private final TelegramBotsLongPollingApplication telegramBotsApplication;
	private final List<SpringLongPollingBot> bots;
	private final Supplier<TelegramUrl> telegramUrlSupplier;
	private final FuncParams addFunc;
	private final FuncParams existsFunc;

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
				.filter(this::notExistsInStorage)
				.map(bot -> getBotId(bot) + "," + getBotName(bot))
				.collect(Collectors.joining("\n"));
		HttpEntity<String> entity = new HttpEntity<>(addFunc.secret() + "\n" + list);
		new RestTemplate().postForObject(
				addFunc.uri(),
				entity,
				String.class
		);
	}

	@SneakyThrows
	private String getBotName(SpringLongPollingBot bot) {
		return TelegramClientBuilder.builder()
				.token(bot.getBotToken())
				.build()
				.execute(new GetMe())
				.getUserName();
	}

	private boolean notExistsInStorage(SpringLongPollingBot bot) {
		return new RestTemplate().postForObject(
				existsFunc.uri(),
				new HttpEntity<>(existsFunc.secret() + ":" + getBotId(bot)),
				Boolean.class
		).equals(Boolean.FALSE);
	}

	private String getBotId(SpringLongPollingBot bot) {
		return bot.getBotToken().split(":")[0];
	}
}
