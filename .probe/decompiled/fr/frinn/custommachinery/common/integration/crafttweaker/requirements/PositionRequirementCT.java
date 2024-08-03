package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.common.requirement.PositionRequirement;
import fr.frinn.custommachinery.impl.util.IntRange;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.custommachinery.requirement.Position")
public interface PositionRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T requirePosition(String xString, String yString, String zString) {
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