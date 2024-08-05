package com.mna.blocks.artifice;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.TransitoryTunnelTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TransitoryTunnelBlock extends Block implements IDontCreateBlockItem, ITranslucentBlock, EntityBlock {

    public static final IntegerProperty FACE_VISIBILITY_MASK = IntegerProperty.create("face_visibility", 0, 63);

    public TransitoryTunnelBlock() {
        super(BlockBehaviour.Properties.of().replaceable().noCollission().strength(0.0F).noOcclusion().noLootTable());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TransitoryTunnelTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide() ? null : (lvl, pos, state1, be) -> TransitoryTunnelTile.ServerTick(lvl, pos, state1, (TransitoryTunnelTile) be);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        int check_mask = getMaskForDirection(side);
        int state_mask = (Integer) state.m_61143_(FACE_VISIBILITY_MASK);
        return (state_mask & check_mask) != 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACE_VISIBILITY_MASK);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        int mask = getStateBasedOnSurroundings(worldIn, pos);
        if ((Integer) state.m_61143_(FACE_VISIBILITY_MASK) != mask) {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(FACE_VISIBILITY_MASK, mask), 3);
        }
    }

    public static int getStateBasedOnSurroundings(Level world, BlockPos pos) {
        int mask = 0;
        for (Direction d : Direction.values()) {
            mask |= calculateDirectionalMask(world, pos, d);
        }
        return mask;
    }

    private static int calculateDirectionalMask(Level world, BlockPos pos, Direction direction) {
        BlockState offsetState = world.getBlockState(pos.offset(direction.getNormal()));
        return offsetState.m_60734_() != BlockInit.TRANSITORY_TUNNEL.get() ? 0 : getMaskForDirection(direction);
    }

    private static int getMaskForDirection(Direction direction) {
        switch(direction) {
            case NORTH:
            default:
                return 1;
            case SOUTH:
                return 2;
            case EAST:
                return 4;
            case WEST:
                return 8;
            case UP:
                return 16;
            case DOWN:
                return 32;
        }
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if (worldIn.isClientSide && rand.nextFloat() < 0.2F) {
            worldIn.addParticle(new MAParticleType(ParticleInit.ARCANE.get()), (double) ((float) pos.m_123341_() + rand.nextFloat()), (double) ((float) pos.m_123342_() + rand.nextFloat()), (double) ((float) pos.m_123343_() + rand.nextFloat()), 0.0, 0.1F, 0.0);
        }
    }
}