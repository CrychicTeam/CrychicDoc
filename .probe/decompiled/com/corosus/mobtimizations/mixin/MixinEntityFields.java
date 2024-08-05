package com.corosus.mobtimizations.mixin;

import com.corosus.mobtimizations.MobtimizationEntityFields;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ Entity.class })
public abstract class MixinEntityFields implements MobtimizationEntityFields {

    @Unique
    private long mobtimizations_lastWanderTime;

    @Unique
    private long mobtimizations_lastPlayerScanTime;

    @Unique
    private boolean mobtimizations_playerInRange;

    @Override
    public long getlastWanderTime() {
        return this.mobtimizations_lastWanderTime;
    }

    @Override
    public void setlastWanderTime(long mobtimizations_lastWanderTime) {
        this.mobtimizations_lastWanderTime = mobtimizations_lastWanderTime;
    }

    @Override
    public long getlastPlayerScanTime() {
        return this.mobtimizations_lastPlayerScanTime;
    }

    @Override
    public void setlastPlayerScanTime(long mobtimizations_lastPlayerScanTime) {
        this.mobtimizations_lastPlayerScanTime = mobtimizations_lastPlayerScanTime;
    }

    @Override
    public boolean isplayerInRange() {
        return this.mobtimizations_playerInRange;
    }

    @Override
    public void setplayerInRange(boolean mobtimizations_playerInRange) {
        this.mobtimizations_playerInRange = mobtimizations_playerInRange;
    }
}