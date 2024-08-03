package icyllis.arc3d.vulkan;

import org.lwjgl.vulkan.VkCommandBuffer;
import org.lwjgl.vulkan.VkDevice;

public abstract class VulkanCommandBuffer extends VkCommandBuffer {

    protected boolean mIsRecording = false;

    public VulkanCommandBuffer(VkDevice device, long handle) {
        super(handle, device);
    }

    public void draw(int vertexCount, int instanceCount, int firstVertex, int firstInstance) {
        VKCore.vkCmdDraw(this, vertexCount, instanceCount, firstVertex, firstInstance);
    }

    public boolean isRecording() {
        return this.mIsRecording;
    }
}