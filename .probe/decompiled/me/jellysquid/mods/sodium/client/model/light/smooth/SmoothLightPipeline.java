package me.jellysquid.mods.sodium.client.model.light.smooth;

import me.jellysquid.mods.sodium.client.model.light.LightPipeline;
import me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess;
import me.jellysquid.mods.sodium.client.model.light.data.QuadLightData;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class SmoothLightPipeline implements LightPipeline {

    private final LightDataAccess lightCache;

    private final AoFaceData[] cachedFaceData = new AoFaceData[12];

    private long cachedPos = Long.MIN_VALUE;

    private final float[] weights = new float[4];

    public SmoothLightPipeline(LightDataAccess cache) {
        this.lightCache = cache;
        for (int i = 0; i < this.cachedFaceData.length; i++) {
            this.cachedFaceData[i] = new AoFaceData();
        }
    }

    @Override
    public void calculate(ModelQuadView quad, BlockPos pos, QuadLightData out, Direction cullFace, Direction lightFace, boolean shade) {
        this.updateCachedData(pos.asLong());
        int flags = quad.getFlags();
        AoNeighborInfo neighborInfo = AoNeighborInfo.get(lightFace);
        if ((flags & 4) != 0 || (flags & 2) != 0 && LightDataAccess.unpackFC(this.lightCache.get(pos))) {
            if ((flags & 1) == 0) {
                this.applyAlignedFullFace(neighborInfo, pos, lightFace, out);
            } else {
                this.applyAlignedPartialFace(neighborInfo, quad, pos, lightFace, out);
            }
        } else if ((flags & 2) != 0) {
            this.applyParallelFace(neighborInfo, quad, pos, lightFace, out);
        } else {
            this.applyNonParallelFace(neighborInfo, quad, pos, lightFace, out);
        }
        this.applySidedBrightness(out, lightFace, shade);
    }

    @Override
    public void reset() {
        this.cachedPos = Long.MIN_VALUE;
    }

    private void applyAlignedFullFace(AoNeighborInfo neighborInfo, BlockPos pos, Direction dir, QuadLightData out) {
        AoFaceData faceData = this.getCachedFaceData(pos, dir, true);
        neighborInfo.mapCorners(faceData.lm, faceData.ao, out.lm, out.br);
    }

    private void applyAlignedPartialFace(AoNeighborInfo neighborInfo, ModelQuadView quad, BlockPos pos, Direction dir, QuadLightData out) {
        for (int i = 0; i < 4; i++) {
            float cx = clamp(quad.getX(i));
            float cy = clamp(quad.getY(i));
            float cz = clamp(quad.getZ(i));
            float[] weights = this.weights;
            neighborInfo.calculateCornerWeights(cx, cy, cz, weights);
            this.applyAlignedPartialFaceVertex(pos, dir, weights, i, out, true);
        }
    }

    private void applyParallelFace(AoNeighborInfo neighborInfo, ModelQuadView quad, BlockPos pos, Direction dir, QuadLightData out) {
        for (int i = 0; i < 4; i++) {
            float cx = clamp(quad.getX(i));
            float cy = clamp(quad.getY(i));
            float cz = clamp(quad.getZ(i));
            float[] weights = this.weights;
            neighborInfo.calculateCornerWeights(cx, cy, cz, weights);
            float depth = neighborInfo.getDepth(cx, cy, cz);
            if (Mth.equal(depth, 1.0F)) {
                this.applyAlignedPartialFaceVertex(pos, dir, weights, i, out, false);
            } else {
                this.applyInsetPartialFaceVertex(pos, dir, depth, 1.0F - depth, weights, i, out);
            }
        }
    }

    private void applyNonParallelFace(AoNeighborInfo neighborInfo, ModelQuadView quad, BlockPos pos, Direction dir, QuadLightData out) {
        for (int i = 0; i < 4; i++) {
            float cx = clamp(quad.getX(i));
            float cy = clamp(quad.getY(i));
            float cz = clamp(quad.getZ(i));
            float[] weights = this.weights;
            neighborInfo.calculateCornerWeights(cx, cy, cz, weights);
            float depth = neighborInfo.getDepth(cx, cy, cz);
            if (Mth.equal(depth, 0.0F)) {
                this.applyAlignedPartialFaceVertex(pos, dir, weights, i, out, true);
            } else if (Mth.equal(depth, 1.0F)) {
                this.applyAlignedPartialFaceVertex(pos, dir, weights, i, out, false);
            } else {
                this.applyInsetPartialFaceVertex(pos, dir, depth, 1.0F - depth, weights, i, out);
            }
        }
    }

    private void applyAlignedPartialFaceVertex(BlockPos pos, Direction dir, float[] w, int i, QuadLightData out, boolean offset) {
        AoFaceData faceData = this.getCachedFaceData(pos, dir, offset);
        if (!faceData.hasUnpackedLightData()) {
            faceData.unpackLightData();
        }
        float sl = faceData.getBlendedSkyLight(w);
        float bl = faceData.getBlendedBlockLight(w);
        float ao = faceData.getBlendedShade(w);
        out.br[i] = ao;
        out.lm[i] = getLightMapCoord(sl, bl);
    }

    private void applyInsetPartialFaceVertex(BlockPos pos, Direction dir, float n1d, float n2d, float[] w, int i, QuadLightData out) {
        AoFaceData n1 = this.getCachedFaceData(pos, dir, false);
        if (!n1.hasUnpackedLightData()) {
            n1.unpackLightData();
        }
        AoFaceData n2 = this.getCachedFaceData(pos, dir, true);
        if (!n2.hasUnpackedLightData()) {
            n2.unpackLightData();
        }
        float ao = n1.getBlendedShade(w) * n1d + n2.getBlendedShade(w) * n2d;
        float sl = n1.getBlendedSkyLight(w) * n1d + n2.getBlendedSkyLight(w) * n2d;
        float bl = n1.getBlendedBlockLight(w) * n1d + n2.getBlendedBlockLight(w) * n2d;
        out.br[i] = ao;
        out.lm[i] = getLightMapCoord(sl, bl);
    }

    private void applySidedBrightness(QuadLightData out, Direction face, boolean shade) {
        float brightness = this.lightCache.getWorld().getShade(face, shade);
        float[] br = out.br;
        for (int i = 0; i < br.length; i++) {
            br[i] *= brightness;
        }
    }

    private AoFaceData getCachedFaceData(BlockPos pos, Direction face, boolean offset) {
        AoFaceData data = this.cachedFaceData[offset ? face.ordinal() : face.ordinal() + 6];
        if (!data.hasLightData()) {
            data.initLightData(this.lightCache, pos, face, offset);
        }
        return data;
    }

    private void updateCachedData(long key) {
        if (this.cachedPos != key) {
            for (AoFaceData data : this.cachedFaceData) {
                data.reset();
            }
            this.cachedPos = key;
        }
    }

    private static float clamp(float v) {
        if (v < 0.0F) {
            return 0.0F;
        } else {
            return v > 1.0F ? 1.0F : v;
        }
    }

    private static int getLightMapCoord(float sl, float bl) {
        return ((int) sl & 0xFF) << 16 | (int) bl & 0xFF;
    }
}