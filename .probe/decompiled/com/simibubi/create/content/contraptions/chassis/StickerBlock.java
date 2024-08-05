package com.simibubi.create.content.contraptions.chassis;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;

public class StickerBlock extends WrenchableDirectionalBlock implements IBE<StickerBlockEntity> {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;

    public StickerBlock(BlockBehaviour.Properties p_i48415_1_) {
        super(p_i48415_1_);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(POWERED, false)).m_61124_(EXTENDED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction nearestLookingDirection = context.getNearestLookingDirection();
        boolean shouldPower = context.m_43725_().m_276867_(context.getClickedPos());
        Direction facing = context.m_43723_() != null && context.m_43723_().m_6144_() ? nearestLookingDirection : nearestLookingDirection.getOpposite();
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(f_52588_, facing)).m_61124_(POWERED, shouldPower);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED, EXTENDED));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            boolean previouslyPowered = (Boolean) state.m_61143_(POWERED);
            if (previouslyPowered != worldIn.m_276867_(pos)) {
                state = (BlockState) state.m_61122_(POWERED);
                if ((Boolean) state.m_61143_(POWERED)) {
                    state = (BlockState) state.m_61122_(EXTENDED);
                }
                worldIn.setBlock(pos, state, 2);
            }
        }
    }

    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return false;
    }

    @Override
    public Class<StickerBlockEntity> getBlockEntityClass() {
        return StickerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StickerBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends StickerBlockEntity>) AllBlockEntityTypes.STICKER.get();
    }

    private boolean isUprightSticker(BlockGetter world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return AllBlocks.STICKER.has(blockState) && blockState.m_61143_(f_52588_) == Direction.UP;
    }

    @Override
    public void fallOn(Level level0, BlockState blockState1, BlockPos blockPos2, Entity entity3, float float4) {
        if (!this.isUprightSticker(level0, blockPos2) || entity3.isSuppressingBounce()) {
            super.m_142072_(level0, blockState1, blockPos2, entity3, float4);
        }
        entity3.causeFallDamage(float4, 1.0F, level0.damageSources().fall());
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter p_176216_1_, Entity p_176216_2_) {
        if (this.isUprightSticker(p_176216_1_, p_176216_2_.blockPosition().below()) && !p_176216_2_.isSuppressingBounce()) {
            this.bounceUp(p_176216_2_);
        } else {
            super.m_5548_(p_176216_1_, p_176216_2_);
        }
    }

    private void bounceUp(Entity p_226946_1_) {
        Vec3 Vector3d = p_226946_1_.getDeltaMovement();
        if (Vector3d.y < 0.0) {
            double d0 = p_226946_1_ instanceof LivingEntity ? 1.0 : 0.8;
            p_226946_1_.setDeltaMovement(Vector3d.x, -Vector3d.y * d0, Vector3d.z);
        }
    }

    @Override
    public void stepOn(Level level0, BlockPos blockPos1, BlockState blockState2, Entity entity3) {
        double d0 = Math.abs(entity3.getDeltaMovement().y);
        if (d0 < 0.1 && !entity3.isSteppingCarefully() && this.isUprightSticker(level0, blockPos1)) {
            double d1 = 0.4 + d0 * 0.2;
            entity3.setDeltaMovement(entity3.getDeltaMovement().multiply(d1, 1.0, d1));
        }
        super.m_141947_(level0, blockPos1, blockState2, entity3);
    }

    public boolean addLandingEffects(BlockState state1, ServerLevel worldserver, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        if (this.isUprightSticker(worldserver, pos)) {
            worldserver.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState()), entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), numberOfParticles, 0.0, 0.0, 0.0, 0.15F);
            return true;
        } else {
            return super.addLandingEffects(state1, worldserver, pos, state2, entity, numberOfParticles);
        }
    }

    public boolean addRunningEffects(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (state.m_61143_(f_52588_) == Direction.UP) {
            Vec3 Vector3d = entity.getDeltaMovement();
            world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.defaultBlockState()).setPos(pos), entity.getX() + ((double) world.random.nextFloat() - 0.5) * (double) entity.getBbWidth(), entity.getY() + 0.1, entity.getZ() + ((double) world.random.nextFloat() - 0.5) * (double) entity.getBbWidth(), Vector3d.x * -4.0, 1.5, Vector3d.z * -4.0);
            return true;
        } else {
            return super.addRunningEffects(state, world, pos, entity);
        }
    }
}