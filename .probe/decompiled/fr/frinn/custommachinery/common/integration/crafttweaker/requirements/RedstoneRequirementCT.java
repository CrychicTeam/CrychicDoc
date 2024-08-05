package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.common.requirement.RedstoneRequirement;
import fr.frinn.custommachinery.impl.util.IntRange;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.custommachinery.requirement.Redstone")
public interface RedstoneRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T requireRedstone(String power) {
        try {
            return this.addRequirement(new RedstoneRequirement(IntRange.createFromString(power)));
        } catch (IllegalArgumentException var3) {
            return this.error("Invalid redstone signal range: \"{}\", {}", new Object[] { power, var3.getMessage() });
        }
    }
}