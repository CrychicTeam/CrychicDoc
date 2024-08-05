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
import fr.frinn.custommachinery.client.integration.jei.wrapper.ExperienceIngredientWrapper;
import fr.frinn.custommachinery.common.component.ExperienceMachineComponent;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.integration.jei.Experience;
import fr.frinn.custommachinery.impl.requirement.AbstractChanceableRequirement;
import fr.frinn.custommachinery.impl.requirement.AbstractRequirement;
import java.util.Collections;
import java.util.List;
import net.minecraft.network.chat.Component;

public class ExperiencePerTickRequirement extends AbstractChanceableRequirement<ExperienceMachineComponent> implements ITickableRequirement<ExperienceMachineComponent>, IJEIIngredientRequirement<Experience> {

    public static final NamedCodec<ExperiencePerTickRequirement> CODEC = NamedCodec.record(experienceRequirementInstance -> experienceRequirementInstance.group(RequirementIOMode.CODEC.fieldOf("mode").forGetter(AbstractRequirement::getMode), NamedCodec.INT.fieldOf("amount").forGetter(requirement -> requirement.amount), NamedCodec.enumCodec(Experience.Form.class).optionalFieldOf("form", Experience.Form.POINT).forGetter(requirement -> requirement.type), NamedCodec.doubleRange(0.0, 1.0).optionalFieldOf("chance", 1.0).forGetter(AbstractChanceableRequirement::getChance)).apply(experienceRequirementInstance, (mode, amount, type, chance) -> {
        ExperiencePerTickRequirement requirement = new ExperiencePerTickRequirement(mode, amount, type);
        requirement.setChance(chance);
        return requirement;
    }), "Experience requirement");

    private final int amount;

    private final Experience.Form type;

    public ExperiencePerTickRequirement(RequirementIOMode mode, int amount, Experience.Form type) {
        super(mode);
        this.amount = amount;
        this.type = type;
    }

    @Override
    public RequirementType<ExperiencePerTickRequirement> getType() {
        return (RequirementType<ExperiencePerTickRequirement>) Registration.EXPERIENCE_PER_TICK_REQUIREMENT.get();
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
        return CraftingResult.pass();
    }

    public CraftingResult processTick(ExperienceMachineComponent component, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        if (this.getForm().isPoint()) {
            if (this.getMode() == RequirementIOMode.INPUT) {
                int canExtract = component.extractXp(amount, true);
                if (canExtract == amount) {
                    component.extractXp(amount, false);
                    return CraftingResult.success();
                } else {
                    return CraftingResult.error(Component.translatable("custommachinery.requirements.xppertick.point.error.input", amount, canExtract));
                }
            } else {
                int canReceive = component.receiveXp(amount, true);
                if (canReceive == amount) {
                    component.receiveXp(amount, false);
                    return CraftingResult.success();
                } else {
                    return CraftingResult.error(Component.translatable("custommachinery.requirements.xppertick.point.error.output", amount));
                }
            }
        } else if (this.getMode() == RequirementIOMode.INPUT) {
            int canExtract = component.extractLevel(amount, true);
            if (canExtract == amount) {
                component.extractLevel(amount, false);
                return CraftingResult.success();
            } else {
                return CraftingResult.error(Component.translatable("custommachinery.requirements.xppertick.level.error.input", amount, canExtract));
            }
        } else {
            int canReceive = component.receiveLevel(amount, true);
            if (canReceive == amount) {
                component.receiveLevel(amount, false);
                return CraftingResult.success();
            } else {
                return CraftingResult.error(Component.translatable("custommachinery.requirements.xppertick.level.error.output", amount));
            }
        }
    }

    public CraftingResult processEnd(ExperienceMachineComponent component, ICraftingContext context) {
        return CraftingResult.pass();
    }

    @Override
    public List<IJEIIngredientWrapper<Experience>> getJEIIngredientWrappers(IMachineRecipe recipe) {
        return Collections.singletonList(new ExperienceIngredientWrapper(this.getMode(), this.amount, this.getChance(), true, recipe.getRecipeTime(), this.type));
    }
}