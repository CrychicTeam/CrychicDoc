package org.embeddedt.modernfix.forge.mixin.bugfix.entity_pose_stack;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Deque;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ PoseStack.class })
@ClientOnlyMixin
public interface PoseStackAccessor {

    @Accessor
    Deque<PoseStack.Pose> getPoseStack();
}