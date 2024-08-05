package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustColorTransitionOptions;
import org.joml.Vector3f;

public class DustColorTransitionParticle extends DustParticleBase<DustColorTransitionOptions> {

    private final Vector3f fromColor;

    private final Vector3f toColor;

    protected DustColorTransitionParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, DustColorTransitionOptions dustColorTransitionOptions7, SpriteSet spriteSet8) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6, dustColorTransitionOptions7, spriteSet8);
        float $$9 = this.f_107223_.nextFloat() * 0.4F + 0.6F;
        this.fromColor = this.randomizeColor(dustColorTransitionOptions7.getFromColor(), $$9);
        this.toColor = this.randomizeColor(dustColorTransitionOptions7.getToColor(), $$9);
    }

    private Vector3f randomizeColor(Vector3f vectorF0, float float1) {
        return new Vector3f(this.m_172104_(vectorF0.x(), float1), this.m_172104_(vectorF0.y(), float1), this.m_172104_(vectorF0.z(), float1));
    }

    private void lerpColors(float float0) {
        float $$1 = ((float) this.f_107224_ + float0) / ((float) this.f_107225_ + 1.0F);
        Vector3f $$2 = new Vector3f(this.fromColor).lerp(this.toColor, $$1);
        this.f_107227_ = $$2.x();
        this.f_107228_ = $$2.y();
        this.f_107229_ = $$2.z();
    }

    @Override
    public void render(VertexConsumer vertexConsumer0, Camera camera1, float float2) {
        this.lerpColors(float2);
        super.m_5744_(vertexConsumer0, camera1, float2);
    }

    public static class Provider implements ParticleProvider<DustColorTransitionOptions> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(DustColorTransitionOptions dustColorTransitionOptions0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new DustColorTransitionParticle(clientLevel1, double2, double3, double4, double5, double6, double7, dustColorTransitionOptions0, this.sprites);
        }
    }
}