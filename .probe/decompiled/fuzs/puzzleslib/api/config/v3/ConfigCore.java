package fuzs.puzzleslib.api.config.v3;

import net.minecraftforge.common.ForgeConfigSpec;

public interface ConfigCore {

    default void addToBuilder(ForgeConfigSpec.Builder builder, ValueCallback callback) {
    }

    default void afterConfigReload() {
    }
}