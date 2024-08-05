package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.common.requirement.TimeRequirement;
import fr.frinn.custommachinery.impl.util.IntRange;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.custommachinery.requirement.Time")
public interface TimeRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T requireTime(String time) {
        try {
            IntRange range = IntRange.createFromString(time);
            return this.addRequirement(new TimeRequirement(range));
        } catch (IllegalArgumentException var3) {
            return this.error("Impossible to parse time range : {},\n{}", new Object[] { time, var3.getMessage() });
        }
    }
}