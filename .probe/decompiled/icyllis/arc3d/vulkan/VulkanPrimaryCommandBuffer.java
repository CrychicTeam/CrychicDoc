package icyllis.arc3d.vulkan;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkCommandBufferAllocateInfo;
import org.lwjgl.vulkan.VkDevice;

public final class VulkanPrimaryCommandBuffer extends VulkanCommandBuffer {

    private VulkanPrimaryCommandBuffer(VkDevice device, long handle) {
        super(device, handle);
    }

    public static VulkanPrimaryCommandBuffer create(VulkanDevice device, long commandPool) {
        MemoryStack stack = MemoryStack.stackPush();
        VulkanPrimaryCommandBuffer var9;
        label43: {
            try {
                PointerBuffer pCommandBuffer = stack.mallocPointer(1);
                int result = VKCore.vkAllocateCommandBuffers(device.device(), VkCommandBufferAllocateInfo.malloc(stack).sType$Default().pNext(0L).commandPool(commandPool).level(0).commandBufferCount(1), pCommandBuffer);
                if (result != 0) {
                    var9 = null;
                    break label43;
                }
                var9 = new VulkanPrimaryCommandBuffer(device.device(), pCommandBuffer.get(0));
            } catch (Throwable var8) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            if (stack != null) {
                stack.close();
            }
            return var9;
        }
        if (stack != null) {
            stack.close();
        }
        return var9;
    }
}