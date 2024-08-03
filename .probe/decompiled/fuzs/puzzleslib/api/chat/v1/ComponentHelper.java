package fuzs.puzzleslib.api.chat.v1;

import fuzs.puzzleslib.impl.chat.FormattedContentSink;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

public class ComponentHelper {

    public static Component toComponent(FormattedText formattedText) {
        return new FormattedContentSink(formattedText).getComponent();
    }

    public static Component toComponent(FormattedCharSequence formattedCharSequence) {
        return new FormattedContentSink(formattedCharSequence).getComponent();
    }
}