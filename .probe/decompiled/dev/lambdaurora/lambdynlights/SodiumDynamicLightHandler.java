package dev.lambdaurora.lambdynlights;

import me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface SodiumDynamicLightHandler {

    ThreadLocal<BlockPos.MutableBlockPos> lambdynlights$pos = ThreadLocal.withInitial(BlockPos.MutableBlockPos::new);

    static int lambdynlights$getLightmap(BlockPos pos, int word, int lightmap) {
        if (!LambDynLights.isEnabled()) {
            return lightmap;
        } else if (LightDataAccess.unpackFO(word)) {
            return lightmap;
        } else {
            double dynamic = LambDynLights.get().getDynamicLightLevel(pos);
            return LambDynLights.get().getLightmapWithDynamicLight(dynamic, lightmap);
        }
    }
}