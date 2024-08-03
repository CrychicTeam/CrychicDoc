package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.util.ItemStackSizeHelper;

@Mixin({ ItemStack.class })
public class ItemStackMixin {

    @Inject(method = { "getMaxStackSize" }, at = { @At("RETURN") }, cancellable = true)
    private void increaseStackLimit(CallbackInfoReturnable<Integer> returnInfo) {
        ItemStackSizeHelper.applyStackSizeToItem((ItemStack) this, returnInfo);
    }

    @Redirect(method = { "save" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putByte(Ljava/lang/String;B)V"))
    private void saveBigStack(CompoundTag tag, String key, byte byte0) {
        int count = ((ItemStack) this).getCount();
        tag.putByte("Count", (byte) Math.min(count, 127));
        if (count > 127) {
            tag.putInt("BigCount", count);
        }
    }

    @Redirect(method = { "<init>(Lnet/minecraft/nbt/CompoundTag;)V" }, at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/ItemStack;count:I", opcode = 181))
    private void readBigStack(ItemStack instance, int value, CompoundTag tag) {
        ItemStackAccessor accessor = (ItemStackAccessor) instance;
        if (tag.contains("BigCount")) {
            accessor.accessSetCount(tag.getInt("BigCount"));
        } else if (tag.getTagType("Count") == 3) {
            accessor.accessSetCount(tag.getInt("Count"));
        } else {
            accessor.accessSetCount(tag.getByte("Count"));
        }
    }
}