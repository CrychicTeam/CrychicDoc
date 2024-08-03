package dev.ftb.mods.ftbquests.net;

import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class TeamDataUpdate {

    public final UUID uuid;

    public final String name;

    public TeamDataUpdate(FriendlyByteBuf buffer) {
        this.uuid = buffer.readUUID();
        this.name = buffer.readUtf(32767);
    }

    public TeamDataUpdate(TeamData data) {
        this.uuid = data.getTeamId();
        this.name = data.getName();
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.uuid);
        buffer.writeUtf(this.name, 32767);
    }
}