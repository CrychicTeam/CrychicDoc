package se.mickelus.tetra.module.schematic.requirement;

import se.mickelus.tetra.module.schematic.CraftingContext;

public class AcceptsImprovementRequirement implements CraftingRequirement {

    String improvement;

    Integer level;

    @Override
    public boolean test(CraftingContext context) {
        if (context.targetMajorModule != null) {
            return this.level != null && !context.targetMajorModule.acceptsImprovementLevel(this.improvement, this.level) ? false : context.targetMajorModule.acceptsImprovement(this.improvement);
        } else {
            return false;
        }
    }
}