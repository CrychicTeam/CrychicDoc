package top.theillusivec4.caelus.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class CaelusNetwork {

    private static final String PTC_VERSION = "1";

    private static SimpleChannel instance;

    private static int id = 0;

    public static void setup() {
        instance = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("caelus", "main")).networkProtocolVersion(() -> "1").clientAcceptedVersions("1"::equals).serverAcceptedVersions("1"::equals).simpleChannel();
        instance.registerMessage(id++, CPacketFlight.class, CPacketFlight::encode, CPacketFlight::decode, CPacketFlight::handle);
    }

    public static void sendFlightC2S() {
        instance.send(PacketDistributor.SERVER.noArg(), new CPacketFlight());
    }
}