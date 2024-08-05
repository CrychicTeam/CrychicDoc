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
import fr.frinn.custommachinery.client.integration.jei.wrapper.ExperienceIngredientWrapper;
import fr.frinn.custommachinery.common.component.ExperienceMachineComponent;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.integration.jei.Experience;
import fr.frinn.custommachinery.impl.requirement.AbstractChanceableRequirement;
import fr.frinn.custommachinery.impl.requirement.AbstractRequirement;
import java.util.Collections;
import java.util.List;
import net.minecraft.network.chat.Component;

public class ExperienceRequirement extends AbstractChanceableRequirement<ExperienceMachineComponent> implements IJEIIngredientRequirement<Experience> {

    public static final NamedCodec<ExperienceRequirement> CODEC = NamedCodec.record(experienceRequirementInstance -> experienceRequirementInstance.group(RequirementIOMode.CODEC.fieldOf("mode").forGetter(AbstractRequirement::getMode), NamedCodec.INT.fieldOf("amount").forGetter(requirement -> requirement.amount), NamedCodec.enumCodec(Experience.Form.class).optionalFieldOf("form", Experience.Form.POINT).forGetter(requirement -> requirement.type), NamedCodec.doubleRange(0.0, 1.0).optionalFieldOf("chance", 1.0).forGetter(AbstractChanceableRequirement::getChance)).apply(experienceRequirementInstance, (mode, amount, type, chance) -> {
        ExperienceRequirement requirement = new ExperienceRequirement(mode, amount, type);
        requirement.setChance(chance);
        return requirement;
    }), "Experience requirement");

    private final int amount;

    private final Experience.Form type;

    public ExperienceRequirement(RequirementIOMode mode, int amount, Experience.Form type) {
        super(mode);
        this.amount = amount;
        this.type = type;
    }

    @Override
    public RequirementType<ExperienceRequirement> getType() {
        return (RequirementType<ExperienceRequirement>) Registration.EXPERIENCE_REQUIREMENT.get();
    }

    @Override
    public MachineComponentType<ExperienceMachineComponent> getComponentType() {
        return (MachineComponentType<ExperienceMachineComponent>) Registration.EXPERIENCE_MACHINE_COMPONENT.get();
    }

    public Experience.Form getForm() {
        return this.type;
    }

    public boolean test(ExperienceMachineComponent component, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        if (this.getForm().isPoint()) {
            return this.getMode() == RequirementIOMode.INPUT ? component.extractXp(amount, true) == amount : component.receiveXp(amount, true) == amount;
        } else {
            return this.getMode() == RequirementIOMode.INPUT ? component.extractLevel(amount, true) == amount : component.receiveLevel(amount, true) == amount;
        }
    }

    public CraftingResult processStart(ExperienceMachineComponent component, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        if (this.getMode() == RequirementIOMode.INPUT) {
            if (this.getForm().isPoint()) {
                int canExtract = component.extractXp(amount, true);
                if (canExtract == amount) {
                    component.extractXp(amount, false);
                    return CraftingResult.success();
                } else {
                    return CraftingResult.error(Component.translatable("custommachinery.requirements.xp.point.error.input", amount, canExtract));
                }
            } else {
                int canExtract = component.extractLevel(amount, true);
                if (canExtract == amount) {
                    component.extractLevel(amount, false);
                    return CraftingResult.success();
                } else {
                    return CraftingResult.error(Component.translatable("custommachinery.requirements.xp.level.error.input", amount, canExtract));
                }
            }
        } else {
            return CraftingResult.pass();
        }
    }

    public CraftingResult processEnd(ExperienceMachineComponent component, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        if (this.getMode() == RequirementIOMode.OUTPUT) {
            if (this.getForm().isPoint()) {
                int canReceive = component.receiveXp(amount, true);
                if (canReceive == amount) {
                    component.receiveXp(amount, false);
                    return CraftingResult.success();
                } else {
                    return CraftingResult.error(Component.translatable("custommachinery.requirements.xp.point.error.output", amount));
                }
            } else {
                int canReceive = component.receiveLevel(amount, true);
                if (canReceive == amount) {
                    component.receiveLevel(amount, false);
                    return CraftingResult.success();
                } else {
                    return CraftingResult.error(Component.translatable("custommachinery.requirements.xp.level.error.output", amount));
                }
            }
        } else {
            return CraftingResult.pass();
        }
    }

    @Override
    public List<IJEIIngredientWrapper<Experience>> getJEIIngredientWrappers(IMachineRecipe recipe) {
        return Collections.singletonList(new ExperienceIngredientWrapper(this.getMode(), this.amount, this.getChance(), false, recipe.getRecipeTime(), this.type));
    }
}