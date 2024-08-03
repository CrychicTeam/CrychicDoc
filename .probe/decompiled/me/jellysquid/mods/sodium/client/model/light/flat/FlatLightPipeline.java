package me.jellysquid.mods.sodium.client.model.light.flat;

import java.util.Arrays;
import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess;
import me.jellysquid.mods.sodium.client.model.light.data.QuadLightData;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class FlatLightPipeline implements LightPipeline {

    private final LightDataAccess lightCache;

    public FlatLightPipeline(LightDataAccess lightCache) {
        this.lightCache = lightCache;
    }

    @Override
    public void calculate(ModelQuadView quad, BlockPos pos, QuadLightData out, Direction cullFace, Direction lightFace, boolean shade) {
        int lightmap;
        if (cullFace != null) {
            lightmap = this.getOffsetLightmap(pos, cullFace);
        } else {
            int flags = quad.getFlags();
            if ((flags & 4) == 0 && ((flags & 2) == 0 || !LightDataAccess.unpackFC(this.lightCache.get(pos)))) {
                lightmap = LightDataAccess.getEmissiveLightmap(this.lightCache.get(pos));
            } else {
                lightmap = this.getOffsetLightmap(pos, lightFace);
            }
        }
        Arrays.fill(out.lm, lightmap);
        Arrays.fill(out.br, this.lightCache.getWorld().getShade(lightFace, shade));
    }

    private int getOffsetLightmap(BlockPos pos, Direction face) {
        int word = this.lightCache.get(pos);
        if (LightDataAccess.unpackEM(word)) {
            return 15728880;
        } else {
            int adjWord = this.lightCache.get(pos, face);
            return LightTexture.pack(Math.max(LightDataAccess.unpackBL(adjWord), LightDataAccess.unpackLU(word)), LightDataAccess.unpackSL(adjWord));
        }
    }
}