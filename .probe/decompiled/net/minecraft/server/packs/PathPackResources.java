package net.minecraft.server.packs;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.FileUtil;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.IoSupplier;
import org.slf4j.Logger;

public class PathPackResources extends AbstractPackResources {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Joiner PATH_JOINER = Joiner.on("/");

    private final Path root;

    public PathPackResources(String string0, Path path1, boolean boolean2) {
        super(string0, boolean2);
        this.root = path1;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... string0) {
        FileUtil.validatePath(string0);
        Path $$1 = FileUtil.resolvePath(this.root, List.of(string0));
        return Files.exists($$1, new LinkOption[0]) ? IoSupplier.create($$1) : null;
    }

    public static boolean validatePath(Path path0) {
        return true;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType packType0, ResourceLocation resourceLocation1) {
        Path $$2 = this.root.resolve(packType0.getDirectory()).resolve(resourceLocation1.getNamespace());
        return getResource(resourceLocation1, $$2);
    }

    public static IoSupplier<InputStream> getResource(ResourceLocation resourceLocation0, Path path1) {
        return (IoSupplier<InputStream>) FileUtil.decomposePath(resourceLocation0.getPath()).get().map(p_251647_ -> {
            Path $$2 = FileUtil.resolvePath(path1, p_251647_);
            return returnFileIfExists($$2);
        }, p_248714_ -> {
            LOGGER.error("Invalid path {}: {}", resourceLocation0, p_248714_.message());
            return null;
        });
    }

    @Nullable
    private static IoSupplier<InputStream> returnFileIfExists(Path path0) {
        return Files.exists(path0, new LinkOption[0]) && validatePath(path0) ? IoSupplier.create(path0) : null;
    }

    @Override
    public void listResources(PackType packType0, String string1, String string2, PackResources.ResourceOutput packResourcesResourceOutput3) {
        FileUtil.decomposePath(string2).get().ifLeft(p_250225_ -> {
            Path $$4 = this.root.resolve(packType0.getDirectory()).resolve(string1);
            listPath(string1, $$4, p_250225_, packResourcesResourceOutput3);
        }).ifRight(p_252338_ -> LOGGER.error("Invalid path {}: {}", string2, p_252338_.message()));
    }

    public static void listPath(String string0, Path path1, List<String> listString2, PackResources.ResourceOutput packResourcesResourceOutput3) {
        Path $$4 = FileUtil.resolvePath(path1, listString2);
        try {
            Stream<Path> $$5 = Files.find($$4, Integer.MAX_VALUE, (p_250060_, p_250796_) -> p_250796_.isRegularFile(), new FileVisitOption[0]);
            try {
                $$5.forEach(p_249092_ -> {
                    String $$4x = PATH_JOINER.join(path1.relativize(p_249092_));
                    ResourceLocation $$5x = ResourceLocation.tryBuild(string0, $$4x);
                    if ($$5x == null) {
                        Util.logAndPauseIfInIde(String.format(Locale.ROOT, "Invalid path in pack: %s:%s, ignoring", string0, $$4x));
                    } else {
                        packResourcesResourceOutput3.accept($$5x, IoSupplier.create(p_249092_));
                    }
                });
            } catch (Throwable var9) {
                if ($$5 != null) {
                    try {
                        $$5.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }
                throw var9;
            }
            if ($$5 != null) {
                $$5.close();
            }
        } catch (NoSuchFileException var10) {
        } catch (IOException var11) {
            LOGGER.error("Failed to list path {}", $$4, var11);
        }
    }

    @Override
    public Set<String> getNamespaces(PackType packType0) {
        Set<String> $$1 = Sets.newHashSet();
        Path $$2 = this.root.resolve(packType0.getDirectory());
        try {
            DirectoryStream<Path> $$3 = Files.newDirectoryStream($$2);
            try {
                for (Path $$4 : $$3) {
                    String $$5 = $$4.getFileName().toString();
                    if ($$5.equals($$5.toLowerCase(Locale.ROOT))) {
                        $$1.add($$5);
                    } else {
                        LOGGER.warn("Ignored non-lowercase namespace: {} in {}", $$5, this.root);
                    }
                }
            } catch (Throwable var9) {
                if ($$3 != null) {
                    try {
                        $$3.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }
                throw var9;
            }
            if ($$3 != null) {
                $$3.close();
            }
        } catch (NoSuchFileException var10) {
        } catch (IOException var11) {
            LOGGER.error("Failed to list path {}", $$2, var11);
        }
        return $$1;
    }

    @Override
    public void close() {
    }
}