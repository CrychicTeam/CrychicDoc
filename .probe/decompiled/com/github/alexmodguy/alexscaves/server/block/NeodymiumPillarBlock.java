package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;

public class NeodymiumPillarBlock extends Block {

    private boolean azure = false;

    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public NeodymiumPillarBlock(boolean azure) {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(ACSoundTypes.NEODYMIUM).lightLevel(i -> 2).emissiveRendering((state, level, pos) -> true));
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(TOP, true)).m_61124_(FACING, Direction.UP));
        this.azure = azure;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = (Direction) state.m_61143_(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        return level.m_8055_(blockpos).m_60783_(level, blockpos, direction);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        BlockState pillar = super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
        if (levelAccessor.m_8055_(blockPos.relative((Direction) state.m_61143_(FACING))).m_60734_() == this.getBlock()) {
            pillar = (BlockState) pillar.m_61124_(TOP, false);
        } else {
            pillar = (BlockState) pillar.m_61124_(TOP, true);
        }
        return pillar;
    }

    public Block getBlock() {
        return this.azure ? ACBlockRegistry.AZURE_NEODYMIUM_PILLAR.get() : ACBlockRegistry.SCARLET_NEODYMIUM_PILLAR.get();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        BlockState above = levelaccessor.m_8055_(blockpos.relative(context.m_43719_()));
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(TOP, above.m_60734_() != this.getBlock())).m_61124_(FACING, context.m_43719_());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(FACING, rotation.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(TOP, FACING);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        Vec3 center = Vec3.atCenterOf(pos);
        if (randomSource.nextInt(5) == 0) {
            level.addParticle(this.azure ? ACParticleRegistry.AZURE_MAGNETIC_ORBIT.get() : ACParticleRegistry.SCARLET_MAGNETIC_ORBIT.get(), center.x, center.y, center.z, center.x, center.y, center.z);
        }
    }
}