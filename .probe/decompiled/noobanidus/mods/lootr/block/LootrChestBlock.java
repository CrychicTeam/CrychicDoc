package noobanidus.mods.lootr.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.block.entities.LootrChestBlockEntity;
import noobanidus.mods.lootr.init.ModBlockEntities;
import noobanidus.mods.lootr.util.ChestUtil;

public class LootrChestBlock extends ChestBlock {

    public LootrChestBlock(BlockBehaviour.Properties properties) {
        super(properties, ModBlockEntities.LOOTR_CHEST::get);
    }

    @Override
    public float getExplosionResistance() {
        return LootrAPI.getExplosionResistance(this, super.m_7325_());
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult trace) {
        if (player.m_6144_()) {
            ChestUtil.handleLootSneak(this, world, pos, player);
        } else if (!ChestBlock.isChestBlockedAt(world, pos)) {
            ChestUtil.handleLootChest(this, world, pos, player);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LootrChestBlockEntity(pos, state);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(f_51480_)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return stateIn;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return f_51485_;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.m_8125_().getOpposite();
        FluidState fluidstate = context.m_43725_().getFluidState(context.getClickedPos());
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(f_51478_, direction)).m_61124_(f_51479_, ChestType.SINGLE)).m_61124_(f_51480_, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(f_51480_) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public float getDestroyProgress(BlockState blockState0, Player player1, BlockGetter blockGetter2, BlockPos blockPos3) {
        return LootrAPI.getDestroyProgress(blockState0, player1, blockGetter2, blockPos3, super.m_5880_(blockState0, player1, blockGetter2, blockPos3));
    }

    @Override
    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
        return LootrAPI.getAnalogOutputSignal(pBlockState, pLevel, pPos, 0);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? LootrChestBlockEntity::lootrLidAnimateTick : null;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.m_7702_(pPos) instanceof LootrChestBlockEntity chest) {
            chest.recheckOpen();
        }
    }
}