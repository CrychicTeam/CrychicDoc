package net.mehvahdjukaar.amendments.integration;

import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import com.lowdragmc.shimmer.client.shader.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.BiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;

public class ShimmerCompat {

    public static void renderWithBloom(PoseStack poseStack, BiConsumer<PoseStack, MultiBufferSource> renderFunction) {
        PoseStack finalStack = RenderUtils.copyPoseStack(poseStack);
        PostProcessing.BLOOM_UNREAL.postEntity(b -> renderFunction.accept(finalStack, b));
    }
}