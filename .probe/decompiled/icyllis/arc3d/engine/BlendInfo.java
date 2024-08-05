package icyllis.arc3d.engine;

public record BlendInfo(int equation, int srcFactor, int dstFactor, float constantR, float constantG, float constantB, float constantA, boolean writeColor) {

    public static final BlendInfo SRC = new BlendInfo(0, 1, 0, 0.0F, 0.0F, 0.0F, 0.0F, true);
}