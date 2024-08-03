package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.moonlight.core.misc.IExtendedItem;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BookPileBlockTile;
import net.mehvahdjukaar.supplementaries.common.events.overrides.SuppAdditionalPlacement;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BookPileBlock extends WaterBlock implements EntityBlock {

    private static final VoxelShape SHAPE_1 = Block.box(3.0, 0.0, 3.0, 13.0, 4.0, 13.0);

    private static final VoxelShape SHAPE_2 = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);

    private static final VoxelShape SHAPE_3 = Block.box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0);

    private static final VoxelShape SHAPE_4 = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);

    public static final IntegerProperty BOOKS = ModBlockProperties.BOOKS;

    public BookPileBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(BOOKS, 1));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (world.getBlockEntity(pos) instanceof BookPileBlockTile tile) {
            ItemStack copy = stack.copy();
            copy.setCount(1);
            tile.m_6836_((Integer) state.m_61143_(BOOKS) - 1, copy);
        }
    }

    public boolean isAcceptedItem(Item i) {
        if (((IExtendedItem) i).moonlight$getAdditionalBehavior() instanceof SuppAdditionalPlacement sp) {
            Block b = sp.getPlacedBlock();
            return !CommonConfigs.Tweaks.MIXED_BOOKS.get() ? b == this : b == ModRegistry.BOOK_PILE.get() || b == ModRegistry.BOOK_PILE_H.get();
        } else {
            return false;
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if ((Integer) state.m_61143_(BOOKS) < 4) {
            Item item = context.m_43722_().getItem();
            if (this.isAcceptedItem(item)) {
                return true;
            }
        }
        return super.m_6864_(state, context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BOOKS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos());
        return blockstate.m_60734_() instanceof BookPileBlock ? (BlockState) blockstate.m_61124_(BOOKS, (Integer) blockstate.m_61143_(BOOKS) + 1) : super.getStateForPlacement(context);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.m_8055_(pos.below()).m_280296_();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BookPileBlockTile(pPos, pState, false);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            if (world.getBlockEntity(pos) instanceof BookPileBlockTile tile) {
                Containers.dropContents(world, pos, tile);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.m_6810_(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof BookPileBlockTile tile) {
            tile.m_8020_((Integer) state.m_61143_(BOOKS) - 1);
        }
        return Items.BOOK.getDefaultInstance();
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return world.getBlockEntity(pos) instanceof BookPileBlockTile tile ? tile.m_8020_(this.getBookIndex(state, pos, target.getLocation())) : Items.BOOK.getDefaultInstance();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch(state.m_61143_(BOOKS)) {
            case 2 ->
                SHAPE_2;
            case 3 ->
                SHAPE_3;
            case 4 ->
                SHAPE_4;
            default ->
                SHAPE_1;
        };
    }

    public float getEnchantPowerBonus(BlockState state, LevelReader world, BlockPos pos) {
        return world.m_7702_(pos) instanceof BookPileBlockTile tile ? tile.getEnchantPower() : 0.0F;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return player.isSecondaryUseActive() && level.getBlockEntity(pos) instanceof BookPileBlockTile tile && player.m_21120_(hand).isEmpty() ? tile.interact(player, hand, this.getBookIndex(state, pos, hit.m_82450_())) : InteractionResult.PASS;
    }

    protected int getBookIndex(BlockState state, BlockPos pos, Vec3 location) {
        double f = 5.0 * (location.y - (double) pos.m_123342_()) / SHAPE_4.bounds().maxY;
        return Mth.clamp((int) f, 0, (Integer) state.m_61143_(BOOKS) - 1);
    }
}