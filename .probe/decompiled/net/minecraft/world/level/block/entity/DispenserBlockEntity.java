package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class DispenserBlockEntity extends RandomizableContainerBlockEntity {

    public static final int CONTAINER_SIZE = 9;

    private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);

    protected DispenserBlockEntity(BlockEntityType<?> blockEntityType0, BlockPos blockPos1, BlockState blockState2) {
        super(blockEntityType0, blockPos1, blockState2);
    }

    public DispenserBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        this(BlockEntityType.DISPENSER, blockPos0, blockState1);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    public int getRandomSlot(RandomSource randomSource0) {
        this.m_59640_(null);
        int $$1 = -1;
        int $$2 = 1;
        for (int $$3 = 0; $$3 < this.items.size(); $$3++) {
            if (!this.items.get($$3).isEmpty() && randomSource0.nextInt($$2++) == 0) {
                $$1 = $$3;
            }
        }
        return $$1;
    }

    public int addItem(ItemStack itemStack0) {
        for (int $$1 = 0; $$1 < this.items.size(); $$1++) {
            if (this.items.get($$1).isEmpty()) {
                this.m_6836_($$1, itemStack0);
                return $$1;
            }
        }
        return -1;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.dispenser");
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.m_142466_(compoundTag0);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.m_59631_(compoundTag0)) {
            ContainerHelper.loadAllItems(compoundTag0, this.items);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.m_183515_(compoundTag0);
        if (!this.m_59634_(compoundTag0)) {
            ContainerHelper.saveAllItems(compoundTag0, this.items);
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullListItemStack0) {
        this.items = nonNullListItemStack0;
    }

    @Override
    protected AbstractContainerMenu createMenu(int int0, Inventory inventory1) {
        return new DispenserMenu(int0, inventory1, this);
    }
}