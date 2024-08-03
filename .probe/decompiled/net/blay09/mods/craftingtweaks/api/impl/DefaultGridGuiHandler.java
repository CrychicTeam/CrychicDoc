package net.blay09.mods.craftingtweaks.api.impl;

import java.util.function.Consumer;
import net.blay09.mods.craftingtweaks.api.ButtonAlignment;
import net.blay09.mods.craftingtweaks.api.ButtonPosition;
import net.blay09.mods.craftingtweaks.api.ButtonStyle;
import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksClientAPI;
import net.blay09.mods.craftingtweaks.api.GridGuiHandler;
import net.blay09.mods.craftingtweaks.api.GridGuiSettings;
import net.blay09.mods.craftingtweaks.api.TweakType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public class DefaultGridGuiHandler implements GridGuiHandler {

    @Override
    public void createButtons(AbstractContainerScreen<?> screen, CraftingGrid grid, Consumer<AbstractWidget> addWidgetFunc) {
        if (!(grid instanceof GridGuiSettings guiSettings)) {
            guiSettings = new DefaultGridGuiSettings();
        }
        int index = 0;
        if (this.createTweakButton(screen, grid, addWidgetFunc, guiSettings, index, TweakType.Rotate)) {
            index++;
        }
        if (this.createTweakButton(screen, grid, addWidgetFunc, guiSettings, index, TweakType.Balance)) {
            index++;
        }
        this.createTweakButton(screen, grid, addWidgetFunc, guiSettings, index, TweakType.Clear);
    }

    private boolean createTweakButton(AbstractContainerScreen<?> screen, CraftingGrid grid, Consumer<AbstractWidget> addWidgetFunc, GridGuiSettings guiSettings, int index, TweakType tweak) {
        if (guiSettings.isButtonVisible(tweak)) {
            ButtonPosition buttonPos = (ButtonPosition) guiSettings.getButtonPosition(tweak).orElseGet(() -> this.getAlignedPosition(screen.getMenu(), grid, guiSettings, index));
            addWidgetFunc.accept(CraftingTweaksClientAPI.createTweakButtonRelative(grid, screen, buttonPos.getX(), buttonPos.getY(), tweak, guiSettings.getButtonStyle()));
            return true;
        } else {
            return false;
        }
    }

    private ButtonPosition getAlignedPosition(AbstractContainerMenu menu, CraftingGrid grid, GridGuiSettings guiSettings, int index) {
        ButtonAlignment alignment = guiSettings.getButtonAlignment();
        ButtonStyle style = guiSettings.getButtonStyle();
        int offsetX = guiSettings.getButtonAlignmentOffsetX();
        int offsetY = guiSettings.getButtonAlignmentOffsetY();
        Player player = Minecraft.getInstance().player;
        Slot firstSlot = menu.slots.get(grid.getGridStartSlot(player, menu));
        int gridLength = (int) Math.sqrt((double) grid.getGridSize(player, menu));
        return switch(alignment) {
            case TOP ->
                new ButtonPosition(offsetX + firstSlot.x + style.getSpacingX() * index, offsetY + firstSlot.y - style.getSpacingY() - style.getMarginY());
            case BOTTOM ->
                new ButtonPosition(offsetX + firstSlot.x + style.getSpacingX() * index, offsetY + firstSlot.y + 18 * gridLength + style.getMarginY());
            case RIGHT ->
                new ButtonPosition(offsetX + firstSlot.x + 18 * gridLength + style.getMarginX(), offsetY + firstSlot.y + style.getSpacingY() * index);
            case LEFT ->
                new ButtonPosition(offsetX + firstSlot.x - style.getSpacingX() - style.getMarginX(), offsetY + firstSlot.y + style.getSpacingY() * index);
        };
    }
}