package net.blay09.mods.balm.api.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class UseItemEvent extends BalmEvent {

    private final Player player;

    private final Level level;

    private final InteractionHand hand;

    private InteractionResult result = InteractionResult.PASS;

    public UseItemEvent(Player player, Level level, InteractionHand hand) {
        this.player = player;
        this.level = level;
        this.hand = hand;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Level getLevel() {
        return this.level;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public InteractionResult getInteractionResult() {
        return this.result;
    }

    public void setResult(InteractionResult result) {
        this.result = result;
    }
}