package net.mehvahdjukaar.supplementaries.common.block.tiles;

import java.util.stream.IntStream;
import net.mehvahdjukaar.moonlight.api.misc.IContainerProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public abstract class OpeneableContainerBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {

    private final ContainerOpenersCounter openersCounter = new OpeneableContainerBlockEntity.ContainerCounter();

    protected NonNullList<ItemStack> items;

    protected OpeneableContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, int size) {
        super(blockEntityType, pos, state);
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public void load(CompoundTag tag) {
        super.m_142466_(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.m_59631_(tag) && tag.contains("Items", 9)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.m_183515_(tag);
        if (!this.m_59634_(tag)) {
            ContainerHelper.saveAllItems(tag, this.items, false);
        }
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
    public int[] getSlotsForFace(Direction side) {
        return IntStream.range(0, this.getContainerSize()).toArray();
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Override
    public void startOpen(Player player) {
        if (!this.f_58859_ && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.f_58859_ && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    public void recheckOpen() {
        if (!this.f_58859_) {
            this.openersCounter.recheckOpeners(this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    protected abstract void updateBlockState(BlockState var1, boolean var2);

    protected abstract void playOpenSound(BlockState var1);

    protected abstract void playCloseSound(BlockState var1);

    public boolean isUnused() {
        return this.openersCounter.getOpenerCount() == 0;
    }

    private class ContainerCounter extends ContainerOpenersCounter {

        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            OpeneableContainerBlockEntity.this.playOpenSound(state);
            OpeneableContainerBlockEntity.this.updateBlockState(state, true);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            OpeneableContainerBlockEntity.this.playCloseSound(state);
            OpeneableContainerBlockEntity.this.updateBlockState(state, false);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int i, int i1) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            if (player.containerMenu instanceof IContainerProvider chestMenu) {
                Container container = chestMenu.getContainer();
                return container == OpeneableContainerBlockEntity.this;
            } else {
                return false;
            }
        }
    }
}