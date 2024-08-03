package de.cristelknight999.wwoo_forge;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("wwoo_forge")
public class wwoo_forge {

    public static final String MODID = "wwoo_forge";

    public static final Logger LOGGER = LogManager.getLogger();

    public wwoo_forge() {
        LOGGER.info("Enabling William Wythers' Overhauled Overworld [FORGE]");
    }
}