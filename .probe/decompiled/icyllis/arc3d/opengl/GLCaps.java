package icyllis.arc3d.opengl;

import icyllis.arc3d.compiler.GLSLVersion;
import icyllis.arc3d.compiler.SPIRVVersion;
import icyllis.arc3d.compiler.TargetApi;
import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.engine.BackendFormat;
import icyllis.arc3d.engine.Caps;
import icyllis.arc3d.engine.ContextOptions;
import icyllis.arc3d.engine.DataUtils;
import icyllis.arc3d.engine.DriverBugWorkarounds;
import icyllis.arc3d.engine.GpuRenderTarget;
import icyllis.arc3d.engine.PipelineDesc;
import icyllis.arc3d.engine.PipelineInfo;
import icyllis.arc3d.engine.ShaderCaps;
import icyllis.arc3d.engine.Swizzle;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;

public final class GLCaps extends Caps {

    public static final List<String> MISSING_EXTENSIONS = new ArrayList();

    final int mVendor;

    final int mDriver;

    final int mMaxFragmentUniformVectors;

    private float mMaxTextureMaxAnisotropy = 1.0F;

    final boolean mSupportsProtected = false;

    private boolean mSkipErrorChecks = false;

    private final int mMaxLabelLength;

    final boolean mDebugSupport;

    final boolean mBufferStorageSupport;

    final boolean mBaseInstanceSupport;

    final boolean mProgramBinarySupport;

    final boolean mCopyImageSupport;

    final boolean mDSASupport;

    final boolean mSPIRVSupport;

    final int[] mProgramBinaryFormats;

    public static final int INVALIDATE_BUFFER_TYPE_NULL_DATA = 1;

    public static final int INVALIDATE_BUFFER_TYPE_INVALIDATE = 2;

    final int mInvalidateBufferType;

    final int mGLSLVersion;

    final boolean mDSAElementBufferBroken;

    private final GLCaps.FormatInfo[] mFormatTable = new GLCaps.FormatInfo[17];

    private final GLBackendFormat[] mColorTypeToBackendFormat = new GLBackendFormat[28];

    private final GLBackendFormat[] mCompressionTypeToBackendFormat = new GLBackendFormat[4];

    @VisibleForTesting
    public GLCaps(ContextOptions options, GLCapabilities caps) {
        super(options);
        List<String> missingExtensions = MISSING_EXTENSIONS;
        missingExtensions.clear();
        if (!caps.OpenGL45) {
            if (!caps.OpenGL21) {
                throw new UnsupportedOperationException("OpenGL 2.1 is unavailable");
            }
            if (!caps.OpenGL30) {
                if (!caps.GL_ARB_framebuffer_object) {
                    missingExtensions.add("ARB_framebuffer_object");
                }
                if (!caps.GL_ARB_map_buffer_range) {
                    missingExtensions.add("ARB_map_buffer_range");
                }
                if (!caps.GL_ARB_texture_float) {
                    missingExtensions.add("ARB_texture_float");
                }
                if (!caps.GL_ARB_texture_rg) {
                    missingExtensions.add("ARB_texture_rg");
                }
                if (!caps.GL_ARB_vertex_array_object) {
                    missingExtensions.add("ARB_vertex_array_object");
                }
                if (!caps.GL_ARB_shader_texture_lod) {
                    missingExtensions.add("ARB_shader_texture_lod");
                }
            }
            if (!caps.OpenGL31) {
                if (!caps.GL_ARB_uniform_buffer_object) {
                    missingExtensions.add("ARB_uniform_buffer_object");
                }
                if (!caps.GL_ARB_copy_buffer) {
                    missingExtensions.add("ARB_copy_buffer");
                }
                if (!caps.GL_ARB_draw_instanced) {
                    missingExtensions.add("ARB_draw_instanced");
                }
            }
            if (!caps.OpenGL32) {
                if (!caps.GL_ARB_draw_elements_base_vertex) {
                    missingExtensions.add("ARB_draw_elements_base_vertex");
                }
                if (!caps.GL_ARB_sync) {
                    missingExtensions.add("ARB_sync");
                }
                if (!caps.GL_ARB_fragment_coord_conventions) {
                    missingExtensions.add("ARB_fragment_coord_conventions");
                }
            }
            if (!caps.OpenGL33) {
                if (!caps.GL_ARB_blend_func_extended) {
                    missingExtensions.add("ARB_blend_func_extended");
                }
                if (!caps.GL_ARB_sampler_objects) {
                    missingExtensions.add("ARB_sampler_objects");
                }
                if (!caps.GL_ARB_explicit_attrib_location) {
                    missingExtensions.add("ARB_explicit_attrib_location");
                }
                if (!caps.GL_ARB_instanced_arrays) {
                    missingExtensions.add("ARB_instanced_arrays");
                }
                if (!caps.GL_ARB_texture_swizzle) {
                    missingExtensions.add("ARB_texture_swizzle");
                }
            }
            if (!missingExtensions.isEmpty()) {
                throw new UnsupportedOperationException("Missing required extensions: " + missingExtensions);
            }
            if (!caps.OpenGL41) {
                if (!caps.GL_ARB_ES2_compatibility) {
                    missingExtensions.add("ARB_ES2_compatibility");
                }
                if (!caps.GL_ARB_get_program_binary) {
                    missingExtensions.add("ARB_get_program_binary");
                }
                if (!caps.GL_ARB_viewport_array) {
                    missingExtensions.add("ARB_viewport_array");
                }
            }
            if (!caps.OpenGL42) {
                if (!caps.GL_ARB_base_instance) {
                    missingExtensions.add("ARB_base_instance");
                }
                if (!caps.GL_ARB_texture_storage) {
                    missingExtensions.add("ARB_texture_storage");
                }
                if (!caps.GL_ARB_internalformat_query) {
                    missingExtensions.add("ARB_internalformat_query");
                }
                if (!caps.GL_ARB_shading_language_420pack) {
                    missingExtensions.add("ARB_shading_language_420pack");
                }
            }
            if (!caps.OpenGL43) {
                if (!caps.GL_ARB_invalidate_subdata) {
                    missingExtensions.add("ARB_invalidate_subdata");
                }
                if (!caps.GL_ARB_explicit_uniform_location) {
                    missingExtensions.add("ARB_explicit_uniform_location");
                }
                if (!caps.GL_ARB_vertex_attrib_binding) {
                    missingExtensions.add("ARB_vertex_attrib_binding");
                }
                if (!caps.GL_ARB_ES3_compatibility) {
                    missingExtensions.add("ARB_ES3_compatibility");
                }
            }
            if (!caps.OpenGL44) {
                if (!caps.GL_ARB_clear_texture) {
                    missingExtensions.add("ARB_clear_texture");
                }
                if (!caps.GL_ARB_buffer_storage) {
                    missingExtensions.add("ARB_buffer_storage");
                }
                if (!caps.GL_ARB_enhanced_layouts) {
                    missingExtensions.add("ARB_enhanced_layouts");
                }
            }
            if (!caps.GL_ARB_texture_barrier) {
                missingExtensions.add("ARB_texture_barrier");
            }
            if (!caps.GL_ARB_direct_state_access) {
                missingExtensions.add("ARB_direct_state_access");
            }
            this.mDSASupport = missingExtensions.isEmpty();
            this.mDebugSupport = caps.OpenGL43 || caps.GL_KHR_debug;
            this.mBaseInstanceSupport = caps.OpenGL42 || caps.GL_ARB_base_instance;
            this.mCopyImageSupport = caps.OpenGL43 || caps.GL_ARB_copy_image && caps.GL_ARB_internalformat_query2;
            this.mProgramBinarySupport = caps.OpenGL41 || caps.GL_ARB_get_program_binary;
            this.mBufferStorageSupport = caps.OpenGL44 || caps.GL_ARB_buffer_storage;
        } else {
            this.mDSASupport = true;
            this.mDebugSupport = true;
            this.mBaseInstanceSupport = true;
            this.mCopyImageSupport = true;
            this.mProgramBinarySupport = true;
            this.mBufferStorageSupport = true;
        }
        String versionString = GLCore.glGetString(7938);
        String vendorString = GLCore.glGetString(7936);
        this.mVendor = GLCore.find_vendor(vendorString);
        this.mDriver = GLCore.find_driver(this.mVendor, vendorString, versionString);
        DriverBugWorkarounds workarounds = this.mDriverBugWorkarounds;
        if (this.mDSASupport) {
            if (DriverBugWorkarounds.isEnabled(workarounds.dsa_element_buffer_broken)) {
                this.mDSAElementBufferBroken = true;
            } else if (DriverBugWorkarounds.isDisabled(workarounds.dsa_element_buffer_broken)) {
                this.mDSAElementBufferBroken = false;
            } else {
                this.mDSAElementBufferBroken = this.mDriver == 1;
            }
        } else {
            this.mDSAElementBufferBroken = false;
        }
        if (!caps.OpenGL41 && !caps.GL_ARB_ES2_compatibility) {
            this.mMaxFragmentUniformVectors = -1;
        } else {
            this.mMaxFragmentUniformVectors = GLCore.glGetInteger(36349);
        }
        this.mMaxVertexAttributes = Math.min(32, GLCore.glGetInteger(34921));
        if (!caps.OpenGL43 && !caps.GL_ARB_invalidate_subdata) {
            this.mInvalidateBufferType = 1;
        } else {
            this.mInvalidateBufferType = 2;
        }
        this.mTransferPixelsToRowBytesSupport = true;
        this.mMustSyncGpuDuringDiscard = false;
        if (this.mDebugSupport) {
            this.mMaxLabelLength = GLCore.glGetInteger(33512);
        } else {
            this.mMaxLabelLength = 0;
        }
        ShaderCaps shaderCaps = this.mShaderCaps;
        if (caps.OpenGL45) {
            shaderCaps.mTargetApi = TargetApi.OPENGL_4_5;
        } else if (caps.OpenGL43) {
            shaderCaps.mTargetApi = TargetApi.OPENGL_4_3;
        } else {
            shaderCaps.mTargetApi = TargetApi.OPENGL_3_3;
        }
        int glslVersion;
        if (caps.OpenGL46) {
            glslVersion = 460;
        } else if (caps.OpenGL45) {
            glslVersion = 450;
        } else if (caps.OpenGL44) {
            glslVersion = 440;
        } else if (caps.OpenGL43) {
            glslVersion = 430;
        } else if (caps.OpenGL42) {
            glslVersion = 420;
        } else if (caps.OpenGL41) {
            glslVersion = 410;
        } else if (caps.OpenGL40) {
            glslVersion = 400;
        } else {
            glslVersion = 330;
        }
        this.mGLSLVersion = glslVersion;
        if (glslVersion >= 450) {
            shaderCaps.mGLSLVersion = GLSLVersion.GLSL_450;
        } else if (glslVersion == 440) {
            shaderCaps.mGLSLVersion = GLSLVersion.GLSL_440;
        } else if (glslVersion == 430) {
            shaderCaps.mGLSLVersion = GLSLVersion.GLSL_430;
        } else if (glslVersion == 420) {
            shaderCaps.mGLSLVersion = GLSLVersion.GLSL_420;
        } else if (glslVersion >= 400) {
            shaderCaps.mGLSLVersion = GLSLVersion.GLSL_400;
        } else {
            shaderCaps.mGLSLVersion = GLSLVersion.GLSL_330;
        }
        this.initGLSL(caps, shaderCaps.mGLSLVersion);
        shaderCaps.mDualSourceBlendingSupport = true;
        if (caps.GL_NV_conservative_raster) {
            this.mConservativeRasterSupport = true;
        }
        shaderCaps.mMaxFragmentSamplers = Math.min(32, GLCore.glGetInteger(34930));
        if (caps.GL_NV_blend_equation_advanced_coherent) {
            this.mBlendEquationSupport = Caps.BlendEquationSupport.ADVANCED_COHERENT;
            shaderCaps.mAdvBlendEqInteraction = 1;
        } else if (caps.GL_KHR_blend_equation_advanced_coherent) {
            this.mBlendEquationSupport = Caps.BlendEquationSupport.ADVANCED_COHERENT;
            this.mShaderCaps.mAdvBlendEqInteraction = 2;
        } else if (caps.GL_NV_blend_equation_advanced) {
            this.mBlendEquationSupport = Caps.BlendEquationSupport.ADVANCED;
            this.mShaderCaps.mAdvBlendEqInteraction = 1;
        } else if (caps.GL_KHR_blend_equation_advanced) {
            this.mBlendEquationSupport = Caps.BlendEquationSupport.ADVANCED;
            this.mShaderCaps.mAdvBlendEqInteraction = 2;
        }
        this.mAnisotropySupport = caps.OpenGL46 || caps.GL_ARB_texture_filter_anisotropic || caps.GL_EXT_texture_filter_anisotropic;
        if (this.mAnisotropySupport) {
            this.mMaxTextureMaxAnisotropy = GLCore.glGetFloat(34047);
        }
        this.mMaxTextureSize = GLCore.glGetInteger(3379);
        this.mMaxRenderTargetSize = GLCore.glGetInteger(34024);
        this.mMaxPreferredRenderTargetSize = this.mMaxRenderTargetSize;
        this.mGpuTracingSupport = caps.GL_EXT_debug_marker;
        this.mDynamicStateArrayGeometryProcessorTextureSupport = true;
        if (this.mProgramBinarySupport) {
            int count = GLCore.glGetInteger(34814);
            this.mProgramBinaryFormats = new int[count];
            if (count > 0) {
                GLCore.glGetIntegerv(34815, this.mProgramBinaryFormats);
            }
        } else {
            this.mProgramBinaryFormats = null;
        }
        boolean spirvSupport = false;
        if (caps.OpenGL46) {
            int count = GLCore.glGetInteger(36345);
            if (count > 0) {
                int[] shaderBinaryFormats = new int[count];
                GLCore.glGetIntegerv(36344, shaderBinaryFormats);
                for (int format : shaderBinaryFormats) {
                    if (format == 38225) {
                        spirvSupport = true;
                        break;
                    }
                }
            }
        }
        this.mSPIRVSupport = spirvSupport;
        if (spirvSupport) {
            shaderCaps.mSPIRVVersion = SPIRVVersion.SPIRV_1_0;
        }
        this.initFormatTable(caps);
        this.finishInitialization(options);
    }

    private void initGLSL(GLCapabilities caps, GLSLVersion version) {
        ShaderCaps shaderCaps = this.mShaderCaps;
        shaderCaps.mPreferFlatInterpolation = true;
        shaderCaps.mVertexIDSupport = true;
        shaderCaps.mInfinitySupport = true;
        shaderCaps.mNonConstantArrayIndexSupport = true;
        shaderCaps.mBitManipulationSupport = version.isAtLeast(GLSLVersion.GLSL_400);
        if (caps.OpenGL40) {
            shaderCaps.mTextureQueryLod = true;
        } else if (caps.GL_ARB_texture_query_lod) {
            shaderCaps.mTextureQueryLod = true;
            shaderCaps.mTextureQueryLodExtension = "GL_ARB_texture_query_lod";
        } else {
            shaderCaps.mTextureQueryLod = false;
        }
        if (caps.OpenGL42) {
            shaderCaps.mShadingLanguage420Pack = true;
        } else if (caps.GL_ARB_shading_language_420pack) {
            shaderCaps.mShadingLanguage420Pack = true;
            shaderCaps.mShadingLanguage420PackExtensionName = "GL_ARB_shading_language_420pack";
        } else {
            shaderCaps.mShadingLanguage420Pack = false;
        }
        if (caps.OpenGL44) {
            shaderCaps.mEnhancedLayouts = true;
        } else if (caps.GL_ARB_enhanced_layouts) {
            shaderCaps.mEnhancedLayouts = true;
            shaderCaps.mEnhancedLayoutsExtensionName = "GL_ARB_enhanced_layouts";
        } else {
            shaderCaps.mEnhancedLayouts = false;
        }
    }

    private void initFormatTable(GLCapabilities caps) {
        int nonMSAARenderFlags = 2;
        int msaaRenderFlags = 6;
        boolean textureStorageSupported = caps.OpenGL42 || caps.GL_ARB_texture_storage;
        this.mFormatTable[0] = new GLCaps.FormatInfo();
        int format = 32856;
        GLCaps.FormatInfo info = this.mFormatTable[1] = new GLCaps.FormatInfo();
        assert this.getFormatInfo(32856) == info && GLCore.glIndexToFormat(1) == 32856;
        info.mFormatType = 1;
        info.mInternalFormatForRenderbuffer = 32856;
        info.mDefaultExternalFormat = 6408;
        info.mDefaultExternalType = 5121;
        info.mDefaultColorType = 6;
        info.mFlags = 17;
        info.mFlags |= 6;
        if (textureStorageSupported) {
            info.mFlags |= 8;
        }
        info.mInternalFormatForTexture = 32856;
        info.mColorTypeInfos = new GLCaps.ColorTypeInfo[3];
        GLCaps.ColorTypeInfo ctInfo = info.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfo.mColorType = 6;
        ctInfo.mFlags = 3;
        this.setColorTypeFormat(6, 32856);
        ctInfo.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormat = ctInfo.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormat.mColorType = 6;
        ioFormat.mExternalType = 5121;
        ioFormat.mExternalWriteFormat = 6408;
        ioFormat.mExternalReadFormat = 6408;
        ioFormat = ctInfo.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormat.mColorType = 7;
        ioFormat.mExternalType = 5121;
        ioFormat.mExternalWriteFormat = 32993;
        ioFormat.mExternalReadFormat = 32993;
        ctInfo = info.mColorTypeInfos[1] = new GLCaps.ColorTypeInfo();
        ctInfo.mColorType = 7;
        ctInfo.mFlags = 3;
        this.setColorTypeFormat(7, 32856);
        ctInfo.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        ioFormat = ctInfo.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormat.mColorType = 7;
        ioFormat.mExternalType = 5121;
        ioFormat.mExternalWriteFormat = 32993;
        ioFormat.mExternalReadFormat = 32993;
        ioFormat = ctInfo.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormat.mColorType = 6;
        ioFormat.mExternalType = 5121;
        ioFormat.mExternalWriteFormat = 0;
        ioFormat.mExternalReadFormat = 6408;
        ctInfo = info.mColorTypeInfos[2] = new GLCaps.ColorTypeInfo();
        ctInfo.mColorType = 5;
        ctInfo.mFlags = 1;
        ctInfo.mReadSwizzle = 21008;
        ctInfo.mExternalIOFormats = new GLCaps.ExternalIOFormat[1];
        ioFormat = ctInfo.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormat.mColorType = 5;
        ioFormat.mExternalType = 5121;
        ioFormat.mExternalWriteFormat = 6408;
        ioFormat.mExternalReadFormat = 6408;
        GLCaps.FormatInfo infox = this.mFormatTable[2] = new GLCaps.FormatInfo();
        infox.mFormatType = 1;
        infox.mInternalFormatForRenderbuffer = 33321;
        infox.mDefaultExternalFormat = 6403;
        infox.mDefaultExternalType = 5121;
        infox.mDefaultColorType = 2;
        infox.mFlags = 17;
        infox.mFlags |= 6;
        if (textureStorageSupported) {
            infox.mFlags |= 8;
        }
        infox.mInternalFormatForTexture = 33321;
        infox.mColorTypeInfos = new GLCaps.ColorTypeInfo[3];
        GLCaps.ColorTypeInfo ctInfox = infox.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfox.mColorType = 2;
        ctInfox.mFlags = 3;
        this.setColorTypeFormat(2, 33321);
        ctInfox.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatx = ctInfox.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatx.mColorType = 2;
        ioFormatx.mExternalType = 5121;
        ioFormatx.mExternalWriteFormat = 6403;
        ioFormatx.mExternalReadFormat = 6403;
        GLCaps.ExternalIOFormat ioFormatxx = ctInfox.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxx.mColorType = 24;
        ioFormatxx.mExternalType = 5121;
        ioFormatxx.mExternalWriteFormat = 0;
        ioFormatxx.mExternalReadFormat = 6408;
        GLCaps.ColorTypeInfo ctInfoxx = infox.mColorTypeInfos[1] = new GLCaps.ColorTypeInfo();
        ctInfoxx.mColorType = 19;
        ctInfoxx.mFlags = 3;
        ctInfoxx.mReadSwizzle = Swizzle.make("000r");
        ctInfoxx.mWriteSwizzle = Swizzle.make("a000");
        this.setColorTypeFormat(19, 33321);
        ctInfoxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxx = ctInfoxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxx.mColorType = 19;
        ioFormatxxx.mExternalType = 5121;
        ioFormatxxx.mExternalWriteFormat = 6403;
        ioFormatxxx.mExternalReadFormat = 6403;
        GLCaps.ExternalIOFormat ioFormatxxxx = ctInfoxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxx.mColorType = 25;
        ioFormatxxxx.mExternalType = 5121;
        ioFormatxxxx.mExternalWriteFormat = 0;
        ioFormatxxxx.mExternalReadFormat = 6408;
        GLCaps.ColorTypeInfo ctInfoxxx = infox.mColorTypeInfos[2] = new GLCaps.ColorTypeInfo();
        ctInfoxxx.mColorType = 22;
        ctInfoxxx.mFlags = 1;
        ctInfoxxx.mReadSwizzle = Swizzle.make("rrr1");
        this.setColorTypeFormat(22, 33321);
        ctInfoxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxx = ctInfoxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxx.mColorType = 22;
        ioFormatxxxxx.mExternalType = 5121;
        ioFormatxxxxx.mExternalWriteFormat = 6403;
        ioFormatxxxxx.mExternalReadFormat = 6403;
        GLCaps.ExternalIOFormat ioFormatxxxxxx = ctInfoxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxx.mColorType = 27;
        ioFormatxxxxxx.mExternalType = 5121;
        ioFormatxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxx.mExternalReadFormat = 6408;
        GLCaps.FormatInfo infoxx = this.mFormatTable[3] = new GLCaps.FormatInfo();
        infoxx.mFormatType = 1;
        infoxx.mInternalFormatForRenderbuffer = 36194;
        infoxx.mDefaultExternalFormat = 6407;
        infoxx.mDefaultExternalType = 33635;
        infoxx.mDefaultColorType = 1;
        infoxx.mFlags = 17;
        infoxx.mFlags |= 6;
        if (textureStorageSupported) {
            infoxx.mFlags |= 8;
        }
        infoxx.mInternalFormatForTexture = 36194;
        infoxx.mColorTypeInfos = new GLCaps.ColorTypeInfo[1];
        GLCaps.ColorTypeInfo ctInfoxxxx = infoxx.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfoxxxx.mColorType = 1;
        ctInfoxxxx.mFlags = 3;
        this.setColorTypeFormat(1, 36194);
        ctInfoxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxxxx = ctInfoxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxx.mColorType = 1;
        ioFormatxxxxxxx.mExternalType = 33635;
        ioFormatxxxxxxx.mExternalWriteFormat = 6407;
        ioFormatxxxxxxx.mExternalReadFormat = 6407;
        GLCaps.ExternalIOFormat ioFormatxxxxxxxx = ctInfoxxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxx.mColorType = 6;
        ioFormatxxxxxxxx.mExternalType = 5121;
        ioFormatxxxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.FormatInfo infoxxx = this.mFormatTable[4] = new GLCaps.FormatInfo();
        infoxxx.mFormatType = 2;
        infoxxx.mInternalFormatForRenderbuffer = 34842;
        infoxxx.mDefaultExternalFormat = 6408;
        infoxxx.mDefaultExternalType = 5131;
        infoxxx.mDefaultColorType = 16;
        infoxxx.mFlags = 17;
        infoxxx.mFlags |= 6;
        if (textureStorageSupported) {
            infoxxx.mFlags |= 8;
        }
        infoxxx.mInternalFormatForTexture = 34842;
        infoxxx.mColorTypeInfos = new GLCaps.ColorTypeInfo[2];
        GLCaps.ColorTypeInfo ctInfoxxxxx = infoxxx.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxx.mColorType = 16;
        ctInfoxxxxx.mFlags = 3;
        this.setColorTypeFormat(16, 34842);
        ctInfoxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxx = ctInfoxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxx.mColorType = 16;
        ioFormatxxxxxxxxx.mExternalType = 5131;
        ioFormatxxxxxxxxx.mExternalWriteFormat = 6408;
        ioFormatxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxx = ctInfoxxxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxx.mColorType = 18;
        ioFormatxxxxxxxxxx.mExternalType = 5126;
        ioFormatxxxxxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.ColorTypeInfo ctInfoxxxxxx = infoxxx.mColorTypeInfos[1] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxxx.mColorType = 17;
        ctInfoxxxxxx.mFlags = 3;
        this.setColorTypeFormat(17, 34842);
        ctInfoxxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxx = ctInfoxxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxx.mColorType = 17;
        ioFormatxxxxxxxxxxx.mExternalType = 5131;
        ioFormatxxxxxxxxxxx.mExternalWriteFormat = 6408;
        ioFormatxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxx = ctInfoxxxxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxx.mColorType = 18;
        ioFormatxxxxxxxxxxxx.mExternalType = 5126;
        ioFormatxxxxxxxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.FormatInfo infoxxxx = this.mFormatTable[5] = new GLCaps.FormatInfo();
        infoxxxx.mFormatType = 2;
        infoxxxx.mInternalFormatForRenderbuffer = 33325;
        infoxxxx.mDefaultExternalFormat = 6403;
        infoxxxx.mDefaultExternalType = 5131;
        infoxxxx.mDefaultColorType = 12;
        infoxxxx.mFlags = 17;
        infoxxxx.mFlags |= 6;
        if (textureStorageSupported) {
            infoxxxx.mFlags |= 8;
        }
        infoxxxx.mInternalFormatForTexture = 33325;
        infoxxxx.mColorTypeInfos = new GLCaps.ColorTypeInfo[1];
        GLCaps.ColorTypeInfo ctInfoxxxxxxx = infoxxxx.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxxxx.mColorType = 21;
        ctInfoxxxxxxx.mFlags = 3;
        ctInfoxxxxxxx.mReadSwizzle = Swizzle.make("000r");
        ctInfoxxxxxxx.mWriteSwizzle = Swizzle.make("a000");
        this.setColorTypeFormat(21, 33325);
        ctInfoxxxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxx = ctInfoxxxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxx.mColorType = 21;
        ioFormatxxxxxxxxxxxxx.mExternalType = 5131;
        ioFormatxxxxxxxxxxxxx.mExternalWriteFormat = 6403;
        ioFormatxxxxxxxxxxxxx.mExternalReadFormat = 6403;
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxx = ctInfoxxxxxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxx.mColorType = 26;
        ioFormatxxxxxxxxxxxxxx.mExternalType = 5126;
        ioFormatxxxxxxxxxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.FormatInfo infoxxxxx = this.mFormatTable[6] = new GLCaps.FormatInfo();
        infoxxxxx.mFormatType = 1;
        infoxxxxx.mInternalFormatForRenderbuffer = 32849;
        infoxxxxx.mDefaultExternalFormat = 6407;
        infoxxxxx.mDefaultExternalType = 5121;
        infoxxxxx.mDefaultColorType = 4;
        infoxxxxx.mFlags = 17;
        if (GLCore.glGetInternalformati(36161, 32849, 33391) == 1) {
            infoxxxxx.mFlags |= 6;
        } else {
            infoxxxxx.mFlags |= 2;
        }
        if (textureStorageSupported) {
            infoxxxxx.mFlags |= 8;
        }
        infoxxxxx.mInternalFormatForTexture = 32849;
        infoxxxxx.mColorTypeInfos = new GLCaps.ColorTypeInfo[1];
        GLCaps.ColorTypeInfo ctInfoxxxxxxxx = infoxxxxx.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxxxxx.mColorType = 5;
        ctInfoxxxxxxxx.mFlags = 3;
        this.setColorTypeFormat(5, 32849);
        ctInfoxxxxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxx = ctInfoxxxxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxx.mColorType = 4;
        ioFormatxxxxxxxxxxxxxxx.mExternalType = 5121;
        ioFormatxxxxxxxxxxxxxxx.mExternalWriteFormat = 6407;
        ioFormatxxxxxxxxxxxxxxx.mExternalReadFormat = 0;
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxx = ctInfoxxxxxxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxx.mColorType = 6;
        ioFormatxxxxxxxxxxxxxxxx.mExternalType = 5121;
        ioFormatxxxxxxxxxxxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.FormatInfo infoxxxxxx = this.mFormatTable[7] = new GLCaps.FormatInfo();
        infoxxxxxx.mFormatType = 1;
        infoxxxxxx.mInternalFormatForRenderbuffer = 33323;
        infoxxxxxx.mDefaultExternalFormat = 33319;
        infoxxxxxx.mDefaultExternalType = 5121;
        infoxxxxxx.mDefaultColorType = 3;
        infoxxxxxx.mFlags = 17;
        infoxxxxxx.mFlags |= 6;
        if (textureStorageSupported) {
            infoxxxxxx.mFlags |= 8;
        }
        infoxxxxxx.mInternalFormatForTexture = 33323;
        infoxxxxxx.mColorTypeInfos = new GLCaps.ColorTypeInfo[2];
        GLCaps.ColorTypeInfo ctInfoxxxxxxxxx = infoxxxxxx.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxxxxxx.mColorType = 3;
        ctInfoxxxxxxxxx.mFlags = 3;
        this.setColorTypeFormat(3, 33323);
        ctInfoxxxxxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxx.mColorType = 3;
        ioFormatxxxxxxxxxxxxxxxxx.mExternalType = 5121;
        ioFormatxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 33319;
        ioFormatxxxxxxxxxxxxxxxxx.mExternalReadFormat = 33319;
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxx.mColorType = 6;
        ioFormatxxxxxxxxxxxxxxxxxx.mExternalType = 5121;
        ioFormatxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.ColorTypeInfo ctInfoxxxxxxxxxx = infoxxxxxx.mColorTypeInfos[1] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxxxxxxx.mColorType = 23;
        ctInfoxxxxxxxxxx.mFlags = 1;
        ctInfoxxxxxxxxxx.mReadSwizzle = Swizzle.make("rrrg");
        this.setColorTypeFormat(23, 33323);
        ctInfoxxxxxxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[1];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxx.mColorType = 23;
        ioFormatxxxxxxxxxxxxxxxxxxx.mExternalType = 5121;
        ioFormatxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 33319;
        ioFormatxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 33319;
        GLCaps.FormatInfo infoxxxxxxx = this.mFormatTable[8] = new GLCaps.FormatInfo();
        infoxxxxxxx.mFormatType = 1;
        infoxxxxxxx.mInternalFormatForRenderbuffer = 32857;
        infoxxxxxxx.mDefaultExternalFormat = 6408;
        infoxxxxxxx.mDefaultExternalType = 33640;
        infoxxxxxxx.mDefaultColorType = 9;
        infoxxxxxxx.mFlags = 17;
        infoxxxxxxx.mFlags |= 6;
        if (textureStorageSupported) {
            infoxxxxxxx.mFlags |= 8;
        }
        infoxxxxxxx.mInternalFormatForTexture = 32857;
        infoxxxxxxx.mColorTypeInfos = new GLCaps.ColorTypeInfo[2];
        GLCaps.ColorTypeInfo ctInfoxxxxxxxxxxx = infoxxxxxxx.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxxxxxxxx.mColorType = 9;
        ctInfoxxxxxxxxxxx.mFlags = 3;
        this.setColorTypeFormat(9, 32857);
        ctInfoxxxxxxxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxx.mColorType = 9;
        ioFormatxxxxxxxxxxxxxxxxxxxx.mExternalType = 33640;
        ioFormatxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 6408;
        ioFormatxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxxx.mColorType = 6;
        ioFormatxxxxxxxxxxxxxxxxxxxxx.mExternalType = 5121;
        ioFormatxxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.ColorTypeInfo ctInfoxxxxxxxxxxxx = infoxxxxxxx.mColorTypeInfos[1] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxxxxxxxxx.mColorType = 10;
        ctInfoxxxxxxxxxxxx.mFlags = 3;
        this.setColorTypeFormat(10, 32857);
        ctInfoxxxxxxxxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxxxx.mColorType = 10;
        ioFormatxxxxxxxxxxxxxxxxxxxxxx.mExternalType = 33640;
        ioFormatxxxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 32993;
        ioFormatxxxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 32993;
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxxxxx.mColorType = 6;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxx.mExternalType = 5121;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.FormatInfo infoxxxxxxxx = this.mFormatTable[9] = new GLCaps.FormatInfo();
        infoxxxxxxxx.mFormatType = 1;
        infoxxxxxxxx.mInternalFormatForRenderbuffer = 35907;
        infoxxxxxxxx.mDefaultExternalFormat = 6408;
        infoxxxxxxxx.mDefaultExternalType = 5121;
        infoxxxxxxxx.mDefaultColorType = 8;
        infoxxxxxxxx.mFlags = 17;
        infoxxxxxxxx.mFlags |= 6;
        if (textureStorageSupported) {
            infoxxxxxxxx.mFlags |= 8;
        }
        infoxxxxxxxx.mInternalFormatForTexture = 35907;
        infoxxxxxxxx.mColorTypeInfos = new GLCaps.ColorTypeInfo[1];
        GLCaps.ColorTypeInfo ctInfoxxxxxxxxxxxxx = infoxxxxxxxx.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxxxxxxxxxx.mColorType = 8;
        ctInfoxxxxxxxxxxxxx.mFlags = 3;
        this.setColorTypeFormat(8, 35907);
        ctInfoxxxxxxxxxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[1];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxx.mColorType = 8;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxx.mExternalType = 5121;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 6408;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.FormatInfo infoxxxxxxxxx = this.mFormatTable[10] = new GLCaps.FormatInfo();
        infoxxxxxxxxx.mFormatType = 1;
        infoxxxxxxxxx.mInternalFormatForTexture = 37492;
        infoxxxxxxxxx.mFlags = 1;
        this.mCompressionTypeToBackendFormat[1] = GLBackendFormat.make(37492);
        GLCaps.FormatInfo infoxxxxxxxxxx = this.mFormatTable[11] = new GLCaps.FormatInfo();
        infoxxxxxxxxxx.mFormatType = 1;
        infoxxxxxxxxxx.mInternalFormatForTexture = 33776;
        if (caps.GL_EXT_texture_compression_s3tc) {
            infoxxxxxxxxxx.mFlags = 1;
            this.mCompressionTypeToBackendFormat[2] = GLBackendFormat.make(33776);
        }
        GLCaps.FormatInfo infoxxxxxxxxxxx = this.mFormatTable[12] = new GLCaps.FormatInfo();
        infoxxxxxxxxxxx.mFormatType = 1;
        infoxxxxxxxxxxx.mInternalFormatForTexture = 33777;
        if (caps.GL_EXT_texture_compression_s3tc) {
            infoxxxxxxxxxxx.mFlags = 1;
            this.mCompressionTypeToBackendFormat[3] = GLBackendFormat.make(33777);
        }
        GLCaps.FormatInfo infoxxxxxxxxxxxx = this.mFormatTable[13] = new GLCaps.FormatInfo();
        infoxxxxxxxxxxxx.mFormatType = 1;
        infoxxxxxxxxxxxx.mInternalFormatForRenderbuffer = 33322;
        infoxxxxxxxxxxxx.mDefaultExternalFormat = 6403;
        infoxxxxxxxxxxxx.mDefaultExternalType = 5123;
        infoxxxxxxxxxxxx.mDefaultColorType = 11;
        infoxxxxxxxxxxxx.mFlags = 17;
        infoxxxxxxxxxxxx.mFlags |= 6;
        if (textureStorageSupported) {
            infoxxxxxxxxxxxx.mFlags |= 8;
        }
        infoxxxxxxxxxxxx.mInternalFormatForTexture = 33322;
        infoxxxxxxxxxxxx.mColorTypeInfos = new GLCaps.ColorTypeInfo[1];
        GLCaps.ColorTypeInfo ctInfoxxxxxxxxxxxxxx = infoxxxxxxxxxxxx.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxxxxxxxxxxx.mColorType = 20;
        ctInfoxxxxxxxxxxxxxx.mFlags = 3;
        ctInfoxxxxxxxxxxxxxx.mReadSwizzle = Swizzle.make("000r");
        ctInfoxxxxxxxxxxxxxx.mWriteSwizzle = Swizzle.make("a000");
        this.setColorTypeFormat(20, 33322);
        ctInfoxxxxxxxxxxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxx.mColorType = 20;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalType = 5123;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 6403;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 6403;
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxxxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxx.mColorType = 25;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalType = 5121;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.FormatInfo infoxxxxxxxxxxxxx = this.mFormatTable[14] = new GLCaps.FormatInfo();
        infoxxxxxxxxxxxxx.mFormatType = 1;
        infoxxxxxxxxxxxxx.mInternalFormatForRenderbuffer = 33324;
        infoxxxxxxxxxxxxx.mDefaultExternalFormat = 33319;
        infoxxxxxxxxxxxxx.mDefaultExternalType = 5123;
        infoxxxxxxxxxxxxx.mDefaultColorType = 13;
        infoxxxxxxxxxxxxx.mFlags = 17;
        infoxxxxxxxxxxxxx.mFlags |= 6;
        if (textureStorageSupported) {
            infoxxxxxxxxxxxxx.mFlags |= 8;
        }
        infoxxxxxxxxxxxxx.mInternalFormatForTexture = 33324;
        infoxxxxxxxxxxxxx.mColorTypeInfos = new GLCaps.ColorTypeInfo[1];
        GLCaps.ColorTypeInfo ctInfoxxxxxxxxxxxxxxx = infoxxxxxxxxxxxxx.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxxxxxxxxxxxx.mColorType = 13;
        ctInfoxxxxxxxxxxxxxxx.mFlags = 3;
        this.setColorTypeFormat(13, 33324);
        ctInfoxxxxxxxxxxxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxx.mColorType = 13;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalType = 5123;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 33319;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 33319;
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxxxxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mColorType = 6;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalType = 5121;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.FormatInfo infoxxxxxxxxxxxxxx = this.mFormatTable[15] = new GLCaps.FormatInfo();
        infoxxxxxxxxxxxxxx.mFormatType = 1;
        infoxxxxxxxxxxxxxx.mInternalFormatForRenderbuffer = 32859;
        infoxxxxxxxxxxxxxx.mDefaultExternalFormat = 6408;
        infoxxxxxxxxxxxxxx.mDefaultExternalType = 5123;
        infoxxxxxxxxxxxxxx.mDefaultColorType = 15;
        infoxxxxxxxxxxxxxx.mFlags = 17;
        infoxxxxxxxxxxxxxx.mFlags |= 6;
        if (textureStorageSupported) {
            infoxxxxxxxxxxxxxx.mFlags |= 8;
        }
        infoxxxxxxxxxxxxxx.mInternalFormatForTexture = 32859;
        infoxxxxxxxxxxxxxx.mColorTypeInfos = new GLCaps.ColorTypeInfo[1];
        GLCaps.ColorTypeInfo ctInfoxxxxxxxxxxxxxxxx = infoxxxxxxxxxxxxxx.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxxxxxxxxxxxxx.mColorType = 15;
        ctInfoxxxxxxxxxxxxxxxx.mFlags = 3;
        this.setColorTypeFormat(15, 32859);
        ctInfoxxxxxxxxxxxxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxxxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mColorType = 15;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalType = 5123;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 6408;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxxxxxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mColorType = 6;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalType = 5121;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 6408;
        GLCaps.FormatInfo infoxxxxxxxxxxxxxxx = this.mFormatTable[16] = new GLCaps.FormatInfo();
        infoxxxxxxxxxxxxxxx.mFormatType = 2;
        infoxxxxxxxxxxxxxxx.mInternalFormatForRenderbuffer = 33327;
        infoxxxxxxxxxxxxxxx.mDefaultExternalFormat = 33319;
        infoxxxxxxxxxxxxxxx.mDefaultExternalType = 5131;
        infoxxxxxxxxxxxxxxx.mDefaultColorType = 14;
        infoxxxxxxxxxxxxxxx.mFlags = 17;
        infoxxxxxxxxxxxxxxx.mFlags |= 6;
        if (textureStorageSupported) {
            infoxxxxxxxxxxxxxxx.mFlags |= 8;
        }
        infoxxxxxxxxxxxxxxx.mInternalFormatForTexture = 33327;
        infoxxxxxxxxxxxxxxx.mColorTypeInfos = new GLCaps.ColorTypeInfo[1];
        GLCaps.ColorTypeInfo ctInfoxxxxxxxxxxxxxxxxx = infoxxxxxxxxxxxxxxx.mColorTypeInfos[0] = new GLCaps.ColorTypeInfo();
        ctInfoxxxxxxxxxxxxxxxxx.mColorType = 14;
        ctInfoxxxxxxxxxxxxxxxxx.mFlags = 3;
        this.setColorTypeFormat(14, 33327);
        ctInfoxxxxxxxxxxxxxxxxx.mExternalIOFormats = new GLCaps.ExternalIOFormat[2];
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxxxxxxxx.mExternalIOFormats[0] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mColorType = 14;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalType = 5131;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 33319;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 33319;
        GLCaps.ExternalIOFormat ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ctInfoxxxxxxxxxxxxxxxxx.mExternalIOFormats[1] = new GLCaps.ExternalIOFormat();
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mColorType = 18;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalType = 5126;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalWriteFormat = 0;
        ioFormatxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.mExternalReadFormat = 6408;
        for (GLCaps.FormatInfo infoxxxxxxxxxxxxxxxx : this.mFormatTable) {
            if (this.mCopyImageSupport && infoxxxxxxxxxxxxxxxx.mInternalFormatForTexture != 0) {
                infoxxxxxxxxxxxxxxxx.mViewCompatibilityClass = GLCore.glGetInternalformati(3553, infoxxxxxxxxxxxxxxxx.mInternalFormatForTexture, 33462);
            }
            if ((infoxxxxxxxxxxxxxxxx.mFlags & 4) != 0) {
                assert (infoxxxxxxxxxxxxxxxx.mFlags & 2) != 0;
                int glFormat = infoxxxxxxxxxxxxxxxx.mInternalFormatForRenderbuffer;
                int count = GLCore.glGetInternalformati(36161, glFormat, 37760);
                if (count > 0) {
                    MemoryStack stack = MemoryStack.stackPush();
                    try {
                        IntBuffer temp = stack.mallocInt(count);
                        GLCore.glGetInternalformativ(36161, glFormat, 32937, temp);
                        if (temp.get(count - 1) == 1) {
                            count--;
                            assert count == 0 || temp.get(count - 1) > 1;
                        }
                        infoxxxxxxxxxxxxxxxx.mColorSampleCounts = new int[count + 1];
                        infoxxxxxxxxxxxxxxxx.mColorSampleCounts[0] = 1;
                        for (int j = 0; j < count; j++) {
                            infoxxxxxxxxxxxxxxxx.mColorSampleCounts[j + 1] = temp.get(count - j - 1);
                        }
                    } catch (Throwable var16) {
                        if (stack != null) {
                            try {
                                stack.close();
                            } catch (Throwable var15) {
                                var16.addSuppressed(var15);
                            }
                        }
                        throw var16;
                    }
                    if (stack != null) {
                        stack.close();
                    }
                }
            } else if ((infoxxxxxxxxxxxxxxxx.mFlags & 2) != 0) {
                infoxxxxxxxxxxxxxxxx.mColorSampleCounts = new int[1];
                infoxxxxxxxxxxxxxxxx.mColorSampleCounts[0] = 1;
            }
        }
        for (int index = 1; index < this.mFormatTable.length; index++) {
            info = this.mFormatTable[index];
            assert (info.mFlags & 4) == 0 || (info.mFlags & 2) != 0;
            assert (info.mFlags & 2) == 0 || (info.mFlags & 1) != 0;
            assert info.mFormatType != 0;
            assert (info.mFlags & 1) == 0 || info.mInternalFormatForTexture != 0;
            assert (info.mFlags & 2) == 0 || info.mInternalFormatForRenderbuffer != 0;
            for (GLCaps.ColorTypeInfo ctInfoxxxxxxxxxxxxxxxxxx : info.mColorTypeInfos) {
                assert ctInfoxxxxxxxxxxxxxxxxxx.mColorType != 0;
                assert ctInfoxxxxxxxxxxxxxxxxxx.mFlags != 0;
                for (GLCaps.ExternalIOFormat ioInfo : ctInfoxxxxxxxxxxxxxxxxxx.mExternalIOFormats) {
                    assert ioInfo.mColorType != 0;
                }
            }
        }
    }

    public GLCaps.FormatInfo getFormatInfo(int format) {
        return this.mFormatTable[GLCore.glFormatToIndex(format)];
    }

    private void setColorTypeFormat(int colorType, int format) {
        assert this.mColorTypeToBackendFormat[colorType] == null;
        assert GLCore.glFormatIsSupported(format);
        this.mColorTypeToBackendFormat[colorType] = GLBackendFormat.make(format);
    }

    public boolean hasDSASupport() {
        return this.mDSASupport;
    }

    public int getInvalidateBufferType() {
        return this.mInvalidateBufferType;
    }

    public boolean hasDebugSupport() {
        return this.mDebugSupport;
    }

    public boolean hasBaseInstanceSupport() {
        return this.mBaseInstanceSupport;
    }

    public boolean hasCopyImageSupport() {
        return this.mCopyImageSupport;
    }

    public boolean hasBufferStorageSupport() {
        return this.mBufferStorageSupport;
    }

    public boolean hasSPIRVSupport() {
        return this.mSPIRVSupport;
    }

    public boolean hasProgramBinarySupport() {
        return this.mProgramBinarySupport;
    }

    @Nullable
    public int[] getProgramBinaryFormats() {
        return this.mProgramBinarySupport ? (int[]) this.mProgramBinaryFormats.clone() : null;
    }

    public int getGLSLVersion() {
        return this.mGLSLVersion;
    }

    @Override
    public boolean isFormatTexturable(BackendFormat format) {
        return this.isFormatTexturable(format.getGLFormat());
    }

    public boolean isFormatTexturable(int format) {
        return (this.getFormatInfo(format).mFlags & 1) != 0;
    }

    public boolean isTextureStorageCompatible(int format) {
        return (this.getFormatInfo(format).mFlags & 8) != 0;
    }

    @Override
    public int getMaxRenderTargetSampleCount(BackendFormat format) {
        return this.getMaxRenderTargetSampleCount(format.getGLFormat());
    }

    public int getMaxRenderTargetSampleCount(int format) {
        int[] table = this.getFormatInfo(format).mColorSampleCounts;
        return table.length == 0 ? 0 : table[table.length - 1];
    }

    @Override
    public boolean isFormatRenderable(int colorType, BackendFormat format, int sampleCount) {
        if (format.isExternal()) {
            return false;
        } else {
            int f = format.getGLFormat();
            return (this.getFormatInfo(f).colorTypeFlags(colorType) & 2) == 0 ? false : this.isFormatRenderable(f, sampleCount);
        }
    }

    @Override
    public boolean isFormatRenderable(BackendFormat format, int sampleCount) {
        return format.isExternal() ? false : this.isFormatRenderable(format.getGLFormat(), sampleCount);
    }

    public boolean isFormatRenderable(int format, int sampleCount) {
        return sampleCount <= this.getMaxRenderTargetSampleCount(format);
    }

    @Override
    public int getRenderTargetSampleCount(int sampleCount, BackendFormat format) {
        return this.getRenderTargetSampleCount(sampleCount, format.getGLFormat());
    }

    public int getRenderTargetSampleCount(int sampleCount, int format) {
        GLCaps.FormatInfo formatInfo = this.getFormatInfo(format);
        if (formatInfo.mColorTypeInfos.length == 0) {
            return 0;
        } else if (sampleCount <= 1) {
            return formatInfo.mColorSampleCounts[0] == 1 ? 1 : 0;
        } else {
            for (int count : formatInfo.mColorSampleCounts) {
                if (count >= sampleCount) {
                    return count;
                }
            }
            return 0;
        }
    }

    @Override
    public boolean onFormatCompatible(int colorType, BackendFormat format) {
        GLCaps.FormatInfo formatInfo = this.getFormatInfo(format.getGLFormat());
        for (GLCaps.ColorTypeInfo info : formatInfo.mColorTypeInfos) {
            if (info.mColorType == colorType) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    protected BackendFormat onGetDefaultBackendFormat(int colorType) {
        return this.mColorTypeToBackendFormat[colorType];
    }

    @Nullable
    @Override
    public BackendFormat getCompressedBackendFormat(int compressionType) {
        return this.mCompressionTypeToBackendFormat[compressionType];
    }

    @Nonnull
    @Override
    public PipelineDesc makeDesc(PipelineDesc desc, GpuRenderTarget renderTarget, PipelineInfo pipelineInfo) {
        return PipelineDesc.build(desc, pipelineInfo, this);
    }

    @Override
    protected short onGetReadSwizzle(BackendFormat format, int colorType) {
        GLCaps.FormatInfo formatInfo = this.getFormatInfo(format.getGLFormat());
        for (GLCaps.ColorTypeInfo ctInfo : formatInfo.mColorTypeInfos) {
            if (ctInfo.mColorType == colorType) {
                return ctInfo.mReadSwizzle;
            }
        }
        assert false;
        return 12816;
    }

    @Override
    public short getWriteSwizzle(BackendFormat format, int colorType) {
        GLCaps.FormatInfo formatInfo = this.getFormatInfo(format.getGLFormat());
        for (GLCaps.ColorTypeInfo ctInfo : formatInfo.mColorTypeInfos) {
            if (ctInfo.mColorType == colorType) {
                return ctInfo.mWriteSwizzle;
            }
        }
        assert false;
        return 12816;
    }

    @Override
    public long getSupportedWriteColorType(int dstColorType, BackendFormat dstFormat, int srcColorType) {
        int fallbackCT = 0;
        GLCaps.FormatInfo formatInfo = this.getFormatInfo(dstFormat.getGLFormat());
        boolean foundSurfaceCT = false;
        long transferOffsetAlignment = 0L;
        if ((formatInfo.mFlags & 16) != 0) {
            transferOffsetAlignment = 1L;
        }
        for (int i = 0; !foundSurfaceCT && i < formatInfo.mColorTypeInfos.length; i++) {
            if (formatInfo.mColorTypeInfos[i].mColorType == dstColorType) {
                GLCaps.ColorTypeInfo ctInfo = formatInfo.mColorTypeInfos[i];
                foundSurfaceCT = true;
                for (GLCaps.ExternalIOFormat ioInfo : ctInfo.mExternalIOFormats) {
                    if (ioInfo.mExternalWriteFormat != 0) {
                        if (ioInfo.mColorType == srcColorType) {
                            return (long) srcColorType | transferOffsetAlignment << 32;
                        }
                        if (fallbackCT == 0) {
                            fallbackCT = ioInfo.mColorType;
                        }
                    }
                }
            }
        }
        return (long) fallbackCT | transferOffsetAlignment << 32;
    }

    public static int getExternalTypeAlignment(int type) {
        return switch(type) {
            case 5120, 5121, 32818, 33634 ->
                1;
            case 5122, 5123, 5131, 32819, 32820, 33635, 33636, 33637, 33638 ->
                2;
            case 5124, 5125, 5126, 32821, 32822, 33639, 33640, 34042, 35899, 35902, 36269 ->
                4;
            default ->
                0;
        };
    }

    @Override
    protected long onSupportedReadColorType(int srcColorType, BackendFormat srcFormat, int dstColorType) {
        int compression = srcFormat.getCompressionType();
        if (compression != 0) {
            return (long) (DataUtils.compressionTypeIsOpaque(compression) ? 5 : 6);
        } else {
            int fallbackColorType = 0;
            long fallbackTransferOffsetAlignment = 0L;
            GLCaps.FormatInfo formatInfo = this.getFormatInfo(srcFormat.getGLFormat());
            for (GLCaps.ColorTypeInfo ctInfo : formatInfo.mColorTypeInfos) {
                if (ctInfo.mColorType == srcColorType) {
                    for (GLCaps.ExternalIOFormat ioInfo : ctInfo.mExternalIOFormats) {
                        if (ioInfo.mExternalReadFormat != 0) {
                            long transferOffsetAlignment = 0L;
                            if ((formatInfo.mFlags & 16) != 0) {
                                transferOffsetAlignment = (long) getExternalTypeAlignment(ioInfo.mExternalType);
                            }
                            if (ioInfo.mColorType == dstColorType) {
                                return (long) dstColorType | transferOffsetAlignment << 32;
                            }
                            if (fallbackColorType == 0) {
                                fallbackColorType = ioInfo.mColorType;
                                fallbackTransferOffsetAlignment = transferOffsetAlignment;
                            }
                        }
                    }
                    break;
                }
            }
            return (long) fallbackColorType | fallbackTransferOffsetAlignment << 32;
        }
    }

    @Override
    protected void onApplyOptionsOverrides(ContextOptions options) {
        super.onApplyOptionsOverrides(options);
        if (options.mSkipGLErrorChecks == Boolean.FALSE) {
            this.mSkipErrorChecks = false;
        } else if (options.mSkipGLErrorChecks == Boolean.TRUE) {
            this.mSkipErrorChecks = true;
        }
    }

    public int getTextureInternalFormat(int format) {
        return this.getFormatInfo(format).mInternalFormatForTexture;
    }

    public int getRenderbufferInternalFormat(int format) {
        return this.getFormatInfo(format).mInternalFormatForRenderbuffer;
    }

    public int getFormatDefaultExternalFormat(int format) {
        return this.getFormatInfo(format).mDefaultExternalFormat;
    }

    public int getFormatDefaultExternalType(int format) {
        return this.getFormatInfo(format).mDefaultExternalType;
    }

    public int getPixelsExternalFormat(int format, int dstColorType, int srcColorType, boolean write) {
        return this.getFormatInfo(format).externalFormat(dstColorType, srcColorType, write);
    }

    public int getPixelsExternalType(int format, int dstColorType, int srcColorType) {
        return this.getFormatInfo(format).externalType(dstColorType, srcColorType);
    }

    public boolean canCopyImage(int srcFormat, int srcSampleCount, int dstFormat, int dstSampleCount) {
        if (!this.mCopyImageSupport) {
            return false;
        } else if ((dstSampleCount > 1 || srcSampleCount > 1) && dstSampleCount != srcSampleCount) {
            return false;
        } else {
            return srcFormat == dstFormat ? true : this.getFormatInfo(srcFormat).mViewCompatibilityClass == this.getFormatInfo(dstFormat).mViewCompatibilityClass;
        }
    }

    public boolean canCopyTexSubImage(int srcFormat, int dstFormat) {
        if (this.getFormatDefaultExternalType(dstFormat) != this.getFormatDefaultExternalType(srcFormat)) {
            return false;
        } else {
            return GLCore.glFormatIsSRGB(dstFormat) != GLCore.glFormatIsSRGB(srcFormat) ? false : (this.getFormatInfo(srcFormat).mFlags & 2) != 0;
        }
    }

    public boolean skipErrorChecks() {
        return this.mSkipErrorChecks;
    }

    public int maxLabelLength() {
        return this.mMaxLabelLength;
    }

    public float maxTextureMaxAnisotropy() {
        return this.mMaxTextureMaxAnisotropy;
    }

    public boolean dsaElementBufferBroken() {
        assert this.hasDSASupport();
        return this.mDSAElementBufferBroken;
    }

    public String toString() {
        return this.toString(true);
    }

    public String toString(boolean includeFormatTable) {
        return "GLCaps{mProgramBinaryFormats=" + Arrays.toString(this.mProgramBinaryFormats) + ", mMaxFragmentUniformVectors=" + this.mMaxFragmentUniformVectors + ", mMaxTextureMaxAnisotropy=" + this.mMaxTextureMaxAnisotropy + ", mSupportsProtected=false, mSkipErrorChecks=" + this.mSkipErrorChecks + ", mMaxLabelLength=" + this.mMaxLabelLength + ", mDebugSupport=" + this.mDebugSupport + ", mBufferStorageSupport=" + this.mBufferStorageSupport + ", mBaseInstanceSupport=" + this.mBaseInstanceSupport + ", mDSASupport=" + this.mDSASupport + ", mDSAElementBufferBroken=" + this.mDSAElementBufferBroken + ", mInvalidateBufferType=" + this.mInvalidateBufferType + (includeFormatTable ? ", mFormatTable=" + Arrays.toString(this.mFormatTable) : "") + ", mColorTypeToBackendFormat=" + Arrays.toString(this.mColorTypeToBackendFormat) + ", mCompressionTypeToBackendFormat=" + Arrays.toString(this.mCompressionTypeToBackendFormat) + ", mShaderCaps=" + this.mShaderCaps + ", mAnisotropySupport=" + this.mAnisotropySupport + ", mGpuTracingSupport=" + this.mGpuTracingSupport + ", mConservativeRasterSupport=" + this.mConservativeRasterSupport + ", mTransferPixelsToRowBytesSupport=" + this.mTransferPixelsToRowBytesSupport + ", mMustSyncGpuDuringDiscard=" + this.mMustSyncGpuDuringDiscard + ", mTextureBarrierSupport=" + this.mTextureBarrierSupport + ", mDynamicStateArrayGeometryProcessorTextureSupport=" + this.mDynamicStateArrayGeometryProcessorTextureSupport + ", mBlendEquationSupport=" + this.mBlendEquationSupport + ", mMapBufferFlags=" + this.mMapBufferFlags + ", mMaxRenderTargetSize=" + this.mMaxRenderTargetSize + ", mMaxPreferredRenderTargetSize=" + this.mMaxPreferredRenderTargetSize + ", mMaxVertexAttributes=" + this.mMaxVertexAttributes + ", mMaxTextureSize=" + this.mMaxTextureSize + ", mInternalMultisampleCount=" + this.mInternalMultisampleCount + ", mMaxPushConstantsSize=" + this.mMaxPushConstantsSize + "}";
    }

    static class ColorTypeInfo {

        int mColorType = 0;

        public static final int UPLOAD_DATA_FLAG = 1;

        public static final int RENDERABLE_FLAG = 2;

        int mFlags = 0;

        short mReadSwizzle = 12816;

        short mWriteSwizzle = 12816;

        GLCaps.ExternalIOFormat[] mExternalIOFormats;

        public int externalFormat(int srcColorType, boolean write) {
            for (GLCaps.ExternalIOFormat ioFormat : this.mExternalIOFormats) {
                if (ioFormat.mColorType == srcColorType) {
                    if (write) {
                        return ioFormat.mExternalWriteFormat;
                    }
                    return ioFormat.mExternalReadFormat;
                }
            }
            return 0;
        }

        public int externalType(int srcColorType) {
            for (GLCaps.ExternalIOFormat ioFormat : this.mExternalIOFormats) {
                if (ioFormat.mColorType == srcColorType) {
                    return ioFormat.mExternalType;
                }
            }
            return 0;
        }

        public String toString() {
            return "ColorTypeInfo{mColorType=" + ImageInfo.colorTypeToString(this.mColorType) + ", mFlags=0x" + Integer.toHexString(this.mFlags) + ", mReadSwizzle=" + Swizzle.toString(this.mReadSwizzle) + ", mWriteSwizzle=" + Swizzle.toString(this.mWriteSwizzle) + ", mExternalIOFormats=" + Arrays.toString(this.mExternalIOFormats) + "}";
        }
    }

    static class ExternalIOFormat {

        int mColorType = 0;

        int mExternalType = 0;

        int mExternalWriteFormat = 0;

        int mExternalReadFormat = 0;

        public String toString() {
            return "ExternalIOFormat{mColorType=" + ImageInfo.colorTypeToString(this.mColorType) + ", mExternalType=" + this.mExternalType + ", mExternalWriteFormat=" + this.mExternalWriteFormat + ", mExternalReadFormat=" + this.mExternalReadFormat + "}";
        }
    }

    static class FormatInfo {

        public static final int TEXTURABLE_FLAG = 1;

        public static final int COLOR_ATTACHMENT_FLAG = 2;

        public static final int COLOR_ATTACHMENT_WITH_MSAA_FLAG = 4;

        public static final int TEXTURE_STORAGE_FLAG = 8;

        public static final int TRANSFERS_FLAG = 16;

        int mFlags = 0;

        public static final int FORMAT_TYPE_UNKNOWN = 0;

        public static final int FORMAT_TYPE_NORMALIZED_FIXED_POINT = 1;

        public static final int FORMAT_TYPE_FLOAT = 2;

        int mFormatType = 0;

        int mInternalFormatForTexture = 0;

        int mInternalFormatForRenderbuffer = 0;

        int mDefaultExternalFormat = 0;

        int mDefaultExternalType = 0;

        int mDefaultColorType = 0;

        int mViewCompatibilityClass = 0;

        int[] mColorSampleCounts = new int[0];

        GLCaps.ColorTypeInfo[] mColorTypeInfos = new GLCaps.ColorTypeInfo[0];

        public int colorTypeFlags(int colorType) {
            for (GLCaps.ColorTypeInfo info : this.mColorTypeInfos) {
                if (info.mColorType == colorType) {
                    return info.mFlags;
                }
            }
            return 0;
        }

        public int externalFormat(int dstColorType, int srcColorType, boolean write) {
            for (GLCaps.ColorTypeInfo info : this.mColorTypeInfos) {
                if (info.mColorType == dstColorType) {
                    return info.externalFormat(srcColorType, write);
                }
            }
            return 0;
        }

        public int externalType(int dstColorType, int srcColorType) {
            for (GLCaps.ColorTypeInfo info : this.mColorTypeInfos) {
                if (info.mColorType == dstColorType) {
                    return info.externalType(srcColorType);
                }
            }
            return 0;
        }

        public String toString() {
            return "FormatInfo{mFlags=0x" + Integer.toHexString(this.mFlags) + ", mFormatType=" + this.mFormatType + ", mInternalFormatForTexture=" + this.mInternalFormatForTexture + ", mInternalFormatForRenderbuffer=" + this.mInternalFormatForRenderbuffer + ", mDefaultExternalFormat=" + this.mDefaultExternalFormat + ", mDefaultExternalType=" + this.mDefaultExternalType + ", mDefaultColorType=" + ImageInfo.colorTypeToString(this.mDefaultColorType) + ", mColorSampleCounts=" + Arrays.toString(this.mColorSampleCounts) + ", mColorTypeInfos=" + Arrays.toString(this.mColorTypeInfos) + "}";
        }
    }
}