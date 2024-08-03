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
import fr.frinn.custommachinery.client.integration.jei.wrapper.ItemIngredientWrapper;
import fr.frinn.custommachinery.common.component.handler.ItemComponentHandler;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import fr.frinn.custommachinery.impl.requirement.AbstractChanceableRequirement;
import fr.frinn.custommachinery.impl.requirement.AbstractRequirement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class DurabilityRequirement extends AbstractChanceableRequirement<ItemComponentHandler> implements IJEIIngredientRequirement<ItemStack> {

    public static final NamedCodec<DurabilityRequirement> CODEC = NamedCodec.record(durabilityRequirementInstance -> durabilityRequirementInstance.group(RequirementIOMode.CODEC.fieldOf("mode").forGetter(AbstractRequirement::getMode), IIngredient.ITEM.fieldOf("item").forGetter(requirement -> requirement.item), NamedCodec.intRange(1, Integer.MAX_VALUE).fieldOf("amount").forGetter(requirement -> requirement.amount), DefaultCodecs.COMPOUND_TAG.optionalFieldOf("nbt").forGetter(requirement -> Optional.ofNullable(requirement.nbt)), NamedCodec.BOOL.optionalFieldOf("break", false).forGetter(requirement -> requirement.canBreak), NamedCodec.doubleRange(0.0, 1.0).optionalFieldOf("chance", 1.0).forGetter(AbstractChanceableRequirement::getChance), NamedCodec.STRING.optionalFieldOf("slot", "").forGetter(requirement -> requirement.slot)).apply(durabilityRequirementInstance, (mode, item, amount, nbt, canBreak, chance, slot) -> {
        DurabilityRequirement requirement = new DurabilityRequirement(mode, item, amount, (CompoundTag) nbt.orElse(null), canBreak, slot);
        requirement.setChance(chance);
        return requirement;
    }), "Durability requirement");

    private final IIngredient<Item> item;

    private final int amount;

    private final CompoundTag nbt;

    private final String slot;

    private final boolean canBreak;

    public DurabilityRequirement(RequirementIOMode mode, IIngredient<Item> item, int amount, @Nullable CompoundTag nbt, boolean canBreak, String slot) {
        super(mode);
        this.item = item;
        this.amount = amount;
        this.nbt = nbt;
        this.canBreak = canBreak;
        this.slot = slot;
    }

    @Override
    public RequirementType<DurabilityRequirement> getType() {
        return (RequirementType<DurabilityRequirement>) Registration.DURABILITY_REQUIREMENT.get();
    }

    public boolean test(ItemComponentHandler component, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        return this.getMode() == RequirementIOMode.INPUT ? this.item.getAll().stream().mapToInt(item -> component.getDurabilityAmount(this.slot, item, this.nbt)).sum() >= amount : this.item.getAll().stream().mapToInt(item -> component.getSpaceForDurability(this.slot, item, this.nbt)).sum() >= amount;
    }

    public CraftingResult processStart(ItemComponentHandler component, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        if (this.getMode() != RequirementIOMode.INPUT) {
            return CraftingResult.pass();
        } else {
            int maxRemove = this.item.getAll().stream().mapToInt(itemx -> component.getDurabilityAmount(this.slot, itemx, this.nbt)).sum();
            if (maxRemove >= amount) {
                int toDamage = amount;
                for (Item item : this.item.getAll()) {
                    int canDamage = component.getDurabilityAmount(this.slot, item, this.nbt);
                    if (canDamage > 0) {
                        canDamage = Math.min(canDamage, toDamage);
                        component.removeDurability(this.slot, item, canDamage, this.nbt, this.canBreak);
                        toDamage -= canDamage;
                        if (toDamage == 0) {
                            return CraftingResult.success();
                        }
                    }
                }
            }
            return CraftingResult.error(Component.translatable("custommachinery.requirements.durability.error.input", this.item, amount, maxRemove));
        }
    }

    public CraftingResult processEnd(ItemComponentHandler component, ICraftingContext context) {
        int amount = (int) context.getIntegerModifiedValue((double) this.amount, this, null);
        if (this.getMode() != RequirementIOMode.OUTPUT) {
            return CraftingResult.pass();
        } else {
            int maxRepair = this.item.getAll().stream().mapToInt(itemx -> component.getSpaceForDurability(this.slot, itemx, this.nbt)).sum();
            if (maxRepair >= amount) {
                int toRepair = amount;
                for (Item item : this.item.getAll()) {
                    int canRepair = component.getSpaceForDurability(this.slot, item, this.nbt);
                    if (canRepair > 0) {
                        canRepair = Math.min(canRepair, toRepair);
                        component.repairItem(this.slot, item, canRepair, this.nbt);
                        toRepair -= canRepair;
                        if (toRepair == 0) {
                            return CraftingResult.success();
                        }
                    }
                }
            }
            return CraftingResult.error(Component.translatable("custommachinery.requirements.durability.error.output", this.item, amount, maxRepair));
        }
    }

    @Override
    public MachineComponentType getComponentType() {
        return (MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get();
    }

    @Override
    public List<IJEIIngredientWrapper<ItemStack>> getJEIIngredientWrappers(IMachineRecipe recipe) {
        return Collections.singletonList(new ItemIngredientWrapper(this.getMode(), this.item, this.amount, this.getChance(), true, this.nbt, this.slot, true));
    }
}