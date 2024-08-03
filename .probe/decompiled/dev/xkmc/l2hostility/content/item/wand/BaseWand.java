package dev.xkmc.l2hostility.content.item.wand;

import dev.xkmc.l2library.util.raytrace.IGlowingTarget;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class BaseWand extends Item implements IGlowingTarget, IMobClickItem {

    public BaseWand(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide() && selected && entity instanceof Player player) {
            RayTraceUtil.clientUpdateTarget(player, 64.0);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!level.isClientSide()) {
            LivingEntity target = RayTraceUtil.serverGetTarget(player);
            if (target != null) {
                this.clickTarget(stack, player, target);
            } else {
                this.clickNothing(stack, player);
            }
        }
        return InteractionResultHolder.success(stack);
    }

    public abstract void clickTarget(ItemStack var1, Player var2, LivingEntity var3);

    public void clickNothing(ItemStack stack, Player player) {
    }

    @Override
    public int getDistance(ItemStack stack) {
        return 64;
    }
}