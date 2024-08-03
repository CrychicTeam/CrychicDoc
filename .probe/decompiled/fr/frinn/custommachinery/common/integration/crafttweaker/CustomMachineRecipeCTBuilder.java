package fr.frinn.custommachinery.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.data.MapData;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.requirement.IChanceableRequirement;
import fr.frinn.custommachinery.api.requirement.IDelayedRequirement;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import fr.frinn.custommachinery.common.crafting.machine.CustomMachineRecipe;
import fr.frinn.custommachinery.common.crafting.machine.CustomMachineRecipeBuilder;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.BiomeRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.BlockRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.ButtonRequirementCT;
import fr.frinn.custommachinery.common.integration.crafttweaker.requirements.ChunkloadRequirementCT;
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
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;
import org.openzen.zencode.java.ZenCodeType.OptionalString;

@ZenRegister
@Name("mods.custommachinery.CMRecipeBuilder")
public class CustomMachineRecipeCTBuilder implements EnergyRequirementCT<CustomMachineRecipeCTBuilder>, EnergyPerTickRequirementCT<CustomMachineRecipeCTBuilder>, FluidRequirementCT<CustomMachineRecipeCTBuilder>, FluidPerTickRequirementCT<CustomMachineRecipeCTBuilder>, ItemRequirementCT<CustomMachineRecipeCTBuilder>, ItemTransformRequirementCT<CustomMachineRecipeCTBuilder>, DurabilityRequirementCT<CustomMachineRecipeCTBuilder>, TimeRequirementCT<CustomMachineRecipeCTBuilder>, PositionRequirementCT<CustomMachineRecipeCTBuilder>, BiomeRequirementCT<CustomMachineRecipeCTBuilder>, DimensionRequirementCT<CustomMachineRecipeCTBuilder>, FuelRequirementCT<CustomMachineRecipeCTBuilder>, CommandRequirementCT<CustomMachineRecipeCTBuilder>, EffectRequirementCT<CustomMachineRecipeCTBuilder>, WeatherRequirementCT<CustomMachineRecipeCTBuilder>, RedstoneRequirementCT<CustomMachineRecipeCTBuilder>, EntityRequirementCT<CustomMachineRecipeCTBuilder>, LightRequirementCT<CustomMachineRecipeCTBuilder>, BlockRequirementCT<CustomMachineRecipeCTBuilder>, StructureRequirementCT<CustomMachineRecipeCTBuilder>, LootTableRequirementCT<CustomMachineRecipeCTBuilder>, DropRequirementCT<CustomMachineRecipeCTBuilder>, FunctionRequirementCT<CustomMachineRecipeCTBuilder>, ButtonRequirementCT<CustomMachineRecipeCTBuilder>, SkyRequirementCT<CustomMachineRecipeCTBuilder>, ItemFilterRequirementCT<CustomMachineRecipeCTBuilder>, ExperienceRequirementCT<CustomMachineRecipeCTBuilder>, ExperiencePerTickRequirementCT<CustomMachineRecipeCTBuilder>, ChunkloadRequirementCT<CustomMachineRecipeCTBuilder> {

    public static final Map<ResourceLocation, Integer> IDS = new HashMap();

    private final CustomMachineRecipeBuilder builder;

    private IRequirement<?> lastRequirement;

    private boolean jei = false;

    public CustomMachineRecipeCTBuilder(CustomMachineRecipeBuilder builder) {
        this.builder = builder;
    }

    @Method
    public static CustomMachineRecipeCTBuilder create(String machine, int time) {
        try {
            return new CustomMachineRecipeCTBuilder(new CustomMachineRecipeBuilder(new ResourceLocation(machine), time));
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
        CustomMachineRecipe recipe = this.builder.build(recipeID);
        ActionAddRecipe<CustomMachineRecipe> action = new ActionAddRecipe(CustomMachineRecipeCTManager.INSTANCE, recipe);
        CraftTweakerAPI.apply(action);
    }

    public CustomMachineRecipeCTBuilder addRequirement(IRequirement<?> requirement) {
        this.lastRequirement = requirement;
        if (!this.jei) {
            this.builder.withRequirement(requirement);
        } else {
            this.builder.withJeiRequirement(requirement);
        }
        return this;
    }

    public CustomMachineRecipeCTBuilder error(String error, Object... args) {
        CraftTweakerAPI.getLogger("custommachinery").error(error, args);
        return this;
    }

    @Method
    public CustomMachineRecipeCTBuilder chance(double chance) {
        if (this.lastRequirement != null && this.lastRequirement instanceof IChanceableRequirement) {
            ((IChanceableRequirement) this.lastRequirement).setChance(chance);
        } else {
            CraftTweakerAPI.getLogger("custommachinery").error("Can't set chance for requirement: " + this.lastRequirement);
        }
        return this;
    }

    @Method
    public CustomMachineRecipeCTBuilder hide() {
        this.builder.hide();
        return this;
    }

    @Method
    public CustomMachineRecipeCTBuilder delay(double delay) {
        if (this.lastRequirement != null && this.lastRequirement instanceof IDelayedRequirement) {
            ((IDelayedRequirement) this.lastRequirement).setDelay(delay);
        } else {
            CraftTweakerAPI.getLogger("custommachinery").error("Can't put delay for requirement: " + this.lastRequirement);
        }
        return this;
    }

    @Method
    public CustomMachineRecipeCTBuilder jei() {
        this.jei = true;
        return this;
    }

    @Method
    public CustomMachineRecipeCTBuilder priority(int priority) {
        if (!this.jei) {
            this.builder.withPriority(priority);
        } else {
            this.builder.withJeiPriority(priority);
        }
        return this;
    }

    @Method
    public CustomMachineRecipeCTBuilder resetOnError() {
        this.builder.setResetOnError();
        return this;
    }

    @Method
    public CustomMachineRecipeCTBuilder info(Consumer<DisplayInfoTemplateCT> consumer) {
        if (this.lastRequirement != null) {
            DisplayInfoTemplateCT template = new DisplayInfoTemplateCT();
            consumer.accept(template);
            this.lastRequirement.setDisplayInfoTemplate(template);
        } else {
            CraftTweakerAPI.getLogger("custommachinery").error("Can't put info for null requirement");
        }
        return this;
    }

    @Method
    public CustomMachineRecipeCTBuilder appearance(Consumer<MachineAppearanceBuilderCT> consumer) {
        MachineAppearanceBuilderCT builder = new MachineAppearanceBuilderCT();
        consumer.accept(builder);
        this.builder.withAppearance(builder.build());
        return this;
    }

    @Method
    public CustomMachineRecipeCTBuilder gui(MapData... elements) {
        for (MapData data : elements) {
            IGuiElement.CODEC.read(NbtOps.INSTANCE, data.getInternal()).resultOrPartial(s -> CraftTweakerAPI.getLogger("custommachinery").error("Error when parsing recipe custom gui element\n" + data + "\n" + s)).ifPresent(this.builder::withGuiElement);
        }
        return this;
    }
}