package icyllis.arc3d.vulkan;

import org.lwjgl.system.NativeType;
import org.lwjgl.vulkan.VK11;

public final class VKCore extends VK11 {

    public static void _CHECK_(@NativeType("VkResult") int vkResult) {
        if (vkResult != 0) {
            throw new AssertionError(getResultMessage(vkResult));
        }
    }

    public static void _CHECK_ERROR_(@NativeType("VkResult") int vkResult) {
        if (vkResult < 0) {
            throw new AssertionError(getResultMessage(vkResult));
        }
    }

    public static String getResultMessage(int result) {
        return switch(result) {
            case -1000011001 ->
                "A validation layer found an error.";
            case -1000003001 ->
                "The display used by a swap-chain does not use the same presentable image layout, or is incompatible in a way that prevents sharing an image.";
            case -1000001004 ->
                "A surface has changed in such a way that it is no longer compatible with the swap-chain, and further presentation requests using the swap-chain will fail. Applications must query the new surface properties and recreate their swap-chain if they wishto continue presenting to the surface.";
            case -1000000001 ->
                "The requested window is already connected to a VkSurfaceKHR, or to some other non-Vulkan API.";
            case -1000000000 ->
                "A surface is no longer available.";
            case -11 ->
                "A requested format is not supported on this device.";
            case -10 ->
                "Too many objects of the type have already been created.";
            case -9 ->
                "The requested version of Vulkan is not supported by the driver or is otherwise incompatible for implementation-specific reasons.";
            case -8 ->
                "A requested feature is not supported.";
            case -7 ->
                "A requested extension is not supported.";
            case -6 ->
                "A requested layer is not present or could not be loaded.";
            case -5 ->
                "Mapping of a memory object has failed.";
            case -4 ->
                "The logical or physical device has been lost.";
            case -3 ->
                "Initialization of an object could not be completed for implementation-specific reasons.";
            case -2 ->
                "A device memory allocation has failed.";
            case -1 ->
                "A host memory allocation has failed.";
            case 0 ->
                "Command successfully completed.";
            case 1 ->
                "A fence or query has not yet completed.";
            case 2 ->
                "A wait operation has not completed in the specified time.";
            case 3 ->
                "An event is signaled.";
            case 4 ->
                "An event is unsignaled.";
            case 5 ->
                "A return array was too small for the result.";
            case 1000001003 ->
                "A swap-chain no longer matches the surface properties exactly, but can still be used to present to the surface successfully.";
            default ->
                String.format("%s [%d]", "Unknown", result);
        };
    }

    public static String getPhysicalDeviceTypeName(@NativeType("VkPhysicalDeviceType") int vkPhysicalDeviceType) {
        return switch(vkPhysicalDeviceType) {
            case 1 ->
                "Integrated GPU";
            case 2 ->
                "Discrete GPU";
            case 3 ->
                "Virtual GPU";
            case 4 ->
                "CPU";
            default ->
                "Other";
        };
    }

    public static int vkFormatChannels(@NativeType("VkFormat") int vkFormat) {
        return switch(vkFormat) {
            case 2, 3, 37, 43, 44, 58, 64, 91, 97, 133 ->
                15;
            case 4, 23, 131, 147 ->
                7;
            case 9, 70, 76 ->
                1;
            case 16, 77, 83 ->
                3;
            default ->
                0;
        };
    }

    public static int vkFormatCompressionType(@NativeType("VkFormat") int vkFormat) {
        return switch(vkFormat) {
            case 131 ->
                2;
            case 133 ->
                3;
            case 147 ->
                1;
            default ->
                0;
        };
    }

    public static int vkFormatBytesPerBlock(@NativeType("VkFormat") int vkFormat) {
        return switch(vkFormat) {
            case 2, 3, 4, 16, 70, 76 ->
                2;
            case 9, 127 ->
                1;
            case 23, 1000156002, 1000156003 ->
                3;
            case 37, 43, 44, 58, 64, 77, 83, 129 ->
                4;
            case 91, 97, 130, 131, 133, 147 ->
                8;
            default ->
                0;
        };
    }

    public static int vkFormatStencilBits(@NativeType("VkFormat") int vkFormat) {
        return switch(vkFormat) {
            case 127, 128, 129, 130 ->
                8;
            default ->
                0;
        };
    }

    public static String vkFormatName(@NativeType("VkFormat") int vkFormat) {
        return switch(vkFormat) {
            case 2 ->
                "R4G4B4A4_UNORM_PACK16";
            case 3 ->
                "B4G4R4A4_UNORM_PACK16";
            case 4 ->
                "R5G6B5_UNORM_PACK16";
            case 9 ->
                "R8_UNORM";
            case 16 ->
                "R8G8_UNORM";
            case 23 ->
                "R8G8B8_UNORM";
            case 37 ->
                "R8G8B8A8_UNORM";
            case 43 ->
                "R8G8B8A8_SRGB";
            case 44 ->
                "B8G8R8A8_UNORM";
            case 58 ->
                "A2R10G10B10_UNORM_PACK32";
            case 64 ->
                "A2B10G10R10_UNORM_PACK32";
            case 70 ->
                "R16_UNORM";
            case 76 ->
                "R16_SFLOAT";
            case 77 ->
                "R16G16_UNORM";
            case 83 ->
                "R16G16_SFLOAT";
            case 91 ->
                "R16G16B16A16_UNORM";
            case 97 ->
                "R16G16B16A16_SFLOAT";
            case 109 ->
                "R32G32B32A32_SFLOAT";
            case 127 ->
                "S8_UINT";
            case 129 ->
                "D24_UNORM_S8_UINT";
            case 130 ->
                "D32_SFLOAT_S8_UINT";
            case 131 ->
                "BC1_RGB_UNORM_BLOCK";
            case 133 ->
                "BC1_RGBA_UNORM_BLOCK";
            case 147 ->
                "ETC2_R8G8B8_UNORM_BLOCK";
            default ->
                "Unknown";
        };
    }
}