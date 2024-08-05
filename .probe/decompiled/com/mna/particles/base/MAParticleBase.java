package com.mna.particles.base;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.FXMovementType;
import com.mna.particles.types.render.ParticleRenderTypes;
import com.mna.tools.math.MathUtils;
import com.mna.tools.math.Vector3;
import com.mojang.math.Axis;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Vector3f;

public abstract class MAParticleBase extends TextureSheetParticle {

    protected FXMovementType movementType;

    private Vector3 start;

    private Vector3 end;

    private Vector3 control_a;

    private Vector3 control_b;

    private ItemStack stack = null;

    private float uo;

    private float vo;

    private boolean decay_velocity = false;

    private boolean orbit_clockwise = true;

    private double angle;

    private double flare = -1.0;

    protected float maxAlpha = 1.0F;

    protected float life_padding = 0.0F;

    protected ArrayList<Vector3> colorTransitions = new ArrayList();

    protected MAParticleBase(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (!this.apply_aging()) {
            this.move();
        }
    }

    public void setMoveVelocity(double x, double y, double z, boolean decay) {
        this.f_107215_ = x;
        this.f_107216_ = y;
        this.f_107217_ = z;
        this.decay_velocity = decay;
        this.movementType = FXMovementType.VELOCITY;
    }

    public void setMoveStationary() {
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.movementType = FXMovementType.STATIONARY;
    }

    public void setMoveLerp(Vector3 start, Vector3 end) {
        this.start = start;
        this.end = end;
        this.movementType = FXMovementType.LERP_POINT;
    }

    public void setMoveLerp(double x, double y, double z, double goal_x, double goal_y, double goal_z) {
        this.setMoveLerp(new Vector3(x, y, z), new Vector3(goal_x, goal_y, goal_z));
    }

    public void setMoveBezier(Vector3 start, Vector3 end, Vector3 controlA, Vector3 controlB) {
        this.start = start;
        this.end = end;
        this.control_a = controlA;
        this.control_b = controlB;
        this.movementType = FXMovementType.BEZIER_POINT;
    }

    public void setMoveBezier(Vector3 start, Vector3 end) {
        this.start = start;
        this.end = end;
        this.generateBezierControlPoints();
        this.movementType = FXMovementType.BEZIER_POINT;
    }

    public void setMoveBezier(double x, double y, double z, double goal_x, double goal_y, double goal_z) {
        this.setMoveBezier(new Vector3(x, y, z), new Vector3(goal_x, goal_y, goal_z));
    }

    public void setMoveRandomly(double x, double y, double z) {
        this.movementType = FXMovementType.VELOCITY;
        this.f_107215_ = -x + Math.random() * 2.0 * y;
        this.f_107216_ = -y + Math.random() * 2.0 * y;
        this.f_107217_ = -z + Math.random() * 2.0 * z;
    }

    public void setMoveOrbit(double cX, double cY, double cZ, double forwardSpeed, double upSpeed, double radius) {
        this.start = new Vector3(cX, cY, cZ);
        this.end = new Vector3(forwardSpeed, radius, upSpeed);
        this.f_107216_ = upSpeed;
        this.orbit_clockwise = Math.random() < 0.5;
        this.f_107225_ = 30;
        this.angle = Math.random() * 360.0;
        this.movementType = FXMovementType.ORBIT;
        this.moveOrbit();
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
    }

    public void setMoveOrbit(double cX, double cY, double cZ, double forwardSpeed, double upSpeed, double radius, double flare) {
        this.flare = flare;
        this.setMoveOrbit(cX, cY, cZ, forwardSpeed, upSpeed, radius);
    }

    public void setMoveSphereOrbit(double cX, double cY, double cZ, double forwardSpeed, double tilt, double radius) {
        this.start = new Vector3(cX, cY, cZ);
        this.end = new Vector3(forwardSpeed, radius, tilt);
        this.f_107225_ = 30;
        this.orbit_clockwise = Math.random() < 0.5;
        this.angle = Math.random() * 360.0;
        this.movementType = FXMovementType.SPHERE_ORBIT;
        this.moveSphereOrbit();
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
    }

    public void setRenderAsStackParticles(ItemStack stack) {
        this.stack = stack.copy();
        this.m_108337_(Minecraft.getInstance().getItemRenderer().getModel(this.stack, this.f_107208_, (LivingEntity) null, 0).getParticleIcon(ModelData.EMPTY));
        this.uo = (float) (Math.random() * 3.0);
        this.vo = (float) (Math.random() * 3.0);
        this.f_107663_ /= 2.0F;
    }

    public void setMaxAlpha(float maxAlpha) {
        this.maxAlpha = MathUtils.clamp01(maxAlpha);
    }

    @Override
    protected float getU0() {
        return this.stack != null && !this.stack.isEmpty() ? this.f_108321_.getU((double) ((this.uo + 1.0F) / 4.0F * 16.0F)) : super.getU0();
    }

    @Override
    protected float getU1() {
        return this.stack != null && !this.stack.isEmpty() ? this.f_108321_.getU((double) (this.uo / 4.0F * 16.0F)) : super.getU1();
    }

    @Override
    protected float getV0() {
        return this.stack != null && !this.stack.isEmpty() ? this.f_108321_.getV((double) (this.vo / 4.0F * 16.0F)) : super.getV0();
    }

    @Override
    protected float getV1() {
        return this.stack != null && !this.stack.isEmpty() ? this.f_108321_.getV((double) ((this.vo + 1.0F) / 4.0F * 16.0F)) : super.getV1();
    }

    private void generateBezierControlPoints() {
        Vector3 o1 = new Vector3((double) this.start.x, (double) this.start.y, (double) this.start.z);
        Vector3 midPoint = new Vector3((double) ((this.start.x + this.end.x) / 2.0F), (double) ((this.start.y + this.end.y) / 2.0F), (double) ((this.start.z + this.end.z) / 2.0F));
        midPoint = midPoint.sub(o1);
        midPoint = midPoint.rotateYaw((float) (Math.PI / 2));
        this.control_a = new Vector3((double) (this.start.x + (this.end.x - this.start.x) / 3.0F), (double) (this.start.y + (this.end.y - this.start.y) / 3.0F), (double) (this.start.z + (this.end.z - this.start.z) / 3.0F));
        this.control_b = new Vector3((double) (this.start.x + (this.end.x - this.start.x) / 3.0F * 2.0F), (double) (this.start.y + (this.end.y - this.start.y) / 3.0F * 2.0F), (double) (this.start.z + (this.end.z - this.start.z) / 3.0F * 2.0F));
        this.control_a = this.control_a.add(midPoint);
        this.control_b = this.control_b.add(midPoint);
    }

    protected void move() {
        if (this.movementType != null) {
            switch(this.movementType) {
                case BEZIER_POINT:
                    this.moveBezier();
                    break;
                case LERP_POINT:
                    this.moveLerp();
                    break;
                case VELOCITY:
                    this.moveVelocity();
                    break;
                case ORBIT:
                    this.moveOrbit();
                    break;
                case SPHERE_ORBIT:
                    this.moveSphereOrbit();
                case STATIONARY:
            }
        }
    }

    protected boolean apply_aging() {
        if ((float) (this.f_107224_++) >= (float) this.f_107225_ + this.life_padding) {
            this.m_107274_();
        }
        float agePct = MathUtils.clamp01((float) this.f_107224_ / (float) this.f_107225_);
        this.lerpAlpha(agePct);
        this.lerpColors(agePct);
        return this.f_107220_;
    }

    protected void lerpAlpha(float agePct) {
        float alpha_T = this.maxAlpha;
        if (agePct < 0.2F) {
            alpha_T = agePct / 0.2F * this.maxAlpha;
        } else if (agePct > 0.8F) {
            alpha_T = (1.0F - (agePct - 0.8F) / 0.2F) * this.maxAlpha;
        }
        this.m_107271_(alpha_T);
    }

    protected void lerpColors(float agePct) {
        if (this.colorTransitions != null && this.colorTransitions.size() >= 2) {
            int cSize = this.colorTransitions.size();
            int cIndex = (int) Math.floor((double) ((float) cSize * agePct)) % cSize;
            int nIndex = cIndex + 1;
            if (nIndex > cSize - 1) {
                nIndex = cSize - 1;
            }
            int colorAge = Math.max(this.f_107225_ / cSize, 1);
            float color_T = (float) (this.f_107224_ % colorAge) / (float) colorAge;
            Vector3 clr = Vector3.lerp((Vector3) this.colorTransitions.get(cIndex), (Vector3) this.colorTransitions.get(nIndex), color_T);
            this.m_107253_(clr.x / 255.0F, clr.y / 255.0F, clr.z / 255.0F);
        }
    }

    private void moveOrbit() {
        this.end.y += 0.01F;
        if (this.orbit_clockwise) {
            this.angle = this.angle + (double) this.end.x;
        } else {
            this.angle = this.angle - (double) this.end.x;
        }
        double expand = this.flare >= 0.0 ? this.flare : (double) this.end.y;
        double pX = (double) this.start.x + Math.cos(this.angle) * expand;
        double pZ = (double) this.start.z + Math.sin(this.angle) * expand;
        this.m_6257_(0.0, this.f_107216_, 0.0);
        this.m_107264_(pX, this.f_107213_, pZ);
    }

    private void moveSphereOrbit() {
        this.angle = this.angle + (double) this.end.x;
        float pX = (float) Math.cos(this.angle);
        float pZ = (float) Math.sin(this.angle);
        Vector3f horizPos = new Vector3f(pX, 0.0F, pZ);
        horizPos.rotate(Axis.XP.rotationDegrees(this.end.z + (float) this.f_107224_));
        horizPos.mul(this.end.y);
        this.m_107264_((double) (this.start.x + horizPos.x()), (double) (this.start.y + horizPos.y()), (double) (this.start.z + horizPos.z()));
    }

    private void moveVelocity() {
        this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
        if (this.decay_velocity) {
            if (this.f_107213_ == this.f_107210_) {
                this.f_107215_ *= 1.1;
                this.f_107217_ *= 1.1;
            }
            this.f_107215_ *= 0.96F;
            this.f_107216_ *= 0.96F;
            this.f_107217_ *= 0.96F;
            if (this.f_107218_) {
                this.f_107215_ *= 0.7F;
                this.f_107217_ *= 0.7F;
            }
        }
        if (this.f_107226_ > 0.0F) {
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
        }
    }

    private void moveLerp() {
        float agePct = (float) this.f_107224_ / (float) this.f_107225_;
        Vector3 pos = Vector3.lerp(this.start, this.end, agePct);
        this.m_107264_((double) pos.x, (double) pos.y, (double) pos.z);
    }

    private void moveBezier() {
        this.setPosition(Vector3.bezier(this.start, this.end, this.control_a, this.control_b, (float) this.f_107224_ / (float) this.f_107225_));
    }

    private void setPosition(Vector3 position) {
        this.m_107264_((double) position.x, (double) position.y, (double) position.z);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15728880;
    }

    public void setLifePadding(int padding) {
        this.life_padding = (float) padding;
    }

    @Nonnull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderTypes.ADDITIVE;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class FXParticleFactoryBase implements ParticleProvider<MAParticleType> {

        protected final SpriteSet spriteSet;

        public FXParticleFactoryBase(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public abstract Particle createParticle(MAParticleType var1, ClientLevel var2, double var3, double var5, double var7, double var9, double var11, double var13);

        protected void configureParticle(MAParticleBase particle, MAParticleType typeIn) {
            if (typeIn.getColor() != null) {
                if (typeIn.getColor().getColor() != -1) {
                    particle.colorTransitions.clear();
                    particle.m_107253_(typeIn.getColor().getRed(), typeIn.getColor().getGreen(), typeIn.getColor().getBlue());
                }
                if (typeIn.getColor().getAlpha() != -1.0F) {
                    particle.setMaxAlpha(typeIn.getColor().getAlpha());
                }
            }
            if (typeIn.getScale() != null) {
                particle.f_107663_ = typeIn.getScale().value();
            }
            if (typeIn.getLife() != null) {
                particle.f_107225_ = typeIn.getLife().value();
            }
            if (typeIn.getLifePadding() != null) {
                particle.setLifePadding(typeIn.getLife().value());
            }
            if (typeIn.getGravity() != null) {
                particle.f_107226_ = typeIn.getGravity().value();
            }
            if (typeIn.getPhysics() != null) {
                particle.f_107219_ = typeIn.getPhysics().value();
            }
            if (typeIn.getMover() != null) {
                typeIn.getMover().configureParticle(particle);
            }
            if (typeIn.getStack() != null) {
                particle.setRenderAsStackParticles(typeIn.getStack().value());
            }
        }
    }
}