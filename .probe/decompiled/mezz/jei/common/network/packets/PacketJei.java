package mezz.jei.common.network.packets;

import io.netty.buffer.Unpooled;
import mezz.jei.common.network.IPacketId;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.tuple.Pair;

public abstract class PacketJei {

    public final Pair<FriendlyByteBuf, Integer> getPacketData() {
        IPacketId packetId = this.getPacketId();
        int packetIdOrdinal = packetId.ordinal();
        FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
        packetBuffer.writeByte(packetIdOrdinal);
        this.writePacketData(packetBuffer);
        return Pair.of(packetBuffer, packetIdOrdinal);
    }

    protected abstract IPacketId getPacketId();

    protected abstract void writePacketData(FriendlyByteBuf var1);
}