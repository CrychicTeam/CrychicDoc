package com.mna.tools.render;

import com.google.common.collect.UnmodifiableIterator;
import com.mna.ManaAndArtifice;
import com.mna.api.faction.IFaction;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.gui.GuiTextures;
import com.mna.recipes.AMRecipeBase;
import com.mna.recipes.RecipeByproduct;
import com.mna.tools.math.MathUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class GuiRenderUtils {

    static final int POINT_RENDER_SIZE = 13;

    private static final ResourceLocation silver_border = new ResourceLocation("mna", "textures/spell/silver_spell_border.png");

    public static void renderManaweavePattern(GuiGraphics pGuiGraphics, int x, int y, float scale, IManaweavePattern p) {
        if (p != null) {
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(scale, scale, scale);
            byte[][] pData = p.get();
            float pointSize = 13.0F;
            for (int k = 0; k < pData.length; k++) {
                for (int j = 0; j < pData[k].length; j++) {
                    if (pData[k][j] != 0) {
                        pGuiGraphics.blit(GuiTextures.Widgets.GUIDE_WIDGETS, x - (int) Math.floor((double) ((float) j * pointSize)), y + (int) Math.floor((double) ((float) k * pointSize)), 0, 0.0F, 0.0F, 13, 13, 256, 256);
                    }
                }
            }
            pGuiGraphics.pose().popPose();
        }
    }

    public static void renderStandardPlayerInventory(GuiGraphics pGuiGraphics, int centerX, int centerY) {
        pGuiGraphics.blit(GuiTextures.Widgets.STANDALONE_INVENTORY_TEXTURE, centerX - 88, centerY - 45, 0.0F, 0.0F, 176, 90, 176, 90);
    }

    public static void line2d(GuiGraphics pGuiGraphics, float src_x, float src_y, float dst_x, float dst_y, float zLevel, int color) {
        line2d(pGuiGraphics, src_x, src_y, dst_x, dst_y, zLevel, 2.0F, color);
    }

    public static void line2d(GuiGraphics pGuiGraphics, float src_x, float src_y, float dst_x, float dst_y, float zLevel, float weight, int color) {
        lineStrip2d(pGuiGraphics, new Vec2[] { new Vec2(src_x, src_y), new Vec2(dst_x, dst_y) }, zLevel, weight, color, color);
    }

    public static void bezierLineBetween(GuiGraphics pGuiGraphics, float src_x, float src_y, float dst_x, float dst_y, float zLevel, float weight, int startColor, int endColor) {
        bezierLineBetween(pGuiGraphics, src_x, src_y, dst_x, dst_y, zLevel, weight, startColor, endColor, true);
    }

    public static void renderSilverSpellBorder(GuiGraphics pGuiGraphics, int x, int y, int width, int height) {
        RenderSystem.enableBlend();
        int frameWidth = 16;
        int frameHeight = 512;
        int frameSize = 16;
        int frame = (int) (ManaAndArtifice.instance.proxy.getClientWorld().getGameTime() % 32L);
        float framePct = (float) frame / 32.0F;
        pGuiGraphics.blit(silver_border, x, y, width, height, 0.0F, framePct * (float) frameHeight, frameSize, frameSize, frameWidth, frameHeight);
    }

    public static void bezierLineBetween(GuiGraphics pGuiGraphics, float src_x, float src_y, float dst_x, float dst_y, float zLevel, float weight, int startColor, int endColor, boolean smartDouble) {
        Vec3 start = new Vec3((double) src_x, (double) src_y, 0.0);
        Vec3 end = new Vec3((double) dst_x, (double) dst_y, 0.0);
        Vec3 delta = end.subtract(start);
        int num_points = 40;
        Vec3[] points = new Vec3[num_points + 1];
        if (dst_x < src_x && smartDouble) {
            float pct_per_point = 1.0F / (float) (num_points / 2);
            float midScale = delta.y < 0.0 ? 0.3F : 0.6F;
            Vec3 mid = start.add(delta.scale((double) midScale));
            double offsetX = Math.max(Math.min(delta.x / 1.5, -10.0), -100.0);
            double offsetY = delta.y < 75.0 ? delta.y / -4.0 : 0.0;
            Vec3 seg_a_control_1 = start.subtract(offsetX, 0.0, 0.0);
            Vec3 seg_a_control_2 = mid.subtract(offsetX, offsetY, 0.0);
            Vec3 seg_b_control_1 = mid.add(offsetX / 2.0, offsetY / 4.0, 0.0);
            Vec3 seg_b_control_2 = end.add(offsetX / 2.0, 0.0, 0.0);
            for (int i = 0; i < num_points; i++) {
                points[i] = MathUtils.bezierVector3d(start, mid, seg_a_control_1, seg_a_control_2, (float) i * pct_per_point);
            }
            for (int i = 0; i < num_points / 2; i++) {
                points[i + num_points / 2] = MathUtils.bezierVector3d(mid, end, seg_b_control_1, seg_b_control_2, (float) i * pct_per_point);
            }
        } else {
            float pct_per_point = 1.0F / (float) num_points;
            Vec3 seg_a_control_1 = start.add(20.0, 0.0, 0.0);
            Vec3 seg_a_control_2 = end.subtract(20.0, 0.0, 0.0);
            for (int i = 0; i < num_points; i++) {
                points[i] = MathUtils.bezierVector3d(start, end, seg_a_control_1, seg_a_control_2, (float) i * pct_per_point);
            }
        }
        points[num_points] = end;
        Vec2[] converted = new Vec2[points.length];
        for (int i = 0; i < points.length; i++) {
            converted[i] = new Vec2((float) points[i].x, (float) points[i].y);
        }
        lineStrip2d(pGuiGraphics, converted, zLevel, weight, startColor, endColor);
    }

    public static void lineStrip2d(GuiGraphics pGuiGraphics, Vec2[] points, float zLevel, float weight, int startColor, int endColor) {
        if (points.length >= 2) {
            RenderSystem.setShader(GameRenderer::m_172757_);
            GlStateManager._depthMask(false);
            GlStateManager._disableCull();
            Tesselator tesselator = RenderSystem.renderThreadTesselator();
            BufferBuilder buffer = tesselator.getBuilder();
            RenderSystem.lineWidth(weight);
            buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
            float pctPerColor = 1.0F / (float) points.length;
            for (int i = 0; i < points.length - 1; i++) {
                Vec2 src = points[i];
                Vec2 dst = points[i + 1];
                int colorIndex = (int) ((long) i - ManaAndArtifice.instance.proxy.getGameTicks()) % (points.length - 1);
                if (colorIndex < 0) {
                    colorIndex += points.length - 1;
                }
                int color1 = MathUtils.lerpColor(startColor, endColor, pctPerColor * (float) colorIndex);
                int color2 = MathUtils.lerpColor(startColor, endColor, pctPerColor * (float) (colorIndex + 1));
                float angle = (float) Math.abs(Math.sin(Math.atan2((double) (dst.x - src.x), (double) (dst.y - src.y))));
                Vector3f nrm = (double) angle > 0.5 ? new Vector3f(1.0F, 0.0F, 0.0F) : new Vector3f(0.0F, 1.0F, 0.0F);
                buffer.m_252986_(pGuiGraphics.pose().last().pose(), src.x, src.y, zLevel).color(color1).normal(nrm.x(), nrm.y(), nrm.z()).endVertex();
                buffer.m_252986_(pGuiGraphics.pose().last().pose(), dst.x, dst.y, zLevel).color(color2).normal(nrm.x(), nrm.y(), nrm.z()).endVertex();
            }
            tesselator.end();
            RenderSystem.lineWidth(1.0F);
            GlStateManager._enableCull();
            GlStateManager._depthMask(true);
        }
    }

    public static void gradientline2d(GuiGraphics pGuiGraphics, float src_x, float src_y, float dst_x, float dst_y, float zLevel, int color1, int color2) {
        lineStrip2d(pGuiGraphics, new Vec2[] { new Vec2(src_x, src_y), new Vec2(dst_x, dst_y) }, zLevel, 2.0F, color1, color2);
    }

    public static void fractalLine2d(GuiGraphics pGuiGraphics, int src_x, int src_y, int dst_x, int dst_y, float zLevel, int color, float displace) {
        fractalLine2d(pGuiGraphics, src_x, src_y, dst_x, dst_y, zLevel, color, displace, 2.0F);
    }

    public static void fractalLine2d(GuiGraphics pGuiGraphics, int src_x, int src_y, int dst_x, int dst_y, float zLevel, int color, float displace, float fractalDetail) {
        Random rand = new Random();
        if (displace < fractalDetail) {
            line2d(pGuiGraphics, (float) src_x, (float) src_y, (float) dst_x, (float) dst_y, zLevel, color);
        } else {
            int mid_x = (dst_x + src_x) / 2;
            int mid_y = (dst_y + src_y) / 2;
            mid_x = (int) ((double) mid_x + ((double) rand.nextFloat() - 0.5) * (double) displace);
            mid_y = (int) ((double) mid_y + ((double) rand.nextFloat() - 0.5) * (double) displace);
            fractalLine2d(pGuiGraphics, src_x, src_y, mid_x, mid_y, zLevel, color, displace / 2.0F, fractalDetail);
            fractalLine2d(pGuiGraphics, dst_x, dst_y, mid_x, mid_y, zLevel, color, displace / 2.0F, fractalDetail);
        }
    }

    public static void renderFactionIcon(GuiGraphics pGuiGraphics, IFaction faction, int x, int y) {
        if (faction != null) {
            pGuiGraphics.blit(faction.getFactionIcon(), x, y, 0.0F, 0.0F, faction.getFactionIconTextureSize(), faction.getFactionIconTextureSize(), faction.getFactionIconTextureSize(), faction.getFactionIconTextureSize());
        }
    }

    public static void renderByproducts(GuiGraphics pGuiGraphics, int posX, int posY, AMRecipeBase recipe, boolean plusOnLeft) {
        if (recipe.getByproducts().size() != 0) {
            Minecraft minecraft = Minecraft.getInstance();
            RenderSystem.enableBlend();
            pGuiGraphics.blit(GuiTextures.Widgets.GUIDE_WIDGETS, posX + 2, posY, 44, 44, 39.0F, 0.0F, 50, 50, 256, 256);
            if (plusOnLeft) {
                pGuiGraphics.drawString(minecraft.font, "+", -1 + posX - minecraft.font.width("+") / 2, 34 + posY - 9, -12105913, false);
            } else {
                pGuiGraphics.drawString(minecraft.font, "+", posX + 25 - minecraft.font.width("+") / 2, posY + 2 - 9, -12105913, false);
            }
            int x = 0;
            int y = 0;
            int count = 0;
            int step = 24;
            float scale_text = 0.7F;
            float scale_item = 0.7F;
            int item_offset_x = 7;
            UnmodifiableIterator var13 = recipe.getByproducts().iterator();
            while (var13.hasNext()) {
                RecipeByproduct byproduct = (RecipeByproduct) var13.next();
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().scale(0.7F, 0.7F, 0.7F);
                pGuiGraphics.renderItem(byproduct.stack, (int) ((float) (posX + x + 7) / 0.7F), (int) ((float) (posY + y + 2) / 0.7F));
                pGuiGraphics.pose().popPose();
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().scale(0.7F, 0.7F, 0.7F);
                pGuiGraphics.drawString(minecraft.font, String.format("%.0f%%", byproduct.chance * 100.0F), (int) ((float) (posX + x + 22) / 0.7F), (int) ((float) (posY + y + 15 - 9) / 0.7F), -12105913, false);
                pGuiGraphics.pose().popPose();
                y += 24;
                if (++count > 6) {
                    break;
                }
            }
            RenderSystem.disableBlend();
        }
    }
}