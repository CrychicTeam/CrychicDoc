package portb.biggerstacks.net;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.gui.ConfigureScreen;

public class PacketHandler {

    public static final ResourceLocation CHANNEL_NAME = new ResourceLocation("biggerstacks", "rules");

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(CHANNEL_NAME, () -> "1", "1"::equals, "1"::equals);

    public static void register() {
        int index = 0;
        INSTANCE.messageBuilder(ClientboundRulesHandshakePacket.class, index++, NetworkDirection.LOGIN_TO_CLIENT).encoder(ClientboundRulesHandshakePacket::encode).decoder(ClientboundRulesHandshakePacket::new).markAsLoginPacket().noResponse().consumerMainThread(PacketHandler::handleHandshake).add();
        INSTANCE.messageBuilder(ClientboundRulesUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT).encoder(ClientboundRulesUpdatePacket::encode).decoder(ClientboundRulesUpdatePacket::new).noResponse().consumerMainThread(PacketHandler::handleUpdate).add();
        INSTANCE.messageBuilder(ClientboundConfigureScreenOpenPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT).encoder(ClientboundConfigureScreenOpenPacket::encode).decoder(ClientboundConfigureScreenOpenPacket::new).consumerMainThread(PacketHandler::handleOpenScreenPacket).add();
        INSTANCE.messageBuilder(ServerboundCreateConfigTemplatePacket.class, index++, NetworkDirection.PLAY_TO_SERVER).encoder(ServerboundCreateConfigTemplatePacket::encode).decoder(ServerboundCreateConfigTemplatePacket::new).consumerNetworkThread(ServerboundCreateConfigTemplatePacket::handleCreateConfigTemplate).add();
    }

    private static void handleHandshake(ClientboundRulesHandshakePacket packet, Supplier<NetworkEvent.Context> ctx) {
        StackSizeRules.setRuleSet(packet.getRules());
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }

    private static void handleUpdate(ClientboundRulesUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        StackSizeRules.setRuleSet(packet.getRules());
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }

    private static boolean handleOpenScreenPacket(ClientboundConfigureScreenOpenPacket clientboundConfigureScreenOpenPacket, Supplier<NetworkEvent.Context> contextSupplier) {
        ConfigureScreen.open(clientboundConfigureScreenOpenPacket);
        return true;
    }
}