package dev.xkmc.l2backpack.network;

import dev.xkmc.l2backpack.content.capability.InvPickupCap;
import dev.xkmc.l2backpack.content.capability.PickupModeCap;
import dev.xkmc.l2backpack.content.capability.PickupTrace;
import dev.xkmc.l2backpack.content.click.DrawerQuickInsert;
import dev.xkmc.l2backpack.content.click.VanillaQuickInsert;
import dev.xkmc.l2backpack.content.insert.OverlayInsertItem;
import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class DrawerInteractToServer extends SerialPacketBase {

    @SerialField
    public DrawerInteractToServer.Type type;

    @SerialField
    public int wid;

    @SerialField
    public int slot;

    @SerialField
    public int limit;

    @SerialField
    public ItemStack stack;

    @SerialField
    public DrawerInteractToServer.Callback suppress;

    @Deprecated
    public DrawerInteractToServer() {
    }

    public DrawerInteractToServer(DrawerInteractToServer.Type type, int wid, int slot, ItemStack carried, DrawerInteractToServer.Callback suppress, int limit) {
        this.type = type;
        this.wid = wid;
        this.slot = slot;
        this.stack = carried;
        this.suppress = suppress;
        this.limit = limit;
    }

    public void handle(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player != null) {
            AbstractContainerMenu menu = player.f_36096_;
            if (menu.containerId == this.wid) {
                if (this.wid == 0 || menu.getSlot(this.slot).allowModification(player)) {
                    ItemStack storage = this.wid == 0 ? player.m_150109_().getItem(this.slot) : menu.getSlot(this.slot).getItem();
                    if (storage.getItem() instanceof OverlayInsertItem drawerItem) {
                        drawerItem.serverTrigger(storage, player);
                        ItemStack var9 = menu.getCarried();
                        if (player.isCreative() && this.wid == 0) {
                            var9 = this.stack;
                        }
                        if (this.type == DrawerInteractToServer.Type.TAKE) {
                            if (!var9.isEmpty()) {
                                return;
                            }
                            ItemStack stack = drawerItem.takeItem(storage, player);
                            if (player.isCreative() && this.wid == 0) {
                                var9 = stack;
                            } else {
                                menu.setCarried(stack);
                                if (this.suppress == DrawerInteractToServer.Callback.SUPPRESS) {
                                    menu.setRemoteCarried(stack.copy());
                                }
                            }
                        } else if (this.type == DrawerInteractToServer.Type.QUICK_MOVE) {
                            if (menu instanceof DrawerQuickInsert ins) {
                                ItemStack stack = drawerItem.takeItem(storage, player);
                                ins.quickMove(player, menu, stack, this.slot);
                                if (!stack.isEmpty()) {
                                    drawerItem.attemptInsert(storage, stack, player);
                                }
                            }
                            if (menu instanceof ChestMenu insx) {
                                ItemStack stack = drawerItem.takeItem(storage, player);
                                ((VanillaQuickInsert) insx).l2backpack$quickMove(player, menu, stack, this.slot);
                                if (!stack.isEmpty()) {
                                    drawerItem.attemptInsert(storage, stack, player);
                                }
                            }
                        } else if (this.type == DrawerInteractToServer.Type.INSERT) {
                            if (this.limit == 0) {
                                drawerItem.attemptInsert(storage, var9, player);
                            } else {
                                ItemStack split = var9.split(this.limit);
                                drawerItem.attemptInsert(storage, split, player);
                                var9.grow(split.getCount());
                            }
                            if (this.suppress == DrawerInteractToServer.Callback.SUPPRESS) {
                                menu.setRemoteCarried(menu.getCarried().copy());
                            }
                        } else if (this.type == DrawerInteractToServer.Type.PICKUP) {
                            Optional<PickupModeCap> cap = storage.getCapability(InvPickupCap.TOKEN).resolve();
                            if (cap.isPresent()) {
                                if (this.limit == 0) {
                                    ((PickupModeCap) cap.get()).doPickup(var9, new PickupTrace(false, player));
                                } else {
                                    ItemStack split = var9.split(this.limit);
                                    ((PickupModeCap) cap.get()).doPickup(split, new PickupTrace(false, player));
                                    var9.grow(split.getCount());
                                }
                                if (this.suppress == DrawerInteractToServer.Callback.SUPPRESS) {
                                    menu.setRemoteCarried(menu.getCarried().copy());
                                }
                            }
                        }
                        if (this.wid != 0) {
                            menu.getSlot(this.slot).setChanged();
                        }
                        if (player.isCreative() && this.wid == 0) {
                            L2Backpack.HANDLER.toClientPlayer(new CreativeSetCarryToClient(var9), player);
                        } else if (this.suppress == DrawerInteractToServer.Callback.SCRAMBLE) {
                            scramble(menu);
                        }
                    }
                }
            }
        }
    }

    private static void scramble(AbstractContainerMenu menu) {
        ItemStack carried = menu.getCarried();
        if (carried.isEmpty()) {
            menu.setRemoteCarried(new ItemStack(Items.FARMLAND, 65));
        } else {
            menu.setRemoteCarried(ItemStack.EMPTY);
        }
        menu.broadcastChanges();
    }

    public static enum Callback {

        REGULAR, SUPPRESS, SCRAMBLE
    }

    public static enum Type {

        INSERT, TAKE, QUICK_MOVE, PICKUP
    }
}