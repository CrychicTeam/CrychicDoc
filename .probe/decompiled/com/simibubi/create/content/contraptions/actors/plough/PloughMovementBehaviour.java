package com.simibubi.create.content.contraptions.actors.plough;

import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.kinetics.base.BlockBreakingMovementBehaviour;
import com.simibubi.create.content.trains.track.FakeTrackBlock;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class PloughMovementBehaviour extends BlockBreakingMovementBehaviour {

    @Override
    public boolean isActive(MovementContext context) {
        return super.isActive(context) && !VecHelper.isVecPointingTowards(context.relativeMotion, ((Direction) context.state.m_61143_(PloughBlock.f_54117_)).getOpposite());
    }

    @Override
    public void visitNewPosition(MovementContext context, BlockPos pos) {
        super.visitNewPosition(context, pos);
        Level world = context.world;
        if (!world.isClientSide) {
            BlockPos below = pos.below();
            if (world.isLoaded(below)) {
                Vec3 vec = VecHelper.getCenterOf(pos);
                PloughBlock.PloughFakePlayer player = this.getPlayer(context);
                if (player != null) {
                    BlockHitResult ray = world.m_45547_(new ClipContext(vec, vec.add(0.0, -1.0, 0.0), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
                    if (ray.getType() == HitResult.Type.BLOCK) {
                        UseOnContext ctx = new UseOnContext(player, InteractionHand.MAIN_HAND, ray);
                        new ItemStack(Items.DIAMOND_HOE).useOn(ctx);
                    }
                }
            }
        }
    }

    @Override
    protected void throwEntity(MovementContext context, Entity entity) {
        super.throwEntity(context, entity);
        if (entity instanceof FallingBlockEntity fbe) {
            if (fbe.getBlockState().m_60734_() instanceof AnvilBlock) {
                if (!(entity.getDeltaMovement().length() < 0.25)) {
                    entity.level().m_45976_(Player.class, new AABB(entity.blockPosition()).inflate(32.0)).forEach(AllAdvancements.ANVIL_PLOUGH::awardTo);
                }
            }
        }
    }

    @Override
    public Vec3 getActiveAreaOffset(MovementContext context) {
        return Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(PloughBlock.f_54117_)).getNormal()).scale(0.45);
    }

    @Override
    protected boolean throwsEntities(Level level) {
        return true;
    }

    @Override
    public boolean canBreak(Level world, BlockPos breakingPos, BlockState state) {
        if (state.m_60795_()) {
            return false;
        } else if (world.getBlockState(breakingPos.below()).m_60734_() instanceof FarmBlock) {
            return false;
        } else if (state.m_60734_() instanceof LiquidBlock) {
            return false;
        } else if (state.m_60734_() instanceof BubbleColumnBlock) {
            return false;
        } else if (state.m_60734_() instanceof NetherPortalBlock) {
            return false;
        } else if (state.m_60734_() instanceof ITrackBlock) {
            return true;
        } else {
            return state.m_60734_() instanceof FakeTrackBlock ? false : state.m_60812_(world, breakingPos).isEmpty();
        }
    }

    @Override
    protected void onBlockBroken(MovementContext context, BlockPos pos, BlockState brokenState) {
        super.onBlockBroken(context, pos, brokenState);
        if (brokenState.m_60734_() == Blocks.SNOW && context.world instanceof ServerLevel world) {
            brokenState.m_287290_(new LootParams.Builder(world).withParameter(LootContextParams.BLOCK_STATE, brokenState).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.THIS_ENTITY, this.getPlayer(context)).withParameter(LootContextParams.TOOL, new ItemStack(Items.IRON_SHOVEL))).forEach(s -> this.dropItem(context, s));
        }
    }

    @Override
    public void stopMoving(MovementContext context) {
        super.stopMoving(context);
        if (context.temporaryData instanceof PloughBlock.PloughFakePlayer) {
            ((PloughBlock.PloughFakePlayer) context.temporaryData).m_146870_();
        }
    }

    private PloughBlock.PloughFakePlayer getPlayer(MovementContext context) {
        if (!(context.temporaryData instanceof PloughBlock.PloughFakePlayer) && context.world != null) {
            PloughBlock.PloughFakePlayer player = new PloughBlock.PloughFakePlayer((ServerLevel) context.world);
            player.m_21008_(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_HOE));
            context.temporaryData = player;
        }
        return (PloughBlock.PloughFakePlayer) context.temporaryData;
    }
}