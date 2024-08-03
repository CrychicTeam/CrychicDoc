package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map.Entry;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BuntingBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class BuntingBlockTileRenderer implements BlockEntityRenderer<BuntingBlockTile> {

    private static ModelPart MODEL;

    private static ModelPart FLAG;

    private static ModelPart BOX;

    public BuntingBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        MODEL = context.bakeLayer(ClientRegistry.BUNTING_MODEL);
        FLAG = MODEL.getChild("flag");
        BOX = MODEL.getChild("box");
    }

    public boolean shouldRender(BuntingBlockTile blockEntity, Vec3 cameraPos) {
        return blockEntity.shouldRenderFancy(cameraPos);
    }

    public void render(BuntingBlockTile tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        BlockPos pos = tile.m_58899_();
        long l = tile.m_58904_().getGameTime();
        for (Entry<Direction, DyeColor> e : tile.getBuntings().entrySet()) {
            renderBunting((DyeColor) e.getValue(), (Direction) e.getKey(), partialTicks, poseStack, null, bufferIn, combinedLightIn, combinedOverlayIn, pos, l);
        }
        poseStack.popPose();
    }

    public static void renderBunting(DyeColor color, Direction dir, float partialTicks, PoseStack poseStack, @Nullable VertexConsumer vertexConsumer, @Nullable MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn, BlockPos pos, long l) {
        if (color != null) {
            poseStack.pushPose();
            Vector3f step = dir.step().mul(0.25F);
            poseStack.mulPose(RotHlpr.rot(dir));
            poseStack.translate(0.0, 0.0, -0.25);
            poseStack.scale(1.0F, -1.0F, -1.0F);
            Material mat = (Material) ModMaterials.BUNTING_MATERIAL.get(color);
            VertexConsumer wrapped;
            if (buffer != null) {
                float h = ((float) Math.floorMod((long) (((float) pos.m_123341_() + step.x) * 7.0F + ((float) pos.m_123342_() + step.y) * 9.0F + ((float) pos.m_123343_() + step.z) * 13.0F) + l, 100L) + partialTicks) / 100.0F;
                int i = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1 : -1;
                FLAG.zRot = (float) i * 0.01F * Mth.cos((float) (Math.PI * 2) * h) * (float) Math.PI;
                wrapped = mat.buffer(buffer, RenderType::m_110452_);
            } else {
                FLAG.xRot = 0.0F;
                wrapped = mat.sprite().wrap(vertexConsumer);
            }
            BOX.xScale = 1.0F;
            BOX.yScale = 1.1F;
            BOX.zScale = 1.1F;
            MODEL.render(poseStack, wrapped, combinedLightIn, combinedOverlayIn);
            poseStack.popPose();
        }
    }

    public static LayerDefinition createMesh() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("flag", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, 0.0F, 0.0F, 7.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        partdefinition.addOrReplaceChild("box", CubeListBuilder.create().texOffs(0, 12).addBox(-4.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        return LayerDefinition.create(meshdefinition, 32, 16);
    }
}