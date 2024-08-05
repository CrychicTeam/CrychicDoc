package com.mna.gui.widgets.lodestar;

import com.mna.ManaAndArtifice;
import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskAreaParameter;
import com.mna.api.items.IPositionalItem;
import com.mna.items.ItemInit;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class AreaParameterInput extends LodestarParameter<Pair<DirectionalPoint, DirectionalPoint>> {

    public static final int V = 22;

    private ItemStack markRuneA = new ItemStack(ItemInit.RUNE_MARKING.get());

    private ItemStack markRuneB = new ItemStack(ItemInit.RUNE_MARKING.get());

    public AreaParameterInput(boolean lowTier, int x, int y, Button.OnPress pressHandler, Component tooltip) {
        super(lowTier, x, y, 22, null, pressHandler, tooltip);
    }

    public AreaParameterInput(boolean lowTier, int x, int y, Pair<DirectionalPoint, DirectionalPoint> defaultValue, Button.OnPress pressHandler, Component tooltip) {
        super(lowTier, x, y, 22, defaultValue, pressHandler, tooltip);
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (this.markRuneA.hasTag()) {
            pGuiGraphics.renderItem(this.markRuneA, this.m_252754_() + 19, this.m_252907_() + 3);
        }
        if (this.markRuneB.hasTag()) {
            pGuiGraphics.renderItem(this.markRuneB, this.m_252754_() + 37, this.m_252907_() + 3);
        }
    }

    @Override
    public void saveTo(ConstructAITaskParameter param) {
        if (param instanceof ConstructTaskAreaParameter && this.value != null) {
            ((ConstructTaskAreaParameter) param).setPoints((DirectionalPoint) this.value.getFirst(), (DirectionalPoint) this.value.getSecond());
        }
    }

    @Override
    public void loadFrom(ConstructAITaskParameter param) {
        if (param instanceof ConstructTaskAreaParameter) {
            this.value = ((ConstructTaskAreaParameter) param).getPoints();
            if (this.value != null) {
                if (this.value.getFirst() != null) {
                    ItemInit.RUNE_MARKING.get().setLocation(this.markRuneA, (DirectionalPoint) this.value.getFirst());
                } else {
                    this.markRuneA.setTag(null);
                }
                if (this.value.getSecond() != null) {
                    ItemInit.RUNE_MARKING.get().setLocation(this.markRuneB, (DirectionalPoint) this.value.getSecond());
                } else {
                    this.markRuneB.setTag(null);
                }
            }
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        ItemStack carried = this.getCursorHeldItem();
        ItemStack slotItem = this.getSlotItemFromClick(mouseX, mouseY);
        if (!slotItem.isEmpty()) {
            if (!carried.isEmpty() && carried.getItem() instanceof IPositionalItem) {
                ItemInit.RUNE_MARKING.get().copyPositionFrom(carried, slotItem);
            } else {
                slotItem.setTag(null);
            }
            if (this.markRuneA.hasTag() && this.markRuneB.hasTag()) {
                this.setValue(new Pair(ItemInit.RUNE_MARKING.get().getDirectionalPoint(this.markRuneA), ItemInit.RUNE_MARKING.get().getDirectionalPoint(this.markRuneB)));
            } else {
                DirectionalPoint a = null;
                DirectionalPoint b = null;
                if (this.markRuneA.hasTag()) {
                    a = ItemInit.RUNE_MARKING.get().getDirectionalPoint(this.markRuneA);
                }
                if (this.markRuneB.hasTag()) {
                    b = ItemInit.RUNE_MARKING.get().getDirectionalPoint(this.markRuneB);
                }
                this.setValue(new Pair(a, b));
            }
            super.m_5716_(mouseX, mouseY);
        }
    }

    private ItemStack getSlotItemFromClick(double mouseX, double mouseY) {
        int padding = 16;
        if (mouseX < (double) (this.m_252754_() + padding)) {
            return ItemStack.EMPTY;
        } else {
            return mouseX < (double) (this.m_252754_() + padding + (this.f_93618_ - padding) / 2) ? this.markRuneA : this.markRuneB;
        }
    }

    @Override
    public List<Component> getTooltipItems() {
        List<Component> baseTT = super.getTooltipItems();
        baseTT.add(Component.literal(" "));
        ItemInit.RUNE_MARKING.get().appendHoverText(this.markRuneA, ManaAndArtifice.instance.proxy.getClientWorld(), baseTT, TooltipFlag.Default.f_256752_);
        baseTT.add(Component.literal("-------"));
        ItemInit.RUNE_MARKING.get().appendHoverText(this.markRuneB, ManaAndArtifice.instance.proxy.getClientWorld(), baseTT, TooltipFlag.Default.f_256752_);
        return baseTT;
    }
}