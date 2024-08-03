package fuzs.overflowingbars;

import fuzs.overflowingbars.config.ClientConfig;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverflowingBars implements ModConstructor {

    public static final String MOD_ID = "overflowingbars";

    public static final String MOD_NAME = "Overflowing Bars";

    public static final Logger LOGGER = LoggerFactory.getLogger("Overflowing Bars");

    public static final ConfigHolder CONFIG = ConfigHolder.builder("overflowingbars").client(ClientConfig.class);

    public static ResourceLocation id(String path) {
        return new ResourceLocation("overflowingbars", path);
    }
}