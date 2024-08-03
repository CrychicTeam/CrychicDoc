package net.minecraft.world.inventory;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;

public class PlayerEnderChestContainer extends SimpleContainer {

    @Nullable
    private EnderChestBlockEntity activeChest;

    public PlayerEnderChestContainer() {
        super(27);
    }

    public void setActiveChest(EnderChestBlockEntity enderChestBlockEntity0) {
        this.activeChest = enderChestBlockEntity0;
    }

    public boolean isActiveChest(EnderChestBlockEntity enderChestBlockEntity0) {
        return this.activeChest == enderChestBlockEntity0;
    }

    @Override
    public void fromTag(ListTag listTag0) {
        for (int $$1 = 0; $$1 < this.m_6643_(); $$1++) {
            this.m_6836_($$1, ItemStack.EMPTY);
        }
        for (int $$2 = 0; $$2 < listTag0.size(); $$2++) {
            CompoundTag $$3 = listTag0.getCompound($$2);
            int $$4 = $$3.getByte("Slot") & 255;
            if ($$4 >= 0 && $$4 < this.m_6643_()) {
                this.m_6836_($$4, ItemStack.of($$3));
            }
        }
    }

    @Override
    public ListTag createTag() {
        ListTag $$0 = new ListTag();
        for (int $$1 = 0; $$1 < this.m_6643_(); $$1++) {
            ItemStack $$2 = this.m_8020_($$1);
            if (!$$2.isEmpty()) {
                CompoundTag $$3 = new CompoundTag();
                $$3.putByte("Slot", (byte) $$1);
                $$2.save($$3);
                $$0.add($$3);
            }
        }
        return $$0;
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.activeChest != null && !this.activeChest.stillValid(player0) ? false : super.stillValid(player0);
    }

    @Override
    public void startOpen(Player player0) {
        if (this.activeChest != null) {
            this.activeChest.startOpen(player0);
        }
        super.m_5856_(player0);
    }

    @Override
    public void stopOpen(Player player0) {
        if (this.activeChest != null) {
            this.activeChest.stopOpen(player0);
        }
        super.m_5785_(player0);
        this.activeChest = null;
    }
}