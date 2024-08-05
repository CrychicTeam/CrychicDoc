package net.mehvahdjukaar.amendments.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.FastColor;

public class BoilingParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    private final TextureAtlasSprite shineSprite;

    private final double waterLevel;

    private final double growFactor;

    private final int lightLevel;

    private int maxTry = 200;

    BoilingParticle(ClientLevel level, double x, double y, double z, double waterLevel, int lightLevel, SpriteSet sprites) {
        super(level, x, y, z);
        this.m_107250_(0.0625F, 0.0625F);
        this.f_107663_ = this.f_107663_ * (this.f_107223_.nextFloat() * 0.4F + 0.16F);
        this.f_107215_ = ((double) level.f_46441_.nextFloat() * 2.0 - 1.0) * 0.005;
        this.f_107216_ = ((double) level.f_46441_.nextFloat() * 2.0 - 1.0) * 0.005;
        this.f_107217_ = ((double) level.f_46441_.nextFloat() * 2.0 - 1.0) * 0.005;
        this.f_107225_ = (int) MthUtils.nextWeighted(level.f_46441_, 26.0F, 1.0F, 5.0F);
        this.sprites = sprites;
        this.setSpriteFromAge(this.sprites);
        this.shineSprite = sprites.get(0, 6);
        this.waterLevel = waterLevel;
        this.growFactor = 0.002 + (double) level.f_46441_.nextFloat() * 0.004;
        this.lightLevel = lightLevel;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.maxTry-- <= 0) {
            this.m_107274_();
        } else {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            if (this.f_107213_ < this.waterLevel) {
                this.f_107216_ += 0.002;
            } else {
                this.f_107213_ = this.waterLevel;
                this.f_107663_ = (float) ((double) this.f_107663_ + this.growFactor);
                this.f_107216_ = 0.0;
                if (this.f_107225_-- <= 0) {
                    this.m_107274_();
                    return;
                }
            }
            this.f_107216_ *= 0.995;
            this.f_107215_ *= 0.98;
            this.f_107217_ *= 0.98;
            if (!this.f_107208_.m_8055_(BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_)).m_204336_(BlockTags.CAULDRONS)) {
                this.m_107274_();
            }
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (this.f_107225_ - this.f_107224_ >= 4) {
            TextureAtlasSprite old = this.f_108321_;
            float oldRed = this.f_107227_;
            float oldGreen = this.f_107228_;
            float oldBlue = this.f_107229_;
            this.m_107253_(1.0F, 1.0F, 1.0F);
            this.m_108337_(this.shineSprite);
            super.m_5744_(buffer, renderInfo, partialTicks);
            this.m_107253_(oldRed, oldGreen, oldBlue);
            this.m_108337_(old);
        }
        super.m_5744_(buffer, renderInfo, partialTicks);
    }

    @Override
    protected int getLightColor(float partialTick) {
        int lightColor = super.m_6355_(partialTick);
        int sky = LightTexture.sky(lightColor);
        int block = LightTexture.block(lightColor);
        return LightTexture.pack(Math.max(this.lightLevel, block), sky);
    }

    @Override
    public void setSpriteFromAge(SpriteSet sprite) {
        if (!this.f_107220_) {
            int newAge = Math.max(1, 5 - (this.f_107225_ - this.f_107224_));
            this.m_108337_(sprite.get(newAge, 5));
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double endY, double z, double color, double startY, double light) {
            int intColor = (int) color;
            float r = (float) FastColor.ARGB32.red(intColor) / 255.0F;
            float g = (float) FastColor.ARGB32.green(intColor) / 255.0F;
            float b = (float) FastColor.ARGB32.blue(intColor) / 255.0F;
            BoilingParticle particle = new BoilingParticle(level, x, startY, z, endY, (int) light, this.sprite);
            particle.m_107253_(r, g, b);
            return particle;
        }
    }
}