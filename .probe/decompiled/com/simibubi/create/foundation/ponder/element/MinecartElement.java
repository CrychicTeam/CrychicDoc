package com.simibubi.create.foundation.ponder.element;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MinecartElement extends AnimatedSceneElement {

    private Vec3 location;

    private LerpedFloat rotation;

    private AbstractMinecart entity;

    private MinecartElement.MinecartConstructor constructor;

    private float initialRotation;

    public MinecartElement(Vec3 location, float rotation, MinecartElement.MinecartConstructor constructor) {
        this.initialRotation = rotation;
        this.location = location.add(0.0, 0.0625, 0.0);
        this.constructor = constructor;
        this.rotation = LerpedFloat.angular().startWithValue((double) rotation);
    }

    @Override
    public void reset(PonderScene scene) {
        super.reset(scene);
        this.entity.m_20343_(0.0, 0.0, 0.0);
        this.entity.f_19854_ = 0.0;
        this.entity.f_19855_ = 0.0;
        this.entity.f_19856_ = 0.0;
        this.entity.f_19790_ = 0.0;
        this.entity.f_19791_ = 0.0;
        this.entity.f_19792_ = 0.0;
        this.rotation.startWithValue((double) this.initialRotation);
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        if (this.entity == null) {
            this.entity = this.constructor.create(scene.getWorld(), 0.0, 0.0, 0.0);
        }
        this.entity.f_19797_++;
        this.entity.m_6853_(true);
        this.entity.f_19854_ = this.entity.m_20185_();
        this.entity.f_19855_ = this.entity.m_20186_();
        this.entity.f_19856_ = this.entity.m_20189_();
        this.entity.f_19790_ = this.entity.m_20185_();
        this.entity.f_19791_ = this.entity.m_20186_();
        this.entity.f_19792_ = this.entity.m_20189_();
    }

    public void setPositionOffset(Vec3 position, boolean immediate) {
        if (this.entity != null) {
            this.entity.m_6034_(position.x, position.y, position.z);
            if (immediate) {
                this.entity.f_19854_ = position.x;
                this.entity.f_19855_ = position.y;
                this.entity.f_19856_ = position.z;
            }
        }
    }

    public void setRotation(float angle, boolean immediate) {
        if (this.entity != null) {
            this.rotation.setValue((double) angle);
            if (immediate) {
                this.rotation.startWithValue((double) angle);
            }
        }
    }

    public Vec3 getPositionOffset() {
        return this.entity != null ? this.entity.m_20182_() : Vec3.ZERO;
    }

    public Vec3 getRotation() {
        return new Vec3(0.0, (double) this.rotation.getValue(), 0.0);
    }

    @Override
    protected void renderLast(PonderWorld world, MultiBufferSource buffer, PoseStack ms, float fade, float pt) {
        EntityRenderDispatcher entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        if (this.entity == null) {
            this.entity = this.constructor.create(world, 0.0, 0.0, 0.0);
        }
        ms.pushPose();
        ms.translate(this.location.x, this.location.y, this.location.z);
        ms.translate(Mth.lerp((double) pt, this.entity.f_19854_, this.entity.m_20185_()), Mth.lerp((double) pt, this.entity.f_19855_, this.entity.m_20186_()), Mth.lerp((double) pt, this.entity.f_19856_, this.entity.m_20189_()));
        TransformStack.cast(ms).rotateY((double) this.rotation.getValue(pt));
        entityrenderermanager.render(this.entity, 0.0, 0.0, 0.0, 0.0F, pt, ms, buffer, this.lightCoordsFromFade(fade));
        ms.popPose();
    }

    public interface MinecartConstructor {

        AbstractMinecart create(Level var1, double var2, double var4, double var6);
    }
}