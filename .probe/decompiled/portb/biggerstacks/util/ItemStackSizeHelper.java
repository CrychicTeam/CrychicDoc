package portb.biggerstacks.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import portb.biggerstacks.BiggerStacks;
import portb.biggerstacks.config.StackSizeRules;
import portb.configlib.ItemProperties;
import portb.configlib.TagAccessor;

public class ItemStackSizeHelper {

    public static void applyStackSizeToItem(ItemStack itemstack, CallbackInfoReturnable<Integer> returnInfo) {
        if (StackSizeRules.getRuleSet() != null) {
            Item item = itemstack.getItem();
            ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(item);
            StackSizeRules.getRuleSet().determineStackSizeForItem(new ItemProperties(itemKey.getNamespace(), itemKey.toString(), "", (Integer) returnInfo.getReturnValue(), item.isEdible(), item instanceof BlockItem, item.canBeDepleted(), item instanceof BucketItem, new ItemStackSizeHelper.TagAccessorImpl(itemstack), item.getClass())).ifPresent(stackSize -> {
                returnInfo.cancel();
                returnInfo.setReturnValue(stackSize);
            });
        } else {
            BiggerStacks.LOGGER.debug("Stack size ruleset is somehow null, using fallback logic. Called from " + CallingClassUtil.getCallerClassName());
            if ((Integer) returnInfo.getReturnValue() > 1) {
                returnInfo.cancel();
                returnInfo.setReturnValue(StackSizeHelper.getNewStackSize());
            }
        }
    }

    private static record TagAccessorImpl(ItemStack item) implements TagAccessor {

        public boolean doesItemHaveTag(String tag) {
            return this.item.is(new TagKey<>(Registries.ITEM, new ResourceLocation(tag)));
        }
    }
}