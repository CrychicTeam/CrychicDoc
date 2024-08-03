package dev.ftb.mods.ftbxmodcompat.ftbquests.kubejs;

import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;

public class FTBQuestsKubeJSTeamDataWrapper extends FTBQuestsKubeJSTeamData {

    private final TeamData teamData;

    public FTBQuestsKubeJSTeamDataWrapper(TeamData d) {
        this.teamData = d;
    }

    @Override
    public BaseQuestFile getFile() {
        return this.teamData.getFile();
    }

    @Override
    public TeamData getData() {
        return this.teamData;
    }
}