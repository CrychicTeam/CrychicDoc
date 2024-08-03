package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class PressurePlateBlock extends BasePressurePlateBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private final PressurePlateBlock.Sensitivity sensitivity;

    protected PressurePlateBlock(PressurePlateBlock.Sensitivity pressurePlateBlockSensitivity0, BlockBehaviour.Properties blockBehaviourProperties1, BlockSetType blockSetType2) {
        super(blockBehaviourProperties1, blockSetType2);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(POWERED, false));
        this.sensitivity = pressurePlateBlockSensitivity0;
    }

    @Override
    protected int getSignalForState(BlockState blockState0) {
        return blockState0.m_61143_(POWERED) ? 15 : 0;
    }

    @Override
    protected BlockState setSignalForState(BlockState blockState0, int int1) {
        return (BlockState) blockState0.m_61124_(POWERED, int1 > 0);
    }

    @Override
    protected int getSignalStrength(Level level0, BlockPos blockPos1) {
        Class $$2 = switch(this.sensitivity) {
            case EVERYTHING ->
                Entity.class;
            case MOBS ->
                LivingEntity.class;
        };
        return m_289607_(level0, f_49287_.move(blockPos1), $$2) > 0 ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(POWERED);
    }

    public static enum Sensitivity {

        EVERYTHING, MOBS
    }
}