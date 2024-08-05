package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.requirement.ExperienceRequirement;
import fr.frinn.custommachinery.impl.integration.jei.Experience;

public interface ExperienceRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireXp(int xp) {
        return this.addRequirement(new ExperienceRequirement(RequirementIOMode.INPUT, xp, Experience.Form.POINT));
    }

    default RecipeJSBuilder produceXp(int xp) {
        return this.addRequirement(new ExperienceRequirement(RequirementIOMode.OUTPUT, xp, Experience.Form.POINT));
    }

    default RecipeJSBuilder requireLevel(int levels) {
        return this.addRequirement(new ExperienceRequirement(RequirementIOMode.INPUT, levels, Experience.Form.LEVEL));
    }

    default RecipeJSBuilder produceLevel(int levels) {
        return this.addRequirement(new ExperienceRequirement(RequirementIOMode.OUTPUT, levels, Experience.Form.LEVEL));
    }
}