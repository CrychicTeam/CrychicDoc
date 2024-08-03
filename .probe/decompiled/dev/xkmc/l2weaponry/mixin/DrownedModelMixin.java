package dev.xkmc.l2weaponry.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2weaponry.init.data.TagGen;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ DrownedModel.class })
public class DrownedModelMixin {

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z") }, method = { "prepareMobModel*" })
    public boolean l2weaponry$prepareMobModel$useOtherThrowables(ItemStack stack, Item item, Operation<Boolean> op) {
        return (Boolean) op.call(new Object[] { stack, item }) || stack.is(TagGen.THROWABLE);
    }
}