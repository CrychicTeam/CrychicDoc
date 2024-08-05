package com.github.alexmodguy.alexscaves.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class WaterTremorParticle extends SimpleAnimatedParticle {

    protected WaterTremorParticle(ClientLevel level, double x, double y, double z, SpriteSet set) {
        super(level, x, y, z, set, 0.0F);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107230_ = 0.8F;
        this.f_107226_ = 0.0F;
        this.f_107663_ = (float) ((Math.random() * 0.25 + 0.75) * 0.5);
        this.f_107225_ = 20;
        this.m_107657_(BiomeColors.getAverageWaterColor(level, BlockPos.containing(x, y, z)));
        this.m_108339_(set);
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos slightlyAbove = BlockPos.containing(this.f_107212_, this.f_107213_ + 0.1F, this.f_107214_);
        BlockPos slightlyBelow = BlockPos.containing(this.f_107212_, this.f_107213_ - 0.1F, this.f_107214_);
        if (!this.isWater(slightlyAbove) && !this.isWater(slightlyBelow)) {
            this.m_107274_();
        }
    }

    private boolean isWater(BlockPos pos) {
        BlockState state = this.f_107208_.m_8055_(pos);
        return state.m_60713_(Blocks.WATER_CAULDRON) ? true : state.m_60819_().is(FluidTags.WATER);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) partialTick, this.f_107209_, this.f_107212_) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTick, this.f_107210_, this.f_107213_) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTick, this.f_107211_, this.f_107214_) - vec3.z());
        Quaternionf quaternion = Axis.XP.rotationDegrees(90.0F);
        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.rotate(quaternion);
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float f4 = this.m_5902_(partialTick);
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f = avector3f[i];
            vector3f.rotate(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = this.m_5970_();
        float f8 = this.m_5952_();
        float f5 = this.m_5951_();
        float f6 = this.m_5950_();
        int j = this.m_6355_(partialTick);
        vertexConsumer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f8, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        vertexConsumer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f8, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        vertexConsumer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f7, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        vertexConsumer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f7, f6).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new WaterTremorParticle(worldIn, x, y, z, this.spriteSet);
        }
    }
}