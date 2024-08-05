package fr.frinn.custommachinery.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import fr.frinn.custommachinery.api.requirement.IChanceableRequirement;
import fr.frinn.custommachinery.api.requirement.IDelayedRequirement;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import fr.frinn.custommachinery.common.crafting.craft.CustomCraftRecipe;
import fr.frinn.custommachinery.common.crafting.craft.CustomCraftRecipeBuilder;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.BiomeRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.BlockRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.ButtonRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.CommandRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.DimensionRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.DropRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.DurabilityRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.EffectRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.EnergyPerTickRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.EnergyRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.EntityRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.ExperiencePerTickRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.ExperienceRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.FluidPerTickRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.FluidRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.FuelRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.FunctionRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.ItemFilterRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.ItemRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.ItemTransformRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.LightRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.LootTableRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.PositionRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.RedstoneRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.SkyRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.StructureRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.TimeRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.WeatherRequirementCT;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;
import org.openzen.zencode.java.ZenCodeType.OptionalString;

@ZenRegister
@Name("mods.custommachinery.CraftRecipeBuilder")
public class CustomCraftRecipeCTBuilder implements EnergyRequirementCT<CustomCraftRecipeCTBuilder>, EnergyPerTickRequirementCT<CustomCraftRecipeCTBuilder>, FluidRequirementCT<CustomCraftRecipeCTBuilder>, FluidPerTickRequirementCT<CustomCraftRecipeCTBuilder>, ItemRequirementCT<CustomCraftRecipeCTBuilder>, ItemTransformRequirementCT<CustomCraftRecipeCTBuilder>, DurabilityRequirementCT<CustomCraftRecipeCTBuilder>, TimeRequirementCT<CustomCraftRecipeCTBuilder>, PositionRequirementCT<CustomCraftRecipeCTBuilder>, BiomeRequirementCT<CustomCraftRecipeCTBuilder>, DimensionRequirementCT<CustomCraftRecipeCTBuilder>, FuelRequirementCT<CustomCraftRecipeCTBuilder>, CommandRequirementCT<CustomCraftRecipeCTBuilder>, EffectRequirementCT<CustomCraftRecipeCTBuilder>, WeatherRequirementCT<CustomCraftRecipeCTBuilder>, RedstoneRequirementCT<CustomCraftRecipeCTBuilder>, EntityRequirementCT<CustomCraftRecipeCTBuilder>, LightRequirementCT<CustomCraftRecipeCTBuilder>, BlockRequirementCT<CustomCraftRecipeCTBuilder>, StructureRequirementCT<CustomCraftRecipeCTBuilder>, LootTableRequirementCT<CustomCraftRecipeCTBuilder>, DropRequirementCT<CustomCraftRecipeCTBuilder>, FunctionRequirementCT<CustomCraftRecipeCTBuilder>, ButtonRequirementCT<CustomCraftRecipeCTBuilder>, SkyRequirementCT<CustomCraftRecipeCTBuilder>, ItemFilterRequirementCT<CustomCraftRecipeCTBuilder>, ExperienceRequirementCT<CustomCraftRecipeCTBuilder>, ExperiencePerTickRequirementCT<CustomCraftRecipeCTBuilder> {

    public static final Map<ResourceLocation, Integer> IDS = new HashMap();

    private final CustomCraftRecipeBuilder builder;

    private IRequirement<?> lastRequirement;

    private boolean jei = false;

    public CustomCraftRecipeCTBuilder(CustomCraftRecipeBuilder builder) {
        this.builder = builder;
    }

    @Method
    public static CustomCraftRecipeCTBuilder create(String machine, IItemStack output) {
        try {
            return new CustomCraftRecipeCTBuilder(new CustomCraftRecipeBuilder(new ResourceLocation(machine), output.getImmutableInternal()));
        } catch (ResourceLocationException var3) {
            throw new IllegalArgumentException("Invalid Machine name: " + machine + "\n" + var3.getMessage());
        }
    }

    @Method
    public void build(@OptionalString String name) {
        ResourceLocation recipeID;
        try {
            if (!name.isEmpty()) {
                if (name.contains(":")) {
                    recipeID = new ResourceLocation(name);
                } else {
                    recipeID = new ResourceLocation("crafttweaker", name);
                }
            } else {
                int uniqueID = (Integer) IDS.computeIfAbsent(this.builder.getMachine(), m -> 0);
                IDS.put(this.builder.getMachine(), uniqueID + 1);
                recipeID = new ResourceLocation("crafttweaker", "custom_craft/" + this.builder.getMachine().getNamespace() + "/" + this.builder.getMachine().getPath() + "/" + uniqueID);
            }
        } catch (ResourceLocationException var5) {
            throw new IllegalArgumentException("Invalid Recipe name: " + name + "\n" + var5.getMessage());
        }
        CustomCraftRecipe recipe = this.builder.build(recipeID);
        ActionAddRecipe<CustomCraftRecipe> action = new ActionAddRecipe(CustomCraftRecipeCTManager.INSTANCE, recipe);
        CraftTweakerAPI.apply(action);
    }

    public CustomCraftRecipeCTBuilder addRequirement(IRequirement<?> requirement) {
        this.lastRequirement = requirement;
        if (!this.jei) {
            this.builder.withRequirement(requirement);
        } else {
            this.builder.withJeiRequirement(requirement);
        }
        return this;
    }

    public CustomCraftRecipeCTBuilder error(String error, Object... args) {
        CraftTweakerAPI.getLogger("custommachinery").error(error, args);
        return this;
    }

    @Method
    public CustomCraftRecipeCTBuilder chance(double chance) {
        if (this.lastRequirement != null && this.lastRequirement instanceof IChanceableRequirement) {
            ((IChanceableRequirement) this.lastRequirement).setChance(chance);
        } else {
            CraftTweakerAPI.getLogger("custommachinery").error("Can't set chance for requirement: " + this.lastRequirement);
        }
        return this;
    }

    @Method
    public CustomCraftRecipeCTBuilder hide() {
        this.builder.hide();
        return this;
    }

    @Method
    public CustomCraftRecipeCTBuilder delay(double delay) {
        if (this.lastRequirement != null && this.lastRequirement instanceof IDelayedRequirement) {
            ((IDelayedRequirement) this.lastRequirement).setDelay(delay);
        } else {
            CraftTweakerAPI.getLogger("custommachinery").error("Can't put delay for requirement: " + this.lastRequirement);
        }
        return this;
    }

    @Method
    public CustomCraftRecipeCTBuilder jei() {
        this.jei = true;
        return this;
    }

    @Method
    public CustomCraftRecipeCTBuilder priority(int priority) {
        if (!this.jei) {
            this.builder.withPriority(priority);
        } else {
            this.builder.withJeiPriority(priority);
        }
        return this;
    }

    @Method
    public CustomCraftRecipeCTBuilder info(Consumer<DisplayInfoTemplateCT> consumer) {
        if (this.lastRequirement != null) {
            DisplayInfoTemplateCT template = new DisplayInfoTemplateCT();
            consumer.accept(template);
            this.lastRequirement.setDisplayInfoTemplate(template);
        } else {
            CraftTweakerAPI.getLogger("custommachinery").error("Can't put info for null requirement");
        }
        return this;
    }
}