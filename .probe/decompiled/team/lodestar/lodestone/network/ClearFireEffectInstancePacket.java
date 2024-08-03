package team.lodestar.lodestone.network;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.handlers.FireEffectHandler;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;

public class ClearFireEffectInstancePacket extends LodestoneClientPacket {

    private final int entityID;

    public ClearFireEffectInstancePacket(int entityID) {
        this.entityID = entityID;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        FireEffectHandler.setCustomFireInstance(Minecraft.getInstance().level.getEntity(this.entityID), null);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, ClearFireEffectInstancePacket.class, ClearFireEffectInstancePacket::encode, ClearFireEffectInstancePacket::decode, LodestoneClientPacket::handle);
    }

    public static ClearFireEffectInstancePacket decode(FriendlyByteBuf buf) {
        return new ClearFireEffectInstancePacket(buf.readInt());
    }
}