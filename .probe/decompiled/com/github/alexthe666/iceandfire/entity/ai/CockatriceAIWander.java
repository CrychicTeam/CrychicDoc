package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class CockatriceAIWander extends Goal {

    private final EntityCockatrice cockatrice;

    private double xPosition;

    private double yPosition;

    private double zPosition;

    private final double speed;

    private int executionChance;

    private boolean mustUpdate;

    public CockatriceAIWander(EntityCockatrice creatureIn, double speedIn) {
        this(creatureIn, speedIn, 20);
    }

    public CockatriceAIWander(EntityCockatrice creatureIn, double speedIn, int chance) {
        this.cockatrice = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.cockatrice.canMove()) {
            return false;
        } else if (this.cockatrice.getCommand() != 3 && this.cockatrice.getCommand() != 0) {
            return false;
        } else if (!this.mustUpdate && this.cockatrice.m_217043_().nextInt(this.executionChance) != 0) {
            return false;
        } else {
            Vec3 Vector3d = DefaultRandomPos.getPos(this.cockatrice, 10, 7);
            if (Vector3d == null) {
                return false;
            } else {
                this.xPosition = Vector3d.x;
                this.yPosition = Vector3d.y;
                this.zPosition = Vector3d.z;
                this.mustUpdate = false;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.cockatrice.m_21573_().isDone();
    }

    @Override
    public void start() {
        this.cockatrice.m_21573_().moveTo(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int newchance) {
        this.executionChance = newchance;
    }
}