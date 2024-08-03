package pm.meh.icterine.mixin;

import java.util.List;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pm.meh.icterine.Common;
import pm.meh.icterine.iface.IItemPredicateMixin;
import pm.meh.icterine.util.LogHelper;

@Mixin({ InventoryChangeTrigger.TriggerInstance.class })
abstract class InventoryChangeTriggerInstanceMixin {

    @Inject(method = { "matches(Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;III)Z" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getContainerSize()I", ordinal = 0) }, locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void matches(Inventory inventory, ItemStack itemStack, int i, int j, int k, CallbackInfoReturnable<Boolean> cir, int predicatesLength, List<ItemPredicate> predicatesList) {
        if (Common.config.OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER && !predicatesList.removeIf(itemPredicate -> itemPredicate.matches(itemStack))) {
            LogHelper.debug("Trigger has multiple predicates, and none of them matches changed item. Skipping full inventory check");
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Redirect(method = { "matches(Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;III)Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/ItemPredicate;matches(Lnet/minecraft/world/item/ItemStack;)Z"))
    public boolean itemPredicateMatches(ItemPredicate itemPredicate, ItemStack itemStack) {
        return Common.config.CHECK_COUNT_BEFORE_ITEM_PREDICATE_MATCH ? ((IItemPredicateMixin) itemPredicate).icterine$fasterMatches(itemStack) : itemPredicate.matches(itemStack);
    }
}