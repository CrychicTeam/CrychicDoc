package com.simibubi.create.content.kinetics.base;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class RotationIndicatorParticle extends SimpleAnimatedParticle {

    protected float radius;

    protected float radius1;

    protected float radius2;

    protected float speed;

    protected Direction.Axis axis;

    protected Vec3 origin;

    protected Vec3 offset;

    protected boolean isVisible;

    private RotationIndicatorParticle(ClientLevel world, double x, double y, double z, int color, float radius1, float radius2, float speed, Direction.Axis axis, int lifeSpan, boolean isVisible, SpriteSet sprite) {
        super(world, x, y, z, sprite, 0.0F);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.origin = new Vec3(x, y, z);
        this.f_107663_ *= 0.75F;
        this.f_107225_ = lifeSpan + this.f_107223_.nextInt(32);
        this.m_107659_(color);
        this.m_107657_(Color.mixColors(color, 16777215, 0.5F));
        this.m_108339_(sprite);
        this.radius1 = radius1;
        this.radius = radius1;
        this.radius2 = radius2;
        this.speed = speed;
        this.axis = axis;
        this.isVisible = isVisible;
        this.offset = axis.isHorizontal() ? new Vec3(0.0, 1.0, 0.0) : new Vec3(1.0, 0.0, 0.0);
        this.move(0.0, 0.0, 0.0);
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
    }

    @Override
    public void tick() {
        super.tick();
        this.radius = this.radius + (this.radius2 - this.radius) * 0.1F;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (this.isVisible) {
            super.m_5744_(buffer, renderInfo, partialTicks);
        }
    }

    @Override
    public void move(double x, double y, double z) {
        float time = (float) AnimationTickHolder.getTicks(this.f_107208_);
        float angle = time * this.speed % 360.0F - this.speed / 2.0F * (float) this.f_107224_ * ((float) this.f_107224_ / (float) this.f_107225_);
        if (this.speed < 0.0F && this.axis.isVertical()) {
            angle += 180.0F;
        }
        Vec3 position = VecHelper.rotate(this.offset.scale((double) this.radius), (double) angle, this.axis).add(this.origin);
        this.f_107212_ = position.x;
        this.f_107213_ = position.y;
        this.f_107214_ = position.z;
    }

    public static class Factory implements ParticleProvider<RotationIndicatorParticleData> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet animatedSprite) {
            this.spriteSet = animatedSprite;
        }

        public Particle createParticle(RotationIndicatorParticleData data, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            boolean visible = worldIn != mc.level || player != null && GogglesItem.isWearingGoggles(player);
            return new RotationIndicatorParticle(worldIn, x, y, z, data.color, data.radius1, data.radius2, data.speed, data.getAxis(), data.lifeSpan, visible, this.spriteSet);
        }
    }
}