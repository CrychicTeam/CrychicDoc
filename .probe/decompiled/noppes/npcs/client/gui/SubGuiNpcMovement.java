package noppes.npcs.client.gui;

import noppes.npcs.ai.EntityAIAnimation;
import noppes.npcs.entity.data.DataAI;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class SubGuiNpcMovement extends GuiBasic implements ITextfieldListener {

    private DataAI ai;

    public SubGuiNpcMovement(DataAI ai) {
        this.ai = ai;
        this.setBackground("menubg.png");
        this.imageWidth = 256;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.init();
        int y = this.guiTop + 4;
        this.addLabel(new GuiLabel(0, "movement.type", this.guiLeft + 4, y + 5));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 80, y, 100, 20, new String[] { "ai.standing", "ai.wandering", "ai.movingpath" }, this.ai.getMovingType()));
        int var10005 = this.guiLeft + 80;
        y += 22;
        this.addButton(new GuiButtonNop(this, 15, var10005, y, 100, 20, new String[] { "movement.ground", "movement.flying", "movement.swimming" }, this.ai.movementType));
        this.addLabel(new GuiLabel(15, "movement.navigation", this.guiLeft + 4, y + 5));
        if (this.ai.getMovingType() == 1) {
            var10005 = this.guiLeft + 100;
            int var3 = y + 22;
            this.addTextField(new GuiTextFieldNop(4, this, var10005, var3, 40, 20, this.ai.walkingRange + ""));
            this.getTextField(4).numbersOnly = true;
            this.getTextField(4).setMinMaxDefault(0, 1000, 10);
            this.addLabel(new GuiLabel(4, "gui.range", this.guiLeft + 4, var3 + 5));
            var10005 = this.guiLeft + 100;
            int var4 = var3 + 22;
            this.addButton(new GuiButtonNop(this, 5, var10005, var4, 50, 20, new String[] { "gui.no", "gui.yes" }, this.ai.npcInteracting ? 1 : 0));
            this.addLabel(new GuiLabel(5, "movement.wanderinteract", this.guiLeft + 4, var4 + 5));
            var10005 = this.guiLeft + 80;
            y = var4 + 22;
            this.addButton(new GuiButtonNop(this, 9, var10005, y, 80, 20, new String[] { "gui.no", "gui.yes" }, this.ai.movingPause ? 1 : 0));
            this.addLabel(new GuiLabel(9, "movement.pauses", this.guiLeft + 4, y + 5));
        } else if (this.ai.getMovingType() == 0) {
            this.addLabel(new GuiLabel(17, "spawner.posoffset", this.guiLeft + 4, y + 27));
            this.addLabel(new GuiLabel(7, "X:", this.guiLeft + 89, y + 27));
            var10005 = this.guiLeft + 99;
            y += 22;
            this.addTextField(new GuiTextFieldNop(7, this, var10005, y, 24, 20, (int) this.ai.bodyOffsetX + ""));
            this.getTextField(7).numbersOnly = true;
            this.getTextField(7).setMinMaxDefault(0, 10, 5);
            this.addLabel(new GuiLabel(8, "Y:", this.guiLeft + 125, y + 5));
            this.addTextField(new GuiTextFieldNop(8, this, this.guiLeft + 135, y, 24, 20, (int) this.ai.bodyOffsetY + ""));
            this.getTextField(8).numbersOnly = true;
            this.getTextField(8).setMinMaxDefault(0, 10, 5);
            this.addLabel(new GuiLabel(9, "Z:", this.guiLeft + 161, y + 5));
            this.addTextField(new GuiTextFieldNop(9, this, this.guiLeft + 171, y, 24, 20, (int) this.ai.bodyOffsetZ + ""));
            this.getTextField(9).numbersOnly = true;
            this.getTextField(9).setMinMaxDefault(0, 10, 5);
            var10005 = this.guiLeft + 80;
            y += 22;
            this.addButton(new GuiButtonNop(this, 3, var10005, y, 100, 20, new String[] { "stats.normal", "movement.sitting", "movement.lying", "movement.hug", "movement.sneaking", "movement.dancing", "movement.aiming", "movement.crawling" }, this.ai.animationType));
            this.addLabel(new GuiLabel(3, "movement.animation", this.guiLeft + 4, y + 5));
            if (this.ai.animationType != 2) {
                var10005 = this.guiLeft + 80;
                y += 22;
                this.addButton(new GuiButtonNop(this, 4, var10005, y, 80, 20, new String[] { "movement.body", "movement.manual", "movement.stalking", "movement.head" }, this.ai.getStandingType()));
                this.addLabel(new GuiLabel(1, "movement.rotation", this.guiLeft + 4, y + 5));
            } else {
                var10005 = this.guiLeft + 99;
                y += 22;
                this.addTextField(new GuiTextFieldNop(5, this, var10005, y, 40, 20, this.ai.orientation + ""));
                this.getTextField(5).numbersOnly = true;
                this.getTextField(5).setMinMaxDefault(0, 359, 0);
                this.addLabel(new GuiLabel(6, "movement.rotation", this.guiLeft + 4, y + 5));
                this.addLabel(new GuiLabel(5, "(0-359)", this.guiLeft + 142, y + 5));
            }
            if (this.ai.getStandingType() == 1 || this.ai.getStandingType() == 3) {
                this.addTextField(new GuiTextFieldNop(5, this, this.guiLeft + 165, y, 40, 20, this.ai.orientation + ""));
                this.getTextField(5).numbersOnly = true;
                this.getTextField(5).setMinMaxDefault(0, 359, 0);
                this.addLabel(new GuiLabel(5, "(0-359)", this.guiLeft + 207, y + 5));
            }
        }
        if (this.ai.getMovingType() != 0) {
            var10005 = this.guiLeft + 80;
            y += 22;
            this.addButton(new GuiButtonNop(this, 12, var10005, y, 100, 20, new String[] { "stats.normal", "movement.sneaking", "movement.aiming", "movement.dancing", "movement.crawling", "movement.hug" }, EntityAIAnimation.getWalkingAnimationGuiIndex(this.ai.animationType)));
            this.addLabel(new GuiLabel(12, "movement.animation", this.guiLeft + 4, y + 5));
        }
        if (this.ai.getMovingType() == 2) {
            var10005 = this.guiLeft + 80;
            int var7 = y + 22;
            this.addButton(new GuiButtonNop(this, 8, var10005, var7, 80, 20, new String[] { "ai.looping", "ai.backtracking" }, this.ai.movingPattern));
            this.addLabel(new GuiLabel(8, "movement.name", this.guiLeft + 4, var7 + 5));
            var10005 = this.guiLeft + 80;
            y = var7 + 22;
            this.addButton(new GuiButtonNop(this, 9, var10005, y, 80, 20, new String[] { "gui.no", "gui.yes" }, this.ai.movingPause ? 1 : 0));
            this.addLabel(new GuiLabel(9, "movement.pauses", this.guiLeft + 4, y + 5));
        }
        var10005 = this.guiLeft + 100;
        y += 22;
        this.addButton(new GuiButtonNop(this, 13, var10005, y, 50, 20, new String[] { "gui.no", "gui.yes" }, this.ai.stopAndInteract ? 1 : 0));
        this.addLabel(new GuiLabel(13, "movement.stopinteract", this.guiLeft + 4, y + 5));
        var10005 = this.guiLeft + 80;
        y += 22;
        this.addTextField(new GuiTextFieldNop(14, this, var10005, y, 50, 18, this.ai.getWalkingSpeed() + ""));
        this.getTextField(14).numbersOnly = true;
        this.getTextField(14).setMinMaxDefault(0, 10, 4);
        this.addLabel(new GuiLabel(14, "stats.movespeed", this.guiLeft + 5, y + 5));
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 190, this.guiTop + 190, 60, 20, "gui.done"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.ai.setMovingType(guibutton.getValue());
            if (this.ai.getMovingType() != 0) {
                this.ai.animationType = 0;
                this.ai.setStandingType(0);
                this.ai.bodyOffsetX = this.ai.bodyOffsetY = this.ai.bodyOffsetZ = 5.0F;
            }
            this.init();
        } else if (guibutton.id == 3) {
            this.ai.animationType = guibutton.getValue();
            this.init();
        } else if (guibutton.id == 4) {
            this.ai.setStandingType(guibutton.getValue());
            this.init();
        } else if (guibutton.id == 5) {
            this.ai.npcInteracting = guibutton.getValue() == 1;
        } else if (guibutton.id == 8) {
            this.ai.movingPattern = guibutton.getValue();
        } else if (guibutton.id == 9) {
            this.ai.movingPause = guibutton.getValue() == 1;
        } else if (guibutton.id == 12) {
            if (guibutton.getValue() == 0) {
                this.ai.animationType = 0;
            }
            if (guibutton.getValue() == 1) {
                this.ai.animationType = 4;
            }
            if (guibutton.getValue() == 2) {
                this.ai.animationType = 6;
            }
            if (guibutton.getValue() == 3) {
                this.ai.animationType = 5;
            }
            if (guibutton.getValue() == 4) {
                this.ai.animationType = 7;
            }
            if (guibutton.getValue() == 5) {
                this.ai.animationType = 3;
            }
        } else if (guibutton.id == 13) {
            this.ai.stopAndInteract = guibutton.getValue() == 1;
        } else if (guibutton.id == 15) {
            this.ai.movementType = guibutton.getValue();
        } else if (guibutton.id == 66) {
            this.close();
        }
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 7) {
            this.ai.bodyOffsetX = (float) textfield.getInteger();
        } else if (textfield.id == 8) {
            this.ai.bodyOffsetY = (float) textfield.getInteger();
        } else if (textfield.id == 9) {
            this.ai.bodyOffsetZ = (float) textfield.getInteger();
        } else if (textfield.id == 5) {
            this.ai.orientation = textfield.getInteger();
        } else if (textfield.id == 4) {
            this.ai.walkingRange = textfield.getInteger();
        } else if (textfield.id == 14) {
            this.ai.setWalkingSpeed(textfield.getInteger());
        }
    }
}