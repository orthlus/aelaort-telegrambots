package art.aelaort.telegram;

import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;

public interface SimpleLongPollingBot extends LongPollingSingleThreadUpdateConsumer {
	String getBotToken();

	default LongPollingUpdateConsumer getUpdatesConsumer() {
		return this;
	}
}
