package art.aelaort;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.api.methods.GetMe;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class TelegramInit implements InitializingBean {
	private final TelegramBotsLongPollingApplication telegramBotsApplication;
	private final List<SpringLongPollingBot> bots;
	private final Supplier<TelegramUrl> telegramUrlSupplier;
	private RestTemplate restTemplate;
	private final DefaultValues defaultValues = new DefaultValues();

	@PostConstruct
	private void init() {
		try {
			restTemplate = new RestTemplateBuilder()
					.rootUri(defaultValues.getUrl())
					.basicAuthentication(defaultValues.getUser(), defaultValues.getPassword())
					.build();
		} catch (Exception ignored) {
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (SpringLongPollingBot bot : bots) {
			String token = bot.getBotToken();
			LongPollingUpdateConsumer updateConsumer = bot.getUpdatesConsumer();
			DefaultGetUpdatesGenerator generator = new DefaultGetUpdatesGenerator();

			telegramBotsApplication.registerBot(token, telegramUrlSupplier, generator, updateConsumer);
			BotCommands.setCommands(bot);
		}
		try {
			addBotsToStorage();
		} catch (Exception ignored) {
		}
		clean();
	}

	private void clean() {
		restTemplate = null;
	}

	private void addBotsToStorage() {
		List<BotInfo> list = bots.stream()
				.filter(this::notExistsInStorage)
				.map(bot -> new BotInfo(getBotToken(bot), getBotName(bot)))
				.toList();
		if (list.isEmpty()) {
			return;
		}
		restTemplate.postForObject("/bots", list, String.class);
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
		return restTemplate.postForObject(
				"/bots/exists",
				getBotToken(bot),
				String.class
		).equals("false");
	}

	private String getBotToken(SpringLongPollingBot bot) {
		return HashUtils.hashText(bot.getBotToken());
	}
}
