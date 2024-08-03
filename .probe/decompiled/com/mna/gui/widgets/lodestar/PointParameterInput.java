package com.mna.gui.widgets.lodestar;

import com.mna.ManaAndArtifice;
import com.mna.api.blocks.DirectionalPoint;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskPointParameter;
import com.mna.api.items.IPositionalItem;
import com.mna.items.ItemInit;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class PointParameterInput extends LodestarParameter<DirectionalPoint> {

    public static final int V = 0;

    private ItemStack markRune = new ItemStack(ItemInit.RUNE_MARKING.get());

    public PointParameterInput(boolean lowTier, int x, int y, Button.OnPress pressHandler, Component tooltip) {
        super(lowTier, x, y, 0, null, pressHandler, tooltip);
    }

    public PointParameterInput(boolean lowTier, int x, int y, DirectionalPoint defaultValue, Button.OnPress pressHandler, Component tooltip) {
        super(lowTier, x, y, 0, defaultValue, pressHandler, tooltip);
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (this.value != null && this.markRune.hasTag()) {
            pGuiGraphics.renderItem(this.markRune, this.m_252754_() + 19, this.m_252907_() + 3);
        }
    }

    @Override
    public void saveTo(ConstructAITaskParameter param) {
        if (param instanceof ConstructTaskPointParameter && this.value != null) {
            ((ConstructTaskPointParameter) param).setPoint(this.value);
        }
    }

    @Override
    public void loadFrom(ConstructAITaskParameter param) {
        if (param instanceof ConstructTaskPointParameter) {
            this.value = new DirectionalPoint(((ConstructTaskPointParameter) param).getPosition(), ((ConstructTaskPointParameter) param).getDirection(), ((ConstructTaskPointParameter) param).getOriginalBlockName());
            if (this.value.getPosition() != null) {
                ItemInit.RUNE_MARKING.get().setLocation(this.markRune, this.value);
            }
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        ItemStack carried = this.getCursorHeldItem();
        if (!carried.isEmpty() && carried.getItem() instanceof IPositionalItem) {
            IPositionalItem<?> pos = (IPositionalItem<?>) carried.getItem();
            ItemInit.RUNE_MARKING.get().copyPositionFrom(carried, this.markRune);
            this.setValue(pos.getDirectionalPoint(carried));
        } else {
            this.markRune.setTag(null);
            this.setValue(null);
        }
        super.m_5716_(mouseX, mouseY);
    }

    @Override
    public List<Component> getTooltipItems() {
        List<Component> baseTT = super.getTooltipItems();
        baseTT.add(Component.literal(" "));
        ItemInit.RUNE_MARKING.get().appendHoverText(this.markRune, ManaAndArtifice.instance.proxy.getClientWorld(), baseTT, TooltipFlag.Default.f_256752_);
        return baseTT;
    }
}