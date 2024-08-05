package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.requirement.ExperiencePerTickRequirement;
import fr.frinn.custommachinery.impl.integration.jei.Experience;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.custommachinery.requirement.ExperiencePerTick")
public interface ExperiencePerTickRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T requireXpPerTick(int xp) {
        return this.addRequirement(new ExperiencePerTickRequirement(RequirementIOMode.INPUT, xp, Experience.Form.POINT));
    }

    @Method
    default T produceXpPerTick(int xp) {
        return this.addRequirement(new ExperiencePerTickRequirement(RequirementIOMode.OUTPUT, xp, Experience.Form.POINT));
    }

    @Method
    default T requireLevelPerTick(int levels) {
        return this.addRequirement(new ExperiencePerTickRequirement(RequirementIOMode.INPUT, levels, Experience.Form.LEVEL));
    }

    @Method
    default T produceLevelPerTick(int levels) {
        return this.addRequirement(new ExperiencePerTickRequirement(RequirementIOMode.OUTPUT, levels, Experience.Form.LEVEL));
    }
}