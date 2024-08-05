package me.jellysquid.mods.sodium.mixin.features.render.entity.shadows;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.caffeinemc.mods.sodium.api.math.MatrixHelper;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.api.vertex.format.common.ModelVertex;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ EntityRenderDispatcher.class })
public class EntityRenderDispatcherMixin {

    @Unique
    private static final int SHADOW_COLOR = ColorABGR.pack(1.0F, 1.0F, 1.0F);

    @Inject(method = { "renderBlockShadow" }, at = { @At("HEAD") }, cancellable = true)
    private static void renderShadowPartFast(PoseStack.Pose entry, VertexConsumer vertices, ChunkAccess chunk, LevelReader world, BlockPos pos, double x, double y, double z, float radius, float opacity, CallbackInfo ci) {
        VertexBufferWriter writer = VertexBufferWriter.tryOf(vertices);
        if (writer != null) {
            ci.cancel();
            BlockPos blockPos = pos.below();
            BlockState blockState = world.m_8055_(blockPos);
            if (blockState.m_60799_() != RenderShape.INVISIBLE && blockState.m_60838_(world, blockPos)) {
                int light = world.getMaxLocalRawBrightness(pos);
                if (light > 3) {
                    VoxelShape voxelShape = blockState.m_60808_(world, blockPos);
                    if (!voxelShape.isEmpty()) {
                        float brightness = LightTexture.getBrightness(world.dimensionType(), light);
                        float alpha = (float) (((double) opacity - (y - (double) pos.m_123342_()) / 2.0) * 0.5 * (double) brightness);
                        if (alpha >= 0.0F) {
                            if (alpha > 1.0F) {
                                alpha = 1.0F;
                            }
                            AABB box = voxelShape.bounds();
                            float minX = (float) ((double) pos.m_123341_() + box.minX - x);
                            float maxX = (float) ((double) pos.m_123341_() + box.maxX - x);
                            float minY = (float) ((double) pos.m_123342_() + box.minY - y);
                            float minZ = (float) ((double) pos.m_123343_() + box.minZ - z);
                            float maxZ = (float) ((double) pos.m_123343_() + box.maxZ - z);
                            renderShadowPart(entry, writer, radius, alpha, minX, maxX, minY, minZ, maxZ);
                        }
                    }
                }
            }
        }
    }

    @Deprecated
    private static void renderShadowPart(PoseStack.Pose matrices, VertexConsumer consumer, float radius, float alpha, float minX, float maxX, float minY, float minZ, float maxZ) {
        renderShadowPart(matrices, VertexBufferWriter.of(consumer), radius, alpha, minX, maxX, minY, minZ, maxZ);
    }

    @Unique
    private static void renderShadowPart(PoseStack.Pose matrices, VertexBufferWriter writer, float radius, float alpha, float minX, float maxX, float minY, float minZ, float maxZ) {
        float size = 0.5F * (1.0F / radius);
        float u1 = -minX * size + 0.5F;
        float u2 = -maxX * size + 0.5F;
        float v1 = -minZ * size + 0.5F;
        float v2 = -maxZ * size + 0.5F;
        Matrix3f matNormal = matrices.normal();
        Matrix4f matPosition = matrices.pose();
        int color = ColorABGR.withAlpha(SHADOW_COLOR, alpha);
        int normal = MatrixHelper.transformNormal(matNormal, Direction.UP);
        MemoryStack stack = MemoryStack.stackPush();
        try {
            long buffer = stack.nmalloc(144);
            writeShadowVertex(buffer, matPosition, minX, minY, minZ, u1, v1, color, normal);
            long ptr = buffer + 36L;
            writeShadowVertex(ptr, matPosition, minX, minY, maxZ, u1, v2, color, normal);
            ptr += 36L;
            writeShadowVertex(ptr, matPosition, maxX, minY, maxZ, u2, v2, color, normal);
            ptr += 36L;
            writeShadowVertex(ptr, matPosition, maxX, minY, minZ, u2, v1, color, normal);
            ptr += 36L;
            writer.push(stack, buffer, 4, ModelVertex.FORMAT);
        } catch (Throwable var24) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var23) {
                    var24.addSuppressed(var23);
                }
            }
            throw var24;
        }
        if (stack != null) {
            stack.close();
        }
    }

    @Unique
    private static void writeShadowVertex(long ptr, Matrix4f matPosition, float x, float y, float z, float u, float v, int color, int normal) {
        float xt = MatrixHelper.transformPositionX(matPosition, x, y, z);
        float yt = MatrixHelper.transformPositionY(matPosition, x, y, z);
        float zt = MatrixHelper.transformPositionZ(matPosition, x, y, z);
        ModelVertex.write(ptr, xt, yt, zt, color, u, v, 15728880, OverlayTexture.NO_OVERLAY, normal);
    }
}