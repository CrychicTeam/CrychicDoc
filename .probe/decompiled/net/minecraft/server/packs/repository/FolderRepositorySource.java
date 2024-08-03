package net.minecraft.server.packs.repository;

import com.mojang.logging.LogUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.FileUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.linkfs.LinkFileSystem;
import org.slf4j.Logger;

public class FolderRepositorySource implements RepositorySource {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Path folder;

    private final PackType packType;

    private final PackSource packSource;

    public FolderRepositorySource(Path path0, PackType packType1, PackSource packSource2) {
        this.folder = path0;
        this.packType = packType1;
        this.packSource = packSource2;
    }

    private static String nameFromPath(Path path0) {
        return path0.getFileName().toString();
    }

    @Override
    public void loadPacks(Consumer<Pack> consumerPack0) {
        try {
            FileUtil.createDirectoriesSafe(this.folder);
            discoverPacks(this.folder, false, (p_248243_, p_248244_) -> {
                String $$3 = nameFromPath(p_248243_);
                Pack $$4 = Pack.readMetaAndCreate("file/" + $$3, Component.literal($$3), false, p_248244_, this.packType, Pack.Position.TOP, this.packSource);
                if ($$4 != null) {
                    consumerPack0.accept($$4);
                }
            });
        } catch (IOException var3) {
            LOGGER.warn("Failed to list packs in {}", this.folder, var3);
        }
    }

    public static void discoverPacks(Path path0, boolean boolean1, BiConsumer<Path, Pack.ResourcesSupplier> biConsumerPathPackResourcesSupplier2) throws IOException {
        DirectoryStream<Path> $$3 = Files.newDirectoryStream(path0);
        try {
            for (Path $$4 : $$3) {
                Pack.ResourcesSupplier $$5 = detectPackResources($$4, boolean1);
                if ($$5 != null) {
                    biConsumerPathPackResourcesSupplier2.accept($$4, $$5);
                }
            }
        } catch (Throwable var8) {
            if ($$3 != null) {
                try {
                    $$3.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }
            throw var8;
        }
        if ($$3 != null) {
            $$3.close();
        }
    }

    @Nullable
    public static Pack.ResourcesSupplier detectPackResources(Path path0, boolean boolean1) {
        BasicFileAttributes $$2;
        try {
            $$2 = Files.readAttributes(path0, BasicFileAttributes.class);
        } catch (NoSuchFileException var5) {
            return null;
        } catch (IOException var6) {
            LOGGER.warn("Failed to read properties of '{}', ignoring", path0, var6);
            return null;
        }
        if ($$2.isDirectory() && Files.isRegularFile(path0.resolve("pack.mcmeta"), new LinkOption[0])) {
            return p_255538_ -> new PathPackResources(p_255538_, path0, boolean1);
        } else {
            if ($$2.isRegularFile() && path0.getFileName().toString().endsWith(".zip")) {
                FileSystem $$6 = path0.getFileSystem();
                if ($$6 == FileSystems.getDefault() || $$6 instanceof LinkFileSystem) {
                    File $$7 = path0.toFile();
                    return p_255541_ -> new FilePackResources(p_255541_, $$7, boolean1);
                }
            }
            LOGGER.info("Found non-pack entry '{}', ignoring", path0);
            return null;
        }
    }
}