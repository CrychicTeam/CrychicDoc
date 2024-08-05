package team.lodestar.lodestone.systems.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.awt.Color;
import java.util.Collection;
import java.util.function.Consumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.behaviors.LodestoneParticleBehavior;
import team.lodestar.lodestone.systems.particle.world.behaviors.components.LodestoneBehaviorComponent;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

public class LodestoneWorldParticle extends TextureSheetParticle {

    public final ParticleRenderType renderType;

    public final LodestoneParticleBehavior behavior;

    public final LodestoneBehaviorComponent behaviorComponent;

    public final RenderHandler.LodestoneRenderLayer renderLayer;

    public final boolean shouldCull;

    public final ParticleEngine.MutableSpriteSet spriteSet;

    public final SimpleParticleOptions.ParticleSpritePicker spritePicker;

    public final SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType;

    public final ColorParticleData colorData;

    public final GenericParticleData transparencyData;

    public final GenericParticleData scaleData;

    public final SpinParticleData spinData;

    public final Collection<Consumer<LodestoneWorldParticle>> tickActors;

    public final Collection<Consumer<LodestoneWorldParticle>> renderActors;

    private boolean reachedPositiveAlpha;

    private boolean reachedPositiveScale;

    public int lifeDelay;

    float[] hsv1 = new float[3];

    float[] hsv2 = new float[3];

    public LodestoneWorldParticle(ClientLevel world, WorldParticleOptions options, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, x, y, z);
        this.renderType = options.renderType;
        this.behavior = options.behavior;
        this.behaviorComponent = this.behavior.getComponent(options.behaviorComponent);
        this.renderLayer = options.renderLayer;
        this.shouldCull = options.shouldCull;
        this.spriteSet = spriteSet;
        this.spritePicker = options.spritePicker;
        this.discardFunctionType = options.discardFunctionType;
        this.colorData = options.colorData;
        this.transparencyData = GenericParticleData.constrictTransparency(options.transparencyData);
        this.scaleData = options.scaleData;
        this.spinData = options.spinData;
        this.tickActors = options.tickActors;
        this.renderActors = options.renderActors;
        this.f_107231_ = options.spinData.spinOffset + options.spinData.startingValue;
        this.f_107215_ = xd;
        this.f_107216_ = yd;
        this.f_107217_ = zd;
        this.m_107257_((Integer) options.lifetimeSupplier.get());
        this.lifeDelay = (Integer) options.lifeDelaySupplier.get();
        this.f_107226_ = (Float) options.gravityStrengthSupplier.get();
        this.f_172258_ = (Float) options.frictionStrengthSupplier.get();
        this.f_107219_ = !options.noClip;
        Color.RGBtoHSB((int) (255.0F * Math.min(1.0F, this.colorData.r1)), (int) (255.0F * Math.min(1.0F, this.colorData.g1)), (int) (255.0F * Math.min(1.0F, this.colorData.b1)), this.hsv1);
        Color.RGBtoHSB((int) (255.0F * Math.min(1.0F, this.colorData.r2)), (int) (255.0F * Math.min(1.0F, this.colorData.g2)), (int) (255.0F * Math.min(1.0F, this.colorData.b2)), this.hsv2);
        if (spriteSet != null) {
            if (this.getSpritePicker().equals(SimpleParticleOptions.ParticleSpritePicker.RANDOM_SPRITE)) {
                this.m_108335_(spriteSet);
            }
            if (this.getSpritePicker().equals(SimpleParticleOptions.ParticleSpritePicker.FIRST_INDEX) || this.getSpritePicker().equals(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)) {
                this.pickSprite(0);
            }
            if (this.getSpritePicker().equals(SimpleParticleOptions.ParticleSpritePicker.LAST_INDEX)) {
                this.pickSprite(spriteSet.sprites.size() - 1);
            }
        }
        options.spawnActors.forEach(actor -> actor.accept(this));
        this.updateTraits();
    }

    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return this.spritePicker;
    }

    public VertexConsumer getVertexConsumer(VertexConsumer original) {
        VertexConsumer consumerToUse = original;
        if (ClientConfig.DELAYED_PARTICLE_RENDERING.getConfigValue() && this.renderType instanceof LodestoneWorldParticleRenderType lodestoneRenderType) {
            consumerToUse = this.renderLayer.getParticleTarget().getBuffer(lodestoneRenderType.renderType);
        }
        return consumerToUse;
    }

    public void pickSprite(int spriteIndex) {
        if (spriteIndex < this.spriteSet.sprites.size() && spriteIndex >= 0) {
            this.m_108337_((TextureAtlasSprite) this.spriteSet.sprites.get(spriteIndex));
        }
    }

    public void pickColor(float colorCoeff) {
        float h = Mth.rotLerp(colorCoeff, 360.0F * this.hsv1[0], 360.0F * this.hsv2[0]) / 360.0F;
        float s = Mth.lerp(colorCoeff, this.hsv1[1], this.hsv2[1]);
        float v = Mth.lerp(colorCoeff, this.hsv1[2], this.hsv2[2]);
        int packed = Color.HSBtoRGB(h, s, v);
        float r = (float) FastColor.ARGB32.red(packed) / 255.0F;
        float g = (float) FastColor.ARGB32.green(packed) / 255.0F;
        float b = (float) FastColor.ARGB32.blue(packed) / 255.0F;
        this.m_107253_(r, g, b);
    }

    protected void updateTraits() {
        boolean shouldAttemptRemoval = this.discardFunctionType == SimpleParticleOptions.ParticleDiscardFunctionType.INVISIBLE;
        if (this.discardFunctionType == SimpleParticleOptions.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE && (this.scaleData.getProgress((float) this.f_107224_, (float) this.f_107225_) > 0.5F || this.transparencyData.getProgress((float) this.f_107224_, (float) this.f_107225_) > 0.5F)) {
            shouldAttemptRemoval = true;
        }
        if (!shouldAttemptRemoval || (!this.reachedPositiveAlpha || !(this.f_107230_ <= 0.0F)) && (!this.reachedPositiveScale || !(this.f_107663_ <= 0.0F))) {
            if (!this.reachedPositiveAlpha && this.f_107230_ > 0.0F) {
                this.reachedPositiveAlpha = true;
            }
            if (!this.reachedPositiveScale && this.f_107663_ > 0.0F) {
                this.reachedPositiveScale = true;
            }
            this.pickColor(this.colorData.colorCurveEasing.ease(this.colorData.getProgress((float) this.f_107224_, (float) this.f_107225_), 0.0F, 1.0F, 1.0F));
            this.f_107663_ = this.scaleData.getValue((float) this.f_107224_, (float) this.f_107225_);
            this.f_107230_ = this.transparencyData.getValue((float) this.f_107224_, (float) this.f_107225_);
            this.f_107204_ = this.f_107231_;
            this.f_107231_ = this.f_107231_ + this.spinData.getValue((float) this.f_107224_, (float) this.f_107225_);
            if (!this.tickActors.isEmpty()) {
                this.tickActors.forEach(a -> a.accept(this));
            }
            if (this.behaviorComponent != null) {
                this.behaviorComponent.tick(this);
            }
        } else {
            this.m_107274_();
        }
    }

    @Override
    public int getLightColor(float pPartialTick) {
        return 15728880;
    }

    @Override
    public void tick() {
        if (this.lifeDelay > 0) {
            this.lifeDelay--;
        } else {
            this.updateTraits();
            if (this.spriteSet != null && this.getSpritePicker().equals(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)) {
                this.m_108339_(this.spriteSet);
            }
            super.m_5989_();
        }
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        if (this.lifeDelay <= 0) {
            this.renderActors.forEach(actor -> actor.accept(this));
            if (this.behavior != null) {
                this.behavior.render(this, this.getVertexConsumer(consumer), camera, partialTicks);
            }
        }
    }

    public boolean shouldCull() {
        return this.shouldCull;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return this.renderType;
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        return super.m_5902_(pScaleFactor);
    }

    @Override
    public float getU0() {
        return super.getU0();
    }

    @Override
    public float getU1() {
        return super.getU1();
    }

    @Override
    public float getV0() {
        return super.getV0();
    }

    @Override
    public float getV1() {
        return super.getV1();
    }

    public float getRoll() {
        return this.f_107231_;
    }

    public float getORoll() {
        return this.f_107204_;
    }

    public float getRed() {
        return this.f_107227_;
    }

    public float getGreen() {
        return this.f_107228_;
    }

    public float getBlue() {
        return this.f_107229_;
    }

    public float getAlpha() {
        return this.f_107230_;
    }

    public double getX() {
        return this.f_107212_;
    }

    public double getY() {
        return this.f_107213_;
    }

    public double getZ() {
        return this.f_107214_;
    }

    public double getXOld() {
        return this.f_107209_;
    }

    public double getYOld() {
        return this.f_107210_;
    }

    public double getZOld() {
        return this.f_107211_;
    }

    public double getXMotion() {
        return this.f_107215_;
    }

    public double getYMotion() {
        return this.f_107216_;
    }

    public double getZMotion() {
        return this.f_107217_;
    }

    public Vec3 getParticlePosition() {
        return new Vec3(this.getX(), this.getY(), this.getZ());
    }

    public void setParticlePosition(Vec3 pos) {
        this.m_107264_(pos.x, pos.y, pos.z);
    }

    public Vec3 getParticleSpeed() {
        return new Vec3(this.getXMotion(), this.getYMotion(), this.getZMotion());
    }

    public void setParticleSpeed(Vec3 speed) {
        this.m_172260_(speed.x, speed.y, speed.z);
    }

    @Override
    public int getLifetime() {
        return this.f_107225_;
    }

    public int getAge() {
        return this.f_107224_;
    }

    public RandomSource getRandom() {
        return this.f_107223_;
    }

    public void tick(int times) {
        for (int i = 0; i < times; i++) {
            this.tick();
        }
    }
}