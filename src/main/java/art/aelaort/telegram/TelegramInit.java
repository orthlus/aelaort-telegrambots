package art.aelaort.telegram;

import art.aelaort.telegram.client.TelegramClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator;
import org.telegram.telegrambots.meta.TelegramUrl;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class TelegramInit {
	private final TelegramBotsLongPollingApplication telegramBotsApplication;
	private final List<SimpleLongPollingBot> bots;
	private final Supplier<TelegramUrl> telegramUrlSupplier;
	private final String botsUrl;
	private final HttpClient client = HttpClient.newHttpClient();

	@PostConstruct
	private void init() throws TelegramApiException {
		for (SimpleLongPollingBot bot : bots) {
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
		client.close();
	}

	private void addBotsToStorage() {
		List<BotInfo> list = bots.stream()
				.map(bot -> new BotInfo(getBotToken(bot), getBotName(bot)))
				.toList();
		if (list.isEmpty()) {
			return;
		}
		requestBots(list);
	}

	private void requestBots(List<BotInfo> list) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(botsUrl + "/bots"))
					.POST(HttpRequest.BodyPublishers.ofByteArray(getJsonStr(list).getBytes(StandardCharsets.UTF_8)))
					.header("Content-Type", "application/octet-stream")
					.build();
			HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

			if (response.statusCode() != HttpURLConnection.HTTP_CREATED) {
				System.err.println("TelegramInit http error code: " + response.statusCode());
			}
		} catch (IOException | InterruptedException e) {
			System.err.println("TelegramInit http error: " + e.getMessage());
		}
	}

	private static String getJsonStr(List<BotInfo> list) {
		try {
			return new ObjectMapper().writeValueAsString(list);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@SneakyThrows
	private String getBotName(SimpleLongPollingBot bot) {
		return TelegramClientBuilder.builder()
				.token(bot.getBotToken())
				.build()
				.execute(new GetMe())
				.getUserName();
	}

	private String getBotToken(SimpleLongPollingBot bot) {
		return HashUtils.hashText(bot.getBotToken());
	}
}
