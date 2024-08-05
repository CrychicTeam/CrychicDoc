package noppes.npcs.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Deque;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ PoseStack.class })
public interface MatrixStackMixin {

    @Accessor("poseStack")
    Deque<PoseStack.Pose> getStack();
}