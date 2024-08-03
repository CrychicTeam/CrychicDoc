package me.jellysquid.mods.lithium.mixin.block.hopper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import me.jellysquid.mods.lithium.api.inventory.LithiumCooldownReceivingInventory;
import me.jellysquid.mods.lithium.api.inventory.LithiumInventory;
import me.jellysquid.mods.lithium.common.block.entity.SleepingBlockEntity;
import me.jellysquid.mods.lithium.common.block.entity.inventory_change_tracking.InventoryChangeListener;
import me.jellysquid.mods.lithium.common.block.entity.inventory_change_tracking.InventoryChangeTracker;
import me.jellysquid.mods.lithium.common.block.entity.inventory_comparator_tracking.ComparatorTracker;
import me.jellysquid.mods.lithium.common.compat.fabric_transfer_api_v1.FabricTransferApiCompat;
import me.jellysquid.mods.lithium.common.entity.movement_tracker.SectionedEntityMovementListener;
import me.jellysquid.mods.lithium.common.entity.movement_tracker.SectionedInventoryEntityMovementTracker;
import me.jellysquid.mods.lithium.common.entity.movement_tracker.SectionedItemEntityMovementTracker;
import me.jellysquid.mods.lithium.common.hopper.BlockStateOnlyInventory;
import me.jellysquid.mods.lithium.common.hopper.HopperCachingState;
import me.jellysquid.mods.lithium.common.hopper.HopperHelper;
import me.jellysquid.mods.lithium.common.hopper.InventoryHelper;
import me.jellysquid.mods.lithium.common.hopper.LithiumStackList;
import me.jellysquid.mods.lithium.common.hopper.UpdateReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = { HopperBlockEntity.class }, priority = 950)
public abstract class HopperBlockEntityMixin extends BlockEntity implements Hopper, UpdateReceiver, LithiumInventory, InventoryChangeListener, SectionedEntityMovementListener {

    @Shadow
    private long tickedGameTime;

    private long myModCountAtLastInsert;

    private long myModCountAtLastExtract;

    private long myModCountAtLastItemCollect;

    private HopperCachingState.BlockInventory insertionMode = HopperCachingState.BlockInventory.UNKNOWN;

    private HopperCachingState.BlockInventory extractionMode = HopperCachingState.BlockInventory.UNKNOWN;

    @Nullable
    private Container insertBlockInventory;

    @Nullable
    private Container extractBlockInventory;

    @Nullable
    private LithiumInventory insertInventory;

    @Nullable
    private LithiumInventory extractInventory;

    @Nullable
    private LithiumStackList insertStackList;

    @Nullable
    private LithiumStackList extractStackList;

    private long insertStackListModCount;

    private long extractStackListModCount;

    private SectionedItemEntityMovementTracker<ItemEntity> collectItemEntityTracker;

    private boolean collectItemEntityTrackerWasEmpty;

    private AABB[] collectItemEntityBoxes;

    private long collectItemEntityAttemptTime;

    private SectionedInventoryEntityMovementTracker<Container> extractInventoryEntityTracker;

    private AABB extractInventoryEntityBox;

    private long extractInventoryEntityFailedSearchTime;

    private SectionedInventoryEntityMovementTracker<Container> insertInventoryEntityTracker;

    private AABB insertInventoryEntityBox;

    private long insertInventoryEntityFailedSearchTime;

    private boolean shouldCheckSleep;

    public HopperBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow
    @Nullable
    private static native Container m_155596_(Level var0, Hopper var1);

    @Shadow
    protected abstract boolean isOnCustomCooldown();

    @Redirect(method = { "extract(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/Hopper;)Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;getInputInventory(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/Hopper;)Lnet/minecraft/inventory/Inventory;"))
    private static Container getExtractInventory(Level world, Hopper hopper) {
        if (!(hopper instanceof HopperBlockEntityMixin hopperBlockEntity)) {
            return m_155596_(world, hopper);
        } else {
            Container blockInventory = hopperBlockEntity.getExtractBlockInventory(world);
            if (blockInventory != null) {
                return blockInventory;
            } else {
                if (hopperBlockEntity.extractInventoryEntityTracker == null) {
                    hopperBlockEntity.initExtractInventoryTracker(world);
                }
                if (hopperBlockEntity.extractInventoryEntityTracker.isUnchangedSince(hopperBlockEntity.extractInventoryEntityFailedSearchTime)) {
                    hopperBlockEntity.extractInventoryEntityFailedSearchTime = hopperBlockEntity.tickedGameTime;
                    return null;
                } else {
                    hopperBlockEntity.extractInventoryEntityFailedSearchTime = Long.MIN_VALUE;
                    hopperBlockEntity.shouldCheckSleep = false;
                    List<Container> inventoryEntities = hopperBlockEntity.extractInventoryEntityTracker.getEntities(hopperBlockEntity.extractInventoryEntityBox);
                    if (inventoryEntities.isEmpty()) {
                        hopperBlockEntity.extractInventoryEntityFailedSearchTime = hopperBlockEntity.tickedGameTime;
                        return null;
                    } else {
                        Container inventory = (Container) inventoryEntities.get(world.random.nextInt(inventoryEntities.size()));
                        if (inventory instanceof LithiumInventory optimizedInventory) {
                            LithiumStackList extractInventoryStackList = InventoryHelper.getLithiumStackList(optimizedInventory);
                            if (inventory != hopperBlockEntity.extractInventory || hopperBlockEntity.extractStackList != extractInventoryStackList) {
                                hopperBlockEntity.cacheExtractLithiumInventory(optimizedInventory);
                            }
                        }
                        return inventory;
                    }
                }
            }
        }
    }

    @Inject(cancellable = true, method = { "ejectItems" }, at = { @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/block/entity/HopperBlockEntity;isInventoryFull(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/util/math/Direction;)Z") }, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void lithiumInsert(Level world, BlockPos pos, BlockState hopperState, HopperBlockEntity hopper, CallbackInfoReturnable<Boolean> cir, Container insertInventory, Direction direction) {
        if (insertInventory != null && hopper instanceof HopperBlockEntity && !(hopper instanceof WorldlyContainer)) {
            HopperBlockEntityMixin hopperBlockEntity = (HopperBlockEntityMixin) hopper;
            LithiumStackList hopperStackList = InventoryHelper.getLithiumStackList(hopperBlockEntity);
            if (hopperBlockEntity.insertInventory == insertInventory && hopperStackList.getModCount() == hopperBlockEntity.myModCountAtLastInsert && hopperBlockEntity.insertStackList != null && hopperBlockEntity.insertStackList.getModCount() == hopperBlockEntity.insertStackListModCount) {
                cir.setReturnValue(false);
            } else {
                boolean insertInventoryWasEmptyHopperNotDisabled = insertInventory instanceof HopperBlockEntityMixin && !((HopperBlockEntityMixin) insertInventory).isOnCustomCooldown() && hopperBlockEntity.insertStackList != null && hopperBlockEntity.insertStackList.getOccupiedSlots() == 0;
                boolean insertInventoryHandlesModdedCooldown = ((LithiumCooldownReceivingInventory) insertInventory).canReceiveTransferCooldown() && hopperBlockEntity.insertStackList != null ? hopperBlockEntity.insertStackList.getOccupiedSlots() == 0 : insertInventory.isEmpty();
                if (hopperBlockEntity.insertInventory != insertInventory || hopperBlockEntity.insertStackList.getFullSlots() != hopperBlockEntity.insertStackList.size()) {
                    Direction fromDirection = ((Direction) hopperState.m_61143_(HopperBlock.FACING)).getOpposite();
                    int size = hopperStackList.size();
                    for (int i = 0; i < size; i++) {
                        ItemStack transferStack = hopperStackList.get(i);
                        if (!transferStack.isEmpty()) {
                            boolean transferSuccess = HopperHelper.tryMoveSingleItem(insertInventory, transferStack, fromDirection);
                            if (transferSuccess) {
                                if (insertInventoryWasEmptyHopperNotDisabled) {
                                    HopperBlockEntityMixin receivingHopper = (HopperBlockEntityMixin) insertInventory;
                                    int k = 8;
                                    if (receivingHopper.tickedGameTime >= hopperBlockEntity.tickedGameTime) {
                                        k = 7;
                                    }
                                    receivingHopper.setCooldown(k);
                                }
                                if (insertInventoryHandlesModdedCooldown) {
                                    ((LithiumCooldownReceivingInventory) insertInventory).setTransferCooldown(hopperBlockEntity.tickedGameTime);
                                }
                                insertInventory.setChanged();
                                cir.setReturnValue(true);
                                return;
                            }
                        }
                    }
                }
                hopperBlockEntity.myModCountAtLastInsert = hopperStackList.getModCount();
                if (hopperBlockEntity.insertStackList != null) {
                    hopperBlockEntity.insertStackListModCount = hopperBlockEntity.insertStackList.getModCount();
                }
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = { "extract(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/Hopper;)Z" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/util/math/Direction;DOWN:Lnet/minecraft/util/math/Direction;", shift = Shift.AFTER) }, cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void lithiumExtract(Level world, Hopper to, CallbackInfoReturnable<Boolean> cir, Boolean unUsed, Container from) {
        if (to instanceof HopperBlockEntityMixin hopperBlockEntity) {
            if (from == hopperBlockEntity.extractInventory && hopperBlockEntity.extractStackList != null) {
                LithiumStackList hopperStackList = InventoryHelper.getLithiumStackList(hopperBlockEntity);
                LithiumStackList fromStackList = hopperBlockEntity.extractStackList;
                if (hopperStackList.getModCount() == hopperBlockEntity.myModCountAtLastExtract && fromStackList.getModCount() == hopperBlockEntity.extractStackListModCount) {
                    if (!(from instanceof ComparatorTracker comparatorTracker) || comparatorTracker.hasAnyComparatorNearby()) {
                        fromStackList.runComparatorUpdatePatternOnFailedExtract(fromStackList, from);
                    }
                    cir.setReturnValue(false);
                } else {
                    int[] availableSlots = from instanceof WorldlyContainer ? ((WorldlyContainer) from).getSlotsForFace(Direction.DOWN) : null;
                    int fromSize = availableSlots != null ? availableSlots.length : from.getContainerSize();
                    for (int i = 0; i < fromSize; i++) {
                        int fromSlot = availableSlots != null ? availableSlots[i] : i;
                        ItemStack itemStack = fromStackList.get(fromSlot);
                        if (!itemStack.isEmpty() && m_271906_(to, from, itemStack, fromSlot, Direction.DOWN)) {
                            ItemStack takenItem = from.removeItem(fromSlot, 1);
                            assert !takenItem.isEmpty();
                            boolean transferSuccess = HopperHelper.tryMoveSingleItem(to, takenItem, null);
                            if (transferSuccess) {
                                to.m_6596_();
                                from.setChanged();
                                cir.setReturnValue(true);
                                return;
                            }
                            ItemStack restoredStack = fromStackList.get(fromSlot);
                            if (restoredStack.isEmpty()) {
                                restoredStack = takenItem;
                            } else {
                                restoredStack.grow(1);
                            }
                            from.setItem(fromSlot, restoredStack);
                        }
                    }
                    hopperBlockEntity.myModCountAtLastExtract = hopperStackList.getModCount();
                    if (fromStackList != null) {
                        hopperBlockEntity.extractStackListModCount = fromStackList.getModCount();
                    }
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Redirect(method = { "insertAndExtract(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/HopperBlockEntity;Ljava/util/function/BooleanSupplier;)Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;isFull()Z"))
    private static boolean lithiumHopperIsFull(HopperBlockEntity hopperBlockEntity) {
        LithiumStackList lithiumStackList = InventoryHelper.getLithiumStackList((HopperBlockEntityMixin) hopperBlockEntity);
        return lithiumStackList.getFullSlots() == lithiumStackList.size();
    }

    @Redirect(method = { "insertAndExtract(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/HopperBlockEntity;Ljava/util/function/BooleanSupplier;)Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;isEmpty()Z"))
    private static boolean lithiumHopperIsEmpty(HopperBlockEntity hopperBlockEntity) {
        LithiumStackList lithiumStackList = InventoryHelper.getLithiumStackList((HopperBlockEntityMixin) hopperBlockEntity);
        return lithiumStackList.getOccupiedSlots() == 0;
    }

    @Shadow
    protected abstract void setCooldown(int var1);

    @Shadow
    protected abstract boolean isOnCooldown();

    @Shadow
    private static native boolean m_271906_(Container var0, Container var1, ItemStack var2, int var3, Direction var4);

    @Override
    public void invalidateCacheOnNeighborUpdate(boolean fromAbove) {
        if (fromAbove) {
            if (this.extractionMode == HopperCachingState.BlockInventory.NO_BLOCK_INVENTORY || this.extractionMode == HopperCachingState.BlockInventory.BLOCK_STATE) {
                this.invalidateBlockExtractionData();
            }
        } else if (this.insertionMode == HopperCachingState.BlockInventory.NO_BLOCK_INVENTORY || this.insertionMode == HopperCachingState.BlockInventory.BLOCK_STATE) {
            this.invalidateBlockInsertionData();
        }
    }

    @Override
    public void invalidateCacheOnNeighborUpdate(Direction fromDirection) {
        boolean fromAbove = fromDirection == Direction.UP;
        if (fromAbove || this.m_58900_().m_61143_(HopperBlock.FACING) == fromDirection) {
            this.invalidateCacheOnNeighborUpdate(fromAbove);
        }
    }

    @Redirect(method = { "ejectItems" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;getOutputInventory(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/inventory/Inventory;"))
    private static Container nullify(Level world, BlockPos pos, BlockState state) {
        return null;
    }

    @ModifyVariable(method = { "ejectItems" }, at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/block/entity/HopperBlockEntity;getOutputInventory(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/inventory/Inventory;"))
    private static Container getLithiumOutputInventory(Container inventory, Level world, BlockPos pos, BlockState hopperState, HopperBlockEntity hopper) {
        HopperBlockEntityMixin hopperBlockEntity = (HopperBlockEntityMixin) hopper;
        return hopperBlockEntity.getInsertInventory(world, hopperState);
    }

    @Redirect(method = { "extract(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/Hopper;)Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;getInputItemEntities(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/Hopper;)Ljava/util/List;"))
    private static List<ItemEntity> lithiumGetInputItemEntities(Level world, Hopper hopper) {
        if (hopper instanceof HopperBlockEntityMixin hopperBlockEntity) {
            if (hopperBlockEntity.collectItemEntityTracker == null) {
                hopperBlockEntity.initCollectItemEntityTracker();
            }
            long modCount = InventoryHelper.getLithiumStackList(hopperBlockEntity).getModCount();
            if ((hopperBlockEntity.collectItemEntityTrackerWasEmpty || hopperBlockEntity.myModCountAtLastItemCollect == modCount) && hopperBlockEntity.collectItemEntityTracker.isUnchangedSince(hopperBlockEntity.collectItemEntityAttemptTime)) {
                hopperBlockEntity.collectItemEntityAttemptTime = hopperBlockEntity.tickedGameTime;
                return Collections.emptyList();
            } else {
                hopperBlockEntity.myModCountAtLastItemCollect = modCount;
                hopperBlockEntity.shouldCheckSleep = false;
                List<ItemEntity> itemEntities = hopperBlockEntity.collectItemEntityTracker.getEntities(hopperBlockEntity.collectItemEntityBoxes);
                hopperBlockEntity.collectItemEntityAttemptTime = hopperBlockEntity.tickedGameTime;
                hopperBlockEntity.collectItemEntityTrackerWasEmpty = itemEntities.isEmpty();
                return itemEntities;
            }
        } else {
            return HopperBlockEntity.getItemsAtAndAbove(world, hopper);
        }
    }

    private void cacheInsertBlockInventory(Container insertInventory) {
        assert !(insertInventory instanceof Entity);
        if (insertInventory instanceof LithiumInventory optimizedInventory) {
            this.cacheInsertLithiumInventory(optimizedInventory);
        } else {
            this.insertInventory = null;
            this.insertStackList = null;
            this.insertStackListModCount = 0L;
        }
        if (insertInventory instanceof BlockEntity || insertInventory instanceof CompoundContainer) {
            this.insertBlockInventory = insertInventory;
            if (insertInventory instanceof InventoryChangeTracker) {
                this.insertionMode = HopperCachingState.BlockInventory.REMOVAL_TRACKING_BLOCK_ENTITY;
                ((InventoryChangeTracker) insertInventory).listenForMajorInventoryChanges(this);
            } else {
                this.insertionMode = HopperCachingState.BlockInventory.BLOCK_ENTITY;
            }
        } else if (insertInventory == null) {
            this.insertBlockInventory = null;
            this.insertionMode = HopperCachingState.BlockInventory.NO_BLOCK_INVENTORY;
        } else {
            this.insertBlockInventory = insertInventory;
            this.insertionMode = insertInventory instanceof BlockStateOnlyInventory ? HopperCachingState.BlockInventory.BLOCK_STATE : HopperCachingState.BlockInventory.UNKNOWN;
        }
    }

    private void cacheInsertLithiumInventory(LithiumInventory optimizedInventory) {
        LithiumStackList insertInventoryStackList = InventoryHelper.getLithiumStackList(optimizedInventory);
        this.insertInventory = optimizedInventory;
        this.insertStackList = insertInventoryStackList;
        this.insertStackListModCount = insertInventoryStackList.getModCount() - 1L;
    }

    private void cacheExtractLithiumInventory(LithiumInventory optimizedInventory) {
        LithiumStackList extractInventoryStackList = InventoryHelper.getLithiumStackList(optimizedInventory);
        this.extractInventory = optimizedInventory;
        this.extractStackList = extractInventoryStackList;
        this.extractStackListModCount = extractInventoryStackList.getModCount() - 1L;
    }

    @Overwrite
    private static boolean isEmptyContainer(Container inv, Direction side) {
        int[] availableSlots = inv instanceof WorldlyContainer ? ((WorldlyContainer) inv).getSlotsForFace(side) : null;
        int fromSize = availableSlots != null ? availableSlots.length : inv.getContainerSize();
        for (int i = 0; i < fromSize; i++) {
            int slot = availableSlots != null ? availableSlots[i] : i;
            if (!inv.getItem(slot).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void cacheExtractBlockInventory(Container extractInventory) {
        assert !(extractInventory instanceof Entity);
        if (extractInventory instanceof LithiumInventory optimizedInventory) {
            this.cacheExtractLithiumInventory(optimizedInventory);
        } else {
            this.extractInventory = null;
            this.extractStackList = null;
            this.extractStackListModCount = 0L;
        }
        if (extractInventory instanceof BlockEntity || extractInventory instanceof CompoundContainer) {
            this.extractBlockInventory = extractInventory;
            if (extractInventory instanceof InventoryChangeTracker) {
                this.extractionMode = HopperCachingState.BlockInventory.REMOVAL_TRACKING_BLOCK_ENTITY;
                ((InventoryChangeTracker) extractInventory).listenForMajorInventoryChanges(this);
            } else {
                this.extractionMode = HopperCachingState.BlockInventory.BLOCK_ENTITY;
            }
        } else if (extractInventory == null) {
            this.extractBlockInventory = null;
            this.extractionMode = HopperCachingState.BlockInventory.NO_BLOCK_INVENTORY;
        } else {
            this.extractBlockInventory = extractInventory;
            this.extractionMode = extractInventory instanceof BlockStateOnlyInventory ? HopperCachingState.BlockInventory.BLOCK_STATE : HopperCachingState.BlockInventory.UNKNOWN;
        }
    }

    public Container getExtractBlockInventory(Level world) {
        Container blockInventory = this.extractBlockInventory;
        if (this.extractionMode == HopperCachingState.BlockInventory.NO_BLOCK_INVENTORY) {
            return null;
        } else if (this.extractionMode == HopperCachingState.BlockInventory.BLOCK_STATE) {
            return blockInventory;
        } else if (this.extractionMode == HopperCachingState.BlockInventory.REMOVAL_TRACKING_BLOCK_ENTITY) {
            return blockInventory;
        } else {
            if (this.extractionMode == HopperCachingState.BlockInventory.BLOCK_ENTITY) {
                BlockEntity blockEntity = (BlockEntity) Objects.requireNonNull(blockInventory);
                BlockPos pos = blockEntity.getBlockPos();
                BlockPos thisPos = this.m_58899_();
                if (!blockEntity.isRemoved() && pos.m_123341_() == thisPos.m_123341_() && pos.m_123342_() == thisPos.m_123342_() + 1 && pos.m_123343_() == thisPos.m_123343_()) {
                    LithiumInventory optimizedInventory = this.extractInventory;
                    if (this.extractInventory == null) {
                        return blockInventory;
                    }
                    LithiumStackList insertInventoryStackList = InventoryHelper.getLithiumStackList(optimizedInventory);
                    if (insertInventoryStackList == this.extractStackList) {
                        return optimizedInventory;
                    }
                    this.invalidateBlockExtractionData();
                }
            }
            blockInventory = HopperHelper.vanillaGetBlockInventory(world, this.m_58899_().above());
            blockInventory = HopperHelper.replaceDoubleInventory(blockInventory);
            this.cacheExtractBlockInventory(blockInventory);
            return blockInventory;
        }
    }

    public Container getInsertBlockInventory(Level world, BlockState hopperState) {
        Container blockInventory = this.insertBlockInventory;
        if (this.insertionMode == HopperCachingState.BlockInventory.NO_BLOCK_INVENTORY) {
            return null;
        } else if (this.insertionMode == HopperCachingState.BlockInventory.BLOCK_STATE) {
            return blockInventory;
        } else if (this.insertionMode == HopperCachingState.BlockInventory.REMOVAL_TRACKING_BLOCK_ENTITY) {
            return blockInventory;
        } else {
            if (this.insertionMode == HopperCachingState.BlockInventory.BLOCK_ENTITY) {
                BlockEntity blockEntity = (BlockEntity) Objects.requireNonNull(blockInventory);
                BlockPos pos = blockEntity.getBlockPos();
                Direction direction = (Direction) hopperState.m_61143_(HopperBlock.FACING);
                BlockPos transferPos = this.m_58899_().relative(direction);
                if (!blockEntity.isRemoved() && pos.equals(transferPos)) {
                    LithiumInventory optimizedInventory = this.insertInventory;
                    if (this.insertInventory == null) {
                        return blockInventory;
                    }
                    LithiumStackList insertInventoryStackList = InventoryHelper.getLithiumStackList(optimizedInventory);
                    if (insertInventoryStackList == this.insertStackList) {
                        return optimizedInventory;
                    }
                    this.invalidateBlockInsertionData();
                }
            }
            Direction direction = (Direction) hopperState.m_61143_(HopperBlock.FACING);
            blockInventory = HopperHelper.vanillaGetBlockInventory(world, this.m_58899_().relative(direction));
            blockInventory = HopperHelper.replaceDoubleInventory(blockInventory);
            this.cacheInsertBlockInventory(blockInventory);
            return blockInventory;
        }
    }

    public Container getInsertInventory(Level world, BlockState hopperState) {
        Container blockInventory = this.getInsertBlockInventory(world, hopperState);
        if (blockInventory != null) {
            return blockInventory;
        } else {
            if (this.insertInventoryEntityTracker == null) {
                this.initInsertInventoryTracker(world, hopperState);
            }
            if (this.insertInventoryEntityTracker.isUnchangedSince(this.insertInventoryEntityFailedSearchTime)) {
                this.insertInventoryEntityFailedSearchTime = this.tickedGameTime;
                return null;
            } else {
                this.insertInventoryEntityFailedSearchTime = Long.MIN_VALUE;
                this.shouldCheckSleep = false;
                List<Container> inventoryEntities = this.insertInventoryEntityTracker.getEntities(this.insertInventoryEntityBox);
                if (inventoryEntities.isEmpty()) {
                    this.insertInventoryEntityFailedSearchTime = this.tickedGameTime;
                    return null;
                } else {
                    Container inventory = (Container) inventoryEntities.get(world.random.nextInt(inventoryEntities.size()));
                    if (inventory instanceof LithiumInventory optimizedInventory) {
                        LithiumStackList insertInventoryStackList = InventoryHelper.getLithiumStackList(optimizedInventory);
                        if (inventory != this.insertInventory || this.insertStackList != insertInventoryStackList) {
                            this.cacheInsertLithiumInventory(optimizedInventory);
                        }
                    }
                    return inventory;
                }
            }
        }
    }

    private void initCollectItemEntityTracker() {
        assert this.f_58857_ instanceof ServerLevel;
        List<AABB> list = new ArrayList();
        AABB encompassingBox = null;
        for (AABB box : HopperHelper.getHopperPickupVolumeBoxes(this)) {
            AABB offsetBox = box.move((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_());
            list.add(offsetBox);
            if (encompassingBox == null) {
                encompassingBox = offsetBox;
            } else {
                encompassingBox = encompassingBox.minmax(offsetBox);
            }
        }
        list.add(encompassingBox);
        this.collectItemEntityBoxes = (AABB[]) list.toArray(new AABB[0]);
        this.collectItemEntityTracker = SectionedItemEntityMovementTracker.registerAt((ServerLevel) this.f_58857_, encompassingBox, ItemEntity.class);
        this.collectItemEntityAttemptTime = Long.MIN_VALUE;
    }

    private void initExtractInventoryTracker(Level world) {
        assert world instanceof ServerLevel;
        BlockPos pos = this.f_58858_.relative(Direction.UP);
        this.extractInventoryEntityBox = new AABB((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), (double) (pos.m_123341_() + 1), (double) (pos.m_123342_() + 1), (double) (pos.m_123343_() + 1));
        this.extractInventoryEntityTracker = SectionedInventoryEntityMovementTracker.registerAt((ServerLevel) this.f_58857_, this.extractInventoryEntityBox, Container.class);
        this.extractInventoryEntityFailedSearchTime = Long.MIN_VALUE;
    }

    private void initInsertInventoryTracker(Level world, BlockState hopperState) {
        assert world instanceof ServerLevel;
        Direction direction = (Direction) hopperState.m_61143_(HopperBlock.FACING);
        BlockPos pos = this.f_58858_.relative(direction);
        this.insertInventoryEntityBox = new AABB((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), (double) (pos.m_123341_() + 1), (double) (pos.m_123342_() + 1), (double) (pos.m_123343_() + 1));
        this.insertInventoryEntityTracker = SectionedInventoryEntityMovementTracker.registerAt((ServerLevel) this.f_58857_, this.insertInventoryEntityBox, Container.class);
        this.insertInventoryEntityFailedSearchTime = Long.MIN_VALUE;
    }

    @Intrinsic
    @Override
    public void setBlockState(BlockState state) {
        super.setBlockState(state);
    }

    @Inject(method = { "setCachedState(Lnet/minecraft/block/BlockState;)V" }, at = { @At("HEAD") })
    private void invalidateOnSetCachedState(BlockState state, CallbackInfo ci) {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide() && state.m_61143_(HopperBlock.FACING) != this.m_58900_().m_61143_(HopperBlock.FACING)) {
            this.invalidateCachedData();
        }
    }

    private void invalidateCachedData() {
        this.shouldCheckSleep = false;
        this.invalidateInsertionData();
        this.invalidateExtractionData();
    }

    private void invalidateInsertionData() {
        if (this.f_58857_ instanceof ServerLevel serverWorld && this.insertInventoryEntityTracker != null) {
            this.insertInventoryEntityTracker.unRegister(serverWorld);
            this.insertInventoryEntityTracker = null;
            this.insertInventoryEntityBox = null;
            this.insertInventoryEntityFailedSearchTime = 0L;
        }
        if (this.insertionMode == HopperCachingState.BlockInventory.REMOVAL_TRACKING_BLOCK_ENTITY) {
            assert this.insertBlockInventory != null;
            ((InventoryChangeTracker) this.insertBlockInventory).stopListenForMajorInventoryChanges(this);
        }
        this.invalidateBlockInsertionData();
    }

    private void invalidateBlockInsertionData() {
        this.insertionMode = HopperCachingState.BlockInventory.UNKNOWN;
        this.insertBlockInventory = null;
        this.insertInventory = null;
        this.insertStackList = null;
        this.insertStackListModCount = 0L;
        if (this instanceof SleepingBlockEntity sleepingBlockEntity) {
            sleepingBlockEntity.wakeUpNow();
        }
    }

    private void invalidateExtractionData() {
        if (this.f_58857_ instanceof ServerLevel serverWorld) {
            if (this.extractInventoryEntityTracker != null) {
                this.extractInventoryEntityTracker.unRegister(serverWorld);
                this.extractInventoryEntityTracker = null;
                this.extractInventoryEntityBox = null;
                this.extractInventoryEntityFailedSearchTime = 0L;
            }
            if (this.collectItemEntityTracker != null) {
                this.collectItemEntityTracker.unRegister(serverWorld);
                this.collectItemEntityTracker = null;
                this.collectItemEntityBoxes = null;
                this.collectItemEntityTrackerWasEmpty = false;
            }
        }
        if (this.extractionMode == HopperCachingState.BlockInventory.REMOVAL_TRACKING_BLOCK_ENTITY) {
            assert this.extractBlockInventory != null;
            ((InventoryChangeTracker) this.extractBlockInventory).stopListenForMajorInventoryChanges(this);
        }
        this.invalidateBlockExtractionData();
    }

    private void invalidateBlockExtractionData() {
        this.extractionMode = HopperCachingState.BlockInventory.UNKNOWN;
        this.extractBlockInventory = null;
        this.extractInventory = null;
        this.extractStackList = null;
        this.extractStackListModCount = 0L;
        if (this instanceof SleepingBlockEntity sleepingBlockEntity) {
            sleepingBlockEntity.wakeUpNow();
        }
    }

    @Inject(method = { "serverTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;insertAndExtract(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/HopperBlockEntity;Ljava/util/function/BooleanSupplier;)Z", shift = Shift.AFTER) })
    private static void checkSleepingConditions(Level world, BlockPos pos, BlockState state, HopperBlockEntity blockEntity, CallbackInfo ci) {
        ((HopperBlockEntityMixin) blockEntity).checkSleepingConditions();
    }

    private void checkSleepingConditions() {
        if (!this.isOnCooldown()) {
            if (this instanceof SleepingBlockEntity thisSleepingBlockEntity) {
                if (thisSleepingBlockEntity.isSleeping()) {
                    return;
                }
                if (!this.shouldCheckSleep) {
                    this.shouldCheckSleep = true;
                    return;
                }
                if (this instanceof InventoryChangeTracker thisTracker) {
                    boolean listenToExtractTracker = false;
                    boolean listenToInsertTracker = false;
                    boolean listenToExtractEntities = false;
                    boolean listenToInsertEntities = false;
                    LithiumStackList thisStackList = InventoryHelper.getLithiumStackList(this);
                    if (this.extractionMode != HopperCachingState.BlockInventory.BLOCK_STATE && thisStackList.getFullSlots() != thisStackList.size()) {
                        if (this.extractionMode == HopperCachingState.BlockInventory.REMOVAL_TRACKING_BLOCK_ENTITY) {
                            Container blockInventory = this.extractBlockInventory;
                            if (this.extractStackList == null || !(blockInventory instanceof InventoryChangeTracker)) {
                                return;
                            }
                            if (this.extractStackList.maybeSendsComparatorUpdatesOnFailedExtract() && (!(blockInventory instanceof ComparatorTracker comparatorTracker) || comparatorTracker.hasAnyComparatorNearby())) {
                                return;
                            }
                            listenToExtractTracker = true;
                        } else {
                            if (this.extractionMode != HopperCachingState.BlockInventory.NO_BLOCK_INVENTORY) {
                                return;
                            }
                            if (FabricTransferApiCompat.FABRIC_TRANSFER_API_V_1_PRESENT && FabricTransferApiCompat.canHopperInteractWithApiInventory((HopperBlockEntity) this, this.m_58900_(), true)) {
                                return;
                            }
                            listenToExtractEntities = true;
                        }
                    }
                    if (this.insertionMode != HopperCachingState.BlockInventory.BLOCK_STATE && 0 < thisStackList.getOccupiedSlots()) {
                        if (this.insertionMode == HopperCachingState.BlockInventory.REMOVAL_TRACKING_BLOCK_ENTITY) {
                            Container blockInventoryx = this.insertBlockInventory;
                            if (this.insertStackList == null || !(blockInventoryx instanceof InventoryChangeTracker)) {
                                return;
                            }
                            listenToInsertTracker = true;
                        } else {
                            if (this.insertionMode != HopperCachingState.BlockInventory.NO_BLOCK_INVENTORY) {
                                return;
                            }
                            if (FabricTransferApiCompat.FABRIC_TRANSFER_API_V_1_PRESENT && FabricTransferApiCompat.canHopperInteractWithApiInventory((HopperBlockEntity) this, this.m_58900_(), false)) {
                                return;
                            }
                            listenToInsertEntities = true;
                        }
                    }
                    if (listenToExtractTracker) {
                        ((InventoryChangeTracker) this.extractBlockInventory).listenForContentChangesOnce(this.extractStackList, this);
                    }
                    if (listenToInsertTracker) {
                        ((InventoryChangeTracker) this.insertBlockInventory).listenForContentChangesOnce(this.insertStackList, this);
                    }
                    if (listenToInsertEntities) {
                        if (this.insertInventoryEntityTracker == null) {
                            this.initInsertInventoryTracker(this.f_58857_, this.m_58900_());
                        }
                        this.insertInventoryEntityTracker.listenToEntityMovementOnce(this);
                    }
                    if (listenToExtractEntities) {
                        if (this.extractInventoryEntityTracker == null) {
                            this.initExtractInventoryTracker(this.f_58857_);
                        }
                        this.extractInventoryEntityTracker.listenToEntityMovementOnce(this);
                        if (this.collectItemEntityTracker == null) {
                            this.initCollectItemEntityTracker();
                        }
                        this.collectItemEntityTracker.listenToEntityMovementOnce(this);
                    }
                    thisTracker.listenForContentChangesOnce(thisStackList, this);
                    thisSleepingBlockEntity.startSleeping();
                }
            }
        }
    }

    @Override
    public void handleInventoryContentModified(Container inventory) {
        if (this instanceof SleepingBlockEntity sleepingBlockEntity) {
            sleepingBlockEntity.wakeUpNow();
        }
    }

    @Override
    public void handleInventoryRemoved(Container inventory) {
        if (this instanceof SleepingBlockEntity sleepingBlockEntity) {
            sleepingBlockEntity.wakeUpNow();
        }
        if (inventory == this.insertBlockInventory) {
            this.invalidateBlockInsertionData();
        }
        if (inventory == this.extractBlockInventory) {
            this.invalidateBlockExtractionData();
        }
        if (inventory == this) {
            this.invalidateCachedData();
        }
    }

    @Override
    public boolean handleComparatorAdded(Container inventory) {
        if (inventory == this.extractBlockInventory && this instanceof SleepingBlockEntity sleepingBlockEntity) {
            sleepingBlockEntity.wakeUpNow();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handleEntityMovement(Class<?> category) {
        if (this instanceof SleepingBlockEntity sleepingBlockEntity) {
            sleepingBlockEntity.wakeUpNow();
        }
    }
}