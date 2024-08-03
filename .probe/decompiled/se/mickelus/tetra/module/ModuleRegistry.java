package se.mickelus.tetra.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.data.MaterialVariantData;
import se.mickelus.tetra.module.data.ModuleData;
import se.mickelus.tetra.module.data.VariantData;

@ParametersAreNonnullByDefault
public class ModuleRegistry {

    private static final Logger logger = LogManager.getLogger();

    public static ModuleRegistry instance;

    private final Map<ResourceLocation, BiFunction<ResourceLocation, ModuleData, ItemModule>> moduleConstructors;

    private Map<ResourceLocation, ItemModule> moduleMap;

    public ModuleRegistry() {
        instance = this;
        this.moduleConstructors = new HashMap();
        this.moduleMap = Collections.emptyMap();
        DataManager.instance.moduleData.onReload(() -> this.setupModules(DataManager.instance.moduleData.getData()));
    }

    private void setupModules(Map<ResourceLocation, ModuleData> data) {
        this.moduleMap = (Map<ResourceLocation, ItemModule>) data.entrySet().stream().filter(entry -> this.validateModuleData((ResourceLocation) entry.getKey(), (ModuleData) entry.getValue())).flatMap(entry -> this.expandEntry(entry).stream()).collect(Collectors.toMap(Entry::getKey, entry -> this.setupModule((ResourceLocation) entry.getKey(), (ModuleData) entry.getValue())));
    }

    private boolean validateModuleData(ResourceLocation identifier, ModuleData data) {
        if (data == null) {
            logger.warn("Failed to create module from module data '{}': Data is null (probably due to it failing to parse)", identifier);
            return false;
        } else if (!this.moduleConstructors.containsKey(data.type)) {
            logger.warn("Failed to create module from module data '{}': Unknown type '{}'", identifier, data.type);
            return false;
        } else if (data.slots != null && data.slots.length >= 1) {
            return true;
        } else {
            logger.warn("Failed to create module from module data '{}': Slots field is empty", identifier);
            return false;
        }
    }

    private Collection<Pair<ResourceLocation, ModuleData>> expandEntry(Entry<ResourceLocation, ModuleData> entry) {
        ModuleData moduleData = (ModuleData) entry.getValue();
        if (moduleData.slotSuffixes.length <= 0) {
            return Collections.singletonList(new ImmutablePair((ResourceLocation) entry.getKey(), (ModuleData) entry.getValue()));
        } else {
            ArrayList<Pair<ResourceLocation, ModuleData>> result = new ArrayList(moduleData.slots.length);
            for (int i = 0; i < moduleData.slots.length; i++) {
                ModuleData dataCopy = moduleData.shallowCopy();
                dataCopy.slots = new String[] { moduleData.slots[i] };
                dataCopy.slotSuffixes = new String[] { moduleData.slotSuffixes[i] };
                ResourceLocation suffixedIdentifier = new ResourceLocation(((ResourceLocation) entry.getKey()).getNamespace(), ((ResourceLocation) entry.getKey()).getPath() + moduleData.slotSuffixes[i]);
                result.add(new ImmutablePair(suffixedIdentifier, dataCopy));
            }
            return result;
        }
    }

    private void expandMaterialVariants(ModuleData moduleData) {
        moduleData.variants = (VariantData[]) Arrays.stream(moduleData.variants).flatMap(variant -> variant instanceof MaterialVariantData ? this.expandMaterialVariant((MaterialVariantData) variant) : Stream.of(variant)).toArray(VariantData[]::new);
    }

    private Stream<VariantData> expandMaterialVariant(MaterialVariantData source) {
        return Arrays.stream(source.materials).map(rl -> rl.getPath().endsWith("/") ? DataManager.instance.materialData.getDataIn(rl) : (Collection) Optional.ofNullable(DataManager.instance.materialData.getData(rl)).map(Collections::singletonList).orElseGet(Collections::emptyList)).flatMap(Collection::stream).map(source::combine);
    }

    private void handleVariantDuplicates(ModuleData data) {
        data.variants = (VariantData[]) ((Map) Arrays.stream(data.variants).collect(Collectors.toMap(variant -> variant.key, Function.identity(), VariantData::merge))).values().toArray(new VariantData[0]);
    }

    private ItemModule setupModule(ResourceLocation identifier, ModuleData data) {
        this.expandMaterialVariants(data);
        this.handleVariantDuplicates(data);
        return (ItemModule) ((BiFunction) this.moduleConstructors.get(data.type)).apply(identifier, data);
    }

    public void registerModuleType(ResourceLocation identifier, BiFunction<ResourceLocation, ModuleData, ItemModule> constructor) {
        this.moduleConstructors.put(identifier, constructor);
    }

    public ItemModule getModule(ResourceLocation identifier) {
        return (ItemModule) this.moduleMap.get(identifier);
    }

    public Collection<ItemModule> getAllModules() {
        return this.moduleMap.values();
    }
}