package net.blay09.mods.craftingtweaks.api;

import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CraftingTweaksClientAPI {

    private static InternalClientMethods internalMethods;

    public static void setupAPI(InternalClientMethods internalMethods) {
        CraftingTweaksClientAPI.internalMethods = internalMethods;
    }

    public static <TMenu extends AbstractContainerMenu, TScreen extends AbstractContainerScreen<TMenu>> void registerCraftingGridGuiHandler(Class<TScreen> clazz, GridGuiHandler handler) {
        internalMethods.registerCraftingGridGuiHandler(clazz, handler);
    }

    @Deprecated
    public static Button createBalanceButton(CraftingGrid grid, int x, int y) {
        return (Button) createTweakButton(grid, x, y, TweakType.Balance);
    }

    @Deprecated
    public static Button createRotateButton(CraftingGrid grid, int x, int y) {
        return (Button) createTweakButton(grid, x, y, TweakType.Rotate);
    }

    @Deprecated
    public static Button createClearButton(CraftingGrid grid, int x, int y) {
        return (Button) createTweakButton(grid, x, y, TweakType.Clear);
    }

    @Deprecated
    public static Button createBalanceButtonRelative(CraftingGrid grid, AbstractContainerScreen<?> screen, int relX, int relY) {
        return (Button) createTweakButtonRelative(grid, screen, relX, relY, TweakType.Balance);
    }

    @Deprecated
    public static Button createRotateButtonRelative(CraftingGrid grid, AbstractContainerScreen<?> screen, int relX, int relY) {
        return (Button) createTweakButtonRelative(grid, screen, relX, relY, TweakType.Rotate);
    }

    @Deprecated
    public static Button createClearButtonRelative(CraftingGrid grid, AbstractContainerScreen<?> screen, int relX, int relY) {
        return (Button) createTweakButtonRelative(grid, screen, relX, relY, TweakType.Clear);
    }

    public static AbstractWidget createTweakButton(CraftingGrid grid, int x, int y, TweakType tweak) {
        return createTweakButton(grid, x, y, tweak, CraftingTweaksButtonStyles.DEFAULT);
    }

    public static AbstractWidget createTweakButton(CraftingGrid grid, int x, int y, TweakType tweak, ButtonStyle style) {
        return switch(tweak) {
            case Clear ->
                internalMethods.createTweakButton(grid, null, x, y, style, TweakType.Clear, TweakType.ForceClear);
            case Rotate ->
                internalMethods.createTweakButton(grid, null, x, y, style, TweakType.Rotate, TweakType.RotateCounterClockwise);
            case Balance ->
                internalMethods.createTweakButton(grid, null, x, y, style, TweakType.Balance, TweakType.Spread);
            case ForceClear ->
                internalMethods.createTweakButton(grid, null, x, y, style, TweakType.ForceClear, TweakType.Clear);
            case RotateCounterClockwise ->
                internalMethods.createTweakButton(grid, null, x, y, style, TweakType.RotateCounterClockwise, TweakType.Rotate);
            case Spread ->
                internalMethods.createTweakButton(grid, null, x, y, style, TweakType.Spread, TweakType.Balance);
        };
    }

    public static AbstractWidget createTweakButtonRelative(CraftingGrid grid, AbstractContainerScreen<?> screen, int x, int y, TweakType tweak) {
        return createTweakButtonRelative(grid, screen, x, y, tweak, CraftingTweaksButtonStyles.DEFAULT);
    }

    public static AbstractWidget createTweakButtonRelative(CraftingGrid grid, AbstractContainerScreen<?> screen, int x, int y, TweakType tweak, ButtonStyle style) {
        int guiLeft = ((AbstractContainerScreenAccessor) screen).getLeftPos();
        int guiTop = ((AbstractContainerScreenAccessor) screen).getTopPos();
        return switch(tweak) {
            case Clear ->
                internalMethods.createTweakButton(grid, screen, x + guiLeft, y + guiTop, style, TweakType.Clear, TweakType.ForceClear);
            case Rotate ->
                internalMethods.createTweakButton(grid, screen, x + guiLeft, y + guiTop, style, TweakType.Rotate, TweakType.RotateCounterClockwise);
            case Balance ->
                internalMethods.createTweakButton(grid, screen, x + guiLeft, y + guiTop, style, TweakType.Balance, TweakType.Spread);
            case ForceClear ->
                internalMethods.createTweakButton(grid, screen, x + guiLeft, y + guiTop, style, TweakType.ForceClear, TweakType.Clear);
            case RotateCounterClockwise ->
                internalMethods.createTweakButton(grid, screen, x + guiLeft, y + guiTop, style, TweakType.RotateCounterClockwise, TweakType.Rotate);
            case Spread ->
                internalMethods.createTweakButton(grid, screen, x + guiLeft, y + guiTop, style, TweakType.Spread, TweakType.Balance);
        };
    }
}