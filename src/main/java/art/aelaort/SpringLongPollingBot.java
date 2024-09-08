package art.aelaort;

import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;

public interface SpringLongPollingBot extends LongPollingSingleThreadUpdateConsumer {
	String getBotToken();

	default LongPollingUpdateConsumer getUpdatesConsumer() {
		return this;
	}

	String getName();

	default String getDescription() {
		return getName();
	}
}
