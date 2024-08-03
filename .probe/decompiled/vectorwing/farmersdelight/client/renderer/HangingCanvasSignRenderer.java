package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModAtlases;

public class HangingCanvasSignRenderer extends CanvasSignRenderer {

    private static final Vec3 TEXT_OFFSET = new Vec3(0.0, -0.32F, 0.073F);

    private final HangingSignRenderer.HangingSignModel signModel;

    public HangingCanvasSignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.signModel = new HangingSignRenderer.HangingSignModel(context.bakeLayer(ModelLayers.createHangingSignModelName(WoodType.SPRUCE)));
    }

    @Override
    public float getSignModelRenderScale() {
        return 1.0F;
    }

    @Override
    public float getSignTextRenderScale() {
        return 0.8F;
    }

    @Override
    public void render(SignBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.m_58900_();
        SignBlock block = (SignBlock) state.m_60734_();
        HangingSignRenderer.HangingSignModel model = this.signModel;
        model.evaluateVisibleParts(state);
        DyeColor dye = null;
        if (block instanceof CanvasSign canvasSign) {
            dye = canvasSign.getBackgroundColor();
        }
        this.renderSignWithText(blockEntity, poseStack, bufferSource, packedLight, packedOverlay, state, block, dye, model);
    }

    @Override
    protected void translateSign(PoseStack poseStack, float angle, BlockState state) {
        poseStack.translate(0.5, 0.9375, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        poseStack.translate(0.0F, -0.3125F, 0.0F);
    }

    @Override
    protected void renderSignModel(PoseStack poseStack, int packedLight, int packedOverlay, Model model, VertexConsumer vertexConsumer) {
        HangingSignRenderer.HangingSignModel hangingSignModel = (HangingSignRenderer.HangingSignModel) model;
        hangingSignModel.root.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }

    @Override
    public Material getCanvasSignMaterial(@Nullable DyeColor dyeColor) {
        return ModAtlases.getHangingCanvasSignMaterial(dyeColor);
    }

    @Override
    public int getCustomVerticalOffset() {
        return 0;
    }

    @Override
    Vec3 getTextOffset() {
        return TEXT_OFFSET;
    }
}