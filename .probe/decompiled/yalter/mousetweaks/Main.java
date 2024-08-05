package yalter.mousetweaks;

import com.mojang.blaze3d.platform.InputConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import yalter.mousetweaks.api.IMTModGuiContainer3Ex;
import yalter.mousetweaks.handlers.GuiContainerCreativeHandler;
import yalter.mousetweaks.handlers.GuiContainerHandler;
import yalter.mousetweaks.handlers.IMTModGuiContainer3ExHandler;

public class Main {

    public static Config config;

    private static Minecraft mc;

    private static Screen openScreen = null;

    private static IGuiScreenHandler handler = null;

    private static boolean disableWheelForThisContainer = false;

    private static Slot oldSelectedSlot = null;

    private static double accumulatedScrollDelta = 0.0;

    private static boolean canDoLMBDrag = false;

    private static boolean canDoRMBDrag = false;

    private static boolean rmbTweakLeftOriginalSlot = false;

    private static boolean initialized = false;

    public static void initialize() {
        Logger.Log("Main.initialize()");
        if (!initialized) {
            mc = Minecraft.getInstance();
            config = new Config(mc.gameDirectory.getAbsolutePath() + File.separator + "config" + File.separator + "MouseTweaks.cfg");
            config.read();
            Logger.Log("Initialized.");
            initialized = true;
        }
    }

    private static void updateScreen(Screen newScreen) {
        if (newScreen != openScreen) {
            openScreen = newScreen;
            handler = null;
            oldSelectedSlot = null;
            accumulatedScrollDelta = 0.0;
            canDoLMBDrag = false;
            canDoRMBDrag = false;
            rmbTweakLeftOriginalSlot = false;
            if (openScreen != null) {
                Logger.DebugLog("You have just opened a " + openScreen.getClass().getName() + ".");
                config.read();
                handler = findHandler(openScreen);
                if (handler == null) {
                    Logger.DebugLog("No valid handler found; Mouse Tweaks is disabled.");
                } else {
                    boolean disableForThisContainer = handler.isMouseTweaksDisabled();
                    disableWheelForThisContainer = handler.isWheelTweakDisabled();
                    Logger.DebugLog("Handler: " + handler.getClass().getSimpleName() + "; Mouse Tweaks is " + (disableForThisContainer ? "disabled" : "enabled") + "; wheel tweak is " + (disableWheelForThisContainer ? "disabled" : "enabled") + ".");
                    if (disableForThisContainer) {
                        handler = null;
                    }
                }
            }
        }
    }

    public static boolean onMouseClicked(Screen screen, double x, double y, MouseButton button) {
        updateScreen(screen);
        if (handler == null) {
            return false;
        } else {
            oldSelectedSlot = handler.getSlotUnderMouse(x, y);
            ItemStack stackOnMouse = mc.player.f_36096_.getCarried();
            if (button == MouseButton.LEFT) {
                if (stackOnMouse.isEmpty()) {
                    canDoLMBDrag = true;
                }
            } else if (button == MouseButton.RIGHT) {
                if (stackOnMouse.isEmpty()) {
                    return false;
                }
                if (!config.rmbTweak) {
                    return false;
                }
                canDoRMBDrag = true;
                rmbTweakLeftOriginalSlot = false;
            }
            return false;
        }
    }

    private static void rmbTweakMaybeClickSlot(Slot slot, ItemStack stackOnMouse) {
        if (slot != null) {
            if (!stackOnMouse.isEmpty()) {
                if (!handler.isIgnored(slot)) {
                    if (!handler.isCraftingOutput(slot)) {
                        if (!(stackOnMouse.getItem() instanceof BundleItem)) {
                            ItemStack selectedSlotStack = slot.getItem();
                            if (!areStacksCompatible(selectedSlotStack, stackOnMouse)) {
                                return;
                            }
                            if (selectedSlotStack.getCount() == slot.getMaxStackSize(selectedSlotStack)) {
                                return;
                            }
                        }
                        handler.clickSlot(slot, MouseButton.RIGHT, false);
                    }
                }
            }
        }
    }

    public static boolean onMouseReleased(Screen screen, double x, double y, MouseButton button) {
        updateScreen(screen);
        if (handler == null) {
            return false;
        } else {
            if (button == MouseButton.LEFT) {
                canDoLMBDrag = false;
            } else if (button == MouseButton.RIGHT) {
                canDoRMBDrag = false;
            }
            return false;
        }
    }

    public static boolean onMouseDrag(Screen screen, double x, double y, MouseButton button) {
        updateScreen(screen);
        if (handler == null) {
            return false;
        } else {
            Slot selectedSlot = handler.getSlotUnderMouse(x, y);
            if (selectedSlot == oldSelectedSlot) {
                return false;
            } else {
                ItemStack stackOnMouse = mc.player.f_36096_.getCarried();
                if (canDoRMBDrag && button == MouseButton.RIGHT && !rmbTweakLeftOriginalSlot) {
                    rmbTweakLeftOriginalSlot = true;
                    handler.disableRMBDraggingFunctionality();
                    rmbTweakMaybeClickSlot(oldSelectedSlot, stackOnMouse);
                }
                oldSelectedSlot = selectedSlot;
                if (selectedSlot == null) {
                    return false;
                } else if (handler.isIgnored(selectedSlot)) {
                    return false;
                } else {
                    if (button == MouseButton.LEFT) {
                        if (!canDoLMBDrag) {
                            return false;
                        }
                        ItemStack selectedSlotStack = selectedSlot.getItem();
                        if (selectedSlotStack.isEmpty()) {
                            return false;
                        }
                        boolean shiftIsDown = InputConstants.isKeyDown(mc.getWindow().getWindow(), 340) || InputConstants.isKeyDown(mc.getWindow().getWindow(), 344);
                        if (stackOnMouse.isEmpty()) {
                            if (!config.lmbTweakWithoutItem || !shiftIsDown) {
                                return false;
                            }
                            handler.clickSlot(selectedSlot, MouseButton.LEFT, true);
                        } else {
                            if (!config.lmbTweakWithItem) {
                                return false;
                            }
                            if (!areStacksCompatible(selectedSlotStack, stackOnMouse)) {
                                return false;
                            }
                            if (shiftIsDown) {
                                handler.clickSlot(selectedSlot, MouseButton.LEFT, true);
                            } else {
                                if (stackOnMouse.getCount() + selectedSlotStack.getCount() > stackOnMouse.getMaxStackSize()) {
                                    return false;
                                }
                                handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                                if (!handler.isCraftingOutput(selectedSlot)) {
                                    handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                                }
                            }
                        }
                    } else if (button == MouseButton.RIGHT) {
                        if (!canDoRMBDrag) {
                            return false;
                        }
                        rmbTweakMaybeClickSlot(selectedSlot, stackOnMouse);
                    }
                    return false;
                }
            }
        }
    }

    public static boolean onMouseScrolled(Screen screen, double x, double y, double scrollDelta) {
        updateScreen(screen);
        if (handler != null && !disableWheelForThisContainer && config.wheelTweak) {
            Slot selectedSlot = handler.getSlotUnderMouse(x, y);
            if (selectedSlot != null && !handler.isIgnored(selectedSlot)) {
                double scaledDelta = config.scrollItemScaling.scale(scrollDelta);
                if (accumulatedScrollDelta != 0.0 && Math.signum(scaledDelta) != Math.signum(accumulatedScrollDelta)) {
                    accumulatedScrollDelta = 0.0;
                }
                accumulatedScrollDelta += scaledDelta;
                int delta = (int) accumulatedScrollDelta;
                accumulatedScrollDelta -= (double) delta;
                if (delta == 0) {
                    return true;
                } else {
                    List<Slot> slots = handler.getSlots();
                    int numItemsToMove = Math.abs(delta);
                    boolean pushItems = delta < 0;
                    if (config.wheelScrollDirection.isPositionAware() && otherInventoryIsAbove(selectedSlot, slots)) {
                        pushItems = !pushItems;
                    }
                    if (config.wheelScrollDirection.isInverted()) {
                        pushItems = !pushItems;
                    }
                    ItemStack selectedSlotStack = selectedSlot.getItem();
                    if (selectedSlotStack.isEmpty()) {
                        return true;
                    } else {
                        ItemStack stackOnMouse = mc.player.f_36096_.getCarried();
                        if (handler.isCraftingOutput(selectedSlot)) {
                            if (!areStacksCompatible(selectedSlotStack, stackOnMouse)) {
                                return true;
                            } else {
                                if (stackOnMouse.isEmpty()) {
                                    if (!pushItems) {
                                        return true;
                                    }
                                    while (numItemsToMove-- > 0) {
                                        List<Slot> targetSlots = findPushSlots(slots, selectedSlot, selectedSlotStack.getCount(), true);
                                        if (targetSlots == null) {
                                            break;
                                        }
                                        handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                                        for (int i = 0; i < targetSlots.size(); i++) {
                                            Slot slot = (Slot) targetSlots.get(i);
                                            if (i == targetSlots.size() - 1) {
                                                handler.clickSlot(slot, MouseButton.LEFT, false);
                                            } else {
                                                int clickTimes = slot.getMaxStackSize(slot.getItem()) - slot.getItem().getCount();
                                                while (clickTimes-- > 0) {
                                                    handler.clickSlot(slot, MouseButton.RIGHT, false);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    while (numItemsToMove-- > 0) {
                                        handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                                    }
                                }
                                return true;
                            }
                        } else if (!stackOnMouse.isEmpty() && areStacksCompatible(selectedSlotStack, stackOnMouse)) {
                            return true;
                        } else if (pushItems) {
                            if (!stackOnMouse.isEmpty() && !selectedSlot.mayPlace(stackOnMouse)) {
                                return true;
                            } else {
                                numItemsToMove = Math.min(numItemsToMove, selectedSlotStack.getCount());
                                List<Slot> targetSlots = findPushSlots(slots, selectedSlot, numItemsToMove, false);
                                assert targetSlots != null;
                                if (targetSlots.isEmpty()) {
                                    return true;
                                } else {
                                    handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                                    for (Slot slot : targetSlots) {
                                        int clickTimes = slot.getMaxStackSize(slot.getItem()) - slot.getItem().getCount();
                                        clickTimes = Math.min(clickTimes, numItemsToMove);
                                        numItemsToMove -= clickTimes;
                                        while (clickTimes-- > 0) {
                                            handler.clickSlot(slot, MouseButton.RIGHT, false);
                                        }
                                    }
                                    handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                                    return true;
                                }
                            }
                        } else {
                            int maxItemsToMove = selectedSlot.getMaxStackSize(selectedSlotStack) - selectedSlotStack.getCount();
                            numItemsToMove = Math.min(numItemsToMove, maxItemsToMove);
                            while (numItemsToMove > 0) {
                                Slot targetSlot = findPullSlot(slots, selectedSlot);
                                if (targetSlot == null) {
                                    break;
                                }
                                int numItemsInTargetSlot = targetSlot.getItem().getCount();
                                if (handler.isCraftingOutput(targetSlot)) {
                                    if (maxItemsToMove < numItemsInTargetSlot) {
                                        break;
                                    }
                                    maxItemsToMove -= numItemsInTargetSlot;
                                    numItemsToMove = Math.min(numItemsToMove - 1, maxItemsToMove);
                                    if (!stackOnMouse.isEmpty() && !selectedSlot.mayPlace(stackOnMouse)) {
                                        break;
                                    }
                                    handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                                    handler.clickSlot(targetSlot, MouseButton.LEFT, false);
                                    handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                                } else {
                                    int numItemsToMoveFromTargetSlot = Math.min(numItemsToMove, numItemsInTargetSlot);
                                    maxItemsToMove -= numItemsToMoveFromTargetSlot;
                                    numItemsToMove -= numItemsToMoveFromTargetSlot;
                                    if (!stackOnMouse.isEmpty() && !targetSlot.mayPlace(stackOnMouse)) {
                                        break;
                                    }
                                    handler.clickSlot(targetSlot, MouseButton.LEFT, false);
                                    if (numItemsToMoveFromTargetSlot == numItemsInTargetSlot) {
                                        handler.clickSlot(selectedSlot, MouseButton.LEFT, false);
                                    } else {
                                        for (int ix = 0; ix < numItemsToMoveFromTargetSlot; ix++) {
                                            handler.clickSlot(selectedSlot, MouseButton.RIGHT, false);
                                        }
                                    }
                                    handler.clickSlot(targetSlot, MouseButton.LEFT, false);
                                }
                            }
                            return true;
                        }
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean otherInventoryIsAbove(Slot selectedSlot, List<Slot> slots) {
        boolean selectedIsInPlayerInventory = selectedSlot.container == mc.player.m_150109_();
        int otherInventorySlotsBelow = 0;
        int otherInventorySlotsAbove = 0;
        for (Slot slot : slots) {
            if (slot.container == mc.player.m_150109_() != selectedIsInPlayerInventory) {
                if (slot.y < selectedSlot.y) {
                    otherInventorySlotsAbove++;
                } else {
                    otherInventorySlotsBelow++;
                }
            }
        }
        return otherInventorySlotsAbove > otherInventorySlotsBelow;
    }

    private static IGuiScreenHandler findHandler(Screen currentScreen) {
        if (currentScreen instanceof IMTModGuiContainer3Ex) {
            return new IMTModGuiContainer3ExHandler((IMTModGuiContainer3Ex) currentScreen);
        } else if (currentScreen instanceof CreativeModeInventoryScreen) {
            return new GuiContainerCreativeHandler((CreativeModeInventoryScreen) currentScreen);
        } else {
            return currentScreen instanceof AbstractContainerScreen ? new GuiContainerHandler((AbstractContainerScreen) currentScreen) : null;
        }
    }

    private static boolean areStacksCompatible(ItemStack a, ItemStack b) {
        return a.isEmpty() || b.isEmpty() || ItemStack.isSameItem(a, b) && ItemStack.isSameItemSameTags(a, b);
    }

    private static Slot findPullSlot(List<Slot> slots, Slot selectedSlot) {
        int startIndex;
        int endIndex;
        int direction;
        if (config.wheelSearchOrder == WheelSearchOrder.FIRST_TO_LAST) {
            startIndex = 0;
            endIndex = slots.size();
            direction = 1;
        } else {
            startIndex = slots.size() - 1;
            endIndex = -1;
            direction = -1;
        }
        ItemStack selectedSlotStack = selectedSlot.getItem();
        boolean findInPlayerInventory = selectedSlot.container != mc.player.m_150109_();
        for (int i = startIndex; i != endIndex; i += direction) {
            Slot slot = (Slot) slots.get(i);
            if (!handler.isIgnored(slot)) {
                boolean slotInPlayerInventory = slot.container == mc.player.m_150109_();
                if (findInPlayerInventory == slotInPlayerInventory) {
                    ItemStack stack = slot.getItem();
                    if (!stack.isEmpty() && areStacksCompatible(selectedSlotStack, stack)) {
                        return slot;
                    }
                }
            }
        }
        return null;
    }

    private static List<Slot> findPushSlots(List<Slot> slots, Slot selectedSlot, int itemCount, boolean mustDistributeAll) {
        ItemStack selectedSlotStack = selectedSlot.getItem();
        boolean findInPlayerInventory = selectedSlot.container != mc.player.m_150109_();
        List<Slot> rv = new ArrayList();
        List<Slot> goodEmptySlots = new ArrayList();
        for (int i = 0; i != slots.size() && itemCount > 0; i++) {
            Slot slot = (Slot) slots.get(i);
            if (!handler.isIgnored(slot)) {
                boolean slotInPlayerInventory = slot.container == mc.player.m_150109_();
                if (findInPlayerInventory == slotInPlayerInventory && !handler.isCraftingOutput(slot)) {
                    ItemStack stack = slot.getItem();
                    if (stack.isEmpty()) {
                        if (slot.mayPlace(selectedSlotStack)) {
                            goodEmptySlots.add(slot);
                        }
                    } else if (areStacksCompatible(selectedSlotStack, stack) && stack.getCount() < slot.getMaxStackSize(stack)) {
                        rv.add(slot);
                        itemCount -= Math.min(itemCount, slot.getMaxStackSize(stack) - stack.getCount());
                    }
                }
            }
        }
        for (int ix = 0; ix != goodEmptySlots.size() && itemCount > 0; ix++) {
            Slot slot = (Slot) goodEmptySlots.get(ix);
            rv.add(slot);
            itemCount -= Math.min(itemCount, slot.getMaxStackSize());
        }
        return mustDistributeAll && itemCount > 0 ? null : rv;
    }
}