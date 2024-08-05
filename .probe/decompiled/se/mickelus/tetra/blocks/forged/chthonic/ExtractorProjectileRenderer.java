package se.mickelus.tetra.blocks.forged.chthonic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class ExtractorProjectileRenderer extends EntityRenderer<ExtractorProjectileEntity> {

    private static BlockRenderDispatcher blockRenderer;

    public ExtractorProjectileRenderer(EntityRendererProvider.Context renderContext) {
        super(renderContext);
        blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    public void render(ExtractorProjectileEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource renderTypeBuffer, int packedLightIn) {
        matrixStack.pushPose();
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.f_19859_, entity.m_146908_()) - 90.0F));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.f_19860_, entity.m_146909_()) + 90.0F));
        matrixStack.translate(-0.3F, -0.1F, -0.45F);
        BakedModel model = blockRenderer.getBlockModelShaper().getBlockModel(ChthonicExtractorBlock.instance.m_49966_());
        blockRenderer.getModelRenderer().renderModel(matrixStack.last(), renderTypeBuffer.getBuffer(Sheets.solidBlockSheet()), ChthonicExtractorBlock.instance.m_49966_(), model, 1.0F, 1.0F, 1.0F, packedLightIn, OverlayTexture.NO_OVERLAY);
        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, renderTypeBuffer, packedLightIn);
    }

    public ResourceLocation getTextureLocation(ExtractorProjectileEntity entity) {
        return null;
    }
}