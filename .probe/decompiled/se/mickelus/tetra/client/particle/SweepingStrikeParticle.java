package se.mickelus.tetra.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@MethodsReturnNonnullByDefault
public class SweepingStrikeParticle extends TextureSheetParticle {

    @OnlyIn(Dist.CLIENT)
    ParticleRenderType renderType = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            RenderSystem.disableCull();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
        }

        public String toString() {
            return "PARTICLE_SHEET_LIT";
        }
    };

    private static final Vector3f ROTATION_VECTOR = new Vector3f(0.5F, 0.5F, 0.5F).normalize();

    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);

    private final boolean reverse;

    private final SpriteSet sprites;

    private final float pitch;

    private final float yaw;

    protected SweepingStrikeParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, int lifetime, boolean reverse, float pitch, float yaw) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        float shade = 0.6F + this.f_107223_.nextFloat() * 0.4F;
        this.f_107227_ = shade;
        this.f_107228_ = shade;
        this.f_107229_ = shade;
        this.f_107663_ = 1.0F;
        this.pitch = pitch / 180.0F * (float) Math.PI;
        this.yaw = yaw / 180.0F * (float) Math.PI;
        this.sprites = spriteSet;
        this.f_107225_ = lifetime;
        this.reverse = reverse;
        this.m_108339_(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return this.renderType;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        this.renderRotatedParticle(consumer, camera, partialTicks, quaternion -> {
            quaternion.mul(Axis.YP.rotation(-this.yaw));
            quaternion.mul(Axis.XP.rotation(this.pitch + (float) (Math.PI / 3)));
        });
    }

    private void renderRotatedParticle(VertexConsumer consumer, Camera camera, float partialTicks, Consumer<Quaternionf> transformApplier) {
        Vec3 vec3 = camera.getPosition();
        float x = (float) (this.f_107212_ - vec3.x());
        float y = (float) (this.f_107213_ - vec3.y());
        float z = (float) (this.f_107214_ - vec3.z());
        Quaternionf quaternion = new Quaternionf().setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
        transformApplier.accept(quaternion);
        quaternion.transform(TRANSFORM_VECTOR);
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float size = this.m_5902_(partialTicks);
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f = avector3f[i];
            quaternion.transform(vector3f);
            vector3f.mul(size);
            vector3f.add(x, y, z);
        }
        int light = this.m_6355_(partialTicks);
        this.makeCornerVertex(consumer, avector3f[0], this.getU1(), this.m_5950_(), light);
        this.makeCornerVertex(consumer, avector3f[1], this.getU1(), this.m_5951_(), light);
        this.makeCornerVertex(consumer, avector3f[2], this.getU0(), this.m_5951_(), light);
        this.makeCornerVertex(consumer, avector3f[3], this.getU0(), this.m_5950_(), light);
    }

    private void makeCornerVertex(VertexConsumer consumer, Vector3f pos, float u, float v, int light) {
        consumer.vertex((double) pos.x(), (double) pos.y(), (double) pos.z()).uv(u, v).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(light).endVertex();
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.m_108339_(this.sprites);
        }
    }

    @Override
    protected float getU0() {
        return this.reverse ? super.getU1() : super.getU0();
    }

    @Override
    protected float getU1() {
        return this.reverse ? super.getU0() : super.getU1();
    }

    @OnlyIn(Dist.CLIENT)
    @ParametersAreNonnullByDefault
    public static class Provider implements ParticleProvider<SweepingStrikeParticleOption> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SweepingStrikeParticleOption option, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new SweepingStrikeParticle(level, x, y, z, this.sprites, option.duration(), option.reverse(), option.pitch(), option.yaw());
        }
    }
}