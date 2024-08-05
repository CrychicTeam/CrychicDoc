package yalter.mousetweaks;

import java.util.List;
import net.minecraft.world.inventory.Slot;

public interface IGuiScreenHandler {

    boolean isMouseTweaksDisabled();

    boolean isWheelTweakDisabled();

    List<Slot> getSlots();

    Slot getSlotUnderMouse(double var1, double var3);

    boolean disableRMBDraggingFunctionality();

    void clickSlot(Slot var1, MouseButton var2, boolean var3);

    boolean isCraftingOutput(Slot var1);

    boolean isIgnored(Slot var1);
}