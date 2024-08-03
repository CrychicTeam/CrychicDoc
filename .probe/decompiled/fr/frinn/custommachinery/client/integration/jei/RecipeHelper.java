package fr.frinn.custommachinery.client.integration.jei;

import fr.frinn.custommachinery.PlatformHelper;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.guielement.IComponentGuiElement;
import fr.frinn.custommachinery.api.integration.jei.IRecipeHelper;
import fr.frinn.custommachinery.common.component.DummyComponentManager;
import fr.frinn.custommachinery.common.component.MachineComponentManager;
import fr.frinn.custommachinery.common.init.CustomMachineBlock;
import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import java.util.Optional;
import mezz.jei.api.helpers.IJeiHelpers;
import net.minecraft.core.BlockPos;

public class RecipeHelper implements IRecipeHelper {

    private final CustomMachine machine;

    private final MachineComponentManager manager;

    private final IJeiHelpers jeiHelpers;

    public RecipeHelper(CustomMachine machine, IJeiHelpers jeiHelpers) {
        this.machine = machine;
        CustomMachineTile tile = PlatformHelper.createMachineTile(BlockPos.ZERO, ((CustomMachineBlock) Registration.CUSTOM_MACHINE_BLOCK.get()).m_49966_());
        tile.setId(machine.getId());
        this.manager = new DummyComponentManager(tile);
        this.jeiHelpers = jeiHelpers;
    }

    public CustomMachine getMachine() {
        return this.machine;
    }

    @Override
    public Optional<IMachineComponentTemplate<?>> getComponentForElement(IComponentGuiElement<?> element) {
        return this.machine.getComponentTemplates().stream().filter(template -> template.getType() == element.getComponentType() && template.getId().equals(element.getComponentId())).findFirst();
    }

    public MachineComponentManager getDummyManager() {
        return this.manager;
    }

    @Override
    public IJeiHelpers getJeiHelpers() {
        return this.jeiHelpers;
    }
}