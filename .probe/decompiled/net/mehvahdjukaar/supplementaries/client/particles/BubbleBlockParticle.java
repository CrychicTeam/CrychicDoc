package net.mehvahdjukaar.supplementaries.client.particles;

import java.util.Random;
import net.mehvahdjukaar.supplementaries.client.renderers.color.ColorHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class BubbleBlockParticle extends TextureSheetParticle {

    private static final Random RANDOM = new Random();

    protected final SpriteSet sprites;

    private final float colorRange;

    private final float startingColorInd;

    public BubbleBlockParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, 0.0, 0.0, 0.0);
        this.f_107215_ = pXSpeed;
        this.f_107216_ = pYSpeed;
        this.f_107217_ = pZSpeed;
        this.sprites = pSprites;
        this.f_107225_ = (int) rand(32.0, 0.85);
        this.colorRange = 0.325F + RANDOM.nextFloat() * 0.5F;
        this.startingColorInd = RANDOM.nextFloat();
        this.m_108339_(this.sprites);
        this.setColorForAge();
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.updateSprite();
        this.m_107271_(Mth.lerp(0.05F, this.f_107230_, 1.0F));
    }

    public void updateSprite() {
        this.m_108339_(this.sprites);
    }

    protected static double rand(double min, double variation) {
        return min / ((double) RANDOM.nextFloat() * variation + (1.0 - variation));
    }

    public void setColorForAge() {
        float age = (float) this.f_107224_ / (float) this.f_107225_;
        float a = (age * this.colorRange + this.startingColorInd + 1.0F) % 1.0F;
        float[] color = ColorHelper.getBubbleColor(a);
        this.f_107227_ = color[0];
        this.f_107228_ = color[1];
        this.f_107229_ = color[2];
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float age) {
        return 0.825F;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Factory(SpriteSet pSprites) {
            this.sprite = pSprites;
        }

        @Nullable
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BubbleBlockParticle p = new BubbleBlockParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprite);
            p.f_107226_ = 0.0F;
            p.f_107225_ = 4;
            p.f_107219_ = false;
            return p;
        }
    }
}