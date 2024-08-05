package dev.latvian.mods.kubejs.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.latvian.mods.kubejs.KubeJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public class SendDataFromServerMessage extends BaseS2CMessage {

    private final String channel;

    private final CompoundTag data;

    public SendDataFromServerMessage(String c, @Nullable CompoundTag d) {
        this.channel = c;
        this.data = d;
    }

    SendDataFromServerMessage(FriendlyByteBuf buf) {
        this.channel = buf.readUtf(120);
        this.data = buf.readNbt();
    }

    @Override
    public MessageType getType() {
        return KubeJSNet.SEND_DATA_FROM_SERVER;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.channel, 120);
        buf.writeNbt(this.data);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (!this.channel.isEmpty()) {
            KubeJS.PROXY.handleDataFromServerPacket(this.channel, this.data);
        }
    }
}