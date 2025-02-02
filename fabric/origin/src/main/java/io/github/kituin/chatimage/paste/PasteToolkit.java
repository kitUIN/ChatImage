package io.github.kituin.chatimage.paste;

import java.util.Locale;

public class PasteToolkit {

    private static final IPasteCompat pasteCompat;

    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac");
    }

    static {
        if (isMac()) {
            pasteCompat = new MacPasteCompat();
        } else {
            pasteCompat = new WindowsPasteCompat();
        }
    }

    public static IPasteCompat getPasteCompat() {
        return pasteCompat;
    }
}
