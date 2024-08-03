package pm.meh.icterine.mixin;

import java.util.function.Supplier;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pm.meh.icterine.Common;
import pm.meh.icterine.iface.IItemStackMixin;
import pm.meh.icterine.impl.StackSizeThresholdManager;
import pm.meh.icterine.util.LogHelper;

@Mixin({ InventoryChangeTrigger.class })
abstract class InventoryChangeTriggerMixin {

    @Inject(method = { "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V" }, at = { @At("HEAD") }, cancellable = true)
    public void trigger(ServerPlayer serverPlayer, Inventory inventory, ItemStack itemStack, CallbackInfo ci) {
        if ((!itemStack.isEmpty() || !Common.config.IGNORE_TRIGGERS_FOR_EMPTIED_STACKS) && (!Common.config.IGNORE_TRIGGERS_FOR_DECREASED_STACKS || itemStack.getCount() >= ((IItemStackMixin) itemStack).icterine$getPreviousStackSize()) && (!Common.config.OPTIMIZE_TRIGGERS_FOR_INCREASED_STACKS || StackSizeThresholdManager.doesStackPassThreshold(itemStack))) {
            LogHelper.debug((Supplier<String>) (() -> "InventoryChangeTrigger passed for %s".formatted(itemStack)));
        } else {
            ci.cancel();
            LogHelper.debug((Supplier<String>) (() -> "InventoryChangeTrigger cancelled for %s".formatted(itemStack)));
        }
    }
}