package fuzs.puzzleslib.impl;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PuzzlesLib {

    public static final String MOD_ID = "puzzleslib";

    public static final String MOD_NAME = "Puzzles Lib";

    public static final Logger LOGGER = LoggerFactory.getLogger("Puzzles Lib");

    public static ResourceLocation id(String path) {
        return new ResourceLocation("puzzleslib", path);
    }
}