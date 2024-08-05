package com.simibubi.create.content.redstone.analogLever;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

public class AnalogLeverBlock extends FaceAttachedHorizontalDirectionalBlock implements IBE<AnalogLeverBlockEntity> {

    public AnalogLeverBlock(BlockBehaviour.Properties p_i48402_1_) {
        super(p_i48402_1_);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            addParticles(state, worldIn, pos, 1.0F);
            return InteractionResult.SUCCESS;
        } else {
            return this.onBlockEntityUse(worldIn, pos, be -> {
                boolean sneak = player.m_6144_();
                be.changeState(sneak);
                float f = 0.25F + (float) (be.state + 5) / 15.0F * 0.5F;
                worldIn.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.2F, f);
                return InteractionResult.SUCCESS;
            });
        }
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return (Integer) this.getBlockEntityOptional(blockAccess, pos).map(al -> al.state).orElse(0);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return m_53200_(blockState) == side ? this.getSignal(blockState, blockAccess, pos, side) : 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        this.withBlockEntityDo(worldIn, pos, be -> {
            if (be.state != 0 && rand.nextFloat() < 0.25F) {
                addParticles(stateIn, worldIn, pos, 0.5F);
            }
        });
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && state.m_60734_() != newState.m_60734_()) {
            this.withBlockEntityDo(worldIn, pos, be -> {
                if (be.state != 0) {
                    updateNeighbors(state, worldIn, pos);
                }
                worldIn.removeBlockEntity(pos);
            });
        }
    }

    private static void addParticles(BlockState state, LevelAccessor worldIn, BlockPos pos, float alpha) {
        Direction direction = ((Direction) state.m_61143_(f_54117_)).getOpposite();
        Direction direction1 = m_53200_(state).getOpposite();
        double d0 = (double) pos.m_123341_() + 0.5 + 0.1 * (double) direction.getStepX() + 0.2 * (double) direction1.getStepX();
        double d1 = (double) pos.m_123342_() + 0.5 + 0.1 * (double) direction.getStepY() + 0.2 * (double) direction1.getStepY();
        double d2 = (double) pos.m_123343_() + 0.5 + 0.1 * (double) direction.getStepZ() + 0.2 * (double) direction1.getStepZ();
        worldIn.addParticle(new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), alpha), d0, d1, d2, 0.0, 0.0, 0.0);
    }

    static void updateNeighbors(BlockState state, Level world, BlockPos pos) {
        world.updateNeighborsAt(pos, state.m_60734_());
        world.updateNeighborsAt(pos.relative(m_53200_(state).getOpposite()), state.m_60734_());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Blocks.LEVER.m_5940_(state, worldIn, pos, context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder.add(f_54117_, f_53179_));
    }

    @Override
    public Class<AnalogLeverBlockEntity> getBlockEntityClass() {
        return AnalogLeverBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends AnalogLeverBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends AnalogLeverBlockEntity>) AllBlockEntityTypes.ANALOG_LEVER.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}