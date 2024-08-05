package net.minecraftforge.client.extensions.common;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public interface IClientMobEffectExtensions {

    IClientMobEffectExtensions DEFAULT = new IClientMobEffectExtensions() {
    };

    static IClientMobEffectExtensions of(MobEffectInstance instance) {
        return of(instance.getEffect());
    }

    static IClientMobEffectExtensions of(MobEffect effect) {
        return effect.getEffectRendererInternal() instanceof IClientMobEffectExtensions r ? r : DEFAULT;
    }

    default boolean isVisibleInInventory(MobEffectInstance instance) {
        return true;
    }

    default boolean isVisibleInGui(MobEffectInstance instance) {
        return true;
    }

    default boolean renderInventoryIcon(MobEffectInstance instance, EffectRenderingInventoryScreen<?> screen, GuiGraphics guiGraphics, int x, int y, int blitOffset) {
        return false;
    }

    default boolean renderInventoryText(MobEffectInstance instance, EffectRenderingInventoryScreen<?> screen, GuiGraphics guiGraphics, int x, int y, int blitOffset) {
        return false;
    }

    default boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphics guiGraphics, int x, int y, float z, float alpha) {
        return false;
    }
}