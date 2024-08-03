package net.minecraft.network.chat.contents;

import java.util.Locale;

public class TranslatableFormatException extends IllegalArgumentException {

    public TranslatableFormatException(TranslatableContents translatableContents0, String string1) {
        super(String.format(Locale.ROOT, "Error parsing: %s: %s", translatableContents0, string1));
    }

    public TranslatableFormatException(TranslatableContents translatableContents0, int int1) {
        super(String.format(Locale.ROOT, "Invalid index %d requested for %s", int1, translatableContents0));
    }

    public TranslatableFormatException(TranslatableContents translatableContents0, Throwable throwable1) {
        super(String.format(Locale.ROOT, "Error while parsing: %s", translatableContents0), throwable1);
    }
}