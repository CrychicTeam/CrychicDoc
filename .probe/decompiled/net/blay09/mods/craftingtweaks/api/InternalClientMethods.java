package net.blay09.mods.craftingtweaks.api;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public interface InternalClientMethods {

    Button createTweakButton(CraftingGrid var1, @Nullable AbstractContainerScreen<?> var2, int var3, int var4, ButtonStyle var5, TweakType var6, TweakType var7);

    <TScreen extends AbstractContainerScreen<TMenu>, TMenu extends AbstractContainerMenu> void registerCraftingGridGuiHandler(Class<TScreen> var1, GridGuiHandler var2);
}