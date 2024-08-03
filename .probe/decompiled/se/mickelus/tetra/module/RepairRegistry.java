package se.mickelus.tetra.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.schematic.RepairDefinition;
import se.mickelus.tetra.module.schematic.SchematicDefinition;

@ParametersAreNonnullByDefault
public class RepairRegistry {

    private static final Logger logger = LogManager.getLogger();

    public static RepairRegistry instance;

    private final Map<String, List<RepairDefinition>> repairMap;

    private List<RepairDefinition> injectedRepairs;

    public RepairRegistry() {
        instance = this;
        this.repairMap = new HashMap();
        this.injectedRepairs = new LinkedList();
        DataManager.instance.repairData.onReload(() -> this.setupDefinitions(DataManager.instance.repairData.getData()));
    }

    public void injectFromSchematics(Collection<SchematicDefinition> schematicDefinitions) {
        this.injectedRepairs = (List<RepairDefinition>) schematicDefinitions.stream().filter(schematicDefinition -> schematicDefinition.repair).flatMap(schematicDefinition -> Arrays.stream(schematicDefinition.outcomes)).filter(RepairDefinition::validateOutcome).map(RepairDefinition::new).collect(Collectors.toList());
    }

    private void setupDefinitions(Map<ResourceLocation, RepairDefinition> data) {
        this.repairMap.clear();
        this.injectedRepairs.forEach(this::putDefinition);
        data.entrySet().stream().filter(entry -> this.validate((ResourceLocation) entry.getKey(), (RepairDefinition) entry.getValue())).forEach(entry -> this.putDefinition((RepairDefinition) entry.getValue()));
    }

    private void putDefinition(RepairDefinition definition) {
        if (definition.replace && this.repairMap.containsKey(definition.moduleVariant)) {
            ((List) this.repairMap.get(definition.moduleVariant)).clear();
        }
        ((List) this.repairMap.computeIfAbsent(definition.moduleVariant, key -> new ArrayList())).add(definition);
    }

    private boolean validate(ResourceLocation identifier, RepairDefinition definition) {
        if (definition == null) {
            logger.warn("Failed to load repair definition '{}': Data is null (probably due to it failing to parse)", identifier);
            return false;
        } else if (definition.material == null) {
            logger.warn("Failed to load repair definition '{}': material field is empty", identifier);
            return false;
        } else if (definition.moduleKey == null) {
            logger.warn("Failed to load repair definition '{}': moduleKey field is empty", identifier);
            return false;
        } else if (definition.moduleVariant == null) {
            logger.warn("Failed to load repair definition '{}': moduleVariant field is empty", identifier);
            return false;
        } else {
            return true;
        }
    }

    public List<RepairDefinition> getDefinitions(String moduleVariant) {
        return (List<RepairDefinition>) this.repairMap.getOrDefault(moduleVariant, Collections.emptyList());
    }
}