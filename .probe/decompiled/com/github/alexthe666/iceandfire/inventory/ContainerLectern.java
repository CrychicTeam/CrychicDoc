package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemBestiary;
import com.github.alexthe666.iceandfire.message.MessageUpdateLectern;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ContainerLectern extends AbstractContainerMenu {

    private final Container tileFurnace;

    private final int[] possiblePagesInt = new int[3];

    public ContainerLectern(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(2), playerInventory, new SimpleContainerData(0));
    }

    public ContainerLectern(int id, Container furnaceInventory, Inventory playerInventory, ContainerData vars) {
        super(IafContainerRegistry.IAF_LECTERN_CONTAINER.get(), id);
        this.tileFurnace = furnaceInventory;
        this.m_38897_(new SlotLectern(furnaceInventory, 0, 15, 47) {

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return super.m_5857_(stack) && !stack.isEmpty() && stack.getItem() instanceof ItemBestiary;
            }
        });
        this.m_38897_(new Slot(furnaceInventory, 1, 35, 47) {

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return super.mayPlace(stack) && !stack.isEmpty() && stack.getItem() == IafItemRegistry.MANUSCRIPT.get();
            }
        });
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.m_38897_(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; k++) {
            this.m_38897_(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    private static int getPageField(int i) {
        if (IceAndFire.PROXY.getRefrencedTE() instanceof TileEntityLectern) {
            TileEntityLectern lectern = (TileEntityLectern) IceAndFire.PROXY.getRefrencedTE();
            return lectern.selectedPages[i] == null ? -1 : lectern.selectedPages[i].ordinal();
        } else {
            return -1;
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
    }

    public void onUpdate() {
        this.possiblePagesInt[0] = getPageField(0);
        this.possiblePagesInt[1] = getPageField(1);
        this.possiblePagesInt[2] = getPageField(2);
    }

    @Override
    public boolean stillValid(@NotNull Player playerIn) {
        return this.tileFurnace.stillValid(playerIn);
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.tileFurnace.getContainerSize()) {
                if (!this.m_38903_(itemstack1, this.tileFurnace.getContainerSize(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.m_38853_(0).mayPlace(itemstack1) && !this.m_38853_(0).hasItem()) {
                if (!this.m_38903_(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.m_38853_(1).mayPlace(itemstack1) && !this.m_38853_(1).hasItem()) {
                if (!this.m_38903_(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.tileFurnace.getContainerSize() <= 5 || !this.m_38903_(itemstack1, 5, this.tileFurnace.getContainerSize(), false)) {
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

    public int getManuscriptAmount() {
        ItemStack itemstack = this.tileFurnace.getItem(1);
        return !itemstack.isEmpty() && itemstack.getItem() == IafItemRegistry.MANUSCRIPT.get() ? itemstack.getCount() : 0;
    }

    public EnumBestiaryPages[] getPossiblePages() {
        this.possiblePagesInt[0] = getPageField(0);
        this.possiblePagesInt[1] = getPageField(1);
        this.possiblePagesInt[2] = getPageField(2);
        EnumBestiaryPages[] pages = new EnumBestiaryPages[3];
        if (this.tileFurnace.getItem(0).getItem() == IafItemRegistry.BESTIARY.get()) {
            if (this.possiblePagesInt[0] < 0) {
                pages[0] = null;
            } else {
                pages[0] = EnumBestiaryPages.values()[Math.min(EnumBestiaryPages.values().length, this.possiblePagesInt[0])];
            }
            if (this.possiblePagesInt[1] < 0) {
                pages[1] = null;
            } else {
                pages[1] = EnumBestiaryPages.values()[Math.min(EnumBestiaryPages.values().length, this.possiblePagesInt[1])];
            }
            if (this.possiblePagesInt[2] < 0) {
                pages[2] = null;
            } else {
                pages[2] = EnumBestiaryPages.values()[Math.min(EnumBestiaryPages.values().length, this.possiblePagesInt[2])];
            }
        }
        return pages;
    }

    @Override
    public boolean clickMenuButton(Player playerIn, int id) {
        this.possiblePagesInt[0] = getPageField(0);
        this.possiblePagesInt[1] = getPageField(1);
        this.possiblePagesInt[2] = getPageField(2);
        ItemStack bookStack = this.tileFurnace.getItem(0);
        ItemStack manuscriptStack = this.tileFurnace.getItem(1);
        int i = 3;
        if (!playerIn.m_9236_().isClientSide && !playerIn.isCreative()) {
            manuscriptStack.shrink(i);
            if (manuscriptStack.isEmpty()) {
                this.tileFurnace.setItem(1, ItemStack.EMPTY);
            }
            return false;
        } else if ((manuscriptStack.isEmpty() || manuscriptStack.getCount() < i || manuscriptStack.getItem() != IafItemRegistry.MANUSCRIPT.get()) && !playerIn.isCreative()) {
            return false;
        } else if (this.possiblePagesInt[id] > 0 && !bookStack.isEmpty()) {
            EnumBestiaryPages page = this.getPossiblePages()[Mth.clamp(id, 0, 2)];
            if (page != null) {
                if (bookStack.getItem() == IafItemRegistry.BESTIARY.get()) {
                    this.tileFurnace.setItem(0, bookStack);
                    if (IceAndFire.PROXY.getRefrencedTE() instanceof TileEntityLectern) {
                        if (!playerIn.m_9236_().isClientSide) {
                            if (bookStack.getItem() == IafItemRegistry.BESTIARY.get()) {
                                EnumBestiaryPages.addPage(EnumBestiaryPages.fromInt(page.ordinal()), bookStack);
                            }
                            if (this.tileFurnace instanceof TileEntityLectern entityLectern) {
                                entityLectern.randomizePages(bookStack, manuscriptStack);
                            }
                        } else {
                            IceAndFire.sendMSGToServer(new MessageUpdateLectern(IceAndFire.PROXY.getRefrencedTE().getBlockPos().asLong(), 0, 0, 0, true, page.ordinal()));
                        }
                    }
                }
                this.tileFurnace.setChanged();
                this.m_6199_(this.tileFurnace);
                playerIn.m_9236_().playSound(null, playerIn.m_20183_(), IafSoundRegistry.BESTIARY_PAGE, SoundSource.BLOCKS, 1.0F, playerIn.m_9236_().random.nextFloat() * 0.1F + 0.9F);
            }
            this.onUpdate();
            return true;
        } else {
            return false;
        }
    }
}