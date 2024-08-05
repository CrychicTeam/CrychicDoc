package icyllis.arc3d.compiler;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public final class IntrinsicList {

    public static final int kNotIntrinsic = -1;

    public static final int kRound = 0;

    public static final int kRoundEven = 1;

    public static final int kTrunc = 2;

    public static final int kAbs = 3;

    public static final int kSign = 4;

    public static final int kFloor = 5;

    public static final int kCeil = 6;

    public static final int kFract = 7;

    public static final int kRadians = 8;

    public static final int kDegrees = 9;

    public static final int kSin = 10;

    public static final int kCos = 11;

    public static final int kTan = 12;

    public static final int kAsin = 13;

    public static final int kAcos = 14;

    public static final int kAtan = 15;

    public static final int kSinh = 16;

    public static final int kCosh = 17;

    public static final int kTanh = 18;

    public static final int kAsinh = 19;

    public static final int kAcosh = 20;

    public static final int kAtanh = 21;

    public static final int kPow = 22;

    public static final int kExp = 23;

    public static final int kLog = 24;

    public static final int kExp2 = 25;

    public static final int kLog2 = 26;

    public static final int kSqrt = 27;

    public static final int kInverseSqrt = 28;

    public static final int kMod = 29;

    public static final int kModf = 30;

    public static final int kMin = 31;

    public static final int kMax = 32;

    public static final int kClamp = 33;

    public static final int kSaturate = 34;

    public static final int kMix = 35;

    public static final int kStep = 36;

    public static final int kSmoothStep = 37;

    public static final int kIsNan = 38;

    public static final int kIsInf = 39;

    public static final int kFloatBitsToInt = 40;

    public static final int kFloatBitsToUint = 41;

    public static final int kIntBitsToFloat = 42;

    public static final int kUintBitsToFloat = 43;

    public static final int kFma = 44;

    public static final int kFrexp = 45;

    public static final int kLdexp = 46;

    public static final int kPackSnorm4x8 = 47;

    public static final int kPackUnorm4x8 = 48;

    public static final int kPackSnorm2x16 = 49;

    public static final int kPackUnorm2x16 = 50;

    public static final int kPackHalf2x16 = 51;

    public static final int kPackDouble2x32 = 52;

    public static final int kUnpackSnorm4x8 = 53;

    public static final int kUnpackUnorm4x8 = 54;

    public static final int kUnpackSnorm2x16 = 55;

    public static final int kUnpackUnorm2x16 = 56;

    public static final int kUnpackHalf2x16 = 57;

    public static final int kUnpackDouble2x32 = 58;

    public static final int kLength = 59;

    public static final int kDistance = 60;

    public static final int kDot = 61;

    public static final int kCross = 62;

    public static final int kNormalize = 63;

    public static final int kFaceForward = 64;

    public static final int kReflect = 65;

    public static final int kRefract = 66;

    public static final int kAny = 67;

    public static final int kAll = 68;

    public static final int kLogicalNot = 69;

    public static final int kEqual = 70;

    public static final int kNotEqual = 71;

    public static final int kLessThan = 72;

    public static final int kGreaterThan = 73;

    public static final int kLessThanEqual = 74;

    public static final int kGreaterThanEqual = 75;

    public static final int kMatrixCompMult = 76;

    public static final int kOuterProduct = 77;

    public static final int kDeterminant = 78;

    public static final int kMatrixInverse = 79;

    public static final int kTranspose = 80;

    public static final int kDPdx = 81;

    public static final int kDPdy = 82;

    public static final int kFwidth = 83;

    public static final int kDPdxFine = 84;

    public static final int kDPdyFine = 85;

    public static final int kFwidthFine = 86;

    public static final int kDPdxCoarse = 87;

    public static final int kDPdyCoarse = 88;

    public static final int kFwidthCoarse = 89;

    public static final int kInterpolateAtCentroid = 90;

    public static final int kInterpolateAtSample = 91;

    public static final int kInterpolateAtOffset = 92;

    public static final int kAddCarry = 93;

    public static final int kAddBorrow = 94;

    public static final int kUMulExtended = 95;

    public static final int kIMulExtended = 96;

    public static final int kBitfieldExtract = 97;

    public static final int kBitfieldInsert = 98;

    public static final int kBitReverse = 99;

    public static final int kBitCount = 100;

    public static final int kFindLSB = 101;

    public static final int kFindMSB = 102;

    public static final int kAtomicAdd = 103;

    public static final int kAtomicMin = 104;

    public static final int kAtomicMax = 105;

    public static final int kAtomicAnd = 106;

    public static final int kAtomicOr = 107;

    public static final int kAtomicXor = 108;

    public static final int kAtomicExchange = 109;

    public static final int kAtomicCompSwap = 110;

    public static final int kBarrier = 111;

    public static final int kMemoryBarrier = 112;

    public static final int kMemoryBarrierBuffer = 113;

    public static final int kMemoryBarrierShared = 114;

    public static final int kMemoryBarrierImage = 115;

    public static final int kWorkgroupBarrier = 116;

    public static final int kAnyInvocation = 117;

    public static final int kAllInvocations = 118;

    public static final int kAllInvocationsEqual = 119;

    public static final int kTextureQuerySize = 120;

    public static final int kTextureQueryLod = 121;

    public static final int kTextureQueryLevels = 122;

    public static final int kTextureQuerySamples = 123;

    public static final int kTexture = 124;

    public static final int kTextureProj = 125;

    public static final int kTextureLod = 126;

    public static final int kTextureOffset = 127;

    public static final int kTextureFetch = 128;

    public static final int kTextureFetchOffset = 129;

    public static final int kTextureProjOffset = 130;

    public static final int kTextureLodOffset = 131;

    public static final int kTextureProjLod = 132;

    public static final int kTextureProjLodOffset = 133;

    public static final int kTextureGrad = 134;

    public static final int kTextureGradOffset = 135;

    public static final int kTextureProjGrad = 136;

    public static final int kTextureProjGradOffset = 137;

    public static final int kTextureGather = 138;

    public static final int kTextureGatherOffset = 139;

    public static final int kTextureGatherOffsets = 140;

    public static final int kImageQuerySize = 141;

    public static final int kImageQuerySamples = 142;

    public static final int kImageLoad = 143;

    public static final int kImageStore = 144;

    public static final int kImageAtomicAdd = 145;

    public static final int kImageAtomicMin = 146;

    public static final int kImageAtomicMax = 147;

    public static final int kImageAtomicAnd = 148;

    public static final int kImageAtomicOr = 149;

    public static final int kImageAtomicXor = 150;

    public static final int kImageAtomicExchange = 151;

    public static final int kImageAtomicCompSwap = 152;

    public static final int kSubpassLoad = 153;

    private static final Object2IntOpenHashMap<String> sIntrinsicMap;

    public static int findIntrinsicKind(String name) {
        return sIntrinsicMap.getInt(name);
    }

    private IntrinsicList() {
    }

    static {
        Object2IntOpenHashMap<String> map = new Object2IntOpenHashMap(191);
        map.defaultReturnValue(-1);
        map.put("round", 0);
        map.put("roundEven", 1);
        map.put("trunc", 2);
        map.put("abs", 3);
        map.put("sign", 4);
        map.put("floor", 5);
        map.put("ceil", 6);
        map.put("fract", 7);
        map.put("radians", 8);
        map.put("degrees", 9);
        map.put("sin", 10);
        map.put("cos", 11);
        map.put("tan", 12);
        map.put("asin", 13);
        map.put("acos", 14);
        map.put("atan", 15);
        map.put("sinh", 16);
        map.put("cosh", 17);
        map.put("tanh", 18);
        map.put("asinh", 19);
        map.put("acosh", 20);
        map.put("atanh", 21);
        map.put("pow", 22);
        map.put("exp", 23);
        map.put("log", 24);
        map.put("exp2", 25);
        map.put("log2", 26);
        map.put("sqrt", 27);
        map.put("inversesqrt", 28);
        map.put("mod", 29);
        map.put("modf", 30);
        map.put("min", 31);
        map.put("max", 32);
        map.put("clamp", 33);
        map.put("saturate", 34);
        map.put("mix", 35);
        map.put("step", 36);
        map.put("smoothstep", 37);
        map.put("isnan", 38);
        map.put("isinf", 39);
        map.put("floatBitsToInt", 40);
        map.put("floatBitsToUint", 41);
        map.put("intBitsToFloat", 42);
        map.put("uintBitsToFloat", 43);
        map.put("fma", 44);
        map.put("frexp", 45);
        map.put("ldexp", 46);
        map.put("packSnorm4x8", 47);
        map.put("packUnorm4x8", 48);
        map.put("packSnorm2x16", 49);
        map.put("packUnorm2x16", 50);
        map.put("packHalf2x16", 51);
        map.put("packDouble2x32", 52);
        map.put("unpackSnorm4x8", 53);
        map.put("unpackUnorm4x8", 54);
        map.put("unpackSnorm2x16", 55);
        map.put("unpackUnorm2x16", 56);
        map.put("unpackHalf2x16", 57);
        map.put("unpackDouble2x32", 58);
        map.put("length", 59);
        map.put("distance", 60);
        map.put("dot", 61);
        map.put("cross", 62);
        map.put("normalize", 63);
        map.put("faceforward", 64);
        map.put("reflect", 65);
        map.put("refract", 66);
        map.put("any", 67);
        map.put("all", 68);
        map.put("not", 69);
        map.put("equal", 70);
        map.put("notEqual", 71);
        map.put("lessThan", 72);
        map.put("greaterThan", 73);
        map.put("lessThanEqual", 74);
        map.put("greaterThanEqual", 75);
        map.put("matrixCompMult", 76);
        map.put("outerProduct", 77);
        map.put("determinant", 78);
        map.put("inverse", 79);
        map.put("transpose", 80);
        map.put("dFdx", 81);
        map.put("dFdy", 82);
        map.put("fwidth", 83);
        map.put("dFdxFine", 84);
        map.put("dFdyFine", 85);
        map.put("fwidthFine", 86);
        map.put("dFdxCoarse", 87);
        map.put("dFdyCoarse", 88);
        map.put("fwidthCoarse", 89);
        sIntrinsicMap = map;
    }
}