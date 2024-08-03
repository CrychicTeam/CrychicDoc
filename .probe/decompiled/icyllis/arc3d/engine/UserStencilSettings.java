package icyllis.arc3d.engine;

public final class UserStencilSettings {

    public static final short DISABLED_STENCIL_FLAG = 1;

    public static final short TEST_ALWAYS_PASSES_STENCIL_FLAG = 2;

    public static final short NO_MODIFY_STENCIL_STENCIL_FLAG = 4;

    public static final short NO_WRAP_OPS_STENCIL_FLAG = 8;

    public static final short SINGLE_SIDED_STENCIL_FLAG = 16;

    public static final short LAST_STENCIL_FLAG = 16;

    public static final short ALL_STENCIL_FLAGS = 31;

    public static final short USER_STENCIL_TEST_ALWAYS_IF_IN_CLIP = 0;

    public static final short USER_STENCIL_TEST_EQUAL_IF_IN_CLIP = 1;

    public static final short USER_STENCIL_TEST_LESS_IF_IN_CLIP = 2;

    public static final short USER_STENCIL_TEST_LEQUAL_IF_IN_CLIP = 3;

    public static final short USER_STENCIL_TEST_ALWAYS = 4;

    public static final short USER_STENCIL_TEST_NEVER = 5;

    public static final short USER_STENCIL_TEST_GREATER = 6;

    public static final short USER_STENCIL_TEST_GEQUAL = 7;

    public static final short USER_STENCIL_TEST_LESS = 8;

    public static final short USER_STENCIL_TEST_LEQUAL = 9;

    public static final short USER_STENCIL_TEST_EQUAL = 10;

    public static final short USER_STENCIL_TEST_NOTEQUAL = 11;

    public static final short LAST_CLIPPED_STENCIL_TEST = 3;

    public static final int USER_STENCIL_TEST_COUNT = 12;

    public static final byte USER_STENCIL_OP_KEEP = 0;

    public static final byte USER_STENCIL_OP_ZERO = 1;

    public static final byte USER_STENCIL_OP_REPLACE = 2;

    public static final byte USER_STENCIL_OP_INVERT = 3;

    public static final byte USER_STENCIL_OP_INC_WRAP = 4;

    public static final byte USER_STENCIL_OP_DEC_WRAP = 5;

    public static final byte USER_STENCIL_OP_INC_MAYBE_CLAMP = 6;

    public static final byte USER_STENCIL_OP_DEC_MAYBE_CLAMP = 7;

    public static final byte USER_STENCIL_OP_ZERO_CLIP_BIT = 8;

    public static final byte USER_STENCIL_OP_SET_CLIP_BIT = 9;

    public static final byte USER_STENCIL_OP_INVERT_CLIP_BIT = 10;

    public static final byte USER_STENCIL_OP_SET_CLIP_AND_REPLACE_USER_BITS = 11;

    public static final byte USER_STENCIL_OP_ZERO_CLIP_AND_USER_BITS = 12;

    public static final byte LAST_USER_ONLY_STENCIL_OP = 7;

    public static final byte LAST_CLIP_ONLY_STENCIL_OP = 10;

    public static final int USER_STENCIL_OP_COUNT = 13;

    public final short mCWFlags;

    public final short mCWFlags2;

    public final StencilFaceSettings mCWFace;

    public final short mCCWFlags;

    public final short mCCWFlags2;

    public final StencilFaceSettings mCCWFace;

    public UserStencilSettings(short ref, short test, short testMask, byte passOp, byte failOp, short writeMask) {
        this.mCWFlags = (short) (flags(test, passOp, failOp, false) | 16);
        this.mCWFlags2 = (short) (flags(test, passOp, failOp, true) | 16);
        this.mCWFace = new StencilFaceSettings(ref, test, effectiveTestMask(test, testMask), passOp, failOp, effectiveWriteMask(test, passOp, failOp, writeMask));
        this.mCCWFlags = this.mCWFlags;
        this.mCCWFlags2 = this.mCWFlags2;
        this.mCCWFace = this.mCWFace;
    }

    public UserStencilSettings(short cwRef, short ccwRef, short cwTest, short ccwTest, short cwTestMask, short ccwTestMask, byte cwPassOp, byte ccwPassOp, byte cwFailOp, byte ccwFailOp, short cwWriteMask, short ccwWriteMask) {
        this.mCWFlags = flags(cwTest, cwPassOp, cwFailOp, false);
        this.mCWFlags2 = flags(cwTest, cwPassOp, cwFailOp, true);
        this.mCWFace = new StencilFaceSettings(cwRef, cwTest, effectiveTestMask(cwTest, cwTestMask), cwPassOp, cwFailOp, effectiveWriteMask(cwTest, cwPassOp, cwFailOp, cwWriteMask));
        this.mCCWFlags = flags(ccwTest, ccwPassOp, ccwFailOp, false);
        this.mCCWFlags2 = flags(ccwTest, ccwPassOp, ccwFailOp, true);
        this.mCCWFace = new StencilFaceSettings(ccwRef, ccwTest, effectiveTestMask(ccwTest, ccwTestMask), ccwPassOp, ccwFailOp, effectiveWriteMask(ccwTest, ccwPassOp, ccwFailOp, ccwWriteMask));
    }

    public short flags(boolean hasStencilClip) {
        return (short) ((hasStencilClip ? this.mCWFlags2 : this.mCWFlags) & (hasStencilClip ? this.mCCWFlags2 : this.mCCWFlags));
    }

    public boolean isDisabled(boolean hasStencilClip) {
        return (this.flags(hasStencilClip) & 1) != 0;
    }

    public boolean testAlwaysPasses(boolean hasStencilClip) {
        return (this.flags(hasStencilClip) & 2) != 0;
    }

    public boolean isDoubleSided(boolean hasStencilClip) {
        return (this.flags(hasStencilClip) & 16) == 0;
    }

    public boolean usesWrapOp(boolean hasStencilClip) {
        return (this.flags(hasStencilClip) & 8) == 0;
    }

    private static boolean testAlwaysPasses(short test, boolean hasStencilClip) {
        return !hasStencilClip && 0 == test || 4 == test;
    }

    private static boolean doesNotModifyStencil(short test, byte passOp, byte failOp, boolean hasStencilClip) {
        return (5 == test || 0 == passOp) && (testAlwaysPasses(test, hasStencilClip) || 0 == failOp);
    }

    private static boolean isDisabled(short test, byte passOp, byte failOp, boolean hasStencilClip) {
        return testAlwaysPasses(test, hasStencilClip) && doesNotModifyStencil(test, passOp, failOp, hasStencilClip);
    }

    private static boolean usesWrapOps(byte passOp, byte failOp) {
        return 4 == passOp || 5 == passOp || 4 == failOp || 5 == failOp;
    }

    private static boolean testIgnoresRef(short test) {
        return 0 == test || 4 == test || 5 == test;
    }

    private static short flags(short test, byte passOp, byte failOp, boolean hasStencilClip) {
        return (short) ((isDisabled(test, passOp, failOp, hasStencilClip) ? 1 : 0) | (testAlwaysPasses(test, hasStencilClip) ? 2 : 0) | (doesNotModifyStencil(test, passOp, failOp, hasStencilClip) ? 4 : 0) | (usesWrapOps(passOp, failOp) ? 0 : 8));
    }

    private static short effectiveTestMask(short test, short testMask) {
        return testIgnoresRef(test) ? 0 : testMask;
    }

    private static short effectiveWriteMask(short test, byte passOp, byte failOp, short writeMask) {
        return doesNotModifyStencil(test, passOp, failOp, true) ? 0 : writeMask;
    }
}