package icyllis.arc3d.vulkan;

import javax.annotation.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.vma.Vma;
import org.lwjgl.util.vma.VmaAllocatorCreateInfo;
import org.lwjgl.util.vma.VmaVulkanFunctions;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkPhysicalDevice;

public class VulkanMemoryAllocator implements AutoCloseable {

    private long mAllocator;

    private VulkanMemoryAllocator(long allocator) {
        this.mAllocator = allocator;
    }

    @Nullable
    public static VulkanMemoryAllocator make(VkInstance instance, VkPhysicalDevice physicalDevice, VkDevice device, int physicalDeviceVersion) {
        MemoryStack stack = MemoryStack.stackPush();
        VulkanMemoryAllocator var11;
        label43: {
            try {
                VmaVulkanFunctions pVulkanFunctions = VmaVulkanFunctions.calloc(stack).set(instance, device);
                VmaAllocatorCreateInfo pCreateInfo = VmaAllocatorCreateInfo.calloc(stack).physicalDevice(physicalDevice).device(device).pVulkanFunctions(pVulkanFunctions).instance(instance);
                PointerBuffer pAllocator = stack.pointers(0L);
                if (Vma.vmaCreateAllocator(pCreateInfo, pAllocator) != 0) {
                    var11 = null;
                    break label43;
                }
                var11 = new VulkanMemoryAllocator(pAllocator.get(0));
            } catch (Throwable var10) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }
                }
                throw var10;
            }
            if (stack != null) {
                stack.close();
            }
            return var11;
        }
        if (stack != null) {
            stack.close();
        }
        return var11;
    }

    public void close() {
        if (this.mAllocator != 0L) {
            Vma.vmaDestroyAllocator(this.mAllocator);
        }
        this.mAllocator = 0L;
    }

    public int allocateMemoryForBuffer(long buffer, VulkanAllocation allocation) {
        return -1;
    }
}