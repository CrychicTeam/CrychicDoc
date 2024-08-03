package com.github.alexmodguy.alexscaves.client.render.blockentity;

import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.render.entity.NotorRenderer;
import com.github.alexmodguy.alexscaves.server.block.blockentity.HologramProjectorBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class HologramProjectorBlockRenderer<T extends HologramProjectorBlockEntity> implements BlockEntityRenderer<T> {

    private static final Map<BlockPos, HologramProjectorBlockEntity> allOnScreen = new HashMap();

    private static final Map<UUID, PlayerInfo> playerInfo = new HashMap();

    private static PlayerModel playerModel = null;

    private static PlayerModel slimPlayerModel = null;

    public HologramProjectorBlockRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
    }

    public static void renderEntireBatch(LevelRenderer levelRenderer, PoseStack poseStack, int renderTick, Camera camera, float partialTick) {
        if (!allOnScreen.isEmpty()) {
            List<BlockPos> sortedPoses = new ArrayList(allOnScreen.keySet());
            Collections.sort(sortedPoses, (blockPos1, blockPos2) -> sortBlockPos(camera, blockPos1, blockPos2));
            poseStack.pushPose();
            Vec3 cameraPos = camera.getPosition();
            poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
            for (BlockPos pos : sortedPoses) {
                MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
                Vec3 blockAt = Vec3.atCenterOf(pos);
                poseStack.pushPose();
                poseStack.translate(blockAt.x, blockAt.y, blockAt.z);
                renderAt((HologramProjectorBlockEntity) allOnScreen.get(pos), partialTick, poseStack, multibuffersource$buffersource);
                poseStack.popPose();
                multibuffersource$buffersource.endBatch();
            }
            poseStack.popPose();
        }
        allOnScreen.clear();
    }

    private static int sortBlockPos(Camera camera, BlockPos blockPos1, BlockPos blockPos2) {
        double d1 = camera.getPosition().distanceTo(Vec3.atCenterOf(blockPos1));
        double d2 = camera.getPosition().distanceTo(Vec3.atCenterOf(blockPos2));
        return Double.compare(d2, d1);
    }

    public void render(T hologram, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!hologram.m_58901_()) {
            allOnScreen.put(hologram.m_58899_(), hologram);
        } else {
            allOnScreen.remove(hologram.m_58899_());
        }
    }

    private static void renderAt(HologramProjectorBlockEntity projectorBlockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn) {
        PostEffectRegistry.renderEffectForNextTick(ClientProxy.HOLOGRAM_SHADER);
        Entity holoEntity = projectorBlockEntity.getDisplayEntity(Minecraft.getInstance().level);
        float amount = projectorBlockEntity.getSwitchAmount(partialTicks);
        float ticks = (float) projectorBlockEntity.tickCount + partialTicks;
        float bob1 = (float) (Math.sin((double) (ticks * 0.05F + amount)) * 0.1F);
        float bob2 = (float) (Math.cos((double) (ticks * 0.05F + amount)) * 0.1F);
        float length = (1.0F + bob1) * amount;
        float width = ((holoEntity == null ? 0.8F : holoEntity.getBbWidth()) + bob2) * amount;
        if (holoEntity instanceof LivingEntity living) {
            width *= living.getScale();
        }
        VertexConsumer lightConsumer = bufferIn.getBuffer(ACRenderTypes.getHologramLights());
        poseStack.pushPose();
        float padStart = 0.125F;
        float padEnd = 1.0F - padStart;
        poseStack.pushPose();
        poseStack.translate(-0.5F, -0.235F, -0.5F);
        float cameraY = Minecraft.getInstance().getEntityRenderDispatcher().camera.getYRot();
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f1 = posestack$pose.pose();
        Matrix3f matrix3f1 = posestack$pose.normal();
        lightConsumer.vertex(matrix4f1, padStart, 0.0F, padEnd).color(220, 220, 255, (int) (amount * 150.0F)).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f1, 0.0F, 1.0F, 0.0F).endVertex();
        lightConsumer.vertex(matrix4f1, padEnd, 0.0F, padEnd).color(220, 220, 255, (int) (amount * 150.0F)).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f1, 0.0F, 1.0F, 0.0F).endVertex();
        lightConsumer.vertex(matrix4f1, padEnd, 0.0F, padStart).color(220, 220, 255, (int) (amount * 150.0F)).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f1, 0.0F, 1.0F, 0.0F).endVertex();
        lightConsumer.vertex(matrix4f1, padStart, 0.0F, padStart).color(220, 220, 255, (int) (amount * 150.0F)).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f1, 0.0F, 1.0F, 0.0F).endVertex();
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0.0F, -0.2F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - cameraY));
        PoseStack.Pose posestack$pose1 = poseStack.last();
        Matrix4f matrix4f2 = posestack$pose1.pose();
        Matrix3f matrix3f2 = posestack$pose1.normal();
        shineOriginVertex(lightConsumer, matrix4f2, matrix3f2, 0.0F, 0.0F);
        shineLeftCornerVertex(lightConsumer, matrix4f2, matrix3f2, length, width, 0.0F, 0.0F);
        shineRightCornerVertex(lightConsumer, matrix4f2, matrix3f2, length, width, 0.0F, 0.0F);
        shineLeftCornerVertex(lightConsumer, matrix4f2, matrix3f2, length, width, 0.0F, 0.0F);
        if (projectorBlockEntity.isPlayerRender()) {
            poseStack.pushPose();
            poseStack.scale(1.0F, amount, 1.0F);
            poseStack.translate(0.0F, length + 1.5F, 0.0F);
            poseStack.mulPose(Axis.YN.rotationDegrees(180.0F - cameraY + projectorBlockEntity.getRotation(partialTicks)));
            renderPlayerHologram(projectorBlockEntity.getLastPlayerUUID(), partialTicks, poseStack, bufferIn, 240);
            poseStack.popPose();
        } else if (holoEntity != null) {
            poseStack.pushPose();
            poseStack.scale(1.0F, amount, 1.0F);
            poseStack.translate(0.0F, length + 1.5F, 0.0F);
            poseStack.mulPose(Axis.YN.rotationDegrees(180.0F - cameraY + projectorBlockEntity.getRotation(partialTicks)));
            NotorRenderer.renderEntityInHologram(holoEntity, 0.0, 0.0, 0.0, 0.0F, partialTicks, poseStack, bufferIn, 240);
            poseStack.popPose();
        }
        poseStack.popPose();
        poseStack.popPose();
    }

    private static PlayerInfo getPlayerInfo(UUID uuid) {
        if (!playerInfo.containsKey(uuid)) {
            playerInfo.put(uuid, Minecraft.getInstance().getConnection().getPlayerInfo(uuid));
        }
        return (PlayerInfo) playerInfo.get(uuid);
    }

    private static String getPlayerModelName(PlayerInfo playerInfo, UUID uuid) {
        return playerInfo == null ? DefaultPlayerSkin.getSkinModelName(uuid) : playerInfo.getModelName();
    }

    private static ResourceLocation getPlayerSkinTextureLocation(PlayerInfo playerInfo, UUID uuid) {
        return playerInfo == null ? DefaultPlayerSkin.getDefaultSkin(uuid) : playerInfo.getSkinLocation();
    }

    private static void renderPlayerHologram(UUID lastPlayerUUID, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int i) {
        PostEffectRegistry.renderEffectForNextTick(ClientProxy.HOLOGRAM_SHADER);
        PlayerInfo playerInfo = getPlayerInfo(lastPlayerUUID);
        String modelName = getPlayerModelName(playerInfo, lastPlayerUUID);
        EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? extends Player> renderer = (EntityRenderer<? extends Player>) manager.getSkinMap().get(modelName);
        if (playerModel == null || slimPlayerModel == null) {
            playerModel = new PlayerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER), false);
            slimPlayerModel = new PlayerModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM), true);
        }
        PlayerModel model = modelName.equals("slim") ? slimPlayerModel : playerModel;
        model.f_102610_ = false;
        if (renderer instanceof LivingEntityRenderer livingEntityRenderer) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getHologram(getPlayerSkinTextureLocation(playerInfo, lastPlayerUUID)));
            poseStack.pushPose();
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            model.m_7695_(poseStack, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
    }

    private static void shineOriginVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, 0.0F, 0.0F, 0.0F).color(255, 255, 255, 230).uv(xOffset + 0.5F, yOffset).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void shineLeftCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, -ACMath.HALF_SQRT_3 * float4, float3, 0.0F).color(0, 0, 255, 0).uv(xOffset, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private static void shineRightCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, ACMath.HALF_SQRT_3 * float4, float3, 0.0F).color(0, 0, 255, 0).uv(xOffset + 1.0F, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    @Override
    public int getViewDistance() {
        return 128;
    }
}