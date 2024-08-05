package net.minecraft.world.level.block;

import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class LayeredCauldronBlock extends AbstractCauldronBlock {

    public static final int MIN_FILL_LEVEL = 1;

    public static final int MAX_FILL_LEVEL = 3;

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;

    private static final int BASE_CONTENT_HEIGHT = 6;

    private static final double HEIGHT_PER_LEVEL = 3.0;

    public static final Predicate<Biome.Precipitation> RAIN = p_153553_ -> p_153553_ == Biome.Precipitation.RAIN;

    public static final Predicate<Biome.Precipitation> SNOW = p_153526_ -> p_153526_ == Biome.Precipitation.SNOW;

    private final Predicate<Biome.Precipitation> fillPredicate;

    public LayeredCauldronBlock(BlockBehaviour.Properties blockBehaviourProperties0, Predicate<Biome.Precipitation> predicateBiomePrecipitation1, Map<Item, CauldronInteraction> mapItemCauldronInteraction2) {
        super(blockBehaviourProperties0, mapItemCauldronInteraction2);
        this.fillPredicate = predicateBiomePrecipitation1;
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LEVEL, 1));
    }

    @Override
    public boolean isFull(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(LEVEL) == 3;
    }

    @Override
    protected boolean canReceiveStalactiteDrip(Fluid fluid0) {
        return fluid0 == Fluids.WATER && this.fillPredicate == RAIN;
    }

    @Override
    protected double getContentHeight(BlockState blockState0) {
        return (6.0 + (double) ((Integer) blockState0.m_61143_(LEVEL)).intValue() * 3.0) / 16.0;
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (!level1.isClientSide && entity3.isOnFire() && this.m_151979_(blockState0, blockPos2, entity3)) {
            entity3.clearFire();
            if (entity3.mayInteract(level1, blockPos2)) {
                this.handleEntityOnFireInside(blockState0, level1, blockPos2);
            }
        }
    }

    protected void handleEntityOnFireInside(BlockState blockState0, Level level1, BlockPos blockPos2) {
        lowerFillLevel(blockState0, level1, blockPos2);
    }

    public static void lowerFillLevel(BlockState blockState0, Level level1, BlockPos blockPos2) {
        int $$3 = (Integer) blockState0.m_61143_(LEVEL) - 1;
        BlockState $$4 = $$3 == 0 ? Blocks.CAULDRON.defaultBlockState() : (BlockState) blockState0.m_61124_(LEVEL, $$3);
        level1.setBlockAndUpdate(blockPos2, $$4);
        level1.m_220407_(GameEvent.BLOCK_CHANGE, blockPos2, GameEvent.Context.of($$4));
    }

    @Override
    public void handlePrecipitation(BlockState blockState0, Level level1, BlockPos blockPos2, Biome.Precipitation biomePrecipitation3) {
        if (CauldronBlock.shouldHandlePrecipitation(level1, biomePrecipitation3) && (Integer) blockState0.m_61143_(LEVEL) != 3 && this.fillPredicate.test(biomePrecipitation3)) {
            BlockState $$4 = (BlockState) blockState0.m_61122_(LEVEL);
            level1.setBlockAndUpdate(blockPos2, $$4);
            level1.m_220407_(GameEvent.BLOCK_CHANGE, blockPos2, GameEvent.Context.of($$4));
        }
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return (Integer) blockState0.m_61143_(LEVEL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(LEVEL);
    }

    @Override
    protected void receiveStalactiteDrip(BlockState blockState0, Level level1, BlockPos blockPos2, Fluid fluid3) {
        if (!this.isFull(blockState0)) {
            BlockState $$4 = (BlockState) blockState0.m_61124_(LEVEL, (Integer) blockState0.m_61143_(LEVEL) + 1);
            level1.setBlockAndUpdate(blockPos2, $$4);
            level1.m_220407_(GameEvent.BLOCK_CHANGE, blockPos2, GameEvent.Context.of($$4));
            level1.m_46796_(1047, blockPos2, 0);
        }
    }
}