package se.mickelus.tetra.module.schematic;

import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.tetra.module.data.ToolData;

@ParametersAreNonnullByDefault
public class RepairDefinition {

    public OutcomeMaterial material;

    public ToolData requiredTools;

    public String moduleKey;

    public String moduleVariant;

    public int experienceCost;

    public boolean replace = false;

    public RepairDefinition(OutcomeDefinition outcomeDefinition) {
        this.moduleKey = outcomeDefinition.moduleKey;
        this.moduleVariant = outcomeDefinition.moduleVariant;
        this.material = outcomeDefinition.material;
        this.requiredTools = outcomeDefinition.requiredTools;
        this.experienceCost = outcomeDefinition.experienceCost;
    }

    public static boolean validateOutcome(OutcomeDefinition outcome) {
        return outcome.moduleVariant != null && outcome.material != null && outcome.material.isValid();
    }
}