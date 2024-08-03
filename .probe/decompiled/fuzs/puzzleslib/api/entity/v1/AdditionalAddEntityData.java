package fuzs.puzzleslib.api.entity.v1;

import fuzs.puzzleslib.impl.PuzzlesLibMod;
import fuzs.puzzleslib.impl.entity.ClientboundAddEntityDataMessage;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;

public interface AdditionalAddEntityData {

    static <T extends Entity & AdditionalAddEntityData> Packet<ClientGamePacketListener> getPacket(T entity) {
        ClientboundAddEntityPacket vanillaPacket = new ClientboundAddEntityPacket(entity);
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        entity.writeAdditionalAddEntityData(buf);
        return PuzzlesLibMod.NETWORK.toClientboundPacket(new ClientboundAddEntityDataMessage(vanillaPacket, buf));
    }

    void writeAdditionalAddEntityData(FriendlyByteBuf var1);

    void readAdditionalAddEntityData(FriendlyByteBuf var1);
}