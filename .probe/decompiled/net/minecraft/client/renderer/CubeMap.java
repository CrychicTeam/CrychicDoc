package net.minecraft.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class CubeMap {

    private static final int SIDES = 6;

    private final ResourceLocation[] images = new ResourceLocation[6];

    public CubeMap(ResourceLocation resourceLocation0) {
        for (int $$1 = 0; $$1 < 6; $$1++) {
            this.images[$$1] = resourceLocation0.withPath(resourceLocation0.getPath() + "_" + $$1 + ".png");
        }
    }

    public void render(Minecraft minecraft0, float float1, float float2, float float3) {
        Tesselator $$4 = Tesselator.getInstance();
        BufferBuilder $$5 = $$4.getBuilder();
        Matrix4f $$6 = new Matrix4f().setPerspective(1.4835298F, (float) minecraft0.getWindow().getWidth() / (float) minecraft0.getWindow().getHeight(), 0.05F, 10.0F);
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix($$6, VertexSorting.DISTANCE_TO_ORIGIN);
        PoseStack $$7 = RenderSystem.getModelViewStack();
        $$7.pushPose();
        $$7.setIdentity();
        $$7.mulPose(Axis.XP.rotationDegrees(180.0F));
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShader(GameRenderer::m_172820_);
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        int $$8 = 2;
        for (int $$9 = 0; $$9 < 4; $$9++) {
            $$7.pushPose();
            float $$10 = ((float) ($$9 % 2) / 2.0F - 0.5F) / 256.0F;
            float $$11 = ((float) ($$9 / 2) / 2.0F - 0.5F) / 256.0F;
            float $$12 = 0.0F;
            $$7.translate($$10, $$11, 0.0F);
            $$7.mulPose(Axis.XP.rotationDegrees(float1));
            $$7.mulPose(Axis.YP.rotationDegrees(float2));
            RenderSystem.applyModelViewMatrix();
            for (int $$13 = 0; $$13 < 6; $$13++) {
                RenderSystem.setShaderTexture(0, this.images[$$13]);
                $$5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                int $$14 = Math.round(255.0F * float3) / ($$9 + 1);
                if ($$13 == 0) {
                    $$5.m_5483_(-1.0, -1.0, 1.0).uv(0.0F, 0.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(-1.0, 1.0, 1.0).uv(0.0F, 1.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(1.0, 1.0, 1.0).uv(1.0F, 1.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(1.0, -1.0, 1.0).uv(1.0F, 0.0F).color(255, 255, 255, $$14).endVertex();
                }
                if ($$13 == 1) {
                    $$5.m_5483_(1.0, -1.0, 1.0).uv(0.0F, 0.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(1.0, 1.0, 1.0).uv(0.0F, 1.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(1.0, 1.0, -1.0).uv(1.0F, 1.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(1.0, -1.0, -1.0).uv(1.0F, 0.0F).color(255, 255, 255, $$14).endVertex();
                }
                if ($$13 == 2) {
                    $$5.m_5483_(1.0, -1.0, -1.0).uv(0.0F, 0.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(1.0, 1.0, -1.0).uv(0.0F, 1.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(-1.0, 1.0, -1.0).uv(1.0F, 1.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(-1.0, -1.0, -1.0).uv(1.0F, 0.0F).color(255, 255, 255, $$14).endVertex();
                }
                if ($$13 == 3) {
                    $$5.m_5483_(-1.0, -1.0, -1.0).uv(0.0F, 0.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(-1.0, 1.0, -1.0).uv(0.0F, 1.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(-1.0, 1.0, 1.0).uv(1.0F, 1.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(-1.0, -1.0, 1.0).uv(1.0F, 0.0F).color(255, 255, 255, $$14).endVertex();
                }
                if ($$13 == 4) {
                    $$5.m_5483_(-1.0, -1.0, -1.0).uv(0.0F, 0.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(-1.0, -1.0, 1.0).uv(0.0F, 1.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(1.0, -1.0, 1.0).uv(1.0F, 1.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(1.0, -1.0, -1.0).uv(1.0F, 0.0F).color(255, 255, 255, $$14).endVertex();
                }
                if ($$13 == 5) {
                    $$5.m_5483_(-1.0, 1.0, 1.0).uv(0.0F, 0.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(-1.0, 1.0, -1.0).uv(0.0F, 1.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(1.0, 1.0, -1.0).uv(1.0F, 1.0F).color(255, 255, 255, $$14).endVertex();
                    $$5.m_5483_(1.0, 1.0, 1.0).uv(1.0F, 0.0F).color(255, 255, 255, $$14).endVertex();
                }
                $$4.end();
            }
            $$7.popPose();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.colorMask(true, true, true, false);
        }
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.restoreProjectionMatrix();
        $$7.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
    }

    public CompletableFuture<Void> preload(TextureManager textureManager0, Executor executor1) {
        CompletableFuture<?>[] $$2 = new CompletableFuture[6];
        for (int $$3 = 0; $$3 < $$2.length; $$3++) {
            $$2[$$3] = textureManager0.preload(this.images[$$3], executor1);
        }
        return CompletableFuture.allOf($$2);
    }
}