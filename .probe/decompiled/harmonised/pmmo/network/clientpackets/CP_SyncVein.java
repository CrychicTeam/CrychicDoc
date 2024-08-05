package harmonised.pmmo.network.clientpackets;

import harmonised.pmmo.client.utils.VeinTracker;
import harmonised.pmmo.util.MsLoggy;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class CP_SyncVein {

    double charge;

    public CP_SyncVein(double charge) {
        this.charge = charge;
    }

    public CP_SyncVein(FriendlyByteBuf buf) {
        this(buf.readDouble());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(this.charge);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "Vein Update on Client: " + this.charge);
            VeinTracker.currentCharge = this.charge;
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}