package vazkii.patchouli.api;

import java.util.function.UnaryOperator;
import net.minecraft.network.chat.Style;

public interface IStyleStack {

    void modifyStyle(UnaryOperator<Style> var1);

    void pushStyle(Style var1);

    Style popStyle();

    Style peekStyle();

    void reset();
}