package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.common.requirement.RedstoneRequirement;
import fr.frinn.custommachinery.impl.util.IntRange;

public interface RedstoneRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireRedstone(String power) {
        try {
            return this.addRequirement(new RedstoneRequirement(IntRange.createFromString(power)));
        } catch (IllegalArgumentException var3) {
            return this.error("Invalid redstone signal range: \"{}\", {}", new Object[] { power, var3.getMessage() });
        }
    }
}