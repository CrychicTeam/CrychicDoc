package team.lodestar.lodestone.network.interaction;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.events.types.RightClickEmptyServer;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

public class UpdateRightClickPacket extends LodestoneServerPacket {

    private final boolean rightClickHeld;

    public UpdateRightClickPacket(boolean rightClick) {
        this.rightClickHeld = rightClick;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.rightClickHeld);
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        if (this.rightClickHeld) {
            RightClickEmptyServer.onRightClickEmptyServer(((NetworkEvent.Context) context.get()).getSender());
        }
        LodestonePlayerDataCapability.getCapabilityOptional(((NetworkEvent.Context) context.get()).getSender()).ifPresent(c -> c.rightClickHeld = this.rightClickHeld);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, UpdateRightClickPacket.class, UpdateRightClickPacket::encode, UpdateRightClickPacket::decode, LodestoneServerPacket::handle);
    }

    public static UpdateRightClickPacket decode(FriendlyByteBuf buf) {
        return new UpdateRightClickPacket(buf.readBoolean());
    }
}