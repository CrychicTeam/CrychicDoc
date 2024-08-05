package com.simibubi.create.compat.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import java.util.function.Supplier;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class DoubleItemIcon implements IDrawable {

    private Supplier<ItemStack> primarySupplier;

    private Supplier<ItemStack> secondarySupplier;

    private ItemStack primaryStack;

    private ItemStack secondaryStack;

    public DoubleItemIcon(Supplier<ItemStack> primary, Supplier<ItemStack> secondary) {
        this.primarySupplier = primary;
        this.secondarySupplier = secondary;
    }

    @Override
    public int getWidth() {
        return 18;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        if (this.primaryStack == null) {
            this.primaryStack = (ItemStack) this.primarySupplier.get();
            this.secondaryStack = (ItemStack) this.secondarySupplier.get();
        }
        RenderSystem.enableDepthTest();
        matrixStack.pushPose();
        matrixStack.translate((float) xOffset, (float) yOffset, 0.0F);
        matrixStack.pushPose();
        matrixStack.translate(1.0F, 1.0F, 0.0F);
        GuiGameElement.of(this.primaryStack).render(graphics);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate(10.0F, 10.0F, 100.0F);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        GuiGameElement.of(this.secondaryStack).render(graphics);
        matrixStack.popPose();
        matrixStack.popPose();
    }
}