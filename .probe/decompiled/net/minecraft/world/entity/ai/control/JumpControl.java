package net.minecraft.world.entity.ai.control;

import net.minecraft.world.entity.Mob;

public class JumpControl implements Control {

    private final Mob mob;

    protected boolean jump;

    public JumpControl(Mob mob0) {
        this.mob = mob0;
    }

    public void jump() {
        this.jump = true;
    }

    public void tick() {
        this.mob.m_6862_(this.jump);
        this.jump = false;
    }
}