package team.lodestar.lodestone.systems.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public abstract class LodestoneServerPacket {

    public void encode(FriendlyByteBuf buf) {
    }

    public final void handle(Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).enqueueWork(() -> this.execute(context));
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
    }

    public void execute(Supplier<NetworkEvent.Context> context) {
    }
}