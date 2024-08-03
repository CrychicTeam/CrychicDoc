package com.clientcrafting;

import java.util.Random;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("clientcrafting")
public class ClientCraftingMod {

    public static final String MOD_ID = "clientcrafting";

    public static final Logger LOGGER = LogManager.getLogger();

    public static Random rand = new Random();
}