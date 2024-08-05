package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.common.requirement.ChunkloadRequirement;

public interface ChunkloadRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder chunkload() {
        return this.chunkload(1);
    }

    default RecipeJSBuilder chunkload(int radius) {
        return radius >= 1 && radius <= 32 ? this.addRequirement(new ChunkloadRequirement(radius)) : this.error("Invalid radius for chunkload requirement: {}.\nMust be between 1 and 32", new Object[] { radius });
    }
}