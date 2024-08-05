package icyllis.arc3d.engine;

public interface Engine {

    int COLOR_ENCODING_UNORM = 0;

    int COLOR_ENCODING_UNORM_PACK16 = 1;

    int COLOR_ENCODING_UNORM_PACK32 = 2;

    int COLOR_ENCODING_UNORM_SRGB = 3;

    int COLOR_ENCODING_FLOAT = 4;

    int CLAMP_TYPE_AUTO = 0;

    int CLAMP_TYPE_MANUAL = 1;

    int CLAMP_TYPE_NONE = 2;

    int MASK_FORMAT_A8 = 0;

    int MASK_FORMAT_A565 = 1;

    int MASK_FORMAT_ARGB = 2;

    boolean Ownership_Borrowed = false;

    boolean Ownership_Owned = true;

    int INVALID_RESOURCE_HANDLE = -1;

    static int colorTypeToPublic(int ct) {
        return switch(ct) {
            case 0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22 ->
                ct;
            case 4, 11, 12, 23, 24, 25, 26, 27 ->
                0;
            default ->
                throw new AssertionError(ct);
        };
    }

    static int colorTypeEncoding(int ct) {
        return switch(ct) {
            case 0, 2, 3, 4, 5, 6, 7, 11, 13, 15, 19, 20, 22, 23, 24, 25, 27 ->
                0;
            case 1 ->
                1;
            case 8 ->
                3;
            case 9, 10 ->
                2;
            case 12, 14, 16, 17, 18, 21, 26 ->
                4;
            default ->
                throw new AssertionError(ct);
        };
    }

    static int colorTypeClampType(int ct) {
        return switch(ct) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 15, 19, 20, 22, 23, 24, 25, 27 ->
                0;
            case 12, 14, 16, 18, 21, 26 ->
                2;
            case 17 ->
                1;
            default ->
                throw new AssertionError(ct);
        };
    }

    public interface BackendApi {

        int kOpenGL = 0;

        int kVulkan = 1;

        int kMock = 2;

        static String toString(int value) {
            return switch(value) {
                case 0 ->
                    "OpenGL";
                case 1 ->
                    "Vulkan";
                case 2 ->
                    "Mock";
                default ->
                    throw new AssertionError(value);
            };
        }
    }

    public interface BudgetType {

        byte Budgeted = 0;

        byte NotBudgeted = 1;

        byte WrapCacheable = 2;
    }

    public interface BufferUsageFlags {

        int kVertex = 1;

        int kIndex = 2;

        int kTransferSrc = 4;

        int kTransferDst = 8;

        int kUniform = 16;

        int kDrawIndirect = 32;

        int kStatic = 64;

        int kDynamic = 128;

        int kStreaming = 256;
    }

    public interface GLBackendState {

        int kRenderTarget = 1;

        int kPixelStore = 2;

        int kPipeline = 4;

        int kTexture = 8;

        int kStencil = 16;

        int kRaster = 32;

        int kBlend = 64;

        int kView = 128;

        int kMisc = 256;
    }

    public interface IOType {

        int kRead = 0;

        int kWrite = 1;

        int kRW = 2;
    }

    public interface LoadOp {

        byte Load = 0;

        byte Clear = 1;

        byte DontCare = 2;

        byte Count = 3;
    }

    public interface LoadStoreOps {

        byte StoreOpShift = 4;

        byte Load_Store = 0;

        byte Clear_Store = 1;

        byte DontLoad_Store = 2;

        byte Load_DontStore = 16;

        byte Clear_DontStore = 17;

        byte DontLoad_DontStore = 18;

        static byte make(byte load, byte store) {
            ???;
        }

        static byte loadOp(byte ops) {
            return (byte) (ops & 15);
        }

        static byte storeOp(byte ops) {
            return (byte) (ops >>> 4);
        }

        static {
            ???;
        }
    }

    public interface PrimitiveType {

        byte PointList = 0;

        byte LineList = 1;

        byte LineStrip = 2;

        byte TriangleList = 3;

        byte TriangleStrip = 4;
    }

    public interface ShaderFlags {

        int kVertex = 1;

        int kFragment = 2;
    }

    public interface StoreOp {

        byte Store = 0;

        byte DontCare = 1;

        byte Count = 2;
    }

    public interface SurfaceOrigin {

        int kUpperLeft = 0;

        int kLowerLeft = 1;
    }

    public interface VertexAttribType {

        byte kFloat = 0;

        byte kFloat2 = 1;

        byte kFloat3 = 2;

        byte kFloat4 = 3;

        byte kHalf = 4;

        byte kHalf2 = 5;

        byte kHalf4 = 6;

        byte kInt2 = 7;

        byte kInt3 = 8;

        byte kInt4 = 9;

        byte kByte = 10;

        byte kByte2 = 11;

        byte kByte4 = 12;

        byte kUByte = 13;

        byte kUByte2 = 14;

        byte kUByte4 = 15;

        byte kUByte_norm = 16;

        byte kUByte4_norm = 17;

        byte kShort2 = 18;

        byte kShort4 = 19;

        byte kUShort2 = 20;

        byte kUShort2_norm = 21;

        byte kInt = 22;

        byte kUInt = 23;

        byte kUShort_norm = 24;

        byte kUShort4_norm = 25;

        byte kLast = 25;

        static int size(byte type) {
            switch(type) {
                case 0:
                    return 4;
                case 1:
                    return 8;
                case 2:
                    return 12;
                case 3:
                    return 16;
                case 4:
                case 24:
                    return 2;
                case 5:
                case 18:
                case 20:
                case 21:
                    return 4;
                case 6:
                case 19:
                case 25:
                    return 8;
                case 7:
                    return 8;
                case 8:
                    return 12;
                case 9:
                    return 16;
                case 10:
                case 13:
                case 16:
                    return 1;
                case 11:
                case 14:
                    return 2;
                case 12:
                case 15:
                case 17:
                    return 4;
                case 22:
                case 23:
                    return 4;
                default:
                    throw new AssertionError(type);
            }
        }
    }
}