package net.minecraftforge.network;

import io.netty.util.AttributeKey;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.event.EventNetworkChannel;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NetworkConstants {

    public static final String FMLNETMARKER = "FML";

    public static final int FMLNETVERSION = 3;

    public static final String NETVERSION = "FML3";

    public static final String NOVERSION = "NONE";

    static final Marker NETWORK = MarkerManager.getMarker("FMLNETWORK");

    static final AttributeKey<String> FML_NETVERSION = AttributeKey.valueOf("fml:netversion");

    static final AttributeKey<HandshakeHandler> FML_HANDSHAKE_HANDLER = AttributeKey.valueOf("fml:handshake");

    static final AttributeKey<MCRegisterPacketHandler.ChannelList> FML_MC_REGISTRY = AttributeKey.valueOf("minecraft:netregistry");

    static final AttributeKey<ConnectionData> FML_CONNECTION_DATA = AttributeKey.valueOf("fml:conndata");

    static final AttributeKey<ConnectionData.ModMismatchData> FML_MOD_MISMATCH_DATA = AttributeKey.valueOf("fml:mismatchdata");

    static final ResourceLocation FML_HANDSHAKE_RESOURCE = new ResourceLocation("fml:handshake");

    static final ResourceLocation FML_PLAY_RESOURCE = new ResourceLocation("fml:play");

    static final ResourceLocation MC_REGISTER_RESOURCE = new ResourceLocation("minecraft:register");

    static final ResourceLocation MC_UNREGISTER_RESOURCE = new ResourceLocation("minecraft:unregister");

    static final SimpleChannel handshakeChannel = NetworkInitialization.getHandshakeChannel();

    static final SimpleChannel playChannel = NetworkInitialization.getPlayChannel();

    static final List<EventNetworkChannel> mcRegChannels = NetworkInitialization.buildMCRegistrationChannels();

    public static final String IGNORESERVERONLY = "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31";

    public static String init() {
        return "FML3";
    }
}