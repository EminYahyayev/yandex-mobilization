package com.ewintory.yandex.mobilization.utils;

import java.util.Iterator;
import java.util.List;

public final class StringUtils {

    private StringUtils() {
        throw new AssertionError("No instances.");
    }

    @SuppressWarnings("unused")
    public static String join(List<String> strings, String delimiter) {
        return join(strings, delimiter, new StringBuilder(strings.size() * 8));
    }

    @SuppressWarnings("unused")
    public static String joinIntegers(List<Integer> integers, String delimiter) {
        return joinIntegers(integers, delimiter, new StringBuilder(30));
    }


    @SuppressWarnings("unused")
    public static String join(List<String> strings, String delimiter, StringBuilder builder) {
        builder.setLength(0);
        if (strings != null)
            for (String str : strings) {
                if (builder.length() > 0) builder.append(delimiter);
                builder.append(str);
            }
        return builder.toString();
    }

    @SuppressWarnings("unused")
    public static String join(String[] strings, String delimiter, StringBuilder builder) {
        builder.setLength(0);
        if (strings != null)
            for (String str : strings) {
                if (builder.length() > 0) builder.append(delimiter);
                builder.append(str);
            }
        return builder.toString();
    }

    @SuppressWarnings("unused")
    public static String join(Iterator<String> iterator, String delimiter, StringBuilder builder) {
        builder.setLength(0);
        while (iterator.hasNext()) {
            if (builder.length() > 0)
                builder.append(delimiter);
            builder.append(iterator.next());
        }
        return builder.toString();
    }

    @SuppressWarnings("unused")
    public static String joinIntegers(List<Integer> integers, String delimiter, StringBuilder builder) {
        builder.setLength(0);
        if (integers != null)
            for (Integer integer : integers) {
                if (builder.length() > 0) builder.append(delimiter);
                builder.append(String.valueOf(integer));
            }
        return builder.toString();
    }

    /**
     * Was taken from <a src="http://stackoverflow.com/a/25379180/4178734">SO answer</a>
     *
     * @param src
     * @param what
     * @return
     */
    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }
}