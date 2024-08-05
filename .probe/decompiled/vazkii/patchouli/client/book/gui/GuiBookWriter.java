package vazkii.patchouli.client.book.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.gui.button.GuiButtonBook;
import vazkii.patchouli.common.book.Book;

public class GuiBookWriter extends GuiBook {

    private BookTextRenderer text;

    private BookTextRenderer editableText;

    private EditBox textfield;

    private static String savedText = "";

    private static boolean drawHeader;

    public GuiBookWriter(Book book) {
        super(book, Component.empty());
    }

    @Override
    public void init() {
        super.init();
        this.text = new BookTextRenderer(this, Component.translatable("patchouli.gui.lexicon.editor.info"), 15, 38);
        this.textfield = new EditBox(this.f_96547_, 10, 140, 116, 20, Component.empty());
        this.textfield.setMaxLength(Integer.MAX_VALUE);
        this.textfield.setValue(savedText);
        GuiButtonBook button = new GuiButtonBook(this, this.bookLeft + 115, this.bookTop + 156 - 36, 330, 9, 11, 11, this::handleToggleHeaderButton, Component.translatable("patchouli.gui.lexicon.button.toggle_mock_header"));
        this.m_142416_(button);
        this.refreshText();
    }

    @Override
    void drawForegroundElements(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawForegroundElements(graphics, mouseX, mouseY, partialTicks);
        this.drawCenteredStringNoShadow(graphics, I18n.get("patchouli.gui.lexicon.editor"), 73, 18, this.book.headerColor);
        drawSeparator(graphics, this.book, 15, 30);
        if (drawHeader) {
            this.drawCenteredStringNoShadow(graphics, I18n.get("patchouli.gui.lexicon.editor.mock_header"), 199, 18, this.book.headerColor);
            drawSeparator(graphics, this.book, 141, 30);
        }
        this.textfield.m_88315_(graphics, mouseX, mouseY, partialTicks);
        this.text.render(graphics, mouseX, mouseY);
        this.editableText.render(graphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClickedScaled(double mouseX, double mouseY, int mouseButton) {
        return this.textfield.m_6375_(this.getRelativeX(mouseX), this.getRelativeY(mouseY), mouseButton) || this.text.click(mouseX, mouseY, mouseButton) || this.editableText.click(mouseX, mouseY, mouseButton) || super.mouseClickedScaled(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if (this.textfield.keyPressed(key, scanCode, modifiers)) {
            this.refreshText();
            return true;
        } else {
            return super.keyPressed(key, scanCode, modifiers);
        }
    }

    @Override
    public boolean charTyped(char c, int i) {
        if (this.textfield.charTyped(c, i)) {
            this.refreshText();
            return true;
        } else {
            return super.m_5534_(c, i);
        }
    }

    private void handleToggleHeaderButton(Button button) {
        drawHeader = !drawHeader;
        this.refreshText();
    }

    private void refreshText() {
        int yPos = 18 + (drawHeader ? 22 : -4);
        savedText = this.textfield.getValue();
        try {
            this.editableText = new BookTextRenderer(this, Component.literal(savedText), 141, yPos);
        } catch (Throwable var3) {
            this.editableText = new BookTextRenderer(this, Component.literal("[ERROR]"), 141, yPos);
            PatchouliAPI.LOGGER.catching(var3);
        }
    }
}