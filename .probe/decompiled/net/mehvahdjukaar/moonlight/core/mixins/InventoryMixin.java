package net.mehvahdjukaar.moonlight.core.mixins;

import java.util.Iterator;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.events.IDropItemOnDeathEvent;
import net.mehvahdjukaar.moonlight.api.events.MoonlightEventsHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ Inventory.class })
public abstract class InventoryMixin {

    @Shadow
    @Final
    public Player player;

    @Unique
    private ItemStack moonlight$toRestore = null;

    @Inject(method = { "dropAll" }, at = { @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", shift = Shift.BEFORE) }, locals = LocalCapture.CAPTURE_FAILHARD)
    public void fireDropEvent(CallbackInfo ci, Iterator var1, List<ItemStack> list, int i) {
        if (this.player.m_21224_() || this.player.f_20890_) {
            ItemStack stack = (ItemStack) list.get(i);
            IDropItemOnDeathEvent event = IDropItemOnDeathEvent.create(stack, this.player, true);
            MoonlightEventsHelper.postEvent(event, IDropItemOnDeathEvent.class);
            if (event.isCanceled()) {
                list.set(i, ItemStack.EMPTY);
                this.moonlight$toRestore = event.getReturnItemStack();
            }
        }
    }

    @Inject(method = { "dropAll" }, at = { @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", shift = Shift.AFTER) }, locals = LocalCapture.CAPTURE_FAILHARD)
    public void restoreNotDropped(CallbackInfo ci, Iterator var1, List<ItemStack> list, int i) {
        if (this.moonlight$toRestore != null) {
            list.set(i, this.moonlight$toRestore);
            this.moonlight$toRestore = null;
        }
    }
}