package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FireworkRocketItem;

public class FireworkParticles {

    public static class FlashProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public FlashProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            FireworkParticles.OverlayParticle $$8 = new FireworkParticles.OverlayParticle(clientLevel1, double2, double3, double4);
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }

    public static class OverlayParticle extends TextureSheetParticle {

        OverlayParticle(ClientLevel clientLevel0, double double1, double double2, double double3) {
            super(clientLevel0, double1, double2, double3);
            this.f_107225_ = 4;
        }

        @Override
        public ParticleRenderType getRenderType() {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }

        @Override
        public void render(VertexConsumer vertexConsumer0, Camera camera1, float float2) {
            this.m_107271_(0.6F - ((float) this.f_107224_ + float2 - 1.0F) * 0.25F * 0.5F);
            super.m_5744_(vertexConsumer0, camera1, float2);
        }

        @Override
        public float getQuadSize(float float0) {
            return 7.1F * Mth.sin(((float) this.f_107224_ + float0 - 1.0F) * 0.25F * (float) Math.PI);
        }
    }

    static class SparkParticle extends SimpleAnimatedParticle {

        private boolean trail;

        private boolean flicker;

        private final ParticleEngine engine;

        private float fadeR;

        private float fadeG;

        private float fadeB;

        private boolean hasFade;

        SparkParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, ParticleEngine particleEngine7, SpriteSet spriteSet8) {
            super(clientLevel0, double1, double2, double3, spriteSet8, 0.1F);
            this.f_107215_ = double4;
            this.f_107216_ = double5;
            this.f_107217_ = double6;
            this.engine = particleEngine7;
            this.f_107663_ *= 0.75F;
            this.f_107225_ = 48 + this.f_107223_.nextInt(12);
            this.m_108339_(spriteSet8);
        }

        public void setTrail(boolean boolean0) {
            this.trail = boolean0;
        }

        public void setFlicker(boolean boolean0) {
            this.flicker = boolean0;
        }

        @Override
        public void render(VertexConsumer vertexConsumer0, Camera camera1, float float2) {
            if (!this.flicker || this.f_107224_ < this.f_107225_ / 3 || (this.f_107224_ + this.f_107225_) / 3 % 2 == 0) {
                super.m_5744_(vertexConsumer0, camera1, float2);
            }
        }

        @Override
        public void tick() {
            super.tick();
            if (this.trail && this.f_107224_ < this.f_107225_ / 2 && (this.f_107224_ + this.f_107225_) % 2 == 0) {
                FireworkParticles.SparkParticle $$0 = new FireworkParticles.SparkParticle(this.f_107208_, this.f_107212_, this.f_107213_, this.f_107214_, 0.0, 0.0, 0.0, this.engine, this.f_107644_);
                $$0.m_107271_(0.99F);
                $$0.m_107253_(this.f_107227_, this.f_107228_, this.f_107229_);
                $$0.f_107224_ = $$0.f_107225_ / 2;
                if (this.hasFade) {
                    $$0.hasFade = true;
                    $$0.fadeR = this.fadeR;
                    $$0.fadeG = this.fadeG;
                    $$0.fadeB = this.fadeB;
                }
                $$0.flicker = this.flicker;
                this.engine.add($$0);
            }
        }
    }

    public static class SparkProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public SparkProvider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            FireworkParticles.SparkParticle $$8 = new FireworkParticles.SparkParticle(clientLevel1, double2, double3, double4, double5, double6, double7, Minecraft.getInstance().particleEngine, this.sprites);
            $$8.m_107271_(0.99F);
            return $$8;
        }
    }

    public static class Starter extends NoRenderParticle {

        private int life;

        private final ParticleEngine engine;

        private ListTag explosions;

        private boolean twinkleDelay;

        public Starter(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, ParticleEngine particleEngine7, @Nullable CompoundTag compoundTag8) {
            super(clientLevel0, double1, double2, double3);
            this.f_107215_ = double4;
            this.f_107216_ = double5;
            this.f_107217_ = double6;
            this.engine = particleEngine7;
            this.f_107225_ = 8;
            if (compoundTag8 != null) {
                this.explosions = compoundTag8.getList("Explosions", 10);
                if (this.explosions.isEmpty()) {
                    this.explosions = null;
                } else {
                    this.f_107225_ = this.explosions.size() * 2 - 1;
                    for (int $$9 = 0; $$9 < this.explosions.size(); $$9++) {
                        CompoundTag $$10 = this.explosions.getCompound($$9);
                        if ($$10.getBoolean("Flicker")) {
                            this.twinkleDelay = true;
                            this.f_107225_ += 15;
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public void tick() {
            if (this.life == 0 && this.explosions != null) {
                boolean $$0 = this.isFarAwayFromCamera();
                boolean $$1 = false;
                if (this.explosions.size() >= 3) {
                    $$1 = true;
                } else {
                    for (int $$2 = 0; $$2 < this.explosions.size(); $$2++) {
                        CompoundTag $$3 = this.explosions.getCompound($$2);
                        if (FireworkRocketItem.Shape.byId($$3.getByte("Type")) == FireworkRocketItem.Shape.LARGE_BALL) {
                            $$1 = true;
                            break;
                        }
                    }
                }
                SoundEvent $$4;
                if ($$1) {
                    $$4 = $$0 ? SoundEvents.FIREWORK_ROCKET_LARGE_BLAST_FAR : SoundEvents.FIREWORK_ROCKET_LARGE_BLAST;
                } else {
                    $$4 = $$0 ? SoundEvents.FIREWORK_ROCKET_BLAST_FAR : SoundEvents.FIREWORK_ROCKET_BLAST;
                }
                this.f_107208_.playLocalSound(this.f_107212_, this.f_107213_, this.f_107214_, $$4, SoundSource.AMBIENT, 20.0F, 0.95F + this.f_107223_.nextFloat() * 0.1F, true);
            }
            if (this.life % 2 == 0 && this.explosions != null && this.life / 2 < this.explosions.size()) {
                int $$6 = this.life / 2;
                CompoundTag $$7 = this.explosions.getCompound($$6);
                FireworkRocketItem.Shape $$8 = FireworkRocketItem.Shape.byId($$7.getByte("Type"));
                boolean $$9 = $$7.getBoolean("Trail");
                boolean $$10 = $$7.getBoolean("Flicker");
                int[] $$11 = $$7.getIntArray("Colors");
                int[] $$12 = $$7.getIntArray("FadeColors");
                if ($$11.length == 0) {
                    $$11 = new int[] { DyeColor.BLACK.getFireworkColor() };
                }
                switch($$8) {
                    case SMALL_BALL:
                    default:
                        this.createParticleBall(0.25, 2, $$11, $$12, $$9, $$10);
                        break;
                    case LARGE_BALL:
                        this.createParticleBall(0.5, 4, $$11, $$12, $$9, $$10);
                        break;
                    case STAR:
                        this.createParticleShape(0.5, new double[][] { { 0.0, 1.0 }, { 0.3455, 0.309 }, { 0.9511, 0.309 }, { 0.3795918367346939, -0.12653061224489795 }, { 0.6122448979591837, -0.8040816326530612 }, { 0.0, -0.35918367346938773 } }, $$11, $$12, $$9, $$10, false);
                        break;
                    case CREEPER:
                        this.createParticleShape(0.5, new double[][] { { 0.0, 0.2 }, { 0.2, 0.2 }, { 0.2, 0.6 }, { 0.6, 0.6 }, { 0.6, 0.2 }, { 0.2, 0.2 }, { 0.2, 0.0 }, { 0.4, 0.0 }, { 0.4, -0.6 }, { 0.2, -0.6 }, { 0.2, -0.4 }, { 0.0, -0.4 } }, $$11, $$12, $$9, $$10, true);
                        break;
                    case BURST:
                        this.createParticleBurst($$11, $$12, $$9, $$10);
                }
                int $$13 = $$11[0];
                float $$14 = (float) (($$13 & 0xFF0000) >> 16) / 255.0F;
                float $$15 = (float) (($$13 & 0xFF00) >> 8) / 255.0F;
                float $$16 = (float) (($$13 & 0xFF) >> 0) / 255.0F;
                Particle $$17 = this.engine.createParticle(ParticleTypes.FLASH, this.f_107212_, this.f_107213_, this.f_107214_, 0.0, 0.0, 0.0);
                $$17.setColor($$14, $$15, $$16);
            }
            this.life++;
            if (this.life > this.f_107225_) {
                if (this.twinkleDelay) {
                    boolean $$18 = this.isFarAwayFromCamera();
                    SoundEvent $$19 = $$18 ? SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR : SoundEvents.FIREWORK_ROCKET_TWINKLE;
                    this.f_107208_.playLocalSound(this.f_107212_, this.f_107213_, this.f_107214_, $$19, SoundSource.AMBIENT, 20.0F, 0.9F + this.f_107223_.nextFloat() * 0.15F, true);
                }
                this.m_107274_();
            }
        }

        private boolean isFarAwayFromCamera() {
            Minecraft $$0 = Minecraft.getInstance();
            return $$0.gameRenderer.getMainCamera().getPosition().distanceToSqr(this.f_107212_, this.f_107213_, this.f_107214_) >= 256.0;
        }

        private void createParticle(double double0, double double1, double double2, double double3, double double4, double double5, int[] int6, int[] int7, boolean boolean8, boolean boolean9) {
            FireworkParticles.SparkParticle $$10 = (FireworkParticles.SparkParticle) this.engine.createParticle(ParticleTypes.FIREWORK, double0, double1, double2, double3, double4, double5);
            $$10.setTrail(boolean8);
            $$10.setFlicker(boolean9);
            $$10.m_107271_(0.99F);
            int $$11 = this.f_107223_.nextInt(int6.length);
            $$10.m_107657_(int6[$$11]);
            if (int7.length > 0) {
                $$10.m_107659_(Util.getRandom(int7, this.f_107223_));
            }
        }

        private void createParticleBall(double double0, int int1, int[] int2, int[] int3, boolean boolean4, boolean boolean5) {
            double $$6 = this.f_107212_;
            double $$7 = this.f_107213_;
            double $$8 = this.f_107214_;
            for (int $$9 = -int1; $$9 <= int1; $$9++) {
                for (int $$10 = -int1; $$10 <= int1; $$10++) {
                    for (int $$11 = -int1; $$11 <= int1; $$11++) {
                        double $$12 = (double) $$10 + (this.f_107223_.nextDouble() - this.f_107223_.nextDouble()) * 0.5;
                        double $$13 = (double) $$9 + (this.f_107223_.nextDouble() - this.f_107223_.nextDouble()) * 0.5;
                        double $$14 = (double) $$11 + (this.f_107223_.nextDouble() - this.f_107223_.nextDouble()) * 0.5;
                        double $$15 = Math.sqrt($$12 * $$12 + $$13 * $$13 + $$14 * $$14) / double0 + this.f_107223_.nextGaussian() * 0.05;
                        this.createParticle($$6, $$7, $$8, $$12 / $$15, $$13 / $$15, $$14 / $$15, int2, int3, boolean4, boolean5);
                        if ($$9 != -int1 && $$9 != int1 && $$10 != -int1 && $$10 != int1) {
                            $$11 += int1 * 2 - 1;
                        }
                    }
                }
            }
        }

        private void createParticleShape(double double0, double[][] double1, int[] int2, int[] int3, boolean boolean4, boolean boolean5, boolean boolean6) {
            double $$7 = double1[0][0];
            double $$8 = double1[0][1];
            this.createParticle(this.f_107212_, this.f_107213_, this.f_107214_, $$7 * double0, $$8 * double0, 0.0, int2, int3, boolean4, boolean5);
            float $$9 = this.f_107223_.nextFloat() * (float) Math.PI;
            double $$10 = boolean6 ? 0.034 : 0.34;
            for (int $$11 = 0; $$11 < 3; $$11++) {
                double $$12 = (double) $$9 + (double) ((float) $$11 * (float) Math.PI) * $$10;
                double $$13 = $$7;
                double $$14 = $$8;
                for (int $$15 = 1; $$15 < double1.length; $$15++) {
                    double $$16 = double1[$$15][0];
                    double $$17 = double1[$$15][1];
                    for (double $$18 = 0.25; $$18 <= 1.0; $$18 += 0.25) {
                        double $$19 = Mth.lerp($$18, $$13, $$16) * double0;
                        double $$20 = Mth.lerp($$18, $$14, $$17) * double0;
                        double $$21 = $$19 * Math.sin($$12);
                        $$19 *= Math.cos($$12);
                        for (double $$22 = -1.0; $$22 <= 1.0; $$22 += 2.0) {
                            this.createParticle(this.f_107212_, this.f_107213_, this.f_107214_, $$19 * $$22, $$20, $$21 * $$22, int2, int3, boolean4, boolean5);
                        }
                    }
                    $$13 = $$16;
                    $$14 = $$17;
                }
            }
        }

        private void createParticleBurst(int[] int0, int[] int1, boolean boolean2, boolean boolean3) {
            double $$4 = this.f_107223_.nextGaussian() * 0.05;
            double $$5 = this.f_107223_.nextGaussian() * 0.05;
            for (int $$6 = 0; $$6 < 70; $$6++) {
                double $$7 = this.f_107215_ * 0.5 + this.f_107223_.nextGaussian() * 0.15 + $$4;
                double $$8 = this.f_107217_ * 0.5 + this.f_107223_.nextGaussian() * 0.15 + $$5;
                double $$9 = this.f_107216_ * 0.5 + this.f_107223_.nextDouble() * 0.5;
                this.createParticle(this.f_107212_, this.f_107213_, this.f_107214_, $$7, $$9, $$8, int0, int1, boolean2, boolean3);
            }
        }
    }
}