package dev.latvian.mods.kubejs.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.latvian.mods.kubejs.KubeJS;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class AddStageMessage extends BaseS2CMessage {

    private final UUID player;

    private final String stage;

    public AddStageMessage(UUID p, String s) {
        this.player = p;
        this.stage = s;
    }

    AddStageMessage(FriendlyByteBuf buf) {
        this.player = buf.readUUID();
        this.stage = buf.readUtf();
    }

    @Override
    public MessageType getType() {
        return KubeJSNet.ADD_STAGE;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.player);
        buf.writeUtf(this.stage);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player p0 = KubeJS.PROXY.getClientPlayer();
        if (p0 != null) {
            Player p = this.player.equals(p0.m_20148_()) ? p0 : p0.m_9236_().m_46003_(this.player);
            if (p != null) {
                p.kjs$getStages().add(this.stage);
            }
        }
    }
}