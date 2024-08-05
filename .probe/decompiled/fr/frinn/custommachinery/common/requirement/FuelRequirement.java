package fr.frinn.custommachinery.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientRequirement;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.requirement.ITickableRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.client.integration.jei.wrapper.FuelItemIngredientWrapper;
import fr.frinn.custommachinery.common.component.FuelMachineComponent;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.requirement.AbstractRequirement;
import java.util.Collections;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FuelRequirement extends AbstractRequirement<FuelMachineComponent> implements ITickableRequirement<FuelMachineComponent>, IJEIIngredientRequirement<ItemStack> {

    public static final NamedCodec<FuelRequirement> CODEC = NamedCodec.record(fuelRequirementInstance -> fuelRequirementInstance.group(NamedCodec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("amount", 1).forGetter(requirement -> requirement.amount)).apply(fuelRequirementInstance, FuelRequirement::new), "Fuel requirement");

    private final int amount;

    public FuelRequirement(int amount) {
        super(RequirementIOMode.INPUT);
        this.amount = amount;
    }

    public int getAmount() {
        return this.amount;
    }

    @Override
    public RequirementType<FuelRequirement> getType() {
        return (RequirementType<FuelRequirement>) Registration.FUEL_REQUIREMENT.get();
    }

    public boolean test(FuelMachineComponent component, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        return component.canStartRecipe(amount);
    }

    public CraftingResult processStart(FuelMachineComponent component, ICraftingContext context) {
        return CraftingResult.pass();
    }

    public CraftingResult processTick(FuelMachineComponent component, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        return component.burn(amount) ? CraftingResult.success() : CraftingResult.error(Component.translatable("custommachinery.requirements.fuel.error"));
    }

    public CraftingResult processEnd(FuelMachineComponent component, ICraftingContext context) {
        return CraftingResult.pass();
    }

    @Override
    public MachineComponentType<FuelMachineComponent> getComponentType() {
        return (MachineComponentType<FuelMachineComponent>) Registration.FUEL_MACHINE_COMPONENT.get();
    }

    @Override
    public List<IJEIIngredientWrapper<ItemStack>> getJEIIngredientWrappers(IMachineRecipe recipe) {
        return Collections.singletonList(new FuelItemIngredientWrapper(this.amount));
    }
}