package se.mickelus.tetra.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.mutil.util.Filter;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.schematic.ConfigSchematic;
import se.mickelus.tetra.module.schematic.CraftingContext;
import se.mickelus.tetra.module.schematic.InvalidSchematicException;
import se.mickelus.tetra.module.schematic.MaterialOutcomeDefinition;
import se.mickelus.tetra.module.schematic.OutcomeDefinition;
import se.mickelus.tetra.module.schematic.SchematicDefinition;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class SchematicRegistry {

    private static final Logger logger = LogManager.getLogger();

    public static SchematicRegistry instance;

    private final Map<ResourceLocation, UpgradeSchematic> dynamicSchematics;

    private Map<ResourceLocation, UpgradeSchematic> schematicMap;

    public SchematicRegistry() {
        instance = this;
        this.schematicMap = Collections.emptyMap();
        this.dynamicSchematics = new HashMap();
        DataManager.instance.schematicData.onReload(() -> this.setupSchematics(DataManager.instance.schematicData.getData()));
    }

    public static UpgradeSchematic getSchematic(ResourceLocation identifier) {
        return (UpgradeSchematic) instance.schematicMap.get(identifier);
    }

    public static UpgradeSchematic getSchematic(String key) {
        return getSchematic(new ResourceLocation("tetra", key));
    }

    public static Collection<UpgradeSchematic> getAllSchematics() {
        return instance.schematicMap.values();
    }

    public static UpgradeSchematic[] getSchematics(ItemStack itemStack) {
        return (UpgradeSchematic[]) getAllSchematics().stream().filter(upgradeSchematic -> upgradeSchematic.isRelevant(itemStack)).toArray(UpgradeSchematic[]::new);
    }

    public static UpgradeSchematic[] getSchematics(ItemStack itemStack, String slot) {
        return (UpgradeSchematic[]) getAllSchematics().stream().filter(upgradeSchematic -> upgradeSchematic.isRelevant(itemStack)).filter(upgradeSchematic -> upgradeSchematic.isApplicableForSlot(slot, itemStack)).toArray(UpgradeSchematic[]::new);
    }

    public static UpgradeSchematic[] getSchematics(CraftingContext context) {
        return (UpgradeSchematic[]) getAllSchematics().stream().filter(upgradeSchematic -> upgradeSchematic.isRelevant(context.targetStack)).filter(upgradeSchematic -> upgradeSchematic.isApplicableForSlot(context.slot, context.targetStack)).filter(upgradeSchematic -> upgradeSchematic.matchesRequirements(context)).toArray(UpgradeSchematic[]::new);
    }

    public static UpgradeSchematic[] getSchematics(ItemStack itemStack, String slot, Player player, Level level, BlockPos pos, BlockState blockState, ResourceLocation[] unlocks) {
        return getSchematics(new CraftingContext(level, pos, blockState, player, itemStack, slot, unlocks));
    }

    public void registerSchematic(UpgradeSchematic schematic) {
        this.dynamicSchematics.put(new ResourceLocation("tetra", schematic.getKey()), schematic);
    }

    private void setupSchematics(Map<ResourceLocation, SchematicDefinition> data) {
        this.schematicMap = (Map<ResourceLocation, UpgradeSchematic>) data.entrySet().stream().filter(entry -> this.validateSchematicDefinition((ResourceLocation) entry.getKey(), (SchematicDefinition) entry.getValue())).flatMap(entry -> this.createSchematics((ResourceLocation) entry.getKey(), (SchematicDefinition) entry.getValue()).stream()).filter(entry -> entry.getRight() != null).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        this.dynamicSchematics.forEach((identifier, schematic) -> this.schematicMap.put(identifier, schematic));
        RepairRegistry.instance.injectFromSchematics(data.values());
    }

    private boolean validateSchematicDefinition(ResourceLocation identifier, SchematicDefinition definition) {
        if (definition == null) {
            logger.warn("Failed to create schematic from schematic definition '{}': Data is null (probably due to it failing to parse)", identifier);
            return false;
        } else if (definition.slots != null && definition.slots.length >= 1) {
            return true;
        } else {
            logger.warn("Failed to create schematic from schematic definition '{}': Slots field is empty", identifier);
            return false;
        }
    }

    private Collection<Pair<ResourceLocation, ConfigSchematic>> createSchematics(ResourceLocation identifier, SchematicDefinition definition) {
        this.processDefinition(definition);
        if (definition.slots.length != definition.keySuffixes.length) {
            try {
                return Collections.singletonList(new ImmutablePair(identifier, new ConfigSchematic(definition)));
            } catch (InvalidSchematicException var7) {
                var7.printMessage();
                return Collections.singletonList(new ImmutablePair(identifier, null));
            }
        } else {
            ArrayList<Pair<ResourceLocation, ConfigSchematic>> result = new ArrayList(definition.slots.length);
            for (int i = 0; i < definition.slots.length; i++) {
                try {
                    ResourceLocation suffixedIdentifier = new ResourceLocation(identifier.getNamespace(), identifier.getPath() + definition.keySuffixes[i]);
                    result.add(new ImmutablePair(suffixedIdentifier, new ConfigSchematic(definition, definition.keySuffixes[i], definition.slots[i])));
                } catch (InvalidSchematicException var6) {
                    var6.printMessage();
                }
            }
            return result;
        }
    }

    private void processDefinition(SchematicDefinition definition) {
        if (definition.applicableMaterials == null) {
            definition.applicableMaterials = (String[]) Arrays.stream(definition.outcomes).flatMap(outcome -> {
                if (outcome instanceof MaterialOutcomeDefinition) {
                    return Arrays.stream(((MaterialOutcomeDefinition) outcome).materials).map(ResourceLocation::m_135815_).map(path -> {
                        if (path.endsWith("/")) {
                            return "#" + path.substring(0, path.length() - 1);
                        } else {
                            int separatorIndex = path.lastIndexOf("/");
                            return separatorIndex != -1 ? "!" + path.substring(separatorIndex + 1) : "!" + path;
                        }
                    });
                } else {
                    if (outcome.material.isValid()) {
                        ItemStack[] applicableItemStacks = outcome.material.getApplicableItemStacks();
                        if (applicableItemStacks.length > 0) {
                            return Stream.of(ForgeRegistries.ITEMS.getKey(applicableItemStacks[0].getItem()).toString());
                        }
                    }
                    return Stream.empty();
                }
            }).toArray(String[]::new);
        }
        definition.outcomes = (OutcomeDefinition[]) Arrays.stream(definition.outcomes).flatMap(outcome -> outcome instanceof MaterialOutcomeDefinition ? this.expandMaterialOutcome((MaterialOutcomeDefinition) outcome) : Stream.of(outcome)).filter(Filter.distinct(outcome -> outcome.material)).sorted((a, b) -> Boolean.compare(b.material != null && b.material.isTagged(), a.material != null && a.material.isTagged())).toArray(OutcomeDefinition[]::new);
    }

    private Stream<OutcomeDefinition> expandMaterialOutcome(MaterialOutcomeDefinition source) {
        return Arrays.stream(source.materials).map(rl -> rl.getPath().endsWith("/") ? DataManager.instance.materialData.getDataIn(rl) : (Collection) Optional.ofNullable(DataManager.instance.materialData.getData(rl)).map(Collections::singletonList).orElseGet(Collections::emptyList)).flatMap(Collection::stream).map(source::combine);
    }
}