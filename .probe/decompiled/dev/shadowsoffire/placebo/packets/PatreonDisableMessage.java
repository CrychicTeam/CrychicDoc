package dev.shadowsoffire.placebo.packets;

import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.network.MessageHelper;
import dev.shadowsoffire.placebo.network.MessageProvider;
import dev.shadowsoffire.placebo.network.PacketDistro;
import dev.shadowsoffire.placebo.patreon.TrailsManager;
import dev.shadowsoffire.placebo.patreon.WingsManager;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class PatreonDisableMessage {

    private int type;

    private UUID id;

    public PatreonDisableMessage(int type) {
        this.type = type;
    }

    public PatreonDisableMessage(int type, UUID id) {
        this(type);
        this.id = id;
    }

    public static class Provider implements MessageProvider<PatreonDisableMessage> {

        @Override
        public Class<PatreonDisableMessage> getMsgClass() {
            return PatreonDisableMessage.class;
        }

        public void write(PatreonDisableMessage msg, FriendlyByteBuf buf) {
            buf.writeByte(msg.type);
            buf.writeByte(msg.id == null ? 0 : 1);
            if (msg.id != null) {
                buf.writeUUID(msg.id);
            }
        }

        public PatreonDisableMessage read(FriendlyByteBuf buf) {
            int type = buf.readByte();
            return buf.readByte() == 1 ? new PatreonDisableMessage(type, buf.readUUID()) : new PatreonDisableMessage(type);
        }

        public void handle(PatreonDisableMessage msg, Supplier<NetworkEvent.Context> ctx) {
            if (((NetworkEvent.Context) ctx.get()).getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                MessageHelper.handlePacket(() -> PacketDistro.sendToAll(Placebo.CHANNEL, new PatreonDisableMessage(msg.type, ((NetworkEvent.Context) ctx.get()).getSender().m_20148_())), ctx);
            } else {
                MessageHelper.handlePacket(() -> {
                    Set<UUID> set = msg.type == 0 ? TrailsManager.DISABLED : WingsManager.DISABLED;
                    if (set.contains(msg.id)) {
                        set.remove(msg.id);
                    } else {
                        set.add(msg.id);
                    }
                }, ctx);
            }
        }
    }
}