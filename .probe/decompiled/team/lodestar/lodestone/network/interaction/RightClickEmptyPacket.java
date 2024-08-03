package team.lodestar.lodestone.network.interaction;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.events.types.RightClickEmptyServer;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

public class RightClickEmptyPacket extends LodestoneServerPacket {

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        RightClickEmptyServer.onRightClickEmptyServer(((NetworkEvent.Context) context.get()).getSender());
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, RightClickEmptyPacket.class, LodestoneServerPacket::encode, RightClickEmptyPacket::decode, LodestoneServerPacket::handle);
    }

    public static RightClickEmptyPacket decode(FriendlyByteBuf buf) {
        return new RightClickEmptyPacket();
    }
}