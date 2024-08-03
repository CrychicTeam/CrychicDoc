package fr.frinn.custommachinery.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.integration.jei.IDisplayInfo;
import fr.frinn.custommachinery.api.integration.jei.IDisplayInfoRequirement;
import fr.frinn.custommachinery.api.requirement.IDelayedRequirement;
import fr.frinn.custommachinery.api.requirement.ITickableRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.client.ClientHandler;
import fr.frinn.custommachinery.client.render.CustomMachineRenderer;
import fr.frinn.custommachinery.common.component.StructureMachineComponent;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.network.CPlaceStructurePacket;
import fr.frinn.custommachinery.common.util.BlockStructure;
import fr.frinn.custommachinery.common.util.PartialBlockState;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import fr.frinn.custommachinery.impl.requirement.AbstractDelayedChanceableRequirement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

public class StructureRequirement extends AbstractDelayedChanceableRequirement<StructureMachineComponent> implements ITickableRequirement<StructureMachineComponent>, IDisplayInfoRequirement {

    public static final NamedCodec<StructureRequirement> CODEC = NamedCodec.record(structureRequirementInstance -> structureRequirementInstance.group(NamedCodec.STRING.listOf().listOf().fieldOf("pattern").forGetter(requirement -> requirement.pattern), NamedCodec.unboundedMap(DefaultCodecs.CHARACTER, IIngredient.BLOCK, "Map<Character, Block>").fieldOf("keys").forGetter(requirement -> requirement.keys), NamedCodec.enumCodec(StructureRequirement.Action.class).optionalFieldOf("action", StructureRequirement.Action.CHECK).forGetter(requirement -> requirement.action), NamedCodec.doubleRange(0.0, 1.0).optionalFieldOf("chance", 1.0).forGetter(AbstractDelayedChanceableRequirement::getChance), NamedCodec.doubleRange(0.0, 1.0).optionalFieldOf("delay", 0.0).forGetter(IDelayedRequirement::getDelay)).apply(structureRequirementInstance, (pattern, keys, action, chance, delay) -> {
        StructureRequirement requirement = new StructureRequirement(pattern, keys, action);
        requirement.setChance(chance);
        requirement.setDelay(delay);
        return requirement;
    }), "Structure requirement");

    private final List<List<String>> pattern;

    private final Map<Character, IIngredient<PartialBlockState>> keys;

    private final StructureRequirement.Action action;

    private final BlockStructure structure;

    public StructureRequirement(List<List<String>> pattern, Map<Character, IIngredient<PartialBlockState>> keys, StructureRequirement.Action action) {
        super(RequirementIOMode.INPUT);
        this.pattern = pattern;
        this.keys = keys;
        this.action = action;
        BlockStructure.Builder builder = BlockStructure.Builder.start();
        for (List<String> levels : pattern) {
            builder.aisle((String[]) levels.toArray(new String[0]));
        }
        for (Entry<Character, IIngredient<PartialBlockState>> key : keys.entrySet()) {
            builder.where((Character) key.getKey(), (IIngredient<PartialBlockState>) key.getValue());
        }
        this.structure = builder.build();
    }

    @Override
    public RequirementType<StructureRequirement> getType() {
        return (RequirementType<StructureRequirement>) Registration.STRUCTURE_REQUIREMENT.get();
    }

    public boolean test(StructureMachineComponent component, ICraftingContext context) {
        return switch(this.action) {
            case CHECK, DESTROY, BREAK ->
                component.checkStructure(this.structure);
            default ->
                true;
        };
    }

    public CraftingResult processStart(StructureMachineComponent component, ICraftingContext context) {
        if (this.getDelay() != 0.0) {
            return CraftingResult.pass();
        } else {
            switch(this.action) {
                case DESTROY:
                    component.destroyStructure(this.structure, false);
                    break;
                case BREAK:
                    component.destroyStructure(this.structure, true);
                    break;
                case PLACE_BREAK:
                    component.placeStructure(this.structure, true);
                    break;
                case PLACE_DESTROY:
                    component.placeStructure(this.structure, false);
            }
            return CraftingResult.success();
        }
    }

    public CraftingResult processEnd(StructureMachineComponent component, ICraftingContext context) {
        if (this.getDelay() != 1.0) {
            return CraftingResult.pass();
        } else {
            switch(this.action) {
                case DESTROY:
                    component.destroyStructure(this.structure, false);
                    break;
                case BREAK:
                    component.destroyStructure(this.structure, true);
                    break;
                case PLACE_BREAK:
                    component.placeStructure(this.structure, true);
                    break;
                case PLACE_DESTROY:
                    component.placeStructure(this.structure, false);
            }
            return CraftingResult.success();
        }
    }

    @Override
    public MachineComponentType<StructureMachineComponent> getComponentType() {
        return (MachineComponentType<StructureMachineComponent>) Registration.STRUCTURE_MACHINE_COMPONENT.get();
    }

    public CraftingResult processTick(StructureMachineComponent component, ICraftingContext context) {
        if (this.action != StructureRequirement.Action.PLACE_BREAK && this.action != StructureRequirement.Action.PLACE_DESTROY) {
            if (this.action != StructureRequirement.Action.CHECK && this.getDelay() < context.getRemainingTime() / (double) context.getRecipe().getRecipeTime()) {
                return CraftingResult.pass();
            } else {
                return component.checkStructure(this.structure) ? CraftingResult.success() : CraftingResult.error(Component.translatable("custommachinery.requirements.structure.error"));
            }
        } else {
            return CraftingResult.pass();
        }
    }

    public CraftingResult execute(StructureMachineComponent component, ICraftingContext context) {
        if (this.getDelay() != 0.0 && this.getDelay() != 1.0 && this.action != StructureRequirement.Action.CHECK) {
            switch(this.action) {
                case DESTROY:
                    component.destroyStructure(this.structure, false);
                    break;
                case BREAK:
                    component.destroyStructure(this.structure, true);
                    break;
                case PLACE_BREAK:
                    component.placeStructure(this.structure, true);
                    break;
                case PLACE_DESTROY:
                    component.placeStructure(this.structure, false);
            }
            return CraftingResult.success();
        } else {
            return CraftingResult.pass();
        }
    }

    @Override
    public void getDisplayInfo(IDisplayInfo info) {
        info.addTooltip(Component.translatable("custommachinery.requirements.structure.info"));
        info.addTooltip(Component.translatable("custommachinery.requirements.structure.click"));
        ((Map) this.pattern.stream().flatMap(Collection::stream).flatMap(s -> s.chars().mapToObj(c -> (char) c)).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))).forEach((key, amount) -> {
            IIngredient<PartialBlockState> ingredient = (IIngredient<PartialBlockState>) this.keys.get(key);
            if (ingredient != null && amount > 0L) {
                info.addTooltip(Component.translatable("custommachinery.requirements.structure.list", amount, Utils.getBlockName(ingredient).withStyle(ChatFormatting.GOLD)));
            }
        });
        switch(this.action) {
            case DESTROY:
                info.addTooltip(Component.translatable("custommachinery.requirements.structure.destroy").withStyle(ChatFormatting.DARK_RED));
                break;
            case BREAK:
                info.addTooltip(Component.translatable("custommachinery.requirements.structure.break").withStyle(ChatFormatting.DARK_RED));
                break;
            case PLACE_BREAK:
            case PLACE_DESTROY:
                info.addTooltip(Component.translatable("custommachinery.requirements.structure.place").withStyle(ChatFormatting.DARK_RED));
        }
        info.addTooltip(Component.translatable("custommachinery.requirements.structure.creative").withStyle(ChatFormatting.DARK_PURPLE), IDisplayInfo.TooltipPredicate.CREATIVE);
        info.setClickAction((machine, recipe, mouseButton) -> {
            if (ClientHandler.isShiftKeyDown()) {
                new CPlaceStructurePacket(machine.getId(), this.pattern, this.keys).sendToServer();
            } else {
                CustomMachineRenderer.addRenderBlock(machine.getId(), this.structure::getBlocks);
            }
        });
        info.setItemIcon(Items.STRUCTURE_BLOCK);
    }

    public static enum Action {

        CHECK, DESTROY, BREAK, PLACE_BREAK, PLACE_DESTROY
    }
}