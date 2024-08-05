package dev.latvian.mods.kubejs.player;

import dev.latvian.mods.kubejs.entity.LivingEntityEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public abstract class PlayerEventJS extends LivingEntityEventJS {

    public abstract Player getEntity();

    @Nullable
    @Override
    public Player getPlayer() {
        return this.getEntity();
    }

    @Info("Checks if the player has the specified game stage")
    public boolean hasGameStage(String stage) {
        return this.getEntity().kjs$getStages().has(stage);
    }

    @Info("Adds the specified game stage to the player")
    public void addGameStage(String stage) {
        this.getEntity().kjs$getStages().add(stage);
    }

    @Info("Removes the specified game stage from the player")
    public void removeGameStage(String stage) {
        this.getEntity().kjs$getStages().remove(stage);
    }
}