package com.mna.items.worldgen;

import com.mna.blocks.BlockInit;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

public class ItemWakebloom extends BlockItem {

    public ItemWakebloom(Block blockIn, Item.Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        HitResult raytraceresult = m_41435_(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);
        if (raytraceresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            if (raytraceresult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockraytraceresult = (BlockHitResult) raytraceresult;
                BlockPos blockpos = blockraytraceresult.getBlockPos();
                Direction direction = blockraytraceresult.getDirection();
                if (!worldIn.mayInteract(playerIn, blockpos) || !playerIn.mayUseItemAt(blockpos.relative(direction), direction, itemstack)) {
                    return InteractionResultHolder.fail(itemstack);
                }
                BlockPos blockpos1 = blockpos.above();
                FluidState ifluidstate = worldIn.getFluidState(blockpos);
                if (ifluidstate.getType() == Fluids.WATER && worldIn.m_46859_(blockpos1)) {
                    BlockSnapshot blocksnapshot = BlockSnapshot.create(worldIn.dimension(), worldIn, blockpos1);
                    worldIn.setBlock(blockpos1, BlockInit.WAKEBLOOM.get().m_49966_(), 11);
                    if (ForgeEventFactory.onBlockPlace(playerIn, blocksnapshot, Direction.UP)) {
                        blocksnapshot.restore(true, false);
                        return InteractionResultHolder.fail(itemstack);
                    }
                    if (playerIn instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) playerIn, blockpos1, itemstack);
                    }
                    if (!playerIn.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    worldIn.playSound(playerIn, blockpos, SoundEvents.LILY_PAD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResultHolder.success(itemstack);
                }
            }
            return InteractionResultHolder.fail(itemstack);
        }
    }
}