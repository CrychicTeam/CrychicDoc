package net.mehvahdjukaar.moonlight.api.block;

import java.util.UUID;
import java.util.stream.IntStream;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class ItemDisplayTile extends RandomizableContainerBlockEntity implements WorldlyContainer, IOwnerProtected {

    @Nullable
    private UUID owner = null;

    private NonNullList<ItemStack> stacks;

    protected ItemDisplayTile(BlockEntityType type, BlockPos pos, BlockState state) {
        this(type, pos, state, 1);
    }

    protected ItemDisplayTile(BlockEntityType type, BlockPos pos, BlockState state, int slots) {
        super(type, pos, state);
        this.stacks = NonNullList.withSize(slots, ItemStack.EMPTY);
    }

    @Override
    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }

    @Nullable
    @Override
    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public void setChanged() {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            this.updateTileOnInventoryChanged();
            if (this.needsToUpdateClientWhenChanged()) {
                this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
            }
            super.m_6596_();
        }
    }

    public void updateTileOnInventoryChanged() {
    }

    public boolean needsToUpdateClientWhenChanged() {
        return true;
    }

    public void updateClientVisualsOnLoad() {
    }

    public ItemStack getDisplayedItem() {
        return this.m_8020_(0);
    }

    public void setDisplayedItem(ItemStack stack) {
        this.m_6836_(0, stack);
    }

    public InteractionResult interact(Player player, InteractionHand handIn) {
        return this.interact(player, handIn, 0);
    }

    public InteractionResult interact(Player player, InteractionHand handIn, int slot) {
        if (!this.isAccessibleBy(player)) {
            player.displayClientMessage(Component.translatable("container.isLocked", ""), true);
        } else if (handIn == InteractionHand.MAIN_HAND) {
            ItemStack handItem = player.m_21120_(handIn);
            if (handItem.isEmpty()) {
                ItemStack it = this.m_8016_(slot);
                if (!it.isEmpty()) {
                    this.onItemRemoved(player, it, slot);
                    if (!this.f_58857_.isClientSide()) {
                        player.m_21008_(handIn, it);
                        this.setChanged();
                    } else {
                        this.updateClientVisualsOnLoad();
                    }
                    return InteractionResult.sidedSuccess(this.f_58857_.isClientSide);
                }
            } else if (!handItem.isEmpty() && this.canPlaceItem(slot, handItem)) {
                ItemStack it = handItem.copy();
                it.setCount(1);
                this.m_6836_(slot, it);
                if (!player.isCreative()) {
                    handItem.shrink(1);
                }
                this.onItemAdded(player, it, slot);
                if (!this.f_58857_.isClientSide()) {
                    this.f_58857_.playSound(null, this.f_58858_, this.getAddItemSound(), SoundSource.BLOCKS, 1.0F, this.f_58857_.random.nextFloat() * 0.1F + 0.95F);
                } else {
                    this.updateClientVisualsOnLoad();
                }
                return InteractionResult.sidedSuccess(this.f_58857_.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    public void onItemRemoved(Player player, ItemStack stack, int slot) {
        this.f_58857_.m_220407_(GameEvent.BLOCK_CHANGE, this.f_58858_, GameEvent.Context.of(player, this.m_58900_()));
    }

    public void onItemAdded(Player player, ItemStack stack, int slot) {
        this.f_58857_.m_220407_(GameEvent.BLOCK_CHANGE, this.f_58858_, GameEvent.Context.of(player, this.m_58900_()));
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, this.f_58858_, stack);
            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        }
    }

    public SoundEvent getAddItemSound() {
        return SoundEvents.ITEM_FRAME_ADD_ITEM;
    }

    @Override
    public void load(CompoundTag compound) {
        super.m_142466_(compound);
        if (!this.m_59631_(compound)) {
            this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        }
        ContainerHelper.loadAllItems(compound, this.stacks);
        if (this.f_58857_ != null) {
            if (this.f_58857_.isClientSide) {
                this.updateClientVisualsOnLoad();
            } else {
                this.updateTileOnInventoryChanged();
            }
        }
        this.loadOwner(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.m_183515_(compound);
        if (!this.m_59634_(compound)) {
            ContainerHelper.saveAllItems(compound, this.stacks);
        }
        this.saveOwner(compound);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Override
    public int getContainerSize() {
        return this.stacks.size();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Deprecated(forRemoval = true)
    @Internal
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory player) {
        return ChestMenu.threeRows(id, player, this);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    public void setItems(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return this.m_7983_();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return IntStream.range(0, this.getContainerSize()).toArray();
    }
}