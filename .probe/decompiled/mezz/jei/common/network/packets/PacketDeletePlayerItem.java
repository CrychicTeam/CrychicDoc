package mezz.jei.common.network.packets;

import java.util.concurrent.CompletableFuture;
import mezz.jei.common.config.IServerConfig;
import mezz.jei.common.network.IConnectionToClient;
import mezz.jei.common.network.IPacketId;
import mezz.jei.common.network.PacketIdServer;
import mezz.jei.common.network.ServerPacketContext;
import mezz.jei.common.network.ServerPacketData;
import mezz.jei.common.platform.IPlatformRegistry;
import mezz.jei.common.platform.Services;
import mezz.jei.common.util.ServerCommandUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacketDeletePlayerItem extends PacketJei {

    private static final Logger LOGGER = LogManager.getLogger();

    private final ItemStack itemStack;

    public PacketDeletePlayerItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public IPacketId getPacketId() {
        return PacketIdServer.DELETE_ITEM;
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) {
        IPlatformRegistry<Item> registry = Services.PLATFORM.getRegistry(Registries.ITEM);
        int itemId = registry.getId(this.itemStack.getItem());
        buf.writeVarInt(itemId);
    }

    public static CompletableFuture<Void> readPacketData(ServerPacketData data) {
        FriendlyByteBuf buf = data.buf();
        ServerPacketContext context = data.context();
        ServerPlayer player = context.player();
        int itemId = buf.readVarInt();
        return (CompletableFuture<Void>) Services.PLATFORM.getRegistry(Registries.ITEM).getValue(itemId).map(item -> {
            MinecraftServer server = player.server;
            return server.m_18707_(() -> deletePlayerItem(player, context, item));
        }).orElseGet(() -> {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Player '{} ({})' tried to delete Item ID '{}' but no item is registered with that ID.", player.m_7755_(), player.m_20148_(), itemId);
            }
            return CompletableFuture.completedFuture(null);
        });
    }

    private static void deletePlayerItem(ServerPlayer player, ServerPacketContext context, Item item) {
        IServerConfig serverConfig = context.serverConfig();
        if (ServerCommandUtil.hasPermissionForCheatMode(player, serverConfig)) {
            ItemStack playerItem = player.f_36096_.getCarried();
            if (playerItem.getItem() == item) {
                player.f_36096_.setCarried(ItemStack.EMPTY);
            } else if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Player '{} ({})' tried to delete Item '{}' but is currently holding a different ItemStack '{}'.", player.m_7755_(), player.m_20148_(), item, playerItem.getDisplayName());
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                ItemStack playerItem = player.f_36096_.getCarried();
                LOGGER.debug("Player '{} ({})' tried to delete ItemStack '{}' but does not have permission.", player.m_7755_(), player.m_20148_(), playerItem.getDisplayName());
            }
            IConnectionToClient connection = context.connection();
            connection.sendPacketToClient(new PacketCheatPermission(false), player);
        }
    }
}