package net.blay09.mods.craftingtweaks.api.impl;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.craftingtweaks.CraftingTweaks;
import net.blay09.mods.craftingtweaks.api.ButtonStyle;
import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.api.GridGuiHandler;
import net.blay09.mods.craftingtweaks.api.InternalClientMethods;
import net.blay09.mods.craftingtweaks.api.TweakType;
import net.blay09.mods.craftingtweaks.client.ClientProvider;
import net.blay09.mods.craftingtweaks.client.CraftingTweaksClient;
import net.blay09.mods.craftingtweaks.client.CraftingTweaksClientProviderManager;
import net.blay09.mods.craftingtweaks.client.GuiTweakButton;
import net.blay09.mods.craftingtweaks.network.BalanceMessage;
import net.blay09.mods.craftingtweaks.network.ClearMessage;
import net.blay09.mods.craftingtweaks.network.RotateMessage;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class InternalClientMethodsImpl implements InternalClientMethods {

    @Override
    public Button createTweakButton(CraftingGrid grid, @Nullable AbstractContainerScreen<?> screen, int x, int y, ButtonStyle style, TweakType tweak, TweakType altTweak) {
        return new GuiTweakButton(screen, x, y, style, grid, tweak, altTweak) {

            @Override
            protected void onTweakButtonClicked(Player player, AbstractContainerMenu container, CraftingGrid grid, TweakType tweak) {
                InternalClientMethodsImpl.executeTweak(player, container, grid, tweak);
            }
        };
    }

    @Override
    public <TScreen extends AbstractContainerScreen<TMenu>, TMenu extends AbstractContainerMenu> void registerCraftingGridGuiHandler(Class<TScreen> clazz, GridGuiHandler handler) {
        CraftingTweaksClientProviderManager.registerCraftingGridGuiHandler(clazz, handler);
    }

    private static void executeTweak(Player player, AbstractContainerMenu container, CraftingGrid grid, TweakType tweak) {
        switch(tweak) {
            case Balance:
            case Spread:
                if (CraftingTweaks.isServerSideInstalled) {
                    Balm.getNetworking().sendToServer(new BalanceMessage(grid.getId(), tweak == TweakType.Spread));
                } else {
                    ClientProvider clientProvider = CraftingTweaksClient.getClientProvider();
                    if (tweak == TweakType.Spread) {
                        clientProvider.spreadGrid(player, container, grid);
                    } else {
                        clientProvider.balanceGrid(player, container, grid);
                    }
                }
                break;
            case Rotate:
            case RotateCounterClockwise:
                if (CraftingTweaks.isServerSideInstalled) {
                    Balm.getNetworking().sendToServer(new RotateMessage(grid.getId(), tweak == TweakType.RotateCounterClockwise));
                } else {
                    ClientProvider clientProvider = CraftingTweaksClient.getClientProvider();
                    clientProvider.rotateGrid(player, container, grid, tweak == TweakType.RotateCounterClockwise);
                }
                break;
            case ForceClear:
            case Clear:
                if (CraftingTweaks.isServerSideInstalled) {
                    Balm.getNetworking().sendToServer(new ClearMessage(grid.getId(), tweak == TweakType.ForceClear));
                } else {
                    ClientProvider clientProvider = CraftingTweaksClient.getClientProvider();
                    clientProvider.clearGrid(player, container, grid, tweak == TweakType.ForceClear);
                }
        }
    }
}