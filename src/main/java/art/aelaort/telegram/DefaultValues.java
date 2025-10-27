package art.aelaort.telegram;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Getter
public class DefaultValues {
	private final String url;
	private final String user;
	private final String password;

	public DefaultValues() {
		Properties p = loadProperties();
		this.url = p.getProperty("url");
		this.user = p.getProperty("user");
		this.password = p.getProperty("password");
	}

	private Properties loadProperties() {
		try {
			Properties properties = new Properties();
			String file = "default-values.properties";
			try (InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(file),
					StandardCharsets.UTF_8)) {
				properties.load(reader);
			}
			return properties;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
