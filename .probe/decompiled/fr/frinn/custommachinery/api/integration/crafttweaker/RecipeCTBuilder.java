package fr.frinn.custommachinery.api.integration.crafttweaker;

import fr.frinn.custommachinery.api.requirement.IRequirement;

public interface RecipeCTBuilder<T> {

    T addRequirement(IRequirement<?> var1);

    T error(String var1, Object... var2);
}