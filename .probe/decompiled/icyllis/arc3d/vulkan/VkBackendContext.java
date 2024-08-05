package icyllis.arc3d.vulkan;

import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures2;
import org.lwjgl.vulkan.VkQueue;

public final class VkBackendContext {

    public VkInstance mInstance;

    public VkPhysicalDevice mPhysicalDevice;

    public VkDevice mDevice;

    public VkQueue mQueue;

    public int mGraphicsQueueIndex;

    public int mMaxAPIVersion;

    public VkPhysicalDeviceFeatures mDeviceFeatures;

    public VkPhysicalDeviceFeatures2 mDeviceFeatures2;

    public boolean mProtectedContext;
}