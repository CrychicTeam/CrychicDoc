package team.lodestar.lodestone.systems.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public abstract class LodestoneClientPacket {

    public void encode(FriendlyByteBuf buf) {
    }

    public final void handle(Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).enqueueWork(() -> {
            if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide().equals(LogicalSide.CLIENT)) {
                LodestoneClientPacket.ClientOnly.clientData(this, context);
            }
        });
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public void execute(Supplier<NetworkEvent.Context> context) {
    }

    public static class ClientOnly {

        public static void clientData(LodestoneClientPacket packet, Supplier<NetworkEvent.Context> context) {
            packet.execute(context);
        }
    }
}