package net.raphimc.immediatelyfast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.raphimc.immediatelyfast.apiimpl.ApiAccessImpl;
import net.raphimc.immediatelyfast.compat.IrisCompat;
import net.raphimc.immediatelyfast.feature.core.ImmediatelyFastConfig;
import net.raphimc.immediatelyfast.feature.core.ImmediatelyFastRuntimeConfig;
import net.raphimc.immediatelyfast.feature.fast_buffer_upload.PersistentMappedStreamingBuffer;
import net.raphimc.immediatelyfast.feature.sign_text_buffering.SignTextCache;
import net.raphimc.immediatelyfastapi.ImmediatelyFastApi;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GLCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

public class ImmediatelyFast {

    public static final Logger LOGGER = LoggerFactory.getLogger("ImmediatelyFast");

    public static final Unsafe UNSAFE = getUnsafe();

    public static String VERSION;

    public static ImmediatelyFastConfig config;

    public static ImmediatelyFastRuntimeConfig runtimeConfig;

    public static PersistentMappedStreamingBuffer persistentMappedStreamingBuffer;

    public static SignTextCache signTextCache;

    public static void earlyInit() {
        if (config == null) {
            loadConfig();
            if (!config.debug_only_and_not_recommended_disable_mod_conflict_handling && config.hud_batching && PlatformCode.getModVersion("slight-gui-modifications").isPresent()) {
                LOGGER.warn("Slight GUI Modifications detected. Force disabling HUD Batching optimization.");
                config.hud_batching = false;
            }
            createRuntimeConfig();
            ImmediatelyFastApi.setApiImpl(new ApiAccessImpl());
            VERSION = (String) PlatformCode.getModVersion("immediatelyfast").orElseThrow(NullPointerException::new);
            PlatformCode.checkModCompatibility();
        }
    }

    public static void windowInit() {
        GLCapabilities cap = GL.getCapabilities();
        String gpuVendor = GL11C.glGetString(7936);
        String gpuModel = GL11C.glGetString(7937);
        String glVersion = GL11C.glGetString(7938);
        LOGGER.info("Initializing ImmediatelyFast " + VERSION + " on " + gpuModel + " (" + gpuVendor + ") with OpenGL " + glVersion);
        boolean isNvidia = false;
        boolean isAmd = false;
        boolean isIntel = false;
        if (gpuVendor != null) {
            String gpuVendorLower = gpuVendor.toLowerCase();
            isNvidia = gpuVendorLower.startsWith("nvidia");
            isAmd = gpuVendorLower.startsWith("ati") || gpuVendorLower.startsWith("amd");
            isIntel = gpuVendorLower.startsWith("intel");
        }
        Objects.requireNonNull(config, "Config not loaded yet");
        Objects.requireNonNull(runtimeConfig, "Runtime config not created yet");
        if (config.fast_buffer_upload) {
            boolean supportsCaps = cap.GL_ARB_direct_state_access && cap.GL_ARB_buffer_storage && cap.glMemoryBarrier != 0L;
            boolean supportedGpu = !isIntel || config.debug_only_and_not_recommended_disable_hardware_conflict_handling;
            boolean requiresCoherentBufferMapping = isAmd && !config.debug_only_and_not_recommended_disable_hardware_conflict_handling;
            boolean supportsLegacyFastBufferUpload = isNvidia || config.debug_only_and_not_recommended_disable_hardware_conflict_handling;
            if (supportsCaps && supportedGpu) {
                if (requiresCoherentBufferMapping) {
                    LOGGER.info("AMD GPU detected. Enabling coherent buffer mapping");
                    config.fast_buffer_upload_explicit_flush = false;
                }
                persistentMappedStreamingBuffer = new PersistentMappedStreamingBuffer(config.fast_buffer_upload_size_mb * 1024L * 1024L);
            } else {
                runtimeConfig.fast_buffer_upload = false;
                if (supportsLegacyFastBufferUpload) {
                    runtimeConfig.legacy_fast_buffer_upload = true;
                    LOGGER.info("Using legacy fast buffer upload optimization");
                } else {
                    LOGGER.warn("Force disabling fast buffer upload optimization due to unsupported GPU");
                }
            }
        }
        if (!config.debug_only_and_not_recommended_disable_mod_conflict_handling) {
            PlatformCode.getModVersion("iris").or(() -> PlatformCode.getModVersion("oculus")).ifPresent(version -> {
                LOGGER.info("Found Iris/Oculus " + version + ". Enabling compatibility.");
                IrisCompat.init();
            });
        }
    }

    public static void lateInit() {
        if (config.experimental_sign_text_buffering) {
            signTextCache = new SignTextCache();
            ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(signTextCache);
        }
    }

    public static void onWorldJoin() {
        if (signTextCache != null) {
            signTextCache.clearCache();
        }
    }

    public static void loadConfig() {
        File configFile = PlatformCode.getConfigDirectory().resolve("immediatelyfast.json").toFile();
        if (configFile.exists()) {
            try {
                config = (ImmediatelyFastConfig) new Gson().fromJson(new FileReader(configFile), ImmediatelyFastConfig.class);
            } catch (Throwable var3) {
                LOGGER.error("Failed to load ImmediatelyFast config. Resetting it.", var3);
            }
        }
        if (config == null) {
            config = new ImmediatelyFastConfig();
        }
        try {
            Files.writeString(configFile.toPath(), new GsonBuilder().setPrettyPrinting().create().toJson(config));
        } catch (Throwable var2) {
            LOGGER.error("Failed to save ImmediatelyFast config.", var2);
        }
    }

    public static void createRuntimeConfig() {
        runtimeConfig = new ImmediatelyFastRuntimeConfig(config);
    }

    private static Unsafe getUnsafe() {
        try {
            for (Field field : Unsafe.class.getDeclaredFields()) {
                if (field.getType().equals(Unsafe.class)) {
                    field.setAccessible(true);
                    return (Unsafe) field.get(null);
                }
            }
        } catch (Throwable var4) {
        }
        throw new IllegalStateException("Unable to get Unsafe instance");
    }
}