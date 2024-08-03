package icyllis.arc3d.opengl;

import icyllis.arc3d.engine.PipelineDesc;
import icyllis.arc3d.engine.PipelineInfo;
import icyllis.arc3d.engine.shading.PipelineBuilder;
import icyllis.arc3d.engine.shading.UniformHandler;
import icyllis.arc3d.engine.shading.VaryingHandler;
import java.io.PrintWriter;
import java.lang.ref.Reference;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class GLPipelineStateBuilder extends PipelineBuilder {

    private final GLDevice mDevice;

    private final VaryingHandler mVaryingHandler;

    private final GLUniformHandler mUniformHandler;

    private ByteBuffer mFinalizedVertSource;

    private ByteBuffer mFinalizedFragSource;

    private GLPipelineStateBuilder(GLDevice device, PipelineDesc desc, PipelineInfo pipelineInfo) {
        super(desc, pipelineInfo);
        this.mDevice = device;
        this.mVaryingHandler = new VaryingHandler(this);
        this.mUniformHandler = new GLUniformHandler(this);
    }

    @Nonnull
    public static GLGraphicsPipelineState createGraphicsPipelineState(GLDevice device, PipelineDesc desc, PipelineInfo pipelineInfo) {
        return new GLGraphicsPipelineState(device, CompletableFuture.supplyAsync(() -> {
            GLPipelineStateBuilder builder = new GLPipelineStateBuilder(device, desc, pipelineInfo);
            builder.build();
            return builder;
        }));
    }

    private void build() {
        if (this.emitAndInstallProcs()) {
            this.mVaryingHandler.finish();
            this.mFinalizedVertSource = this.mVS.toUTF8();
            this.mFinalizedFragSource = this.mFS.toUTF8();
            this.mVS = null;
            this.mFS = null;
        }
    }

    boolean finish(GLGraphicsPipelineState dest) {
        if (this.mFinalizedVertSource != null && this.mFinalizedFragSource != null) {
            int program = GLCore.glCreateProgram();
            if (program == 0) {
                return false;
            } else {
                PrintWriter errorWriter = this.mDevice.getContext().getErrorWriter();
                int frag = GLCore.glCompileShader(35632, this.mFinalizedFragSource, this.mDevice.getPipelineStateCache().getStats(), errorWriter);
                if (frag == 0) {
                    GLCore.glDeleteProgram(program);
                    return false;
                } else {
                    int vert = GLCore.glCompileShader(35633, this.mFinalizedVertSource, this.mDevice.getPipelineStateCache().getStats(), errorWriter);
                    if (vert == 0) {
                        GLCore.glDeleteProgram(program);
                        GLCore.glDeleteShader(frag);
                        return false;
                    } else {
                        GLCore.glAttachShader(program, vert);
                        GLCore.glAttachShader(program, frag);
                        GLCore.glLinkProgram(program);
                        if (GLCore.glGetProgrami(program, 35714) == 0) {
                            boolean var19;
                            try {
                                String log = GLCore.glGetProgramInfoLog(program);
                                GLCore.handleLinkError(errorWriter, new String[] { "Vertex GLSL", "Fragment GLSL" }, new String[] { MemoryUtil.memUTF8(this.mFinalizedVertSource), MemoryUtil.memUTF8(this.mFinalizedFragSource) }, log);
                                var19 = false;
                            } finally {
                                Reference.reachabilityFence(this.mFinalizedVertSource);
                                Reference.reachabilityFence(this.mFinalizedFragSource);
                                GLCore.glDeleteProgram(program);
                                GLCore.glDeleteShader(frag);
                                GLCore.glDeleteShader(vert);
                            }
                            return var19;
                        } else {
                            GLCore.glDetachShader(program, vert);
                            GLCore.glDetachShader(program, frag);
                            GLCore.glDeleteShader(frag);
                            GLCore.glDeleteShader(vert);
                            MemoryStack stack = MemoryStack.stackPush();
                            try {
                                IntBuffer pLength = stack.mallocInt(1);
                                IntBuffer pBinaryFormat = stack.mallocInt(1);
                                GLCore.glGetProgramiv(program, 34625, pLength);
                                int len = pLength.get(0);
                                System.out.println(len);
                                if (len > 0) {
                                    ByteBuffer pBinary = stack.malloc(len);
                                    GLCore.glGetProgramBinary(program, pLength, pBinaryFormat, pBinary);
                                    System.out.println(pBinaryFormat.get(0));
                                }
                            } catch (Throwable var16) {
                                if (stack != null) {
                                    try {
                                        stack.close();
                                    } catch (Throwable var14) {
                                        var16.addSuppressed(var14);
                                    }
                                }
                                throw var16;
                            }
                            if (stack != null) {
                                stack.close();
                            }
                            GLVertexArray vertexArray = GLVertexArray.make(this.mDevice, this.mPipelineInfo.geomProc());
                            if (vertexArray == null) {
                                GLCore.glDeleteProgram(program);
                                return false;
                            } else {
                                dest.init(new GLProgram(this.mDevice, program), vertexArray, this.mUniformHandler.mUniforms, this.mUniformHandler.mCurrentOffset, this.mUniformHandler.mSamplers, this.mGPImpl);
                                return true;
                            }
                        }
                    }
                }
            }
        } else {
            return false;
        }
    }

    public GLCaps caps() {
        return this.mDevice.getCaps();
    }

    @Override
    public UniformHandler uniformHandler() {
        return this.mUniformHandler;
    }

    @Override
    public VaryingHandler varyingHandler() {
        return this.mVaryingHandler;
    }
}