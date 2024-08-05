package noppes.npcs.packets.server;

import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.EventHooks;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.roles.RoleFollower;
import noppes.npcs.shared.client.util.NoppesStringUtils;

public class SPacketFollowerHire extends PacketServerBasic {

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    public static void encode(SPacketFollowerHire msg, FriendlyByteBuf buf) {
    }

    public static SPacketFollowerHire decode(FriendlyByteBuf buf) {
        return new SPacketFollowerHire();
    }

    @Override
    protected void handle() {
        if (this.npc.role.getType() == 2) {
            if (this.npc.role.getType() == 2) {
                AbstractContainerMenu con = this.player.f_36096_;
                if (con != null && con instanceof ContainerNPCFollowerHire container) {
                    RoleFollower role = (RoleFollower) this.npc.role;
                    followerBuy(role, container.currencyMatrix, this.player, this.npc);
                }
            }
        }
    }

    public static void followerBuy(RoleFollower role, Container currencyInv, ServerPlayer player, EntityNPCInterface npc) {
        ItemStack currency = currencyInv.getItem(0);
        if (currency != null && !currency.isEmpty()) {
            HashMap<ItemStack, Integer> cd = new HashMap();
            for (int slot = 0; slot < role.inventory.items.size(); slot++) {
                ItemStack is = role.inventory.items.get(slot);
                if (!is.isEmpty() && is.getItem() == currency.getItem()) {
                    int days = 1;
                    if (role.rates.containsKey(slot)) {
                        days = (Integer) role.rates.get(slot);
                    }
                    cd.put(is, days);
                }
            }
            if (cd.size() != 0) {
                int stackSize = currency.getCount();
                int days = 0;
                int possibleDays = 0;
                int possibleSize = stackSize;
                while (true) {
                    for (ItemStack item : cd.keySet()) {
                        int rDays = (Integer) cd.get(item);
                        int rValue = item.getCount();
                        if (rValue <= stackSize) {
                            int newStackSize = stackSize % rValue;
                            int size = stackSize - newStackSize;
                            int posDays = size / rValue * rDays;
                            if (possibleDays <= posDays) {
                                possibleDays = posDays;
                                possibleSize = newStackSize;
                            }
                        }
                    }
                    if (stackSize == possibleSize) {
                        RoleEvent.FollowerHireEvent event = new RoleEvent.FollowerHireEvent(player, npc.wrappedNPC, days);
                        if (EventHooks.onNPCRole(npc, event)) {
                            return;
                        }
                        if (event.days == 0) {
                            return;
                        }
                        if (stackSize <= 0) {
                            currencyInv.setItem(0, ItemStack.EMPTY);
                        } else {
                            currencyInv.setItem(0, currency.split(stackSize));
                        }
                        npc.say(player, new Line(NoppesStringUtils.formatText(role.dialogHire.replace("{days}", days + ""), player, npc)));
                        role.setOwner(player);
                        role.addDays(days);
                        return;
                    }
                    stackSize = possibleSize;
                    days += possibleDays;
                    possibleDays = 0;
                }
            }
        }
    }
}