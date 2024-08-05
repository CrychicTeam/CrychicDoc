package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.integration.kubejs.KubeJSIntegration;
import fr.frinn.custommachinery.common.requirement.DurabilityRequirement;
import fr.frinn.custommachinery.common.util.ingredient.ItemIngredient;
import fr.frinn.custommachinery.common.util.ingredient.ItemTagIngredient;
import java.util.Map;
import net.minecraft.world.item.ItemStack;

public interface DurabilityRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder damageItem(ItemStack stack, int amount) {
        return this.damageItem(stack, amount, "");
    }

    default RecipeJSBuilder damageItem(ItemStack stack, int amount, String slot) {
        return this.addRequirement(new DurabilityRequirement(RequirementIOMode.INPUT, new ItemIngredient(stack.getItem()), amount, KubeJSIntegration.nbtFromStack(stack), true, slot));
    }

    default RecipeJSBuilder damageItemNoBreak(ItemStack stack, int amount) {
        return this.damageItem(stack, amount, "");
    }

    default RecipeJSBuilder damageItemNoBreak(ItemStack stack, int amount, String slot) {
        return this.addRequirement(new DurabilityRequirement(RequirementIOMode.INPUT, new ItemIngredient(stack.getItem()), amount, KubeJSIntegration.nbtFromStack(stack), false, slot));
    }

    default RecipeJSBuilder damageItemTag(String tag, int amount) {
        return this.damageItemTag(tag, amount, null, "");
    }

    default RecipeJSBuilder damageItemTag(String tag, int amount, Object thing) {
        return thing instanceof String ? this.damageItemTag(tag, amount, null, (String) thing) : this.damageItemTag(tag, amount, MapJS.of(thing), "");
    }

    default RecipeJSBuilder damageItemTag(String tag, int amount, Map<?, ?> nbt, String slot) {
        try {
            return this.addRequirement(new DurabilityRequirement(RequirementIOMode.INPUT, ItemTagIngredient.create(tag), amount, nbt == null ? null : NBTUtils.toTagCompound(nbt), true, slot));
        } catch (IllegalArgumentException var6) {
            return this.error(var6.getMessage(), new Object[0]);
        }
    }

    default RecipeJSBuilder damageItemTagNoBreak(String tag, int amount) {
        return this.damageItemTagNoBreak(tag, amount, null, "");
    }

    default RecipeJSBuilder damageItemTagNoBreak(String tag, int amount, Object thing) {
        return thing instanceof String ? this.damageItemTagNoBreak(tag, amount, null, (String) thing) : this.damageItemTagNoBreak(tag, amount, MapJS.of(thing), "");
    }

    default RecipeJSBuilder damageItemTagNoBreak(String tag, int amount, Map<?, ?> nbt, String slot) {
        try {
            return this.addRequirement(new DurabilityRequirement(RequirementIOMode.INPUT, ItemTagIngredient.create(tag), amount, nbt == null ? null : NBTUtils.toTagCompound(nbt), false, slot));
        } catch (IllegalArgumentException var6) {
            return this.error(var6.getMessage(), new Object[0]);
        }
    }

    default RecipeJSBuilder repairItem(ItemStack stack, int amount) {
        return this.repairItem(stack, amount, "");
    }

    default RecipeJSBuilder repairItem(ItemStack stack, int amount, String slot) {
        return this.addRequirement(new DurabilityRequirement(RequirementIOMode.OUTPUT, new ItemIngredient(stack.getItem()), amount, KubeJSIntegration.nbtFromStack(stack), false, slot));
    }

    default RecipeJSBuilder repairItemTag(String tag, int amount) {
        return this.repairItemTag(tag, amount, null, "");
    }

    default RecipeJSBuilder repairItemTag(String tag, int amount, Object thing) {
        return thing instanceof String ? this.repairItemTag(tag, amount, null, (String) thing) : this.repairItemTag(tag, amount, MapJS.of(thing), "");
    }

    default RecipeJSBuilder repairItemTag(String tag, int amount, Map<?, ?> nbt, String slot) {
        try {
            return this.addRequirement(new DurabilityRequirement(RequirementIOMode.OUTPUT, ItemTagIngredient.create(tag), amount, nbt == null ? null : NBTUtils.toTagCompound(nbt), false, slot));
        } catch (IllegalArgumentException var6) {
            return this.error(var6.getMessage(), new Object[0]);
        }
    }
}