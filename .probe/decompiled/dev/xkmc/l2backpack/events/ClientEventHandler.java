package dev.xkmc.l2backpack.events;

import com.mojang.blaze3d.platform.InputConstants;
import dev.xkmc.l2backpack.compat.CuriosCompat;
import dev.xkmc.l2backpack.content.capability.PickupBagItem;
import dev.xkmc.l2backpack.content.drawer.BaseDrawerItem;
import dev.xkmc.l2backpack.content.drawer.DrawerItem;
import dev.xkmc.l2backpack.content.insert.OverlayInsertItem;
import dev.xkmc.l2backpack.content.tool.IBagTool;
import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2backpack.init.data.BackpackKeys;
import dev.xkmc.l2backpack.network.CreativeSetCarryToServer;
import dev.xkmc.l2backpack.network.DrawerInteractToServer;
import dev.xkmc.l2itemselector.events.GenericKeyEvent;
import dev.xkmc.l2library.util.Proxy;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2backpack", bus = Bus.FORGE)
public class ClientEventHandler {

    @SubscribeEvent
    public static void keyEvent(GenericKeyEvent event) {
        if (!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 292)) {
            if (Minecraft.getInstance().screen == null && Proxy.getClientPlayer() != null && event.test(BackpackKeys.OPEN.map.getKey()) && event.getAction() == 1 && (BackpackSlotClickListener.canOpen(Proxy.getClientPlayer().m_6844_(EquipmentSlot.CHEST)) || CuriosCompat.getSlot(Proxy.getClientPlayer(), BackpackSlotClickListener::canOpen).isPresent())) {
                L2Backpack.SLOT_CLICK.keyBind();
            }
        }
    }

    @SubscribeEvent
    public static void onScreenLeftClick(ScreenEvent.MouseButtonReleased.Pre event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?> scr && scr.getMenu().getCarried().getItem() instanceof IBagTool) {
            Slot slot = scr.getSlotUnderMouse();
            if (slot != null && slot.getItem().getItem() instanceof PickupBagItem) {
                if (scr instanceof CreativeModeInventoryScreen) {
                    L2Backpack.HANDLER.toServer(new CreativeSetCarryToServer(ItemStack.EMPTY));
                }
                event.setCanceled(true);
            }
            return;
        }
        if (onRelease(event)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onScreenRightClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?> scr && scr.getMenu().getCarried().getItem() instanceof IBagTool) {
            Slot slot = scr.getSlotUnderMouse();
            if (slot != null && slot.getItem().getItem() instanceof PickupBagItem && scr instanceof CreativeModeInventoryScreen) {
                L2Backpack.HANDLER.toServer(new CreativeSetCarryToServer(scr.getMenu().getCarried()));
            }
            return;
        }
        if (onPress(event)) {
            event.setCanceled(true);
        }
    }

    private static boolean onRelease(ScreenEvent.MouseButtonReleased.Pre event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?> cont) {
            Slot slot = cont.getSlotUnderMouse();
            ItemStack carried = cont.getMenu().getCarried();
            boolean bypass = !carried.isEmpty() && slot != null && slot.getItem().getItem() instanceof OverlayInsertItem item && !item.mayClientTake();
            if (bypass || event.getButton() == 0) {
                return insertItem(event, cont, slot, true, event.getButton());
            }
            if (event.getButton() == 1 && slot != null) {
                if (slot.getItem().getItem() instanceof OverlayInsertItem itemx && itemx.mayClientTake() && !cont.getMenu().getCarried().isEmpty()) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private static boolean onPress(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?> cont) {
            Slot slot = cont.getSlotUnderMouse();
            ItemStack carried = cont.getMenu().getCarried();
            boolean bypass = !carried.isEmpty() && slot != null && slot.getItem().getItem() instanceof OverlayInsertItem item && !item.mayClientTake();
            if (bypass || event.getButton() == 0) {
                return insertItem(event, cont, slot, false, event.getButton());
            }
            if (event.getButton() == 1) {
                if (extractItem(event, cont, slot)) {
                    return true;
                }
                if (slot != null) {
                    return slot.getItem().getItem() instanceof BaseDrawerItem && !cont.getMenu().getCarried().isEmpty();
                }
            }
        }
        return false;
    }

    private static boolean insertItem(ScreenEvent event, AbstractContainerScreen<?> cont, @Nullable Slot slot, boolean perform, int button) {
        if (slot == null) {
            return false;
        } else {
            ItemStack storage = slot.getItem();
            ItemStack carried = cont.getMenu().getCarried();
            if (storage.getItem() instanceof OverlayInsertItem drawer) {
                Player player = Proxy.getClientPlayer();
                return player != null && slot.allowModification(player) ? drawer.clientInsert(storage, carried, cont.getMenu().containerId, slot, perform, button, DrawerInteractToServer.Callback.REGULAR, 0) : false;
            } else {
                return false;
            }
        }
    }

    private static boolean extractItem(ScreenEvent.MouseButtonPressed.Pre event, AbstractContainerScreen<?> cont, @Nullable Slot slot) {
        if (slot == null) {
            return false;
        } else {
            ItemStack stack = cont.getMenu().getCarried();
            ItemStack drawerStack = slot.getItem();
            if (drawerStack.getItem() instanceof OverlayInsertItem drawer) {
                Player player = Proxy.getClientPlayer();
                if (player == null || !slot.allowModification(player)) {
                    return false;
                } else if (drawer.mayClientTake() && stack.isEmpty()) {
                    if (Screen.hasShiftDown()) {
                        sendDrawerPacket(DrawerInteractToServer.Type.QUICK_MOVE, cont, slot);
                    } else {
                        sendDrawerPacket(DrawerInteractToServer.Type.TAKE, cont, slot);
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private static void sendDrawerPacket(DrawerInteractToServer.Type type, AbstractContainerScreen<?> cont, Slot slot) {
        sendDrawerPacket(type, cont, slot, DrawerInteractToServer.Callback.REGULAR, 0);
    }

    private static void sendDrawerPacket(DrawerInteractToServer.Type type, AbstractContainerScreen<?> cont, Slot slot, DrawerInteractToServer.Callback suppress, int limit) {
        int index = cont.getMenu().containerId == 0 ? slot.getSlotIndex() : slot.index;
        L2Backpack.HANDLER.toServer(new DrawerInteractToServer(type, cont.getMenu().containerId, index, cont.getMenu().getCarried(), suppress, limit));
    }

    public static boolean clientDrawerTake(AbstractContainerScreen<?> cont, Slot slot) {
        ItemStack stack = cont.getMenu().getCarried();
        ItemStack drawerStack = slot.getItem();
        if (drawerStack.getItem() instanceof DrawerItem drawer) {
            Player player = Proxy.getClientPlayer();
            if (player == null || !slot.allowModification(player)) {
                return false;
            } else if (drawer.mayClientTake() && stack.isEmpty()) {
                cont.getMenu().setCarried(drawer.takeItem(drawerStack, Integer.MAX_VALUE, Proxy.getClientPlayer(), false));
                sendDrawerPacket(DrawerInteractToServer.Type.TAKE, cont, slot, DrawerInteractToServer.Callback.SUPPRESS, 0);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean clientDrawerInsert(AbstractContainerScreen<?> cont, Slot slot, int limit) {
        ItemStack storage = slot.getItem();
        ItemStack carried = cont.getMenu().getCarried();
        if (storage.getItem() instanceof OverlayInsertItem drawer) {
            Player player = Proxy.getClientPlayer();
            if (player != null && slot.allowModification(player)) {
                DrawerInteractToServer.Callback var10007 = limit == 0 ? DrawerInteractToServer.Callback.SCRAMBLE : DrawerInteractToServer.Callback.SUPPRESS;
                boolean ans = drawer.clientInsert(storage, carried, cont.getMenu().containerId, slot, true, 0, var10007, limit);
                if (ans) {
                    if (limit == 0) {
                        cont.getMenu().setCarried(ItemStack.EMPTY);
                    } else {
                        cont.getMenu().getCarried().shrink(limit);
                    }
                }
                return ans;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}