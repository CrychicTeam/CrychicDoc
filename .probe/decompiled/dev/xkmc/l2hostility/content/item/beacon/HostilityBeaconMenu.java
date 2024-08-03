package dev.xkmc.l2hostility.content.item.beacon;

import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2hostility.init.registrate.LHBlocks;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class HostilityBeaconMenu extends AbstractContainerMenu {

    private final Container beacon = new SimpleContainer(1) {

        @Override
        public boolean canPlaceItem(int p_39066_, ItemStack p_39067_) {
            return p_39067_.is(ItemTags.BEACON_PAYMENT_ITEMS);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    };

    private final HostilityBeaconMenu.PaymentSlot paymentSlot;

    private final ContainerLevelAccess access;

    private final ContainerData beaconData;

    public HostilityBeaconMenu(MenuType<HostilityBeaconMenu> type, int id, Inventory inv, @Nullable FriendlyByteBuf data) {
        this(type, id, inv, new SimpleContainerData(2), ContainerLevelAccess.NULL);
    }

    public HostilityBeaconMenu(MenuType<HostilityBeaconMenu> type, int id, Inventory cont, ContainerData data, ContainerLevelAccess access) {
        super(type, id);
        m_38886_(data, 2);
        this.beaconData = data;
        this.access = access;
        this.paymentSlot = new HostilityBeaconMenu.PaymentSlot(this.beacon, 0, 136, 110);
        this.m_38897_(this.paymentSlot);
        this.m_38884_(data);
        int i = 36;
        int j = 137;
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 9; l++) {
                this.m_38897_(new Slot(cont, l + k * 9 + 9, i + l * 18, j + k * 18));
            }
        }
        for (int i1 = 0; i1 < 9; i1++) {
            this.m_38897_(new Slot(cont, i1, i + i1 * 18, 195));
        }
    }

    @Override
    public void removed(Player player0) {
        super.removed(player0);
        if (!player0.m_9236_().isClientSide) {
            ItemStack itemstack = this.paymentSlot.m_6201_(this.paymentSlot.getMaxStackSize());
            if (!itemstack.isEmpty()) {
                player0.drop(itemstack, false);
            }
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return m_38889_(this.access, player0, (Block) LHBlocks.HOSTILITY_BEACON.get());
    }

    @Override
    public void setData(int int0, int int1) {
        super.setData(int0, int1);
        this.m_38946_();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ans = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            ans = slotItem.copy();
            if (index == 0) {
                if (!this.m_38903_(slotItem, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotItem, ans);
            } else {
                if (this.m_38903_(slotItem, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
                if (index >= 1 && index < 28) {
                    if (!this.m_38903_(slotItem, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 28 && index < 37) {
                    if (!this.m_38903_(slotItem, 1, 28, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.m_38903_(slotItem, 1, 37, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (slotItem.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (slotItem.getCount() == ans.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotItem);
        }
        return ans;
    }

    public int getLevels() {
        return this.beaconData.get(0);
    }

    public int getPrimaryEffect() {
        return this.beaconData.get(1);
    }

    @Override
    public boolean clickMenuButton(Player player, int btn) {
        if (btn >= 0 && btn < 6 && btn / 2 < this.getLevels()) {
            this.updateEffects(btn);
            return true;
        } else {
            return super.clickMenuButton(player, btn);
        }
    }

    public void updateEffects(int opt) {
        if (this.paymentSlot.m_6657_()) {
            this.beaconData.set(1, opt);
            this.paymentSlot.m_6201_(1);
            this.access.execute(Level::m_151543_);
        }
    }

    public boolean hasPayment() {
        return !this.beacon.getItem(0).isEmpty();
    }

    static class PaymentSlot extends Slot {

        public PaymentSlot(Container container0, int int1, int int2, int int3) {
            super(container0, int1, int2, int3);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack0) {
            return itemStack0.is(LHTagGen.BEACON_PAYMENT);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}