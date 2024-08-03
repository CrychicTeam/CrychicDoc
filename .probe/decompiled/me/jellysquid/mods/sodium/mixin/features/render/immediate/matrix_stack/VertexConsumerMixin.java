package me.jellysquid.mods.sodium.mixin.features.render.immediate.matrix_stack;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.caffeinemc.mods.sodium.api.math.MatrixHelper;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ VertexConsumer.class })
public interface VertexConsumerMixin {

    @Shadow
    VertexConsumer normal(float var1, float var2, float var3);

    @Shadow
    VertexConsumer vertex(double var1, double var3, double var5);

    @Overwrite
    default VertexConsumer vertex(Matrix4f matrix, float x, float y, float z) {
        float xt = MatrixHelper.transformPositionX(matrix, x, y, z);
        float yt = MatrixHelper.transformPositionY(matrix, x, y, z);
        float zt = MatrixHelper.transformPositionZ(matrix, x, y, z);
        return this.vertex((double) xt, (double) yt, (double) zt);
    }

    @Overwrite
    default VertexConsumer normal(Matrix3f matrix, float x, float y, float z) {
        float xt = MatrixHelper.transformNormalX(matrix, x, y, z);
        float yt = MatrixHelper.transformNormalY(matrix, x, y, z);
        float zt = MatrixHelper.transformNormalZ(matrix, x, y, z);
        return this.normal(xt, yt, zt);
    }
}