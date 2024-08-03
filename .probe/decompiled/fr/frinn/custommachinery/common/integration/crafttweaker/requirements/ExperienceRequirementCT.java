package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.requirement.ExperienceRequirement;
import fr.frinn.custommachinery.impl.integration.jei.Experience;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.custommachinery.requirement.Experience")
public interface ExperienceRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T requireXp(int xp) {
        return this.addRequirement(new ExperienceRequirement(RequirementIOMode.INPUT, xp, Experience.Form.POINT));
    }

    @Method
    default T produceXp(int xp) {
        return this.addRequirement(new ExperienceRequirement(RequirementIOMode.OUTPUT, xp, Experience.Form.POINT));
    }

    @Method
    default T requireLevel(int levels) {
        return this.addRequirement(new ExperienceRequirement(RequirementIOMode.INPUT, levels, Experience.Form.LEVEL));
    }

    @Method
    default T produceLevel(int levels) {
        return this.addRequirement(new ExperienceRequirement(RequirementIOMode.OUTPUT, levels, Experience.Form.LEVEL));
    }
}