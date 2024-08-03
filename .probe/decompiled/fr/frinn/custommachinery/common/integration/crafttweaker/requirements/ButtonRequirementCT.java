package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.common.requirement.ButtonRequirement;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.custommachinery.requirement.Button")
public interface ButtonRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T requireButtonPressed(String id) {
        return this.addRequirement(new ButtonRequirement(id, false));
    }

    @Method
    default T requireButtonReleased(String id) {
        return this.addRequirement(new ButtonRequirement(id, true));
    }
}