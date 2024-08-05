package com.almostreliable.morejs.features.misc;

import dev.latvian.mods.kubejs.player.PlayerEventJS;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;

public class PiglinPlayerBehaviorEventJS extends PlayerEventJS {

    private final Player player;

    private final Piglin piglin;

    private final Optional<Player> playerNotWearingGoldArmor;

    private PiglinPlayerBehaviorEventJS.PiglinBehavior behavior = PiglinPlayerBehaviorEventJS.PiglinBehavior.KEEP;

    private boolean ignoreHoldingCheck;

    public PiglinPlayerBehaviorEventJS(Piglin piglin, Player player, Optional<Player> playerNotWearingGoldArmor) {
        this.piglin = piglin;
        this.player = player;
        this.playerNotWearingGoldArmor = playerNotWearingGoldArmor;
    }

    public void ignoreHoldingCheck() {
        this.ignoreHoldingCheck = true;
    }

    @Override
    public Player getEntity() {
        return this.player;
    }

    public Piglin getPiglin() {
        return this.piglin;
    }

    public boolean isAggressiveAlready() {
        return this.playerNotWearingGoldArmor.isPresent();
    }

    @Nullable
    public Player getPreviousTargetPlayer() {
        return (Player) this.playerNotWearingGoldArmor.orElse(null);
    }

    public PiglinPlayerBehaviorEventJS.PiglinBehavior getBehavior() {
        return this.behavior;
    }

    public void setBehavior(PiglinPlayerBehaviorEventJS.PiglinBehavior behavior) {
        this.behavior = behavior;
    }

    public boolean isIgnoreHoldingCheck() {
        return this.ignoreHoldingCheck;
    }

    public static enum PiglinBehavior {

        ATTACK, IGNORE, KEEP
    }
}