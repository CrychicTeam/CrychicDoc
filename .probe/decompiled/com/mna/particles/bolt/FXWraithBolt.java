package com.mna.particles.bolt;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.types.render.ParticleRenderTypes;
import com.mna.tools.math.Vector3;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FXWraithBolt extends TextureSheetParticle {

    private LightningData data;

    public FXWraithBolt(ClientLevel worldIn, double startX, double startY, double startZ, double endX, double endY, double endZ, SpriteSet sprite) {
        super(worldIn, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        this.f_108321_ = sprite.get(this.f_107223_);
        this.data = new LightningData(new Vector3(startX, startY, startZ), new Vector3(endX, endY, endZ), worldIn.f_46441_.nextLong());
        this.data.setMaxAge(20);
        this.data.setSpeed(2);
        this.f_107225_ = this.data.getMaxAge();
        this.m_107264_(startX, startY, startZ);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107227_ = 0.75F;
        this.f_107228_ = 0.12F;
        this.f_107229_ = 0.06F;
        this.data.setMaxOffset(0.25F);
        this.data.fractalize(8);
        this.data.finalize();
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.data.onUpdate();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 vec3d = renderInfo.getPosition();
        float f = (float) (Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_) - vec3d.x());
        float f1 = (float) (Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_) - vec3d.y());
        float f2 = (float) (Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_) - vec3d.z());
        Vector3 posOffset = new Vector3((double) f, (double) f1, (double) f2);
        Vector3 particleOrigin = new Vector3(this.f_107212_, this.f_107213_, this.f_107214_);
        int count = 0;
        int maxIndex = (int) Math.ceil((double) (((float) this.data.getAge() + partialTicks) / (float) this.data.getMaxAge() * (float) this.data.numSegments()));
        Vector3 lastEnd1 = null;
        Vector3 lastEnd2 = null;
        if (this.data.getAge() == this.data.getMaxAge()) {
            int ticksRemaining = this.f_107225_ - this.f_107224_;
            this.f_107230_ = (float) ticksRemaining / 10.0F;
        }
        for (Segment s : this.data.getSegments()) {
            if (count > maxIndex) {
                break;
            }
            float width = 0.08F;
            Vector3 start = s.getStart().sub(particleOrigin);
            Vector3 end = s.getEnd().sub(particleOrigin);
            Vector3[] avector3f = new Vector3[] { lastEnd1 == null ? start.add(new Vector3((double) (-width), 0.0, (double) (-width))) : lastEnd1, lastEnd2 == null ? start.add(new Vector3((double) (-width), 0.0, (double) width)) : lastEnd2, end.add(new Vector3((double) width, 0.0, (double) width)), end.add(new Vector3((double) width, 0.0, (double) (-width))) };
            lastEnd1 = avector3f[2];
            lastEnd2 = avector3f[3];
            for (int i = 0; i < 4; i++) {
                avector3f[i] = avector3f[i].add(posOffset);
            }
            float minU = this.m_5970_();
            float maxU = this.m_5952_();
            float minV = this.m_5951_();
            float maxV = this.m_5950_();
            int j = 15728640;
            buffer.vertex((double) avector3f[3].x, (double) avector3f[3].y, (double) avector3f[3].z).uv(maxU, maxV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[2].x, (double) avector3f[2].y, (double) avector3f[2].z).uv(maxU, minV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[0].x, (double) avector3f[0].y, (double) avector3f[0].z).uv(minU, minV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[1].x, (double) avector3f[1].y, (double) avector3f[1].z).uv(minU, maxV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[3].x, (double) avector3f[3].y, (double) avector3f[3].z).uv(maxU, maxV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[2].x, (double) avector3f[2].y, (double) avector3f[2].z).uv(maxU, minV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[0].x, (double) avector3f[0].y, (double) avector3f[0].z).uv(minU, minV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            buffer.vertex((double) avector3f[1].x, (double) avector3f[1].y, (double) avector3f[1].z).uv(minU, maxV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
            count++;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderTypes.ADDITIVE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXWraithBoltFactory implements ParticleProvider<MAParticleType> {

        protected final SpriteSet spriteSet;

        public FXWraithBoltFactory(SpriteSet p_i50846_1_) {
            this.spriteSet = p_i50846_1_;
        }

        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FXWraithBolt(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}