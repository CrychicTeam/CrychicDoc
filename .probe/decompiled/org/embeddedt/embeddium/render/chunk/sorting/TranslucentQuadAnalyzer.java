package org.embeddedt.embeddium.render.chunk.sorting;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import java.util.BitSet;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class TranslucentQuadAnalyzer {

    private static final int EXPECTED_QUADS = 1000;

    private final FloatArrayList quadCenters = new FloatArrayList(3000);

    private final Vector3f[] vertexPositions = new Vector3f[4];

    private final Vector3f currentNormal = new Vector3f();

    private final Vector3f globalNormal = new Vector3f();

    private final BitSet normalSigns = new BitSet(1000);

    private static final BitSet EMPTY = new BitSet();

    private int currentVertex;

    private boolean hasDistinctNormals;

    public TranslucentQuadAnalyzer() {
        for (int i = 0; i < 4; i++) {
            this.vertexPositions[i] = new Vector3f();
        }
    }

    private static BitSet cloneBits(BitSet bits) {
        return bits.isEmpty() ? EMPTY : (BitSet) bits.clone();
    }

    private boolean areAllQuadsOnSamePlane(float[] centerArray) {
        float a = this.globalNormal.x;
        float b = this.globalNormal.y;
        float c = this.globalNormal.z;
        float d = a * centerArray[0] + b * centerArray[1] + c * centerArray[2];
        int nQuads = centerArray.length / 3;
        for (int quadIdx = 1; quadIdx < nQuads; quadIdx++) {
            int centerOff = quadIdx * 3;
            float candidateD = a * centerArray[centerOff + 0] + b * centerArray[centerOff + 1] + c * centerArray[centerOff + 2];
            if (!Mth.equal(candidateD, d)) {
                return false;
            }
        }
        return true;
    }

    public TranslucentQuadAnalyzer.SortState getSortState() {
        if (this.quadCenters.isEmpty()) {
            this.clear();
            return TranslucentQuadAnalyzer.SortState.NONE;
        } else {
            float[] centerArray = this.quadCenters.toArray(new float[0]);
            TranslucentQuadAnalyzer.Level sortLevel;
            if (this.hasDistinctNormals) {
                sortLevel = TranslucentQuadAnalyzer.Level.DYNAMIC;
            } else {
                sortLevel = this.areAllQuadsOnSamePlane(centerArray) ? TranslucentQuadAnalyzer.Level.NONE : TranslucentQuadAnalyzer.Level.STATIC;
            }
            TranslucentQuadAnalyzer.SortState finalState = sortLevel == TranslucentQuadAnalyzer.Level.NONE ? TranslucentQuadAnalyzer.SortState.NONE : new TranslucentQuadAnalyzer.SortState(sortLevel, centerArray, cloneBits(this.normalSigns), new Vector3f(this.globalNormal));
            this.clear();
            return finalState;
        }
    }

    public void clear() {
        this.quadCenters.clear();
        this.currentVertex = 0;
        this.globalNormal.zero();
        this.normalSigns.clear();
        this.hasDistinctNormals = false;
    }

    private void calculateNormal() {
        Vector3f v0 = this.vertexPositions[0];
        float x0 = v0.x;
        float y0 = v0.y;
        float z0 = v0.z;
        Vector3f v1 = this.vertexPositions[1];
        float x1 = v1.x;
        float y1 = v1.y;
        float z1 = v1.z;
        Vector3f v2 = this.vertexPositions[2];
        float x2 = v2.x;
        float y2 = v2.y;
        float z2 = v2.z;
        Vector3f v3 = this.vertexPositions[3];
        float x3 = v3.x;
        float y3 = v3.y;
        float z3 = v3.z;
        float dx0 = x2 - x0;
        float dy0 = y2 - y0;
        float dz0 = z2 - z0;
        float dx1 = x3 - x1;
        float dy1 = y3 - y1;
        float dz1 = z3 - z1;
        float normX = dy0 * dz1 - dz0 * dy1;
        float normY = dz0 * dx1 - dx0 * dz1;
        float normZ = dx0 * dy1 - dy0 * dx1;
        float l = (float) Math.sqrt((double) (normX * normX + normY * normY + normZ * normZ));
        if (l != 0.0F) {
            normX /= l;
            normY /= l;
            normZ /= l;
        }
        this.currentNormal.set(normX, normY, normZ);
    }

    private void captureQuad() {
        float totalX = 0.0F;
        float totalY = 0.0F;
        float totalZ = 0.0F;
        for (Vector3f vertex : this.vertexPositions) {
            totalX += vertex.x;
            totalY += vertex.y;
            totalZ += vertex.z;
        }
        FloatArrayList centers = this.quadCenters;
        int currentQuadIndex = centers.size() / 3;
        centers.ensureCapacity(centers.size() + 3);
        centers.add(totalX / 4.0F);
        centers.add(totalY / 4.0F);
        centers.add(totalZ / 4.0F);
        if (!this.hasDistinctNormals) {
            this.calculateNormal();
            if (this.globalNormal.x == 0.0F && this.globalNormal.y == 0.0F && this.globalNormal.z == 0.0F) {
                this.globalNormal.set(this.currentNormal);
            } else {
                float dotProduct = this.globalNormal.dot(this.currentNormal);
                if ((double) Math.abs(dotProduct) >= 0.98) {
                    if (dotProduct < 0.0F) {
                        this.normalSigns.set(currentQuadIndex);
                    }
                } else {
                    this.hasDistinctNormals = true;
                }
            }
        }
    }

    public void capture(ChunkVertexEncoder.Vertex vertex) {
        int i = this.currentVertex;
        this.vertexPositions[i].set(vertex.x, vertex.y, vertex.z);
        if (++i == 4) {
            this.captureQuad();
            i = 0;
        }
        this.currentVertex = i;
    }

    public static enum Level {

        NONE, STATIC, DYNAMIC;

        public static final TranslucentQuadAnalyzer.Level[] VALUES = values();
    }

    public static record SortState(TranslucentQuadAnalyzer.Level level, float[] centers, BitSet normalSigns, Vector3f sharedNormal) {

        public static final TranslucentQuadAnalyzer.SortState NONE = new TranslucentQuadAnalyzer.SortState(TranslucentQuadAnalyzer.Level.NONE, null, null, null);

        public boolean requiresDynamicSorting() {
            return this.level.ordinal() >= TranslucentQuadAnalyzer.Level.DYNAMIC.ordinal();
        }

        public TranslucentQuadAnalyzer.SortState compactForStorage() {
            return this.requiresDynamicSorting() ? this : new TranslucentQuadAnalyzer.SortState(this.level, null, null, null);
        }
    }
}