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
import fr.frinn.custommachinery.client.integration.jei.wrapper.ItemFilterIngredientWrapper;
import fr.frinn.custommachinery.common.component.handler.ItemComponentHandler;
import fr.frinn.custommachinery.common.component.variant.item.FilterItemComponentVariant;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import fr.frinn.custommachinery.impl.requirement.AbstractRequirement;
import java.util.Collections;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class ItemFilterRequirement extends AbstractRequirement<ItemComponentHandler> implements ITickableRequirement<ItemComponentHandler>, IJEIIngredientRequirement<ItemStack> {

    public static final NamedCodec<ItemFilterRequirement> CODEC = NamedCodec.record(itemFilterRequirementInstance -> itemFilterRequirementInstance.group(DefaultCodecs.INGREDIENT.fieldOf("ingredient").aliases("item").forGetter(requirement -> requirement.ingredient), NamedCodec.STRING.optionalFieldOf("slot", "").forGetter(requirement -> requirement.slot)).apply(itemFilterRequirementInstance, ItemFilterRequirement::new), "Item filter requirement");

    private final Ingredient ingredient;

    private final String slot;

    public ItemFilterRequirement(Ingredient ingredient, String slot) {
        super(RequirementIOMode.INPUT);
        this.ingredient = ingredient;
        this.slot = slot;
    }

    @Override
    public RequirementType<ItemFilterRequirement> getType() {
        return (RequirementType<ItemFilterRequirement>) Registration.ITEM_FILTER_REQUIREMENT.get();
    }

    @Override
    public MachineComponentType getComponentType() {
        return (MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get();
    }

    public boolean test(ItemComponentHandler handler, ICraftingContext context) {
        return handler.getComponents().stream().filter(component -> component.getVariant() == FilterItemComponentVariant.INSTANCE).anyMatch(component -> this.ingredient.test(component.getItemStack()));
    }

    public CraftingResult processStart(ItemComponentHandler component, ICraftingContext context) {
        return CraftingResult.pass();
    }

    public CraftingResult processEnd(ItemComponentHandler component, ICraftingContext context) {
        return CraftingResult.pass();
    }

    public CraftingResult processTick(ItemComponentHandler component, ICraftingContext context) {
        return this.test(component, context) ? CraftingResult.success() : CraftingResult.error(Component.translatable("custommachinery.requirements.item_filter.error"));
    }

    @Override
    public List<IJEIIngredientWrapper<ItemStack>> getJEIIngredientWrappers(IMachineRecipe recipe) {
        return Collections.singletonList(new ItemFilterIngredientWrapper(this.ingredient, this.slot));
    }
}