package noppes.npcs.packets;

import java.util.concurrent.CompletableFuture;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.common.util.LogWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class IPacketServer implements Packet<ServerGamePacketListener> {

    private static final Logger LOGGER = LogManager.getLogger();

    public ServerPlayer player;

    public EntityNPCInterface npc;

    public void handle(ServerGamePacketListener handler) {
        this.enqueueWork(() -> {
            try {
                this.player = ((ServerGamePacketListenerImpl) handler).player;
                this.npc = NoppesUtilServer.getEditingNpc(this.player);
                if (!this.requiresNpc() || this.npc != null) {
                    if (this.getPermission() == null || CustomNpcsPermissions.hasPermission(this.player, this.getPermission())) {
                        if (!this.toolAllowed(this.player.m_150109_().getSelected())) {
                            this.warn("tried to use custom npcs without a tool in hand, possibly a hacker");
                        } else {
                            this.handle();
                        }
                    }
                }
            } catch (Throwable var3) {
                LogWriter.except(var3);
                throw var3;
            }
        });
    }

    public boolean requiresNpc() {
        return false;
    }

    public PermissionNode<Boolean> getPermission() {
        return null;
    }

    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.wand;
    }

    public abstract void handle();

    private void warn(String warning) {
        LOGGER.warn(this.player.m_7755_().getString() + ": " + warning + " - " + this);
    }

    public CompletableFuture<Void> enqueueWork(Runnable runnable) {
        BlockableEventLoop<?> executor = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
        if (!executor.isSameThread()) {
            return executor.submitAsync(runnable);
        } else {
            runnable.run();
            return CompletableFuture.completedFuture(null);
        }
    }
}