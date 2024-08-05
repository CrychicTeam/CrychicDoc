package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.Collections;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.block.IColored;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.AbstractPresentBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.PresentBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPresentBlock extends WaterBlock implements EntityBlock, IColored {

    private final DyeColor color;

    public static final VoxelShape SHAPE_LID = Block.box(1.0, 10.0, 1.0, 15.0, 14.0, 15.0);

    public static final VoxelShape SHAPE_OPEN = Block.box(2.0, 0.0, 2.0, 14.0, 10.0, 14.0);

    public static final VoxelShape SHAPE_CLOSED = Shapes.or(SHAPE_OPEN, SHAPE_LID);

    public static final BooleanProperty PACKED = ModBlockProperties.PACKED;

    protected AbstractPresentBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = color;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(PACKED, false));
    }

    @Nullable
    @Override
    public DyeColor getColor() {
        return this.color;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, PACKED);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        return level.getBlockEntity(pos) instanceof AbstractPresentBlockTile tile ? tile.interact(level, pos, state, player) : InteractionResult.PASS;
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (worldIn.getBlockEntity(pos) instanceof AbstractPresentBlockTile tile) {
            if (!worldIn.isClientSide && player.isCreative() && !tile.m_7983_()) {
                ItemStack itemstack = tile.getPresentItem(this);
                ItemEntity itementity = new ItemEntity(worldIn, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, itemstack);
                itementity.setDefaultPickUpDelay();
                worldIn.m_7967_(itementity);
            } else {
                tile.m_59640_(player);
            }
        }
        super.m_5707_(worldIn, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof AbstractPresentBlockTile tile) {
            ItemStack itemstack = tile.getPresentItem(this);
            return Collections.singletonList(itemstack);
        } else {
            return super.m_49635_(state, builder);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack itemstack = super.m_7397_(level, pos, state);
        return level.getBlockEntity(pos) instanceof AbstractPresentBlockTile tile ? tile.getPresentItem(this) : itemstack;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasCustomHoverName() && worldIn.getBlockEntity(pos) instanceof AbstractPresentBlockTile tile) {
            tile.m_58638_(stack.getHoverName());
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.m_61143_(PACKED) ? SHAPE_CLOSED : SHAPE_OPEN;
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.m_60713_(newState.m_60734_())) {
            worldIn.updateNeighbourForOutputSignal(pos, state.m_60734_());
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(worldIn.getBlockEntity(pos));
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        return worldIn.getBlockEntity(pos) instanceof MenuProvider menuProvider ? menuProvider : null;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        CompoundTag tag = context.m_43722_().getTag();
        if (tag != null && tag.contains("BlockEntityTag")) {
            CompoundTag t = tag.getCompound("BlockEntityTag");
            if (t.contains("Items")) {
                state = (BlockState) state.m_61124_(PACKED, true);
            }
        }
        return state;
    }

    @Override
    public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource rand) {
        if (serverLevel.m_7702_(pos) instanceof PresentBlockTile tile) {
            tile.recheckOpen();
        }
    }
}