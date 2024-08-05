package io.github.lightman314.lightmanscurrency.common.menus;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import io.github.lightman314.lightmanscurrency.common.emergency_ejection.EjectionData;
import io.github.lightman314.lightmanscurrency.common.emergency_ejection.EjectionSaveData;
import io.github.lightman314.lightmanscurrency.common.menus.containers.SuppliedContainer;
import io.github.lightman314.lightmanscurrency.common.menus.slots.OutputSlot;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class EjectionRecoveryMenu extends LazyMessageMenu {

    public static final MenuProvider PROVIDER = new EjectionRecoveryMenu.Provider();

    private int selectedIndex = 0;

    private final Container dummyContainer = new SimpleContainer(54);

    public EjectionRecoveryMenu(int menuID, Inventory inventory) {
        this(ModMenus.EJECTION_RECOVERY.get(), menuID, inventory);
    }

    public List<EjectionData> getValidEjectionData() {
        return EjectionSaveData.GetValidEjectionData(this.isClient(), this.player);
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public EjectionData getSelectedData() {
        List<EjectionData> data = this.getValidEjectionData();
        return !data.isEmpty() && this.selectedIndex >= 0 && this.selectedIndex < data.size() ? (EjectionData) data.get(this.selectedIndex) : null;
    }

    private Container getSelectedContainer() {
        List<EjectionData> data = this.getValidEjectionData();
        this.changeSelection(this.selectedIndex);
        return !data.isEmpty() && this.selectedIndex >= 0 && this.selectedIndex < data.size() ? (Container) data.get(this.selectedIndex) : this.dummyContainer;
    }

    protected EjectionRecoveryMenu(MenuType<?> type, int menuID, Inventory inventory) {
        super(type, menuID, inventory);
        Container ejectionContainer = new SuppliedContainer(this::getSelectedContainer);
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 9; x++) {
                this.m_38897_(new OutputSlot(ejectionContainer, x + y * 9, 8 + x * 18, 18 + y * 18));
            }
        }
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.m_38897_(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 140 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            this.m_38897_(new Slot(inventory, x, 8 + x * 18, 198));
        }
    }

    @Override
    public void HandleMessage(@Nonnull LazyPacketData message) {
        if (message.contains("ChangeSelection", (byte) 2)) {
            this.changeSelection(message.getInt("ChangeSelection"));
        }
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotIndex < 54) {
                if (!this.m_38903_(itemstack1, 54, this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 0, 54, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void removed(@Nonnull Player player) {
        super.m_6877_(player);
        this.m_150411_(player, this.dummyContainer);
    }

    public void changeSelection(int newSelection) {
        this.changeSelection(newSelection, this.getValidEjectionData().size());
    }

    private void changeSelection(int newSelection, int dataSize) {
        if (this.isClient()) {
            this.SendMessage(LazyPacketData.simpleInt("ChangeSelection", this.selectedIndex));
        } else {
            int oldSelection = this.selectedIndex;
            this.selectedIndex = MathUtil.clamp(newSelection, 0, dataSize - 1);
            if (this.selectedIndex != oldSelection && this.isServer()) {
                this.SendMessage(LazyPacketData.simpleInt("ChangeSelection", this.selectedIndex));
            }
        }
    }

    private static class Provider implements MenuProvider {

        @Override
        public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inventory, @Nonnull Player player) {
            return new EjectionRecoveryMenu(id, inventory);
        }

        @Nonnull
        @Override
        public Component getDisplayName() {
            return Component.empty();
        }
    }
}