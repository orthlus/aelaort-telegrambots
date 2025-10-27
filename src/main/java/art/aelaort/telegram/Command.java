package art.aelaort.telegram;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Command {
	static <T extends Command> Map<String, T> buildMap(Class<T> commandsClass) {
		if (commandsClass.isEnum()) {
			return Stream.of(commandsClass.getEnumConstants())
					.collect(Collectors.toMap(
							t -> "/" + t.toString().toLowerCase(),
							Function.identity(),
							(t1, t2) -> {
								throw new IllegalStateException("Duplicate key in enum " + commandsClass.getName());
							},
							TreeMap::new
					));
		}
		throw new RuntimeException("passed not enum class");
	}
}
