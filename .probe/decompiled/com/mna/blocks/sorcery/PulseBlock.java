package com.mna.blocks.sorcery;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PulseBlock extends Block implements IDontCreateBlockItem {

    public static final IntegerProperty OUTPUT_POWER = IntegerProperty.create("output_power", 1, 15);

    protected static final VoxelShape SHAPE = Block.box(6.0, 6.0, 6.0, 10.0, 10.0, 10.0);

    public PulseBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.0F).noCollission().noLootTable());
        this.m_49959_((BlockState) this.m_49966_().m_61124_(OUTPUT_POWER, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OUTPUT_POWER);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        worldIn.m_7731_(pos, Blocks.AIR.defaultBlockState(), 3);
        worldIn.updateNeighborsAt(pos, this);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        Vec3 origin = Vec3.atCenterOf(pPos);
        Vec3 boltTarget = origin.add(-0.5 + Math.random(), -0.5, -0.5 + Math.random());
        pLevel.addParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()).setColor(255, 0, 0), origin.x, origin.y, origin.z, boltTarget.x, boltTarget.y, boltTarget.z);
        for (int i = 0; i < pState.m_61143_(OUTPUT_POWER) / 3; i++) {
            pLevel.addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()).setColor(255, 0, 0).setGravity(0.05F), origin.x, origin.y, origin.z, (-0.5 + Math.random()) * 0.2, 0.05, (-0.5 + Math.random()) * 0.2);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.m_60746_(blockAccess, pos, side);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return (Integer) blockState.m_61143_(OUTPUT_POWER);
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }
}