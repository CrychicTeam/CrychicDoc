package com.github.alexmodguy.alexscaves.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Consumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RaygunBlastParticle extends TextureSheetParticle {

    private Direction direction;

    private float randomRot = 0.0F;

    protected RaygunBlastParticle(ClientLevel world, double x, double y, double z, Direction direction) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.direction = direction;
        this.f_107219_ = false;
        this.m_107250_(1.0F, 1.0F);
        this.m_107253_(1.0F, 1.0F, 1.0F);
        this.f_107225_ = world.f_46441_.nextInt(20) + 20;
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.randomRot = (float) ((Math.PI * 2) * (double) world.f_46441_.nextFloat());
        this.f_107663_ = 0.2F + world.f_46441_.nextFloat() * 0.4F;
        this.f_172258_ = 0.0F;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        float f = ((float) this.f_107224_ - (float) (this.f_107225_ / 2)) / (float) this.f_107225_;
        float f1 = (float) this.f_107224_ / (float) this.f_107225_;
        float f2 = 1.0F - 0.1F * f1;
        this.f_172258_ = 1.0F - 0.65F * f1;
        if (this.f_107224_ > this.f_107225_ / 2) {
            this.m_107271_(1.0F - f * 2.0F);
        }
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        BlockPos connectedTo = BlockPos.containing(this.f_107212_ + (double) ((float) this.direction.getStepX() * -0.1F), this.f_107213_ + (double) ((float) this.direction.getStepY() * -0.1F), this.f_107214_ + (double) ((float) this.direction.getStepZ() * -0.1F));
        BlockState state = this.f_107208_.m_8055_(connectedTo);
        if (this.f_107224_++ >= this.f_107225_ || state.m_60795_() || !state.m_60783_(this.f_107208_, connectedTo, this.direction.getOpposite())) {
            this.m_107274_();
        } else if (this.f_107223_.nextFloat() < 0.5F && this.f_107224_ < this.f_107225_ / 2) {
            this.f_107208_.addParticle(ParticleTypes.SMOKE.getType(), this.f_107212_, this.f_107213_, this.f_107214_, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        this.renderSignal(vertexConsumer, camera, partialTick, quaternionf -> {
            quaternionf.mul(this.direction.getRotation());
            quaternionf.rotateX((float) (-Math.PI / 2));
        });
        this.renderSignal(vertexConsumer, camera, partialTick, quaternionf -> {
            quaternionf.mul(this.direction.getRotation());
            quaternionf.rotateX((float) (Math.PI / 2));
        });
    }

    private void renderSignal(VertexConsumer consumer, Camera camera, float partialTicks, Consumer<Quaternionf> rots) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_) - vec3.z());
        Vector3f vector3f = new Vector3f(0.5F, 0.5F, 0.5F).normalize();
        Quaternionf quaternionf = new Quaternionf().setAngleAxis(0.0F, vector3f.x(), vector3f.y(), vector3f.z());
        rots.accept(quaternionf);
        quaternionf.rotateZ(this.randomRot);
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float f3 = this.m_5902_(partialTicks);
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f1 = avector3f[i];
            vector3f1.rotate(quaternionf);
            vector3f1.mul(f3);
            vector3f1.add(f, f1, f2);
        }
        float f6 = this.m_5970_();
        float f7 = this.m_5952_();
        float f4 = this.m_5951_();
        float f5 = this.m_5950_();
        int j = this.m_6355_(partialTicks);
        consumer.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).uv(f7, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        consumer.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).uv(f7, f4).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        consumer.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).uv(f6, f4).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
        consumer.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).uv(f6, f5).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(j).endVertex();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Direction direction = Direction.from3DDataValue((int) xSpeed);
            RaygunBlastParticle particle = new RaygunBlastParticle(worldIn, x, y, z, direction);
            particle.m_108335_(this.spriteSet);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class TremorzillaFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public TremorzillaFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Direction direction = Direction.from3DDataValue((int) xSpeed);
            RaygunBlastParticle particle = new RaygunBlastParticle(worldIn, x, y, z, direction);
            particle.m_108335_(this.spriteSet);
            particle.f_107663_ = 1.0F + worldIn.f_46441_.nextFloat() * 0.5F;
            particle.f_107225_ = 60 + worldIn.f_46441_.nextInt(20);
            return particle;
        }
    }
}