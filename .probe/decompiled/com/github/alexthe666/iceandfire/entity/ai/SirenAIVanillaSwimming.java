package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;

public class SirenAIVanillaSwimming extends Goal {

    private final EntitySiren entity;

    public SirenAIVanillaSwimming(EntitySiren entityIn) {
        this.entity = entityIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        if (entityIn.m_21573_() instanceof GroundPathNavigation) {
            entityIn.m_21573_().setCanFloat(true);
        }
    }

    @Override
    public boolean canUse() {
        return (this.entity.m_20069_() || this.entity.m_20077_()) && this.entity.wantsToSing();
    }

    @Override
    public void tick() {
        if (this.entity.m_217043_().nextFloat() < 0.8F) {
            this.entity.m_21569_().jump();
        }
    }
}