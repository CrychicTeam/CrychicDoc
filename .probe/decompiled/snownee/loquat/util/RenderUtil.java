package snownee.loquat.util;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.AABB;

public interface RenderUtil {

    static void renderLineBox(PoseStack poseStack, AABB box, float red, float green, float blue, float alpha) {
        RenderType.lines().m_110185_();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(RenderType.lines().mode(), RenderType.lines().format());
        LevelRenderer.renderLineBox(poseStack, bufferBuilder, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, red, green, blue, alpha, red, green, blue);
        tesselator.end();
        RenderType.lines().m_110188_();
    }
}