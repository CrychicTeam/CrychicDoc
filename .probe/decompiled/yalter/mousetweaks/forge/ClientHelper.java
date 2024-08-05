package yalter.mousetweaks.forge;

import net.minecraftforge.client.ConfigScreenHandler;
import yalter.mousetweaks.ConfigScreen;

public class ClientHelper {

    public static ConfigScreenHandler.ConfigScreenFactory createConfigScreenFactory() {
        return new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ConfigScreen(screen));
    }
}