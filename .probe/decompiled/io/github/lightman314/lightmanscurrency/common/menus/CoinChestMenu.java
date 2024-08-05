package io.github.lightman314.lightmanscurrency.common.menus;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.upgrades.slot.UpgradeInputSlot;
import io.github.lightman314.lightmanscurrency.common.blockentity.CoinChestBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import io.github.lightman314.lightmanscurrency.common.menus.slots.CoinSlot;
import io.github.lightman314.lightmanscurrency.common.menus.slots.SimpleSlot;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.BlockEntityValidator;
import io.github.lightman314.lightmanscurrency.common.upgrades.types.coin_chest.CoinChestUpgradeData;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CoinChestMenu extends LazyMessageMenu {

    public final CoinChestBlockEntity be;

    private final List<CoinSlot> coinSlots;

    private final List<UpgradeInputSlot> upgradeSlots;

    private final List<SimpleSlot> inventorySlots;

    private Consumer<LazyPacketData> extraHandler = d -> {
    };

    public CoinChestMenu(int id, Inventory inventory, CoinChestBlockEntity be) {
        super(ModMenus.COIN_CHEST.get(), id, inventory);
        this.be = be;
        this.be.startOpen(this.player);
        this.addValidator(BlockEntityValidator.of(be));
        this.addValidator(this.be::allowAccess);
        List<CoinSlot> cSlots = new ArrayList();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                CoinSlot s = new CoinSlot(this.be.getStorage(), x + 9 * y, 8 + x * 18, 93 + y * 18, true);
                this.m_38897_(s);
                cSlots.add(s);
            }
        }
        this.coinSlots = ImmutableList.copyOf(cSlots);
        List<UpgradeInputSlot> uSlots = new ArrayList();
        for (int y = 0; y < 3; y++) {
            UpgradeInputSlot s = new UpgradeInputSlot(this.be.getUpgrades(), y, 152, 21 + y * 18, this.be);
            s.setListener(() -> this.be.checkUpgradeEquipped(y));
            this.m_38897_(s);
            uSlots.add(s);
        }
        this.upgradeSlots = ImmutableList.copyOf(uSlots);
        List<SimpleSlot> iSlots = new ArrayList();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                SimpleSlot s = new SimpleSlot(inventory, x + y * 9 + 9, 8 + x * 18, 161 + y * 18);
                iSlots.add(s);
                this.m_38897_(s);
            }
        }
        for (int x = 0; x < 9; x++) {
            SimpleSlot s = new SimpleSlot(inventory, x, 8 + x * 18, 219);
            iSlots.add(s);
            this.m_38897_(s);
        }
        this.inventorySlots = ImmutableList.copyOf(iSlots);
    }

    public void SetUpgradeSlotVisibility(boolean visible) {
        for (UpgradeInputSlot slot : this.upgradeSlots) {
            slot.active = visible;
        }
    }

    public void SetCoinSlotVisibility(boolean visible) {
        for (CoinSlot slot : this.coinSlots) {
            slot.active = visible;
        }
    }

    public void SetInventoryVisibility(boolean visible) {
        for (SimpleSlot slot : this.inventorySlots) {
            slot.active = visible;
        }
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            clickedStack = slotStack.copy();
            if (index < 30) {
                if (!this.m_38903_(slotStack, 30, this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(slotStack, 0, 30, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return clickedStack;
    }

    @Override
    public void removed(@Nonnull Player player) {
        super.m_6877_(player);
        this.be.stopOpen(player);
    }

    public final void AddExtraHandler(@Nonnull Consumer<LazyPacketData> extraHandler) {
        this.extraHandler = extraHandler;
    }

    @Override
    public void HandleMessage(@Nonnull LazyPacketData message) {
        this.extraHandler.accept(message);
        UnmodifiableIterator var2 = this.be.getChestUpgrades().iterator();
        while (var2.hasNext()) {
            CoinChestUpgradeData data = (CoinChestUpgradeData) var2.next();
            data.upgrade.HandleMenuMessage(this, data, message);
        }
    }
}