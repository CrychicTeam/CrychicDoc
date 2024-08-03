package fr.frinn.custommachinery.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientRequirement;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.client.integration.jei.wrapper.EnergyIngredientWrapper;
import fr.frinn.custommachinery.common.component.EnergyMachineComponent;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.integration.jei.Energy;
import fr.frinn.custommachinery.impl.requirement.AbstractChanceableRequirement;
import fr.frinn.custommachinery.impl.requirement.AbstractRequirement;
import java.util.Collections;
import java.util.List;
import net.minecraft.network.chat.Component;

public class EnergyRequirement extends AbstractChanceableRequirement<EnergyMachineComponent> implements IJEIIngredientRequirement<Energy> {

    public static final NamedCodec<EnergyRequirement> CODEC = NamedCodec.record(energyRequirementInstance -> energyRequirementInstance.group(RequirementIOMode.CODEC.fieldOf("mode").forGetter(AbstractRequirement::getMode), NamedCodec.INT.fieldOf("amount").forGetter(requirement -> requirement.amount), NamedCodec.doubleRange(0.0, 1.0).optionalFieldOf("chance", 1.0).forGetter(AbstractChanceableRequirement::getChance)).apply(energyRequirementInstance, (mode, amount, chance) -> {
        EnergyRequirement requirement = new EnergyRequirement(mode, amount);
        requirement.setChance(chance);
        return requirement;
    }), "Energy requirement");

    private final int amount;

    public EnergyRequirement(RequirementIOMode mode, int amount) {
        super(mode);
        this.amount = amount;
    }

    @Override
    public RequirementType<EnergyRequirement> getType() {
        return (RequirementType<EnergyRequirement>) Registration.ENERGY_REQUIREMENT.get();
    }

    @Override
    public MachineComponentType<EnergyMachineComponent> getComponentType() {
        return (MachineComponentType<EnergyMachineComponent>) Registration.ENERGY_MACHINE_COMPONENT.get();
    }

    public boolean test(EnergyMachineComponent energy, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        return this.getMode() == RequirementIOMode.INPUT ? energy.extractRecipeEnergy(amount, true) == amount : energy.receiveRecipeEnergy(amount, true) == amount;
    }

    public CraftingResult processStart(EnergyMachineComponent energy, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        if (this.getMode() == RequirementIOMode.INPUT) {
            int canExtract = energy.extractRecipeEnergy(amount, true);
            if (canExtract == amount) {
                energy.extractRecipeEnergy(amount, false);
                return CraftingResult.success();
            } else {
                return CraftingResult.error(Component.translatable("custommachinery.requirements.energy.error.input", amount, canExtract));
            }
        } else {
            return CraftingResult.pass();
        }
    }

    public CraftingResult processEnd(EnergyMachineComponent energy, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        if (this.getMode() == RequirementIOMode.OUTPUT) {
            int canReceive = energy.receiveRecipeEnergy(amount, true);
            if (canReceive == amount) {
                energy.receiveRecipeEnergy(amount, false);
                return CraftingResult.success();
            } else {
                return CraftingResult.error(Component.translatable("custommachinery.requirements.energy.error.output", amount));
            }
        } else {
            return CraftingResult.pass();
        }
    }

    @Override
    public List<IJEIIngredientWrapper<Energy>> getJEIIngredientWrappers(IMachineRecipe recipe) {
        return Collections.singletonList(new EnergyIngredientWrapper(this.getMode(), this.amount, this.getChance(), false, recipe.getRecipeTime()));
    }
}