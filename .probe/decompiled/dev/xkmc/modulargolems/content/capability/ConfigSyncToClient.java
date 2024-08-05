package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.UUID;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class ConfigSyncToClient extends SerialPacketBase {

    @SerialField
    public UUID id;

    @SerialField
    public int color;

    @SerialField
    public GolemConfigEntry entry;

    @Deprecated
    public ConfigSyncToClient() {
    }

    public ConfigSyncToClient(GolemConfigEntry entry) {
        this.entry = entry;
        this.id = entry.getID();
        this.color = entry.getColor();
    }

    public void handle(NetworkEvent.Context context) {
        ClientDataHandler.handleUpdate(this.entry.init(this.id, this.color));
    }
}