package se.mickelus.tetra.module.schematic.requirement;

import se.mickelus.tetra.module.schematic.CraftingContext;

public class HasImprovementRequirement implements CraftingRequirement {

    String improvement;

    IntegerPredicate level;

    @Override
    public boolean test(CraftingContext context) {
        if (context.targetMajorModule != null) {
            return this.level != null && !this.level.test(context.targetMajorModule.getImprovementLevel(context.targetStack, this.improvement)) ? false : context.targetMajorModule.getImprovement(context.targetStack, this.improvement) != null;
        } else {
            return false;
        }
    }
}