package fr.frinn.custommachinery.api.machine;

import fr.frinn.custommachinery.api.crafting.IProcessorTemplate;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface ICustomMachine {

    Component getName();

    ResourceLocation getId();

    List<ResourceLocation> getRecipeIds();

    boolean isDummy();

    IMachineAppearance getAppearance(MachineStatus var1);

    List<IGuiElement> getGuiElements();

    IProcessorTemplate<?> getProcessorTemplate();
}