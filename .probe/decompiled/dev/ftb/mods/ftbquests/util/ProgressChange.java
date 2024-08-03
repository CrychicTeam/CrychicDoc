package dev.ftb.mods.ftbquests.util;

import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.Date;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class ProgressChange {

    private final BaseQuestFile file;

    private final Date date;

    private final QuestObjectBase origin;

    private final UUID playerId;

    private boolean reset;

    private boolean notifications;

    public ProgressChange(BaseQuestFile file, QuestObjectBase origin, UUID playerId) {
        this.file = file;
        this.origin = origin;
        this.playerId = playerId;
        this.date = new Date();
        this.reset = true;
        this.notifications = false;
    }

    public ProgressChange(BaseQuestFile f, FriendlyByteBuf buffer) {
        this.file = f;
        this.date = new Date();
        this.origin = this.file.getBase(buffer.readLong());
        this.reset = buffer.readBoolean();
        this.playerId = buffer.readUUID();
        this.notifications = buffer.readBoolean();
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.origin == null ? 0L : this.origin.id);
        buffer.writeBoolean(this.reset);
        buffer.writeUUID(this.playerId);
        buffer.writeBoolean(this.notifications);
    }

    public void maybeForceProgress(UUID teamId) {
        if (this.origin != null) {
            TeamData t = ServerQuestFile.INSTANCE.getOrCreateTeamData(teamId);
            this.origin.forceProgressRaw(t, this);
        }
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public Date getDate() {
        return this.date;
    }

    public ProgressChange setReset(boolean reset) {
        this.reset = reset;
        return this;
    }

    public boolean shouldReset() {
        return this.reset;
    }

    public ProgressChange withNotifications() {
        this.notifications = true;
        return this;
    }

    public boolean shouldNotify() {
        return this.notifications;
    }
}