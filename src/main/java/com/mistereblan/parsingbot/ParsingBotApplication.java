package com.mistereblan.parsingbot;

import bots.ParsingBot;
import config.BotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@SpringBootApplication
public class ParsingBotApplication {

	public static void main(String[] args) {
		BotConfig botConfig = new BotConfig();
		String botToken = botConfig.getToken();

		try {
			TelegramBotsLongPollingApplication telegramBotsApi = new TelegramBotsLongPollingApplication();
			ParsingBot telegramBot = new ParsingBot();
			telegramBotsApi.registerBot(botToken, telegramBot);
		} catch (Exception e) {
			System.out.println("MAIN: Something went wrong");
			e.printStackTrace();
		}


		SpringApplication.run(ParsingBotApplication.class, args);
	}

}
