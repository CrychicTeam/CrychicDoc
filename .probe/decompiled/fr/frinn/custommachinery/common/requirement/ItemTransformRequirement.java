package fr.frinn.custommachinery.common.requirement;

import com.google.common.collect.Lists;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientRequirement;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.client.integration.jei.wrapper.ItemIngredientWrapper;
import fr.frinn.custommachinery.common.component.handler.ItemComponentHandler;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import fr.frinn.custommachinery.common.util.ingredient.ItemIngredient;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import fr.frinn.custommachinery.impl.codec.RegistrarCodec;
import fr.frinn.custommachinery.impl.requirement.AbstractChanceableRequirement;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ItemTransformRequirement extends AbstractChanceableRequirement<ItemComponentHandler> implements IJEIIngredientRequirement<ItemStack> {

    public static final NamedCodec<ItemTransformRequirement> CODEC = NamedCodec.record(itemTransformRequirementInstance -> itemTransformRequirementInstance.group(IIngredient.ITEM.fieldOf("input").forGetter(requirement -> requirement.input), NamedCodec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("input_amount", 1).forGetter(requirement -> requirement.inputAmount), NamedCodec.STRING.optionalFieldOf("input_slot", "").forGetter(requirement -> requirement.inputSlot), DefaultCodecs.COMPOUND_TAG.optionalFieldOf("input_nbt").forGetter(requirement -> Optional.ofNullable(requirement.inputNBT)), RegistrarCodec.ITEM.optionalFieldOf("output", Items.AIR).forGetter(requirement -> requirement.output), NamedCodec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("output_amount", 1).forGetter(requirement -> requirement.outputAmount), NamedCodec.STRING.optionalFieldOf("output_slot", "").forGetter(requirement -> requirement.outputSlot), NamedCodec.BOOL.optionalFieldOf("copy_nbt", true).forGetter(requirement -> requirement.copyNBT), NamedCodec.doubleRange(0.0, 1.0).optionalFieldOf("chance", 1.0).forGetter(AbstractChanceableRequirement::getChance)).apply(itemTransformRequirementInstance, (input, inputAmount, inputSlot, inputNBT, output, outputAmount, outputSlot, copyNBT, chance) -> {
        ItemTransformRequirement requirement = new ItemTransformRequirement(input, inputAmount, inputSlot, (CompoundTag) inputNBT.orElse(null), output, outputAmount, outputSlot, copyNBT, null);
        requirement.setChance(chance);
        return requirement;
    }), "Item transform requirement");

    private final IIngredient<Item> input;

    private final int inputAmount;

    private final String inputSlot;

    @Nullable
    private final CompoundTag inputNBT;

    private final Item output;

    private final int outputAmount;

    private final String outputSlot;

    private final boolean copyNBT;

    @Nullable
    private final Function<CompoundTag, CompoundTag> nbt;

    public ItemTransformRequirement(IIngredient<Item> input, int inputAmount, String inputSlot, @Nullable CompoundTag inputNBT, Item output, int outputAmount, String outputSlot, boolean copyNBT, @Nullable Function<CompoundTag, CompoundTag> nbt) {
        super(RequirementIOMode.OUTPUT);
        this.input = input;
        this.inputAmount = inputAmount;
        this.inputSlot = inputSlot;
        this.inputNBT = inputNBT;
        this.output = output;
        this.outputAmount = outputAmount;
        this.outputSlot = outputSlot;
        this.copyNBT = copyNBT;
        this.nbt = nbt;
    }

    @Override
    public RequirementType<ItemTransformRequirement> getType() {
        return (RequirementType<ItemTransformRequirement>) Registration.ITEM_TRANSFORM_REQUIREMENT.get();
    }

    @Override
    public MachineComponentType getComponentType() {
        return (MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get();
    }

    public boolean test(ItemComponentHandler component, ICraftingContext context) {
        return this.input.getAll().stream().anyMatch(item -> {
            if (component.getItemAmount(this.inputSlot, item, this.inputNBT) < this.inputAmount) {
                return false;
            } else {
                CompoundTag inputNBT = (CompoundTag) component.getComponents().stream().filter(slot -> slot.getItemStack().getItem() == item).findFirst().map(slot -> slot.getItemStack().getTag()).map(CompoundTag::m_6426_).orElse(null);
                CompoundTag outputNBT = null;
                if (this.nbt != null) {
                    outputNBT = (CompoundTag) this.nbt.apply(inputNBT);
                } else if (this.copyNBT && inputNBT != null) {
                    outputNBT = inputNBT;
                }
                return component.getSpaceForItem(this.outputSlot, this.output == Items.AIR ? item : this.output, outputNBT) >= this.outputAmount;
            }
        });
    }

    public CraftingResult processStart(ItemComponentHandler component, ICraftingContext context) {
        return CraftingResult.pass();
    }

    public CraftingResult processEnd(ItemComponentHandler component, ICraftingContext context) {
        for (Item item : this.input.getAll()) {
            if (component.getItemAmount(this.inputSlot, item, this.inputNBT) >= this.inputAmount) {
                CompoundTag inputNBT = (CompoundTag) component.getComponents().stream().filter(slot -> slot.getItemStack().getItem() == item).findFirst().map(slot -> slot.getItemStack().getTag()).map(CompoundTag::m_6426_).orElse(null);
                CompoundTag outputNBT = null;
                if (this.nbt != null) {
                    outputNBT = (CompoundTag) this.nbt.apply(inputNBT);
                } else if (this.copyNBT && inputNBT != null) {
                    outputNBT = inputNBT;
                }
                if (component.getSpaceForItem(this.outputSlot, this.output == Items.AIR ? item : this.output, outputNBT) >= this.outputAmount) {
                    component.removeFromInputs(this.inputSlot, item, this.inputAmount, null);
                    component.addToOutputs(this.outputSlot, this.output == Items.AIR ? item : this.output, this.outputAmount, outputNBT);
                    return CraftingResult.success();
                }
            }
        }
        return CraftingResult.error(Component.translatable("custommachinery.requirements.item_transform.error", this.input.toString(), this.inputAmount));
    }

    @Override
    public List<IJEIIngredientWrapper<ItemStack>> getJEIIngredientWrappers(IMachineRecipe recipe) {
        CompoundTag outputNBT = null;
        if (this.nbt != null) {
            outputNBT = (CompoundTag) this.nbt.apply(this.inputNBT == null ? null : this.inputNBT.copy());
        } else if (this.copyNBT && this.inputNBT != null) {
            outputNBT = this.inputNBT;
        }
        return Lists.newArrayList(new IJEIIngredientWrapper[] { new ItemIngredientWrapper(RequirementIOMode.INPUT, this.input, this.inputAmount, this.getChance(), false, this.inputNBT, this.inputSlot, true), new ItemIngredientWrapper(RequirementIOMode.OUTPUT, (IIngredient<Item>) (this.output == Items.AIR ? this.input : new ItemIngredient(this.output)), this.outputAmount, this.getChance(), false, outputNBT, this.outputSlot, true) });
    }
}