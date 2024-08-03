package icyllis.arc3d.vulkan;

import icyllis.arc3d.engine.ManagedResource;
import java.nio.LongBuffer;
import javax.annotation.Nullable;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkCommandPoolCreateInfo;
import org.lwjgl.vulkan.VkFenceCreateInfo;

public class VulkanCommandPool extends ManagedResource {

    private VulkanPrimaryCommandBuffer mPrimaryCommandBuffer;

    private long mCommandPool;

    private boolean mSubmitted;

    private final long[] mSubmitFence = new long[1];

    private VulkanCommandPool(VulkanDevice device, long handle, VulkanPrimaryCommandBuffer primaryCommandBuffer) {
        super(device);
        this.mCommandPool = handle;
        this.mPrimaryCommandBuffer = primaryCommandBuffer;
    }

    @Nullable
    public static VulkanCommandPool create(VulkanDevice device) {
        int cmdPoolCreateFlags = 1;
        if (device.isProtectedContext()) {
            cmdPoolCreateFlags |= 4;
        }
        MemoryStack stack = MemoryStack.stackPush();
        Object var7;
        label51: {
            long commandPool;
            try {
                LongBuffer pCommandPool = stack.mallocLong(1);
                int result = VKCore.vkCreateCommandPool(device.device(), VkCommandPoolCreateInfo.malloc(stack).sType$Default().pNext(0L).flags(cmdPoolCreateFlags).queueFamilyIndex(device.getQueueIndex()), null, pCommandPool);
                if (result != 0) {
                    var7 = null;
                    break label51;
                }
                commandPool = pCommandPool.get(0);
            } catch (Throwable var9) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }
                throw var9;
            }
            if (stack != null) {
                stack.close();
            }
            VulkanPrimaryCommandBuffer primaryCommandBuffer = VulkanPrimaryCommandBuffer.create(device, commandPool);
            if (primaryCommandBuffer == null) {
                VKCore.vkDestroyCommandPool(device.device(), commandPool, null);
                return null;
            }
            return new VulkanCommandPool(device, commandPool, primaryCommandBuffer);
        }
        if (stack != null) {
            stack.close();
        }
        return (VulkanCommandPool) var7;
    }

    @Override
    protected void deallocate() {
        VKCore.vkDestroyCommandPool(this.getDevice().device(), this.mCommandPool, null);
    }

    public boolean submit() {
        assert !this.mPrimaryCommandBuffer.isRecording();
        if (this.mSubmitFence[0] == 0L) {
            MemoryStack stack = MemoryStack.stackPush();
            boolean var3;
            label63: {
                try {
                    int result = VKCore.vkCreateFence(this.getDevice().device(), VkFenceCreateInfo.calloc(stack).sType$Default(), null, this.mSubmitFence);
                    if (result != 0) {
                        var3 = false;
                        break label63;
                    }
                    this.mSubmitFence[0] = 0L;
                } catch (Throwable var5) {
                    if (stack != null) {
                        try {
                            stack.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }
                    throw var5;
                }
                if (stack != null) {
                    stack.close();
                }
                return false;
            }
            if (stack != null) {
                stack.close();
            }
            return var3;
        } else {
            VKCore._CHECK_ERROR_(VKCore.vkResetFences(this.getDevice().device(), this.mSubmitFence));
            return false;
        }
    }

    public boolean check() {
        if (!this.mSubmitted) {
            return false;
        } else if (this.mSubmitFence[0] == 0L) {
            return true;
        } else {
            int result = VKCore.vkGetFenceStatus(this.getDevice().device(), this.mSubmitFence[0]);
            if (result == 0 || result == -4) {
                return true;
            } else if (result == 1) {
                return false;
            } else {
                throw new RuntimeException(VKCore.getResultMessage(result));
            }
        }
    }

    public void reset() {
        assert this.isSubmitted();
        VKCore.vkResetCommandPool(this.getDevice().device(), this.mCommandPool, 0);
        this.mSubmitted = false;
    }

    public boolean isSubmitted() {
        return this.mSubmitted;
    }

    protected VulkanDevice getDevice() {
        return (VulkanDevice) super.getDevice();
    }
}