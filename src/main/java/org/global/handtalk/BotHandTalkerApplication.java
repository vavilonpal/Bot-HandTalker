package org.global.handtalk;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotHandTalkerApplication {

    public static void main(String[] args) {

        // Import .env variables
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(e ->
                System.setProperty(e.getKey(), e.getValue())
        );

        SpringApplication.run(BotHandTalkerApplication.class, args);
    }

}
