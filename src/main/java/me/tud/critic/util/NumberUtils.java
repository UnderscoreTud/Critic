package me.tud.critic.util;

public final class NumberUtils {

    private NumberUtils() {
        throw new UnsupportedOperationException();
    }

    public static int compare(Number first, Number second) {
        return new NumberComparator().compare(first, second);
    }

    public static Class<? extends Number> getNumberClass(Number... numbers) {
        Class<? extends Number> numberClass = Integer.class;
        for (Number number : numbers) {
            if (number.getClass() == Float.class && numberClass != Float.class)
                numberClass = Float.class;
            if (number.getClass() == Double.class)
                return Double.class;
        }
        return numberClass;
    }

    public static long parseInt(String string) {
        int radix = 10;
        if (string.startsWith("0b"))
            radix = 2;
        else if (string.startsWith("00"))
            radix = 8;
        else if (string.startsWith("0x"))
            radix = 16;

        if (radix != 10)
            string = string.substring(2);

        return Long.parseLong(string, radix);
    }

}
