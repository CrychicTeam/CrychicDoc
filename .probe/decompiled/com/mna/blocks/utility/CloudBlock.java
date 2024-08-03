package com.mna.blocks.utility;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.tools.MATags;
import com.mna.api.tools.RLoc;
import com.mojang.datafixers.util.Pair;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;

public class CloudBlock extends Block implements IDontCreateBlockItem {

    public static final float WATER_CAULDRON_FILL_PROBABILITY_PER_RANDOM_TICK = 0.17578125F;

    public CloudBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WATER).pushReaction(PushReaction.DESTROY).noCollission().noOcclusion().randomTicks());
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        Vec3 drop = Vec3.atBottomCenterOf(pPos).add(-0.5 + Math.random(), 0.0, -0.5 + Math.random());
        pLevel.addParticle(ParticleTypes.DRIPPING_WATER, drop.x, drop.y, drop.z, 0.0, 0.0, 0.0);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        Pair<BlockState, BlockPos> below = this.getNonAirBlockBelow(pPos, pLevel);
        if (below != null) {
            BlockState belowState = (BlockState) below.getFirst();
            BlockPos belowPos = (BlockPos) below.getSecond();
            if (belowState.m_60734_() == Blocks.CAULDRON) {
                if (pRandom.nextFloat() <= 0.17578125F) {
                    pLevel.m_7731_(belowPos, (BlockState) Blocks.WATER_CAULDRON.defaultBlockState().m_61124_(LayeredCauldronBlock.LEVEL, 1), 3);
                }
                return;
            }
            if (belowState.m_60734_() == Blocks.WATER_CAULDRON) {
                int existingFillLevel = (Integer) belowState.m_61143_(LayeredCauldronBlock.LEVEL);
                if (existingFillLevel < 3 && pRandom.nextFloat() <= 1.0F) {
                    pLevel.m_7731_(belowPos, (BlockState) belowState.m_61124_(LayeredCauldronBlock.LEVEL, existingFillLevel + 1), 3);
                }
                return;
            }
            boolean no_age = MATags.isBlockIn(belowState.m_60734_(), RLoc.create("construct_harvestables_no_age"));
            boolean valid = no_age || MATags.isBlockIn(belowState.m_60734_(), RLoc.create("construct_harvestables"));
            if (valid) {
                if (belowState.m_60734_().isRandomlyTicking(belowState)) {
                    belowState.m_60734_().m_213898_(belowState, pLevel, belowPos, pRandom);
                }
                pLevel.m_186460_(belowPos, belowState.m_60734_(), 1);
            }
        }
    }

    @Nullable
    private Pair<BlockState, BlockPos> getNonAirBlockBelow(BlockPos pPos, ServerLevel pLevel) {
        int count = 0;
        BlockPos pos = pPos.below();
        BlockState belowState;
        for (belowState = pLevel.m_8055_(pos); count++ < 5 && belowState.m_60795_(); belowState = pLevel.m_8055_(pos)) {
            pos = pos.below();
        }
        return belowState.m_60795_() ? null : new Pair(belowState, pos);
    }
}