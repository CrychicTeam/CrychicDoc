package dev.shadowsoffire.attributeslib.packet;

import dev.shadowsoffire.attributeslib.client.AttributesLibClient;
import dev.shadowsoffire.placebo.network.MessageHelper;
import dev.shadowsoffire.placebo.network.MessageProvider;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class CritParticleMessage {

    protected final int entityId;

    public CritParticleMessage(int entityId) {
        this.entityId = entityId;
    }

    public static class Provider implements MessageProvider<CritParticleMessage> {

        @Override
        public Class<CritParticleMessage> getMsgClass() {
            return CritParticleMessage.class;
        }

        public void write(CritParticleMessage msg, FriendlyByteBuf buf) {
            buf.writeInt(msg.entityId);
        }

        public CritParticleMessage read(FriendlyByteBuf buf) {
            return new CritParticleMessage(buf.readInt());
        }

        public void handle(CritParticleMessage msg, Supplier<NetworkEvent.Context> ctx) {
            MessageHelper.handlePacket(() -> AttributesLibClient.apothCrit(msg.entityId), ctx);
        }
    }
}