package fr.frinn.custommachinery.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.api.upgrade.IRecipeModifier;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.upgrade.MachineUpgrade;
import fr.frinn.custommachinery.common.upgrade.RecipeModifier;
import fr.frinn.custommachinery.common.upgrade.modifier.AdditionRecipeModifier;
import fr.frinn.custommachinery.common.upgrade.modifier.ExponentialRecipeModifier;
import fr.frinn.custommachinery.common.upgrade.modifier.MultiplicationRecipeModifier;
import fr.frinn.custommachinery.common.upgrade.modifier.SpeedRecipeModifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;
import org.openzen.zencode.java.ZenCodeType.OptionalInt;

@ZenRegister
@Name("mods.custommachinery.CMUpgradeBuilder")
public class CustomMachineUpgradeCTBuilder {

    private final Item item;

    private List<Component> tooltips;

    private final List<ResourceLocation> machines;

    private final List<RecipeModifier> modifiers;

    private final int maxAmount;

    public CustomMachineUpgradeCTBuilder(Item item, int maxAmount) {
        this.item = item;
        this.tooltips = Collections.singletonList(Component.translatable("custommachinery.upgrade.tooltip").withStyle(ChatFormatting.AQUA));
        this.maxAmount = maxAmount;
        this.machines = new ArrayList();
        this.modifiers = new ArrayList();
    }

    @Method
    public static CustomMachineUpgradeCTBuilder create(Item item, @OptionalInt(64) int maxAmount) {
        return new CustomMachineUpgradeCTBuilder(item, maxAmount);
    }

    @Method
    public void build() {
        if (this.machines.isEmpty()) {
            throw new IllegalArgumentException("You must specify at least 1 machine for machine upgrade item: " + BuiltInRegistries.ITEM.getKey(this.item));
        } else if (this.modifiers.isEmpty()) {
            throw new IllegalArgumentException("You must specify at least 1 recipe modifier for machine upgrade item: " + BuiltInRegistries.ITEM.getKey(this.item));
        } else {
            MachineUpgrade upgrade = new MachineUpgrade(this.item, this.machines, this.modifiers, this.tooltips, this.maxAmount);
            CraftTweakerAPI.apply(new CustomMachineUpgradeCTBuilder.AddMachineUpgradeAction(upgrade));
        }
    }

    @Method
    public CustomMachineUpgradeCTBuilder machine(String... string) {
        for (String s : string) {
            ResourceLocation machine;
            try {
                machine = new ResourceLocation(s);
            } catch (ResourceLocationException var8) {
                throw new IllegalArgumentException("Invalid Machine ID: " + s + "\n" + var8.getMessage());
            }
            this.machines.add(machine);
        }
        return this;
    }

    @Method
    public CustomMachineUpgradeCTBuilder tooltip(String... strings) {
        Builder<Component> tooltips = ImmutableList.builder();
        for (String tooltip : strings) {
            try {
                Component component = Component.Serializer.fromJson(tooltip);
                if (component == null) {
                    throw new IllegalArgumentException("");
                }
                tooltips.add(component);
            } catch (Exception var8) {
                tooltips.add(Component.translatable(tooltip));
            }
        }
        this.tooltips = tooltips.build();
        return this;
    }

    @Method
    public CustomMachineUpgradeCTBuilder tooltip(Component... components) {
        this.tooltips = ImmutableList.copyOf(components);
        return this;
    }

    @Method
    public CustomMachineUpgradeCTBuilder modifier(CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder builder) {
        this.modifiers.add(builder.build());
        return this;
    }

    public static class AddMachineUpgradeAction implements IRuntimeAction {

        private final MachineUpgrade upgrade;

        public AddMachineUpgradeAction(MachineUpgrade upgrade) {
            this.upgrade = upgrade;
        }

        public void apply() {
            CustomMachinery.UPGRADES.addUpgrade(this.upgrade);
        }

        public String describe() {
            return "Add a custom machine upgrade for item: " + BuiltInRegistries.ITEM.getKey(this.upgrade.getItem());
        }

        public String systemName() {
            return "custommachinery";
        }
    }

    @ZenRegister
    @Name("mods.custommachinery.CMRecipeModifierBuilder")
    public static class CTRecipeModifierBuilder {

        private final RequirementType<?> requirement;

        private final RequirementIOMode mode;

        private final IRecipeModifier.OPERATION operation;

        private final double modifier;

        private String target = "";

        private double chance = 1.0;

        private double max = Double.POSITIVE_INFINITY;

        private double min = Double.NEGATIVE_INFINITY;

        private Component tooltip = null;

        private CTRecipeModifierBuilder(RequirementType<?> type, RequirementIOMode mode, IRecipeModifier.OPERATION operation, double modifier) {
            this.requirement = type;
            this.mode = mode;
            this.operation = operation;
            this.modifier = modifier;
        }

        @Method
        public static CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder addInput(RequirementTypeCTBrackets.CTRequirementType type, double modifier) {
            return new CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder(type.getType(), RequirementIOMode.INPUT, IRecipeModifier.OPERATION.ADDITION, modifier);
        }

        @Method
        public static CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder mulInput(RequirementTypeCTBrackets.CTRequirementType type, double modifier) {
            return new CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder(type.getType(), RequirementIOMode.INPUT, IRecipeModifier.OPERATION.MULTIPLICATION, modifier);
        }

        @Method
        public static CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder expInput(RequirementTypeCTBrackets.CTRequirementType type, double modifier) {
            return new CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder(type.getType(), RequirementIOMode.INPUT, IRecipeModifier.OPERATION.EXPONENTIAL, modifier);
        }

        @Method
        public static CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder addOutput(RequirementTypeCTBrackets.CTRequirementType type, double modifier) {
            return new CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder(type.getType(), RequirementIOMode.OUTPUT, IRecipeModifier.OPERATION.ADDITION, modifier);
        }

        @Method
        public static CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder mulOutput(RequirementTypeCTBrackets.CTRequirementType type, double modifier) {
            return new CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder(type.getType(), RequirementIOMode.OUTPUT, IRecipeModifier.OPERATION.MULTIPLICATION, modifier);
        }

        @Method
        public static CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder expOutput(RequirementTypeCTBrackets.CTRequirementType type, double modifier) {
            return new CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder(type.getType(), RequirementIOMode.OUTPUT, IRecipeModifier.OPERATION.EXPONENTIAL, modifier);
        }

        @Method
        public CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder target(String target) {
            this.target = target;
            return this;
        }

        @Method
        public CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder chance(double chance) {
            this.chance = chance;
            return this;
        }

        @Method
        public CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder max(double max) {
            this.max = max;
            return this;
        }

        @Method
        public CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder min(double min) {
            this.min = min;
            return this;
        }

        @Method
        public CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder tooltip(String tooltip) {
            try {
                this.tooltip = Component.Serializer.fromJson(tooltip);
            } catch (Exception var3) {
                this.tooltip = Component.translatable(tooltip);
            }
            return this;
        }

        @Method
        public CustomMachineUpgradeCTBuilder.CTRecipeModifierBuilder tooltip(Component tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        private RecipeModifier build() {
            if (this.requirement == Registration.SPEED_REQUIREMENT.get()) {
                return new SpeedRecipeModifier(this.operation, this.modifier, this.chance, this.max, this.min, this.tooltip);
            } else {
                return (RecipeModifier) (switch(this.operation) {
                    case ADDITION ->
                        new AdditionRecipeModifier(this.requirement, this.mode, this.modifier, this.target, this.chance, this.max, this.min, this.tooltip);
                    case MULTIPLICATION ->
                        new MultiplicationRecipeModifier(this.requirement, this.mode, this.modifier, this.target, this.chance, this.max, this.min, this.tooltip);
                    case EXPONENTIAL ->
                        new ExponentialRecipeModifier(this.requirement, this.mode, this.modifier, this.target, this.chance, this.max, this.min, this.tooltip);
                });
            }
        }
    }
}