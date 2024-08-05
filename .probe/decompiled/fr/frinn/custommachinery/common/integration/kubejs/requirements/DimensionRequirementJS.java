package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.common.requirement.DimensionRequirement;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public interface DimensionRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder dimensionWhitelist(String[] dimensions) {
        List<ResourceLocation> dimensionsID = new ArrayList();
        for (String dimension : dimensions) {
            if (!ResourceLocation.isValidResourceLocation(dimension)) {
                return this.error("Invalid dimension ID: {}", new Object[] { dimension });
            }
            dimensionsID.add(new ResourceLocation(dimension));
        }
        return this.addRequirement(new DimensionRequirement(dimensionsID, false));
    }

    default RecipeJSBuilder dimensionBlacklist(String[] dimensions) {
        List<ResourceLocation> dimensionsID = new ArrayList();
        for (String dimension : dimensions) {
            if (!ResourceLocation.isValidResourceLocation(dimension)) {
                return this.error("Invalid dimension ID: {}", new Object[] { dimension });
            }
            dimensionsID.add(new ResourceLocation(dimension));
        }
        return this.addRequirement(new DimensionRequirement(dimensionsID, true));
    }
}