package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class UpdateTaskProgressMessage extends BaseS2CMessage {

    private final UUID teamId;

    private final long task;

    private final long progress;

    public UpdateTaskProgressMessage(FriendlyByteBuf buffer) {
        this.teamId = buffer.readUUID();
        this.task = buffer.readLong();
        this.progress = buffer.readVarLong();
    }

    public UpdateTaskProgressMessage(TeamData teamData, long task, long progress) {
        this.teamId = teamData.getTeamId();
        this.task = task;
        this.progress = progress;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.UPDATE_TASK_PROGRESS;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.teamId);
        buffer.writeLong(this.task);
        buffer.writeVarLong(this.progress);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.updateTaskProgress(this.teamId, this.task, this.progress);
    }
}