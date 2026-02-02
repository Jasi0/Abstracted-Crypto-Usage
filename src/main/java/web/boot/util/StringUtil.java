package web.boot.util;

public final class StringUtil {
    private StringUtil() {}

    public static String reverse(String s) {
        if (s == null) return null;
        return new StringBuilder(s).reverse().toString();
    }

    public static String uppercase(String s) {
        if (s == null) return null;
        return s.toUpperCase();
    }
}