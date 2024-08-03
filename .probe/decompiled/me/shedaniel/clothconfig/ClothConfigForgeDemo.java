package me.shedaniel.clothconfig;

import me.shedaniel.clothconfig2.ClothConfigDemo;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class ClothConfigForgeDemo {

    public static void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> ClothConfigDemo.getConfigBuilderWithDemo().setParentScreen(parent).build()));
    }
}