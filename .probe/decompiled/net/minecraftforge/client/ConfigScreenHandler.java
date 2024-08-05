package net.minecraftforge.client;

import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

public class ConfigScreenHandler {

    public static Optional<BiFunction<Minecraft, Screen, Screen>> getScreenFactoryFor(IModInfo selectedMod) {
        return ModList.get().getModContainerById(selectedMod.getModId()).flatMap(mc -> mc.getCustomExtension(ConfigScreenHandler.ConfigScreenFactory.class).map(ConfigScreenHandler.ConfigScreenFactory::screenFunction));
    }

    public static record ConfigScreenFactory(BiFunction<Minecraft, Screen, Screen> screenFunction) implements IExtensionPoint<ConfigScreenHandler.ConfigScreenFactory> {
    }
}