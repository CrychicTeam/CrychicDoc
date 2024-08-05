package icyllis.modernui.mc;

import java.nio.file.Path;
import java.util.ServiceLoader;

public abstract class MuiPlatform {

    static final MuiPlatform INSTANCE = (MuiPlatform) ServiceLoader.load(MuiPlatform.class).findFirst().orElseThrow();

    public static MuiPlatform get() {
        return INSTANCE;
    }

    public abstract Path getBootstrapPath();

    public abstract boolean isClient();
}