package noppes.npcs.client.gui;

import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;

public class SubGuiEditText extends GuiBasic {

    public String text;

    public boolean cancelled = true;

    public int id;

    public SubGuiEditText(String text) {
        this.text = text;
        this.setBackground("extrasmallbg.png");
        this.imageWidth = 176;
        this.imageHeight = 71;
    }

    public SubGuiEditText(int id, String text) {
        this(text);
        this.id = id;
    }

    @Override
    public void init() {
        super.init();
        this.addTextField(new GuiTextFieldNop(0, this.wrapper.parent, this.guiLeft + 4, this.guiTop + 14, 168, 20, this.text));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 4, this.guiTop + 44, 80, 20, "gui.done"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 90, this.guiTop + 44, 80, 20, "gui.cancel"));
    }

    @Override
    public void buttonEvent(GuiButtonNop button) {
        if (button.id == 0) {
            this.cancelled = false;
            this.text = this.getTextField(0).m_94155_();
        }
        this.close();
    }

    @Override
    public void save() {
    }
}