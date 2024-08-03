package dev.ftb.mods.ftbxmodcompat.ftbquests.kubejs;

import dev.ftb.mods.ftbquests.events.ObjectCompletedEvent;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.latvian.mods.kubejs.player.EntityArrayList;
import dev.latvian.mods.kubejs.server.ServerEventJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class QuestObjectCompletedEventJS extends ServerEventJS {

    public final ObjectCompletedEvent<?> event;

    private final FTBQuestsKubeJSTeamDataWrapper wrapper;

    public QuestObjectCompletedEventJS(ObjectCompletedEvent<?> e) {
        super(UtilsJS.staticServer);
        this.event = e;
        this.wrapper = new FTBQuestsKubeJSTeamDataWrapper(this.event.getData());
    }

    public FTBQuestsKubeJSTeamDataWrapper getData() {
        return this.wrapper;
    }

    public QuestObject getObject() {
        return this.event.getObject();
    }

    public EntityArrayList getNotifiedPlayers() {
        return new EntityArrayList(this.server.overworld(), this.event.getNotifiedPlayers());
    }

    public EntityArrayList getOnlineMembers() {
        return this.getData().getOnlineMembers();
    }

    @Nullable
    public ServerPlayer getPlayer() {
        return this.event.getData().getFile() instanceof ServerQuestFile file ? file.getCurrentPlayer() : null;
    }
}