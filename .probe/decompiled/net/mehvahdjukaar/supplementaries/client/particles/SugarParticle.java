package net.mehvahdjukaar.supplementaries.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;

public class SugarParticle extends TerrainParticle {

    public static final ParticleRenderType TERRAIN_SHEET_OPAQUE = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(true);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
        }

        public String toString() {
            return "TERRAIN_SHEET_OPAQUE";
        }
    };

    public SugarParticle(ClientLevel clientLevel, double x, double y, double z, double speedX, double speedY, double speedZ) {
        super(clientLevel, x, y, z, speedX, speedY, speedZ, ((Block) ModRegistry.SUGAR_CUBE.get()).defaultBlockState());
        this.f_107225_ = (int) (40.0F / (this.f_107223_.nextFloat() * 0.7F + 0.3F));
        this.m_107253_(1.0F, 1.0F, 1.0F);
        this.f_107215_ *= 0.6;
        this.f_107217_ *= 0.6;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        BlockPos pos = BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_);
        FluidState fluid = this.f_107208_.m_6425_(pos);
        boolean wasTouchingWater = fluid.is(FluidTags.WATER);
        if (wasTouchingWater && Math.abs(this.f_107213_ - (double) pos.m_123342_() - (double) fluid.getOwnHeight()) < 0.01 && this.f_107208_.m_6425_(pos.above()).isEmpty()) {
            this.f_107226_ = 0.0F;
            this.f_107216_ = 0.0;
        } else {
            this.f_107226_ = wasTouchingWater ? -0.05F : 1.0F;
        }
        super.m_5989_();
        if (this.f_107226_ != 0.0F) {
            BlockPos pos2 = BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_);
            FluidState fluid2 = this.f_107208_.m_6425_(pos2);
            boolean isTouchingWater = fluid2.is(FluidTags.WATER);
            if (wasTouchingWater && !isTouchingWater) {
                this.m_107264_(this.f_107212_, (double) ((float) pos.m_123342_() + fluid.getHeight(this.f_107208_, pos)) - 0.005, this.f_107214_);
                this.f_107226_ = 0.0F;
            }
            if (!wasTouchingWater && isTouchingWater) {
                this.m_107264_(this.f_107212_, (double) ((float) pos2.m_123342_() + fluid2.getHeight(this.f_107208_, pos2)) - 0.005, this.f_107214_);
                this.f_107226_ = 0.0F;
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return TERRAIN_SHEET_OPAQUE;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Factory(SpriteSet set) {
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SugarParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}