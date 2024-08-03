package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.inventory.ContainerLectern;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemBestiary;
import com.github.alexthe666.iceandfire.message.MessageUpdateLectern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

public class TileEntityLectern extends BaseContainerBlockEntity implements WorldlyContainer {

    private static final int[] slotsTop = new int[] { 0 };

    private static final int[] slotsSides = new int[] { 1 };

    private static final int[] slotsBottom = new int[] { 0 };

    private static final Random RANDOM = new Random();

    private static final ArrayList<EnumBestiaryPages> EMPTY_LIST = new ArrayList();

    public final ContainerData furnaceData = new ContainerData() {

        @Override
        public int get(int index) {
            return 0;
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 0;
        }
    };

    public float pageFlip;

    public float pageFlipPrev;

    public float pageHelp1;

    public float pageHelp2;

    public EnumBestiaryPages[] selectedPages = new EnumBestiaryPages[3];

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);

    private final Random localRand = new Random();

    private NonNullList<ItemStack> stacks = NonNullList.withSize(3, ItemStack.EMPTY);

    public TileEntityLectern(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.IAF_LECTERN.get(), pos, state);
    }

    public static void bookAnimationTick(Level level0, BlockPos blockPos1, BlockState blockState2, TileEntityLectern tileEntityLectern3) {
        float f1 = tileEntityLectern3.pageHelp1;
        do {
            tileEntityLectern3.pageHelp1 = tileEntityLectern3.pageHelp1 + (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
        } while (f1 == tileEntityLectern3.pageHelp1);
        tileEntityLectern3.pageFlipPrev = tileEntityLectern3.pageFlip;
        float f = (tileEntityLectern3.pageHelp1 - tileEntityLectern3.pageFlip) * 0.04F;
        float f3 = 0.02F;
        f = Mth.clamp(f, -f3, f3);
        tileEntityLectern3.pageHelp2 = tileEntityLectern3.pageHelp2 + (f - tileEntityLectern3.pageHelp2) * 0.9F;
        tileEntityLectern3.pageFlip = tileEntityLectern3.pageFlip + tileEntityLectern3.pageHelp2;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @NotNull
    @Override
    public ItemStack getItem(int index) {
        return this.stacks.get(index);
    }

    private List<EnumBestiaryPages> getPossiblePages() {
        List<EnumBestiaryPages> list = EnumBestiaryPages.possiblePages(this.stacks.get(0));
        return (List<EnumBestiaryPages>) (list != null && !list.isEmpty() ? list : EMPTY_LIST);
    }

    @NotNull
    @Override
    public ItemStack removeItem(int index, int count) {
        if (!this.stacks.get(index).isEmpty()) {
            if (this.stacks.get(index).getCount() <= count) {
                ItemStack itemstack = this.stacks.get(index);
                this.stacks.set(index, ItemStack.EMPTY);
                return itemstack;
            } else {
                ItemStack itemstack = this.stacks.get(index).split(count);
                if (this.stacks.get(index).getCount() == 0) {
                    this.stacks.set(index, ItemStack.EMPTY);
                }
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        boolean isSame = !stack.isEmpty() && ItemStack.isSameItem(stack, this.stacks.get(index)) && ItemStack.matches(stack, this.stacks.get(index));
        this.stacks.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        if (!isSame) {
            this.m_6596_();
            if (this.stacks.get(1).isEmpty()) {
                this.selectedPages[0] = null;
                this.selectedPages[1] = null;
                this.selectedPages[2] = null;
                IceAndFire.sendMSGToAll(new MessageUpdateLectern(this.f_58858_.asLong(), -1, -1, -1, false, 0));
            } else {
                this.selectedPages = this.randomizePages(this.getItem(0), this.getItem(1));
            }
        }
    }

    public EnumBestiaryPages[] randomizePages(ItemStack bestiary, ItemStack manuscript) {
        if (!this.f_58857_.isClientSide) {
            if (bestiary.getItem() == IafItemRegistry.BESTIARY.get()) {
                List<EnumBestiaryPages> possibleList = this.getPossiblePages();
                this.localRand.setSeed(this.f_58857_.getGameTime());
                Collections.shuffle(possibleList, this.localRand);
                if (!possibleList.isEmpty()) {
                    this.selectedPages[0] = (EnumBestiaryPages) possibleList.get(0);
                } else {
                    this.selectedPages[0] = null;
                }
                if (possibleList.size() > 1) {
                    this.selectedPages[1] = (EnumBestiaryPages) possibleList.get(1);
                } else {
                    this.selectedPages[1] = null;
                }
                if (possibleList.size() > 2) {
                    this.selectedPages[2] = (EnumBestiaryPages) possibleList.get(2);
                } else {
                    this.selectedPages[2] = null;
                }
            }
            int page1 = this.selectedPages[0] == null ? -1 : this.selectedPages[0].ordinal();
            int page2 = this.selectedPages[1] == null ? -1 : this.selectedPages[1].ordinal();
            int page3 = this.selectedPages[2] == null ? -1 : this.selectedPages[2].ordinal();
            IceAndFire.sendMSGToAll(new MessageUpdateLectern(this.f_58858_.asLong(), page1, page2, page3, false, 0));
        }
        return this.selectedPages;
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.stacks);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        ContainerHelper.saveAllItems(compound, this.stacks);
    }

    @Override
    public void startOpen(@NotNull Player player) {
    }

    @Override
    public void stopOpen(@NotNull Player player) {
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        } else if (index == 0) {
            return stack.getItem() instanceof ItemBestiary;
        } else {
            return index == 1 ? stack.getItem() == IafItemRegistry.MANUSCRIPT.get() : false;
        }
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.stacks.clear();
    }

    @NotNull
    @Override
    public Component getName() {
        return Component.translatable("block.iceandfire.lectern");
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
        return false;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @NotNull
    @Override
    public int[] getSlotsForFace(@NotNull Direction side) {
        return side == Direction.DOWN ? slotsBottom : (side == Direction.UP ? slotsTop : slotsSides);
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack itemStackIn, Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @NotNull
    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStack.EMPTY;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        this.load(packet.getTag());
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187480_();
    }

    @NotNull
    @Override
    protected Component getDefaultName() {
        return this.getName();
    }

    @NotNull
    @Override
    protected AbstractContainerMenu createMenu(int id, @NotNull Inventory player) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.stacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (this.f_58859_ || facing == null || capability != ForgeCapabilities.ITEM_HANDLER) {
            return super.getCapability(capability, facing);
        } else {
            return facing == Direction.DOWN ? this.handlers[1].cast() : this.handlers[0].cast();
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ContainerLectern(id, this, playerInventory, this.furnaceData);
    }
}