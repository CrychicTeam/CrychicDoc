package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class WeightedPressurePlateBlock extends BasePressurePlateBlock {

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    private final int maxWeight;

    protected WeightedPressurePlateBlock(int int0, BlockBehaviour.Properties blockBehaviourProperties1, BlockSetType blockSetType2) {
        super(blockBehaviourProperties1, blockSetType2);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(POWER, 0));
        this.maxWeight = int0;
    }

    @Override
    protected int getSignalStrength(Level level0, BlockPos blockPos1) {
        int $$2 = Math.min(m_289607_(level0, f_49287_.move(blockPos1), Entity.class), this.maxWeight);
        if ($$2 > 0) {
            float $$3 = (float) Math.min(this.maxWeight, $$2) / (float) this.maxWeight;
            return Mth.ceil($$3 * 15.0F);
        } else {
            return 0;
        }
    }

    @Override
    protected int getSignalForState(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(POWER);
    }

    @Override
    protected BlockState setSignalForState(BlockState blockState0, int int1) {
        return (BlockState) blockState0.m_61124_(POWER, int1);
    }

    @Override
    protected int getPressedTime() {
        return 10;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(POWER);
    }
}