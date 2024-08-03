package dev.latvian.mods.kubejs.integration.forge.gamestages;

import dev.latvian.mods.kubejs.stages.Stages;
import java.util.Collection;
import java.util.List;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.data.IStageData;
import net.darkhax.gamestages.data.StageData;
import net.minecraft.world.entity.player.Player;

public class GameStagesWrapper extends Stages {

    public GameStagesWrapper(Player p) {
        super(p);
    }

    @Override
    public boolean addNoUpdate(String stage) {
        IStageData stageData = GameStageHelper.getPlayerData(this.player);
        if (stageData != null && !stageData.hasStage(stage)) {
            stageData.addStage(stage);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeNoUpdate(String stage) {
        IStageData stageData = GameStageHelper.getPlayerData(this.player);
        if (stageData != null && stageData.hasStage(stage)) {
            stageData.removeStage(stage);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Collection<String> getAll() {
        IStageData stageData = GameStageHelper.getPlayerData(this.player);
        return (Collection<String>) (stageData != null ? stageData.getStages() : List.of());
    }

    @Override
    public void replace(Collection<String> stages) {
        IStageData stageData = new StageData();
        for (String s : stages) {
            stageData.addStage(s);
        }
        this.setClientData(stageData);
    }

    private void setClientData(IStageData stageData) {
        GameStageClientHelper.setClientData(stageData);
    }
}