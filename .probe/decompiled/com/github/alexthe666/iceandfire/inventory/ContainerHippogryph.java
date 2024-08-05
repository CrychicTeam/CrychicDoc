package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class ContainerHippogryph extends AbstractContainerMenu {

    private final Container hippogryphInventory;

    private final EntityHippogryph hippogryph;

    private final Player player;

    public ContainerHippogryph(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(18), playerInventory, null);
    }

    public ContainerHippogryph(int id, Container ratInventory, Inventory playerInventory, EntityHippogryph hippogryph) {
        super(IafContainerRegistry.HIPPOGRYPH_CONTAINER.get(), id);
        this.hippogryphInventory = ratInventory;
        if (hippogryph == null && IceAndFire.PROXY.getReferencedMob() instanceof EntityHippogryph) {
            hippogryph = (EntityHippogryph) IceAndFire.PROXY.getReferencedMob();
        }
        this.hippogryph = hippogryph;
        this.player = playerInventory.player;
        this.hippogryphInventory.startOpen(this.player);
        this.m_38897_(new Slot(this.hippogryphInventory, 0, 8, 18) {

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == Items.SADDLE && !this.m_6657_();
            }

            @Override
            public void setChanged() {
                if (ContainerHippogryph.this.hippogryph != null) {
                    ContainerHippogryph.this.hippogryph.refreshInventory();
                }
            }

            @Override
            public boolean isActive() {
                return true;
            }
        });
        this.m_38897_(new Slot(this.hippogryphInventory, 1, 8, 36) {

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == Blocks.CHEST.asItem() && !this.m_6657_();
            }

            @Override
            public void setChanged() {
                if (ContainerHippogryph.this.hippogryph != null) {
                    ContainerHippogryph.this.hippogryph.refreshInventory();
                }
            }

            @Override
            public boolean isActive() {
                return true;
            }
        });
        this.m_38897_(new Slot(this.hippogryphInventory, 2, 8, 52) {

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return EntityHippogryph.getIntFromArmor(stack) != 0;
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public void setChanged() {
                if (ContainerHippogryph.this.hippogryph != null) {
                    ContainerHippogryph.this.hippogryph.refreshInventory();
                }
            }

            @Override
            public boolean isActive() {
                return true;
            }
        });
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 5; l++) {
                this.m_38897_(new Slot(this.hippogryphInventory, 3 + l + k * 5, 80 + l * 18, 18 + k * 18) {

                    @Override
                    public boolean isActive() {
                        return ContainerHippogryph.this.hippogryph != null && ContainerHippogryph.this.hippogryph.isChested();
                    }

                    @Override
                    public boolean mayPlace(@NotNull ItemStack stack) {
                        return ContainerHippogryph.this.hippogryph != null && ContainerHippogryph.this.hippogryph.isChested();
                    }
                });
            }
        }
        for (int i1 = 0; i1 < 3; i1++) {
            for (int k1 = 0; k1 < 9; k1++) {
                this.m_38897_(new Slot(this.player.getInventory(), k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }
        for (int j1 = 0; j1 < 9; j1++) {
            this.m_38897_(new Slot(this.player.getInventory(), j1, 8 + j1 * 18, 142));
        }
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.hippogryphInventory.getContainerSize()) {
                if (!this.m_38903_(itemstack1, this.hippogryphInventory.getContainerSize(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.m_38853_(1).mayPlace(itemstack1) && !this.m_38853_(1).hasItem()) {
                if (!this.m_38903_(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.m_38853_(2).mayPlace(itemstack1) && !this.m_38853_(2).hasItem()) {
                if (!this.m_38903_(itemstack1, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.m_38853_(0).mayPlace(itemstack1)) {
                if (!this.m_38903_(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.hippogryphInventory.getContainerSize() <= 3 || !this.m_38903_(itemstack1, 3, this.hippogryphInventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player playerIn) {
        return this.hippogryphInventory.stillValid(playerIn) && this.hippogryph.m_6084_() && this.hippogryph.m_20270_(playerIn) < 8.0F;
    }

    @Override
    public void removed(@NotNull Player playerIn) {
        super.removed(playerIn);
        this.hippogryphInventory.stopOpen(playerIn);
    }
}