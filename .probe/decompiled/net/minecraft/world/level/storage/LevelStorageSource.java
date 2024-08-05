package net.minecraft.world.level.storage;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.Nullable;
import net.minecraft.FileUtil;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.visitors.FieldSelector;
import net.minecraft.nbt.visitors.SkipFields;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.DirectoryLock;
import net.minecraft.util.MemoryReserve;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.validation.ContentValidationException;
import net.minecraft.world.level.validation.DirectoryValidator;
import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
import net.minecraft.world.level.validation.PathAllowList;
import org.slf4j.Logger;

public class LevelStorageSource {

    static final Logger LOGGER = LogUtils.getLogger();

    static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD).appendLiteral('-').appendValue(ChronoField.MONTH_OF_YEAR, 2).appendLiteral('-').appendValue(ChronoField.DAY_OF_MONTH, 2).appendLiteral('_').appendValue(ChronoField.HOUR_OF_DAY, 2).appendLiteral('-').appendValue(ChronoField.MINUTE_OF_HOUR, 2).appendLiteral('-').appendValue(ChronoField.SECOND_OF_MINUTE, 2).toFormatter();

    private static final ImmutableList<String> OLD_SETTINGS_KEYS = ImmutableList.of("RandomSeed", "generatorName", "generatorOptions", "generatorVersion", "legacy_custom_options", "MapFeatures", "BonusChest");

    private static final String TAG_DATA = "Data";

    private static final PathAllowList NO_SYMLINKS_ALLOWED = new PathAllowList(List.of());

    public static final String ALLOWED_SYMLINKS_CONFIG_NAME = "allowed_symlinks.txt";

    private final Path baseDir;

    private final Path backupDir;

    final DataFixer fixerUpper;

    private final DirectoryValidator worldDirValidator;

    public LevelStorageSource(Path path0, Path path1, DirectoryValidator directoryValidator2, DataFixer dataFixer3) {
        this.fixerUpper = dataFixer3;
        try {
            FileUtil.createDirectoriesSafe(path0);
        } catch (IOException var6) {
            throw new UncheckedIOException(var6);
        }
        this.baseDir = path0;
        this.backupDir = path1;
        this.worldDirValidator = directoryValidator2;
    }

    public static DirectoryValidator parseValidator(Path path0) {
        if (Files.exists(path0, new LinkOption[0])) {
            try {
                BufferedReader $$1 = Files.newBufferedReader(path0);
                DirectoryValidator var2;
                try {
                    var2 = new DirectoryValidator(PathAllowList.readPlain($$1));
                } catch (Throwable var5) {
                    if ($$1 != null) {
                        try {
                            $$1.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }
                    throw var5;
                }
                if ($$1 != null) {
                    $$1.close();
                }
                return var2;
            } catch (Exception var6) {
                LOGGER.error("Failed to parse {}, disallowing all symbolic links", "allowed_symlinks.txt", var6);
            }
        }
        return new DirectoryValidator(NO_SYMLINKS_ALLOWED);
    }

    public static LevelStorageSource createDefault(Path path0) {
        DirectoryValidator $$1 = parseValidator(path0.resolve("allowed_symlinks.txt"));
        return new LevelStorageSource(path0, path0.resolve("../backups"), $$1, DataFixers.getDataFixer());
    }

    private static <T> DataResult<WorldGenSettings> readWorldGenSettings(Dynamic<T> dynamicT0, DataFixer dataFixer1, int int2) {
        Dynamic<T> $$3 = dynamicT0.get("WorldGenSettings").orElseEmptyMap();
        UnmodifiableIterator $$6 = OLD_SETTINGS_KEYS.iterator();
        while ($$6.hasNext()) {
            String $$4 = (String) $$6.next();
            Optional<Dynamic<T>> $$5 = dynamicT0.get($$4).result();
            if ($$5.isPresent()) {
                $$3 = $$3.set($$4, (Dynamic) $$5.get());
            }
        }
        Dynamic<T> $$6x = DataFixTypes.WORLD_GEN_SETTINGS.updateToCurrentVersion(dataFixer1, $$3, int2);
        return WorldGenSettings.CODEC.parse($$6x);
    }

    private static WorldDataConfiguration readDataConfig(Dynamic<?> dynamic0) {
        return (WorldDataConfiguration) WorldDataConfiguration.CODEC.parse(dynamic0).resultOrPartial(LOGGER::error).orElse(WorldDataConfiguration.DEFAULT);
    }

    public String getName() {
        return "Anvil";
    }

    public LevelStorageSource.LevelCandidates findLevelCandidates() throws LevelStorageException {
        if (!Files.isDirectory(this.baseDir, new LinkOption[0])) {
            throw new LevelStorageException(Component.translatable("selectWorld.load_folder_access"));
        } else {
            try {
                Stream<Path> $$0 = Files.list(this.baseDir);
                LevelStorageSource.LevelCandidates var3;
                try {
                    List<LevelStorageSource.LevelDirectory> $$1 = $$0.filter(p_230839_ -> Files.isDirectory(p_230839_, new LinkOption[0])).map(LevelStorageSource.LevelDirectory::new).filter(p_230835_ -> Files.isRegularFile(p_230835_.dataFile(), new LinkOption[0]) || Files.isRegularFile(p_230835_.oldDataFile(), new LinkOption[0])).toList();
                    var3 = new LevelStorageSource.LevelCandidates($$1);
                } catch (Throwable var5) {
                    if ($$0 != null) {
                        try {
                            $$0.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }
                    throw var5;
                }
                if ($$0 != null) {
                    $$0.close();
                }
                return var3;
            } catch (IOException var6) {
                throw new LevelStorageException(Component.translatable("selectWorld.load_folder_access"));
            }
        }
    }

    public CompletableFuture<List<LevelSummary>> loadLevelSummaries(LevelStorageSource.LevelCandidates levelStorageSourceLevelCandidates0) {
        List<CompletableFuture<LevelSummary>> $$1 = new ArrayList(levelStorageSourceLevelCandidates0.levels.size());
        for (LevelStorageSource.LevelDirectory $$2 : levelStorageSourceLevelCandidates0.levels) {
            $$1.add(CompletableFuture.supplyAsync(() -> {
                boolean $$1x;
                try {
                    $$1x = DirectoryLock.isLocked($$2.path());
                } catch (Exception var6) {
                    LOGGER.warn("Failed to read {} lock", $$2.path(), var6);
                    return null;
                }
                try {
                    LevelSummary $$4 = this.readLevelData($$2, this.levelSummaryReader($$2, $$1x));
                    return $$4 != null ? $$4 : null;
                } catch (OutOfMemoryError var4x) {
                    MemoryReserve.release();
                    System.gc();
                    LOGGER.error(LogUtils.FATAL_MARKER, "Ran out of memory trying to read summary of {}", $$2.directoryName());
                    throw var4x;
                } catch (StackOverflowError var5) {
                    LOGGER.error(LogUtils.FATAL_MARKER, "Ran out of stack trying to read summary of {}. Assuming corruption; attempting to restore from from level.dat_old.", $$2.directoryName());
                    Util.safeReplaceOrMoveFile($$2.dataFile(), $$2.oldDataFile(), $$2.corruptedDataFile(LocalDateTime.now()), true);
                    throw var5;
                }
            }, Util.backgroundExecutor()));
        }
        return Util.sequenceFailFastAndCancel($$1).thenApply(p_230832_ -> p_230832_.stream().filter(Objects::nonNull).sorted().toList());
    }

    private int getStorageVersion() {
        return 19133;
    }

    @Nullable
    <T> T readLevelData(LevelStorageSource.LevelDirectory levelStorageSourceLevelDirectory0, BiFunction<Path, DataFixer, T> biFunctionPathDataFixerT1) {
        if (!Files.exists(levelStorageSourceLevelDirectory0.path(), new LinkOption[0])) {
            return null;
        } else {
            Path $$2 = levelStorageSourceLevelDirectory0.dataFile();
            if (Files.exists($$2, new LinkOption[0])) {
                T $$3 = (T) biFunctionPathDataFixerT1.apply($$2, this.fixerUpper);
                if ($$3 != null) {
                    return $$3;
                }
            }
            $$2 = levelStorageSourceLevelDirectory0.oldDataFile();
            return (T) (Files.exists($$2, new LinkOption[0]) ? biFunctionPathDataFixerT1.apply($$2, this.fixerUpper) : null);
        }
    }

    @Nullable
    private static WorldDataConfiguration getDataConfiguration(Path path0, DataFixer dataFixer1) {
        try {
            if (readLightweightData(path0) instanceof CompoundTag $$3) {
                CompoundTag $$4 = $$3.getCompound("Data");
                int $$5 = NbtUtils.getDataVersion($$4, -1);
                Dynamic<?> $$6 = DataFixTypes.LEVEL.updateToCurrentVersion(dataFixer1, new Dynamic(NbtOps.INSTANCE, $$4), $$5);
                return readDataConfig($$6);
            }
        } catch (Exception var7) {
            LOGGER.error("Exception reading {}", path0, var7);
        }
        return null;
    }

    static BiFunction<Path, DataFixer, Pair<WorldData, WorldDimensions.Complete>> getLevelData(DynamicOps<Tag> dynamicOpsTag0, WorldDataConfiguration worldDataConfiguration1, Registry<LevelStem> registryLevelStem2, Lifecycle lifecycle3) {
        return (p_265020_, p_265021_) -> {
            CompoundTag $$6;
            try {
                $$6 = NbtIo.readCompressed(p_265020_.toFile());
            } catch (IOException var17) {
                throw new UncheckedIOException(var17);
            }
            CompoundTag $$9 = $$6.getCompound("Data");
            CompoundTag $$10 = $$9.contains("Player", 10) ? $$9.getCompound("Player") : null;
            $$9.remove("Player");
            int $$11 = NbtUtils.getDataVersion($$9, -1);
            Dynamic<?> $$12 = DataFixTypes.LEVEL.updateToCurrentVersion(p_265021_, new Dynamic(dynamicOpsTag0, $$9), $$11);
            WorldGenSettings $$13 = (WorldGenSettings) readWorldGenSettings($$12, p_265021_, $$11).getOrThrow(false, Util.prefix("WorldGenSettings: ", LOGGER::error));
            LevelVersion $$14 = LevelVersion.parse($$12);
            LevelSettings $$15 = LevelSettings.parse($$12, worldDataConfiguration1);
            WorldDimensions.Complete $$16 = $$13.dimensions().bake(registryLevelStem2);
            Lifecycle $$17 = $$16.lifecycle().add(lifecycle3);
            PrimaryLevelData $$18 = PrimaryLevelData.parse($$12, p_265021_, $$11, $$10, $$15, $$14, $$16.specialWorldProperty(), $$13.options(), $$17);
            return Pair.of($$18, $$16);
        };
    }

    BiFunction<Path, DataFixer, LevelSummary> levelSummaryReader(LevelStorageSource.LevelDirectory levelStorageSourceLevelDirectory0, boolean boolean1) {
        return (p_289916_, p_289917_) -> {
            try {
                if (Files.isSymbolicLink(p_289916_)) {
                    List<ForbiddenSymlinkInfo> $$4 = new ArrayList();
                    this.worldDirValidator.validateSymlink(p_289916_, $$4);
                    if (!$$4.isEmpty()) {
                        ???;
                        return new LevelSummary.SymlinkLevelSummary(levelStorageSourceLevelDirectory0.directoryName(), levelStorageSourceLevelDirectory0.iconFile());
                    }
                }
                if (readLightweightData(p_289916_) instanceof CompoundTag $$6) {
                    CompoundTag $$7 = $$6.getCompound("Data");
                    int $$8 = NbtUtils.getDataVersion($$7, -1);
                    Dynamic<?> $$9 = DataFixTypes.LEVEL.updateToCurrentVersion(p_289917_, new Dynamic(NbtOps.INSTANCE, $$7), $$8);
                    LevelVersion $$10 = LevelVersion.parse($$9);
                    int $$11 = $$10.levelDataVersion();
                    if ($$11 == 19132 || $$11 == 19133) {
                        boolean $$12 = $$11 != this.getStorageVersion();
                        Path $$13 = levelStorageSourceLevelDirectory0.iconFile();
                        WorldDataConfiguration $$14 = readDataConfig($$9);
                        LevelSettings $$15 = LevelSettings.parse($$9, $$14);
                        FeatureFlagSet $$16 = parseFeatureFlagsFromSummary($$9);
                        boolean $$17 = FeatureFlags.isExperimental($$16);
                        return new LevelSummary($$15, $$10, levelStorageSourceLevelDirectory0.directoryName(), $$12, boolean1, $$17, $$13);
                    }
                } else {
                    LOGGER.warn("Invalid root tag in {}", p_289916_);
                }
                return null;
            } catch (Exception var18) {
                LOGGER.error("Exception reading {}", p_289916_, var18);
                return null;
            }
        };
    }

    private static FeatureFlagSet parseFeatureFlagsFromSummary(Dynamic<?> dynamic0) {
        Set<ResourceLocation> $$1 = (Set<ResourceLocation>) dynamic0.get("enabled_features").asStream().flatMap(p_248492_ -> p_248492_.asString().result().map(ResourceLocation::m_135820_).stream()).collect(Collectors.toSet());
        return FeatureFlags.REGISTRY.fromNames($$1, p_248503_ -> {
        });
    }

    @Nullable
    private static Tag readLightweightData(Path path0) throws IOException {
        SkipFields $$1 = new SkipFields(new FieldSelector("Data", CompoundTag.TYPE, "Player"), new FieldSelector("Data", CompoundTag.TYPE, "WorldGenSettings"));
        NbtIo.parseCompressed(path0.toFile(), $$1);
        return $$1.m_197713_();
    }

    public boolean isNewLevelIdAcceptable(String string0) {
        try {
            Path $$1 = this.getLevelPath(string0);
            Files.createDirectory($$1);
            Files.deleteIfExists($$1);
            return true;
        } catch (IOException var3) {
            return false;
        }
    }

    public boolean levelExists(String string0) {
        return Files.isDirectory(this.getLevelPath(string0), new LinkOption[0]);
    }

    private Path getLevelPath(String string0) {
        return this.baseDir.resolve(string0);
    }

    public Path getBaseDir() {
        return this.baseDir;
    }

    public Path getBackupPath() {
        return this.backupDir;
    }

    public LevelStorageSource.LevelStorageAccess validateAndCreateAccess(String string0) throws IOException, ContentValidationException {
        Path $$1 = this.getLevelPath(string0);
        List<ForbiddenSymlinkInfo> $$2 = this.worldDirValidator.validateSave($$1, true);
        if (!$$2.isEmpty()) {
            throw new ContentValidationException($$1, $$2);
        } else {
            return new LevelStorageSource.LevelStorageAccess(string0, $$1);
        }
    }

    public LevelStorageSource.LevelStorageAccess createAccess(String string0) throws IOException {
        Path $$1 = this.getLevelPath(string0);
        return new LevelStorageSource.LevelStorageAccess(string0, $$1);
    }

    public DirectoryValidator getWorldDirValidator() {
        return this.worldDirValidator;
    }

    public static record LevelCandidates(List<LevelStorageSource.LevelDirectory> f_230840_) implements Iterable<LevelStorageSource.LevelDirectory> {

        private final List<LevelStorageSource.LevelDirectory> levels;

        public LevelCandidates(List<LevelStorageSource.LevelDirectory> f_230840_) {
            this.levels = f_230840_;
        }

        public boolean isEmpty() {
            return this.levels.isEmpty();
        }

        public Iterator<LevelStorageSource.LevelDirectory> iterator() {
            return this.levels.iterator();
        }
    }

    public static record LevelDirectory(Path f_230850_) {

        private final Path path;

        public LevelDirectory(Path f_230850_) {
            this.path = f_230850_;
        }

        public String directoryName() {
            return this.path.getFileName().toString();
        }

        public Path dataFile() {
            return this.resourcePath(LevelResource.LEVEL_DATA_FILE);
        }

        public Path oldDataFile() {
            return this.resourcePath(LevelResource.OLD_LEVEL_DATA_FILE);
        }

        public Path corruptedDataFile(LocalDateTime p_230857_) {
            return this.path.resolve(LevelResource.LEVEL_DATA_FILE.getId() + "_corrupted_" + p_230857_.format(LevelStorageSource.FORMATTER));
        }

        public Path iconFile() {
            return this.resourcePath(LevelResource.ICON_FILE);
        }

        public Path lockFile() {
            return this.resourcePath(LevelResource.LOCK_FILE);
        }

        public Path resourcePath(LevelResource p_230855_) {
            return this.path.resolve(p_230855_.getId());
        }
    }

    public class LevelStorageAccess implements AutoCloseable {

        final DirectoryLock lock;

        final LevelStorageSource.LevelDirectory levelDirectory;

        private final String levelId;

        private final Map<LevelResource, Path> resources = Maps.newHashMap();

        LevelStorageAccess(String string0, Path path1) throws IOException {
            this.levelId = string0;
            this.levelDirectory = new LevelStorageSource.LevelDirectory(path1);
            this.lock = DirectoryLock.create(path1);
        }

        public String getLevelId() {
            return this.levelId;
        }

        public Path getLevelPath(LevelResource levelResource0) {
            return (Path) this.resources.computeIfAbsent(levelResource0, this.levelDirectory::m_230854_);
        }

        public Path getDimensionPath(ResourceKey<Level> resourceKeyLevel0) {
            return DimensionType.getStorageFolder(resourceKeyLevel0, this.levelDirectory.path());
        }

        private void checkLock() {
            if (!this.lock.isValid()) {
                throw new IllegalStateException("Lock is no longer valid");
            }
        }

        public PlayerDataStorage createPlayerStorage() {
            this.checkLock();
            return new PlayerDataStorage(this, LevelStorageSource.this.fixerUpper);
        }

        @Nullable
        public LevelSummary getSummary() {
            this.checkLock();
            return LevelStorageSource.this.readLevelData(this.levelDirectory, LevelStorageSource.this.levelSummaryReader(this.levelDirectory, false));
        }

        @Nullable
        public Pair<WorldData, WorldDimensions.Complete> getDataTag(DynamicOps<Tag> dynamicOpsTag0, WorldDataConfiguration worldDataConfiguration1, Registry<LevelStem> registryLevelStem2, Lifecycle lifecycle3) {
            this.checkLock();
            return LevelStorageSource.this.readLevelData(this.levelDirectory, LevelStorageSource.getLevelData(dynamicOpsTag0, worldDataConfiguration1, registryLevelStem2, lifecycle3));
        }

        @Nullable
        public WorldDataConfiguration getDataConfiguration() {
            this.checkLock();
            return LevelStorageSource.this.readLevelData(this.levelDirectory, LevelStorageSource::m_230828_);
        }

        public void saveDataTag(RegistryAccess registryAccess0, WorldData worldData1) {
            this.saveDataTag(registryAccess0, worldData1, null);
        }

        public void saveDataTag(RegistryAccess registryAccess0, WorldData worldData1, @Nullable CompoundTag compoundTag2) {
            File $$3 = this.levelDirectory.path().toFile();
            CompoundTag $$4 = worldData1.createTag(registryAccess0, compoundTag2);
            CompoundTag $$5 = new CompoundTag();
            $$5.put("Data", $$4);
            try {
                File $$6 = File.createTempFile("level", ".dat", $$3);
                NbtIo.writeCompressed($$5, $$6);
                File $$7 = this.levelDirectory.oldDataFile().toFile();
                File $$8 = this.levelDirectory.dataFile().toFile();
                Util.safeReplaceFile($$8, $$6, $$7);
            } catch (Exception var10) {
                LevelStorageSource.LOGGER.error("Failed to save level {}", $$3, var10);
            }
        }

        public Optional<Path> getIconFile() {
            return !this.lock.isValid() ? Optional.empty() : Optional.of(this.levelDirectory.iconFile());
        }

        public void deleteLevel() throws IOException {
            this.checkLock();
            final Path $$0 = this.levelDirectory.lockFile();
            LevelStorageSource.LOGGER.info("Deleting level {}", this.levelId);
            for (int $$1 = 1; $$1 <= 5; $$1++) {
                LevelStorageSource.LOGGER.info("Attempt {}...", $$1);
                try {
                    Files.walkFileTree(this.levelDirectory.path(), new SimpleFileVisitor<Path>() {

                        public FileVisitResult visitFile(Path p_78323_, BasicFileAttributes p_78324_) throws IOException {
                            if (!p_78323_.equals($$0)) {
                                LevelStorageSource.LOGGER.debug("Deleting {}", p_78323_);
                                Files.delete(p_78323_);
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        public FileVisitResult postVisitDirectory(Path p_78320_, @Nullable IOException p_78321_) throws IOException {
                            if (p_78321_ != null) {
                                throw p_78321_;
                            } else {
                                if (p_78320_.equals(LevelStorageAccess.this.levelDirectory.path())) {
                                    LevelStorageAccess.this.lock.close();
                                    Files.deleteIfExists($$0);
                                }
                                Files.delete(p_78320_);
                                return FileVisitResult.CONTINUE;
                            }
                        }
                    });
                    break;
                } catch (IOException var6) {
                    if ($$1 >= 5) {
                        throw var6;
                    }
                    LevelStorageSource.LOGGER.warn("Failed to delete {}", this.levelDirectory.path(), var6);
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException var5) {
                    }
                }
            }
        }

        public void renameLevel(String string0) throws IOException {
            this.checkLock();
            Path $$1 = this.levelDirectory.dataFile();
            if (Files.exists($$1, new LinkOption[0])) {
                CompoundTag $$2 = NbtIo.readCompressed($$1.toFile());
                CompoundTag $$3 = $$2.getCompound("Data");
                $$3.putString("LevelName", string0);
                NbtIo.writeCompressed($$2, $$1.toFile());
            }
        }

        public long makeWorldBackup() throws IOException {
            this.checkLock();
            String $$0 = LocalDateTime.now().format(LevelStorageSource.FORMATTER) + "_" + this.levelId;
            Path $$1 = LevelStorageSource.this.getBackupPath();
            try {
                FileUtil.createDirectoriesSafe($$1);
            } catch (IOException var9) {
                throw new RuntimeException(var9);
            }
            Path $$3 = $$1.resolve(FileUtil.findAvailableName($$1, $$0, ".zip"));
            final ZipOutputStream $$4 = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream($$3)));
            try {
                final Path $$5 = Paths.get(this.levelId);
                Files.walkFileTree(this.levelDirectory.path(), new SimpleFileVisitor<Path>() {

                    public FileVisitResult visitFile(Path p_78339_, BasicFileAttributes p_78340_) throws IOException {
                        if (p_78339_.endsWith("session.lock")) {
                            return FileVisitResult.CONTINUE;
                        } else {
                            String $$2 = $$5.resolve(LevelStorageAccess.this.levelDirectory.path().relativize(p_78339_)).toString().replace('\\', '/');
                            ZipEntry $$3 = new ZipEntry($$2);
                            $$4.putNextEntry($$3);
                            com.google.common.io.Files.asByteSource(p_78339_.toFile()).copyTo($$4);
                            $$4.closeEntry();
                            return FileVisitResult.CONTINUE;
                        }
                    }
                });
            } catch (Throwable var8) {
                try {
                    $$4.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
                throw var8;
            }
            $$4.close();
            return Files.size($$3);
        }

        public void close() throws IOException {
            this.lock.close();
        }
    }
}