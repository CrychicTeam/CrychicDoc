package net.minecraft.world.level.block;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShulkerBoxBlock extends BaseEntityBlock {

    private static final float OPEN_AABB_SIZE = 1.0F;

    private static final VoxelShape UP_OPEN_AABB = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);

    private static final VoxelShape DOWN_OPEN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    private static final VoxelShape WES_OPEN_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);

    private static final VoxelShape EAST_OPEN_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    private static final VoxelShape NORTH_OPEN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);

    private static final VoxelShape SOUTH_OPEN_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);

    private static final Map<Direction, VoxelShape> OPEN_SHAPE_BY_DIRECTION = Util.make(Maps.newEnumMap(Direction.class), p_258974_ -> {
        p_258974_.put(Direction.NORTH, NORTH_OPEN_AABB);
        p_258974_.put(Direction.EAST, EAST_OPEN_AABB);
        p_258974_.put(Direction.SOUTH, SOUTH_OPEN_AABB);
        p_258974_.put(Direction.WEST, WES_OPEN_AABB);
        p_258974_.put(Direction.UP, UP_OPEN_AABB);
        p_258974_.put(Direction.DOWN, DOWN_OPEN_AABB);
    });

    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;

    public static final ResourceLocation CONTENTS = new ResourceLocation("contents");

    @Nullable
    private final DyeColor color;

    public ShulkerBoxBlock(@Nullable DyeColor dyeColor0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.color = dyeColor0;
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.UP));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new ShulkerBoxBlockEntity(this.color, blockPos0, blockState1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, BlockEntityType.SHULKER_BOX, ShulkerBoxBlockEntity::m_155672_);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (player3.isSpectator()) {
            return InteractionResult.CONSUME;
        } else if (level1.getBlockEntity(blockPos2) instanceof ShulkerBoxBlockEntity $$7) {
            if (canOpen(blockState0, level1, blockPos2, $$7)) {
                player3.openMenu($$7);
                player3.awardStat(Stats.OPEN_SHULKER_BOX);
                PiglinAi.angerNearbyPiglins(player3, true);
            }
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.PASS;
        }
    }

    private static boolean canOpen(BlockState blockState0, Level level1, BlockPos blockPos2, ShulkerBoxBlockEntity shulkerBoxBlockEntity3) {
        if (shulkerBoxBlockEntity3.getAnimationStatus() != ShulkerBoxBlockEntity.AnimationStatus.CLOSED) {
            return true;
        } else {
            AABB $$4 = Shulker.getProgressDeltaAabb((Direction) blockState0.m_61143_(FACING), 0.0F, 0.5F).move(blockPos2).deflate(1.0E-6);
            return level1.m_45772_($$4);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(FACING, blockPlaceContext0.m_43719_());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING);
    }

    @Override
    public void playerWillDestroy(Level level0, BlockPos blockPos1, BlockState blockState2, Player player3) {
        BlockEntity $$4 = level0.getBlockEntity(blockPos1);
        if ($$4 instanceof ShulkerBoxBlockEntity $$5) {
            if (!level0.isClientSide && player3.isCreative() && !$$5.m_7983_()) {
                ItemStack $$6 = getColoredItemStack(this.getColor());
                $$4.saveToItem($$6);
                if ($$5.m_8077_()) {
                    $$6.setHoverName($$5.m_7770_());
                }
                ItemEntity $$7 = new ItemEntity(level0, (double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 0.5, (double) blockPos1.m_123343_() + 0.5, $$6);
                $$7.setDefaultPickUpDelay();
                level0.m_7967_($$7);
            } else {
                $$5.m_59640_(player3);
            }
        }
        super.m_5707_(level0, blockPos1, blockState2, player3);
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState0, LootParams.Builder lootParamsBuilder1) {
        BlockEntity $$2 = lootParamsBuilder1.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if ($$2 instanceof ShulkerBoxBlockEntity $$3) {
            lootParamsBuilder1 = lootParamsBuilder1.withDynamicDrop(CONTENTS, p_56219_ -> {
                for (int $$2x = 0; $$2x < $$3.getContainerSize(); $$2x++) {
                    p_56219_.accept($$3.m_8020_($$2x));
                }
            });
        }
        return super.m_49635_(blockState0, lootParamsBuilder1);
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
        if (itemStack4.hasCustomHoverName()) {
            BlockEntity $$5 = level0.getBlockEntity(blockPos1);
            if ($$5 instanceof ShulkerBoxBlockEntity) {
                ((ShulkerBoxBlockEntity) $$5).m_58638_(itemStack4.getHoverName());
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            BlockEntity $$5 = level1.getBlockEntity(blockPos2);
            if ($$5 instanceof ShulkerBoxBlockEntity) {
                level1.updateNeighbourForOutputSignal(blockPos2, blockState0.m_60734_());
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable BlockGetter blockGetter1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        super.m_5871_(itemStack0, blockGetter1, listComponent2, tooltipFlag3);
        CompoundTag $$4 = BlockItem.getBlockEntityData(itemStack0);
        if ($$4 != null) {
            if ($$4.contains("LootTable", 8)) {
                listComponent2.add(Component.literal("???????"));
            }
            if ($$4.contains("Items", 9)) {
                NonNullList<ItemStack> $$5 = NonNullList.withSize(27, ItemStack.EMPTY);
                ContainerHelper.loadAllItems($$4, $$5);
                int $$6 = 0;
                int $$7 = 0;
                for (ItemStack $$8 : $$5) {
                    if (!$$8.isEmpty()) {
                        $$7++;
                        if ($$6 <= 4) {
                            $$6++;
                            MutableComponent $$9 = $$8.getHoverName().copy();
                            $$9.append(" x").append(String.valueOf($$8.getCount()));
                            listComponent2.add($$9);
                        }
                    }
                }
                if ($$7 - $$6 > 0) {
                    listComponent2.add(Component.translatable("container.shulkerBox.more", $$7 - $$6).withStyle(ChatFormatting.ITALIC));
                }
            }
        }
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        if (blockGetter1.getBlockEntity(blockPos2) instanceof ShulkerBoxBlockEntity $$4 && !$$4.isClosed()) {
            return (VoxelShape) OPEN_SHAPE_BY_DIRECTION.get(((Direction) blockState0.m_61143_(FACING)).getOpposite());
        }
        return Shapes.block();
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        BlockEntity $$4 = blockGetter1.getBlockEntity(blockPos2);
        return $$4 instanceof ShulkerBoxBlockEntity ? Shapes.create(((ShulkerBoxBlockEntity) $$4).getBoundingBox(blockState0)) : Shapes.block();
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return AbstractContainerMenu.getRedstoneSignalFromContainer((Container) level1.getBlockEntity(blockPos2));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        ItemStack $$3 = super.m_7397_(blockGetter0, blockPos1, blockState2);
        blockGetter0.getBlockEntity(blockPos1, BlockEntityType.SHULKER_BOX).ifPresent(p_187446_ -> p_187446_.m_187476_($$3));
        return $$3;
    }

    @Nullable
    public static DyeColor getColorFromItem(Item item0) {
        return getColorFromBlock(Block.byItem(item0));
    }

    @Nullable
    public static DyeColor getColorFromBlock(Block block0) {
        return block0 instanceof ShulkerBoxBlock ? ((ShulkerBoxBlock) block0).getColor() : null;
    }

    public static Block getBlockByColor(@Nullable DyeColor dyeColor0) {
        if (dyeColor0 == null) {
            return Blocks.SHULKER_BOX;
        } else {
            switch(dyeColor0) {
                case WHITE:
                    return Blocks.WHITE_SHULKER_BOX;
                case ORANGE:
                    return Blocks.ORANGE_SHULKER_BOX;
                case MAGENTA:
                    return Blocks.MAGENTA_SHULKER_BOX;
                case LIGHT_BLUE:
                    return Blocks.LIGHT_BLUE_SHULKER_BOX;
                case YELLOW:
                    return Blocks.YELLOW_SHULKER_BOX;
                case LIME:
                    return Blocks.LIME_SHULKER_BOX;
                case PINK:
                    return Blocks.PINK_SHULKER_BOX;
                case GRAY:
                    return Blocks.GRAY_SHULKER_BOX;
                case LIGHT_GRAY:
                    return Blocks.LIGHT_GRAY_SHULKER_BOX;
                case CYAN:
                    return Blocks.CYAN_SHULKER_BOX;
                case PURPLE:
                default:
                    return Blocks.PURPLE_SHULKER_BOX;
                case BLUE:
                    return Blocks.BLUE_SHULKER_BOX;
                case BROWN:
                    return Blocks.BROWN_SHULKER_BOX;
                case GREEN:
                    return Blocks.GREEN_SHULKER_BOX;
                case RED:
                    return Blocks.RED_SHULKER_BOX;
                case BLACK:
                    return Blocks.BLACK_SHULKER_BOX;
            }
        }
    }

    @Nullable
    public DyeColor getColor() {
        return this.color;
    }

    public static ItemStack getColoredItemStack(@Nullable DyeColor dyeColor0) {
        return new ItemStack(getBlockByColor(dyeColor0));
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }
}