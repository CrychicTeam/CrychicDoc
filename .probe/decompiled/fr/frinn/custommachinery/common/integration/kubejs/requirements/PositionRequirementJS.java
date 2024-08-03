package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.common.requirement.PositionRequirement;
import fr.frinn.custommachinery.impl.util.IntRange;

public interface PositionRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requirePosition(String xString, String yString, String zString) {
        IntRange x;
        try {
            x = IntRange.createFromString(xString);
        } catch (IllegalArgumentException var10) {
            return this.error("Invalid X position range: {} {}", new Object[] { xString, var10.getMessage() });
        }
        IntRange y;
        try {
            y = IntRange.createFromString(yString);
        } catch (IllegalArgumentException var9) {
            return this.error("Invalid Y position range: {} {}", new Object[] { xString, var9.getMessage() });
        }
        IntRange z;
        try {
            z = IntRange.createFromString(zString);
        } catch (IllegalArgumentException var8) {
            return this.error("Invalid Z position range: {} {}", new Object[] { xString, var8.getMessage() });
        }
        return this.addRequirement(new PositionRequirement(x, y, z));
    }
}