package net.blay09.mods.craftingtweaks.api;

import net.minecraft.world.inventory.AbstractContainerMenu;

public interface CraftingGridDecorator {

    CraftingGridDecorator disableTweak(TweakType var1);

    CraftingGridDecorator disableAllTweaks();

    CraftingGridDecorator usePhantomItems();

    CraftingGridDecorator rotateHandler(GridRotateHandler<AbstractContainerMenu> var1);

    CraftingGridDecorator balanceHandler(GridBalanceHandler<AbstractContainerMenu> var1);

    CraftingGridDecorator clearHandler(GridClearHandler<AbstractContainerMenu> var1);

    CraftingGridDecorator transferHandler(GridTransferHandler<AbstractContainerMenu> var1);

    CraftingGridDecorator hideTweakButton(TweakType var1);

    CraftingGridDecorator hideAllTweakButtons();

    CraftingGridDecorator setButtonAlignment(ButtonAlignment var1);

    CraftingGridDecorator setButtonAlignmentOffset(int var1, int var2);

    CraftingGridDecorator setButtonStyle(ButtonStyle var1);

    CraftingGridDecorator setButtonPosition(TweakType var1, int var2, int var3);
}