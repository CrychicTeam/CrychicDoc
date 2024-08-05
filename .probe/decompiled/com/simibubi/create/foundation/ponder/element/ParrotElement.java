package com.simibubi.create.foundation.ponder.element;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import com.simibubi.create.foundation.utility.AngleHelper;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class ParrotElement extends AnimatedSceneElement {

    private Vec3 location;

    private Parrot entity;

    private ParrotElement.ParrotPose pose;

    private boolean deferConductor = false;

    private Supplier<? extends ParrotElement.ParrotPose> initialPose;

    public static ParrotElement create(Vec3 location, Supplier<? extends ParrotElement.ParrotPose> pose) {
        return new ParrotElement(location, pose);
    }

    protected ParrotElement(Vec3 location, Supplier<? extends ParrotElement.ParrotPose> pose) {
        this.location = location;
        this.initialPose = pose;
        this.setPose((ParrotElement.ParrotPose) this.initialPose.get());
    }

    @Override
    public void reset(PonderScene scene) {
        super.reset(scene);
        this.setPose((ParrotElement.ParrotPose) this.initialPose.get());
        this.entity.m_20343_(0.0, 0.0, 0.0);
        this.entity.f_19854_ = 0.0;
        this.entity.f_19855_ = 0.0;
        this.entity.f_19856_ = 0.0;
        this.entity.f_19790_ = 0.0;
        this.entity.f_19791_ = 0.0;
        this.entity.f_19792_ = 0.0;
        this.entity.m_146926_(this.entity.f_19860_ = 0.0F);
        this.entity.m_146922_(this.entity.f_19859_ = 180.0F);
        this.entity.getPersistentData().remove("TrainHat");
        this.deferConductor = false;
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        if (this.entity == null) {
            this.entity = this.pose.create(scene.getWorld());
            this.entity.m_146922_(this.entity.f_19859_ = 180.0F);
            if (this.deferConductor) {
                this.setConductor(this.deferConductor);
            }
            this.deferConductor = false;
        }
        this.entity.f_19797_++;
        this.entity.f_20886_ = this.entity.f_20885_;
        this.entity.oFlapSpeed = this.entity.flapSpeed;
        this.entity.oFlap = this.entity.flap;
        this.entity.m_6853_(true);
        this.entity.f_19854_ = this.entity.m_20185_();
        this.entity.f_19855_ = this.entity.m_20186_();
        this.entity.f_19856_ = this.entity.m_20189_();
        this.entity.f_19859_ = this.entity.m_146908_();
        this.entity.f_19860_ = this.entity.m_146909_();
        this.pose.tick(scene, this.entity, this.location);
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

    public void setRotation(Vec3 eulers, boolean immediate) {
        if (this.entity != null) {
            this.entity.m_146926_((float) eulers.x);
            this.entity.m_146922_((float) eulers.y);
            if (immediate) {
                this.entity.f_19860_ = this.entity.m_146909_();
                this.entity.f_19859_ = this.entity.m_146908_();
            }
        }
    }

    public void setConductor(boolean isConductor) {
        if (this.entity == null) {
            this.deferConductor = isConductor;
        } else {
            CompoundTag data = this.entity.getPersistentData();
            if (isConductor) {
                data.putBoolean("TrainHat", true);
            } else {
                data.remove("TrainHat");
            }
        }
    }

    public Vec3 getPositionOffset() {
        return this.entity != null ? this.entity.m_20182_() : Vec3.ZERO;
    }

    public Vec3 getRotation() {
        return this.entity != null ? new Vec3((double) this.entity.m_146909_(), (double) this.entity.m_146908_(), 0.0) : Vec3.ZERO;
    }

    @Override
    protected void renderLast(PonderWorld world, MultiBufferSource buffer, PoseStack ms, float fade, float pt) {
        EntityRenderDispatcher entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        if (this.entity == null) {
            this.entity = this.pose.create(world);
            this.entity.m_146922_(this.entity.f_19859_ = 180.0F);
        }
        ms.pushPose();
        ms.translate(this.location.x, this.location.y, this.location.z);
        ms.translate(Mth.lerp((double) pt, this.entity.f_19854_, this.entity.m_20185_()), Mth.lerp((double) pt, this.entity.f_19855_, this.entity.m_20186_()), Mth.lerp((double) pt, this.entity.f_19856_, this.entity.m_20189_()));
        TransformStack.cast(ms).rotateY((double) AngleHelper.angleLerp((double) pt, (double) this.entity.f_19859_, (double) this.entity.m_146908_()));
        entityrenderermanager.render(this.entity, 0.0, 0.0, 0.0, 0.0F, pt, ms, buffer, this.lightCoordsFromFade(fade));
        ms.popPose();
    }

    public void setPose(ParrotElement.ParrotPose pose) {
        this.pose = pose;
    }

    public static class DancePose extends ParrotElement.ParrotPose {

        @Override
        Parrot create(PonderWorld world) {
            Parrot entity = super.create(world);
            entity.setRecordPlayingNearby(BlockPos.ZERO, true);
            return entity;
        }

        @Override
        void tick(PonderScene scene, Parrot entity, Vec3 location) {
            entity.f_19859_ = entity.m_146908_();
            entity.m_146922_(entity.m_146908_() - 2.0F);
        }
    }

    public static class FaceCursorPose extends ParrotElement.FaceVecPose {

        @Override
        protected Vec3 getFacedVec(PonderScene scene) {
            Minecraft minecraft = Minecraft.getInstance();
            Window w = minecraft.getWindow();
            double mouseX = minecraft.mouseHandler.xpos() * (double) w.getGuiScaledWidth() / (double) w.getScreenWidth();
            double mouseY = minecraft.mouseHandler.ypos() * (double) w.getGuiScaledHeight() / (double) w.getScreenHeight();
            return scene.getTransform().screenToScene(mouseX, mouseY, 300, 0.0F);
        }
    }

    public static class FacePointOfInterestPose extends ParrotElement.FaceVecPose {

        @Override
        protected Vec3 getFacedVec(PonderScene scene) {
            return scene.getPointOfInterest();
        }
    }

    public abstract static class FaceVecPose extends ParrotElement.ParrotPose {

        @Override
        void tick(PonderScene scene, Parrot entity, Vec3 location) {
            Vec3 p_200602_2_ = this.getFacedVec(scene);
            Vec3 Vector3d = location.add(entity.m_20299_(0.0F));
            double d0 = p_200602_2_.x - Vector3d.x;
            double d1 = p_200602_2_.y - Vector3d.y;
            double d2 = p_200602_2_.z - Vector3d.z;
            double d3 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
            float targetPitch = Mth.wrapDegrees((float) (-(Mth.atan2(d1, d3) * 180.0F / (float) Math.PI)));
            float targetYaw = Mth.wrapDegrees((float) (-(Mth.atan2(d2, d0) * 180.0F / (float) Math.PI)) + 90.0F);
            entity.m_146926_(AngleHelper.angleLerp(0.4F, (double) entity.m_146909_(), (double) targetPitch));
            entity.m_146922_(AngleHelper.angleLerp(0.4F, (double) entity.m_146908_(), (double) targetYaw));
        }

        protected abstract Vec3 getFacedVec(PonderScene var1);
    }

    public static class FlappyPose extends ParrotElement.ParrotPose {

        @Override
        void tick(PonderScene scene, Parrot entity, Vec3 location) {
            double length = entity.m_20182_().subtract(entity.f_19790_, entity.f_19791_, entity.f_19792_).length();
            entity.m_6853_(false);
            double phase = Math.min(length * 15.0, 8.0);
            float f = (float) ((double) (PonderUI.ponderTicks % 100) * phase);
            entity.flapSpeed = Mth.sin(f) + 1.0F;
            if (length == 0.0) {
                entity.flapSpeed = 0.0F;
            }
        }
    }

    public abstract static class ParrotPose {

        abstract void tick(PonderScene var1, Parrot var2, Vec3 var3);

        Parrot create(PonderWorld world) {
            Parrot entity = new Parrot(EntityType.PARROT, world);
            Parrot.Variant[] variants = Parrot.Variant.values();
            Parrot.Variant variant = variants[Create.RANDOM.nextInt(variants.length)];
            entity.setVariant(variant == Parrot.Variant.BLUE ? Parrot.Variant.RED_BLUE : variant);
            return entity;
        }
    }

    public static class SpinOnComponentPose extends ParrotElement.ParrotPose {

        private BlockPos componentPos;

        public SpinOnComponentPose(BlockPos componentPos) {
            this.componentPos = componentPos;
        }

        @Override
        void tick(PonderScene scene, Parrot entity, Vec3 location) {
            BlockEntity blockEntity = scene.getWorld().m_7702_(this.componentPos);
            if (blockEntity instanceof KineticBlockEntity) {
                float rpm = ((KineticBlockEntity) blockEntity).getSpeed();
                entity.f_19859_ = entity.m_146908_();
                entity.m_146922_(entity.m_146908_() + rpm * 0.3F);
            }
        }
    }
}