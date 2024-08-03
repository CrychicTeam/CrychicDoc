package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin({ ItemEntity.class })
public class ItemEntityMixin {

    @ModifyConstant(method = { "merge(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V" }, constant = { @Constant(intValue = 64) })
    private static int increaseStackLimit(int val) {
        return StackSizeHelper.getNewStackSize();
    }
}