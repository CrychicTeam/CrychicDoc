package com.simibubi.create.compat.jei;

import com.simibubi.create.AllPackets;
import com.simibubi.create.content.logistics.filter.AttributeFilterScreen;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import com.simibubi.create.foundation.gui.menu.GhostItemSubmitPacket;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GhostIngredientHandler<T extends GhostItemMenu<?>> implements IGhostIngredientHandler<AbstractSimiContainerScreen<T>> {

    public <I> List<IGhostIngredientHandler.Target<I>> getTargetsTyped(AbstractSimiContainerScreen<T> gui, ITypedIngredient<I> ingredient, boolean doStart) {
        boolean isAttributeFilter = gui instanceof AttributeFilterScreen;
        List<IGhostIngredientHandler.Target<I>> targets = new LinkedList();
        if (ingredient.getType() == VanillaTypes.ITEM_STACK) {
            for (int i = 36; i < ((GhostItemMenu) gui.m_6262_()).f_38839_.size(); i++) {
                if (((Slot) ((GhostItemMenu) gui.m_6262_()).f_38839_.get(i)).isActive()) {
                    targets.add(new GhostIngredientHandler.GhostTarget<>(gui, i - 36, isAttributeFilter));
                }
                if (isAttributeFilter) {
                    break;
                }
            }
        }
        return targets;
    }

    @Override
    public void onComplete() {
    }

    @Override
    public boolean shouldHighlightTargets() {
        return true;
    }

    private static class GhostTarget<I, T extends GhostItemMenu<?>> implements IGhostIngredientHandler.Target<I> {

        private final Rect2i area;

        private final AbstractSimiContainerScreen<T> gui;

        private final int slotIndex;

        private final boolean isAttributeFilter;

        public GhostTarget(AbstractSimiContainerScreen<T> gui, int slotIndex, boolean isAttributeFilter) {
            this.gui = gui;
            this.slotIndex = slotIndex;
            this.isAttributeFilter = isAttributeFilter;
            Slot slot = (Slot) ((GhostItemMenu) gui.m_6262_()).f_38839_.get(slotIndex + 36);
            this.area = new Rect2i(gui.getGuiLeft() + slot.x, gui.getGuiTop() + slot.y, 16, 16);
        }

        @Override
        public Rect2i getArea() {
            return this.area;
        }

        @Override
        public void accept(I ingredient) {
            ItemStack stack = ((ItemStack) ingredient).copy();
            stack.setCount(1);
            ((GhostItemMenu) this.gui.m_6262_()).ghostInventory.setStackInSlot(this.slotIndex, stack);
            if (!this.isAttributeFilter) {
                AllPackets.getChannel().sendToServer(new GhostItemSubmitPacket(stack, this.slotIndex));
            }
        }
    }
}