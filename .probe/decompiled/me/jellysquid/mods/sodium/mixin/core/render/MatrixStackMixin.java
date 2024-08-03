package me.jellysquid.mods.sodium.mixin.core.render;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayDeque;
import java.util.Deque;
import org.embeddedt.embeddium.render.matrix_stack.CachingPoseStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = { PoseStack.class }, priority = 900)
public abstract class MatrixStackMixin implements CachingPoseStack {

    @Shadow
    @Final
    private Deque<PoseStack.Pose> poseStack;

    @Unique
    private final Deque<PoseStack.Pose> cache = new ArrayDeque();

    private int cacheEnabled = 0;

    @Overwrite
    public void pushPose() {
        PoseStack.Pose prev = (PoseStack.Pose) this.poseStack.getLast();
        PoseStack.Pose entry;
        if (this.cacheEnabled > 0 && !this.cache.isEmpty()) {
            entry = (PoseStack.Pose) this.cache.removeLast();
            entry.pose().set(prev.pose());
            entry.normal().set(prev.normal());
        } else {
            entry = new PoseStack.Pose(new Matrix4f(prev.pose()), new Matrix3f(prev.normal()));
        }
        this.poseStack.addLast(entry);
    }

    @Overwrite
    public void popPose() {
        PoseStack.Pose pose = (PoseStack.Pose) this.poseStack.removeLast();
        if (this.cacheEnabled > 0) {
            this.cache.addLast(pose);
        }
    }

    @Override
    public void embeddium$setCachingEnabled(boolean flag) {
        this.cacheEnabled += flag ? 1 : -1;
    }
}