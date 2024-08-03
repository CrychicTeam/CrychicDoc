package com.yungnickyoung.minecraft.paxi;

import java.io.File;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PaxiCommon {

    public static final String MOD_ID = "paxi";

    public static File BASE_PACK_DIRECTORY;

    public static Path DATA_PACK_DIRECTORY;

    public static Path RESOURCE_PACK_DIRECTORY;

    public static File DATAPACK_ORDERING_FILE;

    public static File RESOURCEPACK_ORDERING_FILE;

    public static final Logger LOGGER = LogManager.getLogger("paxi");

    public static void init() {
    }
}