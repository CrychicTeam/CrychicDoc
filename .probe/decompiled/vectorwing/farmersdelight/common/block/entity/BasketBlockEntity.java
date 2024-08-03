package vectorwing.farmersdelight.common.block.entity;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import vectorwing.farmersdelight.common.block.BasketBlock;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class BasketBlockEntity extends RandomizableContainerBlockEntity implements Basket {

    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);

    private int transferCooldown = -1;

    public BasketBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.BASKET.get(), pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.m_142466_(compound);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.m_59631_(compound)) {
            ContainerHelper.loadAllItems(compound, this.items);
        }
        this.transferCooldown = compound.getInt("TransferCooldown");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.m_183515_(compound);
        if (!this.m_59634_(compound)) {
            ContainerHelper.saveAllItems(compound, this.items);
        }
        compound.putInt("TransferCooldown", this.transferCooldown);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        this.m_59640_(null);
        return ContainerHelper.removeItem(this.getItems(), index, count);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.m_59640_(null);
        this.getItems().set(index, stack);
        if (stack.getCount() > this.m_6893_()) {
            stack.setCount(this.m_6893_());
        }
    }

    @Override
    protected Component getDefaultName() {
        return TextUtils.getTranslation("container.basket");
    }

    public static boolean pullItems(Level level, Basket basket, int facingIndex) {
        for (ItemEntity itementity : getCaptureItems(level, basket, facingIndex)) {
            if (captureItem(basket, itementity)) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack putStackInInventoryAllSlots(Container destination, ItemStack stack) {
        int i = destination.getContainerSize();
        for (int j = 0; j < i && !stack.isEmpty(); j++) {
            stack = insertStack(destination, stack, j);
        }
        return stack;
    }

    private static boolean canInsertItemInSlot(Container inventoryIn, ItemStack stack, int index, @Nullable Direction side) {
        return !inventoryIn.canPlaceItem(index, stack) ? false : !(inventoryIn instanceof WorldlyContainer) || ((WorldlyContainer) inventoryIn).canPlaceItemThroughFace(index, stack, side);
    }

    private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        return stack1.getCount() <= stack1.getMaxStackSize() && ItemStack.isSameItemSameTags(stack1, stack2);
    }

    private static ItemStack insertStack(Container destination, ItemStack stack, int index) {
        ItemStack itemstack = destination.getItem(index);
        if (canInsertItemInSlot(destination, stack, index, null)) {
            boolean flag = false;
            boolean isDestinationEmpty = destination.isEmpty();
            if (itemstack.isEmpty()) {
                destination.setItem(index, stack);
                stack = ItemStack.EMPTY;
                flag = true;
            } else if (canCombine(itemstack, stack)) {
                int i = stack.getMaxStackSize() - itemstack.getCount();
                int j = Math.min(stack.getCount(), i);
                stack.shrink(j);
                itemstack.grow(j);
                flag = j > 0;
            }
            if (flag) {
                if (isDestinationEmpty && destination instanceof BasketBlockEntity firstBasket && !firstBasket.mayTransfer()) {
                    int k = 0;
                    firstBasket.setTransferCooldown(8 - k);
                }
                destination.setChanged();
            }
        }
        return stack;
    }

    public static boolean captureItem(Container inventory, ItemEntity itemEntity) {
        boolean flag = false;
        ItemStack entityItemStack = itemEntity.getItem().copy();
        ItemStack remainderStack = putStackInInventoryAllSlots(inventory, entityItemStack);
        if (remainderStack.isEmpty()) {
            flag = true;
            itemEntity.m_146870_();
        } else {
            itemEntity.setItem(remainderStack);
        }
        return flag;
    }

    public static List<ItemEntity> getCaptureItems(Level level, Basket basket, int facingIndex) {
        return (List<ItemEntity>) basket.getFacingCollectionArea(facingIndex).toAabbs().stream().flatMap(aabb -> level.m_6443_(ItemEntity.class, aabb.move(basket.getLevelX() - 0.5, basket.getLevelY() - 0.5, basket.getLevelZ() - 0.5), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = itemsIn;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return ChestMenu.threeRows(id, player, this);
    }

    public void setTransferCooldown(int ticks) {
        this.transferCooldown = ticks;
    }

    private boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    public boolean mayTransfer() {
        return this.transferCooldown > 8;
    }

    private void updateHopper(Supplier<Boolean> supplier) {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide && !this.isOnTransferCooldown() && (Boolean) this.m_58900_().m_61143_(BlockStateProperties.ENABLED)) {
            boolean flag = false;
            if (!this.isFull()) {
                flag = (Boolean) supplier.get();
            }
            if (flag) {
                this.setTransferCooldown(8);
                this.m_6596_();
            }
        }
    }

    private boolean isFull() {
        for (ItemStack itemstack : this.items) {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    public void onEntityCollision(Entity entity) {
        if (entity instanceof ItemEntity) {
            BlockPos blockpos = this.m_58899_();
            int facing = ((Direction) this.m_58900_().m_61143_(BasketBlock.FACING)).get3DDataValue();
            if (Shapes.joinIsNotEmpty(Shapes.create(entity.getBoundingBox().move((double) (-blockpos.m_123341_()), (double) (-blockpos.m_123342_()), (double) (-blockpos.m_123343_()))), this.getFacingCollectionArea(facing), BooleanOp.AND)) {
                this.updateHopper(() -> captureItem(this, (ItemEntity) entity));
            }
        }
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

    public static void pushItemsTick(Level level, BlockPos pos, BlockState state, BasketBlockEntity blockEntity) {
        blockEntity.transferCooldown--;
        if (!blockEntity.isOnTransferCooldown()) {
            blockEntity.setTransferCooldown(0);
            int facing = ((Direction) state.m_61143_(BasketBlock.FACING)).get3DDataValue();
            blockEntity.updateHopper(() -> pullItems(level, blockEntity, facing));
        }
    }
}