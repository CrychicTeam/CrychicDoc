package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class VallumraptorWanderGoal extends RandomStrollGoal {

    private VallumraptorEntity raptor;

    public VallumraptorWanderGoal(VallumraptorEntity vallumraptor, double speed, int rate) {
        super(vallumraptor, speed, rate);
        this.raptor = vallumraptor;
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        return this.raptor.isPackFollower() ? DefaultRandomPos.getPosTowards(this.f_25725_, 10, 7, ((Entity) this.raptor.getPackLeader()).position(), (float) (Math.PI / 2)) : DefaultRandomPos.getPos(this.f_25725_, 16, 7);
    }
}