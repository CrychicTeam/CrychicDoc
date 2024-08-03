package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.List;
import noppes.npcs.api.constants.PotionEffectType;
import noppes.npcs.entity.data.DataMelee;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonBiDirectional;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class SubGuiNpcMeleeProperties extends GuiBasic implements ITextfieldListener {

    private DataMelee stats;

    private static final String[] potionNames;

    public SubGuiNpcMeleeProperties(DataMelee stats) {
        this.stats = stats;
        this.setBackground("menubg.png");
        this.imageWidth = 256;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(1, "stats.meleestrength", this.guiLeft + 5, this.guiTop + 15));
        this.addTextField(new GuiTextFieldNop(1, this, this.guiLeft + 85, this.guiTop + 10, 50, 18, this.stats.getStrength() + ""));
        this.getTextField(1).numbersOnly = true;
        this.getTextField(1).setMinMaxDefault(0, Integer.MAX_VALUE, 5);
        this.addLabel(new GuiLabel(2, "stats.meleerange", this.guiLeft + 5, this.guiTop + 45));
        this.addTextField(new GuiTextFieldNop(2, this, this.guiLeft + 85, this.guiTop + 40, 50, 18, this.stats.getRange() + ""));
        this.getTextField(2).numbersOnly = true;
        this.getTextField(2).setMinMaxDefault(1, 30, 2);
        this.addLabel(new GuiLabel(3, "stats.meleespeed", this.guiLeft + 5, this.guiTop + 75));
        this.addTextField(new GuiTextFieldNop(3, this, this.guiLeft + 85, this.guiTop + 70, 50, 18, this.stats.getDelay() + ""));
        this.getTextField(3).numbersOnly = true;
        this.getTextField(3).setMinMaxDefault(1, 1000, 20);
        this.addLabel(new GuiLabel(4, "enchantment.minecraft.knockback", this.guiLeft + 5, this.guiTop + 105));
        this.addTextField(new GuiTextFieldNop(4, this, this.guiLeft + 85, this.guiTop + 100, 50, 18, this.stats.getKnockback() + ""));
        this.getTextField(4).numbersOnly = true;
        this.getTextField(4).setMinMaxDefault(0, 4, 0);
        this.addLabel(new GuiLabel(5, "stats.meleeeffect", this.guiLeft + 5, this.guiTop + 135));
        int effect = this.stats.getEffectType();
        if (effect == 666) {
            effect = potionNames.length - 1;
        }
        this.addButton(new GuiButtonBiDirectional(this, 5, this.guiLeft + 85, this.guiTop + 130, 100, 20, potionNames, effect));
        if (this.stats.getEffectType() != 0) {
            this.addLabel(new GuiLabel(6, "gui.time", this.guiLeft + 5, this.guiTop + 165));
            this.addTextField(new GuiTextFieldNop(6, this, this.guiLeft + 85, this.guiTop + 160, 50, 18, this.stats.getEffectTime() + ""));
            this.getTextField(6).numbersOnly = true;
            this.getTextField(6).setMinMaxDefault(1, 99999, 5);
            if (this.stats.getEffectType() != 666) {
                this.addLabel(new GuiLabel(7, "stats.amplify", this.guiLeft + 5, this.guiTop + 195));
                this.addButton(new GuiButtonBiDirectional(this, 7, this.guiLeft + 85, this.guiTop + 190, 52, 20, new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }, this.stats.getEffectStrength()));
            }
        }
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 164, this.guiTop + 192, 90, 20, "gui.done"));
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 1) {
            this.stats.setStrength(textfield.getInteger());
        } else if (textfield.id == 2) {
            this.stats.setRange(textfield.getInteger());
        } else if (textfield.id == 3) {
            this.stats.setDelay(textfield.getInteger());
        } else if (textfield.id == 4) {
            this.stats.setKnockback(textfield.getInteger());
        } else if (textfield.id == 6) {
            this.stats.setEffect(this.stats.getEffectType(), this.stats.getEffectStrength(), textfield.getInteger());
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop button) {
        if (button.id == 5) {
            int effect = button.getValue();
            if (effect == potionNames.length - 1) {
                effect = 666;
            }
            this.stats.setEffect(effect, this.stats.getEffectStrength(), this.stats.getEffectTime());
            this.init();
        }
        if (button.id == 7) {
            this.stats.setEffect(this.stats.getEffectType(), button.getValue(), this.stats.getEffectTime());
        }
        if (button.id == 66) {
            this.close();
        }
    }

    static {
        List<String> list = new ArrayList();
        list.add("gui.none");
        for (int i = 1; i < 33; i++) {
            list.add(PotionEffectType.getMCType(i).getDescriptionId());
        }
        list.add("block.minecraft.fire");
        potionNames = (String[]) list.toArray(new String[list.size()]);
    }
}