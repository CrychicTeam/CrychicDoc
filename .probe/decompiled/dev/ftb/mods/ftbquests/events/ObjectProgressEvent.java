package dev.ftb.mods.ftbquests.events;

import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.Date;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;

public abstract class ObjectProgressEvent<T extends QuestObject> {

    protected final QuestProgressEventData<T> data;

    protected ObjectProgressEvent(QuestProgressEventData<T> d) {
        this.data = d;
    }

    public boolean isCancelable() {
        return true;
    }

    public Date getTime() {
        return this.data.getTime();
    }

    public TeamData getData() {
        return this.data.getTeamData();
    }

    public T getObject() {
        return this.data.getObject();
    }

    public List<ServerPlayer> getOnlineMembers() {
        return this.data.getOnlineMembers();
    }

    public List<ServerPlayer> getNotifiedPlayers() {
        return this.data.getNotifiedPlayers();
    }
}