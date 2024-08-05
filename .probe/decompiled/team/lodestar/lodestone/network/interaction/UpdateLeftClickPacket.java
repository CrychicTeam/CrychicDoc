package team.lodestar.lodestone.network.interaction;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

public class UpdateLeftClickPacket extends LodestoneServerPacket {

    private final boolean leftClickHeld;

    public UpdateLeftClickPacket(boolean rightClick) {
        this.leftClickHeld = rightClick;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.leftClickHeld);
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        LodestonePlayerDataCapability.getCapabilityOptional(((NetworkEvent.Context) context.get()).getSender()).ifPresent(c -> c.leftClickHeld = this.leftClickHeld);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, UpdateLeftClickPacket.class, UpdateLeftClickPacket::encode, UpdateLeftClickPacket::decode, LodestoneServerPacket::handle);
    }

    public static UpdateLeftClickPacket decode(FriendlyByteBuf buf) {
        return new UpdateLeftClickPacket(buf.readBoolean());
    }
}