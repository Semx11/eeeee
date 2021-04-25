package me.semx11.eeeee.render;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum EnumRenderType {

    OFF,
    EEE(s -> s.replaceAll("(?<!\u00a7)[a-zA-Z0-9]", "e")),
    VEWELS(s -> {
        s = s.replaceAll("(?<!\u00a7)[aeoiu]", "e");
        return s.replaceAll("(?<!\u00a7)[AEOIU]", "E");
    }),
    VOWELS(s -> {
        s = s.replaceAll("(?<!\u00a7)[a]", "\u00a7s");
        s = s.replaceAll("(?<!\u00a7)[e]", "\u00a7t");
        s = s.replaceAll("(?<!\u00a7)[o]", "\u00a7v");
        s = s.replaceAll("(?<!\u00a7)[i]", "\u00a7w");
        s = s.replaceAll("(?<!\u00a7)[u]", "\u00a7x");
        s = s.replaceAll("(?<!\u00a7)[A]", "\u00a7S");
        s = s.replaceAll("(?<!\u00a7)[E]", "\u00a7T");
        s = s.replaceAll("(?<!\u00a7)[O]", "\u00a7V");
        s = s.replaceAll("(?<!\u00a7)[I]", "\u00a7W");
        s = s.replaceAll("(?<!\u00a7)[U]", "\u00a7X");

        s = s.replaceAll("\u00a7s", "e");
        s = s.replaceAll("\u00a7t", "o");
        s = s.replaceAll("\u00a7v", "i");
        s = s.replaceAll("\u00a7w", "u");
        s = s.replaceAll("\u00a7x", "a");
        s = s.replaceAll("\u00a7S", "E");
        s = s.replaceAll("\u00a7T", "O");
        s = s.replaceAll("\u00a7V", "I");
        s = s.replaceAll("\u00a7W", "U");
        return s.replaceAll("\u00a7X", "A");
    }),
    RANDOM_NUMBERS(s -> {
        if (isCached(s)) {
            return getFromCache(s);
        }
        Pattern pattern = Pattern.compile("(?<!\u00a7)\\d");
        Matcher matcher = pattern.matcher(s);

        StringBuffer buffer = new StringBuffer(s.length());
        while (matcher.find()) {
            matcher.appendReplacement(buffer, Integer.toString(getRandomNumber()));
        }
        matcher.appendTail(buffer);
        String result = buffer.toString();
        cacheString(s, result);
        return result;
    }),
    ROT13(s -> {
        Pattern pattern = Pattern.compile("(?<!\u00a7)[a-zA-Z]+");
        Matcher matcher = pattern.matcher(s);

        StringBuffer buffer = new StringBuffer(s.length());
        while (matcher.find()) {
            matcher.appendReplacement(buffer, rot13(matcher.group(0)));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }),
    REVERSE_ALL(s -> {
        if (!s.contains("\u00a7")) {
            return new StringBuilder(s).reverse().toString();
        }
        if (!s.startsWith("\u00a7")) {
            s = "\u00a7f" + s;
        }
        s += "\u00a7";
        Pattern pattern = Pattern
                .compile("((?<format>(\u00a7[0-9a-fk-or])+)(?<text>[^\u00a7]+?(?=\u00a7)))");
        Matcher matcher = pattern.matcher(s);
        StringBuilder build = new StringBuilder();
        while (matcher.find()) {
            String format = matcher.group("format");
            String text = matcher.group("text");
            build.insert(0, format + new StringBuilder(text).reverse());
        }
        String text = build.toString();
        if (text.startsWith("\u00a7rr\u00a7")) {
            text = text.substring(4);
        }
        return text;
    });

    private static final Random RANDOM = new Random();
    private static final Map<String, String> STRING_CACHE = new ConcurrentHashMap<>();

    private static long cacheAge = System.currentTimeMillis();

    private boolean hasFunction;
    private Function<String, String> function;

    EnumRenderType() {
        this.hasFunction = false;
    }

    EnumRenderType(Function<String, String> function) {
        this.hasFunction = true;
        this.function = function;
    }

    public boolean hasFunction() {
        return hasFunction;
    }

    public String apply(String s) {
        if (this == RANDOM_NUMBERS && System.currentTimeMillis() - cacheAge >= 5000) {
            clearCache();
        }
        if (isCached(s)) {
            return getFromCache(s);
        }
        String apply = function.apply(s);
        cacheString(s, apply);
        return apply;
    }

    public EnumRenderType next() {
        clearCache();
        EnumRenderType[] values = EnumRenderType.values();
        return values[this.ordinal() + 1 > values.length - 1 ? 0 : this.ordinal() + 1];
    }

    public static int getRandomNumber() {
        return RANDOM.nextInt(9) + 1;
    }

    public static boolean isCached(String key) {
        return STRING_CACHE.containsKey(key);
    }

    public static void cacheString(String key, String value) {
        STRING_CACHE.put(key, value);
    }

    public static String getFromCache(String key) {
        return STRING_CACHE.get(key);
    }

    public static void clearCache() {
        System.out.println("String cache size: " + STRING_CACHE.size());
        STRING_CACHE.clear();
        cacheAge = System.currentTimeMillis();
    }

    public static String rot13(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 'a' && c <= 'm') {
                c += 13;
            } else if (c >= 'A' && c <= 'M') {
                c += 13;
            } else if (c >= 'n' && c <= 'z') {
                c -= 13;
            } else if (c >= 'N' && c <= 'Z') {
                c -= 13;
            }
            sb.append(c);
        }
        return sb.toString();
    }

}
