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

public class FungusBlock extends BushBlock implements BonemealableBlock {

    protected static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 9.0, 12.0);

    private static final double BONEMEAL_SUCCESS_PROBABILITY = 0.4;

    private final Block requiredBlock;

    private final ResourceKey<ConfiguredFeature<?, ?>> feature;

    protected FungusBlock(BlockBehaviour.Properties blockBehaviourProperties0, ResourceKey<ConfiguredFeature<?, ?>> resourceKeyConfiguredFeature1, Block block2) {
        super(blockBehaviourProperties0);
        this.feature = resourceKeyConfiguredFeature1;
        this.requiredBlock = block2;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_204336_(BlockTags.NYLIUM) || blockState0.m_60713_(Blocks.MYCELIUM) || blockState0.m_60713_(Blocks.SOUL_SOIL) || super.mayPlaceOn(blockState0, blockGetter1, blockPos2);
    }

    private Optional<? extends Holder<ConfiguredFeature<?, ?>>> getFeature(LevelReader levelReader0) {
        return levelReader0.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(this.feature);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        BlockState $$4 = levelReader0.m_8055_(blockPos1.below());
        return $$4.m_60713_(this.requiredBlock);
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return (double) randomSource1.nextFloat() < 0.4;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        this.getFeature(serverLevel0).ifPresent(p_256352_ -> ((ConfiguredFeature) p_256352_.value()).place(serverLevel0, serverLevel0.getChunkSource().getGenerator(), randomSource1, blockPos2));
    }
}