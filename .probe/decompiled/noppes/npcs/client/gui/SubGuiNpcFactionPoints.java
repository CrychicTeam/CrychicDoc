package noppes.npcs.client.gui;

import net.minecraft.client.resources.language.I18n;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class SubGuiNpcFactionPoints extends GuiBasic implements ITextfieldListener {

    private Faction faction;

    public SubGuiNpcFactionPoints(Faction faction) {
        this.faction = faction;
        this.setBackground("menubg.png");
        this.imageWidth = 256;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(2, "faction.default", this.guiLeft + 4, this.guiTop + 33));
        this.addTextField(new GuiTextFieldNop(2, this, this.guiLeft + 8 + this.f_96547_.width(this.getLabel(2).m_6035_()), this.guiTop + 28, 70, 20, this.faction.defaultPoints + ""));
        this.getTextField(2).m_94199_(6);
        this.getTextField(2).numbersOnly = true;
        String title = I18n.get("faction.unfriendly") + "<->" + I18n.get("faction.neutral");
        this.addLabel(new GuiLabel(3, title, this.guiLeft + 4, this.guiTop + 80));
        this.addTextField(new GuiTextFieldNop(3, this, this.guiLeft + 8 + this.f_96547_.width(title), this.guiTop + 75, 70, 20, this.faction.neutralPoints + ""));
        title = I18n.get("faction.neutral") + "<->" + I18n.get("faction.friendly");
        this.addLabel(new GuiLabel(4, title, this.guiLeft + 4, this.guiTop + 105));
        this.addTextField(new GuiTextFieldNop(4, this, this.guiLeft + 8 + this.f_96547_.width(title), this.guiTop + 100, 70, 20, this.faction.friendlyPoints + ""));
        this.getTextField(3).numbersOnly = true;
        this.getTextField(4).numbersOnly = true;
        if (this.getTextField(3).m_252754_() > this.getTextField(4).m_252754_()) {
            this.getTextField(4).m_252865_(this.getTextField(3).m_252754_());
        } else {
            this.getTextField(3).m_252865_(this.getTextField(4).m_252754_());
        }
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 20, this.guiTop + 192, 90, 20, "gui.done"));
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 2) {
            this.faction.defaultPoints = textfield.getInteger();
        } else if (textfield.id == 3) {
            this.faction.neutralPoints = textfield.getInteger();
        } else if (textfield.id == 4) {
            this.faction.friendlyPoints = textfield.getInteger();
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 66) {
            this.close();
        }
    }
}