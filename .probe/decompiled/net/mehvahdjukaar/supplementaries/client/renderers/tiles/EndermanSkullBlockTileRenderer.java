package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.models.EndermanSkullModel;
import net.mehvahdjukaar.supplementaries.common.block.tiles.EndermanSkullBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class EndermanSkullBlockTileRenderer implements BlockEntityRenderer<EndermanSkullBlockTile> {

    @Nullable
    private final EndermanSkullModel model;

    public EndermanSkullBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new EndermanSkullModel(context.getModelSet().bakeLayer(ClientRegistry.ENDERMAN_HEAD_MODEL));
    }

    public void render(EndermanSkullBlockTile blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        float f = blockEntity.getMouthAnimation(partialTick);
        BlockState blockState = blockEntity.m_58900_();
        boolean wall = blockState.m_60734_() instanceof WallSkullBlock;
        Direction direction = wall ? (Direction) blockState.m_61143_(WallSkullBlock.FACING) : null;
        float rotation = 22.5F * (float) (wall ? (2 + direction.get2DDataValue()) * 4 : (Integer) blockState.m_61143_(SkullBlock.ROTATION));
        RenderType renderType = RenderType.entityCutoutNoCull(ModTextures.ENDERMAN_HEAD);
        poseStack.pushPose();
        if (direction != null) {
            Vector3f v = direction.step();
            v.mul(0.001F);
            poseStack.translate(v.x(), v.y(), v.z());
        }
        SkullBlockRenderer.renderSkull(direction, rotation, f, poseStack, bufferSource, packedLight, this.model, renderType);
        renderType = RenderType.eyes(ModTextures.ENDERMAN_HEAD_EYES);
        SkullBlockRenderer.renderSkull(direction, rotation, f, poseStack, bufferSource, 15728640, this.model, renderType);
        poseStack.popPose();
    }
}