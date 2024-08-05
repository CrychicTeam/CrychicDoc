package icyllis.arc3d.compiler;

import icyllis.arc3d.compiler.tree.Type;

public final class BuiltinTypes {

    public final Type mVoid = Type.makeSpecialType("void", "v", (byte) 8);

    public final Type mBool = Type.makeScalarType("bool", "b", (byte) 3, 0, 1);

    public final Type mBool2 = Type.makeVectorType("bool2", "b2", this.mBool, 2);

    public final Type mBool3 = Type.makeVectorType("bool3", "b3", this.mBool, 3);

    public final Type mBool4 = Type.makeVectorType("bool4", "b4", this.mBool, 4);

    public final Type mShort = Type.makeScalarType("short", "s", (byte) 1, 3, 16, 32);

    public final Type mShort2 = Type.makeVectorType("short2", "s2", this.mShort, 2);

    public final Type mShort3 = Type.makeVectorType("short3", "s3", this.mShort, 3);

    public final Type mShort4 = Type.makeVectorType("short4", "s4", this.mShort, 4);

    public final Type mUShort = Type.makeScalarType("ushort", "S", (byte) 2, 4, 16, 32);

    public final Type mUShort2 = Type.makeVectorType("ushort2", "S2", this.mUShort, 2);

    public final Type mUShort3 = Type.makeVectorType("ushort3", "S3", this.mUShort, 3);

    public final Type mUShort4 = Type.makeVectorType("ushort4", "S4", this.mUShort, 4);

    public final Type mInt = Type.makeScalarType("int", "i", (byte) 1, 6, 32);

    public final Type mInt2 = Type.makeVectorType("int2", "i2", this.mInt, 2);

    public final Type mInt3 = Type.makeVectorType("int3", "i3", this.mInt, 3);

    public final Type mInt4 = Type.makeVectorType("int4", "i4", this.mInt, 4);

    public final Type mUInt = Type.makeScalarType("uint", "I", (byte) 2, 7, 32);

    public final Type mUInt2 = Type.makeVectorType("uint2", "I2", this.mUInt, 2);

    public final Type mUInt3 = Type.makeVectorType("uint3", "I3", this.mUInt, 3);

    public final Type mUInt4 = Type.makeVectorType("uint4", "I4", this.mUInt, 4);

    public final Type mHalf = Type.makeScalarType("half", "h", (byte) 0, 9, 16, 32);

    public final Type mHalf2 = Type.makeVectorType("half2", "h2", this.mHalf, 2);

    public final Type mHalf3 = Type.makeVectorType("half3", "h3", this.mHalf, 3);

    public final Type mHalf4 = Type.makeVectorType("half4", "h4", this.mHalf, 4);

    public final Type mFloat = Type.makeScalarType("float", "f", (byte) 0, 10, 32);

    public final Type mFloat2 = Type.makeVectorType("float2", "f2", this.mFloat, 2);

    public final Type mFloat3 = Type.makeVectorType("float3", "f3", this.mFloat, 3);

    public final Type mFloat4 = Type.makeVectorType("float4", "f4", this.mFloat, 4);

    public final Type mHalf2x2 = Type.makeMatrixType("half2x2", "h22", this.mHalf2, 2);

    public final Type mHalf2x3 = Type.makeMatrixType("half2x3", "h23", this.mHalf3, 2);

    public final Type mHalf2x4 = Type.makeMatrixType("half2x4", "h24", this.mHalf4, 2);

    public final Type mHalf3x2 = Type.makeMatrixType("half3x2", "h32", this.mHalf2, 3);

    public final Type mHalf3x3 = Type.makeMatrixType("half3x3", "h33", this.mHalf3, 3);

    public final Type mHalf3x4 = Type.makeMatrixType("half3x4", "h34", this.mHalf4, 3);

    public final Type mHalf4x2 = Type.makeMatrixType("half4x2", "h42", this.mHalf2, 4);

    public final Type mHalf4x3 = Type.makeMatrixType("half4x3", "h43", this.mHalf3, 4);

    public final Type mHalf4x4 = Type.makeMatrixType("half4x4", "h44", this.mHalf4, 4);

    public final Type mFloat2x2 = Type.makeMatrixType("float2x2", "f22", this.mFloat2, 2);

    public final Type mFloat2x3 = Type.makeMatrixType("float2x3", "f23", this.mFloat3, 2);

    public final Type mFloat2x4 = Type.makeMatrixType("float2x4", "f24", this.mFloat4, 2);

    public final Type mFloat3x2 = Type.makeMatrixType("float3x2", "f32", this.mFloat2, 3);

    public final Type mFloat3x3 = Type.makeMatrixType("float3x3", "f33", this.mFloat3, 3);

    public final Type mFloat3x4 = Type.makeMatrixType("float3x4", "f34", this.mFloat4, 3);

    public final Type mFloat4x2 = Type.makeMatrixType("float4x2", "f42", this.mFloat2, 4);

    public final Type mFloat4x3 = Type.makeMatrixType("float4x3", "f43", this.mFloat3, 4);

    public final Type mFloat4x4 = Type.makeMatrixType("float4x4", "f44", this.mFloat4, 4);

    public final Type mVec2 = Type.makeAliasType("vec2", this.mFloat2);

    public final Type mVec3 = Type.makeAliasType("vec3", this.mFloat3);

    public final Type mVec4 = Type.makeAliasType("vec4", this.mFloat4);

    public final Type mBVec2 = Type.makeAliasType("bvec2", this.mBool2);

    public final Type mBVec3 = Type.makeAliasType("bvec3", this.mBool3);

    public final Type mBVec4 = Type.makeAliasType("bvec4", this.mBool4);

    public final Type mIVec2 = Type.makeAliasType("ivec2", this.mInt2);

    public final Type mIVec3 = Type.makeAliasType("ivec3", this.mInt3);

    public final Type mIVec4 = Type.makeAliasType("ivec4", this.mInt4);

    public final Type mUVec2 = Type.makeAliasType("uvec2", this.mUInt2);

    public final Type mUVec3 = Type.makeAliasType("uvec3", this.mUInt3);

    public final Type mUVec4 = Type.makeAliasType("uvec4", this.mUInt4);

    public final Type mMin16Int = Type.makeAliasType("min16int", this.mShort);

    public final Type mMin16Int2 = Type.makeAliasType("min16int2", this.mShort2);

    public final Type mMin16Int3 = Type.makeAliasType("min16int3", this.mShort3);

    public final Type mMin16Int4 = Type.makeAliasType("min16int4", this.mShort4);

    public final Type mMin16UInt = Type.makeAliasType("min16uint", this.mUShort);

    public final Type mMin16UInt2 = Type.makeAliasType("min16uint2", this.mUShort2);

    public final Type mMin16UInt3 = Type.makeAliasType("min16uint3", this.mUShort3);

    public final Type mMin16UInt4 = Type.makeAliasType("min16uint4", this.mUShort4);

    public final Type mMin16Float = Type.makeAliasType("min16float", this.mHalf);

    public final Type mMin16Float2 = Type.makeAliasType("min16float2", this.mHalf2);

    public final Type mMin16Float3 = Type.makeAliasType("min16float3", this.mHalf3);

    public final Type mMin16Float4 = Type.makeAliasType("min16float4", this.mHalf4);

    public final Type mInt32 = Type.makeAliasType("int32_t", this.mInt);

    public final Type mI32Vec2 = Type.makeAliasType("i32vec2", this.mInt2);

    public final Type mI32Vec3 = Type.makeAliasType("i32vec3", this.mInt3);

    public final Type mI32Vec4 = Type.makeAliasType("i32vec4", this.mInt4);

    public final Type mUInt32 = Type.makeAliasType("uint32_t", this.mUInt);

    public final Type mU32Vec2 = Type.makeAliasType("u32vec2", this.mUInt2);

    public final Type mU32Vec3 = Type.makeAliasType("u32vec3", this.mUInt3);

    public final Type mU32Vec4 = Type.makeAliasType("u32vec4", this.mUInt4);

    public final Type mFloat32 = Type.makeAliasType("float32_t", this.mFloat);

    public final Type mF32Vec2 = Type.makeAliasType("f32vec2", this.mFloat2);

    public final Type mF32Vec3 = Type.makeAliasType("f32vec3", this.mFloat3);

    public final Type mF32Vec4 = Type.makeAliasType("f32vec4", this.mFloat4);

    public final Type mMat2 = Type.makeAliasType("mat2", this.mFloat2x2);

    public final Type mMat3 = Type.makeAliasType("mat3", this.mFloat3x3);

    public final Type mMat4 = Type.makeAliasType("mat4", this.mFloat4x4);

    public final Type mMat2x2 = Type.makeAliasType("mat2x2", this.mFloat2x2);

    public final Type mMat2x3 = Type.makeAliasType("mat2x3", this.mFloat2x3);

    public final Type mMat2x4 = Type.makeAliasType("mat2x4", this.mFloat2x4);

    public final Type mMat3x2 = Type.makeAliasType("mat3x2", this.mFloat3x2);

    public final Type mMat3x3 = Type.makeAliasType("mat3x3", this.mFloat3x3);

    public final Type mMat3x4 = Type.makeAliasType("mat3x4", this.mFloat3x4);

    public final Type mMat4x2 = Type.makeAliasType("mat4x2", this.mFloat4x2);

    public final Type mMat4x3 = Type.makeAliasType("mat4x3", this.mFloat4x3);

    public final Type mMat4x4 = Type.makeAliasType("mat4x4", this.mFloat4x4);

    public final Type mF32Mat2 = Type.makeAliasType("f32mat2", this.mFloat2x2);

    public final Type mF32Mat3 = Type.makeAliasType("f32mat3", this.mFloat3x3);

    public final Type mF32Mat4 = Type.makeAliasType("f32mat4", this.mFloat4x4);

    public final Type mF32Mat2x2 = Type.makeAliasType("f32mat2x2", this.mFloat2x2);

    public final Type mF32Mat2x3 = Type.makeAliasType("f32mat2x3", this.mFloat2x3);

    public final Type mF32Mat2x4 = Type.makeAliasType("f32mat2x4", this.mFloat2x4);

    public final Type mF32Mat3x2 = Type.makeAliasType("f32mat3x2", this.mFloat3x2);

    public final Type mF32Mat3x3 = Type.makeAliasType("f32mat3x3", this.mFloat3x3);

    public final Type mF32Mat3x4 = Type.makeAliasType("f32mat3x4", this.mFloat3x4);

    public final Type mF32Mat4x2 = Type.makeAliasType("f32mat4x2", this.mFloat4x2);

    public final Type mF32Mat4x3 = Type.makeAliasType("f32mat4x3", this.mFloat4x3);

    public final Type mF32Mat4x4 = Type.makeAliasType("f32mat4x4", this.mFloat4x4);

    public final Type mImage1D = Type.makeImageType("image1D", "M1", this.mHalf, 0, false, false);

    public final Type mImage2D = Type.makeImageType("image2D", "M2", this.mHalf, 1, false, false);

    public final Type mImage3D = Type.makeImageType("image3D", "M3", this.mHalf, 2, false, false);

    public final Type mImageCube = Type.makeImageType("imageCube", "MC", this.mHalf, 3, false, false);

    public final Type mImageBuffer = Type.makeImageType("imageBuffer", "MB", this.mHalf, 5, false, false);

    public final Type mImage1DArray = Type.makeImageType("image1DArray", "M1A", this.mHalf, 0, true, false);

    public final Type mImage2DArray = Type.makeImageType("image2DArray", "M2A", this.mHalf, 1, true, false);

    public final Type mImageCubeArray = Type.makeImageType("imageCubeArray", "MCA", this.mHalf, 3, true, false);

    public final Type mImage2DMS = Type.makeImageType("image2DMS", "MM", this.mHalf, 1, false, true);

    public final Type mImage2DMSArray = Type.makeImageType("image2DMSArray", "MMA", this.mHalf, 1, true, true);

    public final Type mSubpassInput = Type.makeImageType("subpassInput", "MP", this.mHalf, 6, false, false);

    public final Type mSubpassInputMS = Type.makeImageType("subpassInputMS", "MPM", this.mHalf, 6, false, true);

    public final Type mTexture1D = Type.makeTextureType("texture1D", "T1", this.mHalf, 0, false, false);

    public final Type mTexture2D = Type.makeTextureType("texture2D", "T2", this.mHalf, 1, false, false);

    public final Type mTexture3D = Type.makeTextureType("texture3D", "T3", this.mHalf, 2, false, false);

    public final Type mTextureCube = Type.makeTextureType("textureCube", "TC", this.mHalf, 3, false, false);

    public final Type mTextureBuffer = Type.makeTextureType("textureBuffer", "TB", this.mHalf, 5, false, false);

    public final Type mTexture1DArray = Type.makeTextureType("texture1DArray", "T1A", this.mHalf, 0, true, false);

    public final Type mTexture2DArray = Type.makeTextureType("texture2DArray", "T2A", this.mHalf, 1, true, false);

    public final Type mTextureCubeArray = Type.makeTextureType("textureCubeArray", "TCA", this.mHalf, 3, true, false);

    public final Type mTexture2DMS = Type.makeTextureType("texture2DMS", "TM", this.mHalf, 1, false, true);

    public final Type mTexture2DMSArray = Type.makeTextureType("texture2DMSArray", "TMA", this.mHalf, 1, true, true);

    public final Type mSampler = Type.makeSeparateType("sampler", "ss", this.mVoid, false);

    public final Type mSamplerShadow = Type.makeSeparateType("samplerShadow", "sss", this.mVoid, true);

    public final Type mSampler1D = Type.makeCombinedType("sampler1D", "Z1", this.mHalf, 0, false, false, false);

    public final Type mSampler2D = Type.makeCombinedType("sampler2D", "Z2", this.mHalf, 1, false, false, false);

    public final Type mSampler3D = Type.makeCombinedType("sampler3D", "Z3", this.mHalf, 2, false, false, false);

    public final Type mSamplerCube = Type.makeCombinedType("samplerCube", "ZC", this.mHalf, 3, false, false, false);

    public final Type mSamplerBuffer = Type.makeCombinedType("samplerBuffer", "ZB", this.mHalf, 5, false, false, false);

    public final Type mSampler1DArray = Type.makeCombinedType("sampler1DArray", "Z1A", this.mHalf, 0, false, true, false);

    public final Type mSampler2DArray = Type.makeCombinedType("sampler2DArray", "Z2A", this.mHalf, 1, false, true, false);

    public final Type mSamplerCubeArray = Type.makeCombinedType("samplerCubeArray", "ZCA", this.mHalf, 3, false, true, false);

    public final Type mSampler2DMS = Type.makeCombinedType("sampler2DMS", "ZM", this.mHalf, 1, false, false, true);

    public final Type mSampler2DMSArray = Type.makeCombinedType("sampler2DMSArray", "ZMA", this.mHalf, 1, false, true, true);

    public final Type mSampler1DShadow = Type.makeCombinedType("sampler1DShadow", "Z4", this.mHalf, 0, true, false, false);

    public final Type mSampler2DShadow = Type.makeCombinedType("sampler2DShadow", "Z5", this.mHalf, 1, true, false, false);

    public final Type mSamplerCubeShadow = Type.makeCombinedType("samplerCubeShadow", "ZX", this.mHalf, 3, true, false, false);

    public final Type mSampler1DArrayShadow = Type.makeCombinedType("sampler1DArrayShadow", "Z4A", this.mHalf, 0, true, true, false);

    public final Type mSampler2DArrayShadow = Type.makeCombinedType("sampler2DArrayShadow", "Z5A", this.mHalf, 1, true, true, false);

    public final Type mSamplerCubeArrayShadow = Type.makeCombinedType("samplerCubeArrayShadow", "ZXA", this.mHalf, 3, true, true, false);

    public final Type mInvalid = Type.makeSpecialType("<INVALID>", "O", (byte) 3);

    public final Type mGenFType = Type.makeGenericType("__genFType", this.mFloat, this.mFloat2, this.mFloat3, this.mFloat4);

    public final Type mGenIType = Type.makeGenericType("__genIType", this.mInt, this.mInt2, this.mInt3, this.mInt4);

    public final Type mGenUType = Type.makeGenericType("__genUType", this.mUInt, this.mUInt2, this.mUInt3, this.mUInt4);

    public final Type mGenHType = Type.makeGenericType("__genHType", this.mHalf, this.mHalf2, this.mHalf3, this.mHalf4);

    public final Type mGenSType = Type.makeGenericType("__genSType", this.mShort, this.mShort2, this.mShort3, this.mShort4);

    public final Type mGenUSType = Type.makeGenericType("__genUSType", this.mUShort, this.mUShort2, this.mUShort3, this.mUShort4);

    public final Type mGenBType = Type.makeGenericType("__genBType", this.mBool, this.mBool2, this.mBool3, this.mBool4);

    public final Type mMat = Type.makeGenericType("__mat", this.mFloat2x2, this.mFloat2x3, this.mFloat2x4, this.mFloat3x2, this.mFloat3x3, this.mFloat3x4, this.mFloat4x2, this.mFloat4x3, this.mFloat4x4);

    public final Type mHMat = Type.makeGenericType("__hmat", this.mHalf2x2, this.mHalf2x3, this.mHalf2x4, this.mHalf3x2, this.mHalf3x3, this.mHalf3x4, this.mHalf4x2, this.mHalf4x3, this.mHalf4x4);

    public final Type mVec = Type.makeGenericType("__vec", this.mInvalid, this.mFloat2, this.mFloat3, this.mFloat4);

    public final Type mIVec = Type.makeGenericType("__ivec", this.mInvalid, this.mInt2, this.mInt3, this.mInt4);

    public final Type mUVec = Type.makeGenericType("__uvec", this.mInvalid, this.mUInt2, this.mUInt3, this.mUInt4);

    public final Type mHVec = Type.makeGenericType("__hvec", this.mInvalid, this.mHalf2, this.mHalf3, this.mHalf4);

    public final Type mSVec = Type.makeGenericType("__svec", this.mInvalid, this.mShort2, this.mShort3, this.mShort4);

    public final Type mUSVec = Type.makeGenericType("__usvec", this.mInvalid, this.mUShort2, this.mUShort3, this.mUShort4);

    public final Type mBVec = Type.makeGenericType("__bvec", this.mInvalid, this.mBool2, this.mBool3, this.mBool4);

    public final Type mPoison = Type.makeSpecialType("<POISON>", "P", (byte) 3);
}