package fr.frinn.custommachinery.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.integration.jei.IDisplayInfo;
import fr.frinn.custommachinery.api.integration.jei.IDisplayInfoRequirement;
import fr.frinn.custommachinery.api.requirement.IDelayedRequirement;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import fr.frinn.custommachinery.api.requirement.ITickableRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.common.component.DropMachineComponent;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import fr.frinn.custommachinery.impl.codec.RegistrarCodec;
import fr.frinn.custommachinery.impl.requirement.AbstractDelayedChanceableRequirement;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class DropRequirement extends AbstractDelayedChanceableRequirement<DropMachineComponent> implements ITickableRequirement<DropMachineComponent>, IDisplayInfoRequirement {

    public static final NamedCodec<DropRequirement> CODEC = NamedCodec.record(dropRequirementInstance -> dropRequirementInstance.group(RequirementIOMode.CODEC.fieldOf("mode").forGetter(IRequirement::getMode), DropRequirement.Action.CODEC.fieldOf("action").forGetter(requirement -> requirement.action), IIngredient.ITEM.listOf().optionalFieldOf("input", Collections.emptyList()).forGetter(requirement -> requirement.input), NamedCodec.BOOL.optionalFieldOf("whitelist", true).forGetter(requirement -> requirement.whitelist), RegistrarCodec.ITEM.optionalFieldOf("output", Items.AIR).forGetter(requirement -> requirement.output), DefaultCodecs.COMPOUND_TAG.optionalFieldOf("nbt").forGetter(requirement -> Optional.ofNullable(requirement.nbt)), NamedCodec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("amount", 1).forGetter(requirement -> requirement.amount), NamedCodec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("radius", 1).forGetter(requirement -> requirement.radius), NamedCodec.doubleRange(0.0, 1.0).optionalFieldOf("chance", 1.0).forGetter(AbstractDelayedChanceableRequirement::getChance), NamedCodec.doubleRange(0.0, 1.0).optionalFieldOf("delay", 0.0).forGetter(IDelayedRequirement::getDelay)).apply(dropRequirementInstance, (mode, action, input, whitelist, output, nbt, amount, radius, chance, delay) -> {
        DropRequirement requirement = new DropRequirement(mode, action, input, whitelist, output, (CompoundTag) nbt.orElse(null), amount, radius);
        requirement.setChance(chance);
        requirement.setDelay(delay);
        return requirement;
    }), "Drop requirement");

    private final DropRequirement.Action action;

    private final List<IIngredient<Item>> input;

    private final boolean whitelist;

    private final Item output;

    @Nullable
    private final CompoundTag nbt;

    private final int amount;

    private final int radius;

    public DropRequirement(RequirementIOMode mode, DropRequirement.Action action, List<IIngredient<Item>> input, boolean whitelist, Item output, @Nullable CompoundTag nbt, int amount, int radius) {
        super(mode);
        this.action = action;
        if ((action == DropRequirement.Action.CHECK || action == DropRequirement.Action.CONSUME) && input.isEmpty()) {
            throw new IllegalArgumentException("Drop requirement in" + action + "  mode MUST have at least one input item ingredient !");
        } else {
            this.input = input;
            this.whitelist = whitelist;
            if (action == DropRequirement.Action.PRODUCE && output == Items.AIR) {
                throw new IllegalArgumentException("Drop requirement in " + action + " mode MUST have an output item specified !");
            } else {
                this.output = output;
                this.nbt = nbt;
                this.amount = amount;
                this.radius = radius;
            }
        }
    }

    @Override
    public RequirementType<DropRequirement> getType() {
        return (RequirementType<DropRequirement>) Registration.DROP_REQUIREMENT.get();
    }

    public boolean test(DropMachineComponent component, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        return this.action != DropRequirement.Action.CHECK && this.action != DropRequirement.Action.CONSUME ? true : component.getItemAmount(this.input, this.radius, this.whitelist) >= amount;
    }

    public CraftingResult processStart(DropMachineComponent component, ICraftingContext context) {
        if (this.getDelay() == 0.0 && this.getMode() == RequirementIOMode.INPUT) {
            int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
            int radius = (int) context.getModifiedValue((double) this.radius, this, "radius");
            switch(this.action) {
                case CONSUME:
                    int found = component.getItemAmount(this.input, radius, this.whitelist);
                    if (found >= amount) {
                        component.consumeItem(this.input, amount, radius, this.whitelist);
                        return CraftingResult.success();
                    }
                    return CraftingResult.error(Component.translatable("custommachinery.requirements.drop.error.input", amount, found));
                case PRODUCE:
                    ItemStack stack = Utils.makeItemStack(this.output, amount, this.nbt);
                    if (component.produceItem(stack)) {
                        return CraftingResult.success();
                    }
                    return CraftingResult.error(Component.translatable("custommachinery.requirements.drop.error.input", Component.literal(amount + "x").append(Component.translatable(this.output.getDescriptionId(stack)))));
                default:
                    return CraftingResult.pass();
            }
        } else {
            return CraftingResult.pass();
        }
    }

    public CraftingResult processEnd(DropMachineComponent component, ICraftingContext context) {
        if (this.getDelay() == 0.0 && this.getMode() == RequirementIOMode.OUTPUT) {
            int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
            int radius = (int) context.getModifiedValue((double) this.radius, this, "radius");
            switch(this.action) {
                case CONSUME:
                    int found = component.getItemAmount(this.input, radius, this.whitelist);
                    if (found > amount) {
                        component.consumeItem(this.input, amount, radius, this.whitelist);
                        return CraftingResult.success();
                    }
                    return CraftingResult.error(Component.translatable("custommachinery.requirements.drop.error.input", amount, found));
                case PRODUCE:
                    ItemStack stack = Utils.makeItemStack(this.output, amount, this.nbt);
                    if (component.produceItem(stack)) {
                        return CraftingResult.success();
                    }
                    return CraftingResult.error(Component.translatable("custommachinery.requirements.drop.error.input", Component.literal(amount + "x").append(Component.translatable(this.output.getDescriptionId(stack)))));
                default:
                    return CraftingResult.pass();
            }
        } else {
            return CraftingResult.pass();
        }
    }

    @Override
    public MachineComponentType<DropMachineComponent> getComponentType() {
        return (MachineComponentType<DropMachineComponent>) Registration.DROP_MACHINE_COMPONENT.get();
    }

    public CraftingResult processTick(DropMachineComponent component, ICraftingContext context) {
        if (this.action == DropRequirement.Action.CHECK) {
            int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
            int radius = (int) context.getModifiedValue((double) this.radius, this, "radius");
            int found = component.getItemAmount(this.input, radius, this.whitelist);
            return found >= amount ? CraftingResult.success() : CraftingResult.error(Component.translatable("custommachinery.requirements.drop.error.input", amount, found));
        } else {
            return CraftingResult.pass();
        }
    }

    public CraftingResult execute(DropMachineComponent component, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        int radius = (int) context.getModifiedValue((double) this.radius, this, "radius");
        switch(this.action) {
            case CONSUME:
                int found = component.getItemAmount(this.input, radius, this.whitelist);
                if (found > amount) {
                    component.consumeItem(this.input, amount, radius, this.whitelist);
                    return CraftingResult.success();
                }
                return CraftingResult.error(Component.translatable("custommachinery.requirements.drop.error.input", amount, found));
            case PRODUCE:
                ItemStack stack = Utils.makeItemStack(this.output, amount, this.nbt);
                if (component.produceItem(stack)) {
                    return CraftingResult.success();
                }
                return CraftingResult.error(Component.translatable("custommachinery.requirements.drop.error.output", Component.literal(amount + "x").append(Component.translatable(this.output.getDescriptionId(stack)))));
            default:
                return CraftingResult.pass();
        }
    }

    @Override
    public void getDisplayInfo(IDisplayInfo info) {
        switch(this.action) {
            case CONSUME:
                info.addTooltip(Component.translatable("custommachinery.requirements.drop.info.consume", this.amount, this.radius));
                info.addTooltip(Component.translatable("custommachinery.requirements.drop.info." + (this.whitelist ? "whitelist" : "blacklist")).withStyle(this.whitelist ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_RED));
                this.input.forEach(ingredient -> info.addTooltip(Component.literal("- " + ingredient.toString())));
                info.setItemIcon(Items.HOPPER);
                break;
            case PRODUCE:
                info.addTooltip(Component.translatable("custommachinery.requirements.drop.info.produce", Component.literal(this.amount + "x ").append(Component.translatable(this.output.getDescriptionId())).withStyle(ChatFormatting.GOLD)));
                info.setItemIcon(Items.DROPPER);
                break;
            case CHECK:
                info.addTooltip(Component.translatable("custommachinery.requirements.drop.info.check", this.amount, this.radius));
                info.addTooltip(Component.translatable("custommachinery.requirements.drop.info." + (this.whitelist ? "whitelist" : "blacklist")).withStyle(this.whitelist ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_RED));
                this.input.forEach(ingredient -> info.addTooltip(Component.literal(ingredient.toString())));
                info.setItemIcon(Items.OAK_PRESSURE_PLATE);
        }
    }

    public static enum Action {

        CHECK, CONSUME, PRODUCE;

        public static final NamedCodec<DropRequirement.Action> CODEC = NamedCodec.enumCodec(DropRequirement.Action.class);

        public static DropRequirement.Action value(String mode) {
            return valueOf(mode.toUpperCase(Locale.ENGLISH));
        }
    }
}