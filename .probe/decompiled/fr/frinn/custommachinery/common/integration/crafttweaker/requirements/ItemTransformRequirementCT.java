package fr.frinn.custommachinery.common.integration.crafttweaker.requirements;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.data.IData;
import com.blamejared.crafttweaker.api.data.MapData;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.tag.MCTag;
import fr.frinn.custommachinery.api.integration.crafttweaker.RecipeCTBuilder;
import fr.frinn.custommachinery.common.integration.crafttweaker.CTUtils;
import fr.frinn.custommachinery.common.requirement.ItemTransformRequirement;
import fr.frinn.custommachinery.common.util.ingredient.ItemIngredient;
import fr.frinn.custommachinery.common.util.ingredient.ItemTagIngredient;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;
import org.openzen.zencode.java.ZenCodeType.Optional;
import org.openzen.zencode.java.ZenCodeType.OptionalInt;
import org.openzen.zencode.java.ZenCodeType.OptionalString;

@ZenRegister
@Name("mods.custommachinery.requirement.ItemTransform")
public interface ItemTransformRequirementCT<T> extends RecipeCTBuilder<T> {

    @Method
    default T transformItem(IItemStack stack, @Optional IItemStack output, @OptionalString String inputSlot, @OptionalString String outputSlot, @Optional Function<MapData, MapData> nbt) {
        Item outputItem = output == null ? Items.AIR : output.getDefinition();
        int outputAmount = output == null ? 1 : output.getAmount();
        return this.addRequirement(new ItemTransformRequirement(new ItemIngredient(stack.getDefinition()), stack.getAmount(), inputSlot, CTUtils.nbtFromStack(stack), outputItem, outputAmount, outputSlot, true, new ItemTransformRequirementCT.NbtTransformer(nbt)));
    }

    @Method
    default T transformItemTag(MCTag tag, @OptionalInt(1) int inputAmount, @Optional IData data, @Optional IItemStack output, @OptionalString String inputSlot, @OptionalString String outputSlot, @Optional Function<MapData, MapData> nbt) {
        Item outputItem = output == null ? Items.AIR : output.getDefinition();
        int outputAmount = output == null ? 1 : output.getAmount();
        return this.addRequirement(new ItemTransformRequirement(ItemTagIngredient.create(tag.getTagKey()), inputAmount, inputSlot, CTUtils.getNBT(data), outputItem, outputAmount, outputSlot, true, new ItemTransformRequirementCT.NbtTransformer(nbt)));
    }

    public static class NbtTransformer implements Function<CompoundTag, CompoundTag> {

        private final Function<MapData, MapData> function;

        public NbtTransformer(Function<MapData, MapData> function) {
            this.function = function;
        }

        public CompoundTag apply(@Nullable CompoundTag compoundTag) {
            return ((MapData) this.function.apply(new MapData(compoundTag))).getInternal();
        }
    }
}