package dev.ftb.mods.ftbquests.api;

import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestLink;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbteams.api.Team;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface QuestFile {

    boolean isServerSide();

    boolean canEdit();

    @Nullable
    TeamData getNullableTeamData(UUID var1);

    TeamData getOrCreateTeamData(UUID var1);

    TeamData getOrCreateTeamData(Team var1);

    TeamData getOrCreateTeamData(Entity var1);

    Collection<TeamData> getAllTeamData();

    void forAllChapters(Consumer<Chapter> var1);

    void forAllQuests(Consumer<Quest> var1);

    void forAllQuestLinks(Consumer<QuestLink> var1);
}