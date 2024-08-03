package dev.xkmc.modulargolems.events.event;

import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;

public class HumanoidGolemEvent extends GolemEvent {

    private final HumanoidGolemEntity golem;

    public HumanoidGolemEvent(HumanoidGolemEntity golem) {
        super(golem);
        this.golem = golem;
    }

    public HumanoidGolemEntity getEntity() {
        return this.golem;
    }
}