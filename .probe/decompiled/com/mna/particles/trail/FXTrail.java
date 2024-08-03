package com.mna.particles.trail;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.FXMovementType;
import com.mna.particles.base.MAParticleBase;
import com.mna.particles.bolt.Segment;
import com.mna.particles.types.render.ParticleRenderTypes;
import com.mna.tools.math.Vector3;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FXTrail extends MAParticleBase {

    private ParticleTrail data;

    private Entity followEntity;

    private Vector3 origin;

    public FXTrail(ClientLevel worldIn, double startX, double startY, double startZ, double entityID, double randSeed, double maxAge, SpriteSet sprite) {
        super(worldIn, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        this.f_108321_ = sprite.get(this.f_107223_);
        this.data = new ParticleTrail(worldIn.f_46441_.nextLong());
        this.m_107264_(startX, startY, startZ);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.origin = new Vector3(startX, startY, startZ);
        this.f_107663_ = 0.04F;
        this.m_107250_(3.0F, 3.0F);
        this.f_107227_ = 0.2F;
        this.f_107228_ = 0.2F;
        this.f_107229_ = 0.8F;
    }

    private void setMoveFollowEntity(Level world, int entityID, long seed, int maxAge) {
        this.followEntity = world.getEntity(entityID);
        this.f_107225_ = maxAge;
        this.movementType = FXMovementType.STATIONARY;
        this.origin = null;
    }

    @Override
    public void tick() {
        if (this.followEntity == null) {
            super.tick();
        } else {
            this.moveFollowEntity();
        }
        double time = (double) ((float) this.f_107224_ / 4.0F);
        Vector3 prevPos = new Vector3(this.f_107209_, this.f_107210_, this.f_107211_);
        Vector3 curPos = new Vector3(this.f_107212_, this.f_107213_, this.f_107214_);
        curPos = curPos.add(new Vector3(Math.sin(time), Math.cos(time), Math.sin(time)).scale(0.2F));
        if (this.f_107224_ > 5) {
            this.data.tick(prevPos, curPos);
        }
    }

    private void moveFollowEntity() {
        if (this.f_107225_ == -1) {
            if (this.followEntity != null && this.followEntity.isAlive()) {
                this.f_107220_ = false;
            } else {
                this.m_107274_();
            }
        } else {
            this.f_107224_++;
            if (this.f_107224_ > this.f_107225_) {
                this.m_107274_();
            }
        }
        if (!this.f_107220_) {
            this.f_107209_ = this.followEntity.xo;
            this.f_107210_ = this.followEntity.yo;
            this.f_107211_ = this.followEntity.zo;
            this.m_107264_(this.followEntity.getX(), this.followEntity.getY(), this.followEntity.getZ());
        }
    }

    public boolean shouldCull() {
        return false;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 vec3d = renderInfo.getPosition();
        double lerpX = Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_);
        double lerpY = Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_);
        double lerpZ = Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_);
        float f = (float) (lerpX - vec3d.x());
        float f1 = (float) (lerpY - vec3d.y());
        float f2 = (float) (lerpZ - vec3d.z());
        Vector3 renderViewOffset = new Vector3((double) f, (double) f1, (double) f2);
        Vector3 particleOrigin = this.origin != null ? this.origin : new Vector3(this.f_107212_, this.f_107213_, this.f_107214_);
        if (this.origin != null) {
            Vector3 originDelta = new Vector3(lerpX, lerpY, lerpZ).sub(particleOrigin);
            particleOrigin = particleOrigin.add(originDelta);
        }
        int count = 0;
        Vector3 lastEnd1 = null;
        Vector3 lastEnd2 = null;
        for (Segment s : this.data.getSegments()) {
            float width = this.f_107663_;
            Vector3 start = s.getStart().sub(particleOrigin);
            Vector3 end = s.getEnd().sub(particleOrigin);
            Vector3 dir = end.sub(start).normalize().scale(5.0E-4F);
            Vector3[] avector3f = new Vector3[] { lastEnd1 == null ? start.add(new Vector3((double) (-width), 0.0, (double) (-width))) : lastEnd1.sub(dir), lastEnd2 == null ? start.add(new Vector3((double) (-width), 0.0, (double) width)) : lastEnd2.sub(dir), end.add(new Vector3((double) width, 0.0, (double) width)), end.add(new Vector3((double) width, 0.0, (double) (-width))) };
            lastEnd1 = avector3f[2];
            lastEnd2 = avector3f[3];
            for (int i = 0; i < 4; i++) {
                avector3f[i] = avector3f[i].add(renderViewOffset);
            }
            float startAlpha = this.f_107230_;
            if (count > this.data.getSegments().size() - 10) {
                startAlpha = 0.1F * (float) (this.data.getSegments().size() - count);
            }
            float minU = this.m_5970_();
            float maxU = this.m_5952_();
            float minV = this.m_5951_();
            float maxV = this.m_5950_();
            int j = 15728880;
            buffer.vertex((double) avector3f[3].x, (double) avector3f[3].y, (double) avector3f[3].z).uv(maxU, maxV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[2].x, (double) avector3f[2].y, (double) avector3f[2].z).uv(maxU, minV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[0].x, (double) avector3f[0].y, (double) avector3f[0].z).uv(minU, minV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[1].x, (double) avector3f[1].y, (double) avector3f[1].z).uv(minU, maxV).color(this.f_107227_, this.f_107228_, this.f_107229_, startAlpha).uv2(j).endVertex();
            buffer.vertex((double) avector3f[1].x, (double) avector3f[1].y, (double) avector3f[1].z).uv(maxU, maxV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[0].x, (double) avector3f[0].y, (double) avector3f[0].z).uv(maxU, minV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[2].x, (double) avector3f[2].y, (double) avector3f[2].z).uv(minU, minV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[3].x, (double) avector3f[3].y, (double) avector3f[3].z).uv(minU, maxV).color(this.f_107227_, this.f_107228_, this.f_107229_, startAlpha).uv2(j).endVertex();
            count++;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderTypes.ADDITIVE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXTrailBezierFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXTrailBezierFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXTrail particle = new FXTrail(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particle.setMoveBezier(x, y, z, xSpeed, ySpeed, zSpeed);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXTrailFollowEntityFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXTrailFollowEntityFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXTrail particle = new FXTrail(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particle.setMoveFollowEntity(worldIn, (int) xSpeed, (long) ySpeed, (int) zSpeed);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXTrailOrbitFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXTrailOrbitFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXTrail particle = new FXTrail(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particle.setMoveOrbit(x, y, z, xSpeed, ySpeed, zSpeed);
            particle.f_107225_ = 50;
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXTrailSphereOrbitFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXTrailSphereOrbitFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXTrail particle = new FXTrail(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particle.setMoveSphereOrbit(x, y, z, xSpeed, ySpeed, zSpeed);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXTrailVelocityFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXTrailVelocityFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXTrail particle = new FXTrail(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, false);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}