package net.minecraft.data;

import com.google.common.base.Stopwatch;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import net.minecraft.WorldVersion;
import net.minecraft.server.Bootstrap;
import org.slf4j.Logger;

public class DataGenerator {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Path rootOutputFolder;

    private final PackOutput vanillaPackOutput;

    final Set<String> allProviderIds = new HashSet();

    final Map<String, DataProvider> providersToRun = new LinkedHashMap();

    private final WorldVersion version;

    private final boolean alwaysGenerate;

    public DataGenerator(Path path0, WorldVersion worldVersion1, boolean boolean2) {
        this.rootOutputFolder = path0;
        this.vanillaPackOutput = new PackOutput(this.rootOutputFolder);
        this.version = worldVersion1;
        this.alwaysGenerate = boolean2;
    }

    public void run() throws IOException {
        HashCache $$0 = new HashCache(this.rootOutputFolder, this.allProviderIds, this.version);
        Stopwatch $$1 = Stopwatch.createStarted();
        Stopwatch $$2 = Stopwatch.createUnstarted();
        this.providersToRun.forEach((p_254418_, p_253750_) -> {
            if (!this.alwaysGenerate && !$$0.shouldRunInThisVersion(p_254418_)) {
                LOGGER.debug("Generator {} already run for version {}", p_254418_, this.version.getName());
            } else {
                LOGGER.info("Starting provider: {}", p_254418_);
                $$2.start();
                $$0.applyUpdate((HashCache.UpdateResult) $$0.generateUpdate(p_254418_, p_253750_::m_213708_).join());
                $$2.stop();
                LOGGER.info("{} finished after {} ms", p_254418_, $$2.elapsed(TimeUnit.MILLISECONDS));
                $$2.reset();
            }
        });
        LOGGER.info("All providers took: {} ms", $$1.elapsed(TimeUnit.MILLISECONDS));
        $$0.purgeStaleAndWrite();
    }

    public DataGenerator.PackGenerator getVanillaPack(boolean boolean0) {
        return new DataGenerator.PackGenerator(boolean0, "vanilla", this.vanillaPackOutput);
    }

    public DataGenerator.PackGenerator getBuiltinDatapack(boolean boolean0, String string1) {
        Path $$2 = this.vanillaPackOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve("minecraft").resolve("datapacks").resolve(string1);
        return new DataGenerator.PackGenerator(boolean0, string1, new PackOutput($$2));
    }

    static {
        Bootstrap.bootStrap();
    }

    public class PackGenerator {

        private final boolean toRun;

        private final String providerPrefix;

        private final PackOutput output;

        PackGenerator(boolean boolean0, String string1, PackOutput packOutput2) {
            this.toRun = boolean0;
            this.providerPrefix = string1;
            this.output = packOutput2;
        }

        public <T extends DataProvider> T addProvider(DataProvider.Factory<T> dataProviderFactoryT0) {
            T $$1 = dataProviderFactoryT0.create(this.output);
            String $$2 = this.providerPrefix + "/" + $$1.getName();
            if (!DataGenerator.this.allProviderIds.add($$2)) {
                throw new IllegalStateException("Duplicate provider: " + $$2);
            } else {
                if (this.toRun) {
                    DataGenerator.this.providersToRun.put($$2, $$1);
                }
                return $$1;
            }
        }
    }
}