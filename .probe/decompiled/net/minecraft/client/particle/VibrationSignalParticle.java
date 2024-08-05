package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class VibrationSignalParticle extends TextureSheetParticle {

    private final PositionSource target;

    private float rot;

    private float rotO;

    private float pitch;

    private float pitchO;

    VibrationSignalParticle(ClientLevel clientLevel0, double double1, double double2, double double3, PositionSource positionSource4, int int5) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.f_107663_ = 0.3F;
        this.target = positionSource4;
        this.f_107225_ = int5;
        Optional<Vec3> $$6 = positionSource4.getPosition(clientLevel0);
        if ($$6.isPresent()) {
            Vec3 $$7 = (Vec3) $$6.get();
            double $$8 = double1 - $$7.x();
            double $$9 = double2 - $$7.y();
            double $$10 = double3 - $$7.z();
            this.rotO = this.rot = (float) Mth.atan2($$8, $$10);
            this.pitchO = this.pitch = (float) Mth.atan2($$9, Math.sqrt($$8 * $$8 + $$10 * $$10));
        }
    }

    @Override
    public void render(VertexConsumer vertexConsumer0, Camera camera1, float float2) {
        float $$3 = Mth.sin(((float) this.f_107224_ + float2 - (float) (Math.PI * 2)) * 0.05F) * 2.0F;
        float $$4 = Mth.lerp(float2, this.rotO, this.rot);
        float $$5 = Mth.lerp(float2, this.pitchO, this.pitch) + (float) (Math.PI / 2);
        this.renderSignal(vertexConsumer0, camera1, float2, p_253355_ -> p_253355_.rotateY($$4).rotateX(-$$5).rotateY($$3));
        this.renderSignal(vertexConsumer0, camera1, float2, p_253351_ -> p_253351_.rotateY((float) -Math.PI + $$4).rotateX($$5).rotateY($$3));
    }

    private void renderSignal(VertexConsumer vertexConsumer0, Camera camera1, float float2, Consumer<Quaternionf> consumerQuaternionf3) {
        Vec3 $$4 = camera1.getPosition();
        float $$5 = (float) (Mth.lerp((double) float2, this.f_107209_, this.f_107212_) - $$4.x());
        float $$6 = (float) (Mth.lerp((double) float2, this.f_107210_, this.f_107213_) - $$4.y());
        float $$7 = (float) (Mth.lerp((double) float2, this.f_107211_, this.f_107214_) - $$4.z());
        Vector3f $$8 = new Vector3f(0.5F, 0.5F, 0.5F).normalize();
        Quaternionf $$9 = new Quaternionf().setAngleAxis(0.0F, $$8.x(), $$8.y(), $$8.z());
        consumerQuaternionf3.accept($$9);
        Vector3f[] $$10 = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float $$11 = this.m_5902_(float2);
        for (int $$12 = 0; $$12 < 4; $$12++) {
            Vector3f $$13 = $$10[$$12];
            $$13.rotate($$9);
            $$13.mul($$11);
            $$13.add($$5, $$6, $$7);
        }
        float $$14 = this.m_5970_();
        float $$15 = this.m_5952_();
        float $$16 = this.m_5951_();
        float $$17 = this.m_5950_();
        int $$18 = this.getLightColor(float2);
        vertexConsumer0.vertex((double) $$10[0].x(), (double) $$10[0].y(), (double) $$10[0].z()).uv($$15, $$17).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2($$18).endVertex();
        vertexConsumer0.vertex((double) $$10[1].x(), (double) $$10[1].y(), (double) $$10[1].z()).uv($$15, $$16).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2($$18).endVertex();
        vertexConsumer0.vertex((double) $$10[2].x(), (double) $$10[2].y(), (double) $$10[2].z()).uv($$14, $$16).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2($$18).endVertex();
        vertexConsumer0.vertex((double) $$10[3].x(), (double) $$10[3].y(), (double) $$10[3].z()).uv($$14, $$17).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2($$18).endVertex();
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
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            Optional<Vec3> $$0 = this.target.getPosition(this.f_107208_);
            if ($$0.isEmpty()) {
                this.m_107274_();
            } else {
                int $$1 = this.f_107225_ - this.f_107224_;
                double $$2 = 1.0 / (double) $$1;
                Vec3 $$3 = (Vec3) $$0.get();
                this.f_107212_ = Mth.lerp($$2, this.f_107212_, $$3.x());
                this.f_107213_ = Mth.lerp($$2, this.f_107213_, $$3.y());
                this.f_107214_ = Mth.lerp($$2, this.f_107214_, $$3.z());
                double $$4 = this.f_107212_ - $$3.x();
                double $$5 = this.f_107213_ - $$3.y();
                double $$6 = this.f_107214_ - $$3.z();
                this.rotO = this.rot;
                this.rot = (float) Mth.atan2($$4, $$6);
                this.pitchO = this.pitch;
                this.pitch = (float) Mth.atan2($$5, Math.sqrt($$4 * $$4 + $$6 * $$6));
            }
        }
    }

    public static class Provider implements ParticleProvider<VibrationParticleOption> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(VibrationParticleOption vibrationParticleOption0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            VibrationSignalParticle $$8 = new VibrationSignalParticle(clientLevel1, double2, double3, double4, vibrationParticleOption0.getDestination(), vibrationParticleOption0.getArrivalInTicks());
            $$8.m_108335_(this.sprite);
            $$8.m_107271_(1.0F);
            return $$8;
        }
    }
}