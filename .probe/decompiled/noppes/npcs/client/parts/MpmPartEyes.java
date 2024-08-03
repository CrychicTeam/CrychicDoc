package noppes.npcs.client.parts;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.ModelEyeData;
import noppes.npcs.constants.BodyPart;
import noppes.npcs.shared.client.model.ModelPlaneRenderer;
import noppes.npcs.shared.client.model.NopModelPart;
import noppes.npcs.shared.common.util.EasingFunctions;
import noppes.npcs.shared.common.util.NopVector3f;

public class MpmPartEyes extends MpmPartAbstractClient {

    private static final ResourceLocation glint = new ResourceLocation("moreplayermodels", "textures/parts/eyes/glint.png");

    private static final ResourceLocation brows = new ResourceLocation("moreplayermodels", "textures/parts/eyes/brows.png");

    private static final ResourceLocation pupils = new ResourceLocation("moreplayermodels", "textures/parts/eyes/pupils.png");

    private static final ResourceLocation sclera = new ResourceLocation("moreplayermodels", "textures/parts/eyes/sclera.png");

    private static final NopModelPart sclera1 = new ModelPlaneRenderer(64, 64, 9, 12).addPlane(-1.0F, -1.0F, 0.0F, 2, 1, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(-2.0F, -3.0F, -4.002F));

    private static final NopModelPart sclera2 = new ModelPlaneRenderer(64, 64, 13, 12).addPlane(-1.0F, -1.0F, 0.0F, 2, 1, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(2.0F, -3.0F, -4.002F));

    private static final NopModelPart sclera1M = new ModelPlaneRenderer(64, 64, 9, 12).mirror(true).addPlane(-1.0F, -1.0F, 0.0F, 2, 1, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(-2.0F, -3.0F, -4.002F));

    private static final NopModelPart sclera2M = new ModelPlaneRenderer(64, 64, 13, 12).mirror(true).addPlane(-1.0F, -1.0F, 0.0F, 2, 1, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(2.0F, -3.0F, -4.002F));

    private static final NopModelPart scleraBig1 = new ModelPlaneRenderer(64, 64, 9, 12).addPlane(-1.0F, -1.0F, 0.0F, 2, 2, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(-2.0F, -3.0F, -4.002F));

    private static final NopModelPart scleraBig2 = new ModelPlaneRenderer(64, 64, 13, 12).addPlane(-1.0F, -1.0F, 0.0F, 2, 2, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(2.0F, -3.0F, -4.002F));

    private static final NopModelPart scleraBig1M = new ModelPlaneRenderer(64, 64, 9, 12).mirror(true).addPlane(-1.0F, -1.0F, 0.0F, 2, 2, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(-2.0F, -3.0F, -4.002F));

    private static final NopModelPart scleraBig2M = new ModelPlaneRenderer(64, 64, 13, 12).mirror(true).addPlane(-1.0F, -1.0F, 0.0F, 2, 2, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(2.0F, -3.0F, -4.002F));

    private static final NopModelPart pupils1 = new ModelPlaneRenderer(64, 64, 10, 12).addPlane(-1.0F, -1.0F, 0.0F, 1, 1, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(-1.0F, -3.0F, -4.004F));

    private static final NopModelPart pupils2 = new ModelPlaneRenderer(64, 64, 13, 12).addPlane(-1.0F, -1.0F, 0.0F, 1, 1, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(2.0F, -3.0F, -4.004F));

    private static final NopModelPart pupilsBig1 = new ModelPlaneRenderer(64, 64, 10, 12).addPlane(-1.0F, -1.0F, 0.0F, 1, 2, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(-1.0F, -3.0F, -4.004F));

    private static final NopModelPart pupilsBig2 = new ModelPlaneRenderer(64, 64, 13, 12).addPlane(-1.0F, -1.0F, 0.0F, 1, 2, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(2.0F, -3.0F, -4.004F));

    private static final NopModelPart glint1 = new ModelPlaneRenderer(64, 64, 10, 12).addPlane(-0.5F, -0.5F, 0.0F, 1, 1, new NopVector3f(0.6F, 0.6F, 0.6F), Direction.NORTH).setPos(new NopVector3f(-1.4F, -3.44F, -4.006F));

    private static final NopModelPart glint2 = new ModelPlaneRenderer(64, 64, 13, 12).addPlane(-0.5F, -0.5F, 0.0F, 1, 1, new NopVector3f(0.6F, 0.6F, 0.6F), Direction.NORTH).setPos(new NopVector3f(1.6F, -3.44F, -4.006F));

    private static final NopModelPart lid1 = new ModelPlaneRenderer(64, 64, 9, 11).addPlane(-1.0F, 0.0F, 0.0F, 2, 1, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(-2.0F, -4.0F, -4.008F));

    private static final NopModelPart lid2 = new ModelPlaneRenderer(64, 64, 13, 11).addPlane(-1.0F, 0.0F, 0.0F, 2, 1, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(2.0F, -4.0F, -4.008F));

    private static final NopModelPart brows1 = new ModelPlaneRenderer(64, 64, 9, 11).addPlane(-1.0F, -1.0F, 0.0F, 2, 1, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(-2.0F, -4.0F, -4.01F));

    private static final NopModelPart brows2 = new ModelPlaneRenderer(64, 64, 13, 11).addPlane(-1.0F, -1.0F, 0.0F, 2, 1, NopVector3f.ONE, Direction.NORTH).setPos(new NopVector3f(2.0F, -4.0F, -4.01F));

    public int type;

    public MpmPartEyes(int type, ResourceLocation id) {
        this.type = type;
        this.id = id;
        this.menu = "part.buildin";
        this.name = "Eyes";
        this.bodyPart = BodyPart.HEAD;
        this.hiddenParts = new ArrayList();
        this.isEnabled = true;
        this.author = "Noppes";
    }

    @Override
    public void render(MpmPartData data, PoseStack mStack, MultiBufferSource typeBuffer, int lightmapUV, LivingEntity player) {
        ModelEyeData eyeData = (ModelEyeData) data;
        mStack.pushPose();
        mStack.translate((float) ((ModelEyeData) data).eyePos.x * -0.0625F, (float) ((ModelEyeData) data).eyePos.y * -0.0625F, 0.0F);
        float offset = 0.0F;
        if (eyeData.blinkStart > 0L && player.isAlive()) {
            float f = (float) (System.currentTimeMillis() - eyeData.blinkStart) / 150.0F;
            if (f > 1.0F) {
                f = 2.0F - f;
            }
            if (f < 0.0F) {
                eyeData.blinkStart = 0L;
                f = 0.0F;
            }
            offset = (float) (eyeData.eyeSize + 1) * EasingFunctions.easeInCubic(f);
        }
        if (this.type == 0 || this.type == 1) {
            if (eyeData.skinType == 1) {
                (eyeData.eyeSize == 0 ? sclera1 : scleraBig1).render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(sclera)), lightmapUV, OverlayTexture.NO_OVERLAY);
            } else if (eyeData.skinType == 2) {
                if (eyeData.mirror) {
                    (eyeData.eyeSize == 0 ? sclera1M : scleraBig1M).render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(eyeData.getUrlTexture())), lightmapUV, OverlayTexture.NO_OVERLAY);
                } else {
                    (eyeData.eyeSize == 0 ? sclera1 : scleraBig1).render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(eyeData.getUrlTexture())), lightmapUV, OverlayTexture.NO_OVERLAY);
                }
            }
            if (eyeData.mirror) {
                mStack.translate(-0.0625, 0.0, 0.0);
            }
            if (eyeData.skinType == 1) {
                (eyeData.eyeSize == 0 ? pupils1 : pupilsBig1).render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(pupils)), lightmapUV, OverlayTexture.NO_OVERLAY, eyeData.color.x, eyeData.color.y, eyeData.color.z, 1.0F);
            }
            if (eyeData.glint) {
                glint1.render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(glint)), lightmapUV, OverlayTexture.NO_OVERLAY);
            }
            if (eyeData.mirror) {
                mStack.translate(0.0625, 0.0, 0.0);
            }
            if (offset > 0.0F) {
                lid1.scale = new NopVector3f(1.0F, offset, 1.0F);
                lid1.render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(brows)), lightmapUV, OverlayTexture.NO_OVERLAY, eyeData.lidColor.x, eyeData.lidColor.y, eyeData.lidColor.z, 1.0F);
            }
        }
        mStack.translate((float) ((ModelEyeData) data).eyePos.x * 0.0625F * 2.0F, 0.0F, 0.0F);
        if (this.type == 0 || this.type == 2) {
            if (eyeData.skinType == 1) {
                (eyeData.eyeSize == 0 ? sclera2 : scleraBig2).render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(sclera)), lightmapUV, OverlayTexture.NO_OVERLAY);
            } else if (eyeData.skinType == 2) {
                if (eyeData.mirror) {
                    (eyeData.eyeSize == 0 ? sclera2M : scleraBig2M).render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(eyeData.getUrlTexture())), lightmapUV, OverlayTexture.NO_OVERLAY);
                } else {
                    (eyeData.eyeSize == 0 ? sclera2 : scleraBig2).render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(eyeData.getUrlTexture())), lightmapUV, OverlayTexture.NO_OVERLAY);
                }
            }
            if (eyeData.mirror) {
                mStack.translate(0.0625, 0.0, 0.0);
            }
            if (eyeData.skinType == 1) {
                (eyeData.eyeSize == 0 ? pupils2 : pupilsBig2).render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(pupils)), lightmapUV, OverlayTexture.NO_OVERLAY, eyeData.color.x, eyeData.color.y, eyeData.color.z, 1.0F);
            }
            if (eyeData.glint) {
                glint2.render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(glint)), lightmapUV, OverlayTexture.NO_OVERLAY);
            }
            if (eyeData.mirror) {
                mStack.translate(-0.0625, 0.0, 0.0);
            }
            if (offset > 0.0F) {
                lid2.scale = new NopVector3f(1.0F, offset, 1.0F);
                lid2.render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(brows)), lightmapUV, OverlayTexture.NO_OVERLAY, eyeData.lidColor.x, eyeData.lidColor.y, eyeData.lidColor.z, 1.0F);
            }
        }
        mStack.pushPose();
        mStack.translate(0.0F, offset * 0.0625F, 0.0F);
        if (this.type == 0 || this.type == 2) {
            brows2.scale = eyeData.browThickness;
            brows2.render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(brows)), lightmapUV, OverlayTexture.NO_OVERLAY, eyeData.browColor.x, eyeData.browColor.y, eyeData.browColor.z, 1.0F);
        }
        mStack.translate((float) ((ModelEyeData) data).eyePos.x * -0.0625F * 2.0F, 0.0F, 0.0F);
        if (this.type == 0 || this.type == 1) {
            brows1.scale = eyeData.browThickness;
            brows1.render(mStack, typeBuffer.getBuffer(RenderType.entityTranslucent(brows)), lightmapUV, OverlayTexture.NO_OVERLAY, eyeData.browColor.x, eyeData.browColor.y, eyeData.browColor.z, 1.0F);
        }
        mStack.popPose();
        mStack.popPose();
    }
}