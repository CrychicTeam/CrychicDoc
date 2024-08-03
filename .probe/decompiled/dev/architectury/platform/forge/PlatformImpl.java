package dev.architectury.platform.forge;

import dev.architectury.platform.Mod;
import dev.architectury.utils.Env;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlatformImpl {

    private static final Map<String, Mod> mods = new ConcurrentHashMap();

    public static Path getGameFolder() {
        return FMLPaths.GAMEDIR.get();
    }

    public static Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static Path getModsFolder() {
        return FMLPaths.MODSDIR.get();
    }

    public static Env getEnvironment() {
        return Env.fromPlatform(getEnv());
    }

    public static Dist getEnv() {
        return FMLEnvironment.dist;
    }

    public static boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }

    public static Mod getMod(String id) {
        return (Mod) mods.computeIfAbsent(id, PlatformImpl.ModImpl::new);
    }

    public static Collection<Mod> getMods() {
        for (IModInfo mod : ModList.get().getMods()) {
            getMod(mod.getModId());
        }
        return mods.values();
    }

    public static Collection<String> getModIds() {
        return (Collection<String>) ModList.get().getMods().stream().map(IModInfo::getModId).collect(Collectors.toList());
    }

    public static boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    private static class ModImpl implements Mod {

        private final ModContainer container;

        private final IModInfo info;

        public ModImpl(String id) {
            this.container = (ModContainer) ModList.get().getModContainerById(id).orElseThrow();
            this.info = (IModInfo) ModList.get().getMods().stream().filter(modInfo -> Objects.equals(modInfo.getModId(), id)).findAny().orElseThrow();
        }

        @NotNull
        @Override
        public String getModId() {
            return this.info.getModId();
        }

        @NotNull
        @Override
        public String getVersion() {
            return this.info.getVersion().toString();
        }

        @NotNull
        @Override
        public String getName() {
            return this.info.getDisplayName();
        }

        @NotNull
        @Override
        public String getDescription() {
            return this.info.getDescription();
        }

        @Override
        public Optional<String> getLogoFile(int i) {
            return this.info.getLogoFile();
        }

        @Override
        public List<Path> getFilePaths() {
            return List.of(this.getFilePath());
        }

        @Override
        public Path getFilePath() {
            return this.info.getOwningFile().getFile().getSecureJar().getRootPath();
        }

        @Override
        public Optional<Path> findResource(String... path) {
            return Optional.of(this.info.getOwningFile().getFile().findResource(path)).filter(x$0 -> Files.exists(x$0, new LinkOption[0]));
        }

        @Override
        public Collection<String> getAuthors() {
            Optional<String> optional = this.info.getConfig().getConfigElement(new String[] { "authors" }).map(String::valueOf);
            return (Collection<String>) (optional.isPresent() ? Collections.singleton((String) optional.get()) : Collections.emptyList());
        }

        @Nullable
        @Override
        public Collection<String> getLicense() {
            return Collections.singleton(this.info.getOwningFile().getLicense());
        }

        @Override
        public Optional<String> getHomepage() {
            return this.info.getConfig().getConfigElement(new String[] { "displayURL" }).map(String::valueOf);
        }

        @Override
        public Optional<String> getSources() {
            return Optional.empty();
        }

        @Override
        public Optional<String> getIssueTracker() {
            return this.info.getOwningFile() instanceof ModFileInfo info ? Optional.ofNullable(info.getIssueURL()).map(URL::toString) : Optional.empty();
        }

        @Override
        public void registerConfigurationScreen(Mod.ConfigurationScreenProvider configurationScreenProvider) {
            this.container.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> configurationScreenProvider.provide(screen)));
        }
    }
}