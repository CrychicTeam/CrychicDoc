package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.requirement.ExperiencePerTickRequirement;
import fr.frinn.custommachinery.impl.integration.jei.Experience;

public interface ExperiencePerTickRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireXpPerTick(int xp) {
        return this.addRequirement(new ExperiencePerTickRequirement(RequirementIOMode.INPUT, xp, Experience.Form.POINT));
    }

    default RecipeJSBuilder produceXpPerTick(int xp) {
        return this.addRequirement(new ExperiencePerTickRequirement(RequirementIOMode.OUTPUT, xp, Experience.Form.POINT));
    }

    default RecipeJSBuilder requireLevelPerTick(int levels) {
        return this.addRequirement(new ExperiencePerTickRequirement(RequirementIOMode.INPUT, levels, Experience.Form.LEVEL));
    }

    default RecipeJSBuilder produceLevelPerTick(int levels) {
        return this.addRequirement(new ExperiencePerTickRequirement(RequirementIOMode.OUTPUT, levels, Experience.Form.LEVEL));
    }
}