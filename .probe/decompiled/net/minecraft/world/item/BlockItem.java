package net.minecraft.world.item;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;

public class BlockItem extends Item {

    public static final String BLOCK_ENTITY_TAG = "BlockEntityTag";

    public static final String BLOCK_STATE_TAG = "BlockStateTag";

    @Deprecated
    private final Block block;

    public BlockItem(Block block0, Item.Properties itemProperties1) {
        super(itemProperties1);
        this.block = block0;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        InteractionResult $$1 = this.place(new BlockPlaceContext(useOnContext0));
        if (!$$1.consumesAction() && this.m_41472_()) {
            InteractionResult $$2 = this.m_7203_(useOnContext0.getLevel(), useOnContext0.getPlayer(), useOnContext0.getHand()).getResult();
            return $$2 == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : $$2;
        } else {
            return $$1;
        }
    }

    public InteractionResult place(BlockPlaceContext blockPlaceContext0) {
        if (!this.getBlock().m_245993_(blockPlaceContext0.m_43725_().m_246046_())) {
            return InteractionResult.FAIL;
        } else if (!blockPlaceContext0.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext $$1 = this.updatePlacementContext(blockPlaceContext0);
            if ($$1 == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState $$2 = this.getPlacementState($$1);
                if ($$2 == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock($$1, $$2)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos $$3 = $$1.getClickedPos();
                    Level $$4 = $$1.m_43725_();
                    Player $$5 = $$1.m_43723_();
                    ItemStack $$6 = $$1.m_43722_();
                    BlockState $$7 = $$4.getBlockState($$3);
                    if ($$7.m_60713_($$2.m_60734_())) {
                        $$7 = this.updateBlockStateFromTag($$3, $$4, $$6, $$7);
                        this.updateCustomBlockEntityTag($$3, $$4, $$5, $$6, $$7);
                        $$7.m_60734_().setPlacedBy($$4, $$3, $$7, $$5, $$6);
                        if ($$5 instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) $$5, $$3, $$6);
                        }
                    }
                    SoundType $$8 = $$7.m_60827_();
                    $$4.playSound($$5, $$3, this.getPlaceSound($$7), SoundSource.BLOCKS, ($$8.getVolume() + 1.0F) / 2.0F, $$8.getPitch() * 0.8F);
                    $$4.m_220407_(GameEvent.BLOCK_PLACE, $$3, GameEvent.Context.of($$5, $$7));
                    if ($$5 == null || !$$5.getAbilities().instabuild) {
                        $$6.shrink(1);
                    }
                    return InteractionResult.sidedSuccess($$4.isClientSide);
                }
            }
        }
    }

    protected SoundEvent getPlaceSound(BlockState blockState0) {
        return blockState0.m_60827_().getPlaceSound();
    }

    @Nullable
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext blockPlaceContext0) {
        return blockPlaceContext0;
    }

    protected boolean updateCustomBlockEntityTag(BlockPos blockPos0, Level level1, @Nullable Player player2, ItemStack itemStack3, BlockState blockState4) {
        return updateCustomBlockEntityTag(level1, player2, blockPos0, itemStack3);
    }

    @Nullable
    protected BlockState getPlacementState(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = this.getBlock().getStateForPlacement(blockPlaceContext0);
        return $$1 != null && this.canPlace(blockPlaceContext0, $$1) ? $$1 : null;
    }

    private BlockState updateBlockStateFromTag(BlockPos blockPos0, Level level1, ItemStack itemStack2, BlockState blockState3) {
        BlockState $$4 = blockState3;
        CompoundTag $$5 = itemStack2.getTag();
        if ($$5 != null) {
            CompoundTag $$6 = $$5.getCompound("BlockStateTag");
            StateDefinition<Block, BlockState> $$7 = blockState3.m_60734_().getStateDefinition();
            for (String $$8 : $$6.getAllKeys()) {
                Property<?> $$9 = $$7.getProperty($$8);
                if ($$9 != null) {
                    String $$10 = $$6.get($$8).getAsString();
                    $$4 = updateState($$4, $$9, $$10);
                }
            }
        }
        if ($$4 != blockState3) {
            level1.setBlock(blockPos0, $$4, 2);
        }
        return $$4;
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState blockState0, Property<T> propertyT1, String string2) {
        return (BlockState) propertyT1.getValue(string2).map(p_40592_ -> (BlockState) blockState0.m_61124_(propertyT1, p_40592_)).orElse(blockState0);
    }

    protected boolean canPlace(BlockPlaceContext blockPlaceContext0, BlockState blockState1) {
        Player $$2 = blockPlaceContext0.m_43723_();
        CollisionContext $$3 = $$2 == null ? CollisionContext.empty() : CollisionContext.of($$2);
        return (!this.mustSurvive() || blockState1.m_60710_(blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos())) && blockPlaceContext0.m_43725_().m_45752_(blockState1, blockPlaceContext0.getClickedPos(), $$3);
    }

    protected boolean mustSurvive() {
        return true;
    }

    protected boolean placeBlock(BlockPlaceContext blockPlaceContext0, BlockState blockState1) {
        return blockPlaceContext0.m_43725_().setBlock(blockPlaceContext0.getClickedPos(), blockState1, 11);
    }

    public static boolean updateCustomBlockEntityTag(Level level0, @Nullable Player player1, BlockPos blockPos2, ItemStack itemStack3) {
        MinecraftServer $$4 = level0.getServer();
        if ($$4 == null) {
            return false;
        } else {
            CompoundTag $$5 = getBlockEntityData(itemStack3);
            if ($$5 != null) {
                BlockEntity $$6 = level0.getBlockEntity(blockPos2);
                if ($$6 != null) {
                    if (!level0.isClientSide && $$6.onlyOpCanSetNbt() && (player1 == null || !player1.canUseGameMasterBlocks())) {
                        return false;
                    }
                    CompoundTag $$7 = $$6.saveWithoutMetadata();
                    CompoundTag $$8 = $$7.copy();
                    $$7.merge($$5);
                    if (!$$7.equals($$8)) {
                        $$6.load($$7);
                        $$6.setChanged();
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public String getDescriptionId() {
        return this.getBlock().getDescriptionId();
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        super.appendHoverText(itemStack0, level1, listComponent2, tooltipFlag3);
        this.getBlock().appendHoverText(itemStack0, level1, listComponent2, tooltipFlag3);
    }

    public Block getBlock() {
        return this.block;
    }

    public void registerBlocks(Map<Block, Item> mapBlockItem0, Item item1) {
        mapBlockItem0.put(this.getBlock(), item1);
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return !(this.block instanceof ShulkerBoxBlock);
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity0) {
        if (this.block instanceof ShulkerBoxBlock) {
            ItemStack $$1 = itemEntity0.getItem();
            CompoundTag $$2 = getBlockEntityData($$1);
            if ($$2 != null && $$2.contains("Items", 9)) {
                ListTag $$3 = $$2.getList("Items", 10);
                ItemUtils.onContainerDestroyed(itemEntity0, $$3.stream().map(CompoundTag.class::cast).map(ItemStack::m_41712_));
            }
        }
    }

    @Nullable
    public static CompoundTag getBlockEntityData(ItemStack itemStack0) {
        return itemStack0.getTagElement("BlockEntityTag");
    }

    public static void setBlockEntityData(ItemStack itemStack0, BlockEntityType<?> blockEntityType1, CompoundTag compoundTag2) {
        if (compoundTag2.isEmpty()) {
            itemStack0.removeTagKey("BlockEntityTag");
        } else {
            BlockEntity.addEntityType(compoundTag2, blockEntityType1);
            itemStack0.addTagElement("BlockEntityTag", compoundTag2);
        }
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.getBlock().m_245183_();
    }
}