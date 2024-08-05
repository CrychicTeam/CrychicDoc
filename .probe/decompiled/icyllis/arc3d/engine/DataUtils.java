package icyllis.arc3d.engine;

public final class DataUtils {

    public static boolean compressionTypeIsOpaque(int compression) {
        return switch(compression) {
            case 0, 1, 2 ->
                true;
            case 3 ->
                false;
            default ->
                throw new AssertionError(compression);
        };
    }

    public static int num4x4Blocks(int size) {
        return size + 3 >> 2;
    }

    public static long numBlocks(int compression, int width, int height) {
        return switch(compression) {
            case 0 ->
                (long) width * (long) height;
            case 1, 2, 3 ->
                {
                    long numBlocksWidth = (long) num4x4Blocks(width);
                    long numBlocksHeight = (long) num4x4Blocks(height);
                    ???;
                }
            default ->
                throw new AssertionError(compression);
        };
    }

    private DataUtils() {
    }
}