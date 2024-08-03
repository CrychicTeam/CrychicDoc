package fuzs.puzzleslib.impl.config.core;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ConfigFileTypeHandler;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ForgeModConfig extends ModConfig {

    public ForgeModConfig(Type type, IConfigSpec<?> spec, ModContainer container, String fileName) {
        super(type, spec, container, fileName);
    }

    public ConfigFileTypeHandler getHandler() {
        return ForgeConfigFileTypeHandler.TOML;
    }
}