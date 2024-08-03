package com.simibubi.create.content.contraptions.elevator;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.decoration.slidingDoor.DoorControl;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.TooltipArea;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

public class ElevatorContactScreen extends AbstractSimiScreen {

    private AllGuiTextures background;

    private EditBox shortNameInput;

    private EditBox longNameInput;

    private IconButton confirm;

    private String shortName;

    private String longName;

    private DoorControl doorControl;

    private BlockPos pos;

    public ElevatorContactScreen(BlockPos pos, String prevShortName, String prevLongName, DoorControl prevDoorControl) {
        super(Lang.translateDirect("elevator_contact.title"));
        this.pos = pos;
        this.doorControl = prevDoorControl;
        this.background = AllGuiTextures.ELEVATOR_CONTACT;
        this.shortName = prevShortName;
        this.longName = prevLongName;
    }

    @Override
    public void init() {
        this.setWindowSize(this.background.width + 30, this.background.height);
        super.init();
        int x = this.guiLeft;
        int y = this.guiTop;
        this.confirm = new IconButton(x + 200, y + 58, AllIcons.I_CONFIRM);
        this.confirm.withCallback(this::confirm);
        this.m_142416_(this.confirm);
        this.shortNameInput = this.editBox(33, 30, 4);
        this.shortNameInput.setValue(this.shortName);
        this.centerInput(x);
        this.shortNameInput.setResponder(s -> {
            this.shortName = s;
            this.centerInput(x);
        });
        this.shortNameInput.setFocused(true);
        this.m_7522_(this.shortNameInput);
        this.shortNameInput.setHighlightPos(0);
        this.longNameInput = this.editBox(63, 140, 30);
        this.longNameInput.setValue(this.longName);
        this.longNameInput.setResponder(s -> this.longName = s);
        MutableComponent rmbToEdit = Lang.translate("gui.schedule.lmb_edit").style(ChatFormatting.DARK_GRAY).style(ChatFormatting.ITALIC).component();
        this.m_169394_(new TooltipArea(x + 21, y + 23, 30, 18).withTooltip(ImmutableList.of(Lang.translate("elevator_contact.floor_identifier").color(5476833).component(), rmbToEdit)));
        this.m_169394_(new TooltipArea(x + 57, y + 23, 147, 18).withTooltip(ImmutableList.of(Lang.translate("elevator_contact.floor_description").color(5476833).component(), Lang.translate("crafting_blueprint.optional").style(ChatFormatting.GRAY).component(), rmbToEdit)));
        Pair<ScrollInput, Label> doorControlWidgets = DoorControl.createWidget(x + 58, y + 57, mode -> this.doorControl = mode, this.doorControl);
        this.m_142416_(doorControlWidgets.getFirst());
        this.m_142416_(doorControlWidgets.getSecond());
    }

    private int centerInput(int x) {
        int centeredX = x + (this.shortName.isEmpty() ? 34 : 36 - this.f_96547_.width(this.shortName) / 2);
        this.shortNameInput.m_252865_(centeredX);
        return centeredX;
    }

    private EditBox editBox(int x, int width, int chars) {
        EditBox editBox = new EditBox(this.f_96547_, this.guiLeft + x, this.guiTop + 30, width, 10, Components.immutableEmpty());
        editBox.setTextColor(-1);
        editBox.setTextColorUneditable(-1);
        editBox.setBordered(false);
        editBox.setMaxLength(chars);
        editBox.setFocused(false);
        editBox.m_6375_(0.0, 0.0, 0);
        this.m_142416_(editBox);
        return editBox;
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = this.guiLeft;
        int y = this.guiTop;
        this.background.render(graphics, x, y);
        FormattedCharSequence formattedcharsequence = this.f_96539_.getVisualOrderText();
        graphics.drawString(this.f_96547_, formattedcharsequence, (float) (x + (this.background.width - 8) / 2 - this.f_96547_.width(formattedcharsequence) / 2), (float) y + 6.0F, 3094328, false);
        GuiGameElement.of(AllBlocks.ELEVATOR_CONTACT.asStack()).<GuiGameElement.GuiRenderBuilder>at((float) (x + this.background.width + 6), (float) (y + this.background.height - 56), -200.0F).scale(5.0).render(graphics);
        graphics.renderItem(AllBlocks.TRAIN_DOOR.asStack(), x + 37, y + 58);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        boolean consumed = super.mouseClicked(pMouseX, pMouseY, pButton);
        if (!this.shortNameInput.m_93696_()) {
            int length = this.shortNameInput.getValue().length();
            this.shortNameInput.setHighlightPos(length);
            this.shortNameInput.setCursorPosition(length);
        }
        if (this.shortNameInput.m_198029_()) {
            this.longNameInput.m_6375_(0.0, 0.0, 0);
        }
        if (!consumed && pMouseX > (double) (this.guiLeft + 22) && pMouseY > (double) (this.guiTop + 24) && pMouseX < (double) (this.guiLeft + 50) && pMouseY < (double) (this.guiTop + 40)) {
            this.m_7522_(this.shortNameInput);
            this.shortNameInput.setFocused(true);
            return true;
        } else {
            return consumed;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (super.keyPressed(keyCode, p_keyPressed_2_, p_keyPressed_3_)) {
            return true;
        } else if (keyCode == 257) {
            this.confirm();
            return true;
        } else if (keyCode == 256 && this.m_6913_()) {
            this.m_7379_();
            return true;
        } else {
            return false;
        }
    }

    private void confirm() {
        AllPackets.getChannel().sendToServer(new ElevatorContactEditPacket(this.pos, this.shortName, this.longName, this.doorControl));
        this.m_7379_();
    }
}