package github.pitbox46.fightnbtintegration.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public interface IPacket {

    IPacket readPacketData(FriendlyByteBuf var1);

    void writePacketData(FriendlyByteBuf var1);

    void processPacket(NetworkEvent.Context var1);
}