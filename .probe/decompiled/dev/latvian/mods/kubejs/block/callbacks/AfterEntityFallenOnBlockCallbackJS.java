package dev.latvian.mods.kubejs.block.callbacks;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;

public class AfterEntityFallenOnBlockCallbackJS extends EntitySteppedOnBlockCallbackJS {

    private boolean hasChangedVelocity = false;

    public AfterEntityFallenOnBlockCallbackJS(BlockGetter blockGetter, Entity entity) {
        super(entity.level(), entity, entity.getOnPos(), blockGetter.getBlockState(entity.getOnPos()));
    }

    @Info("Bounce the entity upwards by bounciness * their fall velocity.\nDo not make bounciness negative, as that is a recipe for a long and laggy trip to the void\n")
    public void bounce(float bounciness) {
        Vec3 deltaMovement = this.entity.getDeltaMovement();
        if (!this.isSuppressingBounce() && deltaMovement.y < 0.0) {
            this.entity.setDeltaMovement(deltaMovement.x, -deltaMovement.y * (double) bounciness, deltaMovement.z);
            this.hasChangedVelocity = true;
        }
    }

    @Info("Returns the Vec3 of the entity's velocity. Use .x, .y and .z to get the respective components of that")
    public Vec3 getVelocity() {
        return this.entity.getDeltaMovement();
    }

    @Info("Sets the entity's velocity")
    public void setVelocity(Vec3 vec) {
        this.entity.setDeltaMovement(vec);
        this.hasChangedVelocity = true;
    }

    @Info("Sets the entity's velocity")
    public void setVelocity(float x, float y, float z) {
        this.setVelocity(new Vec3((double) x, (double) y, (double) z));
    }

    @HideFromJS
    public boolean hasChangedVelocity() {
        return this.hasChangedVelocity;
    }
}