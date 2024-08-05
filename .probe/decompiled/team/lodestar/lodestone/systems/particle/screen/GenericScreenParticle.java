package team.lodestar.lodestone.systems.particle.screen;

import com.mojang.blaze3d.vertex.BufferBuilder;
import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.joml.Vector3d;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.systems.particle.screen.base.TextureSheetScreenParticle;

public class GenericScreenParticle extends TextureSheetScreenParticle {

    private final LodestoneScreenParticleRenderType renderType;

    protected final ParticleEngine.MutableSpriteSet spriteSet;

    protected final SimpleParticleOptions.ParticleSpritePicker spritePicker;

    protected final SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType;

    protected final ColorParticleData colorData;

    protected final GenericParticleData transparencyData;

    protected final GenericParticleData scaleData;

    protected final SpinParticleData spinData;

    protected final Consumer<GenericScreenParticle> actor;

    private final boolean tracksStack;

    private final double stackTrackXOffset;

    private final double stackTrackYOffset;

    private boolean reachedPositiveAlpha;

    private boolean reachedPositiveScale;

    private int lifeDelay;

    float[] hsv1 = new float[3];

    float[] hsv2 = new float[3];

    public GenericScreenParticle(ClientLevel world, ScreenParticleOptions options, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double xMotion, double yMotion) {
        super(world, x, y);
        this.renderType = options.renderType;
        this.spriteSet = spriteSet;
        this.spritePicker = options.spritePicker;
        this.discardFunctionType = options.discardFunctionType;
        this.colorData = options.colorData;
        this.transparencyData = GenericParticleData.constrictTransparency(options.transparencyData);
        this.scaleData = options.scaleData;
        this.spinData = options.spinData;
        this.actor = options.actor;
        this.tracksStack = options.tracksStack;
        this.stackTrackXOffset = options.stackTrackXOffset;
        this.stackTrackYOffset = options.stackTrackYOffset;
        this.roll = options.spinData.spinOffset + options.spinData.startingValue;
        this.xMotion = xMotion;
        this.yMotion = yMotion;
        this.setLifetime((Integer) options.lifetimeSupplier.get());
        this.lifeDelay = (Integer) options.lifeDelaySupplier.get();
        this.gravity = (Float) options.gravityStrengthSupplier.get();
        this.friction = (Float) options.frictionStrengthSupplier.get();
        Color.RGBtoHSB((int) (255.0F * Math.min(1.0F, this.colorData.r1)), (int) (255.0F * Math.min(1.0F, this.colorData.g1)), (int) (255.0F * Math.min(1.0F, this.colorData.b1)), this.hsv1);
        Color.RGBtoHSB((int) (255.0F * Math.min(1.0F, this.colorData.r2)), (int) (255.0F * Math.min(1.0F, this.colorData.g2)), (int) (255.0F * Math.min(1.0F, this.colorData.b2)), this.hsv2);
        this.updateTraits();
        if (this.getSpritePicker().equals(SimpleParticleOptions.ParticleSpritePicker.RANDOM_SPRITE)) {
            this.pickSprite(spriteSet);
        }
        if (this.getSpritePicker().equals(SimpleParticleOptions.ParticleSpritePicker.FIRST_INDEX) || this.getSpritePicker().equals(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)) {
            this.pickSprite(0);
        }
        if (this.getSpritePicker().equals(SimpleParticleOptions.ParticleSpritePicker.LAST_INDEX)) {
            this.pickSprite(spriteSet.sprites.size() - 1);
        }
        this.updateTraits();
    }

    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return this.spritePicker;
    }

    public void pickSprite(int spriteIndex) {
        if (spriteIndex < this.spriteSet.sprites.size() && spriteIndex >= 0) {
            this.setSprite((TextureAtlasSprite) this.spriteSet.sprites.get(spriteIndex));
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
        this.setColor(r, g, b);
    }

    public float getCurve(float multiplier) {
        return Mth.clamp((float) this.age * multiplier / (float) this.lifetime, 0.0F, 1.0F);
    }

    protected void updateTraits() {
        boolean shouldAttemptRemoval = this.discardFunctionType == SimpleParticleOptions.ParticleDiscardFunctionType.INVISIBLE;
        if (this.discardFunctionType == SimpleParticleOptions.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE && (this.scaleData.getProgress((float) this.age, (float) this.lifetime) > 0.5F || this.transparencyData.getProgress((float) this.age, (float) this.lifetime) > 0.5F)) {
            shouldAttemptRemoval = true;
        }
        if (!shouldAttemptRemoval || (!this.reachedPositiveAlpha || !(this.alpha <= 0.0F)) && (!this.reachedPositiveScale || !(this.quadSize <= 0.0F))) {
            if (!this.reachedPositiveAlpha && this.alpha > 0.0F) {
                this.reachedPositiveAlpha = true;
            }
            if (!this.reachedPositiveScale && this.quadSize > 0.0F) {
                this.reachedPositiveScale = true;
            }
            this.pickColor(this.colorData.colorCurveEasing.ease(this.colorData.getProgress((float) this.age, (float) this.lifetime), 0.0F, 1.0F, 1.0F));
            this.quadSize = this.scaleData.getValue((float) this.age, (float) this.lifetime);
            this.alpha = Mth.clamp(this.transparencyData.getValue((float) this.age, (float) this.lifetime), 0.0F, 1.0F);
            this.oRoll = this.roll;
            this.roll = this.roll + this.spinData.getValue((float) this.age, (float) this.lifetime);
            if (this.actor != null) {
                this.actor.accept(this);
            }
        } else {
            this.remove();
        }
    }

    @Override
    public void render(BufferBuilder bufferBuilder) {
        if (this.lifeDelay <= 0) {
            if (this.tracksStack) {
                this.x = (double) ScreenParticleHandler.currentItemX + this.stackTrackXOffset + this.xMoved;
                this.y = (double) ScreenParticleHandler.currentItemY + this.stackTrackYOffset + this.yMoved;
            }
            super.render(bufferBuilder);
        }
    }

    @Override
    public void tick() {
        if (this.lifeDelay > 0) {
            this.lifeDelay--;
        } else {
            this.updateTraits();
            if (this.getSpritePicker().equals(SimpleParticleOptions.ParticleSpritePicker.WITH_AGE)) {
                this.setSpriteFromAge(this.spriteSet);
            }
            super.tick();
        }
    }

    @Override
    public LodestoneScreenParticleRenderType getRenderType() {
        return this.renderType;
    }

    public void setParticleSpeed(Vector3d speed) {
        this.setParticleSpeed(speed.x, speed.y);
    }
}