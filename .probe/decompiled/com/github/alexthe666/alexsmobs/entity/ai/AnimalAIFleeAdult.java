package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import java.util.List;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class AnimalAIFleeAdult extends Goal {

    private final Animal childAnimal;

    private Animal parentAnimal;

    private final double moveSpeed;

    private final double fleeDistance;

    private int delayCounter;

    private Path path;

    public AnimalAIFleeAdult(Animal animal, double speed, double fleeDistance) {
        this.childAnimal = animal;
        this.moveSpeed = speed;
        this.fleeDistance = fleeDistance;
    }

    @Override
    public boolean canUse() {
        if (this.childAnimal.m_146764_() >= 0) {
            return false;
        } else {
            List<? extends Animal> list = this.childAnimal.m_9236_().m_45976_(this.childAnimal.getClass(), this.childAnimal.m_20191_().inflate(this.fleeDistance, 4.0, this.fleeDistance));
            Animal animalentity = null;
            double d0 = Double.MAX_VALUE;
            for (Animal animalentity1 : list) {
                if (animalentity1.m_146764_() >= 0) {
                    double d1 = this.childAnimal.m_20280_(animalentity1);
                    if (!(d1 > d0)) {
                        d0 = d1;
                        animalentity = animalentity1;
                    }
                }
            }
            if (animalentity == null) {
                return false;
            } else if (d0 > 19.0) {
                return false;
            } else {
                this.parentAnimal = animalentity;
                Vec3 vec3d = LandRandomPos.getPosAway(this.childAnimal, (int) this.fleeDistance, 7, new Vec3(this.parentAnimal.m_20185_(), this.parentAnimal.m_20186_(), this.parentAnimal.m_20189_()));
                if (vec3d == null) {
                    return false;
                } else if (this.parentAnimal.m_20275_(vec3d.x, vec3d.y, vec3d.z) < this.parentAnimal.m_20280_(this.childAnimal)) {
                    return false;
                } else {
                    this.path = this.childAnimal.m_21573_().createPath(AMBlockPos.fromVec3(vec3d), 0);
                    return this.path != null;
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.childAnimal.m_146764_() >= 0) {
            return false;
        } else {
            return !this.parentAnimal.m_6084_() ? false : !this.childAnimal.m_21573_().isDone();
        }
    }

    @Override
    public void start() {
        this.childAnimal.m_21573_().moveTo(this.path, this.moveSpeed);
    }

    @Override
    public void stop() {
        this.parentAnimal = null;
        this.childAnimal.m_21573_().stop();
        this.path = null;
    }

    @Override
    public void tick() {
    }
}