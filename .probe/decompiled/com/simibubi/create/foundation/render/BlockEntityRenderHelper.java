package com.simibubi.create.foundation.render;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.backend.instancing.InstancedRenderRegistry;
import com.jozufozu.flywheel.config.BackendType;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class BlockEntityRenderHelper {

    public static void renderBlockEntities(Level world, Iterable<BlockEntity> customRenderBEs, PoseStack ms, MultiBufferSource buffer) {
        renderBlockEntities(world, null, customRenderBEs, ms, null, buffer);
    }

    public static void renderBlockEntities(Level world, Iterable<BlockEntity> customRenderBEs, PoseStack ms, MultiBufferSource buffer, float pt) {
        renderBlockEntities(world, null, customRenderBEs, ms, null, buffer, pt);
    }

    public static void renderBlockEntities(Level world, @Nullable VirtualRenderWorld renderWorld, Iterable<BlockEntity> customRenderBEs, PoseStack ms, @Nullable Matrix4f lightTransform, MultiBufferSource buffer) {
        renderBlockEntities(world, renderWorld, customRenderBEs, ms, lightTransform, buffer, AnimationTickHolder.getPartialTicks());
    }

    public static void renderBlockEntities(Level world, @Nullable VirtualRenderWorld renderWorld, Iterable<BlockEntity> customRenderBEs, PoseStack ms, @Nullable Matrix4f lightTransform, MultiBufferSource buffer, float pt) {
        Iterator<BlockEntity> iterator = customRenderBEs.iterator();
        while (iterator.hasNext()) {
            BlockEntity blockEntity = (BlockEntity) iterator.next();
            if (Backend.getBackendType() != BackendType.INSTANCING || !Backend.isFlywheelWorld(renderWorld) || !InstancedRenderRegistry.shouldSkipRender(blockEntity)) {
                BlockEntityRenderer<BlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(blockEntity);
                if (renderer == null) {
                    iterator.remove();
                } else {
                    BlockPos pos = blockEntity.getBlockPos();
                    ms.pushPose();
                    TransformStack.cast(ms).translate(pos);
                    try {
                        int worldLight = getCombinedLight(world, getLightPos(lightTransform, pos), renderWorld, pos);
                        if (renderWorld != null) {
                            blockEntity.setLevel(renderWorld);
                            renderer.render(blockEntity, pt, ms, buffer, worldLight, OverlayTexture.NO_OVERLAY);
                            blockEntity.setLevel(world);
                        } else {
                            renderer.render(blockEntity, pt, ms, buffer, worldLight, OverlayTexture.NO_OVERLAY);
                        }
                    } catch (Exception var13) {
                        iterator.remove();
                        String message = "BlockEntity " + RegisteredObjects.getKeyOrThrow(blockEntity.getType()).toString() + " could not be rendered virtually.";
                        if (AllConfigs.client().explainRenderErrors.get()) {
                            Create.LOGGER.error(message, var13);
                        } else {
                            Create.LOGGER.error(message);
                        }
                    }
                    ms.popPose();
                }
            }
        }
    }

    private static BlockPos getLightPos(@Nullable Matrix4f lightTransform, BlockPos contraptionPos) {
        if (lightTransform != null) {
            Vector4f lightVec = new Vector4f((float) contraptionPos.m_123341_() + 0.5F, (float) contraptionPos.m_123342_() + 0.5F, (float) contraptionPos.m_123343_() + 0.5F, 1.0F);
            lightVec.mul(lightTransform);
            return BlockPos.containing((double) lightVec.x(), (double) lightVec.y(), (double) lightVec.z());
        } else {
            return contraptionPos;
        }
    }

    public static int getCombinedLight(Level world, BlockPos worldPos, @Nullable VirtualRenderWorld renderWorld, BlockPos renderWorldPos) {
        int worldLight = LevelRenderer.getLightColor(world, worldPos);
        if (renderWorld != null) {
            int renderWorldLight = LevelRenderer.getLightColor(renderWorld, renderWorldPos);
            return SuperByteBuffer.maxLight(worldLight, renderWorldLight);
        } else {
            return worldLight;
        }
    }
}