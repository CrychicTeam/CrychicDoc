package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.Nullable;

@Cancelable
public class LivingExperienceDropEvent extends LivingEvent {

    @Nullable
    private final Player attackingPlayer;

    private final int originalExperiencePoints;

    private int droppedExperiencePoints;

    public LivingExperienceDropEvent(LivingEntity entity, @Nullable Player attackingPlayer, int originalExperience) {
        super(entity);
        this.attackingPlayer = attackingPlayer;
        this.originalExperiencePoints = this.droppedExperiencePoints = originalExperience;
    }

    public int getDroppedExperience() {
        return this.droppedExperiencePoints;
    }

    public void setDroppedExperience(int droppedExperience) {
        this.droppedExperiencePoints = droppedExperience;
    }

    @Nullable
    public Player getAttackingPlayer() {
        return this.attackingPlayer;
    }

    public int getOriginalExperience() {
        return this.originalExperiencePoints;
    }
}