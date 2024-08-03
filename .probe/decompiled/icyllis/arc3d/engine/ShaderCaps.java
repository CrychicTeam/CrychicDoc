package icyllis.arc3d.engine;

public class ShaderCaps extends icyllis.arc3d.compiler.ShaderCaps {

    public static final int NotSupported_AdvBlendEqInteraction = 0;

    public static final int Automatic_AdvBlendEqInteraction = 1;

    public static final int GeneralEnable_AdvBlendEqInteraction = 2;

    public boolean mDualSourceBlendingSupport = false;

    public boolean mPreferFlatInterpolation = false;

    public boolean mVertexIDSupport = false;

    public boolean mInfinitySupport = false;

    public boolean mNonConstantArrayIndexSupport = false;

    public boolean mBitManipulationSupport = false;

    public boolean mReducedShaderMode = false;

    public boolean mTextureQueryLod = true;

    public String mTextureQueryLodExtension = null;

    public boolean mShadingLanguage420Pack = true;

    public String mShadingLanguage420PackExtensionName = null;

    public boolean mEnhancedLayouts = true;

    public String mEnhancedLayoutsExtensionName = null;

    public boolean mRequiresLocalOutputColorForFBFetch = false;

    public boolean mMustObfuscateUniformColor = false;

    public boolean mMustWriteToFragColor = false;

    public boolean mColorSpaceMathNeedsFloat = false;

    public boolean mAvoidDfDxForGradientsWhenPossible = false;

    public String mSecondaryOutputExtension = null;

    public int mAdvBlendEqInteraction = 0;

    public int mMaxFragmentSamplers = 0;

    public void applyOptionsOverrides(ContextOptions options) {
        if (options.mReducedShaderVariations) {
            this.mReducedShaderMode = true;
        }
    }

    public final boolean mustEnableAdvBlendEqs() {
        return this.mAdvBlendEqInteraction >= 2;
    }

    @Override
    public String toString() {
        return "ShaderCaps{mDualSourceBlendingSupport=" + this.mDualSourceBlendingSupport + ", mPreferFlatInterpolation=" + this.mPreferFlatInterpolation + ", mVertexIDSupport=" + this.mVertexIDSupport + ", mInfinitySupport=" + this.mInfinitySupport + ", mNonConstantArrayIndexSupport=" + this.mNonConstantArrayIndexSupport + ", mBitManipulationSupport=" + this.mBitManipulationSupport + ", mReducedShaderMode=" + this.mReducedShaderMode + ", mTextureQueryLod=" + this.mTextureQueryLod + ", mTextureQueryLodExtension='" + this.mTextureQueryLodExtension + "', mEnhancedLayouts=" + this.mEnhancedLayouts + ", mEnhancedLayoutsExtensionName='" + this.mEnhancedLayoutsExtensionName + "', mRequiresLocalOutputColorForFBFetch=" + this.mRequiresLocalOutputColorForFBFetch + ", mMustObfuscateUniformColor=" + this.mMustObfuscateUniformColor + ", mMustWriteToFragColor=" + this.mMustWriteToFragColor + ", mColorSpaceMathNeedsFloat=" + this.mColorSpaceMathNeedsFloat + ", mAvoidDfDxForGradientsWhenPossible=" + this.mAvoidDfDxForGradientsWhenPossible + ", mSecondaryOutputExtension='" + this.mSecondaryOutputExtension + "', mAdvBlendEqInteraction=" + this.mAdvBlendEqInteraction + ", mMaxFragmentSamplers=" + this.mMaxFragmentSamplers + ", mTargetApi=" + this.mTargetApi + ", mGLSLVersion=" + this.mGLSLVersion + ", mSPIRVVersion=" + this.mSPIRVVersion + "}";
    }
}