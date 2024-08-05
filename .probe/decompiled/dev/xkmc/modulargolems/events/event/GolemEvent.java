package dev.xkmc.modulargolems.events.event;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraftforge.event.entity.living.LivingEvent;

public class GolemEvent extends LivingEvent {

    private final AbstractGolemEntity<?, ?> golem;

    public GolemEvent(AbstractGolemEntity<?, ?> golem) {
        super(golem);
        this.golem = golem;
    }

    public AbstractGolemEntity<?, ?> getEntity() {
        return this.golem;
    }
}