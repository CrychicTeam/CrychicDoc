package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Consumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ShriekParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ShriekParticle extends TextureSheetParticle {

    private static final Vector3f ROTATION_VECTOR = new Vector3f(0.5F, 0.5F, 0.5F).normalize();

    private static final Vector3f TRANSFORM_VECTOR = new Vector3f(-1.0F, -1.0F, 0.0F);

    private static final float MAGICAL_X_ROT = 1.0472F;

    private int delay;

    ShriekParticle(ClientLevel clientLevel0, double double1, double double2, double double3, int int4) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.f_107663_ = 0.85F;
        this.delay = int4;
        this.f_107225_ = 30;
        this.f_107226_ = 0.0F;
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.1;
        this.f_107217_ = 0.0;
    }

    @Override
    public float getQuadSize(float float0) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + float0) / (float) this.f_107225_ * 0.75F, 0.0F, 1.0F);
    }

    @Override
    public void render(VertexConsumer vertexConsumer0, Camera camera1, float float2) {
        if (this.delay <= 0) {
            this.f_107230_ = 1.0F - Mth.clamp(((float) this.f_107224_ + float2) / (float) this.f_107225_, 0.0F, 1.0F);
            this.renderRotatedParticle(vertexConsumer0, camera1, float2, p_253347_ -> p_253347_.mul(new Quaternionf().rotationX(-1.0472F)));
            this.renderRotatedParticle(vertexConsumer0, camera1, float2, p_253346_ -> p_253346_.mul(new Quaternionf().rotationYXZ((float) -Math.PI, 1.0472F, 0.0F)));
        }
    }

    private void renderRotatedParticle(VertexConsumer vertexConsumer0, Camera camera1, float float2, Consumer<Quaternionf> consumerQuaternionf3) {
        Vec3 $$4 = camera1.getPosition();
        float $$5 = (float) (Mth.lerp((double) float2, this.f_107209_, this.f_107212_) - $$4.x());
        float $$6 = (float) (Mth.lerp((double) float2, this.f_107210_, this.f_107213_) - $$4.y());
        float $$7 = (float) (Mth.lerp((double) float2, this.f_107211_, this.f_107214_) - $$4.z());
        Quaternionf $$8 = new Quaternionf().setAngleAxis(0.0F, ROTATION_VECTOR.x(), ROTATION_VECTOR.y(), ROTATION_VECTOR.z());
        consumerQuaternionf3.accept($$8);
        $$8.transform(TRANSFORM_VECTOR);
        Vector3f[] $$9 = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float $$10 = this.getQuadSize(float2);
        for (int $$11 = 0; $$11 < 4; $$11++) {
            Vector3f $$12 = $$9[$$11];
            $$12.rotate($$8);
            $$12.mul($$10);
            $$12.add($$5, $$6, $$7);
        }
        int $$13 = this.getLightColor(float2);
        this.makeCornerVertex(vertexConsumer0, $$9[0], this.m_5952_(), this.m_5950_(), $$13);
        this.makeCornerVertex(vertexConsumer0, $$9[1], this.m_5952_(), this.m_5951_(), $$13);
        this.makeCornerVertex(vertexConsumer0, $$9[2], this.m_5970_(), this.m_5951_(), $$13);
        this.makeCornerVertex(vertexConsumer0, $$9[3], this.m_5970_(), this.m_5950_(), $$13);
    }

    private void makeCornerVertex(VertexConsumer vertexConsumer0, Vector3f vectorF1, float float2, float float3, int int4) {
        vertexConsumer0.vertex((double) vectorF1.x(), (double) vectorF1.y(), (double) vectorF1.z()).uv(float2, float3).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(int4).endVertex();
    }

    @Override
    public int getLightColor(float float0) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        if (this.delay > 0) {
            this.delay--;
        } else {
            super.m_5989_();
        }
    }

    public static class Provider implements ParticleProvider<ShriekParticleOption> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(ShriekParticleOption shriekParticleOption0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            ShriekParticle $$8 = new ShriekParticle(clientLevel1, double2, double3, double4, shriekParticleOption0.getDelay());
            $$8.m_108335_(this.sprite);
            $$8.m_107271_(1.0F);
            return $$8;
        }
    }
}