package net.minecraft.world.level.block;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ChiseledBookShelfBlock extends BaseEntityBlock {

    private static final int MAX_BOOKS_IN_STORAGE = 6;

    public static final int BOOKS_PER_ROW = 3;

    public static final List<BooleanProperty> SLOT_OCCUPIED_PROPERTIES = List.of(BlockStateProperties.CHISELED_BOOKSHELF_SLOT_0_OCCUPIED, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_1_OCCUPIED, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_2_OCCUPIED, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_3_OCCUPIED, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_4_OCCUPIED, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_5_OCCUPIED);

    public ChiseledBookShelfBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        BlockState $$1 = (BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HorizontalDirectionalBlock.FACING, Direction.NORTH);
        for (BooleanProperty $$2 : SLOT_OCCUPIED_PROPERTIES) {
            $$1 = (BlockState) $$1.m_61124_($$2, false);
        }
        this.m_49959_($$1);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.getBlockEntity(blockPos2) instanceof ChiseledBookShelfBlockEntity $$6) {
            Optional<Vec2> $$8 = getRelativeHitCoordinatesForBlockFace(blockHitResult5, (Direction) blockState0.m_61143_(HorizontalDirectionalBlock.FACING));
            if ($$8.isEmpty()) {
                return InteractionResult.PASS;
            } else {
                int $$9 = getHitSlot((Vec2) $$8.get());
                if ((Boolean) blockState0.m_61143_((Property) SLOT_OCCUPIED_PROPERTIES.get($$9))) {
                    removeBook(level1, blockPos2, player3, $$6, $$9);
                    return InteractionResult.sidedSuccess(level1.isClientSide);
                } else {
                    ItemStack $$10 = player3.m_21120_(interactionHand4);
                    if ($$10.is(ItemTags.BOOKSHELF_BOOKS)) {
                        addBook(level1, blockPos2, player3, $$6, $$10, $$9);
                        return InteractionResult.sidedSuccess(level1.isClientSide);
                    } else {
                        return InteractionResult.CONSUME;
                    }
                }
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    private static Optional<Vec2> getRelativeHitCoordinatesForBlockFace(BlockHitResult blockHitResult0, Direction direction1) {
        Direction $$2 = blockHitResult0.getDirection();
        if (direction1 != $$2) {
            return Optional.empty();
        } else {
            BlockPos $$3 = blockHitResult0.getBlockPos().relative($$2);
            Vec3 $$4 = blockHitResult0.m_82450_().subtract((double) $$3.m_123341_(), (double) $$3.m_123342_(), (double) $$3.m_123343_());
            double $$5 = $$4.x();
            double $$6 = $$4.y();
            double $$7 = $$4.z();
            return switch($$2) {
                case NORTH ->
                    Optional.of(new Vec2((float) (1.0 - $$5), (float) $$6));
                case SOUTH ->
                    Optional.of(new Vec2((float) $$5, (float) $$6));
                case WEST ->
                    Optional.of(new Vec2((float) $$7, (float) $$6));
                case EAST ->
                    Optional.of(new Vec2((float) (1.0 - $$7), (float) $$6));
                case DOWN, UP ->
                    Optional.empty();
            };
        }
    }

    private static int getHitSlot(Vec2 vec0) {
        int $$1 = vec0.y >= 0.5F ? 0 : 1;
        int $$2 = getSection(vec0.x);
        return $$2 + $$1 * 3;
    }

    private static int getSection(float float0) {
        float $$1 = 0.0625F;
        float $$2 = 0.375F;
        if (float0 < 0.375F) {
            return 0;
        } else {
            float $$3 = 0.6875F;
            return float0 < 0.6875F ? 1 : 2;
        }
    }

    private static void addBook(Level level0, BlockPos blockPos1, Player player2, ChiseledBookShelfBlockEntity chiseledBookShelfBlockEntity3, ItemStack itemStack4, int int5) {
        if (!level0.isClientSide) {
            player2.awardStat(Stats.ITEM_USED.get(itemStack4.getItem()));
            SoundEvent $$6 = itemStack4.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_INSERT_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_INSERT;
            chiseledBookShelfBlockEntity3.setItem(int5, itemStack4.split(1));
            level0.playSound(null, blockPos1, $$6, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (player2.isCreative()) {
                itemStack4.grow(1);
            }
            level0.m_142346_(player2, GameEvent.BLOCK_CHANGE, blockPos1);
        }
    }

    private static void removeBook(Level level0, BlockPos blockPos1, Player player2, ChiseledBookShelfBlockEntity chiseledBookShelfBlockEntity3, int int4) {
        if (!level0.isClientSide) {
            ItemStack $$5 = chiseledBookShelfBlockEntity3.removeItem(int4, 1);
            SoundEvent $$6 = $$5.is(Items.ENCHANTED_BOOK) ? SoundEvents.CHISELED_BOOKSHELF_PICKUP_ENCHANTED : SoundEvents.CHISELED_BOOKSHELF_PICKUP;
            level0.playSound(null, blockPos1, $$6, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!player2.getInventory().add($$5)) {
                player2.drop($$5, false);
            }
            level0.m_142346_(player2, GameEvent.BLOCK_CHANGE, blockPos1);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new ChiseledBookShelfBlockEntity(blockPos0, blockState1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(HorizontalDirectionalBlock.FACING);
        SLOT_OCCUPIED_PROPERTIES.forEach(p_261456_ -> stateDefinitionBuilderBlockBlockState0.add(p_261456_));
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            if (level1.getBlockEntity(blockPos2) instanceof ChiseledBookShelfBlockEntity $$6 && !$$6.isEmpty()) {
                for (int $$7 = 0; $$7 < 6; $$7++) {
                    ItemStack $$8 = $$6.getItem($$7);
                    if (!$$8.isEmpty()) {
                        Containers.dropItemStack(level1, (double) blockPos2.m_123341_(), (double) blockPos2.m_123342_(), (double) blockPos2.m_123343_(), $$8);
                    }
                }
                $$6.clearContent();
                level1.updateNeighbourForOutputSignal(blockPos2, this);
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(HorizontalDirectionalBlock.FACING, blockPlaceContext0.m_8125_().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(HorizontalDirectionalBlock.FACING, rotation1.rotate((Direction) blockState0.m_61143_(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        if (level1.isClientSide()) {
            return 0;
        } else {
            return level1.getBlockEntity(blockPos2) instanceof ChiseledBookShelfBlockEntity $$3 ? $$3.getLastInteractedSlot() + 1 : 0;
        }
    }
}