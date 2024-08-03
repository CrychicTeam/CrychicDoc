package com.simibubi.create.content.equipment.bell;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllParticleTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.joml.Quaternionf;

public class SoulParticle extends CustomRotationParticle {

    private final SpriteSet animatedSprite;

    protected int startTicks;

    protected int endTicks;

    protected int numLoops;

    protected int firstStartFrame = 0;

    protected int startFrames = 17;

    protected int firstLoopFrame = 17;

    protected int loopFrames = 16;

    protected int firstEndFrame = 33;

    protected int endFrames = 20;

    protected SoulParticle.AnimationStage animationStage;

    protected int totalFrames = 53;

    protected int ticksPerFrame = 2;

    protected boolean isPerimeter = false;

    protected boolean isExpandingPerimeter = false;

    protected boolean isVisible = true;

    protected int perimeterFrames = 8;

    public SoulParticle(ClientLevel worldIn, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet, ParticleOptions data) {
        super(worldIn, x, y, z, spriteSet, 0.0F);
        this.animatedSprite = spriteSet;
        this.f_107663_ = 0.5F;
        this.m_107250_(this.f_107663_, this.f_107663_);
        this.loopLength = this.loopFrames + (int) (this.f_107223_.nextFloat() * 5.0F - 4.0F);
        this.startTicks = this.startFrames + (int) (this.f_107223_.nextFloat() * 5.0F - 4.0F);
        this.endTicks = this.endFrames + (int) (this.f_107223_.nextFloat() * 5.0F - 4.0F);
        this.numLoops = (int) (1.0F + this.f_107223_.nextFloat() * 2.0F);
        this.setFrame(0);
        this.f_107205_ = true;
        this.mirror = this.f_107223_.nextBoolean();
        this.isPerimeter = data instanceof SoulParticle.PerimeterData;
        this.isExpandingPerimeter = data instanceof SoulParticle.ExpandingPerimeterData;
        this.animationStage = (SoulParticle.AnimationStage) (!this.isPerimeter ? new SoulParticle.StartAnimation(this) : new SoulParticle.PerimeterAnimation(this));
        if (this.isPerimeter) {
            double var16;
            this.f_107210_ = var16 = y - 0.4921875;
            this.totalFrames = this.perimeterFrames;
            this.isVisible = false;
        }
    }

    @Override
    public void tick() {
        this.animationStage.tick();
        this.animationStage = this.animationStage.getNext();
        BlockPos pos = BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_);
        if (this.animationStage == null) {
            this.m_107274_();
        }
        if (!SoulPulseEffect.isDark(this.f_107208_, pos)) {
            this.isVisible = true;
            if (!this.isPerimeter) {
                this.m_107274_();
            }
        } else if (this.isPerimeter) {
            this.isVisible = false;
        }
    }

    @Override
    public void render(VertexConsumer builder, Camera camera, float partialTicks) {
        if (this.isVisible) {
            super.render(builder, camera, partialTicks);
        }
    }

    public void setFrame(int frame) {
        if (frame >= 0 && frame < this.totalFrames) {
            this.m_108337_(this.animatedSprite.get(frame, this.totalFrames));
        }
    }

    @Override
    public Quaternionf getCustomRotation(Camera camera, float partialTicks) {
        return this.isPerimeter ? Axis.XP.rotationDegrees(90.0F) : new Quaternionf().rotationXYZ(0.0F, -camera.getYRot() * (float) (Math.PI / 180.0), 0.0F);
    }

    public abstract static class AnimationStage {

        protected final SoulParticle particle;

        protected int ticks;

        protected int animAge;

        public AnimationStage(SoulParticle particle) {
            this.particle = particle;
        }

        public void tick() {
            this.ticks++;
            if (this.ticks % this.particle.ticksPerFrame == 0) {
                this.animAge++;
            }
        }

        public float getAnimAge() {
            return (float) this.animAge;
        }

        public abstract SoulParticle.AnimationStage getNext();
    }

    public static class Data extends BasicParticleData<SoulParticle> {

        @Override
        public BasicParticleData.IBasicParticleFactory<SoulParticle> getBasicFactory() {
            return (worldIn, x, y, z, vx, vy, vz, spriteSet) -> new SoulParticle(worldIn, x, y, z, vx, vy, vz, spriteSet, this);
        }

        @Override
        public ParticleType<?> getType() {
            return AllParticleTypes.SOUL.get();
        }
    }

    public static class EndAnimation extends SoulParticle.AnimationStage {

        public EndAnimation(SoulParticle particle) {
            super(particle);
        }

        @Override
        public void tick() {
            super.tick();
            this.particle.setFrame(this.particle.firstEndFrame + (int) (this.getAnimAge() / (float) this.particle.endTicks * (float) this.particle.endFrames));
        }

        @Override
        public SoulParticle.AnimationStage getNext() {
            return this.animAge < this.particle.endTicks ? this : null;
        }
    }

    public static class ExpandingPerimeterData extends SoulParticle.PerimeterData {

        @Override
        public ParticleType<?> getType() {
            return AllParticleTypes.SOUL_EXPANDING_PERIMETER.get();
        }
    }

    public static class LoopAnimation extends SoulParticle.AnimationStage {

        int loops;

        public LoopAnimation(SoulParticle particle) {
            super(particle);
        }

        @Override
        public void tick() {
            super.tick();
            int loopTick = this.getLoopTick();
            if (loopTick == 0) {
                this.loops++;
            }
            this.particle.setFrame(this.particle.firstLoopFrame + loopTick);
        }

        private int getLoopTick() {
            return this.animAge % this.particle.loopFrames;
        }

        @Override
        public SoulParticle.AnimationStage getNext() {
            return (SoulParticle.AnimationStage) (this.loops <= this.particle.numLoops ? this : new SoulParticle.EndAnimation(this.particle));
        }
    }

    public static class PerimeterAnimation extends SoulParticle.AnimationStage {

        public PerimeterAnimation(SoulParticle particle) {
            super(particle);
        }

        @Override
        public void tick() {
            super.tick();
            this.particle.setFrame((int) this.getAnimAge() % this.particle.perimeterFrames);
        }

        @Override
        public SoulParticle.AnimationStage getNext() {
            return this.animAge < (this.particle.isExpandingPerimeter ? 8 : this.particle.startTicks + this.particle.endTicks + this.particle.numLoops * this.particle.loopLength) ? this : null;
        }
    }

    public static class PerimeterData extends BasicParticleData<SoulParticle> {

        @Override
        public BasicParticleData.IBasicParticleFactory<SoulParticle> getBasicFactory() {
            return (worldIn, x, y, z, vx, vy, vz, spriteSet) -> new SoulParticle(worldIn, x, y, z, vx, vy, vz, spriteSet, this);
        }

        @Override
        public ParticleType<?> getType() {
            return AllParticleTypes.SOUL_PERIMETER.get();
        }
    }

    public static class StartAnimation extends SoulParticle.AnimationStage {

        public StartAnimation(SoulParticle particle) {
            super(particle);
        }

        @Override
        public void tick() {
            super.tick();
            this.particle.setFrame(this.particle.firstStartFrame + (int) (this.getAnimAge() / (float) this.particle.startTicks * (float) this.particle.startFrames));
        }

        @Override
        public SoulParticle.AnimationStage getNext() {
            return (SoulParticle.AnimationStage) (this.animAge < this.particle.startTicks ? this : new SoulParticle.LoopAnimation(this.particle));
        }
    }
}