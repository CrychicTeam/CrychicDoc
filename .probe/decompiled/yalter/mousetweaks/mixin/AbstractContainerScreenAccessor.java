package yalter.mousetweaks.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ AbstractContainerScreen.class })
public interface AbstractContainerScreenAccessor {

    @Invoker("findSlot")
    Slot mousetweaks$invokeFindSlot(double var1, double var3);

    @Invoker("slotClicked")
    void mousetweaks$invokeSlotClicked(Slot var1, int var2, int var3, ClickType var4);

    @Accessor("isQuickCrafting")
    boolean mousetweaks$getIsQuickCrafting();

    @Accessor("isQuickCrafting")
    void mousetweaks$setIsQuickCrafting(boolean var1);

    @Accessor("quickCraftingButton")
    int mousetweaks$getQuickCraftingButton();

    @Accessor("skipNextRelease")
    void mousetweaks$setSkipNextRelease(boolean var1);
}