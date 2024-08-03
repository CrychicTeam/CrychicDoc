package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.EntityVineLasso;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ItemVineLasso extends Item {

    public ItemVineLasso(Item.Properties props) {
        super(props);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public static boolean isItemInUse(ItemStack stack) {
        return stack.getTag() != null && stack.getTag().contains("Swinging") && stack.getTag().getBoolean("Swinging");
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int i, boolean b) {
        if (entity instanceof LivingEntity) {
            if (stack.getTag() != null) {
                stack.getTag().putBoolean("Swinging", ((LivingEntity) entity).getUseItem() == stack && ((LivingEntity) entity).isUsingItem());
            } else {
                stack.setTag(new CompoundTag());
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack0) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack itemstack = player1.m_21120_(interactionHand2);
        player1.m_6672_(interactionHand2);
        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public void onUseTick(Level worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (count % 7 == 0) {
            livingEntityIn.m_146850_(GameEvent.ITEM_INTERACT_START);
            livingEntityIn.m_5496_(AMSoundRegistry.VINE_LASSO.get(), 1.0F, 1.0F + (livingEntityIn.getRandom().nextFloat() - livingEntityIn.getRandom().nextFloat()) * 0.2F);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity livingEntityIn, int i) {
        if (!worldIn.isClientSide) {
            boolean left = false;
            if (livingEntityIn.getUsedItemHand() == InteractionHand.OFF_HAND && livingEntityIn.getMainArm() == HumanoidArm.RIGHT || livingEntityIn.getUsedItemHand() == InteractionHand.MAIN_HAND && livingEntityIn.getMainArm() == HumanoidArm.LEFT) {
                left = true;
            }
            int power = this.getUseDuration(stack) - i;
            EntityVineLasso lasso = new EntityVineLasso(worldIn, livingEntityIn);
            Vec3 vector3d = livingEntityIn.m_20252_(1.0F);
            lasso.shoot(vector3d.x(), vector3d.y(), vector3d.z(), getPowerForTime(power), 1.0F);
            if (!worldIn.isClientSide) {
                worldIn.m_7967_(lasso);
            }
            stack.shrink(1);
        }
    }

    public static float getPowerForTime(int p) {
        float f = (float) p / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsMobs.PROXY.getISTERProperties());
    }
}