package fr.frinn.custommachinery.api.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.integration.jei.DisplayInfoTemplate;
import fr.frinn.custommachinery.api.integration.jei.IDisplayInfo;
import fr.frinn.custommachinery.impl.codec.RegistrarCodec;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public interface IRequirement<T extends IMachineComponent> {

    NamedCodec<IRequirement<?>> CODEC = NamedCodec.record(iRequirementInstance -> iRequirementInstance.group(RegistrarCodec.REQUIREMENT.dispatch(IRequirement::getType, RequirementType::getCodec, "Requirement").forGetter(requirement -> requirement), DisplayInfoTemplate.CODEC.optionalFieldOf("info").forGetter(requirement -> Optional.ofNullable(requirement.getDisplayInfoTemplate()))).apply(iRequirementInstance, (requirement, info) -> {
        info.ifPresent(requirement::setDisplayInfoTemplate);
        return requirement;
    }), "Requirement");

    RequirementType<? extends IRequirement<?>> getType();

    MachineComponentType<T> getComponentType();

    boolean test(T var1, ICraftingContext var2);

    CraftingResult processStart(T var1, ICraftingContext var2);

    CraftingResult processEnd(T var1, ICraftingContext var2);

    RequirementIOMode getMode();

    void setDisplayInfoTemplate(DisplayInfoTemplate var1);

    @Nullable
    DisplayInfoTemplate getDisplayInfoTemplate();

    default void getDisplayInfo(IDisplayInfo info) {
    }
}