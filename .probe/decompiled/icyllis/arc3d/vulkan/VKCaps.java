package icyllis.arc3d.vulkan;

import icyllis.arc3d.compiler.GLSLVersion;
import icyllis.arc3d.compiler.SPIRVVersion;
import icyllis.arc3d.compiler.ShaderCaps;
import icyllis.arc3d.compiler.TargetApi;
import icyllis.arc3d.engine.Caps;
import icyllis.arc3d.engine.ContextOptions;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VKCapabilitiesDevice;
import org.lwjgl.vulkan.VKCapabilitiesInstance;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures2;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;

public abstract class VKCaps extends Caps {

    public VKCaps(ContextOptions options, VkPhysicalDevice physDev, int physicalDeviceVersion, VkPhysicalDeviceFeatures2 deviceFeatures2, VKCapabilitiesInstance capabilitiesInstance, VKCapabilitiesDevice capabilitiesDevice) {
        super(options);
        ShaderCaps shaderCaps = this.mShaderCaps;
        shaderCaps.mTargetApi = TargetApi.VULKAN_1_0;
        shaderCaps.mGLSLVersion = GLSLVersion.GLSL_450;
        MemoryStack stack = MemoryStack.stackPush();
        try {
            VkPhysicalDeviceProperties properties = VkPhysicalDeviceProperties.malloc(stack);
            VKCore.vkGetPhysicalDeviceProperties(physDev, properties);
            if (Integer.compareUnsigned(physicalDeviceVersion, VKCore.VK_MAKE_VERSION(1, 3, 0)) >= 0) {
                shaderCaps.mSPIRVVersion = SPIRVVersion.SPIRV_1_6;
            } else if (Integer.compareUnsigned(physicalDeviceVersion, VKCore.VK_MAKE_VERSION(1, 2, 0)) >= 0) {
                shaderCaps.mSPIRVVersion = SPIRVVersion.SPIRV_1_5;
            } else if (Integer.compareUnsigned(physicalDeviceVersion, VKCore.VK_MAKE_VERSION(1, 1, 0)) >= 0) {
                shaderCaps.mSPIRVVersion = SPIRVVersion.SPIRV_1_3;
            } else {
                shaderCaps.mSPIRVVersion = SPIRVVersion.SPIRV_1_0;
            }
        } catch (Throwable var12) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var11) {
                    var12.addSuppressed(var11);
                }
            }
            throw var12;
        }
        if (stack != null) {
            stack.close();
        }
    }
}