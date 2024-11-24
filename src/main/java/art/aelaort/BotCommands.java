package art.aelaort;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class BotCommands {
	public static void setCommands(SpringLongPollingBot bot) {
		Class<?>[] declaredClasses = bot.getClass().getDeclaredClasses();
		for (Class<?> declaredClass : declaredClasses) {
			if (declaredClass.isEnum()) {
				if (getInterfaces(declaredClass).contains(Command.class)) {
					List<BotCommand> commands = map(declaredClass);
					setCommands(commands, bot);
					System.out.printf("for %s set commands: %s\n", bot.getClass(), commands);
				}
			}
		}
	}

	private static Set<Class<?>> getInterfaces(Class<?> declaredClass) {
		return new HashSet<>(Arrays.asList(declaredClass.getInterfaces()));
	}

	private static List<BotCommand> map(Class<?> declaredClass) {
		return Stream.of(declaredClass.getEnumConstants())
				.map(Object::toString)
				.map(String::toLowerCase)
				.map(s -> new BotCommand(s, s))
				.toList();
	}

	@SneakyThrows
	private static void setCommands(List<BotCommand> commands, SpringLongPollingBot bot) {
		TelegramClientBuilder.builder()
				.token(bot.getBotToken())
				.build()
				.execute(new SetMyCommands(commands));
	}
}
