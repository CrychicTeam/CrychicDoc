package icyllis.modernui.mc.forge;

import icyllis.modernui.mc.MuiPlatform;
import java.nio.file.Path;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

public final class MuiPlatformForge extends MuiPlatform {

    public static final Path BOOTSTRAP_PATH = FMLPaths.getOrCreateGameRelativePath(FMLPaths.CONFIGDIR.get().resolve("ModernUI")).resolve("bootstrap.properties");

    @Override
    public Path getBootstrapPath() {
        return BOOTSTRAP_PATH;
    }

    @Override
    public boolean isClient() {
        return FMLEnvironment.dist.isClient();
    }
}