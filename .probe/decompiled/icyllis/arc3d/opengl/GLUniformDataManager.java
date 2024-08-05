package icyllis.arc3d.opengl;

import icyllis.arc3d.core.MathUtil;
import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SLDataType;
import icyllis.arc3d.engine.UniformDataManager;
import icyllis.arc3d.engine.shading.UniformHandler;
import java.util.List;
import org.lwjgl.opengl.GL15C;

public class GLUniformDataManager extends UniformDataManager {

    private int mRTWidth;

    private int mRTHeight;

    private boolean mRTFlipY;

    private GLUniformBuffer mUniformBuffer;

    GLUniformDataManager(List<UniformHandler.UniformInfo> uniforms, int uniformSize) {
        super(uniforms.size(), uniformSize);
        int i = 0;
        for (int e = uniforms.size(); i < e; i++) {
            UniformHandler.UniformInfo uniformInfo = (UniformHandler.UniformInfo) uniforms.get(i);
            assert (uniformInfo.mOffset & 16777215) == uniformInfo.mOffset;
            assert MathUtil.isAlign4(uniformInfo.mOffset);
            assert SLDataType.canBeUniformValue(uniformInfo.mVariable.getType());
            this.mUniforms[i] = uniformInfo.mOffset | uniformInfo.mVariable.getType() << 24;
        }
    }

    @Override
    protected void deallocate() {
        super.deallocate();
        this.mUniformBuffer = RefCnt.move(this.mUniformBuffer);
    }

    public void setProjection(int u, int width, int height, boolean flipY) {
        if (width != this.mRTWidth || height != this.mRTHeight || flipY != this.mRTFlipY) {
            if (flipY) {
                this.set4f(u, 2.0F / (float) width, -1.0F, -2.0F / (float) height, 1.0F);
            } else {
                this.set4f(u, 2.0F / (float) width, -1.0F, 2.0F / (float) height, -1.0F);
            }
            this.mRTWidth = width;
            this.mRTHeight = height;
            this.mRTFlipY = flipY;
        }
    }

    public boolean bindAndUploadUniforms(GLDevice device, GLCommandBuffer commandBuffer) {
        if (!this.mUniformsDirty) {
            return true;
        } else {
            if (this.mUniformBuffer == null) {
                this.mUniformBuffer = GLUniformBuffer.make(device, this.mUniformSize, 0);
            }
            if (this.mUniformBuffer == null) {
                return false;
            } else {
                commandBuffer.bindUniformBuffer(this.mUniformBuffer);
                GL15C.nglBufferSubData(35345, 0L, (long) this.mUniformSize, this.mUniformData);
                this.mUniformsDirty = false;
                return true;
            }
        }
    }
}