package dev.xkmc.modulargolems.events.event;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.world.entity.ExperienceOrb;

public class GolemHandleExpEvent extends GolemEvent {

    private final ExperienceOrb orb;

    public GolemHandleExpEvent(AbstractGolemEntity<?, ?> golem, ExperienceOrb exp) {
        super(golem);
        this.orb = exp;
    }

    public ExperienceOrb getOrb() {
        return this.orb;
    }
}