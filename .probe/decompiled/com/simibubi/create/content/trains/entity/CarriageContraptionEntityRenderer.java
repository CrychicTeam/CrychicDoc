package com.simibubi.create.content.trains.entity;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.render.ContraptionEntityRenderer;
import java.util.Objects;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

public class CarriageContraptionEntityRenderer extends ContraptionEntityRenderer<CarriageContraptionEntity> {

    public CarriageContraptionEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public boolean shouldRender(CarriageContraptionEntity entity, Frustum clippingHelper, double cameraX, double cameraY, double cameraZ) {
        Carriage carriage = entity.getCarriage();
        if (carriage != null) {
            for (CarriageBogey bogey : carriage.bogeys) {
                if (bogey != null) {
                    bogey.couplingAnchors.replace(v -> null);
                }
            }
        }
        return super.shouldRender(entity, clippingHelper, cameraX, cameraY, cameraZ);
    }

    public void render(CarriageContraptionEntity entity, float yaw, float partialTicks, PoseStack ms, MultiBufferSource buffers, int overlay) {
        if (entity.validForRender && !entity.firstPositionUpdate) {
            super.render(entity, yaw, partialTicks, ms, buffers, overlay);
            Carriage carriage = entity.getCarriage();
            if (carriage != null) {
                Vec3 position = entity.m_20318_(partialTicks);
                float viewYRot = entity.m_5675_(partialTicks);
                float viewXRot = entity.m_5686_(partialTicks);
                int bogeySpacing = carriage.bogeySpacing;
                carriage.bogeys.forEach(bogey -> {
                    if (bogey != null) {
                        BlockPos bogeyPos = bogey.isLeading ? BlockPos.ZERO : BlockPos.ZERO.relative(entity.getInitialOrientation().getCounterClockWise(), bogeySpacing);
                        if (!Backend.canUseInstancing(entity.m_9236_()) && !entity.getContraption().isHiddenInPortal(bogeyPos)) {
                            ms.pushPose();
                            translateBogey(ms, bogey, bogeySpacing, viewYRot, viewXRot, partialTicks);
                            int light = getBogeyLightCoords(entity, bogey, partialTicks);
                            bogey.type.render(null, bogey.wheelAngle.getValue(partialTicks), ms, partialTicks, buffers, light, overlay, bogey.getStyle(), bogey.bogeyData);
                            ms.popPose();
                        }
                        bogey.updateCouplingAnchor(position, viewXRot, viewYRot, bogeySpacing, partialTicks, bogey.isLeading);
                        if (!carriage.isOnTwoBogeys()) {
                            bogey.updateCouplingAnchor(position, viewXRot, viewYRot, bogeySpacing, partialTicks, !bogey.isLeading);
                        }
                    }
                });
            }
        }
    }

    public static void translateBogey(PoseStack ms, CarriageBogey bogey, int bogeySpacing, float viewYRot, float viewXRot, float partialTicks) {
        boolean selfUpsideDown = bogey.isUpsideDown();
        boolean leadingUpsideDown = bogey.carriage.leadingBogey().isUpsideDown();
        ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(ms).rotateY((double) (viewYRot + 90.0F))).rotateX((double) (-viewXRot))).rotateY(180.0)).translate(0.0, 0.0, bogey.isLeading ? 0.0 : (double) (-bogeySpacing))).rotateY(-180.0)).rotateX((double) viewXRot)).rotateY((double) (-viewYRot - 90.0F))).rotateY((double) bogey.yaw.getValue(partialTicks))).rotateX((double) bogey.pitch.getValue(partialTicks))).translate(0.0, 0.5, 0.0)).rotateZ(selfUpsideDown ? 180.0 : 0.0)).translateY(selfUpsideDown != leadingUpsideDown ? 2.0 : 0.0);
    }

    public static int getBogeyLightCoords(CarriageContraptionEntity entity, CarriageBogey bogey, float partialTicks) {
        BlockPos lightPos = BlockPos.containing((Position) Objects.requireNonNullElseGet(bogey.getAnchorPosition(), () -> entity.m_7371_(partialTicks)));
        return LightTexture.pack(entity.m_9236_().m_45517_(LightLayer.BLOCK, lightPos), entity.m_9236_().m_45517_(LightLayer.SKY, lightPos));
    }
}