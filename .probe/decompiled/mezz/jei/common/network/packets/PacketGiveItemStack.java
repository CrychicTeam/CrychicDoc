package mezz.jei.common.network.packets;

import java.util.concurrent.CompletableFuture;
import mezz.jei.common.config.GiveMode;
import mezz.jei.common.network.IPacketId;
import mezz.jei.common.network.PacketIdServer;
import mezz.jei.common.network.ServerPacketContext;
import mezz.jei.common.network.ServerPacketData;
import mezz.jei.common.util.ServerCommandUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class PacketGiveItemStack extends PacketJei {

    private final ItemStack itemStack;

    private final GiveMode giveMode;

    public PacketGiveItemStack(ItemStack itemStack, GiveMode giveMode) {
        this.itemStack = itemStack;
        this.giveMode = giveMode;
    }

    @Override
    public IPacketId getPacketId() {
        return PacketIdServer.GIVE_ITEM;
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) {
        buf.writeItem(this.itemStack);
        buf.writeEnum(this.giveMode);
    }

    public static CompletableFuture<Void> readPacketData(ServerPacketData data) {
        FriendlyByteBuf buf = data.buf();
        ItemStack itemStack = buf.readItem();
        GiveMode giveMode = buf.readEnum(GiveMode.class);
        ServerPacketContext context = data.context();
        ServerPlayer player = context.player();
        MinecraftServer server = player.server;
        return server.m_18707_(() -> ServerCommandUtil.executeGive(context, itemStack, giveMode));
    }
}