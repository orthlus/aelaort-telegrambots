package art.aelaort;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("telegram.list")
public class TelegramListProperties {
	private String url;
	private String user;
	private String password;
}
