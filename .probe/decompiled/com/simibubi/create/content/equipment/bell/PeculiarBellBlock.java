package com.simibubi.create.content.equipment.bell;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.phys.Vec3;

public class PeculiarBellBlock extends AbstractBellBlock<PeculiarBellBlockEntity> {

    public PeculiarBellBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends PeculiarBellBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends PeculiarBellBlockEntity>) AllBlockEntityTypes.PECULIAR_BELL.get();
    }

    @Override
    public Class<PeculiarBellBlockEntity> getBlockEntityClass() {
        return PeculiarBellBlockEntity.class;
    }

    @Override
    public void playSound(Level world, BlockPos pos) {
        AllSoundEvents.PECULIAR_BELL_USE.playOnServer(world, pos, 2.0F, 0.94F);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState newState = super.m_5573_(ctx);
        if (newState == null) {
            return null;
        } else {
            Level world = ctx.m_43725_();
            BlockPos pos = ctx.getClickedPos();
            return this.tryConvert(world, pos, newState, world.getBlockState(pos.relative(Direction.DOWN)));
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        BlockState newState = super.m_7417_(state, facing, facingState, world, currentPos, facingPos);
        return facing != Direction.DOWN ? newState : this.tryConvert(world, currentPos, newState, facingState);
    }

    protected BlockState tryConvert(LevelAccessor world, BlockPos pos, BlockState state, BlockState underState) {
        if (!AllBlocks.PECULIAR_BELL.has(state)) {
            return state;
        } else {
            Block underBlock = underState.m_60734_();
            if (!Blocks.SOUL_FIRE.equals(underBlock) && !Blocks.SOUL_CAMPFIRE.equals(underBlock)) {
                return state;
            } else {
                if (world.m_5776_()) {
                    this.spawnConversionParticles(world, pos);
                } else if (world instanceof Level) {
                    AllSoundEvents.HAUNTED_BELL_CONVERT.playOnServer((Level) world, pos);
                }
                return (BlockState) ((BlockState) ((BlockState) AllBlocks.HAUNTED_BELL.getDefaultState().m_61124_(HauntedBellBlock.f_49679_, (Direction) state.m_61143_(f_49679_))).m_61124_(HauntedBellBlock.f_49680_, (BellAttachType) state.m_61143_(f_49680_))).m_61124_(HauntedBellBlock.f_49681_, (Boolean) state.m_61143_(f_49681_));
            }
        }
    }

    public void spawnConversionParticles(LevelAccessor world, BlockPos blockPos) {
        RandomSource random = world.getRandom();
        int num = random.nextInt(10) + 15;
        for (int i = 0; i < num; i++) {
            float pitch = random.nextFloat() * 120.0F - 90.0F;
            float yaw = random.nextFloat() * 360.0F;
            Vec3 vel = Vec3.directionFromRotation(pitch, yaw).scale(random.nextDouble() * 0.1 + 0.1);
            Vec3 pos = Vec3.atCenterOf(blockPos);
            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
        }
    }
}