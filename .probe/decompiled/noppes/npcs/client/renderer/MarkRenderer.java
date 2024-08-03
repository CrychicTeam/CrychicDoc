package noppes.npcs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.shared.client.model.Model2DRenderer;

public class MarkRenderer {

    public static final ResourceLocation markExclamation = new ResourceLocation("customnpcs", "textures/marks/exclamation.png");

    public static final ResourceLocation markQuestion = new ResourceLocation("customnpcs", "textures/marks/question.png");

    public static final ResourceLocation markPointer = new ResourceLocation("customnpcs", "textures/marks/pointer.png");

    public static final ResourceLocation markCross = new ResourceLocation("customnpcs", "textures/marks/cross.png");

    public static final ResourceLocation markSkull = new ResourceLocation("customnpcs", "textures/marks/skull.png");

    public static final ResourceLocation markStar = new ResourceLocation("customnpcs", "textures/marks/star.png");

    public static int displayList = -1;

    public static Model2DRenderer renderer = new Model2DRenderer(0, 0, 32, 32, 32, 32, markExclamation);

    public static void render(RenderLivingEvent.Post event, MarkData.Mark mark) {
        PoseStack matrixStack = event.getPoseStack();
        matrixStack.pushPose();
        int color = mark.color;
        float red = (float) (color >> 16 & 0xFF) / 255.0F;
        float green = (float) (color >> 8 & 0xFF) / 255.0F;
        float blue = (float) (color & 0xFF) / 255.0F;
        ResourceLocation location = markExclamation;
        if (mark.type == 1) {
            location = markQuestion;
        } else if (mark.type == 3) {
            location = markPointer;
        } else if (mark.type == 5) {
            location = markCross;
        } else if (mark.type == 4) {
            location = markSkull;
        } else if (mark.type == 6) {
            location = markStar;
        }
        matrixStack.translate(0.0, (double) event.getEntity().m_20206_() + 0.6, 0.0);
        matrixStack.mulPose(Axis.XN.rotationDegrees(180.0F));
        matrixStack.mulPose(Axis.YP.rotationDegrees(event.getEntity().yHeadRot));
        matrixStack.translate(-0.5F, 0.0F, 0.0F);
        renderer.render(location, matrixStack, event.getMultiBufferSource().getBuffer(RenderType.entityCutout(location)), event.getPackedLight(), OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
        matrixStack.popPose();
    }
}