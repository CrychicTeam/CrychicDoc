package se.mickelus.tetra.module.schematic;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.tetra.module.data.MaterialData;
import se.mickelus.tetra.module.data.ToolData;

@ParametersAreNonnullByDefault
public class MaterialOutcomeDefinition extends OutcomeDefinition {

    public ResourceLocation[] materials = new ResourceLocation[0];

    public int countOffset = 0;

    public float countFactor = 1.0F;

    public int toolOffset = 0;

    public float toolFactor = 1.0F;

    public float experienceOffset = 0.0F;

    public float experienceFactor = 0.0F;

    public OutcomeDefinition combine(MaterialData materialData) {
        UniqueOutcomeDefinition result = new UniqueOutcomeDefinition();
        if (materialData.hiddenOutcomes) {
            result.hidden = true;
        }
        result.moduleKey = this.moduleKey;
        result.materialSlot = this.materialSlot;
        if (this.moduleVariant != null) {
            result.moduleVariant = this.moduleVariant + materialData.key;
            result.improvements = (Map<String, Integer>) Stream.of(this.improvements, materialData.improvements).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Integer::max));
        } else if (!this.improvements.isEmpty()) {
            result.improvements = (Map<String, Integer>) this.improvements.entrySet().stream().collect(Collectors.toMap(e -> (String) e.getKey() + materialData.key, Entry::getValue));
        }
        result.material = materialData.material.offsetCount(this.countFactor, this.countOffset);
        if (materialData.requiredTools != null) {
            result.requiredTools = ToolData.offsetLevel(materialData.requiredTools, this.toolFactor, this.toolOffset);
        }
        result.requiredTools = ToolData.merge(this.requiredTools, (ToolData) Optional.ofNullable(materialData.requiredTools).map(materialTools -> ToolData.offsetLevel(materialTools, this.toolFactor, this.toolOffset)).orElse(null));
        result.experienceCost = Math.round(materialData.experienceCost * this.experienceFactor + this.experienceOffset);
        return result;
    }
}