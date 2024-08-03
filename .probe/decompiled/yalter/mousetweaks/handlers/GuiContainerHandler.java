package yalter.mousetweaks.handlers;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.MerchantResultSlot;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import yalter.mousetweaks.IGuiScreenHandler;
import yalter.mousetweaks.MouseButton;
import yalter.mousetweaks.api.MouseTweaksDisableWheelTweak;
import yalter.mousetweaks.api.MouseTweaksIgnore;
import yalter.mousetweaks.mixin.AbstractContainerScreenAccessor;

public class GuiContainerHandler implements IGuiScreenHandler {

    Minecraft mc = Minecraft.getInstance();

    private final AbstractContainerScreen screen;

    private final AbstractContainerScreenAccessor screenAccessor;

    public GuiContainerHandler(AbstractContainerScreen screen) {
        this.screen = screen;
        this.screenAccessor = (AbstractContainerScreenAccessor) screen;
    }

    @Override
    public boolean isMouseTweaksDisabled() {
        return this.screen.getClass().isAnnotationPresent(MouseTweaksIgnore.class);
    }

    @Override
    public boolean isWheelTweakDisabled() {
        return this.screen.getClass().isAnnotationPresent(MouseTweaksDisableWheelTweak.class);
    }

    @Override
    public List<Slot> getSlots() {
        return this.screen.getMenu().slots;
    }

    @Override
    public Slot getSlotUnderMouse(double mouseX, double mouseY) {
        return this.screenAccessor.mousetweaks$invokeFindSlot(mouseX, mouseY);
    }

    @Override
    public boolean disableRMBDraggingFunctionality() {
        this.screenAccessor.mousetweaks$setSkipNextRelease(true);
        if (this.screenAccessor.mousetweaks$getIsQuickCrafting() && this.screenAccessor.mousetweaks$getQuickCraftingButton() == 1) {
            this.screenAccessor.mousetweaks$setIsQuickCrafting(false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clickSlot(Slot slot, MouseButton mouseButton, boolean shiftPressed) {
        this.screenAccessor.mousetweaks$invokeSlotClicked(slot, slot.index, mouseButton.getValue(), shiftPressed ? ClickType.QUICK_MOVE : ClickType.PICKUP);
    }

    @Override
    public boolean isCraftingOutput(Slot slot) {
        return slot instanceof ResultSlot || slot instanceof FurnaceResultSlot || slot instanceof MerchantResultSlot;
    }

    @Override
    public boolean isIgnored(Slot slot) {
        return false;
    }
}