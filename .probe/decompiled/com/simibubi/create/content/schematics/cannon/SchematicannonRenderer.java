package com.simibubi.create.content.schematics.cannon;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.model.ModelUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SchematicannonRenderer extends SafeBlockEntityRenderer<SchematicannonBlockEntity> {

    public SchematicannonRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(SchematicannonBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        boolean blocksLaunching = !blockEntity.flyingBlocks.isEmpty();
        if (blocksLaunching) {
            renderLaunchedBlocks(blockEntity, partialTicks, ms, buffer, light, overlay);
        }
        if (!Backend.canUseInstancing(blockEntity.m_58904_())) {
            BlockPos pos = blockEntity.m_58899_();
            BlockState state = blockEntity.m_58900_();
            double[] cannonAngles = getCannonAngles(blockEntity, pos, partialTicks);
            double yaw = cannonAngles[0];
            double pitch = cannonAngles[1];
            double recoil = getRecoil(blockEntity, partialTicks);
            ms.pushPose();
            VertexConsumer vb = buffer.getBuffer(RenderType.solid());
            SuperByteBuffer connector = CachedBufferer.partial(AllPartialModels.SCHEMATICANNON_CONNECTOR, state);
            connector.translate(0.5, 0.0, 0.5);
            connector.rotate(Direction.UP, (float) ((yaw + 90.0) / 180.0 * Math.PI));
            connector.translate(-0.5, 0.0, -0.5);
            connector.light(light).renderInto(ms, vb);
            SuperByteBuffer pipe = CachedBufferer.partial(AllPartialModels.SCHEMATICANNON_PIPE, state);
            pipe.translate(0.5, 0.9375, 0.5);
            pipe.rotate(Direction.UP, (float) ((yaw + 90.0) / 180.0 * Math.PI));
            pipe.rotate(Direction.SOUTH, (float) (pitch / 180.0 * Math.PI));
            pipe.translate(-0.5, -0.9375, -0.5);
            pipe.translate(0.0, -recoil / 100.0, 0.0);
            pipe.light(light).renderInto(ms, vb);
            ms.popPose();
        }
    }

    public static double[] getCannonAngles(SchematicannonBlockEntity blockEntity, BlockPos pos, float partialTicks) {
        BlockPos target = blockEntity.printer.getCurrentTarget();
        double yaw;
        double pitch;
        if (target != null) {
            Vec3 diff = Vec3.atLowerCornerOf(target.subtract(pos));
            if (blockEntity.previousTarget != null) {
                diff = Vec3.atLowerCornerOf(blockEntity.previousTarget).add(Vec3.atLowerCornerOf(target.subtract(blockEntity.previousTarget)).scale((double) partialTicks)).subtract(Vec3.atLowerCornerOf(pos));
            }
            double diffX = diff.x();
            double diffZ = diff.z();
            yaw = Mth.atan2(diffX, diffZ);
            yaw = yaw / Math.PI * 180.0;
            float distance = Mth.sqrt((float) (diffX * diffX + diffZ * diffZ));
            double yOffset = (double) (0.0F + distance * 2.0F);
            pitch = Mth.atan2((double) distance, diff.y() * 3.0 + yOffset);
            pitch = pitch / Math.PI * 180.0 + 10.0;
        } else {
            yaw = (double) blockEntity.defaultYaw;
            pitch = 40.0;
        }
        return new double[] { yaw, pitch };
    }

    public static double getRecoil(SchematicannonBlockEntity blockEntity, float partialTicks) {
        double recoil = 0.0;
        for (LaunchedItem launched : blockEntity.flyingBlocks) {
            if (launched.ticksRemaining != 0 && (float) (launched.ticksRemaining + 1) - partialTicks > (float) (launched.totalTicks - 10)) {
                recoil = Math.max(recoil, (double) ((float) (launched.ticksRemaining + 1) - partialTicks - (float) launched.totalTicks + 10.0F));
            }
        }
        return recoil;
    }

    private static void renderLaunchedBlocks(SchematicannonBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        for (LaunchedItem launched : blockEntity.flyingBlocks) {
            if (launched.ticksRemaining != 0) {
                Vec3 start = Vec3.atCenterOf(blockEntity.m_58899_().above());
                Vec3 target = Vec3.atCenterOf(launched.target);
                Vec3 distance = target.subtract(start);
                double yDifference = target.y - start.y;
                double throwHeight = Math.sqrt(distance.lengthSqr()) * 0.6F + yDifference;
                Vec3 cannonOffset = distance.add(0.0, throwHeight, 0.0).normalize().scale(2.0);
                start = start.add(cannonOffset);
                yDifference = target.y - start.y;
                float progress = ((float) launched.totalTicks - ((float) (launched.ticksRemaining + 1) - partialTicks)) / (float) launched.totalTicks;
                Vec3 blockLocationXZ = target.subtract(start).scale((double) progress).multiply(1.0, 0.0, 1.0);
                double yOffset = (double) (2.0F * (1.0F - progress) * progress) * throwHeight + (double) (progress * progress) * yDifference;
                Vec3 blockLocation = blockLocationXZ.add(0.5, yOffset + 1.5, 0.5).add(cannonOffset);
                ms.pushPose();
                ms.translate(blockLocation.x, blockLocation.y, blockLocation.z);
                ms.translate(0.125F, 0.125F, 0.125F);
                ms.mulPose(Axis.YP.rotationDegrees(360.0F * progress));
                ms.mulPose(Axis.XP.rotationDegrees(360.0F * progress));
                ms.translate(-0.125F, -0.125F, -0.125F);
                if (launched instanceof LaunchedItem.ForBlockState) {
                    BlockState state;
                    if (launched instanceof LaunchedItem.ForBelt) {
                        state = AllBlocks.SHAFT.getDefaultState();
                    } else {
                        state = ((LaunchedItem.ForBlockState) launched).state;
                    }
                    float scale = 0.3F;
                    ms.scale(scale, scale, scale);
                    Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, ms, buffer, light, overlay, ModelUtil.VIRTUAL_DATA, null);
                } else if (launched instanceof LaunchedItem.ForEntity) {
                    float scale = 1.2F;
                    ms.scale(scale, scale, scale);
                    Minecraft.getInstance().getItemRenderer().renderStatic(launched.stack, ItemDisplayContext.GROUND, light, overlay, ms, buffer, blockEntity.m_58904_(), 0);
                }
                ms.popPose();
                if (launched.ticksRemaining == launched.totalTicks && blockEntity.firstRenderTick) {
                    start = start.subtract(0.5, 0.5, 0.5);
                    blockEntity.firstRenderTick = false;
                    for (int i = 0; i < 10; i++) {
                        RandomSource r = blockEntity.m_58904_().getRandom();
                        double sX = cannonOffset.x * 0.01F;
                        double sY = (cannonOffset.y + 1.0) * 0.01F;
                        double sZ = cannonOffset.z * 0.01F;
                        double rX = (double) r.nextFloat() - sX * 40.0;
                        double rY = (double) r.nextFloat() - sY * 40.0;
                        double rZ = (double) r.nextFloat() - sZ * 40.0;
                        blockEntity.m_58904_().addParticle(ParticleTypes.CLOUD, start.x + rX, start.y + rY, start.z + rZ, sX, sY, sZ);
                    }
                }
            }
        }
    }

    public boolean shouldRenderOffScreen(SchematicannonBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 128;
    }
}