package com.simibubi.create.content.redstone.link.controller;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.ControlsUtil;
import com.simibubi.create.foundation.utility.Lang;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class LinkedControllerScreen extends AbstractSimiContainerScreen<LinkedControllerMenu> {

    protected AllGuiTextures background;

    private List<Rect2i> extraAreas = Collections.emptyList();

    private IconButton resetButton;

    private IconButton confirmButton;

    public LinkedControllerScreen(LinkedControllerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.background = AllGuiTextures.LINKED_CONTROLLER;
    }

    @Override
    protected void init() {
        this.setWindowSize(this.background.width, this.background.height + 4 + AllGuiTextures.PLAYER_INVENTORY.height);
        this.setWindowOffset(1, 0);
        super.init();
        int x = this.f_97735_;
        int y = this.f_97736_;
        this.resetButton = new IconButton(x + this.background.width - 62, y + this.background.height - 24, AllIcons.I_TRASH);
        this.resetButton.withCallback(() -> {
            ((LinkedControllerMenu) this.f_97732_).clearContents();
            ((LinkedControllerMenu) this.f_97732_).sendClearPacket();
        });
        this.confirmButton = new IconButton(x + this.background.width - 33, y + this.background.height - 24, AllIcons.I_CONFIRM);
        this.confirmButton.withCallback(() -> this.f_96541_.player.closeContainer());
        this.m_142416_(this.resetButton);
        this.m_142416_(this.confirmButton);
        this.extraAreas = ImmutableList.of(new Rect2i(x + this.background.width + 4, y + this.background.height - 44, 64, 56));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int invX = this.getLeftOfCentered(AllGuiTextures.PLAYER_INVENTORY.width);
        int invY = this.f_97736_ + this.background.height + 4;
        this.renderPlayerInventory(graphics, invX, invY);
        int x = this.f_97735_;
        int y = this.f_97736_;
        this.background.render(graphics, x, y);
        graphics.drawString(this.f_96547_, this.f_96539_, x + 15, y + 4, 5841956, false);
        GuiGameElement.of(((LinkedControllerMenu) this.f_97732_).contentHolder).<GuiGameElement.GuiRenderBuilder>at((float) (x + this.background.width - 4), (float) (y + this.background.height - 56), -200.0F).scale(5.0).render(graphics);
    }

    @Override
    protected void containerTick() {
        if (!((LinkedControllerMenu) this.f_97732_).player.m_21205_().equals(((LinkedControllerMenu) this.f_97732_).contentHolder, false)) {
            ((LinkedControllerMenu) this.f_97732_).player.closeContainer();
        }
        super.containerTick();
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        if (((LinkedControllerMenu) this.f_97732_).m_142621_().isEmpty() && this.f_97734_ != null && this.f_97734_.container != ((LinkedControllerMenu) this.f_97732_).playerInventory) {
            List<Component> list = new LinkedList();
            if (this.f_97734_.hasItem()) {
                list = this.m_280553_(this.f_97734_.getItem());
            }
            graphics.renderComponentTooltip(this.f_96547_, this.addToTooltip(list, this.f_97734_.getSlotIndex()), x, y);
        } else {
            super.m_280072_(graphics, x, y);
        }
    }

    private List<Component> addToTooltip(List<Component> list, int slot) {
        if (slot >= 0 && slot < 12) {
            list.add(Lang.translateDirect("linked_controller.frequency_slot_" + (slot % 2 + 1), ((KeyMapping) ControlsUtil.getControls().get(slot / 2)).getTranslatedKeyMessage().getString()).withStyle(ChatFormatting.GOLD));
            return list;
        } else {
            return list;
        }
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return this.extraAreas;
    }
}