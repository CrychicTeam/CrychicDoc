package net.mehvahdjukaar.supplementaries.common.block.tiles;

import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.TrappedPresentBlock;
import net.mehvahdjukaar.supplementaries.common.block.present.IPresentItemBehavior;
import net.mehvahdjukaar.supplementaries.common.inventories.TrappedPresentContainerMenu;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TrappedPresentBlockTile extends AbstractPresentBlockTile {

    public TrappedPresentBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.TRAPPED_PRESENT_TILE.get(), pos, state);
    }

    @Override
    public boolean canHoldItems() {
        return this.isPrimed();
    }

    public static boolean isPrimed(ItemStack stack) {
        CompoundTag com = stack.getTag();
        if (com != null) {
            CompoundTag tag = com.getCompound("BlockEntityTag");
            return tag.contains("Items");
        } else {
            return false;
        }
    }

    public boolean isPrimed() {
        return (Boolean) this.m_58900_().m_61143_(TrappedPresentBlock.PACKED);
    }

    public void updateState(boolean primed) {
        if (!this.f_58857_.isClientSide && this.isPrimed() != primed) {
            if (primed) {
                this.f_58857_.playSound(null, this.f_58858_, (SoundEvent) ModSounds.PRESENT_PACK.get(), SoundSource.BLOCKS, 1.0F, this.f_58857_.random.nextFloat() * 0.1F + 0.95F);
            } else {
                this.f_58857_.playSound(null, this.f_58858_, (SoundEvent) ModSounds.PRESENT_BREAK.get(), SoundSource.BLOCKS, 0.75F, this.f_58857_.random.nextFloat() * 0.1F + 1.2F);
            }
            this.f_58857_.setBlock(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(TrappedPresentBlock.PACKED, primed), 3);
        }
    }

    @Override
    public boolean canOpen(Player player) {
        if (!super.m_7525_(player)) {
            return false;
        } else {
            return !this.isUnused() ? false : !this.isPrimed();
        }
    }

    @Override
    public InteractionResult interact(Level level, BlockPos pos, BlockState state, Player player) {
        if ((Boolean) state.m_61143_(TrappedPresentBlock.ON_COOLDOWN)) {
            return InteractionResult.FAIL;
        } else if (this.isUnused()) {
            if (this.canOpen(player)) {
                if (player instanceof ServerPlayer serverPlayer) {
                    PlatHelper.openCustomMenu(serverPlayer, this, pos);
                    PiglinAi.angerNearbyPiglins(player, true);
                }
            } else {
                level.setBlockAndUpdate(pos, (BlockState) state.m_61124_(TrappedPresentBlock.ON_COOLDOWN, true));
                level.m_186460_(pos, state.m_60734_(), 10);
                if (level instanceof ServerLevel sl) {
                    this.detonate(sl, pos);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    public void detonate(ServerLevel level, BlockPos pos) {
        BlockSourceImpl blocksourceimpl = new BlockSourceImpl(level, pos);
        ItemStack stack = this.m_8020_(0);
        IPresentItemBehavior presentItemBehavior = TrappedPresentBlock.getPresentBehavior(stack);
        this.updateState(false);
        presentItemBehavior.trigger(blocksourceimpl, stack);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("gui.supplementaries.trapped_present");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv) {
        return inv.player.isSpectator() ? null : new TrappedPresentContainerMenu(id, inv, this);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}