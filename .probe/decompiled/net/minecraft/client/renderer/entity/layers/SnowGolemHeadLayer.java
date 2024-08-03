package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SnowGolemHeadLayer extends RenderLayer<SnowGolem, SnowGolemModel<SnowGolem>> {

    private final BlockRenderDispatcher blockRenderer;

    private final ItemRenderer itemRenderer;

    public SnowGolemHeadLayer(RenderLayerParent<SnowGolem, SnowGolemModel<SnowGolem>> renderLayerParentSnowGolemSnowGolemModelSnowGolem0, BlockRenderDispatcher blockRenderDispatcher1, ItemRenderer itemRenderer2) {
        super(renderLayerParentSnowGolemSnowGolemModelSnowGolem0);
        this.blockRenderer = blockRenderDispatcher1;
        this.itemRenderer = itemRenderer2;
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, SnowGolem snowGolem3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (snowGolem3.hasPumpkin()) {
            boolean $$10 = Minecraft.getInstance().shouldEntityAppearGlowing(snowGolem3) && snowGolem3.m_20145_();
            if (!snowGolem3.m_20145_() || $$10) {
                poseStack0.pushPose();
                ((SnowGolemModel) this.m_117386_()).getHead().translateAndRotate(poseStack0);
                float $$11 = 0.625F;
                poseStack0.translate(0.0F, -0.34375F, 0.0F);
                poseStack0.mulPose(Axis.YP.rotationDegrees(180.0F));
                poseStack0.scale(0.625F, -0.625F, -0.625F);
                ItemStack $$12 = new ItemStack(Blocks.CARVED_PUMPKIN);
                if ($$10) {
                    BlockState $$13 = Blocks.CARVED_PUMPKIN.defaultBlockState();
                    BakedModel $$14 = this.blockRenderer.getBlockModel($$13);
                    int $$15 = LivingEntityRenderer.getOverlayCoords(snowGolem3, 0.0F);
                    poseStack0.translate(-0.5F, -0.5F, -0.5F);
                    this.blockRenderer.getModelRenderer().renderModel(poseStack0.last(), multiBufferSource1.getBuffer(RenderType.outline(TextureAtlas.LOCATION_BLOCKS)), $$13, $$14, 0.0F, 0.0F, 0.0F, int2, $$15);
                } else {
                    this.itemRenderer.renderStatic(snowGolem3, $$12, ItemDisplayContext.HEAD, false, poseStack0, multiBufferSource1, snowGolem3.m_9236_(), int2, LivingEntityRenderer.getOverlayCoords(snowGolem3, 0.0F), snowGolem3.m_19879_());
                }
                poseStack0.popPose();
            }
        }
    }
}