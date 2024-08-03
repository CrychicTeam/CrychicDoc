package net.minecraft.world.entity.player;

import net.minecraft.nbt.CompoundTag;

public class Abilities {

    public boolean invulnerable;

    public boolean flying;

    public boolean mayfly;

    public boolean instabuild;

    public boolean mayBuild = true;

    private float flyingSpeed = 0.05F;

    private float walkingSpeed = 0.1F;

    public void addSaveData(CompoundTag compoundTag0) {
        CompoundTag $$1 = new CompoundTag();
        $$1.putBoolean("invulnerable", this.invulnerable);
        $$1.putBoolean("flying", this.flying);
        $$1.putBoolean("mayfly", this.mayfly);
        $$1.putBoolean("instabuild", this.instabuild);
        $$1.putBoolean("mayBuild", this.mayBuild);
        $$1.putFloat("flySpeed", this.flyingSpeed);
        $$1.putFloat("walkSpeed", this.walkingSpeed);
        compoundTag0.put("abilities", $$1);
    }

    public void loadSaveData(CompoundTag compoundTag0) {
        if (compoundTag0.contains("abilities", 10)) {
            CompoundTag $$1 = compoundTag0.getCompound("abilities");
            this.invulnerable = $$1.getBoolean("invulnerable");
            this.flying = $$1.getBoolean("flying");
            this.mayfly = $$1.getBoolean("mayfly");
            this.instabuild = $$1.getBoolean("instabuild");
            if ($$1.contains("flySpeed", 99)) {
                this.flyingSpeed = $$1.getFloat("flySpeed");
                this.walkingSpeed = $$1.getFloat("walkSpeed");
            }
            if ($$1.contains("mayBuild", 1)) {
                this.mayBuild = $$1.getBoolean("mayBuild");
            }
        }
    }

    public float getFlyingSpeed() {
        return this.flyingSpeed;
    }

    public void setFlyingSpeed(float float0) {
        this.flyingSpeed = float0;
    }

    public float getWalkingSpeed() {
        return this.walkingSpeed;
    }

    public void setWalkingSpeed(float float0) {
        this.walkingSpeed = float0;
    }
}