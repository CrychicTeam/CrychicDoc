package harmonised.pmmo.network.clientpackets;

import harmonised.pmmo.client.events.ClientTickHandler;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.util.MsLoggy;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class CP_UpdateExperience {

    String skill;

    long xp;

    public CP_UpdateExperience(String skill, long xp) {
        this.skill = skill;
        this.xp = xp;
    }

    public CP_UpdateExperience(FriendlyByteBuf buf) {
        this.xp = buf.readLong();
        this.skill = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(this.xp);
        buf.writeUtf(this.skill);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            long currentXPraw = Core.get(LogicalSide.CLIENT).getData().getXpRaw(null, this.skill);
            if (currentXPraw != this.xp) {
                Core.get(LogicalSide.CLIENT).getData().setXpRaw(null, this.skill, this.xp);
                ClientTickHandler.addToGainList(this.skill, this.xp - currentXPraw);
                MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.XP, "Client Packet Handled for updating experience of " + this.skill + "[" + this.xp + "]");
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}