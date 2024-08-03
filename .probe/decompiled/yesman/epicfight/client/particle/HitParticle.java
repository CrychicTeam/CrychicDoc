package yesman.epicfight.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class HitParticle extends TextureSheetParticle {

    protected final SpriteSet animatedSprite;

    public HitParticle(ClientLevel world, double x, double y, double z, SpriteSet animatedSprite) {
        super(world, x, y, z);
        this.f_107227_ = 1.0F;
        this.f_107228_ = 1.0F;
        this.f_107229_ = 1.0F;
        this.animatedSprite = animatedSprite;
        this.m_108339_(animatedSprite);
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.m_108339_(this.animatedSprite);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.BLEND_LIGHTMAP_PARTICLE;
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }
}