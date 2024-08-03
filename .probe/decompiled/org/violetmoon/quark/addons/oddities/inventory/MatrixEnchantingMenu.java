package org.violetmoon.quark.addons.oddities.inventory;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.block.be.MatrixEnchantingTableBlockEntity;
import org.violetmoon.quark.addons.oddities.module.MatrixEnchantingModule;

public class MatrixEnchantingMenu extends AbstractContainerMenu {

    public final MatrixEnchantingTableBlockEntity enchanter;

    public MatrixEnchantingMenu(int id, Inventory playerInv, MatrixEnchantingTableBlockEntity tile) {
        super(MatrixEnchantingModule.menuType, id);
        this.enchanter = tile;
        this.m_38897_(new Slot(tile, 0, 15, 20) {

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.m_38897_(new Slot(tile, 1, 15, 44) {

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return MatrixEnchantingMenu.this.isLapis(stack);
            }
        });
        this.m_38897_(new Slot(tile, 2, 59, 32) {

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NotNull Player thePlayer, @NotNull ItemStack stack) {
                MatrixEnchantingMenu.this.finish(thePlayer, stack);
                super.onTake(thePlayer, stack);
            }
        });
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.m_38897_(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; k++) {
            this.m_38897_(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    public static MatrixEnchantingMenu fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        MatrixEnchantingTableBlockEntity te = (MatrixEnchantingTableBlockEntity) playerInventory.player.m_9236_().getBlockEntity(pos);
        return new MatrixEnchantingMenu(windowId, playerInventory, te);
    }

    private boolean isLapis(ItemStack stack) {
        return stack.is(Tags.Items.GEMS_LAPIS);
    }

    private void finish(Player player, ItemStack stack) {
        this.enchanter.m_6836_(0, ItemStack.EMPTY);
        player.awardStat(Stats.ENCHANT_ITEM);
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.ENCHANTED_ITEM.trigger(serverPlayer, stack, 1);
            if (this.enchanter.isMatrixInfluenced()) {
                MatrixEnchantingModule.influenceTrigger.trigger(serverPlayer);
            }
        }
        player.m_9236_().playSound(null, this.enchanter.m_58899_(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, player.m_9236_().random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean stillValid(@NotNull Player playerIn) {
        Level world = this.enchanter.m_58904_();
        BlockPos pos = this.enchanter.m_58899_();
        return world.getBlockState(pos).m_60734_() != MatrixEnchantingModule.matrixEnchanter ? false : playerIn.m_20275_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5) <= 64.0;
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            originalStack = stackInSlot.copy();
            if (index < 3) {
                if (!this.m_38903_(stackInSlot, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.isLapis(stackInSlot)) {
                if (!this.m_38903_(stackInSlot, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (((Slot) this.f_38839_.get(0)).hasItem() || !((Slot) this.f_38839_.get(0)).mayPlace(stackInSlot)) {
                    return ItemStack.EMPTY;
                }
                if (stackInSlot.hasTag()) {
                    ((Slot) this.f_38839_.get(0)).set(stackInSlot.split(1));
                } else if (!stackInSlot.isEmpty()) {
                    ((Slot) this.f_38839_.get(0)).set(new ItemStack(stackInSlot.getItem(), 1));
                    stackInSlot.shrink(1);
                }
            }
            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stackInSlot.getCount() == originalStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, stackInSlot);
        }
        return originalStack;
    }
}