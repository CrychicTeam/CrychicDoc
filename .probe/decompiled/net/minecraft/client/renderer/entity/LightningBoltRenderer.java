package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LightningBolt;
import org.joml.Matrix4f;

public class LightningBoltRenderer extends EntityRenderer<LightningBolt> {

    public LightningBoltRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
    }

    public void render(LightningBolt lightningBolt0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        float[] $$6 = new float[8];
        float[] $$7 = new float[8];
        float $$8 = 0.0F;
        float $$9 = 0.0F;
        RandomSource $$10 = RandomSource.create(lightningBolt0.seed);
        for (int $$11 = 7; $$11 >= 0; $$11--) {
            $$6[$$11] = $$8;
            $$7[$$11] = $$9;
            $$8 += (float) ($$10.nextInt(11) - 5);
            $$9 += (float) ($$10.nextInt(11) - 5);
        }
        VertexConsumer $$12 = multiBufferSource4.getBuffer(RenderType.lightning());
        Matrix4f $$13 = poseStack3.last().pose();
        for (int $$14 = 0; $$14 < 4; $$14++) {
            RandomSource $$15 = RandomSource.create(lightningBolt0.seed);
            for (int $$16 = 0; $$16 < 3; $$16++) {
                int $$17 = 7;
                int $$18 = 0;
                if ($$16 > 0) {
                    $$17 = 7 - $$16;
                }
                if ($$16 > 0) {
                    $$18 = $$17 - 2;
                }
                float $$19 = $$6[$$17] - $$8;
                float $$20 = $$7[$$17] - $$9;
                for (int $$21 = $$17; $$21 >= $$18; $$21--) {
                    float $$22 = $$19;
                    float $$23 = $$20;
                    if ($$16 == 0) {
                        $$19 += (float) ($$15.nextInt(11) - 5);
                        $$20 += (float) ($$15.nextInt(11) - 5);
                    } else {
                        $$19 += (float) ($$15.nextInt(31) - 15);
                        $$20 += (float) ($$15.nextInt(31) - 15);
                    }
                    float $$24 = 0.5F;
                    float $$25 = 0.45F;
                    float $$26 = 0.45F;
                    float $$27 = 0.5F;
                    float $$28 = 0.1F + (float) $$14 * 0.2F;
                    if ($$16 == 0) {
                        $$28 *= (float) $$21 * 0.1F + 1.0F;
                    }
                    float $$29 = 0.1F + (float) $$14 * 0.2F;
                    if ($$16 == 0) {
                        $$29 *= ((float) $$21 - 1.0F) * 0.1F + 1.0F;
                    }
                    quad($$13, $$12, $$19, $$20, $$21, $$22, $$23, 0.45F, 0.45F, 0.5F, $$28, $$29, false, false, true, false);
                    quad($$13, $$12, $$19, $$20, $$21, $$22, $$23, 0.45F, 0.45F, 0.5F, $$28, $$29, true, false, true, true);
                    quad($$13, $$12, $$19, $$20, $$21, $$22, $$23, 0.45F, 0.45F, 0.5F, $$28, $$29, true, true, false, true);
                    quad($$13, $$12, $$19, $$20, $$21, $$22, $$23, 0.45F, 0.45F, 0.5F, $$28, $$29, false, true, false, false);
                }
            }
        }
    }

    private static void quad(Matrix4f matrixF0, VertexConsumer vertexConsumer1, float float2, float float3, int int4, float float5, float float6, float float7, float float8, float float9, float float10, float float11, boolean boolean12, boolean boolean13, boolean boolean14, boolean boolean15) {
        vertexConsumer1.vertex(matrixF0, float2 + (boolean12 ? float11 : -float11), (float) (int4 * 16), float3 + (boolean13 ? float11 : -float11)).color(float7, float8, float9, 0.3F).endVertex();
        vertexConsumer1.vertex(matrixF0, float5 + (boolean12 ? float10 : -float10), (float) ((int4 + 1) * 16), float6 + (boolean13 ? float10 : -float10)).color(float7, float8, float9, 0.3F).endVertex();
        vertexConsumer1.vertex(matrixF0, float5 + (boolean14 ? float10 : -float10), (float) ((int4 + 1) * 16), float6 + (boolean15 ? float10 : -float10)).color(float7, float8, float9, 0.3F).endVertex();
        vertexConsumer1.vertex(matrixF0, float2 + (boolean14 ? float11 : -float11), (float) (int4 * 16), float3 + (boolean15 ? float11 : -float11)).color(float7, float8, float9, 0.3F).endVertex();
    }

    public ResourceLocation getTextureLocation(LightningBolt lightningBolt0) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}