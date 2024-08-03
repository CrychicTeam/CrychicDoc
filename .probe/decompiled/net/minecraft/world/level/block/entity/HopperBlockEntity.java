package net.minecraft.world.level.block.entity;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;

public class HopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper {

    public static final int MOVE_ITEM_SPEED = 8;

    public static final int HOPPER_CONTAINER_SIZE = 5;

    private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);

    private int cooldownTime = -1;

    private long tickedGameTime;

    public HopperBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.HOPPER, blockPos0, blockState1);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.m_142466_(compoundTag0);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.m_59631_(compoundTag0)) {
            ContainerHelper.loadAllItems(compoundTag0, this.items);
        }
        this.cooldownTime = compoundTag0.getInt("TransferCooldown");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.m_183515_(compoundTag0);
        if (!this.m_59634_(compoundTag0)) {
            ContainerHelper.saveAllItems(compoundTag0, this.items);
        }
        compoundTag0.putInt("TransferCooldown", this.cooldownTime);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        this.m_59640_(null);
        return ContainerHelper.removeItem(this.getItems(), int0, int1);
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        this.m_59640_(null);
        this.getItems().set(int0, itemStack1);
        if (itemStack1.getCount() > this.m_6893_()) {
            itemStack1.setCount(this.m_6893_());
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.hopper");
    }

    public static void pushItemsTick(Level level0, BlockPos blockPos1, BlockState blockState2, HopperBlockEntity hopperBlockEntity3) {
        hopperBlockEntity3.cooldownTime--;
        hopperBlockEntity3.tickedGameTime = level0.getGameTime();
        if (!hopperBlockEntity3.isOnCooldown()) {
            hopperBlockEntity3.setCooldown(0);
            tryMoveItems(level0, blockPos1, blockState2, hopperBlockEntity3, () -> suckInItems(level0, hopperBlockEntity3));
        }
    }

    private static boolean tryMoveItems(Level level0, BlockPos blockPos1, BlockState blockState2, HopperBlockEntity hopperBlockEntity3, BooleanSupplier booleanSupplier4) {
        if (level0.isClientSide) {
            return false;
        } else {
            if (!hopperBlockEntity3.isOnCooldown() && (Boolean) blockState2.m_61143_(HopperBlock.ENABLED)) {
                boolean $$5 = false;
                if (!hopperBlockEntity3.m_7983_()) {
                    $$5 = ejectItems(level0, blockPos1, blockState2, hopperBlockEntity3);
                }
                if (!hopperBlockEntity3.inventoryFull()) {
                    $$5 |= booleanSupplier4.getAsBoolean();
                }
                if ($$5) {
                    hopperBlockEntity3.setCooldown(8);
                    m_155232_(level0, blockPos1, blockState2);
                    return true;
                }
            }
            return false;
        }
    }

    private boolean inventoryFull() {
        for (ItemStack $$0 : this.items) {
            if ($$0.isEmpty() || $$0.getCount() != $$0.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    private static boolean ejectItems(Level level0, BlockPos blockPos1, BlockState blockState2, Container container3) {
        Container $$4 = getAttachedContainer(level0, blockPos1, blockState2);
        if ($$4 == null) {
            return false;
        } else {
            Direction $$5 = ((Direction) blockState2.m_61143_(HopperBlock.FACING)).getOpposite();
            if (isFullContainer($$4, $$5)) {
                return false;
            } else {
                for (int $$6 = 0; $$6 < container3.getContainerSize(); $$6++) {
                    if (!container3.getItem($$6).isEmpty()) {
                        ItemStack $$7 = container3.getItem($$6).copy();
                        ItemStack $$8 = addItem(container3, $$4, container3.removeItem($$6, 1), $$5);
                        if ($$8.isEmpty()) {
                            $$4.setChanged();
                            return true;
                        }
                        container3.setItem($$6, $$7);
                    }
                }
                return false;
            }
        }
    }

    private static IntStream getSlots(Container container0, Direction direction1) {
        return container0 instanceof WorldlyContainer ? IntStream.of(((WorldlyContainer) container0).getSlotsForFace(direction1)) : IntStream.range(0, container0.getContainerSize());
    }

    private static boolean isFullContainer(Container container0, Direction direction1) {
        return getSlots(container0, direction1).allMatch(p_59379_ -> {
            ItemStack $$2 = container0.getItem(p_59379_);
            return $$2.getCount() >= $$2.getMaxStackSize();
        });
    }

    private static boolean isEmptyContainer(Container container0, Direction direction1) {
        return getSlots(container0, direction1).allMatch(p_59319_ -> container0.getItem(p_59319_).isEmpty());
    }

    public static boolean suckInItems(Level level0, Hopper hopper1) {
        Container $$2 = getSourceContainer(level0, hopper1);
        if ($$2 != null) {
            Direction $$3 = Direction.DOWN;
            return isEmptyContainer($$2, $$3) ? false : getSlots($$2, $$3).anyMatch(p_59363_ -> tryTakeInItemFromSlot(hopper1, $$2, p_59363_, $$3));
        } else {
            for (ItemEntity $$4 : getItemsAtAndAbove(level0, hopper1)) {
                if (addItem(hopper1, $$4)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean tryTakeInItemFromSlot(Hopper hopper0, Container container1, int int2, Direction direction3) {
        ItemStack $$4 = container1.getItem(int2);
        if (!$$4.isEmpty() && canTakeItemFromContainer(hopper0, container1, $$4, int2, direction3)) {
            ItemStack $$5 = $$4.copy();
            ItemStack $$6 = addItem(container1, hopper0, container1.removeItem(int2, 1), null);
            if ($$6.isEmpty()) {
                container1.setChanged();
                return true;
            }
            container1.setItem(int2, $$5);
        }
        return false;
    }

    public static boolean addItem(Container container0, ItemEntity itemEntity1) {
        boolean $$2 = false;
        ItemStack $$3 = itemEntity1.getItem().copy();
        ItemStack $$4 = addItem(null, container0, $$3, null);
        if ($$4.isEmpty()) {
            $$2 = true;
            itemEntity1.m_146870_();
        } else {
            itemEntity1.setItem($$4);
        }
        return $$2;
    }

    public static ItemStack addItem(@Nullable Container container0, Container container1, ItemStack itemStack2, @Nullable Direction direction3) {
        if (container1 instanceof WorldlyContainer $$4 && direction3 != null) {
            int[] $$5 = $$4.getSlotsForFace(direction3);
            for (int $$6 = 0; $$6 < $$5.length && !itemStack2.isEmpty(); $$6++) {
                itemStack2 = tryMoveInItem(container0, container1, itemStack2, $$5[$$6], direction3);
            }
            return itemStack2;
        }
        int $$7 = container1.getContainerSize();
        for (int $$8 = 0; $$8 < $$7 && !itemStack2.isEmpty(); $$8++) {
            itemStack2 = tryMoveInItem(container0, container1, itemStack2, $$8, direction3);
        }
        return itemStack2;
    }

    private static boolean canPlaceItemInContainer(Container container0, ItemStack itemStack1, int int2, @Nullable Direction direction3) {
        if (!container0.canPlaceItem(int2, itemStack1)) {
            return false;
        } else {
            if (container0 instanceof WorldlyContainer $$4 && !$$4.canPlaceItemThroughFace(int2, itemStack1, direction3)) {
                return false;
            }
            return true;
        }
    }

    private static boolean canTakeItemFromContainer(Container container0, Container container1, ItemStack itemStack2, int int3, Direction direction4) {
        if (!container1.canTakeItem(container0, int3, itemStack2)) {
            return false;
        } else {
            if (container1 instanceof WorldlyContainer $$5 && !$$5.canTakeItemThroughFace(int3, itemStack2, direction4)) {
                return false;
            }
            return true;
        }
    }

    private static ItemStack tryMoveInItem(@Nullable Container container0, Container container1, ItemStack itemStack2, int int3, @Nullable Direction direction4) {
        ItemStack $$5 = container1.getItem(int3);
        if (canPlaceItemInContainer(container1, itemStack2, int3, direction4)) {
            boolean $$6 = false;
            boolean $$7 = container1.isEmpty();
            if ($$5.isEmpty()) {
                container1.setItem(int3, itemStack2);
                itemStack2 = ItemStack.EMPTY;
                $$6 = true;
            } else if (canMergeItems($$5, itemStack2)) {
                int $$8 = itemStack2.getMaxStackSize() - $$5.getCount();
                int $$9 = Math.min(itemStack2.getCount(), $$8);
                itemStack2.shrink($$9);
                $$5.grow($$9);
                $$6 = $$9 > 0;
            }
            if ($$6) {
                if ($$7 && container1 instanceof HopperBlockEntity $$10 && !$$10.isOnCustomCooldown()) {
                    int $$11 = 0;
                    if (container0 instanceof HopperBlockEntity $$12 && $$10.tickedGameTime >= $$12.tickedGameTime) {
                        $$11 = 1;
                    }
                    $$10.setCooldown(8 - $$11);
                }
                container1.setChanged();
            }
        }
        return itemStack2;
    }

    @Nullable
    private static Container getAttachedContainer(Level level0, BlockPos blockPos1, BlockState blockState2) {
        Direction $$3 = (Direction) blockState2.m_61143_(HopperBlock.FACING);
        return getContainerAt(level0, blockPos1.relative($$3));
    }

    @Nullable
    private static Container getSourceContainer(Level level0, Hopper hopper1) {
        return getContainerAt(level0, hopper1.getLevelX(), hopper1.getLevelY() + 1.0, hopper1.getLevelZ());
    }

    public static List<ItemEntity> getItemsAtAndAbove(Level level0, Hopper hopper1) {
        return (List<ItemEntity>) hopper1.getSuckShape().toAabbs().stream().flatMap(p_155558_ -> level0.m_6443_(ItemEntity.class, p_155558_.move(hopper1.getLevelX() - 0.5, hopper1.getLevelY() - 0.5, hopper1.getLevelZ() - 0.5), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
    }

    @Nullable
    public static Container getContainerAt(Level level0, BlockPos blockPos1) {
        return getContainerAt(level0, (double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 0.5, (double) blockPos1.m_123343_() + 0.5);
    }

    @Nullable
    private static Container getContainerAt(Level level0, double double1, double double2, double double3) {
        Container $$4 = null;
        BlockPos $$5 = BlockPos.containing(double1, double2, double3);
        BlockState $$6 = level0.getBlockState($$5);
        Block $$7 = $$6.m_60734_();
        if ($$7 instanceof WorldlyContainerHolder) {
            $$4 = ((WorldlyContainerHolder) $$7).getContainer($$6, level0, $$5);
        } else if ($$6.m_155947_()) {
            BlockEntity $$8 = level0.getBlockEntity($$5);
            if ($$8 instanceof Container) {
                $$4 = (Container) $$8;
                if ($$4 instanceof ChestBlockEntity && $$7 instanceof ChestBlock) {
                    $$4 = ChestBlock.getContainer((ChestBlock) $$7, $$6, level0, $$5, true);
                }
            }
        }
        if ($$4 == null) {
            List<Entity> $$9 = level0.getEntities((Entity) null, new AABB(double1 - 0.5, double2 - 0.5, double3 - 0.5, double1 + 0.5, double2 + 0.5, double3 + 0.5), EntitySelector.CONTAINER_ENTITY_SELECTOR);
            if (!$$9.isEmpty()) {
                $$4 = (Container) $$9.get(level0.random.nextInt($$9.size()));
            }
        }
        return $$4;
    }

    private static boolean canMergeItems(ItemStack itemStack0, ItemStack itemStack1) {
        return itemStack0.getCount() <= itemStack0.getMaxStackSize() && ItemStack.isSameItemSameTags(itemStack0, itemStack1);
    }

    @Override
    public double getLevelX() {
        return (double) this.f_58858_.m_123341_() + 0.5;
    }

    @Override
    public double getLevelY() {
        return (double) this.f_58858_.m_123342_() + 0.5;
    }

    @Override
    public double getLevelZ() {
        return (double) this.f_58858_.m_123343_() + 0.5;
    }

    private void setCooldown(int int0) {
        this.cooldownTime = int0;
    }

    private boolean isOnCooldown() {
        return this.cooldownTime > 0;
    }

    private boolean isOnCustomCooldown() {
        return this.cooldownTime > 8;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullListItemStack0) {
        this.items = nonNullListItemStack0;
    }

    public static void entityInside(Level level0, BlockPos blockPos1, BlockState blockState2, Entity entity3, HopperBlockEntity hopperBlockEntity4) {
        if (entity3 instanceof ItemEntity && Shapes.joinIsNotEmpty(Shapes.create(entity3.getBoundingBox().move((double) (-blockPos1.m_123341_()), (double) (-blockPos1.m_123342_()), (double) (-blockPos1.m_123343_()))), hopperBlockEntity4.m_59300_(), BooleanOp.AND)) {
            tryMoveItems(level0, blockPos1, blockState2, hopperBlockEntity4, () -> addItem(hopperBlockEntity4, (ItemEntity) entity3));
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int int0, Inventory inventory1) {
        return new HopperMenu(int0, inventory1, this);
    }
}