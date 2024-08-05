package harmonised.pmmo.network.clientpackets;

import harmonised.pmmo.config.codecs.CodecTypes;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.util.MsLoggy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class CP_SetOtherExperience {

    Map<String, Long> map;

    public CP_SetOtherExperience(Map<String, Long> map) {
        this.map = map;
    }

    public CP_SetOtherExperience(FriendlyByteBuf buf) {
        this((Map<String, Long>) CodecTypes.LONG_CODEC.parse(NbtOps.INSTANCE, buf.readNbt()).result().orElse(new HashMap()));
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt((CompoundTag) CodecTypes.LONG_CODEC.encodeStart(NbtOps.INSTANCE, this.map).result().orElse(new CompoundTag()));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            Core.get(LogicalSide.CLIENT).getData().setXpMap(UUID.randomUUID(), this.map);
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.NETWORK, "Client Packet Handled for getting Other Experience");
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}