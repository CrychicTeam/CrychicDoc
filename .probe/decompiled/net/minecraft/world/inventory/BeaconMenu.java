package net.minecraft.world.inventory;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class BeaconMenu extends AbstractContainerMenu {

    private static final int PAYMENT_SLOT = 0;

    private static final int SLOT_COUNT = 1;

    private static final int DATA_COUNT = 3;

    private static final int INV_SLOT_START = 1;

    private static final int INV_SLOT_END = 28;

    private static final int USE_ROW_SLOT_START = 28;

    private static final int USE_ROW_SLOT_END = 37;

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

    private final BeaconMenu.PaymentSlot paymentSlot;

    private final ContainerLevelAccess access;

    private final ContainerData beaconData;

    public BeaconMenu(int int0, Container container1) {
        this(int0, container1, new SimpleContainerData(3), ContainerLevelAccess.NULL);
    }

    public BeaconMenu(int int0, Container container1, ContainerData containerData2, ContainerLevelAccess containerLevelAccess3) {
        super(MenuType.BEACON, int0);
        m_38886_(containerData2, 3);
        this.beaconData = containerData2;
        this.access = containerLevelAccess3;
        this.paymentSlot = new BeaconMenu.PaymentSlot(this.beacon, 0, 136, 110);
        this.m_38897_(this.paymentSlot);
        this.m_38884_(containerData2);
        int $$4 = 36;
        int $$5 = 137;
        for (int $$6 = 0; $$6 < 3; $$6++) {
            for (int $$7 = 0; $$7 < 9; $$7++) {
                this.m_38897_(new Slot(container1, $$7 + $$6 * 9 + 9, 36 + $$7 * 18, 137 + $$6 * 18));
            }
        }
        for (int $$8 = 0; $$8 < 9; $$8++) {
            this.m_38897_(new Slot(container1, $$8, 36 + $$8 * 18, 195));
        }
    }

    @Override
    public void removed(Player player0) {
        super.removed(player0);
        if (!player0.m_9236_().isClientSide) {
            ItemStack $$1 = this.paymentSlot.m_6201_(this.paymentSlot.getMaxStackSize());
            if (!$$1.isEmpty()) {
                player0.drop($$1, false);
            }
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return m_38889_(this.access, player0, Blocks.BEACON);
    }

    @Override
    public void setData(int int0, int int1) {
        super.setData(int0, int1);
        this.m_38946_();
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.f_38839_.get(int1);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            $$2 = $$4.copy();
            if (int1 == 0) {
                if (!this.m_38903_($$4, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                $$3.onQuickCraft($$4, $$2);
            } else if (!this.paymentSlot.m_6657_() && this.paymentSlot.mayPlace($$4) && $$4.getCount() == 1) {
                if (!this.m_38903_($$4, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (int1 >= 1 && int1 < 28) {
                if (!this.m_38903_($$4, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (int1 >= 28 && int1 < 37) {
                if (!this.m_38903_($$4, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_($$4, 1, 37, false)) {
                return ItemStack.EMPTY;
            }
            if ($$4.isEmpty()) {
                $$3.setByPlayer(ItemStack.EMPTY);
            } else {
                $$3.setChanged();
            }
            if ($$4.getCount() == $$2.getCount()) {
                return ItemStack.EMPTY;
            }
            $$3.onTake(player0, $$4);
        }
        return $$2;
    }

    public int getLevels() {
        return this.beaconData.get(0);
    }

    @Nullable
    public MobEffect getPrimaryEffect() {
        return MobEffect.byId(this.beaconData.get(1));
    }

    @Nullable
    public MobEffect getSecondaryEffect() {
        return MobEffect.byId(this.beaconData.get(2));
    }

    public void updateEffects(Optional<MobEffect> optionalMobEffect0, Optional<MobEffect> optionalMobEffect1) {
        if (this.paymentSlot.m_6657_()) {
            this.beaconData.set(1, (Integer) optionalMobEffect0.map(MobEffect::m_19459_).orElse(-1));
            this.beaconData.set(2, (Integer) optionalMobEffect1.map(MobEffect::m_19459_).orElse(-1));
            this.paymentSlot.m_6201_(1);
            this.access.execute(Level::m_151543_);
        }
    }

    public boolean hasPayment() {
        return !this.beacon.getItem(0).isEmpty();
    }

    class PaymentSlot extends Slot {

        public PaymentSlot(Container container0, int int1, int int2, int int3) {
            super(container0, int1, int2, int3);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack0) {
            return itemStack0.is(ItemTags.BEACON_PAYMENT_ITEMS);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}