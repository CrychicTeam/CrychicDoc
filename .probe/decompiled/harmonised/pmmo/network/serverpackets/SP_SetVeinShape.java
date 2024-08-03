package harmonised.pmmo.network.serverpackets;

import harmonised.pmmo.features.veinmining.VeinMiningLogic;
import harmonised.pmmo.features.veinmining.VeinShapeData;
import harmonised.pmmo.util.MsLoggy;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SP_SetVeinShape {

    VeinShapeData.ShapeType mode;

    public SP_SetVeinShape(VeinShapeData.ShapeType mode) {
        this.mode = mode;
    }

    public SP_SetVeinShape(FriendlyByteBuf buf) {
        this.mode = buf.readEnum(VeinShapeData.ShapeType.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(this.mode);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            VeinMiningLogic.shapePerPlayer.put(((NetworkEvent.Context) ctx.get()).getSender().m_20148_(), this.mode);
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "Vein Shape Updated for: {} with {}", ((NetworkEvent.Context) ctx.get()).getSender().m_6302_(), this.mode.name());
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}