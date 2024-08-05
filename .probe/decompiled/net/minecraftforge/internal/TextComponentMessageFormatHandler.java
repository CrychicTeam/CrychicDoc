package net.minecraftforge.internal;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.common.ForgeI18n;

public class TextComponentMessageFormatHandler {

    public static int handle(TranslatableContents parent, Consumer<FormattedText> addChild, Object[] formatArgs, String format) {
        try {
            String formattedString = ForgeI18n.parseFormat(format, formatArgs);
            if (format.indexOf(39) != -1) {
                boolean onlyMissingQuotes = format.chars().filter(ch -> formattedString.indexOf((char) ch) == -1).allMatch(ch -> ch == 39);
                if (onlyMissingQuotes) {
                    return 0;
                }
            }
            MutableComponent component = Component.literal(formattedString);
            addChild.accept(component);
            return format.length();
        } catch (IllegalArgumentException var6) {
            return 0;
        }
    }
}