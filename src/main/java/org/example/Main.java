package org.example;

import java.net.URL;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Throwable {
        HashMap<URL, String> previousPages = new HashMap<>();
        previousPages.put(new URL("https://www.google.com"), "<title>Google</title>");
        previousPages.put(new URL("https://www.yandex.ru"), "<title>Yandex</title>");
        previousPages.put(new URL("https://www.yahoo.com"), "<title>Yahoo</title>");

        HashMap<URL, String> currentPages = new HashMap<>();
        currentPages.put(new URL("https://www.bing.com"), "<title>Bing</title>");
        currentPages.put(new URL("https://www.google.com"), "<title>Google</title>");
        currentPages.put(new URL("https://www.yandex.ru"), "<title>Яндекс</title>");
        currentPages.put(new URL("https://www.rambler.ru"), "<title>Rambler</title>");

        String pattern = """
            Здравствуйте, дорогая и.о. секретаря
                        
            За последние сутки во вверенных Вам сайтах произошли следующие изменения:
                        
            Исчезли следующие страницы: {0}
            Появились следующие новые страницы: {1}
            Изменились следующие страницы: {2}
                        
            С уважением,
            автоматизированная система
            мониторинга.
            """;
        MessageBuilder messageBuilder = new MessageBuilder(pattern);
        System.out.println(messageBuilder.buildMessage(previousPages, currentPages));
    }
}