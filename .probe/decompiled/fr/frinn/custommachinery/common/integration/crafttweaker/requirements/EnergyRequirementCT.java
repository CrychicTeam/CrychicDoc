package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.requirement.EnergyRequirement;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.custommachinery.requirement.Energy")
public interface EnergyRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T requireEnergy(int amount) {
        return this.addRequirement(new EnergyRequirement(RequirementIOMode.INPUT, amount));
    }

    @Method
    default T produceEnergy(int amount) {
        return this.addRequirement(new EnergyRequirement(RequirementIOMode.OUTPUT, amount));
    }
}