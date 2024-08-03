package fr.frinn.custommachinery.common.integration.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import fr.frinn.custommachinery.api.crafting.IRecipeBuilder;
import fr.frinn.custommachinery.api.integration.jei.DisplayInfoTemplate;
import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.IChanceableRequirement;
import fr.frinn.custommachinery.api.requirement.IDelayedRequirement;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import fr.frinn.custommachinery.common.util.Utils;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;
import org.slf4j.helpers.MessageFormatter;

public abstract class AbstractRecipeJSBuilder<T extends IRecipeBuilder<? extends Recipe<?>>> extends RecipeJS implements RecipeJSBuilder {

    public static final Map<ResourceLocation, Map<ResourceLocation, Integer>> IDS = new HashMap();

    private final ResourceLocation typeID;

    private IRequirement<?> lastRequirement;

    private boolean jei = false;

    public AbstractRecipeJSBuilder(ResourceLocation typeID) {
        this.typeID = typeID;
    }

    @Override
    public void afterLoaded() {
        super.afterLoaded();
        ResourceLocation machine = this.getValue(CustomMachineryRecipeSchemas.MACHINE_ID);
        if (machine == null) {
            throw new RecipeExceptionJS("Invalid machine id: " + this.getValue(CustomMachineryRecipeSchemas.MACHINE_ID));
        } else {
            if (this.newRecipe) {
                int uniqueID = (Integer) ((Map) IDS.computeIfAbsent(this.typeID, id -> new HashMap())).computeIfAbsent(machine, m -> 0);
                ((Map) IDS.get(this.typeID)).put(machine, uniqueID + 1);
                this.id = new ResourceLocation("kubejs", this.typeID.getPath() + "/" + machine.getNamespace() + "/" + machine.getPath() + "/" + uniqueID);
            }
        }
    }

    @Nullable
    @Override
    public Recipe<?> createRecipe() {
        if (this.removed) {
            return null;
        } else if (!this.newRecipe) {
            return super.createRecipe();
        } else {
            T builder = this.makeBuilder();
            for (IRequirement<?> requirement : this.getValue(CustomMachineryRecipeSchemas.REQUIREMENTS)) {
                builder.withRequirement(requirement);
            }
            for (IRequirement<?> requirement : this.getValue(CustomMachineryRecipeSchemas.JEI_REQUIREMENTS)) {
                builder.withJeiRequirement(requirement);
            }
            builder.withPriority(this.getValue(CustomMachineryRecipeSchemas.PRIORITY));
            builder.withJeiPriority(this.getValue(CustomMachineryRecipeSchemas.JEI_PRIORITY));
            if (this.getValue(CustomMachineryRecipeSchemas.HIDDEN)) {
                builder.hide();
            }
            return (Recipe<?>) builder.build(this.getOrCreateId());
        }
    }

    @Override
    public String getFromToString() {
        return ((Recipe) Objects.requireNonNull(this.createRecipe())).toString();
    }

    public abstract T makeBuilder();

    public AbstractRecipeJSBuilder<T> jei() {
        this.jei = true;
        return this;
    }

    public AbstractRecipeJSBuilder<T> priority(int priority) {
        if (!this.jei) {
            this.setValue(CustomMachineryRecipeSchemas.PRIORITY, Integer.valueOf(priority));
        } else {
            this.setValue(CustomMachineryRecipeSchemas.JEI_PRIORITY, Integer.valueOf(priority));
        }
        return this;
    }

    public AbstractRecipeJSBuilder<T> chance(double chance) {
        if (this.lastRequirement != null && this.lastRequirement instanceof IChanceableRequirement) {
            ((IChanceableRequirement) this.lastRequirement).setChance(chance);
        } else {
            ScriptType.SERVER.console.warn("Can't set chance for requirement: " + this.lastRequirement);
        }
        return this;
    }

    public AbstractRecipeJSBuilder<T> info(Consumer<DisplayInfoTemplate> consumer) {
        if (this.lastRequirement == null) {
            this.error("Can't add info on a null requirement !");
        }
        try {
            DisplayInfoTemplate template = new DisplayInfoTemplate();
            consumer.accept(template);
            this.lastRequirement.setDisplayInfoTemplate(template);
        } catch (Exception var3) {
            this.error("Error when adding custom display info on requirement {}\n{}", this.lastRequirement, var3);
        }
        return this;
    }

    public AbstractRecipeJSBuilder<T> hide() {
        this.setValue(CustomMachineryRecipeSchemas.HIDDEN, Boolean.valueOf(true));
        return this;
    }

    public AbstractRecipeJSBuilder<T> delay(double delay) {
        if (this.lastRequirement != null && this.lastRequirement instanceof IDelayedRequirement) {
            ((IDelayedRequirement) this.lastRequirement).setDelay(delay);
        } else {
            ScriptType.SERVER.console.warn("Can't set delay for requirement: " + this.lastRequirement);
        }
        return this;
    }

    public AbstractRecipeJSBuilder<T> addRequirement(IRequirement<?> requirement) {
        this.lastRequirement = requirement;
        if (!this.jei) {
            this.setValue(CustomMachineryRecipeSchemas.REQUIREMENTS, Utils.addToArray(this.getValue(CustomMachineryRecipeSchemas.REQUIREMENTS), requirement));
        } else {
            this.setValue(CustomMachineryRecipeSchemas.JEI_REQUIREMENTS, Utils.addToArray(this.getValue(CustomMachineryRecipeSchemas.JEI_REQUIREMENTS), requirement));
        }
        return this;
    }

    @Override
    public RecipeJSBuilder error(String error, Object... args) {
        throw new RecipeExceptionJS(MessageFormatter.arrayFormat(error, args).getMessage());
    }
}