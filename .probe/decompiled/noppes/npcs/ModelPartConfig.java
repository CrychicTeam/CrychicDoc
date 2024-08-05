package noppes.npcs;

import net.minecraft.nbt.CompoundTag;

public class ModelPartConfig {

    public float scaleX = 1.0F;

    public float scaleY = 1.0F;

    public float scaleZ = 1.0F;

    public float transX = 0.0F;

    public float transY = 0.0F;

    public float transZ = 0.0F;

    public boolean notShared = false;

    public CompoundTag writeToNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putFloat("ScaleX", this.scaleX);
        compound.putFloat("ScaleY", this.scaleY);
        compound.putFloat("ScaleZ", this.scaleZ);
        compound.putFloat("TransX", this.transX);
        compound.putFloat("TransY", this.transY);
        compound.putFloat("TransZ", this.transZ);
        compound.putBoolean("NotShared", this.notShared);
        return compound;
    }

    public void readFromNBT(CompoundTag compound) {
        this.scaleX = this.checkValue(compound.getFloat("ScaleX"), 0.0F, 2.0F);
        this.scaleY = this.checkValue(compound.getFloat("ScaleY"), 0.0F, 2.0F);
        this.scaleZ = this.checkValue(compound.getFloat("ScaleZ"), 0.0F, 2.0F);
        this.transX = this.checkValue(compound.getFloat("TransX"), -1.0F, 1.0F);
        this.transY = this.checkValue(compound.getFloat("TransY"), -1.0F, 1.0F);
        this.transZ = this.checkValue(compound.getFloat("TransZ"), -1.0F, 1.0F);
        this.notShared = compound.getBoolean("NotShared");
    }

    public String toString() {
        return "ScaleX: " + this.scaleX + " - ScaleY: " + this.scaleY + " - ScaleZ: " + this.scaleZ;
    }

    public void setScale(float x, float y, float z) {
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
    }

    public void setScale(float x, float y) {
        this.scaleZ = this.scaleX = x;
        this.scaleY = y;
    }

    public float checkValue(float given, float min, float max) {
        if (given < min) {
            return min;
        } else {
            return given > max ? max : given;
        }
    }

    public void setTranslate(float transX, float transY, float transZ) {
        this.transX = transX;
        this.transY = transY;
        this.transZ = transZ;
    }

    public void copyValues(ModelPartConfig config) {
        this.scaleX = config.scaleX;
        this.scaleY = config.scaleY;
        this.scaleZ = config.scaleZ;
        this.transX = config.transX;
        this.transY = config.transY;
        this.transZ = config.transZ;
    }
}