package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class ConfigHeartBeatToServer extends SerialPacketBase {

    @SerialField
    public UUID id;

    @SerialField
    public int color;

    @Deprecated
    public ConfigHeartBeatToServer() {
    }

    public ConfigHeartBeatToServer(UUID id, int color) {
        this.id = id;
        this.color = color;
    }

    public void handle(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player != null) {
            GolemConfigEntry entry = GolemConfigStorage.get(player.m_9236_()).getStorage(this.id, this.color);
            if (entry != null) {
                entry.heartBeat(player.serverLevel(), player);
            }
        }
    }
}