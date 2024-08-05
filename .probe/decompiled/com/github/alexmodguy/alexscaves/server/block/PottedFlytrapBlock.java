package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;

public class PottedFlytrapBlock extends FlowerPotBlock {

    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public PottedFlytrapBlock() {
        super(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> ACBlockRegistry.FLYTRAP.get(), BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY).randomTicks());
        this.m_49959_((BlockState) this.m_49966_().m_61124_(OPEN, true));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        super.m_213898_(state, level, pos, randomSource);
        if ((Boolean) state.m_61143_(OPEN)) {
            level.m_7731_(pos, (BlockState) state.m_61124_(OPEN, false), 2);
            level.m_186460_(pos, this, 100 + randomSource.nextInt(100));
        } else {
            level.m_7731_(pos, (BlockState) state.m_61124_(OPEN, true), 2);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        super.m_213897_(state, level, pos, randomSource);
        level.m_7731_(pos, (BlockState) state.m_61124_(OPEN, true), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(OPEN);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if ((Boolean) state.m_61143_(OPEN) && randomSource.nextInt(3) == 0) {
            Vec3 center = Vec3.upFromBottomCenterOf(pos, 0.75).add(state.m_60824_(level, pos));
            level.addParticle(ACParticleRegistry.FLY.get(), center.x, center.y, center.z, center.x, center.y, center.z);
        }
    }
}