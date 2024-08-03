package dev.latvian.mods.kubejs.stages;

import net.minecraft.world.entity.player.Player;

public class StageChangeEvent {

    private final Stages stages;

    private final String stage;

    StageChangeEvent(Stages p, String s) {
        this.stages = p;
        this.stage = s;
    }

    public Player getPlayer() {
        return this.stages.player;
    }

    public String getStage() {
        return this.stage;
    }

    public Stages getPlayerStages() {
        return this.stages;
    }
}