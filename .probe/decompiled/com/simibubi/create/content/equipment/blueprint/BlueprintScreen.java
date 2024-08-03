package com.simibubi.create.content.equipment.blueprint;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.logistics.filter.FilterScreenPacket;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Lang;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BlueprintScreen extends AbstractSimiContainerScreen<BlueprintMenu> {

    protected AllGuiTextures background;

    private List<Rect2i> extraAreas = Collections.emptyList();

    private IconButton resetButton;

    private IconButton confirmButton;

    public BlueprintScreen(BlueprintMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.background = AllGuiTextures.BLUEPRINT;
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
            ((BlueprintMenu) this.f_97732_).clearContents();
            this.contentsCleared();
            ((BlueprintMenu) this.f_97732_).sendClearPacket();
        });
        this.confirmButton = new IconButton(x + this.background.width - 33, y + this.background.height - 24, AllIcons.I_CONFIRM);
        this.confirmButton.withCallback(() -> this.f_96541_.player.closeContainer());
        this.m_142416_(this.resetButton);
        this.m_142416_(this.confirmButton);
        this.extraAreas = ImmutableList.of(new Rect2i(x + this.background.width, y + this.background.height - 36, 56, 44));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int invX = this.getLeftOfCentered(AllGuiTextures.PLAYER_INVENTORY.width);
        int invY = this.f_97736_ + this.background.height + 4;
        this.renderPlayerInventory(graphics, invX, invY);
        int x = this.f_97735_;
        int y = this.f_97736_;
        this.background.render(graphics, x, y);
        graphics.drawString(this.f_96547_, this.f_96539_, x + 15, y + 4, 16777215, false);
        GuiGameElement.of(AllPartialModels.CRAFTING_BLUEPRINT_1x1).<GuiGameElement.GuiRenderBuilder>at((float) (x + this.background.width + 20), (float) (y + this.background.height - 32), 0.0F).rotate(45.0, -45.0, 22.5).scale(40.0).render(graphics);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        if (((BlueprintMenu) this.f_97732_).m_142621_().isEmpty() && this.f_97734_ != null && this.f_97734_.container != ((BlueprintMenu) this.f_97732_).playerInventory) {
            List<Component> list = new LinkedList();
            if (this.f_97734_.hasItem()) {
                list = this.m_280553_(this.f_97734_.getItem());
            }
            graphics.renderComponentTooltip(this.f_96547_, this.addToTooltip(list, this.f_97734_.getSlotIndex(), true), x, y);
        } else {
            super.m_280072_(graphics, x, y);
        }
    }

    private List<Component> addToTooltip(List<Component> list, int slot, boolean isEmptySlot) {
        if (slot >= 0 && slot <= 10) {
            if (slot < 9) {
                list.add(Lang.translateDirect("crafting_blueprint.crafting_slot").withStyle(ChatFormatting.GOLD));
                if (isEmptySlot) {
                    list.add(Lang.translateDirect("crafting_blueprint.filter_items_viable").withStyle(ChatFormatting.GRAY));
                }
            } else if (slot == 9) {
                list.add(Lang.translateDirect("crafting_blueprint.display_slot").withStyle(ChatFormatting.GOLD));
                if (!isEmptySlot) {
                    list.add(Lang.translateDirect("crafting_blueprint." + (((BlueprintMenu) this.f_97732_).contentHolder.inferredIcon ? "inferred" : "manually_assigned")).withStyle(ChatFormatting.GRAY));
                }
            } else if (slot == 10) {
                list.add(Lang.translateDirect("crafting_blueprint.secondary_display_slot").withStyle(ChatFormatting.GOLD));
                if (isEmptySlot) {
                    list.add(Lang.translateDirect("crafting_blueprint.optional").withStyle(ChatFormatting.GRAY));
                }
            }
            return list;
        } else {
            return list;
        }
    }

    @Override
    protected void containerTick() {
        if (!((BlueprintMenu) this.f_97732_).contentHolder.isEntityAlive()) {
            ((BlueprintMenu) this.f_97732_).player.closeContainer();
        }
        super.containerTick();
    }

    protected void contentsCleared() {
    }

    protected void sendOptionUpdate(FilterScreenPacket.Option option) {
        AllPackets.getChannel().sendToServer(new FilterScreenPacket(option));
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return this.extraAreas;
    }
}