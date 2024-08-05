package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;

public class Swim extends Behavior<Mob> {

    private final float chance;

    public Swim(float float0) {
        super(ImmutableMap.of());
        this.chance = float0;
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Mob mob1) {
        return mob1.m_20069_() && mob1.m_204036_(FluidTags.WATER) > mob1.m_20204_() || mob1.m_20077_();
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Mob mob1, long long2) {
        return this.checkExtraStartConditions(serverLevel0, mob1);
    }

    protected void tick(ServerLevel serverLevel0, Mob mob1, long long2) {
        if (mob1.m_217043_().nextFloat() < this.chance) {
            mob1.getJumpControl().jump();
        }
    }
}