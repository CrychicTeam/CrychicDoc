package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.entity.EntityEnderiophageRocket;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemEnderiophageRocket extends Item {

    public ItemEnderiophageRocket(Item.Properties group) {
        super(group);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!world.isClientSide) {
            ItemStack itemstack = context.getItemInHand();
            Vec3 vector3d = context.getClickLocation();
            Direction direction = context.getClickedFace();
            FireworkRocketEntity fireworkrocketentity = new EntityEnderiophageRocket(world, context.getPlayer(), vector3d.x + (double) direction.getStepX() * 0.15, vector3d.y + (double) direction.getStepY() * 0.15, vector3d.z + (double) direction.getStepZ() * 0.15, itemstack);
            world.m_7967_(fireworkrocketentity);
            if (!context.getPlayer().isCreative()) {
                itemstack.shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (playerIn.m_21255_()) {
            ItemStack itemstack = playerIn.m_21120_(handIn);
            if (!worldIn.isClientSide) {
                worldIn.m_7967_(new EntityEnderiophageRocket(worldIn, itemstack, playerIn));
                if (!playerIn.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
            }
            return InteractionResultHolder.sidedSuccess(playerIn.m_21120_(handIn), worldIn.isClientSide());
        } else {
            return InteractionResultHolder.pass(playerIn.m_21120_(handIn));
        }
    }
}