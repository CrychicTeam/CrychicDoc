package net.mehvahdjukaar.supplementaries.common.block.tiles;

import net.mehvahdjukaar.supplementaries.common.block.blocks.SafeBlock;
import net.mehvahdjukaar.supplementaries.common.inventories.SackContainerMenu;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SackBlockTile extends OpeneableContainerBlockEntity {

    public SackBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.SACK_TILE.get(), pos, state, 27);
    }

    @Override
    public int getContainerSize() {
        return getUnlockedSlots();
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("gui.supplementaries.sack");
    }

    @Override
    protected void playOpenSound(BlockState state) {
        double d0 = (double) this.f_58858_.m_123341_() + 0.5;
        double d1 = (double) this.f_58858_.m_123342_() + 1.0;
        double d2 = (double) this.f_58858_.m_123343_() + 0.5;
        this.f_58857_.playSound(null, d0, d1, d2, (SoundEvent) ModSounds.SACK_OPEN.get(), SoundSource.BLOCKS, 1.0F, this.f_58857_.random.nextFloat() * 0.1F + 0.95F);
    }

    @Override
    protected void playCloseSound(BlockState state) {
        double d0 = (double) this.f_58858_.m_123341_() + 0.5;
        double d1 = (double) this.f_58858_.m_123342_() + 1.0;
        double d2 = (double) this.f_58858_.m_123343_() + 0.5;
        this.f_58857_.playSound(null, d0, d1, d2, (SoundEvent) ModSounds.SACK_OPEN.get(), SoundSource.BLOCKS, 1.0F, this.f_58857_.random.nextFloat() * 0.1F + 0.8F);
    }

    @Override
    protected void updateBlockState(BlockState state, boolean open) {
        this.f_58857_.setBlock(this.m_58899_(), (BlockState) state.m_61124_(SafeBlock.OPEN, open), 3);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv) {
        return inv.player.isSpectator() ? null : new SackContainerMenu(id, inv, this);
    }

    public static int getUnlockedSlots() {
        return (Integer) CommonConfigs.Functional.SACK_SLOTS.get();
    }

    public boolean isSlotUnlocked(int ind) {
        return ind < getUnlockedSlots();
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return this.isSlotUnlocked(index) && MiscUtils.isAllowedInShulker(stack, this.m_58904_());
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return this.isSlotUnlocked(index);
    }

    public boolean acceptsTransfer(Player player) {
        return true;
    }
}