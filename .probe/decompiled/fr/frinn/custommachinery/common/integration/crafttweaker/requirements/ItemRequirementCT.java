package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.data.IData;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.tag.MCTag;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.integration.crafttweaker.CTUtils;
import fr.frinn.custommachinery.common.requirement.ItemRequirement;
import fr.frinn.custommachinery.common.util.ingredient.ItemIngredient;
import fr.frinn.custommachinery.common.util.ingredient.ItemTagIngredient;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;
import org.openzen.zencode.java.ZenCodeType.Optional;
import org.openzen.zencode.java.ZenCodeType.OptionalInt;
import org.openzen.zencode.java.ZenCodeType.OptionalString;

@ZenRegister
@Name("mods.custommachinery.requirement.Item")
public interface ItemRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T requireItem(IItemStack stack, @OptionalString String slot) {
        return this.addRequirement(new ItemRequirement(RequirementIOMode.INPUT, new ItemIngredient(stack.getDefinition()), stack.getAmount(), CTUtils.nbtFromStack(stack), slot));
    }

    @Method
    default T requireItemTag(MCTag tag, @OptionalInt(1) int amount, @Optional IData data, @OptionalString String slot) {
        try {
            return this.addRequirement(new ItemRequirement(RequirementIOMode.INPUT, ItemTagIngredient.create(tag.getTagKey()), amount, CTUtils.getNBT(data), slot));
        } catch (IllegalArgumentException var6) {
            return this.error(var6.getMessage(), new Object[0]);
        }
    }

    @Method
    default T produceItem(IItemStack stack, @OptionalString String slot) {
        return this.addRequirement(new ItemRequirement(RequirementIOMode.OUTPUT, new ItemIngredient(stack.getDefinition()), stack.getAmount(), CTUtils.nbtFromStack(stack), slot));
    }
}