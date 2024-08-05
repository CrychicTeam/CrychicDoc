package icyllis.arc3d.core;

import javax.annotation.Nonnull;

public final class SLDataType {

    public static final byte kVoid = 0;

    public static final byte kBool = 1;

    public static final byte kBool2 = 2;

    public static final byte kBool3 = 3;

    public static final byte kBool4 = 4;

    public static final byte kShort = 5;

    public static final byte kShort2 = 6;

    public static final byte kShort3 = 7;

    public static final byte kShort4 = 8;

    public static final byte kUShort = 9;

    public static final byte kUShort2 = 10;

    public static final byte kUShort3 = 11;

    public static final byte kUShort4 = 12;

    public static final byte kFloat = 13;

    public static final byte kFloat2 = 14;

    public static final byte kFloat3 = 15;

    public static final byte kFloat4 = 16;

    public static final byte kFloat2x2 = 17;

    public static final byte kFloat3x3 = 18;

    public static final byte kFloat4x4 = 19;

    public static final byte kHalf = 20;

    public static final byte kHalf2 = 21;

    public static final byte kHalf3 = 22;

    public static final byte kHalf4 = 23;

    public static final byte kHalf2x2 = 24;

    public static final byte kHalf3x3 = 25;

    public static final byte kHalf4x4 = 26;

    public static final byte kInt = 27;

    public static final byte kInt2 = 28;

    public static final byte kInt3 = 29;

    public static final byte kInt4 = 30;

    public static final byte kUInt = 31;

    public static final byte kUInt2 = 32;

    public static final byte kUInt3 = 33;

    public static final byte kUInt4 = 34;

    public static final byte kSampler2D = 35;

    public static final byte kTexture2D = 36;

    public static final byte kSampler = 37;

    public static final byte kSubpassInput = 38;

    public static final byte kLast = 38;

    public static boolean checkSLType(byte type) {
        return type >= 0 && type <= 38;
    }

    public static boolean isFloatType(byte type) {
        switch(type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                return false;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
                throw new IllegalArgumentException(String.valueOf(type));
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
                return true;
        }
    }

    public static boolean isIntegralType(byte type) {
        switch(type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 35:
            case 36:
            case 37:
            case 38:
                return false;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
                throw new IllegalArgumentException(String.valueOf(type));
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
                return true;
        }
    }

    public static boolean isBooleanType(byte type) {
        switch(type) {
            case 0:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                return false;
            case 1:
            case 2:
            case 3:
            case 4:
                return true;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
                throw new IllegalArgumentException(String.valueOf(type));
        }
    }

    public static boolean canBeUniformValue(byte type) {
        return isFloatType(type) || isIntegralType(type);
    }

    public static int vectorDim(byte type) {
        switch(type) {
            case 0:
            case 17:
            case 18:
            case 19:
            case 35:
            case 36:
            case 37:
            case 38:
                return -1;
            case 1:
            case 13:
            case 27:
            case 31:
                return 1;
            case 2:
            case 14:
            case 28:
            case 32:
                return 2;
            case 3:
            case 15:
            case 29:
            case 33:
                return 3;
            case 4:
            case 16:
            case 30:
            case 34:
                return 4;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
                throw new IllegalArgumentException(String.valueOf(type));
        }
    }

    public static int matrixOrder(byte type) {
        switch(type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 13:
            case 14:
            case 15:
            case 16:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                return -1;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
                throw new IllegalArgumentException(String.valueOf(type));
            case 17:
                return 2;
            case 18:
                return 3;
            case 19:
                return 4;
        }
    }

    public static boolean isCombinedSamplerType(byte type) {
        switch(type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 36:
            case 37:
            case 38:
                return false;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
                throw new IllegalArgumentException(String.valueOf(type));
            case 35:
                return true;
        }
    }

    public static boolean isMatrixType(byte type) {
        switch(type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 13:
            case 14:
            case 15:
            case 16:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                return false;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
                throw new IllegalArgumentException(String.valueOf(type));
            case 17:
            case 18:
            case 19:
                return true;
        }
    }

    public static int locations(byte type) {
        switch(type) {
            case 0:
            case 35:
            case 36:
            case 37:
            case 38:
                return 0;
            case 1:
            case 2:
            case 3:
            case 4:
            case 13:
            case 14:
            case 15:
            case 16:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
                return 1;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
                throw new IllegalArgumentException(String.valueOf(type));
            case 17:
                return 2;
            case 18:
                return 3;
            case 19:
                return 4;
        }
    }

    @Nonnull
    public static String typeString(byte type) {
        switch(type) {
            case 0:
                return "void";
            case 1:
                return "bool";
            case 2:
                return "bvec2";
            case 3:
                return "bvec3";
            case 4:
                return "bvec4";
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
                throw new IllegalArgumentException(String.valueOf(type));
            case 13:
                return "float";
            case 14:
                return "vec2";
            case 15:
                return "vec3";
            case 16:
                return "vec4";
            case 17:
                return "mat2";
            case 18:
                return "mat3";
            case 19:
                return "mat4";
            case 27:
                return "int";
            case 28:
                return "ivec2";
            case 29:
                return "ivec3";
            case 30:
                return "ivec4";
            case 31:
                return "uint";
            case 32:
                return "uvec2";
            case 33:
                return "uvec3";
            case 34:
                return "uvec4";
            case 35:
                return "sampler2D";
            case 36:
                return "texture2D";
            case 37:
                return "sampler";
            case 38:
                return "subpassInput";
        }
    }
}