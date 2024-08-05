package net.minecraftforge.data.event;

import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.event.IModBusEvent;

public class GatherDataEvent extends Event implements IModBusEvent {

    private final DataGenerator dataGenerator;

    private final GatherDataEvent.DataGeneratorConfig config;

    private final ExistingFileHelper existingFileHelper;

    private final ModContainer modContainer;

    public GatherDataEvent(ModContainer mc, DataGenerator dataGenerator, GatherDataEvent.DataGeneratorConfig dataGeneratorConfig, ExistingFileHelper existingFileHelper) {
        this.modContainer = mc;
        this.dataGenerator = dataGenerator;
        this.config = dataGeneratorConfig;
        this.existingFileHelper = existingFileHelper;
    }

    public ModContainer getModContainer() {
        return this.modContainer;
    }

    public Collection<Path> getInputs() {
        return this.config.getInputs();
    }

    public DataGenerator getGenerator() {
        return this.dataGenerator;
    }

    public ExistingFileHelper getExistingFileHelper() {
        return this.existingFileHelper;
    }

    public CompletableFuture<HolderLookup.Provider> getLookupProvider() {
        return this.config.lookupProvider;
    }

    public boolean includeServer() {
        return this.config.server;
    }

    public boolean includeClient() {
        return this.config.client;
    }

    public boolean includeDev() {
        return this.config.dev;
    }

    public boolean includeReports() {
        return this.config.reports;
    }

    public boolean validate() {
        return this.config.validate;
    }

    public static class DataGeneratorConfig {

        private final Set<String> mods;

        private final Path path;

        private final Collection<Path> inputs;

        private final CompletableFuture<HolderLookup.Provider> lookupProvider;

        private final boolean server;

        private final boolean client;

        private final boolean dev;

        private final boolean reports;

        private final boolean validate;

        private final boolean flat;

        private final List<DataGenerator> generators = new ArrayList();

        public DataGeneratorConfig(Set<String> mods, Path path, Collection<Path> inputs, CompletableFuture<HolderLookup.Provider> lookupProvider, boolean server, boolean client, boolean dev, boolean reports, boolean validate, boolean flat) {
            this.mods = mods;
            this.path = path;
            this.inputs = inputs;
            this.lookupProvider = lookupProvider;
            this.server = server;
            this.client = client;
            this.dev = dev;
            this.reports = reports;
            this.validate = validate;
            this.flat = flat;
        }

        public Collection<Path> getInputs() {
            return this.inputs;
        }

        public Set<String> getMods() {
            return this.mods;
        }

        public boolean isFlat() {
            return this.flat || this.getMods().size() == 1;
        }

        public DataGenerator makeGenerator(Function<Path, Path> pathEnhancer, boolean shouldExecute) {
            DataGenerator generator = new DataGenerator((Path) pathEnhancer.apply(this.path), DetectedVersion.tryDetectVersion(), shouldExecute);
            if (shouldExecute) {
                this.generators.add(generator);
            }
            return generator;
        }

        public void runAll() {
            Map<Path, List<DataGenerator>> paths = (Map<Path, List<DataGenerator>>) this.generators.stream().collect(Collectors.groupingBy(gen -> gen.getPackOutput().getOutputFolder(), LinkedHashMap::new, Collectors.toList()));
            paths.values().forEach(LamdbaExceptionUtils.rethrowConsumer(lst -> {
                DataGenerator parent = (DataGenerator) lst.get(0);
                for (int x = 1; x < lst.size(); x++) {
                    ((DataGenerator) lst.get(x)).getProvidersView().forEach((name, provider) -> parent.addProvider(true, provider));
                }
                parent.run();
            }));
        }
    }
}