package software.bernie.geckolib.network;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.network.packet.AnimDataSyncPacket;
import software.bernie.geckolib.network.packet.AnimTriggerPacket;
import software.bernie.geckolib.network.packet.BlockEntityAnimDataSyncPacket;
import software.bernie.geckolib.network.packet.BlockEntityAnimTriggerPacket;
import software.bernie.geckolib.network.packet.EntityAnimDataSyncPacket;
import software.bernie.geckolib.network.packet.EntityAnimTriggerPacket;

public final class GeckoLibNetwork {

    private static final String VER = "1";

    private static final SimpleChannel PACKET_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("geckolib", "main"), () -> "1", "1"::equals, "1"::equals);

    private static final Map<String, GeoAnimatable> SYNCED_ANIMATABLES = new Object2ObjectOpenHashMap();

    public static void init() {
        int id = 0;
        PACKET_CHANNEL.registerMessage(id++, AnimDataSyncPacket.class, AnimDataSyncPacket::encode, AnimDataSyncPacket::decode, AnimDataSyncPacket::receivePacket);
        PACKET_CHANNEL.registerMessage(id++, AnimTriggerPacket.class, AnimTriggerPacket::encode, AnimTriggerPacket::decode, AnimTriggerPacket::receivePacket);
        PACKET_CHANNEL.registerMessage(id++, EntityAnimDataSyncPacket.class, EntityAnimDataSyncPacket::encode, EntityAnimDataSyncPacket::decode, EntityAnimDataSyncPacket::receivePacket);
        PACKET_CHANNEL.registerMessage(id++, EntityAnimTriggerPacket.class, EntityAnimTriggerPacket::encode, EntityAnimTriggerPacket::decode, EntityAnimTriggerPacket::receivePacket);
        PACKET_CHANNEL.registerMessage(id++, BlockEntityAnimDataSyncPacket.class, BlockEntityAnimDataSyncPacket::encode, BlockEntityAnimDataSyncPacket::decode, BlockEntityAnimDataSyncPacket::receivePacket);
        PACKET_CHANNEL.registerMessage(id++, BlockEntityAnimTriggerPacket.class, BlockEntityAnimTriggerPacket::encode, BlockEntityAnimTriggerPacket::decode, BlockEntityAnimTriggerPacket::receivePacket);
    }

    public static synchronized void registerSyncedAnimatable(GeoAnimatable animatable) {
        GeoAnimatable existing = (GeoAnimatable) SYNCED_ANIMATABLES.put(animatable.getClass().toString(), animatable);
        if (existing == null) {
            GeckoLib.LOGGER.debug("Registered SyncedAnimatable for " + animatable.getClass().toString());
        }
    }

    @Nullable
    public static GeoAnimatable getSyncedAnimatable(String className) {
        GeoAnimatable animatable = (GeoAnimatable) SYNCED_ANIMATABLES.get(className);
        if (animatable == null) {
            GeckoLib.LOGGER.error("Attempting to retrieve unregistered synced animatable! (" + className + ")");
        }
        return animatable;
    }

    public static <M> void send(M packet, PacketDistributor.PacketTarget distributor) {
        PACKET_CHANNEL.send(distributor, packet);
    }
}