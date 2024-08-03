package dev.xkmc.modulargolems.content.entity.mode;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.data.MGLangData;

public class PatrolMode extends GolemMode {

    protected PatrolMode(boolean positioned, boolean movable, boolean wander, MGLangData lang, MGLangData name) {
        super(positioned, movable, wander, lang, name);
    }

    @Override
    public double getStartFollowDistance(AbstractGolemEntity<?, ?> golem) {
        return 0.0;
    }

    @Override
    public void tick(AbstractGolemEntity<?, ?> golem) {
        if (golem.m_20069_() || golem.m_20096_()) {
            if (golem.m_21573_().isDone()) {
                golem.advancePatrolStage();
            }
        }
    }

    @Override
    public double getStopDistance() {
        return 0.0;
    }
}