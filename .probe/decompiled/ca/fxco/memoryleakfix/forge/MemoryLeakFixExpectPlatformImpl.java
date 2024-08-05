package ca.fxco.memoryleakfix.forge;

import java.lang.reflect.Field;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public class MemoryLeakFixExpectPlatformImpl {

    private static final ArtifactVersion MCVERSION;

    public static boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }

    public static int compareMinecraftToVersion(String version) {
        return MCVERSION.compareTo(new DefaultArtifactVersion(version));
    }

    public static String getMappingType() {
        return MCVERSION.compareTo(new DefaultArtifactVersion("1.16.5")) > 0 ? "forge" : "mcp";
    }

    public static boolean isDevEnvironment() {
        return !FMLLoader.isProduction();
    }

    static {
        String mcVersion;
        try {
            Object versionInfo = FMLLoader.class.getMethod("versionInfo").invoke(null);
            mcVersion = (String) versionInfo.getClass().getMethod("mcVersion").invoke(versionInfo);
        } catch (Exception var4) {
            try {
                Field field = FMLLoader.class.getDeclaredField("mcVersion");
                field.setAccessible(true);
                mcVersion = (String) field.get(null);
            } catch (Exception var3) {
                throw new RuntimeException("[MemoryLeakFix] Reflection failed at getting the Minecraft version from Forge", var3);
            }
        }
        MCVERSION = new DefaultArtifactVersion(mcVersion);
    }
}