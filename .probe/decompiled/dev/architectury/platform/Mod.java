package dev.architectury.platform;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public interface Mod {

    String getModId();

    String getVersion();

    String getName();

    String getDescription();

    Optional<String> getLogoFile(int var1);

    List<Path> getFilePaths();

    @Deprecated(forRemoval = true)
    Path getFilePath();

    Optional<Path> findResource(String... var1);

    Collection<String> getAuthors();

    @Nullable
    Collection<String> getLicense();

    Optional<String> getHomepage();

    Optional<String> getSources();

    Optional<String> getIssueTracker();

    @OnlyIn(Dist.CLIENT)
    void registerConfigurationScreen(Mod.ConfigurationScreenProvider var1);

    @FunctionalInterface
    @OnlyIn(Dist.CLIENT)
    public interface ConfigurationScreenProvider {

        Screen provide(Screen var1);
    }
}