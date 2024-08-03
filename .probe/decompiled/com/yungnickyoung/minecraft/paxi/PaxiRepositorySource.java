package com.yungnickyoung.minecraft.paxi;

import com.google.common.collect.Lists;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.yungnickyoung.minecraft.paxi.mixin.accessor.FolderRepositorySourceAccessor;
import com.yungnickyoung.minecraft.yungsapi.io.JSON;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;

public class PaxiRepositorySource extends FolderRepositorySource {

    private static final FileFilter PACK_FILTER = file -> {
        boolean isValidPackZip = file.isFile() && file.getName().endsWith(".zip");
        boolean isValidPackFolder = file.isDirectory() && new File(file, "pack.mcmeta").isFile();
        return isValidPackZip || isValidPackFolder;
    };

    private File ordering;

    public List<String> orderedPaxiPacks = new ArrayList();

    public List<String> unorderedPaxiPacks = new ArrayList();

    public PaxiRepositorySource(Path packsFolder, PackType packType, File ordering) {
        super(packsFolder, packType, PaxiPackSource.PACK_SOURCE_PAXI);
        this.ordering = ordering;
    }

    @Override
    public void loadPacks(Consumer<Pack> packAdder) {
        File folder = ((FolderRepositorySourceAccessor) this).getFolder().toFile();
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        if (this.ordering != null && !this.ordering.isFile()) {
            PaxiRepositorySource.PackOrdering emptyPackOrdering = new PaxiRepositorySource.PackOrdering(new String[0]);
            try {
                JSON.createJsonFileFromObject(this.ordering.toPath(), emptyPackOrdering);
            } catch (IOException var10) {
                PaxiCommon.LOGGER.error("Unable to create default pack ordering file! This shouldn't happen.");
                PaxiCommon.LOGGER.error(var10.toString());
            }
        }
        Path[] packs = this.loadPacksFromFiles();
        for (Path packPath : packs) {
            String packName = packPath.getFileName().toString();
            Pack resourcePackProfile = Pack.readMetaAndCreate(packName, Component.literal(packName), true, this.createPackResourcesSupplier(packPath), ((FolderRepositorySourceAccessor) this).getPackType(), Pack.Position.TOP, PaxiPackSource.PACK_SOURCE_PAXI);
            if (resourcePackProfile != null) {
                packAdder.accept(resourcePackProfile);
            }
        }
    }

    private Path[] loadPacksFromFiles() {
        if (this.ordering != null) {
            PaxiRepositorySource.PackOrdering packOrdering = null;
            try {
                packOrdering = JSON.loadObjectFromJsonFile(this.ordering.toPath(), PaxiRepositorySource.PackOrdering.class);
            } catch (JsonIOException | JsonSyntaxException | IOException var6) {
                PaxiCommon.LOGGER.error("Error loading Paxi ordering JSON file {}: {}", this.ordering.getName(), var6.toString());
            }
            if (packOrdering == null) {
                PaxiCommon.LOGGER.error("Unable to load ordering JSON file {}! Is it proper JSON formatting? Ignoring load order...", this.ordering.getName());
                File[] files = ((FolderRepositorySourceAccessor) this).getFolder().toFile().listFiles(PACK_FILTER);
                return toPaths(files);
            } else if (packOrdering.getOrderedPackNames() == null) {
                PaxiCommon.LOGGER.error("Unable to find entry with name 'loadOrder' in load ordering JSON file {}! Ignoring load order...", this.ordering.getName());
                File[] files = ((FolderRepositorySourceAccessor) this).getFolder().toFile().listFiles(PACK_FILTER);
                return toPaths(files);
            } else {
                List<File> orderedPacks = this.filesFromNames(packOrdering.getOrderedPackNames(), PACK_FILTER);
                File[] allPacks = ((FolderRepositorySourceAccessor) this).getFolder().toFile().listFiles(PACK_FILTER);
                List<File> unorderedPacks = (List<File>) (allPacks == null ? Lists.newArrayList() : (List) Arrays.stream(allPacks).filter(file -> !orderedPacks.contains(file)).collect(Collectors.toList()));
                orderedPacks.forEach(file -> this.orderedPaxiPacks.add(file.getName()));
                unorderedPacks.forEach(file -> this.unorderedPaxiPacks.add(file.getName()));
                File[] files = (File[]) Stream.of(unorderedPacks, orderedPacks).flatMap(Collection::stream).toArray(File[]::new);
                return toPaths(files);
            }
        } else {
            File[] files = ((FolderRepositorySourceAccessor) this).getFolder().toFile().listFiles(PACK_FILTER);
            return toPaths(files);
        }
    }

    private List<File> filesFromNames(String[] packFileNames, FileFilter filter) {
        ArrayList<File> packFiles = new ArrayList();
        for (String fileName : packFileNames) {
            File packFile = new File(((FolderRepositorySourceAccessor) this).getFolder().toFile().toString(), fileName);
            if (!packFile.exists()) {
                PaxiCommon.LOGGER.error("Unable to find pack with name {} specified in load ordering JSON file {}! Skipping...", fileName, this.ordering.getName());
            } else if (filter == null || filter.accept(packFile)) {
                packFiles.add(packFile);
            }
        }
        return packFiles;
    }

    private Pack.ResourcesSupplier createPackResourcesSupplier(Path path) {
        return FolderRepositorySource.detectPackResources(path, false);
    }

    private static Path[] toPaths(File[] files) {
        return files == null ? new Path[0] : (Path[]) Arrays.stream(files).map(File::toPath).toArray(Path[]::new);
    }

    public boolean hasPacks() {
        return this.unorderedPaxiPacks.size() > 0 || this.orderedPaxiPacks.size() > 0;
    }

    private static class PackOrdering {

        @SerializedName("loadOrder")
        private String[] orderedPackNames;

        public PackOrdering(String[] orderedPackNames) {
            this.orderedPackNames = orderedPackNames;
        }

        public String[] getOrderedPackNames() {
            return this.orderedPackNames;
        }
    }
}