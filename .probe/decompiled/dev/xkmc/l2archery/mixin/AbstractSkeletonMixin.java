package dev.xkmc.l2archery.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2archery.init.data.ArcheryTagGen;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ AbstractSkeleton.class })
public class AbstractSkeletonMixin {

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z") }, method = { "reassessWeaponGoal" })
    public boolean l2archery$reassessWeaponGoal$useOtherBows(ItemStack stack, Item item, Operation<Boolean> op) {
        return (Boolean) op.call(new Object[] { stack, item }) || stack.is(ArcheryTagGen.PROF_BOWS);
    }
}