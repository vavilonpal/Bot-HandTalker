package org.global.handtalk.bot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "bot")
@PropertySource("classpath:application.yml")
@Getter
@Setter
public class BotProperties {
    String name;
    String token;
}
