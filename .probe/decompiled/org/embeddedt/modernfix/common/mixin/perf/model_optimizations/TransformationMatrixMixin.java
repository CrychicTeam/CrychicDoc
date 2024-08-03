package org.embeddedt.modernfix.common.mixin.perf.model_optimizations;

import com.mojang.math.Transformation;
import java.util.Objects;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ Transformation.class })
@ClientOnlyMixin
public class TransformationMatrixMixin {

    @Shadow
    @Final
    private Matrix4f matrix;

    private Integer cachedHashCode = null;

    @Overwrite(remap = false)
    public int hashCode() {
        int hash;
        if (this.cachedHashCode != null) {
            hash = this.cachedHashCode;
        } else {
            hash = Objects.hashCode(this.matrix);
            this.cachedHashCode = hash;
        }
        return hash;
    }
}