package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.EntityFart;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ItemStinkRay extends Item {

    public static final Predicate<ItemStack> IS_FART_BOTTLE = stack -> stack.getItem() == AMItemRegistry.STINK_BOTTLE.get();

    public ItemStinkRay(Item.Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return isUsable(stack) ? 72000 : 0;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;
    }

    @Override
    public boolean isBarVisible(ItemStack itemStack) {
        return super.isBarVisible(itemStack) && isUsable(itemStack);
    }

    public static float getPowerForTime(int i) {
        float f = (float) i / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int time) {
        if (entity instanceof Player player && isUsable(itemStack)) {
            int i = this.getUseDuration(itemStack) - time;
            if (i >= 10) {
                boolean left = false;
                if (entity.getUsedItemHand() == InteractionHand.OFF_HAND && entity.getMainArm() == HumanoidArm.RIGHT || entity.getUsedItemHand() == InteractionHand.MAIN_HAND && entity.getMainArm() == HumanoidArm.LEFT) {
                    left = true;
                }
                EntityFart blood = new EntityFart(level, entity, !left);
                Vec3 vector3d = entity.m_20252_(1.0F);
                RandomSource rand = level.getRandom();
                entity.m_146850_(GameEvent.ITEM_INTERACT_START);
                entity.m_5496_(AMSoundRegistry.STINK_RAY.get(), 1.0F, 0.9F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);
                blood.shoot(vector3d.x(), vector3d.y(), vector3d.z(), 0.2F + getPowerForTime(i) * 0.4F, 10.0F);
                if (!level.isClientSide) {
                    level.m_7967_(blood);
                }
                itemStack.hurtAndBreak(1, entity, breaker -> breaker.broadcastBreakEvent(entity.getUsedItemHand()));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        playerIn.m_6672_(handIn);
        if (!isUsable(itemstack)) {
            ItemStack ammo = this.findAmmo(playerIn);
            boolean flag = playerIn.isCreative();
            if (!ammo.isEmpty()) {
                ammo.shrink(1);
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (!playerIn.addItem(bottle)) {
                    playerIn.drop(bottle, false);
                }
                flag = true;
            }
            if (flag) {
                itemstack.setDamageValue(0);
            }
        }
        return InteractionResultHolder.consume(itemstack);
    }

    public ItemStack findAmmo(Player entity) {
        if (entity.isCreative()) {
            return new ItemStack(AMItemRegistry.STINK_BOTTLE.get());
        } else {
            for (int i = 0; i < entity.getInventory().getContainerSize(); i++) {
                ItemStack itemstack1 = entity.getInventory().getItem(i);
                if (IS_FART_BOTTLE.test(itemstack1)) {
                    return itemstack1;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.isSameItem(oldStack, newStack);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsMobs.PROXY.getISTERProperties());
    }
}