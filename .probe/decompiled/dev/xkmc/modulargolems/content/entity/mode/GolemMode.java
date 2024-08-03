package dev.xkmc.modulargolems.content.entity.mode;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class GolemMode {

    private final int id;

    private final boolean positioned;

    private final boolean movable;

    private final boolean wander;

    private final MGLangData lang;

    private final MGLangData name;

    protected GolemMode(boolean positioned, boolean movable, boolean wander, MGLangData lang, MGLangData name) {
        this.positioned = positioned;
        this.movable = movable;
        this.wander = wander;
        this.lang = lang;
        this.name = name;
        this.id = GolemModes.LIST.size();
        GolemModes.LIST.add(this);
    }

    public boolean canChangeDimensions() {
        return !this.hasPos();
    }

    public int getID() {
        return this.id;
    }

    public boolean isMovable() {
        return this.movable;
    }

    public boolean hasPos() {
        return this.positioned;
    }

    public boolean couldRandomStroll() {
        return this.wander;
    }

    public Component getName() {
        return this.name.get();
    }

    public Component getDesc(AbstractGolemEntity<?, ?> golem) {
        if (this.positioned) {
            BlockPos p = golem.getGuardPos();
            return this.lang.get(p.m_123341_(), p.m_123342_(), p.m_123343_());
        } else {
            return this.lang.get();
        }
    }

    public double getStartFollowDistance(AbstractGolemEntity<?, ?> golem) {
        return this.couldRandomStroll() ? MGConfig.COMMON.stopWanderRadius.get() : MGConfig.COMMON.startFollowRadius.get();
    }

    public void tick(AbstractGolemEntity<?, ?> golem) {
    }

    public double getStopDistance() {
        return 3.0;
    }
}