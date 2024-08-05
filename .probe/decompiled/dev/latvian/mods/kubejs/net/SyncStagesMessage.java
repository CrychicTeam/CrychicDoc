package dev.latvian.mods.kubejs.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.latvian.mods.kubejs.KubeJS;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class SyncStagesMessage extends BaseS2CMessage {

    private final UUID player;

    private final Collection<String> stages;

    public SyncStagesMessage(UUID p, Collection<String> s) {
        this.player = p;
        this.stages = s;
    }

    SyncStagesMessage(FriendlyByteBuf buf) {
        this.player = buf.readUUID();
        int s = buf.readVarInt();
        this.stages = new ArrayList(s);
        for (int i = 0; i < s; i++) {
            this.stages.add(buf.readUtf());
        }
    }

    @Override
    public MessageType getType() {
        return KubeJSNet.SYNC_STAGES;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.player);
        buf.writeVarInt(this.stages.size());
        for (String s : this.stages) {
            buf.writeUtf(s);
        }
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        Player p0 = KubeJS.PROXY.getClientPlayer();
        if (p0 != null) {
            Player p = this.player.equals(p0.m_20148_()) ? p0 : p0.m_9236_().m_46003_(this.player);
            if (p != null) {
                p.kjs$getStages().replace(this.stages);
            }
        }
    }
}