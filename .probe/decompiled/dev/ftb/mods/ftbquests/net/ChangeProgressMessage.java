package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.util.NetUtils;
import dev.ftb.mods.ftbquests.util.ProgressChange;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.network.FriendlyByteBuf;

public class ChangeProgressMessage extends BaseC2SMessage {

    private final UUID teamId;

    private final ProgressChange progressChange;

    ChangeProgressMessage(FriendlyByteBuf buffer) {
        this.teamId = buffer.readUUID();
        this.progressChange = new ProgressChange(ServerQuestFile.INSTANCE, buffer);
    }

    public ChangeProgressMessage(UUID teamId, ProgressChange progressChange) {
        this.teamId = teamId;
        this.progressChange = progressChange;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.CHANGE_PROGRESS;
    }

    public static void sendToServer(TeamData team, QuestObjectBase object, Consumer<ProgressChange> progressChange) {
        if (!team.isLocked()) {
            ProgressChange change = new ProgressChange(team.getFile(), object, FTBQuestsClient.getClientPlayer().m_20148_());
            progressChange.accept(change);
            new ChangeProgressMessage(team.getTeamId(), change).sendToServer();
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.teamId);
        this.progressChange.write(buffer);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (NetUtils.canEdit(context)) {
            this.progressChange.maybeForceProgress(this.teamId);
        }
    }
}