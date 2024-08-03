package icyllis.arc3d.opengl;

import icyllis.arc3d.core.SLDataType;
import icyllis.arc3d.engine.Processor;
import icyllis.arc3d.engine.ShaderVar;
import icyllis.arc3d.engine.shading.PipelineBuilder;
import icyllis.arc3d.engine.shading.UniformHandler;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import java.util.ArrayList;

public class GLUniformHandler extends UniformHandler {

    final ArrayList<UniformHandler.UniformInfo> mUniforms = new ArrayList();

    final ArrayList<UniformHandler.UniformInfo> mSamplers = new ArrayList();

    final ShortArrayList mSamplerSwizzles = new ShortArrayList();

    int mCurrentOffset;

    GLUniformHandler(PipelineBuilder pipelineBuilder) {
        super(pipelineBuilder);
    }

    @Override
    public ShaderVar getUniformVariable(int handle) {
        return ((UniformHandler.UniformInfo) this.mUniforms.get(handle)).mVariable;
    }

    @Override
    public int numUniforms() {
        return this.mUniforms.size();
    }

    @Override
    public UniformHandler.UniformInfo uniform(int index) {
        return (UniformHandler.UniformInfo) this.mUniforms.get(index);
    }

    @Override
    protected int internalAddUniformArray(Processor owner, int visibility, byte type, String name, int arrayCount) {
        assert SLDataType.canBeUniformValue(type);
        assert visibility != 0;
        assert !name.contains("__");
        String resolvedName;
        if (name.startsWith("SV_")) {
            resolvedName = name;
        } else {
            resolvedName = this.mPipelineBuilder.nameVariable('u', name);
        }
        assert !resolvedName.contains("__");
        int offset = getAlignedOffset(this.mCurrentOffset, type, arrayCount, false);
        this.mCurrentOffset = this.mCurrentOffset + getAlignedStride(type, arrayCount, false);
        int handle = this.mUniforms.size();
        String layoutQualifier;
        if (this.mPipelineBuilder.shaderCaps().mEnhancedLayouts) {
            layoutQualifier = "offset = " + offset;
        } else {
            layoutQualifier = "";
        }
        UniformHandler.UniformInfo tempInfo = new UniformHandler.UniformInfo();
        tempInfo.mVariable = new ShaderVar(resolvedName, type, (byte) 0, arrayCount, layoutQualifier, "");
        tempInfo.mVisibility = visibility;
        tempInfo.mOwner = owner;
        tempInfo.mRawName = name;
        tempInfo.mOffset = offset;
        this.mUniforms.add(tempInfo);
        return handle;
    }

    @Override
    protected int addSampler(int samplerState, short swizzle, String name) {
        assert name != null && !name.isEmpty();
        String resolvedName = this.mPipelineBuilder.nameVariable('u', name);
        int handle = this.mSamplers.size();
        String layoutQualifier;
        if (this.mPipelineBuilder.shaderCaps().mShadingLanguage420Pack) {
            layoutQualifier = "binding = " + handle;
        } else {
            layoutQualifier = "";
        }
        UniformHandler.UniformInfo tempInfo = new UniformHandler.UniformInfo();
        tempInfo.mVariable = new ShaderVar(resolvedName, (byte) 35, (byte) 4, 0, layoutQualifier, "");
        tempInfo.mVisibility = 2;
        tempInfo.mOwner = null;
        tempInfo.mRawName = name;
        this.mSamplers.add(tempInfo);
        this.mSamplerSwizzles.add(swizzle);
        assert this.mSamplers.size() == this.mSamplerSwizzles.size();
        return handle;
    }

    @Override
    protected String samplerVariable(int handle) {
        return ((UniformHandler.UniformInfo) this.mSamplers.get(handle)).mVariable.getName();
    }

    @Override
    protected short samplerSwizzle(int handle) {
        return this.mSamplerSwizzles.getShort(handle);
    }

    @Override
    protected void appendUniformDecls(int visibility, StringBuilder out) {
        assert visibility != 0;
        boolean firstMember = false;
        boolean firstVisible = false;
        for (UniformHandler.UniformInfo uniform : this.mUniforms) {
            assert SLDataType.canBeUniformValue(uniform.mVariable.getType());
            if (!firstMember) {
                assert uniform.mOffset == 0;
                firstMember = true;
            }
            if ((uniform.mVisibility & visibility) != 0) {
                firstVisible = true;
            }
        }
        if (firstVisible) {
            out.append("layout(std140");
            if (this.mPipelineBuilder.shaderCaps().mShadingLanguage420Pack) {
                out.append(", binding = ");
                out.append(0);
                String extensionName = this.mPipelineBuilder.shaderCaps().mShadingLanguage420PackExtensionName;
                this.mPipelineBuilder.addExtension(visibility, extensionName);
            }
            out.append(") uniform ");
            out.append("UniformBlock");
            out.append(" {\n");
            for (UniformHandler.UniformInfo uniform : this.mUniforms) {
                uniform.mVariable.appendDecl(out);
                out.append(";\n");
            }
            out.append("};\n");
            if (this.mPipelineBuilder.shaderCaps().mEnhancedLayouts) {
                String extensionName = this.mPipelineBuilder.shaderCaps().mEnhancedLayoutsExtensionName;
                this.mPipelineBuilder.addExtension(visibility, extensionName);
            }
        }
        for (UniformHandler.UniformInfo sampler : this.mSamplers) {
            assert sampler.mVariable.getType() == 35;
            if ((sampler.mVisibility & visibility) != 0) {
                sampler.mVariable.appendDecl(out);
                out.append(";\n");
                if (this.mPipelineBuilder.shaderCaps().mShadingLanguage420Pack) {
                    String extensionName = this.mPipelineBuilder.shaderCaps().mShadingLanguage420PackExtensionName;
                    this.mPipelineBuilder.addExtension(visibility, extensionName);
                }
            }
        }
    }
}