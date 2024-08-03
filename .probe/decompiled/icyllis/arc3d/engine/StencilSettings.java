package icyllis.arc3d.engine;

public final class StencilSettings {

    private static final short INVALID_PRIVATE_FLAG = 32;

    public static final short STENCIL_TEST_ALWAYS = 0;

    public static final short STENCIL_TEST_NEVER = 1;

    public static final short STENCIL_TEST_GREATER = 2;

    public static final short STENCIL_TEST_GEQUAL = 3;

    public static final short STENCIL_TEST_LESS = 4;

    public static final short STENCIL_TEST_LEQUAL = 5;

    public static final short STENCIL_TEST_EQUAL = 6;

    public static final short STENCIL_TEST_NOTEQUAL = 7;

    public static final int STENCIL_TEST_COUNT = 8;

    public static final byte STENCIL_OP_KEEP = 0;

    public static final byte STENCIL_OP_ZERO = 1;

    public static final byte STENCIL_OP_REPLACE = 2;

    public static final byte STENCIL_OP_INVERT = 3;

    public static final byte STENCIL_OP_INC_WRAP = 4;

    public static final byte STENCIL_OP_DEC_WRAP = 5;

    public static final byte STENCIL_OP_INC_CLAMP = 6;

    public static final byte STENCIL_OP_DEC_CLAMP = 7;

    public static final int STENCIL_OP_COUNT = 8;

    private int mFlags;

    private StencilFaceSettings mCWFace;

    private StencilFaceSettings mCCWFace;
}