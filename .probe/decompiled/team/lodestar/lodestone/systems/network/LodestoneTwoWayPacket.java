package team.lodestar.lodestone.systems.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public abstract class LodestoneTwoWayPacket {

    public void encode(FriendlyByteBuf buf) {
    }

    public final void handle(Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).enqueueWork(() -> {
            if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide().equals(LogicalSide.CLIENT)) {
                LodestoneTwoWayPacket.ClientOnly.clientData(this, context);
            } else {
                this.serverExecute(context);
            }
        });
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
    }

    public void serverExecute(Supplier<NetworkEvent.Context> context) {
    }

    @OnlyIn(Dist.CLIENT)
    public void clientExecute(Supplier<NetworkEvent.Context> context) {
    }

    public static class ClientOnly {

        public static void clientData(LodestoneTwoWayPacket packet, Supplier<NetworkEvent.Context> context) {
            packet.clientExecute(context);
        }
    }
}