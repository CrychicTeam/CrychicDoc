package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedFallingBlockEntity;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.common.block.IRopeConnection;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SackBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SackBlock extends FallingBlock implements EntityBlock, SimpleWaterloggedBlock {

    public static final List<Block> SACK_BLOCKS = new ArrayList();

    public static final VoxelShape SHAPE_CLOSED = Shapes.or(Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0), Block.box(6.0, 12.0, 6.0, 10.0, 13.0, 10.0), Block.box(5.0, 13.0, 5.0, 11.0, 16.0, 11.0));

    public static final VoxelShape SHAPE_OPEN = Shapes.or(Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0), Block.box(6.0, 12.0, 6.0, 10.0, 13.0, 10.0), Block.box(3.0, 13.0, 3.0, 13.0, 14.0, 13.0));

    public static final ResourceLocation CONTENTS = new ResourceLocation("contents");

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public SackBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(OPEN, false)).m_61124_(WATERLOGGED, false));
        SACK_BLOCKS.add(this);
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter reader, BlockPos pos) {
        return 12226410;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.m_60734_() != oldState.m_60734_()) {
            worldIn.m_186460_(pos, this, this.m_7198_());
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(OPEN, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return (BlockState) this.m_49966_().m_61124_(WATERLOGGED, flag);
    }

    public static boolean canFall(BlockPos pos, LevelAccessor world) {
        return (world.m_46859_(pos.below()) || m_53241_(world.m_8055_(pos.below()))) && pos.m_123342_() >= world.m_141937_() && !IRopeConnection.isSupportingCeiling(pos.above(), world);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (level.m_7702_(pos) instanceof SackBlockTile tile) {
            tile.recheckOpen();
            if (canFall(pos, level)) {
                ImprovedFallingBlockEntity entity = ImprovedFallingBlockEntity.fall((EntityType<? extends FallingBlockEntity>) ModEntities.FALLING_SACK.get(), level, pos, state, true);
                entity.f_31944_ = tile.m_187482_();
                float power = (float) this.getAnalogOutputSignal(state, level, pos) / 15.0F;
                entity.m_149656_(1.0F + power * 5.0F, 40);
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SackBlockTile(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (player.isSpectator()) {
            return InteractionResult.CONSUME;
        } else if (worldIn.getBlockEntity(pos) instanceof SackBlockTile tile) {
            PlatHelper.openCustomMenu((ServerPlayer) player, tile, p -> {
                p.writeBoolean(true);
                p.writeBlockPos(pos);
                p.writeInt(tile.getContainerSize());
            });
            PiglinAi.angerNearbyPiglins(player, true);
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (worldIn.getBlockEntity(pos) instanceof SackBlockTile tile) {
            if (!worldIn.isClientSide && player.isCreative()) {
                ItemStack itemstack = new ItemStack(this);
                saveTileToItem(tile, itemstack);
                ItemEntity itementity = new ItemEntity(worldIn, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, itemstack);
                itementity.setDefaultPickUpDelay();
                worldIn.m_7967_(itementity);
            } else {
                tile.m_59640_(player);
            }
        }
        super.m_5707_(worldIn, pos, state, player);
    }

    private static void saveTileToItem(SackBlockTile tile, ItemStack itemstack) {
        CompoundTag compoundTag = tile.m_187482_();
        if (!compoundTag.isEmpty()) {
            itemstack.addTagElement("BlockEntityTag", compoundTag);
        }
        if (tile.m_8077_()) {
            itemstack.setHoverName(tile.m_7770_());
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof SackBlockTile tile) {
            builder = builder.withDynamicDrop(CONTENTS, context -> {
                for (int i = 0; i < tile.getContainerSize(); i++) {
                    context.accept(tile.m_8020_(i));
                }
            });
        }
        return super.m_49635_(state, builder);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack itemstack = super.m_7397_(level, pos, state);
        if (level.getBlockEntity(pos) instanceof SackBlockTile tile) {
            saveTileToItem(tile, itemstack);
        }
        return itemstack;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName() && worldIn.getBlockEntity(pos) instanceof SackBlockTile tile) {
            tile.m_58638_(stack.getHoverName());
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.m_61143_(OPEN) ? SHAPE_OPEN : SHAPE_CLOSED;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.m_60713_(newState.m_60734_())) {
            worldIn.updateNeighbourForOutputSignal(pos, state.m_60734_());
            super.m_6810_(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return worldIn.getBlockEntity(pos) instanceof Container tile ? AbstractContainerMenu.getRedstoneSignalFromContainer(tile) : 0;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        return worldIn.getBlockEntity(pos) instanceof MenuProvider menuProvider ? menuProvider : null;
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState state1, FallingBlockEntity blockEntity) {
        super.m_48792_(level, pos, state, state1, blockEntity);
        if (!blockEntity.m_20067_()) {
            level.playSound(null, pos, state.m_60827_().getPlaceSound(), SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }
        level.m_186460_(pos, this, this.m_7198_());
    }
}