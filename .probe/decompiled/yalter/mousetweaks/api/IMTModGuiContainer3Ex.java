package yalter.mousetweaks.api;

import java.util.List;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public interface IMTModGuiContainer3Ex {

    boolean MT_isMouseTweaksDisabled();

    boolean MT_isWheelTweakDisabled();

    List<Slot> MT_getSlots();

    Slot MT_getSlotUnderMouse(double var1, double var3);

    boolean MT_isCraftingOutput(Slot var1);

    boolean MT_isIgnored(Slot var1);

    boolean MT_disableRMBDraggingFunctionality();

    void MT_clickSlot(Slot var1, int var2, ClickType var3);
}