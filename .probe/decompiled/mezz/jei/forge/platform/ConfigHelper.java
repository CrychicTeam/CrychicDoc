package mezz.jei.forge.platform;

import java.nio.file.Path;
import java.util.Optional;
import mezz.jei.common.platform.IPlatformConfigHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

public class ConfigHelper implements IPlatformConfigHelper {

    @Override
    public Path getModConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public Optional<Screen> getConfigScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        return ModList.get().getModContainerById("jei").map(ModContainer::getModInfo).flatMap(ConfigScreenHandler::getScreenFactoryFor).map(f -> (Screen) f.apply(minecraft, minecraft.screen));
    }
}