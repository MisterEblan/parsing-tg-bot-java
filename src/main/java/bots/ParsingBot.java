package bots;

import config.BotConfig;
import config.ParserConfig;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import parser.SimpleParser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ParsingBot implements LongPollingSingleThreadUpdateConsumer {
    BotConfig botConfig = new BotConfig();
    private final TelegramClient telegramClient = new OkHttpTelegramClient(botConfig.getToken());

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/test")) {
            Map<String, String>logInfo = getInfoForLog(update);
            String answer = parseCommand();

            SendMessage sendMessage = new SendMessage(logInfo.get("chatId"), answer);

            try {
                telegramClient.execute(sendMessage);

                log(
                        logInfo.get("firstName"),
                        logInfo.get("lastName"),
                        logInfo.get("userId"),
                        logInfo.get("userMessage"),
                        answer
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String parseCommand() {
        SimpleParser parser = new SimpleParser();
        ParserConfig config = new ParserConfig();
        StringBuilder result = new StringBuilder();

        try {

            Elements products = parser.parse(config.getUrl(), config.getProductQuery());
            Elements prices   = parser.parse(config.getUrl(), config.getPriceQuery());

            for (Element productName : products) {
                result.append(productName.text());
            }

            for (Element productPrice : prices) {
                result.append("\n").append(productPrice.text()).append(" RUB");
            }
        } catch (IOException e) {
            System.out.println("\nFailed to parse...");
            e.printStackTrace();
        }

        return result.toString();
    }

    private Map<String, String> getInfoForLog(Update update) {
        Map<String, String> LoggerInfo = new HashMap<String, String>();

        String firsName    = update.getMessage().getChat().getFirstName();
        String lastName    = update.getMessage().getChat().getLastName();
        String userId      = String.valueOf(update.getMessage().getChat().getId());
        String userMessage = update.getMessage().getText();
        String chatId        = String.valueOf(update.getMessage().getChatId());

        LoggerInfo.put("firstName", firsName);
        LoggerInfo.put("lastName", lastName);
        LoggerInfo.put("userId", userId);
        LoggerInfo.put("userMessage", userMessage);
        LoggerInfo.put("chatId", chatId);

        return LoggerInfo;
    }

    private void log(String firstName, String lastName, String userId, String userMessage, String answer) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date       date       = new Date();

        System.out.println("\n\t\t==========LOG==========");

        System.out.println(dateFormat.format(date));
        System.out.printf("Message from %s %s (id = %s)", firstName, lastName, userId);
        System.out.printf("\nText: \n%s", userMessage);
        System.out.printf("\n\nBot's answer: \n%s", answer);

        System.out.println("\n\t  ==========LOG END==========");
    }
}