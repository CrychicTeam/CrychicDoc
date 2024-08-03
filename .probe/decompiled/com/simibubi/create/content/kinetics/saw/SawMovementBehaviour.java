package com.simibubi.create.content.kinetics.saw;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.kinetics.base.BlockBreakingMovementBehaviour;
import com.simibubi.create.foundation.damageTypes.CreateDamageSources;
import com.simibubi.create.foundation.utility.AbstractBlockBreakQueue;
import com.simibubi.create.foundation.utility.TreeCutter;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Optional;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;

public class SawMovementBehaviour extends BlockBreakingMovementBehaviour {

    @Override
    public boolean isActive(MovementContext context) {
        return super.isActive(context) && !VecHelper.isVecPointingTowards(context.relativeMotion, ((Direction) context.state.m_61143_(SawBlock.FACING)).getOpposite());
    }

    @Override
    public Vec3 getActiveAreaOffset(MovementContext context) {
        return Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(SawBlock.FACING)).getNormal()).scale(0.65F);
    }

    @Override
    public void visitNewPosition(MovementContext context, BlockPos pos) {
        super.visitNewPosition(context, pos);
        Vec3 facingVec = Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(SawBlock.FACING)).getNormal());
        facingVec = (Vec3) context.rotation.apply(facingVec);
        Direction closestToFacing = Direction.getNearest(facingVec.x, facingVec.y, facingVec.z);
        if (closestToFacing.getAxis().isVertical() && context.data.contains("BreakingPos")) {
            context.data.remove("BreakingPos");
            context.stall = false;
        }
    }

    @Override
    public boolean canBreak(Level world, BlockPos breakingPos, BlockState state) {
        return super.canBreak(world, breakingPos, state) && SawBlockEntity.isSawable(state);
    }

    @Override
    protected void onBlockBroken(MovementContext context, BlockPos pos, BlockState brokenState) {
        if (!brokenState.m_204336_(BlockTags.LEAVES)) {
            Optional<AbstractBlockBreakQueue> dynamicTree = TreeCutter.findDynamicTree(brokenState.m_60734_(), pos);
            if (dynamicTree.isPresent()) {
                ((AbstractBlockBreakQueue) dynamicTree.get()).destroyBlocks(context.world, null, (stack, dropPos) -> this.dropItemFromCutTree(context, stack, dropPos));
            } else {
                TreeCutter.findTree(context.world, pos).destroyBlocks(context.world, null, (stack, dropPos) -> this.dropItemFromCutTree(context, stack, dropPos));
            }
        }
    }

    public void dropItemFromCutTree(MovementContext context, BlockPos pos, ItemStack stack) {
        ItemStack remainder = ItemHandlerHelper.insertItem(context.contraption.getSharedInventory(), stack, false);
        if (!remainder.isEmpty()) {
            Level world = context.world;
            Vec3 dropPos = VecHelper.getCenterOf(pos);
            float distance = context.position == null ? 1.0F : (float) dropPos.distanceTo(context.position);
            ItemEntity entity = new ItemEntity(world, dropPos.x, dropPos.y, dropPos.z, remainder);
            entity.m_20256_(context.relativeMotion.scale((double) (distance / 20.0F)));
            world.m_7967_(entity);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        SawRenderer.renderInContraption(context, renderWorld, matrices, buffer);
    }

    @Override
    protected boolean shouldDestroyStartBlock(BlockState stateToBreak) {
        return !TreeCutter.canDynamicTreeCutFrom(stateToBreak.m_60734_());
    }

    @Override
    protected DamageSource getDamageSource(Level level) {
        return CreateDamageSources.saw(level);
    }
}