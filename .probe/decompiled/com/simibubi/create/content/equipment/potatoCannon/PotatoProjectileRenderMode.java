package com.simibubi.create.content.equipment.potatoCannon;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface PotatoProjectileRenderMode {

    @OnlyIn(Dist.CLIENT)
    void transform(PoseStack var1, PotatoProjectileEntity var2, float var3);

    static int entityRandom(Entity entity, int maxValue) {
        return System.identityHashCode(entity) * 31 % maxValue;
    }

    public static class Billboard implements PotatoProjectileRenderMode {

        public static final PotatoProjectileRenderMode.Billboard INSTANCE = new PotatoProjectileRenderMode.Billboard();

        @OnlyIn(Dist.CLIENT)
        @Override
        public void transform(PoseStack ms, PotatoProjectileEntity entity, float pt) {
            Minecraft mc = Minecraft.getInstance();
            Vec3 p1 = mc.getCameraEntity().getEyePosition(pt);
            Vec3 diff = entity.m_20191_().getCenter().subtract(p1);
            ((TransformStack) TransformStack.cast(ms).rotateY((double) (AngleHelper.deg(Mth.atan2(diff.x, diff.z)) + 180.0F))).rotateX((double) AngleHelper.deg(Mth.atan2(diff.y, (double) Mth.sqrt((float) (diff.x * diff.x + diff.z * diff.z)))));
        }
    }

    public static class StuckToEntity implements PotatoProjectileRenderMode {

        private Vec3 offset;

        public StuckToEntity(Vec3 offset) {
            this.offset = offset;
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void transform(PoseStack ms, PotatoProjectileEntity entity, float pt) {
            TransformStack.cast(ms).rotateY((double) AngleHelper.deg(Mth.atan2(this.offset.x, this.offset.z)));
        }
    }

    public static class TowardMotion implements PotatoProjectileRenderMode {

        private int spriteAngleOffset;

        private float spin;

        public TowardMotion(int spriteAngleOffset, float spin) {
            this.spriteAngleOffset = spriteAngleOffset;
            this.spin = spin;
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void transform(PoseStack ms, PotatoProjectileEntity entity, float pt) {
            Vec3 diff = entity.m_20184_();
            ((TransformStack) TransformStack.cast(ms).rotateY((double) AngleHelper.deg(Mth.atan2(diff.x, diff.z)))).rotateX((double) (270.0F + AngleHelper.deg(Mth.atan2(diff.y, (double) (-Mth.sqrt((float) (diff.x * diff.x + diff.z * diff.z)))))));
            ((TransformStack) TransformStack.cast(ms).rotateY((double) (((float) entity.f_19797_ + pt) * 20.0F * this.spin + (float) PotatoProjectileRenderMode.entityRandom(entity, 360)))).rotateZ((double) (-this.spriteAngleOffset));
        }
    }

    public static class Tumble extends PotatoProjectileRenderMode.Billboard {

        public static final PotatoProjectileRenderMode.Tumble INSTANCE = new PotatoProjectileRenderMode.Tumble();

        @OnlyIn(Dist.CLIENT)
        @Override
        public void transform(PoseStack ms, PotatoProjectileEntity entity, float pt) {
            super.transform(ms, entity, pt);
            ((TransformStack) TransformStack.cast(ms).rotateZ((double) (((float) entity.f_19797_ + pt) * 2.0F * (float) PotatoProjectileRenderMode.entityRandom(entity, 16)))).rotateX((double) (((float) entity.f_19797_ + pt) * (float) PotatoProjectileRenderMode.entityRandom(entity, 32)));
        }
    }
}