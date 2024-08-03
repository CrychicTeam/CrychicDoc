package dev.ftb.mods.ftbxmodcompat.ftbquests.kubejs;

import dev.ftb.mods.ftbquests.api.FTBQuestsAPI;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.QuestObjectType;
import dev.ftb.mods.ftbquests.quest.QuestShape;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.latvian.mods.kubejs.core.PlayerKJS;
import dev.latvian.mods.kubejs.util.AttachedData;
import java.util.Map;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FTBQuestsKubeJSWrapper {

    public static final FTBQuestsKubeJSWrapper INSTANCE = new FTBQuestsKubeJSWrapper();

    public Map<String, QuestShape> getQuestShapes() {
        return QuestShape.map();
    }

    public Map<String, QuestObjectType> getQuestObjectTypes() {
        return QuestObjectType.NAME_MAP.map;
    }

    public BaseQuestFile getFile(Level level) {
        return FTBQuestsAPI.api().getQuestFile(level.isClientSide);
    }

    @Nullable
    public TeamData getData(Level level, UUID uuid) {
        return this.getFile(level).getOrCreateTeamData((Team) FTBTeamsAPI.api().getManager().getTeamForPlayerID(uuid).orElse(null));
    }

    @Nullable
    public TeamData getData(Player player) {
        return this.getFile(player.m_9236_()).getOrCreateTeamData(player);
    }

    @Nullable
    public QuestObjectBase getObject(Level level, Object id) {
        BaseQuestFile file = this.getFile(level);
        return file.getBase(file.getID(id));
    }

    @Nullable
    public FTBQuestsKubeJSPlayerData getServerDataFromPlayer(Player player) {
        try {
            AttachedData<?> data = ((PlayerKJS) player).kjs$getData();
            return (FTBQuestsKubeJSPlayerData) data.get("ftbquests");
        } catch (Throwable var3) {
            return null;
        }
    }

    @Nullable
    public FTBQuestsKubeJSPlayerData getServerDataFromSource(CommandSourceStack source) {
        try {
            return this.getServerDataFromPlayer(source.getPlayerOrException());
        } catch (Throwable var3) {
            return null;
        }
    }
}