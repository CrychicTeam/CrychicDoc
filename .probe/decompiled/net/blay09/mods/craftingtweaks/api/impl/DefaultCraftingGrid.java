package net.blay09.mods.craftingtweaks.api.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.blay09.mods.craftingtweaks.api.ButtonAlignment;
import net.blay09.mods.craftingtweaks.api.ButtonPosition;
import net.blay09.mods.craftingtweaks.api.ButtonStyle;
import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.api.CraftingGridDecorator;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksButtonStyles;
import net.blay09.mods.craftingtweaks.api.GridBalanceHandler;
import net.blay09.mods.craftingtweaks.api.GridClearHandler;
import net.blay09.mods.craftingtweaks.api.GridGuiSettings;
import net.blay09.mods.craftingtweaks.api.GridRotateHandler;
import net.blay09.mods.craftingtweaks.api.GridTransferHandler;
import net.blay09.mods.craftingtweaks.api.NoopHandler;
import net.blay09.mods.craftingtweaks.api.TweakType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class DefaultCraftingGrid implements CraftingGrid, CraftingGridDecorator, GridGuiSettings {

    private final ResourceLocation id;

    private final int start;

    private final int size;

    private final Set<TweakType> disabledTweaks = new HashSet();

    private final Set<TweakType> hiddenButtons = new HashSet();

    private final Map<TweakType, ButtonPosition> buttonPositions = new HashMap();

    private GridClearHandler<AbstractContainerMenu> clearHandler = CraftingGrid.super.clearHandler();

    private GridBalanceHandler<AbstractContainerMenu> balanceHandler = CraftingGrid.super.balanceHandler();

    private GridRotateHandler<AbstractContainerMenu> rotateHandler = CraftingGrid.super.rotateHandler();

    private GridTransferHandler<AbstractContainerMenu> transferHandler = CraftingGrid.super.transferHandler();

    private ButtonStyle buttonStyle = CraftingTweaksButtonStyles.DEFAULT;

    private ButtonAlignment buttonAlignment = ButtonAlignment.LEFT;

    private int buttonAlignmentOffsetX;

    private int buttonAlignmentOffsetY;

    public DefaultCraftingGrid(ResourceLocation id, int start, int size) {
        this.id = id;
        this.start = start;
        this.size = size;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public int getGridStartSlot(Player player, AbstractContainerMenu menu) {
        return this.start;
    }

    @Override
    public int getGridSize(Player player, AbstractContainerMenu menu) {
        return this.size;
    }

    @Override
    public CraftingGridDecorator disableTweak(TweakType tweak) {
        this.disabledTweaks.add(tweak);
        switch(tweak) {
            case Clear:
                this.clearHandler = new NoopHandler();
                break;
            case Balance:
                this.balanceHandler = new NoopHandler();
                break;
            case Rotate:
                this.rotateHandler = new NoopHandler();
        }
        return this;
    }

    @Override
    public CraftingGridDecorator disableAllTweaks() {
        this.disableTweak(TweakType.Balance);
        this.disableTweak(TweakType.Rotate);
        this.disableTweak(TweakType.Clear);
        return this;
    }

    @Override
    public CraftingGridDecorator usePhantomItems() {
        if (this.clearHandler instanceof DefaultGridClearHandler defaultClearHandler) {
            defaultClearHandler.setPhantomItems(true);
        }
        return this;
    }

    @Override
    public GridClearHandler<AbstractContainerMenu> clearHandler() {
        return this.clearHandler;
    }

    @Override
    public GridBalanceHandler<AbstractContainerMenu> balanceHandler() {
        return this.balanceHandler;
    }

    @Override
    public GridTransferHandler<AbstractContainerMenu> transferHandler() {
        return this.transferHandler;
    }

    @Override
    public GridRotateHandler<AbstractContainerMenu> rotateHandler() {
        return this.rotateHandler;
    }

    @Override
    public CraftingGridDecorator rotateHandler(GridRotateHandler<AbstractContainerMenu> rotateHandler) {
        this.rotateHandler = rotateHandler;
        return this;
    }

    @Override
    public CraftingGridDecorator balanceHandler(GridBalanceHandler<AbstractContainerMenu> balanceHandler) {
        this.balanceHandler = balanceHandler;
        return this;
    }

    @Override
    public CraftingGridDecorator clearHandler(GridClearHandler<AbstractContainerMenu> clearHandler) {
        this.clearHandler = clearHandler;
        return this;
    }

    @Override
    public CraftingGridDecorator transferHandler(GridTransferHandler<AbstractContainerMenu> transferHandler) {
        this.transferHandler = transferHandler;
        return this;
    }

    @Override
    public CraftingGridDecorator hideTweakButton(TweakType tweak) {
        this.hiddenButtons.add(tweak);
        return this;
    }

    @Override
    public CraftingGridDecorator hideAllTweakButtons() {
        this.hideTweakButton(TweakType.Clear);
        this.hideTweakButton(TweakType.Balance);
        this.hideTweakButton(TweakType.Rotate);
        return this;
    }

    @Override
    public CraftingGridDecorator setButtonAlignment(ButtonAlignment alignment) {
        this.buttonAlignment = alignment;
        return this;
    }

    @Override
    public CraftingGridDecorator setButtonAlignmentOffset(int offsetX, int offsetY) {
        this.buttonAlignmentOffsetX = offsetX;
        this.buttonAlignmentOffsetY = offsetY;
        return this;
    }

    @Override
    public CraftingGridDecorator setButtonStyle(ButtonStyle style) {
        this.buttonStyle = style;
        return this;
    }

    @Override
    public CraftingGridDecorator setButtonPosition(TweakType tweak, int x, int y) {
        this.buttonPositions.put(tweak, new ButtonPosition(x, y));
        return this;
    }

    @Override
    public boolean isTweakActive(TweakType tweak) {
        return !this.disabledTweaks.contains(tweak);
    }

    @Override
    public boolean isButtonVisible(TweakType tweak) {
        return !this.hiddenButtons.contains(tweak);
    }

    @Override
    public ButtonAlignment getButtonAlignment() {
        return this.buttonAlignment;
    }

    @Override
    public int getButtonAlignmentOffsetX() {
        return this.buttonAlignmentOffsetX;
    }

    @Override
    public int getButtonAlignmentOffsetY() {
        return this.buttonAlignmentOffsetY;
    }

    @Override
    public ButtonStyle getButtonStyle() {
        return this.buttonStyle;
    }

    @Override
    public Optional<ButtonPosition> getButtonPosition(TweakType tweak) {
        return Optional.ofNullable((ButtonPosition) this.buttonPositions.get(tweak));
    }
}