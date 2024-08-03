package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class EntitySheepAIFollowCyclops extends Goal {

    Animal childAnimal;

    EntityCyclops cyclops;

    double moveSpeed;

    private int delayCounter;

    public EntitySheepAIFollowCyclops(Animal animal, double speed) {
        this.childAnimal = animal;
        this.moveSpeed = speed;
    }

    @Override
    public boolean canUse() {
        List<EntityCyclops> list = this.childAnimal.m_9236_().m_45976_(EntityCyclops.class, this.childAnimal.m_20191_().inflate(16.0, 8.0, 16.0));
        EntityCyclops cyclops = null;
        double d0 = Double.MAX_VALUE;
        for (EntityCyclops cyclops1 : list) {
            double d1 = this.childAnimal.m_20280_(cyclops1);
            if (d1 <= d0) {
                d0 = d1;
                cyclops = cyclops1;
            }
        }
        if (cyclops == null) {
            return false;
        } else if (d0 < 10.0) {
            return false;
        } else {
            this.cyclops = cyclops;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.cyclops.m_6084_()) {
            return false;
        } else {
            double d0 = this.childAnimal.m_20280_(this.cyclops);
            return d0 >= 9.0 && d0 <= 256.0;
        }
    }

    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        this.cyclops = null;
    }

    @Override
    public void tick() {
        if (--this.delayCounter <= 0) {
            this.delayCounter = this.m_183277_(10);
            if (this.childAnimal.m_20280_(this.cyclops) > 10.0) {
                Path path = this.getPathToLivingEntity(this.childAnimal, this.cyclops);
                if (path != null) {
                    this.childAnimal.m_21573_().moveTo(path, this.moveSpeed);
                }
            }
        }
    }

    public Path getPathToLivingEntity(Animal entityIn, EntityCyclops cyclops) {
        PathNavigation navi = entityIn.m_21573_();
        Vec3 Vector3d = DefaultRandomPos.getPosTowards(entityIn, 2, 7, cyclops.m_20182_(), (float) (Math.PI / 2));
        if (Vector3d != null) {
            BlockPos blockpos = BlockPos.containing(Vector3d);
            return navi.createPath(blockpos, 0);
        } else {
            return null;
        }
    }
}