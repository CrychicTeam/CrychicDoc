package se.mickelus.tetra.module.schematic.requirement;

import java.util.Optional;
import se.mickelus.tetra.aspect.ItemAspect;
import se.mickelus.tetra.module.schematic.CraftingContext;

public class AspectRequirement implements CraftingRequirement {

    ItemAspect aspect;

    IntegerPredicate level;

    public AspectRequirement(ItemAspect aspect, IntegerPredicate level) {
        this.aspect = aspect;
        this.level = level;
    }

    @Override
    public boolean test(CraftingContext context) {
        return (Boolean) Optional.ofNullable(context.targetModule).map(module -> module.getAspects(context.targetStack)).filter(aspects -> aspects.contains(this.aspect)).map(aspects -> this.level == null || this.level.test(aspects.getLevel(this.aspect))).orElse(false);
    }
}