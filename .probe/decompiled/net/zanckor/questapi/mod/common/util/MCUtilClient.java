package net.zanckor.questapi.mod.common.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.joml.Quaternionf;

@EventBusSubscriber(modid = "questapi", value = { Dist.CLIENT })
public class MCUtilClient {

    public static String properNoun(String text) {
        return text.length() < 1 ? text : text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static List<List<FormattedCharSequence>> splitText(String text, Font font, int textMaxLength) {
        List<List<FormattedCharSequence>> textBlocks = new ArrayList();
        textBlocks.add(font.split(Component.literal(text), textMaxLength));
        return textBlocks;
    }

    public static void playSound(SoundEvent sound, float minPitch, float maxPitch) {
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        soundManager.play(SimpleSoundInstance.forUI(sound, Mth.randomBetween(RandomSource.create(), minPitch, maxPitch)));
    }

    public static void renderLine(GuiGraphics graphics, PoseStack poseStack, float xPos, float yPos, float textIndent, String text, Font font) {
        graphics.drawString(font, text, (int) xPos, (int) yPos, 0, false);
        poseStack.translate(0.0F, textIndent, 0.0F);
    }

    public static void renderLine(GuiGraphics graphics, PoseStack poseStack, int maxTextLength, float xPos, float yPos, float textIndent, String text, Font font) {
        float splitIndent = 0.0F;
        for (List<FormattedCharSequence> line : splitText(text, font, maxTextLength * 5)) {
            for (FormattedCharSequence lineString : line) {
                graphics.drawString(font, lineString, (int) xPos, (int) (yPos + textIndent * (splitIndent / 2.0F)), 0, false);
                splitIndent++;
            }
        }
        poseStack.translate(0.0F, textIndent, 0.0F);
    }

    public static void renderLine(GuiGraphics graphics, PoseStack poseStack, int maxTextLength, float xPos, float yPos, float textIndent, MutableComponent text, Font font) {
        float splitIndent = 0.0F;
        Style style = text.getStyle();
        for (List<FormattedCharSequence> line : splitText(text.getString(), font, maxTextLength * 5)) {
            for (FormattedCharSequence lineString : line) {
                StringBuilder sb = new StringBuilder();
                lineString.accept((index, style1, cp) -> {
                    sb.appendCodePoint(cp);
                    return true;
                });
                graphics.drawString(font, Component.literal(sb.toString()).withStyle(style), (int) xPos, (int) (yPos + textIndent * splitIndent), 0, false);
                splitIndent++;
            }
        }
        poseStack.translate(0.0F, textIndent * splitIndent, 0.0F);
    }

    public static void renderLine(GuiGraphics graphics, PoseStack poseStack, int maxTextLength, float xPos, float yPos, float scale, float textIndent, MutableComponent text, Font font) {
        poseStack.pushPose();
        poseStack.translate(xPos, yPos, 0.0F);
        poseStack.scale(scale, scale, 1.0F);
        float splitIndent = 0.0F;
        Style style = text.getStyle();
        for (List<FormattedCharSequence> line : splitText(text.getString(), font, maxTextLength * 5)) {
            for (FormattedCharSequence lineString : line) {
                StringBuilder sb = new StringBuilder();
                lineString.accept((index, style1, cp) -> {
                    sb.appendCodePoint(cp);
                    return true;
                });
                graphics.drawString(font, Component.literal(sb.toString()).withStyle(style), 0, (int) (textIndent * splitIndent), 0, false);
                splitIndent++;
            }
        }
        poseStack.translate(0.0F, textIndent * splitIndent, 0.0F);
        poseStack.popPose();
    }

    public static void renderLine(GuiGraphics graphics, PoseStack poseStack, float xPos, float yPos, float textIndent, MutableComponent text, Font font) {
        graphics.drawString(font, text, (int) xPos, (int) yPos, 0, false);
        poseStack.translate(0.0F, textIndent, 0.0F);
    }

    public static void renderLine(GuiGraphics graphics, PoseStack poseStack, float xPos, float yPos, float textIndent, MutableComponent text1, MutableComponent text2, Font font) {
        graphics.drawString(font, text1 + " " + text2, (int) xPos, (int) yPos, 0, false);
        poseStack.translate(0.0F, textIndent, 0.0F);
    }

    public static void renderLines(GuiGraphics graphics, PoseStack poseStack, float textIndent, int paragraphIndent, int textMaxLength, String text, Font font) {
        float splitIndent = 0.0F;
        for (List<FormattedCharSequence> line : splitText(text, font, textMaxLength * 5)) {
            for (FormattedCharSequence lineString : line) {
                graphics.drawString(font, lineString, 0, (int) (textIndent * (splitIndent / 2.0F)), 0, false);
                splitIndent++;
            }
        }
        poseStack.translate(0.0F, (float) paragraphIndent, 0.0F);
    }

    public static void renderLines(GuiGraphics graphics, PoseStack poseStack, float textIndent, int paragraphIndent, int textMaxLength, Component text, Font font) {
        float splitIndent = 0.0F;
        Style style = text.getStyle();
        for (List<FormattedCharSequence> line : splitText(String.valueOf(text), font, textMaxLength * 5)) {
            for (FormattedCharSequence lineString : line) {
                graphics.drawString(font, lineString, 0, (int) (textIndent * (splitIndent / 2.0F)), 0, false);
                splitIndent++;
            }
        }
        poseStack.translate(0.0F, (float) paragraphIndent, 0.0F);
    }

    public static void renderText(GuiGraphics graphics, PoseStack poseStack, double xPosition, double yPosition, float textIndent, float scale, int textMaxLength, String text, Font font) {
        float splitIndent = 0.0F;
        List<List<FormattedCharSequence>> splintedText = splitText(text, font, textMaxLength * 5);
        poseStack.pushPose();
        poseStack.translate(xPosition, yPosition, 0.0);
        poseStack.scale(scale, scale, 1.0F);
        for (List<FormattedCharSequence> line : splintedText) {
            for (FormattedCharSequence lineString : line) {
                graphics.drawString(font, lineString, 0, (int) (textIndent * (splitIndent / 2.0F)), 0, false);
                splitIndent++;
            }
        }
        poseStack.popPose();
    }

    public static void renderText(GuiGraphics graphics, PoseStack poseStack, double width, double height, float textIndent, float scale, int textMaxLength, List<String> text, Font font) {
        if (text != null) {
            float splitIndent = 0.0F;
            if (text.size() > 6) {
                for (int i = 0; i < text.size() - 6; i++) {
                    scale = (float) ((double) scale * 0.85);
                }
            }
            poseStack.pushPose();
            poseStack.translate(width, height, 0.0);
            poseStack.scale(scale, scale, 1.0F);
            for (String s : text) {
                for (List<FormattedCharSequence> textBlock : splitText(s, font, 5 * textMaxLength)) {
                    for (FormattedCharSequence line : textBlock) {
                        if (splitIndent < 2.0F) {
                            graphics.drawString(font, line, 0, (int) (textIndent * (splitIndent / 2.0F)), 0, false);
                            splitIndent++;
                        }
                        if (textBlock.size() >= 2 && ((FormattedCharSequence) textBlock.get(textBlock.size() - 1)).equals(line)) {
                            poseStack.translate(0.0F, textIndent / 2.0F, 0.0F);
                        }
                    }
                    splitIndent++;
                }
                splitIndent = 0.0F;
            }
            poseStack.popPose();
        }
    }

    public static MutableComponent formatString(String text1, String text2, ChatFormatting chatFormatting1, ChatFormatting chatFormatting2) {
        return Component.literal(text1).withStyle(chatFormatting1).append(Component.literal(text2).withStyle(chatFormatting2));
    }

    public static Entity getEntityByUUID(UUID uuid) {
        for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
            if (entity.getUUID().equals(uuid)) {
                return entity;
            }
        }
        return null;
    }

    public static void renderEntity(double xPos, double yPos, double size, double xRot, double yRot, LivingEntity entity) {
        float f = (float) Math.atan(xRot / 40.0);
        float f1 = (float) Math.atan(yRot / 40.0);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        entity.getPersistentData().putBoolean("beingRenderedOnInventory", true);
        posestack.translate(xPos, yPos, 1050.0);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0, 0.0, 1000.0);
        posestack1.scale((float) size, (float) size, (float) size);
        Quaternionf quaternion = new Quaternionf().rotateZ((float) Math.toRadians(180.0));
        posestack1.mulPose(quaternion);
        float f2 = entity.yBodyRot;
        float f3 = entity.m_146908_();
        float f4 = entity.m_146909_();
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;
        entity.yBodyRot = 180.0F;
        entity.m_146922_(180.0F + f * 40.0F);
        entity.m_146926_(-f1 * 20.0F);
        entity.yHeadRot = entity.m_146908_();
        entity.yHeadRotO = entity.m_146908_();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityrenderdispatcher.setRenderShadow(false);
        entity.m_20340_(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880));
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        entity.yBodyRot = f2;
        entity.m_146922_(f3);
        entity.m_146926_(f4);
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;
        entity.getPersistentData().putBoolean("beingRenderedOnInventory", false);
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    public static void renderEntity(double xPos, double yPos, double size, double rotation, LivingEntity entity, PoseStack poseStack) {
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.last().pose().mul(poseStack.last().pose());
        posestack.translate(xPos, yPos, 1050.0);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0, 0.0, 1000.0);
        posestack1.scale((float) size, (float) size, (float) size);
        posestack1.mulPose(new Quaternionf().rotateXYZ((float) Math.toRadians(rotation), (float) Math.toRadians(90.0), (float) Math.toRadians(180.0)));
        float f2 = entity.yBodyRot;
        float f3 = entity.m_146908_();
        float f4 = entity.m_146909_();
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;
        entity.yBodyRot = 180.0F;
        entity.m_146922_(180.0F);
        entity.yHeadRot = entity.m_146908_();
        entity.yHeadRotO = entity.m_146908_();
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityrenderdispatcher.setRenderShadow(false);
        entity.m_20340_(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880));
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        entity.yBodyRot = f2;
        entity.m_146922_(f3);
        entity.m_146926_(f4);
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    public static void renderItem(ItemStack itemStack, int xPos, int yPos, double size, double rotation, PoseStack poseStack) {
        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(itemStack, null, null, 0);
        Quaternionf quaternion = new Quaternionf().rotateZ((float) Math.toRadians(rotation * 10.0));
        Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.last().pose().mul(poseStack.last().pose());
        posestack.translate((float) xPos, (float) yPos, 0.0F);
        posestack.scale(1.0F, -1.0F, 1.0F);
        posestack.scale(16.0F, 16.0F, 16.0F);
        posestack.scale((float) size, (float) size, (float) size);
        posestack.mulPose(quaternion);
        RenderSystem.applyModelViewMatrix();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        Lighting.setupForFlatItems();
        Minecraft.getInstance().getItemRenderer().render(itemStack, ItemDisplayContext.GUI, false, new PoseStack(), multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, model);
        multibuffersource$buffersource.endBatch();
        RenderSystem.enableDepthTest();
        Lighting.setupFor3DItems();
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    public static Button createButton(int xPos, int yPos, int width, int height, Component component, Button.OnPress onPress) {
        Button button = Button.builder(component, onPress).build();
        button.m_264152_(xPos, yPos);
        button.m_93674_(width);
        button.setHeight(height);
        return button;
    }
}