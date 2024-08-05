package net.minecraft.world.level.block;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MushroomBlock extends BushBlock implements BonemealableBlock {

    protected static final float AABB_OFFSET = 3.0F;

    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);

    private final ResourceKey<ConfiguredFeature<?, ?>> feature;

    public MushroomBlock(BlockBehaviour.Properties blockBehaviourProperties0, ResourceKey<ConfiguredFeature<?, ?>> resourceKeyConfiguredFeature1) {
        super(blockBehaviourProperties0);
        this.feature = resourceKeyConfiguredFeature1;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (randomSource3.nextInt(25) == 0) {
            int $$4 = 5;
            int $$5 = 4;
            for (BlockPos $$6 : BlockPos.betweenClosed(blockPos2.offset(-4, -1, -4), blockPos2.offset(4, 1, 4))) {
                if (serverLevel1.m_8055_($$6).m_60713_(this)) {
                    if (--$$4 <= 0) {
                        return;
                    }
                }
            }
            BlockPos $$7 = blockPos2.offset(randomSource3.nextInt(3) - 1, randomSource3.nextInt(2) - randomSource3.nextInt(2), randomSource3.nextInt(3) - 1);
            for (int $$8 = 0; $$8 < 4; $$8++) {
                if (serverLevel1.m_46859_($$7) && blockState0.m_60710_(serverLevel1, $$7)) {
                    blockPos2 = $$7;
                }
                $$7 = blockPos2.offset(randomSource3.nextInt(3) - 1, randomSource3.nextInt(2) - randomSource3.nextInt(2), randomSource3.nextInt(3) - 1);
            }
            if (serverLevel1.m_46859_($$7) && blockState0.m_60710_(serverLevel1, $$7)) {
                serverLevel1.m_7731_($$7, blockState0, 2);
            }
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_60804_(blockGetter1, blockPos2);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockPos $$3 = blockPos2.below();
        BlockState $$4 = levelReader1.m_8055_($$3);
        return $$4.m_204336_(BlockTags.MUSHROOM_GROW_BLOCK) ? true : levelReader1.m_45524_(blockPos2, 0) < 13 && this.mayPlaceOn($$4, levelReader1, $$3);
    }

    public boolean growMushroom(ServerLevel serverLevel0, BlockPos blockPos1, BlockState blockState2, RandomSource randomSource3) {
        Optional<? extends Holder<ConfiguredFeature<?, ?>>> $$4 = serverLevel0.m_9598_().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(this.feature);
        if ($$4.isEmpty()) {
            return false;
        } else {
            serverLevel0.m_7471_(blockPos1, false);
            if (((ConfiguredFeature) ((Holder) $$4.get()).value()).place(serverLevel0, serverLevel0.getChunkSource().getGenerator(), randomSource3, blockPos1)) {
                return true;
            } else {
                serverLevel0.m_7731_(blockPos1, blockState2, 3);
                return false;
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return (double) randomSource1.nextFloat() < 0.4;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        this.growMushroom(serverLevel0, blockPos2, blockState3, randomSource1);
    }
}