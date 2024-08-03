package net.minecraft.client.gui.components;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.ComponentCollector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

public class ComponentRenderUtils {

    private static final FormattedCharSequence INDENT = FormattedCharSequence.codepoint(32, Style.EMPTY);

    private static String stripColor(String string0) {
        return Minecraft.getInstance().options.chatColors().get() ? string0 : ChatFormatting.stripFormatting(string0);
    }

    public static List<FormattedCharSequence> wrapComponents(FormattedText formattedText0, int int1, Font font2) {
        ComponentCollector $$3 = new ComponentCollector();
        formattedText0.visit((p_93997_, p_93998_) -> {
            $$3.append(FormattedText.of(stripColor(p_93998_), p_93997_));
            return Optional.empty();
        }, Style.EMPTY);
        List<FormattedCharSequence> $$4 = Lists.newArrayList();
        font2.getSplitter().splitLines($$3.getResultOrEmpty(), int1, Style.EMPTY, (p_94003_, p_94004_) -> {
            FormattedCharSequence $$3x = Language.getInstance().getVisualOrder(p_94003_);
            $$4.add(p_94004_ ? FormattedCharSequence.composite(INDENT, $$3x) : $$3x);
        });
        return (List<FormattedCharSequence>) ($$4.isEmpty() ? Lists.newArrayList(new FormattedCharSequence[] { FormattedCharSequence.EMPTY }) : $$4);
    }
}