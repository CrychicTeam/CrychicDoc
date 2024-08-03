package icyllis.modernui.core;

import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.ContextOptions;
import icyllis.arc3d.engine.DirectContext;
import icyllis.arc3d.vulkan.VKCore;
import icyllis.arc3d.vulkan.VkBackendContext;
import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import org.lwjgl.vulkan.VK;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkDevice;
import org.lwjgl.vulkan.VkDeviceCreateInfo;
import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.lwjgl.vulkan.VkPhysicalDevice;
import org.lwjgl.vulkan.VkPhysicalDeviceBlendOperationAdvancedFeaturesEXT;
import org.lwjgl.vulkan.VkPhysicalDeviceFeatures2;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties;
import org.lwjgl.vulkan.VkPhysicalDeviceProperties2;
import org.lwjgl.vulkan.VkQueueFamilyProperties;
import org.lwjgl.vulkan.VkExtensionProperties.Buffer;

public final class VulkanManager implements AutoCloseable {

    private static volatile VulkanManager sInstance;

    private VkInstance mInstance;

    private VkPhysicalDevice mPhysicalDevice;

    private VkDevice mDevice;

    private int mGraphicsQueueIndex = -1;

    private int mComputeQueueIndex = -1;

    private final Object2IntOpenHashMap<String> mInstanceExtensions = new Object2IntOpenHashMap();

    private final Object2IntOpenHashMap<String> mDeviceExtensions = new Object2IntOpenHashMap();

    private VkPhysicalDeviceFeatures2 mPhysicalDeviceFeatures2;

    private int mDriverVersion;

    private VulkanManager() {
    }

    @NonNull
    public static VulkanManager getInstance() {
        if (sInstance == null) {
            synchronized (VulkanManager.class) {
                if (sInstance == null) {
                    sInstance = new VulkanManager();
                }
            }
        }
        return sInstance;
    }

    public synchronized void initialize() {
        if (this.mDevice == null) {
            if (!GLFWVulkan.glfwVulkanSupported()) {
                TinyFileDialogs.tinyfd_messageBox("Failed to launch Modern UI", "Vulkan is not supported on your current platform. Make sure your operating system and graphics card drivers are up-to-date.", "ok", "error", true);
                throw new RuntimeException("Vulkan is not supported");
            } else {
                int version = VK.getInstanceVersionSupported();
                ModernUI.LOGGER.info(ModernUI.MARKER, "Vulkan version: {}.{}.{}", VKCore.VK_VERSION_MAJOR(version), VKCore.VK_VERSION_MINOR(version), VKCore.VK_VERSION_PATCH(version));
                if (version < VKCore.VK_API_VERSION_1_1) {
                    TinyFileDialogs.tinyfd_messageBox("Failed to launch Modern UI", "Vulkan 1.1 is not supported on your current platform. Make sure your operating system and graphics card drivers are up-to-date.", "ok", "error", true);
                    throw new RuntimeException("Vulkan 1.1 is not supported");
                } else {
                    MemoryStack stack = MemoryStack.stackPush();
                    try {
                        IntBuffer pCount = stack.mallocInt(1);
                        VKCore._CHECK_(VKCore.vkEnumerateInstanceExtensionProperties((ByteBuffer) null, pCount, null));
                        int count = pCount.get(0);
                        Buffer properties = VkExtensionProperties.malloc(count, stack);
                        VKCore._CHECK_(VKCore.vkEnumerateInstanceExtensionProperties((ByteBuffer) null, pCount, properties));
                        for (VkExtensionProperties prop : properties) {
                            this.mInstanceExtensions.putIfAbsent(prop.extensionNameString(), prop.specVersion());
                        }
                    } catch (Throwable var31) {
                        if (stack != null) {
                            try {
                                stack.close();
                            } catch (Throwable var25) {
                                var31.addSuppressed(var25);
                            }
                        }
                        throw var31;
                    }
                    if (stack != null) {
                        stack.close();
                    }
                    ModernUI.LOGGER.info(ModernUI.MARKER, "Enumerated {} instance extensions", this.mInstanceExtensions.size());
                    ModernUI.LOGGER.debug(ModernUI.MARKER, this.mInstanceExtensions);
                    stack = MemoryStack.stackPush();
                    try {
                        ByteBuffer appName = stack.UTF8("Modern UI", true);
                        ByteBuffer engineName = stack.UTF8("Arc 3D", true);
                        VkApplicationInfo appInfo = VkApplicationInfo.calloc(stack).sType$Default().pApplicationName(appName).pEngineName(engineName).apiVersion(version);
                        VkInstanceCreateInfo pCreateInfo = VkInstanceCreateInfo.calloc(stack).sType$Default().pApplicationInfo(appInfo).ppEnabledLayerNames(null).ppEnabledExtensionNames(GLFWVulkan.glfwGetRequiredInstanceExtensions());
                        PointerBuffer pInstance = stack.mallocPointer(1);
                        VKCore._CHECK_ERROR_(VKCore.vkCreateInstance(pCreateInfo, null, pInstance));
                        this.mInstance = new VkInstance(pInstance.get(0), pCreateInfo);
                    } catch (Throwable var27) {
                        if (stack != null) {
                            try {
                                stack.close();
                            } catch (Throwable var24) {
                                var27.addSuppressed(var24);
                            }
                        }
                        throw var27;
                    }
                    if (stack != null) {
                        stack.close();
                    }
                    ModernUI.LOGGER.info(ModernUI.MARKER, "Created Vulkan instance, Engine: {}", "Arc 3D");
                    stack = MemoryStack.stackPush();
                    try {
                        IntBuffer pCount = stack.mallocInt(1);
                        VKCore._CHECK_(VKCore.vkEnumeratePhysicalDevices(this.mInstance, pCount, null));
                        int deviceCount = pCount.get(0);
                        if (deviceCount == 0) {
                            throw new RuntimeException("No GPU device was found");
                        }
                        PointerBuffer pPhysicalDevices = stack.mallocPointer(deviceCount);
                        VKCore._CHECK_(VKCore.vkEnumeratePhysicalDevices(this.mInstance, pCount, pPhysicalDevices));
                        for (int i = 0; i < deviceCount; i++) {
                            VkPhysicalDevice physicalDevice = new VkPhysicalDevice(pPhysicalDevices.get(i), this.mInstance);
                            if (this.choosePhysicalDeviceLocked(physicalDevice)) {
                                break;
                            }
                        }
                    } catch (Throwable var30) {
                        if (stack != null) {
                            try {
                                stack.close();
                            } catch (Throwable var23) {
                                var30.addSuppressed(var23);
                            }
                        }
                        throw var30;
                    }
                    if (stack != null) {
                        stack.close();
                    }
                    if (this.mPhysicalDevice == null) {
                        TinyFileDialogs.tinyfd_messageBox("Failed to launch Modern UI", "You don't have a device with a Vulkan queue family that supports both graphics and compute.", "ok", "error", true);
                        throw new RuntimeException("No suitable physical device was found");
                    } else {
                        MemoryStack stackx = MemoryStack.stackPush();
                        try {
                            IntBuffer pCount = stackx.mallocInt(1);
                            VKCore._CHECK_(VKCore.vkEnumerateDeviceExtensionProperties(this.mPhysicalDevice, (ByteBuffer) null, pCount, null));
                            int count = pCount.get(0);
                            Buffer properties = VkExtensionProperties.malloc(count, stackx);
                            VKCore._CHECK_(VKCore.vkEnumerateDeviceExtensionProperties(this.mPhysicalDevice, (ByteBuffer) null, pCount, properties));
                            extensionNames = MemoryUtil.memAllocPointer(count);
                            for (VkExtensionProperties prop : properties) {
                                extensionNames.put(prop.extensionName());
                                this.mDeviceExtensions.putIfAbsent(prop.extensionNameString(), prop.specVersion());
                            }
                            extensionNames.flip();
                        } catch (Throwable var29) {
                            if (stackx != null) {
                                try {
                                    stackx.close();
                                } catch (Throwable var22) {
                                    var29.addSuppressed(var22);
                                }
                            }
                            throw var29;
                        }
                        if (stackx != null) {
                            stackx.close();
                        }
                        ModernUI.LOGGER.info(ModernUI.MARKER, "Enumerated {} device extensions", this.mDeviceExtensions.size());
                        ModernUI.LOGGER.debug(ModernUI.MARKER, this.mDeviceExtensions);
                        this.mPhysicalDeviceFeatures2 = VkPhysicalDeviceFeatures2.calloc().sType$Default();
                        if (this.mDeviceExtensions.getInt("VK_EXT_blend_operation_advanced") >= 2) {
                            ModernUI.LOGGER.info(ModernUI.MARKER, "Enabled {}", "VK_EXT_blend_operation_advanced");
                            this.mPhysicalDeviceFeatures2.pNext(VkPhysicalDeviceBlendOperationAdvancedFeaturesEXT.calloc().sType$Default());
                        } else {
                            ModernUI.LOGGER.info(ModernUI.MARKER, "Disabled {}", "VK_EXT_blend_operation_advanced");
                        }
                        VKCore.vkGetPhysicalDeviceFeatures2(this.mPhysicalDevice, this.mPhysicalDeviceFeatures2);
                        this.mPhysicalDeviceFeatures2.features().robustBufferAccess(false);
                        try {
                            stackx = MemoryStack.stackPush();
                            try {
                                org.lwjgl.vulkan.VkDeviceQueueCreateInfo.Buffer queueInfos = VkDeviceQueueCreateInfo.calloc(1, stackx).sType$Default().queueFamilyIndex(this.mGraphicsQueueIndex).pQueuePriorities(stackx.floats(0.0F));
                                VkDeviceCreateInfo pCreateInfo = VkDeviceCreateInfo.calloc(stackx).sType$Default().pNext(this.mPhysicalDeviceFeatures2).pQueueCreateInfos(queueInfos).ppEnabledExtensionNames(extensionNames);
                                PointerBuffer pDevice = stackx.mallocPointer(1);
                                VKCore._CHECK_(VKCore.vkCreateDevice(this.mPhysicalDevice, pCreateInfo, null, pDevice));
                                this.mDevice = new VkDevice(pDevice.get(0), this.mPhysicalDevice, pCreateInfo, VKCore.VK_API_VERSION_1_1);
                            } catch (Throwable var26) {
                                if (stackx != null) {
                                    try {
                                        stackx.close();
                                    } catch (Throwable var21) {
                                        var26.addSuppressed(var21);
                                    }
                                }
                                throw var26;
                            }
                            if (stackx != null) {
                                stackx.close();
                            }
                        } finally {
                            MemoryUtil.memFree(extensionNames);
                        }
                        ModernUI.LOGGER.info(ModernUI.MARKER, "Created Vulkan device, Queue index: {}", this.mGraphicsQueueIndex);
                    }
                }
            }
        }
    }

    private boolean choosePhysicalDeviceLocked(@NonNull VkPhysicalDevice physicalDevice) {
        MemoryStack stack = MemoryStack.stackPush();
        boolean var13;
        label114: {
            boolean var14;
            label115: {
                int flags;
                label116: {
                    int vendorID;
                    try {
                        VkPhysicalDeviceProperties2 properties2 = VkPhysicalDeviceProperties2.calloc(stack).sType$Default();
                        VKCore.vkGetPhysicalDeviceProperties2(physicalDevice, properties2);
                        VkPhysicalDeviceProperties properties = properties2.properties();
                        ModernUI.LOGGER.info(ModernUI.MARKER, "List device ID {}, Name: {}, Type: {}", properties.deviceID(), properties.deviceNameString(), VKCore.getPhysicalDeviceTypeName(properties.deviceType()));
                        if (properties.apiVersion() < VKCore.VK_API_VERSION_1_1) {
                            ModernUI.LOGGER.info(ModernUI.MARKER, "Skip device ID {} because it does not support Vulkan 1.1", properties.deviceID());
                            var13 = false;
                            break label114;
                        }
                        IntBuffer pCount = stack.mallocInt(1);
                        VKCore.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice, pCount, null);
                        int count = pCount.get(0);
                        if (count == 0) {
                            ModernUI.LOGGER.info(ModernUI.MARKER, "Skip device ID {} because no queue family was found", properties.deviceID());
                            var14 = false;
                            break label115;
                        }
                        org.lwjgl.vulkan.VkQueueFamilyProperties.Buffer queues = VkQueueFamilyProperties.malloc(count, stack);
                        VKCore.vkGetPhysicalDeviceQueueFamilyProperties(physicalDevice, pCount, queues);
                        this.mGraphicsQueueIndex = -1;
                        this.mComputeQueueIndex = -1;
                        for (int j = 0; j < count; j++) {
                            VkQueueFamilyProperties queue = (VkQueueFamilyProperties) queues.get(j);
                            if (queue.queueCount() != 0) {
                                flags = queue.queueFlags();
                                if (this.mGraphicsQueueIndex == -1 && (flags & 1) != 0) {
                                    this.mGraphicsQueueIndex = j;
                                }
                                if (this.mComputeQueueIndex == -1 && (flags & 2) != 0) {
                                    this.mComputeQueueIndex = j;
                                }
                                if (this.mGraphicsQueueIndex > 0 && this.mComputeQueueIndex > 0) {
                                    break;
                                }
                            }
                        }
                        if (this.mGraphicsQueueIndex != -1 && this.mComputeQueueIndex != -1) {
                            this.mPhysicalDevice = physicalDevice;
                            vendorID = properties.vendorID();
                            int driverVersion = properties.driverVersion();
                            this.mDriverVersion = driverVersion;
                            Logger var10000 = ModernUI.LOGGER;
                            Marker var10001 = ModernUI.MARKER;
                            Integer var10003 = properties.deviceID();
                            String var10004 = switch(vendorID) {
                                case 4098 ->
                                    "AMD";
                                case 4112 ->
                                    "ImgTec";
                                case 4318 ->
                                    "NVIDIA";
                                case 5045 ->
                                    "ARM";
                                case 20803 ->
                                    "Qualcomm";
                                case 32902 ->
                                    "INTEL";
                                default ->
                                    "0x" + Integer.toHexString(vendorID);
                            };
                            var10000.info(var10001, "Choose device ID {}, vendor ID: {}, driver version: {}", var10003, var10004, switch(vendorID) {
                                case 4318 ->
                                    String.format("%d.%d.%d.%d", driverVersion >>> 22, driverVersion >>> 14 & 0xFF, driverVersion >> 6 & 0xFF, driverVersion & 63);
                                default ->
                                    "0x" + Integer.toHexString(vendorID);
                            });
                            flags = 1;
                            break label116;
                        }
                        ModernUI.LOGGER.info(ModernUI.MARKER, "Skip device ID {} because no suitable queue family was found", properties.deviceID());
                        vendorID = 0;
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
                    return (boolean) vendorID;
                }
                if (stack != null) {
                    stack.close();
                }
                return (boolean) flags;
            }
            if (stack != null) {
                stack.close();
            }
            return var14;
        }
        if (stack != null) {
            stack.close();
        }
        return var13;
    }

    @Nullable
    @SharedPtr
    public DirectContext createContext(@NonNull ContextOptions options) {
        VkBackendContext backendContext = new VkBackendContext();
        backendContext.mInstance = this.mInstance;
        backendContext.mPhysicalDevice = this.mPhysicalDevice;
        backendContext.mDevice = this.mDevice;
        backendContext.mGraphicsQueueIndex = this.mGraphicsQueueIndex;
        backendContext.mDeviceFeatures2 = this.mPhysicalDeviceFeatures2;
        return DirectContext.makeVulkan(backendContext, options);
    }

    public synchronized void close() {
        if (this.mDevice != null) {
            VKCore.vkDeviceWaitIdle(this.mDevice);
            VKCore.vkDestroyDevice(this.mDevice, null);
        }
        if (this.mInstance != null) {
            VKCore.vkDestroyInstance(this.mInstance, null);
        }
        this.mGraphicsQueueIndex = -1;
        this.mComputeQueueIndex = -1;
        this.mDevice = null;
        this.mPhysicalDevice = null;
        this.mInstance = null;
        this.mInstanceExtensions.clear();
        this.mInstanceExtensions.trim();
        this.mDeviceExtensions.clear();
        this.mDeviceExtensions.trim();
        if (this.mPhysicalDeviceFeatures2 != null) {
            freeFeaturesExtensionsStructs(this.mPhysicalDeviceFeatures2);
            this.mPhysicalDeviceFeatures2.free();
        }
        this.mPhysicalDeviceFeatures2 = null;
        ModernUI.LOGGER.info(ModernUI.MARKER, "Terminated VulkanManager");
    }

    public int getDriverVersion() {
        return this.mDriverVersion;
    }

    public static void freeFeaturesExtensionsStructs(@NonNull VkPhysicalDeviceFeatures2 features) {
        long pNext = features.pNext();
        while (pNext != 0L) {
            long current = pNext;
            pNext = VkPhysicalDeviceFeatures2.npNext(pNext);
            MemoryUtil.nmemFree(current);
        }
    }
}