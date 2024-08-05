package net.minecraftforge.client.extensions;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;

public interface IForgeFont {

    FormattedText ELLIPSIS = FormattedText.of("...");

    Font self();

    default FormattedText ellipsize(FormattedText text, int maxWidth) {
        Font self = this.self();
        int strWidth = self.width(text);
        int ellipsisWidth = self.width(ELLIPSIS);
        if (strWidth > maxWidth) {
            return ellipsisWidth >= maxWidth ? self.substrByWidth(text, maxWidth) : FormattedText.composite(self.substrByWidth(text, maxWidth - ellipsisWidth), ELLIPSIS);
        } else {
            return text;
        }
    }
}