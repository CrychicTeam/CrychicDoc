package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.common.requirement.LightRequirement;
import fr.frinn.custommachinery.impl.util.IntRange;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.custommachinery.requirement.Light")
public interface LightRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T requireSkyLight(int level) {
        return this.requireSkyLight(level + "");
    }

    @Method
    default T requireSkyLight(String level) {
        try {
            return this.addRequirement(new LightRequirement(IntRange.createFromString(level), true));
        } catch (IllegalArgumentException var3) {
            return this.error("Invalid light level range: {}, {}", new Object[] { level, var3.getMessage() });
        }
    }

    @Method
    default T requireBlockLight(int level) {
        return this.requireBlockLight(level + "");
    }

    @Method
    default T requireBlockLight(String level) {
        try {
            return this.addRequirement(new LightRequirement(IntRange.createFromString(level), false));
        } catch (IllegalArgumentException var3) {
            return this.error("Invalid light level range: {}, {}", new Object[] { level, var3.getMessage() });
        }
    }
}