package fr.frinn.custommachinery.api.integration.kubejs;

import fr.frinn.custommachinery.api.requirement.IRequirement;

public interface RecipeJSBuilder {

    RecipeJSBuilder addRequirement(IRequirement<?> var1);

    RecipeJSBuilder error(String var1, Object... var2);
}