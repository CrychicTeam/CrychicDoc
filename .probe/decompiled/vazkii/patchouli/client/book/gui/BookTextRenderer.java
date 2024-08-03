package vazkii.patchouli.client.book.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.LiteralContents;
import vazkii.patchouli.api.PatchouliConfigAccess;
import vazkii.patchouli.client.book.text.BookTextParser;
import vazkii.patchouli.client.book.text.TextLayouter;
import vazkii.patchouli.client.book.text.Word;
import vazkii.patchouli.common.base.PatchouliConfig;
import vazkii.patchouli.common.book.Book;

public class BookTextRenderer {

    private final Book book;

    private final List<Word> words;

    private final float scale;

    public BookTextRenderer(GuiBook gui, Component text, int x, int y) {
        this(gui, text, x, y, 116, 9, gui.book.textColor);
    }

    public BookTextRenderer(GuiBook gui, Component text, int x, int y, int width, int lineHeight, int baseColor) {
        this.book = gui.book;
        Component text1;
        if (this.book.i18n && text.getContents() instanceof LiteralContents lc) {
            text1 = Component.literal(I18n.get(lc.text()));
        } else {
            text1 = text;
        }
        Style baseStyle = this.book.getFontStyle().withColor(TextColor.fromRgb(baseColor));
        BookTextParser parser = new BookTextParser(gui, this.book, x, y, width, lineHeight, baseStyle);
        PatchouliConfigAccess.TextOverflowMode overflowMode = this.book.overflowMode;
        if (overflowMode == null) {
            overflowMode = PatchouliConfig.get().overflowMode();
        }
        TextLayouter layouter = new TextLayouter(gui, x, y, lineHeight, width, overflowMode);
        layouter.layout(Minecraft.getInstance().font, parser.parse(text1));
        this.scale = layouter.getScale();
        this.words = layouter.getWords();
    }

    private double rescale(double in, double origin) {
        return origin + (in - origin) / (double) this.scale;
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY) {
        if (!this.words.isEmpty()) {
            Font font = Minecraft.getInstance().font;
            Style style = this.book.getFontStyle();
            Word first = (Word) this.words.get(0);
            graphics.pose().pushPose();
            graphics.pose().translate((float) first.x, (float) first.y, 0.0F);
            graphics.pose().scale(this.scale, this.scale, 1.0F);
            graphics.pose().translate((float) (-first.x), (float) (-first.y), 0.0F);
            int scaledX = (int) this.rescale((double) mouseX, (double) first.x);
            int scaledY = (int) this.rescale((double) mouseY, (double) first.y);
            this.words.forEach(word -> word.render(graphics, font, style, scaledX, scaledY));
            graphics.pose().popPose();
        }
    }

    public boolean click(double mouseX, double mouseY, int mouseButton) {
        if (!this.words.isEmpty()) {
            Word first = (Word) this.words.get(0);
            double scaledX = this.rescale(mouseX, (double) first.x);
            double scaledY = this.rescale(mouseY, (double) first.y);
            for (Word word : this.words) {
                if (word.click(scaledX, scaledY, mouseButton)) {
                    return true;
                }
            }
        }
        return false;
    }
}