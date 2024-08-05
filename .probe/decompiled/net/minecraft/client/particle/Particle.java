package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleGroup;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class Particle {

    private static final AABB INITIAL_AABB = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

    private static final double MAXIMUM_COLLISION_VELOCITY_SQUARED = Mth.square(100.0);

    protected final ClientLevel level;

    protected double xo;

    protected double yo;

    protected double zo;

    protected double x;

    protected double y;

    protected double z;

    protected double xd;

    protected double yd;

    protected double zd;

    private AABB bb = INITIAL_AABB;

    protected boolean onGround;

    protected boolean hasPhysics = true;

    private boolean stoppedByCollision;

    protected boolean removed;

    protected float bbWidth = 0.6F;

    protected float bbHeight = 1.8F;

    protected final RandomSource random = RandomSource.create();

    protected int age;

    protected int lifetime;

    protected float gravity;

    protected float rCol = 1.0F;

    protected float gCol = 1.0F;

    protected float bCol = 1.0F;

    protected float alpha = 1.0F;

    protected float roll;

    protected float oRoll;

    protected float friction = 0.98F;

    protected boolean speedUpWhenYMotionIsBlocked = false;

    protected Particle(ClientLevel clientLevel0, double double1, double double2, double double3) {
        this.level = clientLevel0;
        this.setSize(0.2F, 0.2F);
        this.setPos(double1, double2, double3);
        this.xo = double1;
        this.yo = double2;
        this.zo = double3;
        this.lifetime = (int) (4.0F / (this.random.nextFloat() * 0.9F + 0.1F));
    }

    public Particle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6) {
        this(clientLevel0, double1, double2, double3);
        this.xd = double4 + (Math.random() * 2.0 - 1.0) * 0.4F;
        this.yd = double5 + (Math.random() * 2.0 - 1.0) * 0.4F;
        this.zd = double6 + (Math.random() * 2.0 - 1.0) * 0.4F;
        double $$7 = (Math.random() + Math.random() + 1.0) * 0.15F;
        double $$8 = Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
        this.xd = this.xd / $$8 * $$7 * 0.4F;
        this.yd = this.yd / $$8 * $$7 * 0.4F + 0.1F;
        this.zd = this.zd / $$8 * $$7 * 0.4F;
    }

    public Particle setPower(float float0) {
        this.xd *= (double) float0;
        this.yd = (this.yd - 0.1F) * (double) float0 + 0.1F;
        this.zd *= (double) float0;
        return this;
    }

    public void setParticleSpeed(double double0, double double1, double double2) {
        this.xd = double0;
        this.yd = double1;
        this.zd = double2;
    }

    public Particle scale(float float0) {
        this.setSize(0.2F * float0, 0.2F * float0);
        return this;
    }

    public void setColor(float float0, float float1, float float2) {
        this.rCol = float0;
        this.gCol = float1;
        this.bCol = float2;
    }

    protected void setAlpha(float float0) {
        this.alpha = float0;
    }

    public void setLifetime(int int0) {
        this.lifetime = int0;
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd = this.yd - 0.04 * (double) this.gravity;
            this.move(this.xd, this.yd, this.zd);
            if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
                this.xd *= 1.1;
                this.zd *= 1.1;
            }
            this.xd = this.xd * (double) this.friction;
            this.yd = this.yd * (double) this.friction;
            this.zd = this.zd * (double) this.friction;
            if (this.onGround) {
                this.xd *= 0.7F;
                this.zd *= 0.7F;
            }
        }
    }

    public abstract void render(VertexConsumer var1, Camera var2, float var3);

    public abstract ParticleRenderType getRenderType();

    public String toString() {
        return this.getClass().getSimpleName() + ", Pos (" + this.x + "," + this.y + "," + this.z + "), RGBA (" + this.rCol + "," + this.gCol + "," + this.bCol + "," + this.alpha + "), Age " + this.age;
    }

    public void remove() {
        this.removed = true;
    }

    protected void setSize(float float0, float float1) {
        if (float0 != this.bbWidth || float1 != this.bbHeight) {
            this.bbWidth = float0;
            this.bbHeight = float1;
            AABB $$2 = this.getBoundingBox();
            double $$3 = ($$2.minX + $$2.maxX - (double) float0) / 2.0;
            double $$4 = ($$2.minZ + $$2.maxZ - (double) float0) / 2.0;
            this.setBoundingBox(new AABB($$3, $$2.minY, $$4, $$3 + (double) this.bbWidth, $$2.minY + (double) this.bbHeight, $$4 + (double) this.bbWidth));
        }
    }

    public void setPos(double double0, double double1, double double2) {
        this.x = double0;
        this.y = double1;
        this.z = double2;
        float $$3 = this.bbWidth / 2.0F;
        float $$4 = this.bbHeight;
        this.setBoundingBox(new AABB(double0 - (double) $$3, double1, double2 - (double) $$3, double0 + (double) $$3, double1 + (double) $$4, double2 + (double) $$3));
    }

    public void move(double double0, double double1, double double2) {
        if (!this.stoppedByCollision) {
            double $$3 = double0;
            double $$4 = double1;
            double $$5 = double2;
            if (this.hasPhysics && (double0 != 0.0 || double1 != 0.0 || double2 != 0.0) && double0 * double0 + double1 * double1 + double2 * double2 < MAXIMUM_COLLISION_VELOCITY_SQUARED) {
                Vec3 $$6 = Entity.collideBoundingBox(null, new Vec3(double0, double1, double2), this.getBoundingBox(), this.level, List.of());
                double0 = $$6.x;
                double1 = $$6.y;
                double2 = $$6.z;
            }
            if (double0 != 0.0 || double1 != 0.0 || double2 != 0.0) {
                this.setBoundingBox(this.getBoundingBox().move(double0, double1, double2));
                this.setLocationFromBoundingbox();
            }
            if (Math.abs($$4) >= 1.0E-5F && Math.abs(double1) < 1.0E-5F) {
                this.stoppedByCollision = true;
            }
            this.onGround = $$4 != double1 && $$4 < 0.0;
            if ($$3 != double0) {
                this.xd = 0.0;
            }
            if ($$5 != double2) {
                this.zd = 0.0;
            }
        }
    }

    protected void setLocationFromBoundingbox() {
        AABB $$0 = this.getBoundingBox();
        this.x = ($$0.minX + $$0.maxX) / 2.0;
        this.y = $$0.minY;
        this.z = ($$0.minZ + $$0.maxZ) / 2.0;
    }

    protected int getLightColor(float float0) {
        BlockPos $$1 = BlockPos.containing(this.x, this.y, this.z);
        return this.level.m_46805_($$1) ? LevelRenderer.getLightColor(this.level, $$1) : 0;
    }

    public boolean isAlive() {
        return !this.removed;
    }

    public AABB getBoundingBox() {
        return this.bb;
    }

    public void setBoundingBox(AABB aABB0) {
        this.bb = aABB0;
    }

    public Optional<ParticleGroup> getParticleGroup() {
        return Optional.empty();
    }
}