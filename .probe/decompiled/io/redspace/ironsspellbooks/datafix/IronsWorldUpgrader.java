package io.redspace.ironsspellbooks.datafix;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.data.DataFixerStorage;
import io.redspace.ironsspellbooks.util.ByteHelper;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.storage.LevelStorageSource;

public class IronsWorldUpgrader {

    public int tempCount = 0;

    public static int IRONS_WORLD_DATA_VERSION = 2;

    final int REPORT_PROGRESS_MS = 5000;

    public static final byte[] INHABITED_TIME_MARKER = new byte[] { 73, 110, 104, 97, 98, 105, 116, 101, 100, 84, 105, 109, 101 };

    public static final String REGION_FOLDER = "region";

    public static final String ENTITY_FOLDER = "entities";

    private final LevelStorageSource.LevelStorageAccess levelStorage;

    private final DataFixer dataFixer;

    private int converted;

    private int skipped;

    private int fixes;

    private boolean running;

    private static final Pattern REGEX = Pattern.compile("^r\\.(-?[0-9]+)\\.(-?[0-9]+)\\.mca$");

    private Set<ResourceKey<Level>> levels = null;

    public IronsWorldUpgrader(LevelStorageSource.LevelStorageAccess pLevelStorage, LayeredRegistryAccess<RegistryLayer> registries) {
        this.levelStorage = pLevelStorage;
        try {
            this.levels = (Set<ResourceKey<Level>>) registries.compositeAccess().m_175515_(Registries.LEVEL_STEM).registryKeySet().stream().map(Registries::m_257551_).collect(Collectors.toUnmodifiableSet());
        } catch (Exception var4) {
            IronsSpellbooks.LOGGER.error("IronsWorldUpgrader. Failed to init levels. Cannot upgrade", var4);
        }
        this.dataFixer = new DataFixerBuilder(1).buildUnoptimized();
    }

    public boolean worldNeedsUpgrading() {
        return DataFixerStorage.INSTANCE.getDataVersion() < IRONS_WORLD_DATA_VERSION;
    }

    public void runUpgrade() {
        if (this.levels != null && this.worldNeedsUpgrading()) {
            IronsSpellbooks.LOGGER.info("IronsWorldUpgrader starting upgrade");
            long millis = 0L;
            IronsSpellbooks.LOGGER.info("IronsWorldUpgrader starting ENTITY_FOLDER");
            millis = Util.getMillis();
            this.doWork("entities", null, false, false);
            millis = Util.getMillis() - millis;
            IronsSpellbooks.LOGGER.info("IronsWorldUpgrader finished ENTITY_FOLDER after {} ms.  chunks updated:{} chunks skipped:{} tags fixed:{}", new Object[] { millis, this.converted, this.skipped, this.fixes });
            IronsSpellbooks.LOGGER.info("IronsWorldUpgrader starting REGION_FOLDER (this will take a few minutes on huge worlds..");
            millis = Util.getMillis();
            this.doWork("region", "block_entities", true, true);
            millis = Util.getMillis() - millis;
            IronsSpellbooks.LOGGER.info("IronsWorldUpgrader finished REGION_FOLDER after {} ms.  chunks updated:{} chunks skipped:{} tags fixed:{}", new Object[] { millis, this.converted, this.skipped, this.fixes });
            IronsSpellbooks.LOGGER.info("IronsWorldUpgrader starting fixDimensionStorage");
            millis = Util.getMillis();
            this.fixDimensionStorage();
            millis = Util.getMillis() - millis;
            IronsSpellbooks.LOGGER.info("IronsWorldUpgrader finished fixDimensionStorage after {} ms. tags fixed:{} ", millis, this.fixes);
            int previousVersion = DataFixerStorage.INSTANCE.getDataVersion();
            DataFixerStorage.INSTANCE.setDataVersion(IRONS_WORLD_DATA_VERSION);
            IronsSpellbooks.LOGGER.info("IronsWorldUpgrader V{} -> V{} completed", previousVersion, IRONS_WORLD_DATA_VERSION);
        }
    }

    private void fixDimensionStorage() {
        this.running = true;
        this.converted = 0;
        this.skipped = 0;
        this.fixes = 0;
        this.levels.stream().map(resourceKey -> this.levelStorage.getDimensionPath(resourceKey).resolve("data").toFile()).forEach(dir -> {
            File[] files = dir.listFiles();
            if (files != null) {
                Arrays.stream(files).toList().forEach(this::fixDimensionDataFile);
            }
        });
    }

    private void fixDimensionDataFile(File file) {
        File[] subFiles = file.listFiles();
        if (subFiles != null && subFiles.length > 0) {
            Arrays.stream(subFiles).forEach(this::fixDimensionDataFile);
        } else {
            try {
                CompoundTag compoundTag = NbtIo.readCompressed(file);
                IronsTagTraverser ironsTraverser = new IronsTagTraverser();
                ironsTraverser.visit(compoundTag);
                if (ironsTraverser.changesMade()) {
                    NbtIo.writeCompressed(compoundTag, file);
                }
                this.fixes = this.fixes + ironsTraverser.totalChanges();
            } catch (Exception var5) {
                IronsSpellbooks.LOGGER.error("IronsWorldUpgrader FixDimensionStorage error: {}", var5.getMessage());
            }
        }
    }

    private boolean preScanChunkUpdateNeeded(ChunkStorage chunkStorage, ChunkPos chunkPos) throws Exception {
        RegionFile regionFile = chunkStorage.worker.storage.getRegionFile(chunkPos);
        DataInputStream dataInputStream = regionFile.getChunkDataInputStream(chunkPos);
        try {
            DataInputStream var5 = dataInputStream;
            int markerPos;
            label67: {
                boolean var14;
                label68: {
                    boolean var9;
                    try {
                        if (dataInputStream == null) {
                            markerPos = 0;
                            break label67;
                        }
                        markerPos = ByteHelper.indexOf(dataInputStream, new ParallelMatcher(DataFixerHelpers.DATA_MATCHER_TARGETS));
                        if (markerPos == -1) {
                            var14 = true;
                            break label68;
                        }
                        long inhabitedTime = dataInputStream.readLong();
                        this.tempCount++;
                        var9 = inhabitedTime != 0L;
                    } catch (Throwable var11) {
                        if (dataInputStream != null) {
                            try {
                                var5.close();
                            } catch (Throwable var10) {
                                var11.addSuppressed(var10);
                            }
                        }
                        throw var11;
                    }
                    if (dataInputStream != null) {
                        dataInputStream.close();
                    }
                    return var9;
                }
                if (dataInputStream != null) {
                    dataInputStream.close();
                }
                return var14;
            }
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            return (boolean) markerPos;
        } catch (Exception var12) {
            return true;
        }
    }

    private void doWork(String regionFolder, String filterTag, boolean preScan, boolean checkInhabitedTime) {
        this.running = true;
        this.converted = 0;
        this.skipped = 0;
        this.fixes = 0;
        long nextProgressReportMS = System.currentTimeMillis() + 5000L;
        int totalChunks = 0;
        Builder<ResourceKey<Level>, ListIterator<ChunkPos>> builder = ImmutableMap.builder();
        for (ResourceKey<Level> resourcekey : this.levels) {
            List<ChunkPos> list = this.getAllChunkPos(resourcekey, regionFolder);
            builder.put(resourcekey, list.listIterator());
            totalChunks += list.size();
        }
        if (totalChunks > 0) {
            ImmutableMap<ResourceKey<Level>, ListIterator<ChunkPos>> immutablemap = builder.build();
            Builder<ResourceKey<Level>, ChunkStorage> builder1 = ImmutableMap.builder();
            for (ResourceKey<Level> resourcekey1 : this.levels) {
                Path path = this.levelStorage.getDimensionPath(resourcekey1);
                builder1.put(resourcekey1, new ChunkStorage(path.resolve(regionFolder), this.dataFixer, true));
            }
            ImmutableMap<ResourceKey<Level>, ChunkStorage> immutablemap1 = builder1.build();
            while (this.running) {
                boolean processedItem = false;
                for (ResourceKey<Level> resourcekey2 : this.levels) {
                    ListIterator<ChunkPos> listiterator = (ListIterator<ChunkPos>) immutablemap.get(resourcekey2);
                    ChunkStorage chunkstorage = (ChunkStorage) immutablemap1.get(resourcekey2);
                    if (listiterator.hasNext()) {
                        ChunkPos chunkpos = (ChunkPos) listiterator.next();
                        boolean updated = false;
                        try {
                            if (!preScan || this.preScanChunkUpdateNeeded(chunkstorage, chunkpos)) {
                                CompoundTag chunkDataTag = (CompoundTag) ((Optional) chunkstorage.read(chunkpos).join()).orElse(null);
                                if (chunkDataTag != null && (!checkInhabitedTime || chunkDataTag.getInt("InhabitedTime") != 0)) {
                                    ListTag blockEntitiesTag;
                                    if (filterTag != null) {
                                        blockEntitiesTag = (ListTag) chunkDataTag.get(filterTag);
                                    } else {
                                        blockEntitiesTag = new ListTag();
                                        blockEntitiesTag.add(chunkDataTag);
                                    }
                                    IronsTagTraverser ironsTagTraverser = new IronsTagTraverser();
                                    ironsTagTraverser.visit(blockEntitiesTag);
                                    if (ironsTagTraverser.changesMade()) {
                                        chunkstorage.write(chunkpos, chunkDataTag);
                                        this.fixes = this.fixes + ironsTagTraverser.totalChanges();
                                        updated = true;
                                    }
                                }
                            }
                        } catch (Exception var23) {
                            IronsSpellbooks.LOGGER.error("IronsWorldUpgrader: Error upgrading chunk {}, {}", chunkpos, var23.getMessage());
                        }
                        if (updated) {
                            this.converted++;
                        } else {
                            this.skipped++;
                        }
                        if (System.currentTimeMillis() > nextProgressReportMS) {
                            nextProgressReportMS = System.currentTimeMillis() + 5000L;
                            int chunksProcessed = this.converted + this.skipped;
                            IronsSpellbooks.LOGGER.info("IronsWorldUpgrader {} PROGRESS: {} of {} chunks complete ({}%)", new Object[] { regionFolder, chunksProcessed, totalChunks, String.format("%.2f", (float) chunksProcessed / (float) totalChunks * 100.0F) });
                        }
                        processedItem = true;
                    }
                }
                if (!processedItem) {
                    this.running = false;
                }
            }
            UnmodifiableIterator var29 = immutablemap1.values().iterator();
            while (var29.hasNext()) {
                ChunkStorage chunkstorage1 = (ChunkStorage) var29.next();
                try {
                    chunkstorage1.close();
                } catch (IOException var22) {
                    IronsSpellbooks.LOGGER.error("IronsWorldUpgrader: Error closing chunk storage: {}", var22.getMessage());
                }
            }
        }
    }

    private List<ChunkPos> getAllChunkPos(ResourceKey<Level> resourceKeyLevel0, String folder) {
        File file1 = this.levelStorage.getDimensionPath(resourceKeyLevel0).toFile();
        File file2 = new File(file1, folder);
        File[] afile = file2.listFiles((p_18822_, p_18823_) -> p_18823_.endsWith(".mca"));
        if (afile == null) {
            return ImmutableList.of();
        } else {
            List<ChunkPos> list = Lists.newArrayList();
            for (File file3 : afile) {
                Matcher matcher = REGEX.matcher(file3.getName());
                if (matcher.matches()) {
                    int i = Integer.parseInt(matcher.group(1)) << 5;
                    int j = Integer.parseInt(matcher.group(2)) << 5;
                    try (RegionFile regionfile = new RegionFile(file3.toPath(), file2.toPath(), true)) {
                        for (int k = 0; k < 32; k++) {
                            for (int l = 0; l < 32; l++) {
                                ChunkPos chunkpos = new ChunkPos(k + i, l + j);
                                if (regionfile.doesChunkExist(chunkpos)) {
                                    list.add(chunkpos);
                                }
                            }
                        }
                    } catch (Throwable var20) {
                    }
                }
            }
            return list;
        }
    }
}