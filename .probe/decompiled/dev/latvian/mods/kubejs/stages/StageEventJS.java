package dev.latvian.mods.kubejs.stages;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import net.minecraft.world.entity.player.Player;

public class StageEventJS extends PlayerEventJS {

    private final StageChangeEvent event;

    public StageEventJS(StageChangeEvent e) {
        this.event = e;
    }

    public Stages getPlayerStages() {
        return this.event.getPlayerStages();
    }

    @Override
    public Player getEntity() {
        return this.event.getPlayer();
    }

    public String getStage() {
        return this.event.getStage();
    }
}