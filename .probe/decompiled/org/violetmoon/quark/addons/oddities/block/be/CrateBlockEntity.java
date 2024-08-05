package org.violetmoon.quark.addons.oddities.block.be;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.block.CrateBlock;
import org.violetmoon.quark.addons.oddities.capability.CrateItemHandler;
import org.violetmoon.quark.addons.oddities.inventory.CrateMenu;
import org.violetmoon.quark.addons.oddities.module.CrateModule;

public class CrateBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    private int numPlayersUsing;

    private int[] visibleSlots = new int[0];

    protected final ContainerData crateData = new ContainerData() {

        @Override
        public int get(int index) {
            CrateItemHandler handler = CrateBlockEntity.this.itemHandler();
            return index == 0 ? handler.displayTotal : handler.displaySlots;
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public CrateBlockEntity(BlockPos pos, BlockState state) {
        super(CrateModule.blockEntityType, pos, state);
    }

    public void spillTheTea() {
        this.itemHandler().spill(this.f_58857_, this.f_58858_);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CrateBlockEntity be) {
        be.tick();
    }

    public void tick() {
        this.itemHandler().recalculate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.merge(this.itemHandler().serializeNBT());
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.itemHandler().deserializeNBT(nbt);
    }

    public CrateItemHandler itemHandler() {
        LazyOptional<IItemHandler> handler = this.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (handler.isPresent()) {
            Object var3 = handler.orElse(new EmptyHandler());
            if (var3 instanceof CrateItemHandler) {
                return (CrateItemHandler) var3;
            }
        }
        return new CrateItemHandler();
    }

    @NotNull
    @Override
    public ItemStack getItem(int slot) {
        return this.itemHandler().getStackInSlot(slot);
    }

    @NotNull
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return this.itemHandler().extractItem(slot, 64, true);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        this.itemHandler().setStackInSlot(slot, stack);
    }

    @NotNull
    @Override
    public ItemStack removeItem(int slot, int count) {
        return this.itemHandler().extractItem(slot, 64, true);
    }

    @Override
    public int getContainerSize() {
        return this.itemHandler().getSlots();
    }

    @Override
    public void clearContent() {
        this.itemHandler().clear();
    }

    @Override
    public boolean isEmpty() {
        return this.itemHandler().isEmpty();
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction dir) {
        return true;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, Direction dir) {
        return this.itemHandler().getSlotLimit(index) > 0;
    }

    @NotNull
    @Override
    public int[] getSlotsForFace(@NotNull Direction dir) {
        int slotCount = this.itemHandler().getSlots();
        if (this.visibleSlots.length != slotCount) {
            this.visibleSlots = new int[slotCount];
            int i = 0;
            while (i < slotCount) {
                this.visibleSlots[i] = i++;
            }
        }
        return this.visibleSlots;
    }

    @NotNull
    @Override
    protected Component getDefaultName() {
        return Component.translatable(CrateModule.crate.getDescriptionId());
    }

    @NotNull
    @Override
    protected AbstractContainerMenu createMenu(int id, @NotNull Inventory player) {
        return new CrateMenu(id, player, this, this.crateData);
    }

    @NotNull
    protected IItemHandler createUnSidedHandler() {
        return new CrateItemHandler();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.f_58857_.getBlockEntity(this.f_58858_) != this ? false : !(player.m_20275_((double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.5, (double) this.f_58858_.m_123343_() + 0.5) > 64.0);
    }

    @Override
    public void startOpen(Player player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            this.numPlayersUsing++;
            BlockState blockstate = this.m_58900_();
            boolean flag = (Boolean) blockstate.m_61143_(CrateBlock.PROPERTY_OPEN);
            if (!flag) {
                this.playSound(blockstate, SoundEvents.BARREL_OPEN);
                this.f_58857_.m_142346_(player, GameEvent.CONTAINER_OPEN, this.f_58858_);
                this.setOpenProperty(blockstate, true);
            }
            this.scheduleTick();
        }
    }

    private void scheduleTick() {
        this.f_58857_.m_186460_(this.m_58899_(), this.m_58900_().m_60734_(), 5);
    }

    public void crateTick() {
        int i = this.f_58858_.m_123341_();
        int j = this.f_58858_.m_123342_();
        int k = this.f_58858_.m_123343_();
        this.numPlayersUsing = calculatePlayersUsing(this.f_58857_, this, i, j, k);
        if (this.numPlayersUsing > 0) {
            this.scheduleTick();
        }
    }

    public static int calculatePlayersUsing(Level world, BaseContainerBlockEntity container, int x, int y, int z) {
        int i = 0;
        for (Player playerentity : world.m_45976_(Player.class, new AABB((double) ((float) x - 5.0F), (double) ((float) y - 5.0F), (double) ((float) z - 5.0F), (double) ((float) (x + 1) + 5.0F), (double) ((float) (y + 1) + 5.0F), (double) ((float) (z + 1) + 5.0F)))) {
            if (playerentity.containerMenu instanceof CrateMenu) {
                Container iinventory = ((CrateMenu) playerentity.containerMenu).crate;
                if (iinventory == container) {
                    i++;
                }
            }
        }
        return i;
    }

    @Override
    public void stopOpen(Player player) {
        if (!player.isSpectator()) {
            this.numPlayersUsing--;
        }
        if (this.numPlayersUsing <= 0) {
            BlockState blockstate = this.m_58900_();
            if (!blockstate.m_60713_(CrateModule.crate)) {
                this.m_7651_();
                return;
            }
            boolean flag = (Boolean) blockstate.m_61143_(CrateBlock.PROPERTY_OPEN);
            if (flag) {
                this.playSound(blockstate, SoundEvents.BARREL_CLOSE);
                this.f_58857_.m_142346_(player, GameEvent.CONTAINER_OPEN, this.f_58858_);
                this.setOpenProperty(blockstate, false);
            }
        }
    }

    private void setOpenProperty(BlockState state, boolean open) {
        BlockPos pos = this.m_58899_();
        BlockState prev = this.f_58857_.getBlockState(pos);
        if (prev.m_60713_(state.m_60734_())) {
            this.f_58857_.setBlock(pos, (BlockState) state.m_61124_(CrateBlock.PROPERTY_OPEN, open), 3);
        }
    }

    private void playSound(BlockState state, SoundEvent sound) {
        double d0 = (double) this.f_58858_.m_123341_() + 0.5;
        double d1 = (double) this.f_58858_.m_123342_() + 0.5;
        double d2 = (double) this.f_58858_.m_123343_() + 0.5;
        this.f_58857_.playSound(null, d0, d1, d2, sound, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.9F);
    }
}