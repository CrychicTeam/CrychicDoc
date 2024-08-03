package noppes.npcs.api.gui;

import net.minecraft.world.inventory.Slot;
import noppes.npcs.api.function.gui.GuiItemSlotUpdate;
import noppes.npcs.api.item.IItemStack;

public interface IItemSlot extends ICustomGuiComponent {

    boolean hasStack();

    IItemStack getStack();

    IItemSlot setStack(IItemStack var1);

    int getGuiType();

    IItemSlot setGuiType(int var1);

    boolean isPlayerSlot();

    IItemSlot setOnUpdate(GuiItemSlotUpdate var1);

    Slot getMCSlot();
}