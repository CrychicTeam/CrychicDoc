package me.jellysquid.mods.sodium.client.model.quad.properties;

public enum ModelQuadOrientation {

    NORMAL(new int[] { 0, 1, 2, 3 }), FLIP(new int[] { 1, 2, 3, 0 });

    private final int[] indices;

    private ModelQuadOrientation(int[] indices) {
        this.indices = indices;
    }

    public int getVertexIndex(int idx) {
        return this.indices[idx];
    }

    public static ModelQuadOrientation orientByBrightness(float[] brightnesses, int[] lightmaps) {
        float br02 = brightnesses[0] + brightnesses[2];
        float br13 = brightnesses[1] + brightnesses[3];
        if (br02 > br13) {
            return NORMAL;
        } else if (br02 < br13) {
            return FLIP;
        } else {
            int lm02 = lightmaps[0] + lightmaps[2];
            int lm13 = lightmaps[1] + lightmaps[3];
            return lm02 <= lm13 ? NORMAL : FLIP;
        }
    }
}