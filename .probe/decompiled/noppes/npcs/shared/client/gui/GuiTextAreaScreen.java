package noppes.npcs.shared.client.gui;

import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiTextArea;
import noppes.npcs.shared.client.gui.listeners.ITextChangeListener;
import noppes.npcs.shared.client.util.NoppesStringUtils;

public class GuiTextAreaScreen extends GuiBasic implements ITextChangeListener {

    public String text;

    public String originalText;

    private GuiTextArea textarea;

    private boolean highlighting = false;

    public GuiTextAreaScreen(String text) {
        this.text = text;
        this.originalText = text;
        this.setBackground("bgfilled.png");
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    public GuiTextAreaScreen(String originalText, String text) {
        this(text);
        this.originalText = originalText;
    }

    @Override
    public void init() {
        this.imageWidth = (int) ((double) this.f_96543_ * 0.88);
        this.imageHeight = (int) ((double) this.imageWidth * 0.56);
        if ((double) this.imageHeight > (double) this.f_96544_ * 0.95) {
            this.imageHeight = (int) ((double) this.f_96544_ * 0.95);
            this.imageWidth = (int) ((double) this.imageHeight / 0.56);
        }
        this.bgScale = (float) this.imageWidth / 440.0F;
        super.init();
        if (this.textarea != null) {
            this.text = this.textarea.getText();
        }
        int yoffset = (int) ((double) this.imageHeight * 0.02);
        this.textarea = new GuiTextArea(2, this.guiLeft + 1 + yoffset, this.guiTop + yoffset, this.imageWidth - 100 - yoffset, this.imageHeight - yoffset * 2, this.text);
        this.textarea.setListener(this);
        if (this.highlighting) {
            this.textarea.enableCodeHighlighting();
        }
        this.add(this.textarea);
        this.addButton(new GuiButtonNop(this, 102, this.guiLeft + this.imageWidth - 90 - yoffset, this.guiTop + 20, 56, 20, "gui.clear"));
        this.addButton(new GuiButtonNop(this, 101, this.guiLeft + this.imageWidth - 90 - yoffset, this.guiTop + 43, 56, 20, "gui.paste"));
        this.addButton(new GuiButtonNop(this, 100, this.guiLeft + this.imageWidth - 90 - yoffset, this.guiTop + 66, 56, 20, "gui.copy"));
        this.addButton(new GuiButtonNop(this, 103, this.guiLeft + this.imageWidth - 90 - yoffset, this.guiTop + 89, 56, 20, "gui.reset"));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + this.imageWidth - 90 - yoffset, this.guiTop + 160, 56, 20, "gui.close"));
        this.imageWidth = 420;
        this.imageHeight = 256;
    }

    public GuiTextAreaScreen enableHighlighting() {
        this.highlighting = true;
        return this;
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 100) {
            NoppesStringUtils.setClipboardContents(this.textarea.getText());
        }
        if (id == 101) {
            this.textarea.setText(NoppesStringUtils.getClipboardContents());
        }
        if (id == 102) {
            this.textarea.setText("");
        }
        if (id == 103) {
            this.textarea.setText(this.originalText);
        }
        if (id == 0) {
            this.close();
        }
    }

    @Override
    public void textUpdate(String text) {
        this.text = text;
    }
}