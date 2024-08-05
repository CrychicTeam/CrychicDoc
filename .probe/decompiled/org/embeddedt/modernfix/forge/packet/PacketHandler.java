package org.embeddedt.modernfix.forge.packet;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.embeddedt.modernfix.ModernFixClient;
import org.embeddedt.modernfix.packet.EntityIDSyncPacket;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("modernfix", "main"), () -> "1", NetworkRegistry.acceptMissingOr("1"), NetworkRegistry.acceptMissingOr("1"));

    public static void register() {
        int id = 1;
        INSTANCE.registerMessage(id++, EntityIDSyncPacket.class, EntityIDSyncPacket::serialize, EntityIDSyncPacket::deserialize, PacketHandler::handleSyncPacket);
    }

    private static void handleSyncPacket(EntityIDSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            ((NetworkEvent.Context) contextSupplier.get()).enqueueWork(() -> ModernFixClient.handleEntityIDSync(packet));
            ((NetworkEvent.Context) contextSupplier.get()).setPacketHandled(true);
        });
    }
}