package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.common.requirement.ItemTransformRequirement;
import fr.frinn.custommachinery.common.util.ingredient.ItemIngredient;
import fr.frinn.custommachinery.common.util.ingredient.ItemTagIngredient;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ItemTransformRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder transformItem(ItemStack input) {
        return this.transformItem(input, input);
    }

    default RecipeJSBuilder transformItem(ItemStack input, ItemStack output) {
        return this.transformItem(input, output, "", "");
    }

    default RecipeJSBuilder transformItem(ItemStack input, ItemStack output, String inputSlot, String outputSlot) {
        return this.transformItem(input, output, inputSlot, outputSlot, null);
    }

    default RecipeJSBuilder transformItem(ItemStack input, ItemStack output, String inputSlot, String outputSlot, Function<Map<?, ?>, Object> nbt) {
        return this.addRequirement(new ItemTransformRequirement(new ItemIngredient(input.getItem()), input.getCount(), inputSlot, input.getTag(), output.getItem(), output.getCount(), outputSlot, true, new ItemTransformRequirementJS.NbtTransformer(nbt)));
    }

    default RecipeJSBuilder transformItemTag(String tag) {
        return this.transformItemTag(tag, 1, null);
    }

    default RecipeJSBuilder transformItemTag(String tag, int inputAmount, CompoundTag inputNBT) {
        return this.transformItemTag(tag, inputAmount, inputNBT, ItemStack.EMPTY);
    }

    default RecipeJSBuilder transformItemTag(String tag, int inputAmount, CompoundTag inputNBT, ItemStack output) {
        return this.transformItemTag(tag, inputAmount, inputNBT, output, "", "");
    }

    default RecipeJSBuilder transformItemTag(String tag, int inputAmount, CompoundTag inputNBT, ItemStack output, String inputSlot, String outputSlot) {
        return this.transformItemTag(tag, inputAmount, inputNBT, output, inputSlot, outputSlot, null);
    }

    default RecipeJSBuilder transformItemTag(String tag, int inputAmount, CompoundTag inputNBT, ItemStack output, String inputSlot, String outputSlot, Function<Map<?, ?>, Object> nbt) {
        try {
            return this.addRequirement(new ItemTransformRequirement(ItemTagIngredient.create(tag), inputAmount, inputSlot, inputNBT, output.getItem(), output.getCount(), outputSlot, true, new ItemTransformRequirementJS.NbtTransformer(nbt)));
        } catch (IllegalArgumentException var9) {
            return this.error(var9.getMessage(), new Object[0]);
        }
    }

    public static record NbtTransformer(Function<Map<?, ?>, Object> function) implements Function<CompoundTag, CompoundTag> {

        @Nullable
        public CompoundTag apply(@Nullable CompoundTag compoundTag) {
            Map<?, ?> map = MapJS.of(this.function.apply(MapJS.of(compoundTag)));
            return map == null ? null : NBTUtils.toTagCompound(map);
        }
    }
}