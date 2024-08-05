package net.mehvahdjukaar.supplementaries.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.client.util.VertexUtil;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.client.renderers.color.ColorHelper;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class VertexUtils {

    public static int setColorForAge(float age, float phase) {
        float a = (age + phase) % 1.0F;
        float[] col = ColorHelper.getBubbleColor(a);
        return FastColor.ARGB32.color(255, (int) (col[0] * 255.0F), (int) (col[1] * 255.0F), (int) (col[2] * 255.0F));
    }

    public static void renderBubble(VertexConsumer builder, PoseStack poseStack, int combinedLightIn, BlockPos pos, Level level, float partialTicks) {
        TextureAtlasSprite sprite = ModMaterials.BUBBLE_BLOCK_MATERIAL.sprite();
        builder = sprite.wrap(builder);
        int lu = combinedLightIn & 65535;
        int lv = combinedLightIn >> 16 & 65535;
        float minU = 0.0F;
        float minV = 0.0F;
        float maxU = 1.0F;
        float maxV = 1.0F;
        float w = 1.0F;
        long t = level == null ? System.currentTimeMillis() / 50L : level.getGameTime();
        float time = ((float) Math.floorMod((long) pos.m_123341_() * 7L + (long) pos.m_123342_() * 9L + (long) pos.m_123343_() * 13L + t, 100L) + partialTicks) / 100.0F;
        int cUnw = setColorForAge(time, 0.0F);
        int cUne = setColorForAge(time, 0.15F);
        int cUse = setColorForAge(time, 0.55F);
        int cUsw = setColorForAge(time, 0.35F);
        int cDnw = setColorForAge(time, 0.45F);
        int cDne = setColorForAge(time, 0.85F);
        int cDse = setColorForAge(time, 1.0F);
        int cDsw = setColorForAge(time, 0.65F);
        float amp = (float) ((Double) ClientConfigs.Blocks.BUBBLE_BLOCK_WOBBLE.get() / 10.0);
        w -= 2.0F * amp;
        float unw = amp * Mth.cos((float) (Math.PI * 2) * (time + 0.0F));
        float une = amp * Mth.cos((float) (Math.PI * 2) * (time + 0.25F));
        float use = amp * Mth.cos((float) (Math.PI * 2) * (time + 0.5F));
        float usw = amp * Mth.cos((float) (Math.PI * 2) * (time + 0.75F));
        float l = w / 2.0F;
        vert(builder, poseStack, -l - usw, l + usw, l + usw, minU, maxV, cUsw, lu, lv, 0.0F, 1.0F, 0.0F);
        vert(builder, poseStack, l + use, l + use, l + use, maxU, maxV, cUse, lu, lv, 0.0F, 1.0F, 0.0F);
        vert(builder, poseStack, l + une, l + une, -l - une, maxU, minV, cUne, lu, lv, 0.0F, 1.0F, 0.0F);
        vert(builder, poseStack, -l - unw, l + unw, -l - unw, minU, minV, cUnw, lu, lv, 0.0F, 1.0F, 0.0F);
        vert(builder, poseStack, -l - use, -l - use, -l - use, minU, maxV, cDnw, lu, lv, 0.0F, -1.0F, 0.0F);
        vert(builder, poseStack, l + usw, -l - usw, -l - usw, maxU, maxV, cDne, lu, lv, 0.0F, -1.0F, 0.0F);
        vert(builder, poseStack, l + unw, -l - unw, l + unw, maxU, minV, cDse, lu, lv, 0.0F, -1.0F, 0.0F);
        vert(builder, poseStack, -l - une, -l - une, l + une, minU, minV, cDsw, lu, lv, 0.0F, -1.0F, 0.0F);
        vert(builder, poseStack, l + usw, -l - usw, -l - usw, minU, maxV, cDne, lu, lv, 0.0F, 0.0F, -1.0F);
        vert(builder, poseStack, -l - use, -l - use, -l - use, maxU, maxV, cDnw, lu, lv, 0.0F, 0.0F, -1.0F);
        vert(builder, poseStack, -l - unw, l + unw, -l - unw, maxU, minV, cUnw, lu, lv, 0.0F, 0.0F, -1.0F);
        vert(builder, poseStack, l + une, l + une, -l - une, minU, minV, cUne, lu, lv, 0.0F, 0.0F, -1.0F);
        vert(builder, poseStack, -l - use, -l - use, -l - use, minU, maxV, cDnw, lu, lv, -1.0F, 0.0F, 0.0F);
        vert(builder, poseStack, -l - une, -l - une, l + une, maxU, maxV, cDsw, lu, lv, -1.0F, 0.0F, 0.0F);
        vert(builder, poseStack, -l - usw, l + usw, l + usw, maxU, minV, cUsw, lu, lv, -1.0F, 0.0F, 0.0F);
        vert(builder, poseStack, -l - unw, l + unw, -l - unw, minU, minV, cUnw, lu, lv, -1.0F, 0.0F, 0.0F);
        vert(builder, poseStack, -l - une, -l - une, l + une, minU, maxV, cDsw, lu, lv, 0.0F, 0.0F, 1.0F);
        vert(builder, poseStack, l + unw, -l - unw, l + unw, maxU, maxV, cDse, lu, lv, 0.0F, 0.0F, 1.0F);
        vert(builder, poseStack, l + use, l + use, l + use, maxU, minV, cUse, lu, lv, 0.0F, 0.0F, 1.0F);
        vert(builder, poseStack, -l - usw, l + usw, l + usw, minU, minV, cUsw, lu, lv, 0.0F, 0.0F, 1.0F);
        vert(builder, poseStack, l + unw, -l - unw, l + unw, minU, maxV, cDse, lu, lv, 1.0F, 0.0F, 0.0F);
        vert(builder, poseStack, l + usw, -l - usw, -l - usw, maxU, maxV, cDne, lu, lv, 1.0F, 0.0F, 0.0F);
        vert(builder, poseStack, l + une, l + une, -l - une, maxU, minV, cUne, lu, lv, 1.0F, 0.0F, 0.0F);
        vert(builder, poseStack, l + use, l + use, l + use, minU, minV, cUse, lu, lv, 1.0F, 0.0F, 0.0F);
    }

    public static void vert(VertexConsumer builder, PoseStack poseStack, float x, float y, float z, float u, float v, int color, int lu, int lv, float nx, float ny, float nz) {
        builder.vertex(poseStack.last().pose(), x, y, z);
        builder.color(color);
        builder.uv(u, v);
        builder.overlayCoords(0, 10);
        builder.uv2(lu, lv);
        builder.normal(poseStack.last().normal(), nx, ny, nz);
        builder.endVertex();
    }

    public static void renderFish(MultiBufferSource buffers, PoseStack poseStack, float wo, float ho, int fishType, int combinedLightIn) {
        int textW = 64;
        int textH = 32;
        int fishW = 5;
        int fishH = 4;
        int fishv = --fishType % (textH / fishH);
        int fishu = fishType / (textH / fishH);
        VertexConsumer builder = ModMaterials.FISHIES.buffer(buffers, RenderType::m_110452_);
        float w = (float) fishW / (float) textW;
        float h = (float) fishH / (float) textH;
        float hw = 4.0F * w / 2.0F;
        float hh = 2.0F * h / 2.0F;
        int lu = combinedLightIn & 65535;
        int lv = combinedLightIn >> 16 & 65535;
        float minu = (float) (0 * fishu) * w;
        float minv = (float) (0 * fishv) * h;
        float maxu = 1.0F * w + minu;
        float maxv = 1.0F * h + minv;
        for (int k = 0; k < 2; k++) {
            for (int j = 0; j < 2; j++) {
                VertexUtil.vert(builder, poseStack, hw - Math.abs(wo / 2.0F), -hh + ho, wo, minu, maxv, 1.0F, 1.0F, 1.0F, 1.0F, lu, lv, 0.0F, 1.0F, 0.0F);
                VertexUtil.vert(builder, poseStack, -hw + Math.abs(wo / 2.0F), -hh + ho, -wo, maxu, maxv, 1.0F, 1.0F, 1.0F, 1.0F, lu, lv, 0.0F, 1.0F, 0.0F);
                VertexUtil.vert(builder, poseStack, -hw + Math.abs(wo / 2.0F), hh + ho, -wo, maxu, minv, 1.0F, 1.0F, 1.0F, 1.0F, lu, lv, 0.0F, 1.0F, 0.0F);
                VertexUtil.vert(builder, poseStack, hw - Math.abs(wo / 2.0F), hh + ho, wo, minu, minv, 1.0F, 1.0F, 1.0F, 1.0F, lu, lv, 0.0F, 1.0F, 0.0F);
                poseStack.mulPose(RotHlpr.Y180);
                float temp = minu;
                minu = maxu;
                maxu = temp;
            }
            lu = 240;
            minu += 0.0F;
            maxu += 0.0F;
        }
    }
}