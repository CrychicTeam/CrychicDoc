package net.minecraft.client.particle;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class FallingDustParticle extends TextureSheetParticle {

    private final float rotSpeed;

    private final SpriteSet sprites;

    FallingDustParticle(ClientLevel clientLevel0, double double1, double double2, double double3, float float4, float float5, float float6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3);
        this.sprites = spriteSet7;
        this.f_107227_ = float4;
        this.f_107228_ = float5;
        this.f_107229_ = float6;
        float $$8 = 0.9F;
        this.f_107663_ *= 0.67499995F;
        int $$9 = (int) (32.0 / (Math.random() * 0.8 + 0.2));
        this.f_107225_ = (int) Math.max((float) $$9 * 0.9F, 1.0F);
        this.m_108339_(spriteSet7);
        this.rotSpeed = ((float) Math.random() - 0.5F) * 0.1F;
        this.f_107231_ = (float) Math.random() * (float) (Math.PI * 2);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getQuadSize(float float0) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + float0) / (float) this.f_107225_ * 32.0F, 0.0F, 1.0F);
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
            this.f_107204_ = this.f_107231_;
            this.f_107231_ = this.f_107231_ + (float) Math.PI * this.rotSpeed * 2.0F;
            if (this.f_107218_) {
                this.f_107204_ = this.f_107231_ = 0.0F;
            }
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107216_ -= 0.003F;
            this.f_107216_ = Math.max(this.f_107216_, -0.14F);
        }
    }

    public static class Provider implements ParticleProvider<BlockParticleOption> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        @Nullable
        public Particle createParticle(BlockParticleOption blockParticleOption0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            BlockState $$8 = blockParticleOption0.getState();
            if (!$$8.m_60795_() && $$8.m_60799_() == RenderShape.INVISIBLE) {
                return null;
            } else {
                BlockPos $$9 = BlockPos.containing(double2, double3, double4);
                int $$10 = Minecraft.getInstance().getBlockColors().getColor($$8, clientLevel1, $$9);
                if ($$8.m_60734_() instanceof FallingBlock) {
                    $$10 = ((FallingBlock) $$8.m_60734_()).getDustColor($$8, clientLevel1, $$9);
                }
                float $$11 = (float) ($$10 >> 16 & 0xFF) / 255.0F;
                float $$12 = (float) ($$10 >> 8 & 0xFF) / 255.0F;
                float $$13 = (float) ($$10 & 0xFF) / 255.0F;
                return new FallingDustParticle(clientLevel1, double2, double3, double4, $$11, $$12, $$13, this.sprite);
            }
        }
    }
}