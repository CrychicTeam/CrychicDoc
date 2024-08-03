package fr.frinn.custommachinery.common.util.slot;

import fr.frinn.custommachinery.common.component.ItemMachineComponent;
import fr.frinn.custommachinery.common.network.CSetFilterSlotItemPacket;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FilterSlotItemComponent extends SlotItemComponent {

    public FilterSlotItemComponent(ItemMachineComponent component, int index, int x, int y) {
        super(component, index, x, y);
    }

    public void setFromClient(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setCount(1);
        new CSetFilterSlotItemPacket(copy, this.getComponent().getManager().getTile().m_58899_(), this.getComponent().getId()).sendToServer();
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setCount(1);
        this.getComponent().setItemStack(copy);
        return false;
    }

    @Override
    public boolean mayPickup(Player player) {
        return true;
    }

    @Override
    public Optional<ItemStack> tryRemove(int count, int decrement, Player player) {
        this.getComponent().setItemStack(ItemStack.EMPTY);
        return Optional.empty();
    }
}