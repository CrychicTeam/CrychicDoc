package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.List;
import noppes.npcs.api.constants.PotionEffectType;
import noppes.npcs.entity.data.DataRanged;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonBiDirectional;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class SubGuiNpcProjectiles extends GuiBasic implements ITextfieldListener {

    private DataRanged stats;

    private static final String[] potionNames;

    private String[] trailNames = new String[] { "gui.none", "Smoke", "Portal", "Redstone", "Lightning", "LargeSmoke", "Magic", "Enchant" };

    public SubGuiNpcProjectiles(DataRanged stats) {
        this.stats = stats;
        this.setBackground("menubg.png");
        this.imageWidth = 256;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(1, "effect.minecraft.strength", this.guiLeft + 5, this.guiTop + 15));
        this.addTextField(new GuiTextFieldNop(1, this, this.guiLeft + 45, this.guiTop + 10, 50, 18, this.stats.getStrength() + ""));
        this.getTextField(1).numbersOnly = true;
        this.getTextField(1).setMinMaxDefault(0, Integer.MAX_VALUE, 5);
        this.addLabel(new GuiLabel(2, "enchantment.minecraft.knockback", this.guiLeft + 110, this.guiTop + 15));
        this.addTextField(new GuiTextFieldNop(2, this, this.guiLeft + 150, this.guiTop + 10, 50, 18, this.stats.getKnockback() + ""));
        this.getTextField(2).numbersOnly = true;
        this.getTextField(2).setMinMaxDefault(0, 3, 0);
        this.addLabel(new GuiLabel(3, "stats.size", this.guiLeft + 5, this.guiTop + 45));
        this.addTextField(new GuiTextFieldNop(3, this, this.guiLeft + 45, this.guiTop + 40, 50, 18, this.stats.getSize() + ""));
        this.getTextField(3).numbersOnly = true;
        this.getTextField(3).setMinMaxDefault(5, 20, 10);
        this.addLabel(new GuiLabel(4, "stats.speed", this.guiLeft + 5, this.guiTop + 75));
        this.addTextField(new GuiTextFieldNop(4, this, this.guiLeft + 45, this.guiTop + 70, 50, 18, this.stats.getSpeed() + ""));
        this.getTextField(4).numbersOnly = true;
        this.getTextField(4).setMinMaxDefault(1, 50, 10);
        this.addLabel(new GuiLabel(5, "stats.hasgravity", this.guiLeft + 5, this.guiTop + 105));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 60, this.guiTop + 100, 60, 20, new String[] { "gui.no", "gui.yes" }, this.stats.getHasGravity() ? 1 : 0));
        if (!this.stats.getHasGravity()) {
            this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 140, this.guiTop + 100, 60, 20, new String[] { "gui.constant", "gui.accelerate" }, this.stats.getAccelerate() ? 1 : 0));
        }
        this.addLabel(new GuiLabel(6, "stats.explosive", this.guiLeft + 5, this.guiTop + 135));
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 60, this.guiTop + 130, 60, 20, new String[] { "gui.none", "gui.small", "gui.medium", "gui.large" }, this.stats.getExplodeSize() % 4));
        int effect = this.stats.getEffectType();
        if (effect == 666) {
            effect = potionNames.length - 1;
        }
        this.addLabel(new GuiLabel(7, "stats.rangedeffect", this.guiLeft + 5, this.guiTop + 165));
        this.addButton(new GuiButtonBiDirectional(this, 4, this.guiLeft + 40, this.guiTop + 160, 100, 20, potionNames, effect));
        if (this.stats.getEffectType() != 0) {
            this.addTextField(new GuiTextFieldNop(5, this, this.guiLeft + 140, this.guiTop + 160, 60, 18, this.stats.getEffectTime() + ""));
            this.getTextField(5).numbersOnly = true;
            this.getTextField(5).setMinMaxDefault(1, 99999, 5);
            if (this.stats.getEffectType() != 666) {
                this.addButton(new GuiButtonNop(this, 10, this.guiLeft + 210, this.guiTop + 160, 40, 20, new String[] { "stats.regular", "stats.amplified" }, this.stats.getEffectStrength() % 2));
            }
        }
        this.addLabel(new GuiLabel(8, "stats.trail", this.guiLeft + 5, this.guiTop + 195));
        this.addButton(new GuiButtonNop(this, 5, this.guiLeft + 60, this.guiTop + 190, 60, 20, this.trailNames, this.stats.getParticle()));
        this.addButton(new GuiButtonNop(this, 7, this.guiLeft + 220, this.guiTop + 10, 30, 20, new String[] { "2D", "3D" }, this.stats.getRender3D() ? 1 : 0));
        if (this.stats.getRender3D()) {
            this.addLabel(new GuiLabel(10, "stats.spin", this.guiLeft + 160, this.guiTop + 45));
            this.addButton(new GuiButtonNop(this, 8, this.guiLeft + 220, this.guiTop + 40, 30, 20, new String[] { "gui.no", "gui.yes" }, this.stats.getSpins() ? 1 : 0));
            this.addLabel(new GuiLabel(11, "stats.stick", this.guiLeft + 160, this.guiTop + 75));
            this.addButton(new GuiButtonNop(this, 9, this.guiLeft + 220, this.guiTop + 70, 30, 20, new String[] { "gui.no", "gui.yes" }, this.stats.getSticks() ? 1 : 0));
        }
        this.addButton(new GuiButtonNop(this, 6, this.guiLeft + 140, this.guiTop + 190, 60, 20, new String[] { "stats.noglow", "stats.glows" }, this.stats.getGlows() ? 1 : 0));
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 210, this.guiTop + 190, 40, 20, "gui.done"));
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 1) {
            this.stats.setStrength(textfield.getInteger());
        } else if (textfield.id == 2) {
            this.stats.setKnockback(textfield.getInteger());
        } else if (textfield.id == 3) {
            this.stats.setSize(textfield.getInteger());
        } else if (textfield.id == 4) {
            this.stats.setSpeed(textfield.getInteger());
        } else if (textfield.id == 5) {
            this.stats.setEffect(this.stats.getEffectType(), this.stats.getEffectStrength(), textfield.getInteger());
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.stats.setHasGravity(guibutton.getValue() == 1);
            this.init();
        }
        if (guibutton.id == 1) {
            this.stats.setAccelerate(guibutton.getValue() == 1);
        }
        if (guibutton.id == 3) {
            this.stats.setExplodeSize(guibutton.getValue());
        }
        if (guibutton.id == 4) {
            int effect = guibutton.getValue();
            if (effect == potionNames.length - 1) {
                effect = 666;
            }
            this.stats.setEffect(effect, this.stats.getEffectStrength(), this.stats.getEffectTime());
            this.init();
        }
        if (guibutton.id == 5) {
            this.stats.setParticle(guibutton.getValue());
        }
        if (guibutton.id == 6) {
            this.stats.setGlows(guibutton.getValue() == 1);
        }
        if (guibutton.id == 7) {
            this.stats.setRender3D(guibutton.getValue() == 1);
            this.init();
        }
        if (guibutton.id == 8) {
            this.stats.setSpins(guibutton.getValue() == 1);
        }
        if (guibutton.id == 9) {
            this.stats.setSticks(guibutton.getValue() == 1);
        }
        if (guibutton.id == 10) {
            this.stats.setEffect(this.stats.getEffectType(), guibutton.getValue(), this.stats.getEffectTime());
        }
        if (guibutton.id == 66) {
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