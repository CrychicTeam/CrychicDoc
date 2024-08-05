package icyllis.arc3d.compiler;

public class ShaderCaps {

    public TargetApi mTargetApi = TargetApi.OPENGL_4_5;

    public GLSLVersion mGLSLVersion = GLSLVersion.GLSL_450;

    public SPIRVVersion mSPIRVVersion = SPIRVVersion.SPIRV_1_0;

    public String toString() {
        return "ShaderCaps{mTargetApi=" + this.mTargetApi + ", mGLSLVersion=" + this.mGLSLVersion + ", mSPIRVVersion=" + this.mSPIRVVersion + "}";
    }
}