package org.example;

import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

public class MessageBuilder {
    private final MessageFormat msg;

    public MessageBuilder(final String pattern) {
         msg = new MessageFormat(pattern);
    }

    // Get asymmetric set difference
    private static String getPagesDifference(final Map<URL, String> setToInclude, final Map<URL, String> setToExclude) {
        final var set = new HashSet<URL>(setToInclude.keySet());

        set.remove(null);
        set.removeAll(setToExclude.keySet());
        return set.toString();
    }

    private static String getRemovedPages(final Map<URL, String> prevSites, final Map<URL, String> currSites) {
        return getPagesDifference(prevSites, currSites);
    }

    private static String getNewPages(final Map<URL, String> prevSites, final Map<URL, String> currSites) {
        return getPagesDifference(currSites, prevSites);
    }

    private static String getAlteredPages(final Map<URL, String> prevSites, final Map<URL, String> currSites) {
        var alteredPages = new ArrayList<URL>();

        prevSites.forEach((key, value) -> {
            if (key != null && currSites.containsKey(key) && !(Objects.equals(currSites.get(key), value))) {
                alteredPages.add(key);
            }
        });

        return alteredPages.toString();
    }

    public String buildMessage(final Map<URL, String> prevSites, final Map<URL, String> currSites) {
        final var removedPages = getRemovedPages(prevSites, currSites);
        final var newPages = getNewPages(prevSites, currSites);
        final var alteredPages = getAlteredPages(prevSites, currSites);

        Object[] args = {removedPages, newPages, alteredPages};

        return msg.format(args);
    }
}
