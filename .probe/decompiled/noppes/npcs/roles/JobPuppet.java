package noppes.npcs.roles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.data.role.IJobPuppet;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class JobPuppet extends JobInterface implements IJobPuppet {

    public JobPuppet.PartConfig head = new JobPuppet.PartConfig();

    public JobPuppet.PartConfig larm = new JobPuppet.PartConfig();

    public JobPuppet.PartConfig rarm = new JobPuppet.PartConfig();

    public JobPuppet.PartConfig body = new JobPuppet.PartConfig();

    public JobPuppet.PartConfig lleg = new JobPuppet.PartConfig();

    public JobPuppet.PartConfig rleg = new JobPuppet.PartConfig();

    public JobPuppet.PartConfig head2 = new JobPuppet.PartConfig();

    public JobPuppet.PartConfig larm2 = new JobPuppet.PartConfig();

    public JobPuppet.PartConfig rarm2 = new JobPuppet.PartConfig();

    public JobPuppet.PartConfig body2 = new JobPuppet.PartConfig();

    public JobPuppet.PartConfig lleg2 = new JobPuppet.PartConfig();

    public JobPuppet.PartConfig rleg2 = new JobPuppet.PartConfig();

    public boolean whileStanding = true;

    public boolean whileAttacking = false;

    public boolean whileMoving = false;

    public boolean animate = false;

    public int animationSpeed = 4;

    private int prevTicks = 0;

    private int startTick = 0;

    private float val = 0.0F;

    private float valNext = 0.0F;

    public JobPuppet(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public IJobPuppet.IJobPuppetPart getPart(int part) {
        if (part == 0) {
            return this.head;
        } else if (part == 1) {
            return this.larm;
        } else if (part == 2) {
            return this.rarm;
        } else if (part == 3) {
            return this.body;
        } else if (part == 4) {
            return this.lleg;
        } else if (part == 5) {
            return this.rleg;
        } else if (part == 6) {
            return this.head2;
        } else if (part == 7) {
            return this.larm2;
        } else if (part == 8) {
            return this.rarm2;
        } else if (part == 9) {
            return this.body2;
        } else if (part == 10) {
            return this.lleg2;
        } else if (part == 11) {
            return this.rleg2;
        } else {
            throw new CustomNPCsException("Unknown part " + part);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put("PuppetHead", this.head.writeNBT());
        compound.put("PuppetLArm", this.larm.writeNBT());
        compound.put("PuppetRArm", this.rarm.writeNBT());
        compound.put("PuppetBody", this.body.writeNBT());
        compound.put("PuppetLLeg", this.lleg.writeNBT());
        compound.put("PuppetRLeg", this.rleg.writeNBT());
        compound.put("PuppetHead2", this.head2.writeNBT());
        compound.put("PuppetLArm2", this.larm2.writeNBT());
        compound.put("PuppetRArm2", this.rarm2.writeNBT());
        compound.put("PuppetBody2", this.body2.writeNBT());
        compound.put("PuppetLLeg2", this.lleg2.writeNBT());
        compound.put("PuppetRLeg2", this.rleg2.writeNBT());
        compound.putBoolean("PuppetStanding", this.whileStanding);
        compound.putBoolean("PuppetAttacking", this.whileAttacking);
        compound.putBoolean("PuppetMoving", this.whileMoving);
        compound.putBoolean("PuppetAnimate", this.animate);
        compound.putInt("PuppetAnimationSpeed", this.animationSpeed);
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        this.head.readNBT(compound.getCompound("PuppetHead"));
        this.larm.readNBT(compound.getCompound("PuppetLArm"));
        this.rarm.readNBT(compound.getCompound("PuppetRArm"));
        this.body.readNBT(compound.getCompound("PuppetBody"));
        this.lleg.readNBT(compound.getCompound("PuppetLLeg"));
        this.rleg.readNBT(compound.getCompound("PuppetRLeg"));
        this.head2.readNBT(compound.getCompound("PuppetHead2"));
        this.larm2.readNBT(compound.getCompound("PuppetLArm2"));
        this.rarm2.readNBT(compound.getCompound("PuppetRArm2"));
        this.body2.readNBT(compound.getCompound("PuppetBody2"));
        this.lleg2.readNBT(compound.getCompound("PuppetLLeg2"));
        this.rleg2.readNBT(compound.getCompound("PuppetRLeg2"));
        this.whileStanding = compound.getBoolean("PuppetStanding");
        this.whileAttacking = compound.getBoolean("PuppetAttacking");
        this.whileMoving = compound.getBoolean("PuppetMoving");
        this.setIsAnimated(compound.getBoolean("PuppetAnimate"));
        this.setAnimationSpeed(compound.getInt("PuppetAnimationSpeed"));
    }

    @Override
    public boolean aiShouldExecute() {
        return false;
    }

    private float calcRotation(float r, float r2, float partialTicks) {
        if (!this.animate) {
            return r;
        } else {
            if (this.prevTicks != this.npc.f_19797_) {
                float speed = 0.0F;
                if (this.animationSpeed == 0) {
                    speed = 40.0F;
                } else if (this.animationSpeed == 1) {
                    speed = 24.0F;
                } else if (this.animationSpeed == 2) {
                    speed = 13.0F;
                } else if (this.animationSpeed == 3) {
                    speed = 10.0F;
                } else if (this.animationSpeed == 4) {
                    speed = 7.0F;
                } else if (this.animationSpeed == 5) {
                    speed = 4.0F;
                } else if (this.animationSpeed == 6) {
                    speed = 3.0F;
                } else if (this.animationSpeed == 7) {
                    speed = 2.0F;
                }
                int ticks = this.npc.f_19797_ - this.startTick;
                this.val = 1.0F - (Mth.cos((float) ticks / speed * (float) Math.PI / 2.0F) + 1.0F) / 2.0F;
                this.valNext = 1.0F - (Mth.cos((float) (ticks + 1) / speed * (float) Math.PI / 2.0F) + 1.0F) / 2.0F;
                this.prevTicks = this.npc.f_19797_;
            }
            float f = this.val + (this.valNext - this.val) * partialTicks;
            return r + (r2 - r) * f;
        }
    }

    public float getRotationX(JobPuppet.PartConfig part1, JobPuppet.PartConfig part2, float partialTicks) {
        return this.calcRotation(part1.rotationX, part2.rotationX, partialTicks);
    }

    public float getRotationY(JobPuppet.PartConfig part1, JobPuppet.PartConfig part2, float partialTicks) {
        return this.calcRotation(part1.rotationY, part2.rotationY, partialTicks);
    }

    public float getRotationZ(JobPuppet.PartConfig part1, JobPuppet.PartConfig part2, float partialTicks) {
        return this.calcRotation(part1.rotationZ, part2.rotationZ, partialTicks);
    }

    @Override
    public void reset() {
        this.val = 0.0F;
        this.valNext = 0.0F;
        this.prevTicks = 0;
        this.startTick = this.npc.f_19797_;
    }

    @Override
    public void delete() {
    }

    public boolean isActive() {
        return !this.npc.isAlive() ? false : this.whileAttacking && this.npc.isAttacking() || this.whileMoving && this.npc.isWalking() || this.whileStanding && !this.npc.isWalking();
    }

    @Override
    public boolean getIsAnimated() {
        return this.animate;
    }

    @Override
    public void setIsAnimated(boolean bo) {
        this.animate = bo;
        if (!bo) {
            this.val = 0.0F;
            this.valNext = 0.0F;
            this.prevTicks = 0;
        } else {
            this.startTick = this.npc.f_19797_;
        }
        this.npc.updateClient = true;
    }

    @Override
    public int getAnimationSpeed() {
        return this.animationSpeed;
    }

    @Override
    public void setAnimationSpeed(int speed) {
        this.animationSpeed = ValueUtil.CorrectInt(speed, 0, 7);
        this.npc.updateClient = true;
    }

    @Override
    public int getType() {
        return 9;
    }

    public class PartConfig implements IJobPuppet.IJobPuppetPart {

        public float rotationX = 0.0F;

        public float rotationY = 0.0F;

        public float rotationZ = 0.0F;

        public boolean disabled = false;

        public CompoundTag writeNBT() {
            CompoundTag compound = new CompoundTag();
            compound.putFloat("RotationX", this.rotationX);
            compound.putFloat("RotationY", this.rotationY);
            compound.putFloat("RotationZ", this.rotationZ);
            compound.putBoolean("Disabled", this.disabled);
            return compound;
        }

        public void readNBT(CompoundTag compound) {
            this.rotationX = ValueUtil.correctFloat(compound.getFloat("RotationX"), -1.0F, 1.0F);
            this.rotationY = ValueUtil.correctFloat(compound.getFloat("RotationY"), -1.0F, 1.0F);
            this.rotationZ = ValueUtil.correctFloat(compound.getFloat("RotationZ"), -1.0F, 1.0F);
            this.disabled = compound.getBoolean("Disabled");
        }

        @Override
        public int getRotationX() {
            return (int) ((this.rotationX + 1.0F) * 180.0F);
        }

        @Override
        public int getRotationY() {
            return (int) ((this.rotationY + 1.0F) * 180.0F);
        }

        @Override
        public int getRotationZ() {
            return (int) ((this.rotationZ + 1.0F) * 180.0F);
        }

        @Override
        public void setRotation(int x, int y, int z) {
            this.disabled = false;
            this.rotationX = ValueUtil.correctFloat((float) x / 180.0F - 1.0F, -1.0F, 1.0F);
            this.rotationY = ValueUtil.correctFloat((float) y / 180.0F - 1.0F, -1.0F, 1.0F);
            this.rotationZ = ValueUtil.correctFloat((float) z / 180.0F - 1.0F, -1.0F, 1.0F);
            JobPuppet.this.npc.updateClient = true;
        }
    }
}