package org.example;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class MessageBuilderTest {
    private MessageBuilder messageBuilder;
    private MessageFormat msg;

    @Before
    public void setUp() {
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
        messageBuilder = new MessageBuilder(pattern);
        msg = new MessageFormat(pattern);
    }

    @Test
    public void shouldInsertAlteredPage() throws MalformedURLException {
        HashMap<URL, String> previousPages = new HashMap<>();
        HashMap<URL, String> currentPages = new HashMap<>();

        previousPages.put(new URL("https://www.google.com"), "<title>Google</title>");
        previousPages.put(new URL("https://www.yandex.ru"), "<title>Yandex</title>");
        previousPages.put(new URL("https://www.yahoo.com"), "<title>Yahoo</title>");

        currentPages.put(new URL("https://www.google.com"), "<title>Google</title>");
        currentPages.put(new URL("https://www.yahoo.com"), "<title>Yahooooooooooooooooo</title>");
        currentPages.put(new URL("https://www.yandex.ru"), "<title>Yandex</title>");

        Object[] args = {"[]", "[]", "[https://www.yahoo.com]"};
        String expected = msg.format(args);

        assertEquals(expected, messageBuilder.buildMessage(previousPages, currentPages));
    }

    @Test
    public void shouldInsertNewPage() throws MalformedURLException {
        HashMap<URL, String> previousPages = new HashMap<>();
        HashMap<URL, String> currentPages = new HashMap<>();

        previousPages.put(new URL("https://www.yahoo.com"), "<title>Yahoo</title>");

        currentPages.put(new URL("https://www.google.com"), "<title>Google</title>");
        currentPages.put(new URL("https://www.yahoo.com"), "<title>Yahoo</title>");

        Object[] args = {"[]", "[https://www.google.com]",  "[]"};
        String expected = msg.format(args);

        assertEquals(expected, messageBuilder.buildMessage(previousPages, currentPages));
    }

    @Test
    public void shouldInsertRemovedPage() throws MalformedURLException {
        HashMap<URL, String> previousPages = new HashMap<>();
        HashMap<URL, String> currentPages = new HashMap<>();

        previousPages.put(new URL("https://www.yandex.ru"), "<title>Яндекс</title>");
        previousPages.put(new URL("https://www.google.com"), "<title>Google</title>");

        currentPages.put(new URL("https://www.yandex.ru"), "<title>Яндекс</title>");

        Object[] args = {"[https://www.google.com]",  "[]", "[]"};
        String expected = msg.format(args);

        assertEquals(expected, messageBuilder.buildMessage(previousPages, currentPages));
    }

    @Test
    public void oneOfMapsIsEmpty() throws MalformedURLException {
        HashMap<URL, String> previousPages = new HashMap<>();
        HashMap<URL, String> currentPages = new HashMap<>();

        previousPages.put(new URL("https://www.google.com"), "<title>Google</title>");

        Object[] args = {"[https://www.google.com]", "[]", "[]"};
        String expected = msg.format(args);

        assertEquals(expected, messageBuilder.buildMessage(previousPages, currentPages));
    }

    @Test
    public void bothMapsAreEmpty() {
        HashMap<URL, String> previousPages = new HashMap<>();
        HashMap<URL, String> currentPages = new HashMap<>();

        Object[] args = {"[]", "[]", "[]"};
        String expected = msg.format(args);

        assertEquals(expected, messageBuilder.buildMessage(previousPages, currentPages));
    }

    @Test
    public void oneMapContainsNullValue() throws MalformedURLException {
        HashMap<URL, String> previousPages = new HashMap<>();
        HashMap<URL, String> currentPages = new HashMap<>();

        previousPages.put(new URL("https://www.google.com"), "<title>Google</title>");
        previousPages.put(new URL("https://www.yandex.ru"), "<title>Yandex</title>");
        currentPages.put(new URL("https://www.google.com"), null);
        currentPages.put(new URL("https://www.yandex.ru"), "<title>Yandex</title>");

        Object[] args = {"[]", "[]", "[https://www.google.com]"};
        String expected = msg.format(args);

        assertEquals(expected, messageBuilder.buildMessage(previousPages, currentPages));
    }

    @Test
    public void oneMapContainsNullKey() throws MalformedURLException {
        HashMap<URL, String> previousPages = new HashMap<>();
        HashMap<URL, String> currentPages = new HashMap<>();

        previousPages.put(new URL("https://www.google.com"), "<title>Google</title>");
        previousPages.put(new URL("https://www.yandex.ru"), "<title>Yandex</title>");
        currentPages.put(null, "<title>Google</title>");
        currentPages.put(new URL("https://www.yandex.ru"), "<title>Yandex</title>");

        Object[] args = {"[https://www.google.com]", "[]", "[]"};
        String expected = msg.format(args);

        assertEquals(expected, messageBuilder.buildMessage(previousPages, currentPages));
    }

    @Test
    public void bothMapsContainNullKey() throws MalformedURLException {
        HashMap<URL, String> previousPages = new HashMap<>();
        HashMap<URL, String> currentPages = new HashMap<>();

        previousPages.put(new URL("https://www.google.com"), "<title>Google</title>");
        previousPages.put(null, "<title>Yandex</title>");
        currentPages.put(null, "<title>Google</title>");
        currentPages.put(new URL("https://www.yandex.ru"), "<title>Yandex</title>");

        Object[] args = {"[https://www.google.com]", "[https://www.yandex.ru]", "[]"};
        String expected = msg.format(args);

        assertEquals(expected, messageBuilder.buildMessage(previousPages, currentPages));
    }
}
