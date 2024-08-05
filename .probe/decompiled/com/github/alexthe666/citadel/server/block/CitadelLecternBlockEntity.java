package com.github.alexthe666.citadel.server.block;

import com.github.alexthe666.citadel.Citadel;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.LecternMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CitadelLecternBlockEntity extends BlockEntity implements Clearable, MenuProvider {

    private ItemStack book = ItemStack.EMPTY;

    private final Container bookAccess = new Container() {

        @Override
        public int getContainerSize() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return CitadelLecternBlockEntity.this.book.isEmpty();
        }

        @Override
        public ItemStack getItem(int i) {
            return i == 0 ? CitadelLecternBlockEntity.this.book : ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItem(int i, int j) {
            if (i == 0) {
                ItemStack itemstack = CitadelLecternBlockEntity.this.book.split(j);
                if (CitadelLecternBlockEntity.this.book.isEmpty()) {
                    CitadelLecternBlockEntity.this.onBookItemRemove();
                }
                return itemstack;
            } else {
                return ItemStack.EMPTY;
            }
        }

        @Override
        public ItemStack removeItemNoUpdate(int i) {
            if (i == 0) {
                ItemStack itemstack = CitadelLecternBlockEntity.this.book;
                CitadelLecternBlockEntity.this.book = ItemStack.EMPTY;
                CitadelLecternBlockEntity.this.onBookItemRemove();
                return itemstack;
            } else {
                return ItemStack.EMPTY;
            }
        }

        @Override
        public void setItem(int i, ItemStack stack) {
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void setChanged() {
            CitadelLecternBlockEntity.this.m_6596_();
        }

        @Override
        public boolean stillValid(Player p_59588_) {
            if (CitadelLecternBlockEntity.this.f_58857_.getBlockEntity(CitadelLecternBlockEntity.this.f_58858_) != CitadelLecternBlockEntity.this) {
                return false;
            } else {
                return p_59588_.m_20275_((double) CitadelLecternBlockEntity.this.f_58858_.m_123341_() + 0.5, (double) CitadelLecternBlockEntity.this.f_58858_.m_123342_() + 0.5, (double) CitadelLecternBlockEntity.this.f_58858_.m_123343_() + 0.5) > 64.0 ? false : CitadelLecternBlockEntity.this.hasBook();
            }
        }

        @Override
        public boolean canPlaceItem(int i, ItemStack stack) {
            return false;
        }

        @Override
        public void clearContent() {
        }
    };

    private final ContainerData dataAccess = new ContainerData() {

        @Override
        public int get(int i) {
            return 0;
        }

        @Override
        public void set(int i, int j) {
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public CitadelLecternBlockEntity(BlockPos pos, BlockState state) {
        super(Citadel.LECTERN_BE.get(), pos, state);
    }

    public ItemStack getBook() {
        return this.book;
    }

    public boolean hasBook() {
        return LecternBooks.isLecternBook(this.book);
    }

    public void setBook(ItemStack stack) {
        this.setBook(stack, (Player) null);
    }

    void onBookItemRemove() {
        LecternBlock.resetBookState((Entity) null, this.m_58904_(), this.m_58899_(), this.m_58900_(), false);
    }

    public void setBook(ItemStack itemStack, @Nullable Player player) {
        this.book = itemStack;
        this.m_6596_();
    }

    public int getRedstoneSignal() {
        return this.hasBook() ? 1 : 0;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Book", 10)) {
            this.book = ItemStack.of(tag.getCompound("Book"));
        } else {
            this.book = ItemStack.EMPTY;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.getBook().isEmpty()) {
            tag.put("Book", this.getBook().save(new CompoundTag()));
        }
    }

    @Override
    public void clearContent() {
        this.setBook(ItemStack.EMPTY);
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new LecternMenu(i, this.bookAccess, this.dataAccess);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.lectern");
    }
}