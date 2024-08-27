package art.aelaort;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Command {
	String getCommand();

	static <T extends Command> Map<String, T> buildMap(Class<T> commandsClass) {
		if (commandsClass.isEnum()) {
			return Stream.of(commandsClass.getEnumConstants())
					.sorted()
					.collect(Collectors.toMap(
							Command::getCommand,
							Function.identity(),
							(t, t2) -> t,
							TreeMap::new
					));
		}
		throw new RuntimeException("passed not enum class");
	}
}
