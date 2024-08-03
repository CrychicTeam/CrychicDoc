package noppes.npcs.containers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomContainer;
import noppes.npcs.EventHooks;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.gui.IItemSlot;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.api.wrapper.gui.GuiComponentsScrollableWrapper;
import noppes.npcs.client.gui.custom.components.CustomGuiSlot;
import noppes.npcs.mixin.AbstractContainerMenuMixin;
import noppes.npcs.util.CustomNPCsScheduler;

public class ContainerCustomGui extends AbstractContainerMenu {

    public CustomGuiWrapper customGui;

    public CustomGuiWrapper activeGui;

    public SimpleContainer guiInventory;

    public CompoundTag data;

    public ContainerCustomGui(int containerId, CompoundTag data) {
        super(CustomContainer.container_customgui, containerId);
        this.data = data;
        this.guiInventory = new SimpleContainer(0);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    public void setGui(CustomGuiWrapper gui, Player player) {
        this.activeGui = gui.getActiveGui();
        this.guiInventory = new SimpleContainer(this.activeGui.getSlots().size() + this.activeGui.getScrollingPanel().getSlots().size());
        this.customGui = gui;
        AbstractContainerMenuMixin mix = (AbstractContainerMenuMixin) this;
        this.f_38839_.clear();
        mix.remoteSlots().clear();
        mix.lastSlots().clear();
        for (IItemSlot slot : this.activeGui.getSlots()) {
            Slot s = this.m_38897_(new CustomGuiSlot(gui, this.guiInventory, slot.getID(), slot, player));
            this.guiInventory.setItem(s.index, slot.getStack().getMCItemStack());
        }
        GuiComponentsScrollableWrapper panel = this.activeGui.getScrollingPanel();
        for (IItemSlot slot : panel.getSlots()) {
            Slot s = this.m_38897_(new CustomGuiSlot(gui, this.guiInventory, slot.getID(), slot, player).update(panel.x, panel.y));
            this.guiInventory.setItem(s.index, slot.getStack().getMCItemStack());
        }
        for (IItemSlot slot : this.activeGui.getPlayerSlots()) {
            this.m_38897_(new CustomGuiSlot(gui, player.getInventory(), slot.getID(), slot, player));
        }
        this.update();
    }

    public void update() {
        GuiComponentsScrollableWrapper panel = this.activeGui.getScrollingPanel();
        for (int i = 0; i < this.activeGui.getScrollingPanel().getSlots().size(); i++) {
            CustomGuiSlot slot = (CustomGuiSlot) this.m_38853_(i + this.activeGui.getSlots().size());
            if (panel.isVisible(slot.slot)) {
                slot.update(panel.x, panel.y - panel.scrollAmount);
            } else {
                slot.update(-1073741824, -1073741824);
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.guiInventory.getContainerSize()) {
                if (!this.m_38903_(itemstack1, this.guiInventory.getContainerSize(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 0, this.guiInventory.getContainerSize(), false)) {
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
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId < 0) {
            super.clicked(slotId, dragType, clickTypeIn, player);
        } else {
            if (!player.m_9236_().isClientSide) {
                CustomGuiSlot slot = (CustomGuiSlot) this.m_38853_(slotId);
                if (!EventHooks.onCustomGuiSlotClicked((PlayerWrapper) NpcAPI.Instance().getIEntity(player), ((ContainerCustomGui) player.containerMenu).activeGui, slot.slot, dragType, clickTypeIn.toString())) {
                    super.clicked(slotId, dragType, clickTypeIn, player);
                    CustomNPCsScheduler.runTack(this::m_150429_, 10);
                }
            }
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.m_9236_().isClientSide) {
            EventHooks.onCustomGuiClose((PlayerWrapper) NpcAPI.Instance().getIEntity(player), this.customGui);
        }
    }
}