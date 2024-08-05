package noppes.npcs.packets;

import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.entity.EntityNPCInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class PacketServerBasic {

    private static final Logger LOGGER = LogManager.getLogger();

    public ServerPlayer player;

    public EntityNPCInterface npc;

    public boolean requiresNpc() {
        return false;
    }

    public PermissionNode<Boolean> getPermission() {
        return null;
    }

    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.wand;
    }

    public static void handle(PacketServerBasic msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            msg.player = ((NetworkEvent.Context) ctx.get()).getSender();
            msg.npc = NoppesUtilServer.getEditingNpc(msg.player);
            if (!msg.requiresNpc() || msg.npc != null) {
                if (msg.getPermission() == null || CustomNpcsPermissions.hasPermission(msg.player, msg.getPermission())) {
                    if (!msg.toolAllowed(msg.player.m_150109_().getSelected())) {
                        msg.warn("tried to use custom npcs without a tool in hand, possibly a hacker");
                    } else {
                        msg.handle();
                    }
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }

    private void warn(String warning) {
        LOGGER.warn(this.player.m_7755_().getString() + ": " + warning + " - " + this);
    }

    protected abstract void handle();
}