package me.jellysquid.mods.sodium.mixin.features.model;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.client.renderer.block.model.BlockElement;
import org.embeddedt.embeddium.model.EpsilonizableBlockElement;
import org.embeddedt.embeddium.util.PlatformUtil;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ BlockElement.class })
public class BlockElementMixin implements EpsilonizableBlockElement {

    @Shadow
    @Final
    public Vector3f from;

    @Shadow
    @Final
    public Vector3f to;

    private boolean embeddium$hasEpsilonized;

    private static final float EMBEDDIUM$MINIMUM_EPSILON = 0.008F;

    @Override
    public synchronized void embeddium$epsilonize() {
        if (!this.embeddium$hasEpsilonized) {
            this.embeddium$hasEpsilonized = true;
            if (!PlatformUtil.isLoadValid() || !SodiumClientMod.options().performance.useCompactVertexFormat) {
                return;
            }
            embeddium$epsilonize(this.from);
            embeddium$epsilonize(this.to);
        }
    }

    private static void embeddium$epsilonize(Vector3f v) {
        v.x = embeddium$epsilonize(v.x);
        v.y = embeddium$epsilonize(v.y);
        v.z = embeddium$epsilonize(v.z);
    }

    private static float embeddium$epsilonize(float f) {
        int roundedCoord = Math.round(f);
        float difference = f - (float) roundedCoord;
        return difference != 0.0F && !(Math.abs(difference) >= 0.008F) ? (float) roundedCoord + difference * 10.0F : f;
    }
}