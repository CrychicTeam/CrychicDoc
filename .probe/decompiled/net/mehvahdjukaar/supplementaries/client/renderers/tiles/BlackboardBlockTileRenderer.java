package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mehvahdjukaar.moonlight.api.client.util.LOD;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.client.util.VertexUtil;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.SuppClientPlatformStuff;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BlackboardBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BlackboardBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2i;

public class BlackboardBlockTileRenderer implements BlockEntityRenderer<BlackboardBlockTile> {

    public static final int WIDTH = 6;

    private final Minecraft mc = Minecraft.getInstance();

    private final Camera camera = this.mc.gameRenderer.getMainCamera();

    private final boolean noise = MiscUtils.FESTIVITY.isAprilsFool() && PlatHelper.getPlatform().isForge();

    public BlackboardBlockTileRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public int getViewDistance() {
        return this.noise ? 64 : 8;
    }

    public boolean shouldRender(BlackboardBlockTile blockEntity, Vec3 cameraPos) {
        return BlockEntityRenderer.super.shouldRender(blockEntity, cameraPos);
    }

    public void render(BlackboardBlockTile tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int combinedOverlayIn) {
        if (((BlackboardBlock.UseMode) CommonConfigs.Building.BLACKBOARD_MODE.get()).canManualDraw() || this.noise) {
            Direction dir = tile.getDirection();
            float yaw = -dir.toYRot();
            Vec3 cameraPos = this.camera.getPosition();
            BlockPos pos = tile.m_58899_();
            if (this.noise) {
                int lu = light & 65535;
                int lv = light >> 16 & 65535;
                SuppClientPlatformStuff.getNoiseShader().getUniform("Intensity").set(1.0F);
                poseStack.pushPose();
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(RotHlpr.rot(dir));
                poseStack.translate(-0.5, -0.5, 0.1865);
                VertexConsumer builder = ModMaterials.BLACKBOARD_OUTLINE.buffer(bufferSource, SuppClientPlatformStuff::staticNoise);
                VertexUtil.addQuad(builder, poseStack, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, 255, 255, lu, lv);
                poseStack.popPose();
            } else if (!LOD.isOutOfFocus(cameraPos, pos, yaw, 0.0F, dir, 0.375F)) {
                HitResult hit = this.mc.hitResult;
                if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHit = (BlockHitResult) hit;
                    if (blockHit.getBlockPos().equals(pos) && tile.getDirection() == blockHit.getDirection()) {
                        Player player = this.mc.player;
                        if (player != null && Utils.mayPerformBlockAction(player, pos, player.m_21205_()) && BlackboardBlock.getStackChalkColor(player.m_21205_()) != null) {
                            poseStack.pushPose();
                            poseStack.translate(0.5, 0.5, 0.5);
                            poseStack.mulPose(RotHlpr.rot(dir));
                            poseStack.translate(-0.5, -0.5, 0.1865);
                            int lu = light & 65535;
                            int lv = light >> 16 & 65535;
                            Vector2i pair = BlackboardBlock.getHitSubPixel(blockHit);
                            float p = 0.0625F;
                            float x = (float) pair.x() * p;
                            float y = (float) pair.y() * p;
                            VertexConsumer builder = ModMaterials.BLACKBOARD_OUTLINE.buffer(bufferSource, RenderType::m_110452_);
                            poseStack.translate(1.0F - x - p, 1.0F - y - p, 0.0F);
                            VertexUtil.addQuad(builder, poseStack, 0.0F, 0.0F, p, p, lu, lv);
                            poseStack.popPose();
                        }
                    }
                }
            }
        }
    }
}