package dev.xkmc.l2library.serial.ingredients;

import com.google.gson.JsonObject;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

@SerialClass
public class EnchantmentIngredient extends BaseIngredient<EnchantmentIngredient> {

    public static final BaseIngredient.Serializer<EnchantmentIngredient> INSTANCE = new BaseIngredient.Serializer<>(EnchantmentIngredient.class, new ResourceLocation("l2library", "enchantment"));

    @SerialField
    public Enchantment enchantment;

    @SerialField
    public int min_level;

    @Deprecated
    public EnchantmentIngredient() {
    }

    public EnchantmentIngredient(Enchantment enchantment, int minLevel) {
        super(Stream.of(new EnchantmentIngredient.EnchValue(enchantment, minLevel)));
        this.enchantment = enchantment;
        this.min_level = minLevel;
    }

    protected EnchantmentIngredient validate() {
        return new EnchantmentIngredient(this.enchantment, this.min_level);
    }

    @Override
    public boolean test(ItemStack stack) {
        return (Integer) EnchantmentHelper.getEnchantments(stack).getOrDefault(this.enchantment, 0) >= this.min_level;
    }

    @Override
    public BaseIngredient.Serializer<EnchantmentIngredient> getSerializer() {
        return INSTANCE;
    }

    private static record EnchValue(Enchantment ench, int min) implements Ingredient.Value {

        @Override
        public Collection<ItemStack> getItems() {
            return IntStream.range(this.min, this.ench.getMaxLevel() + 1).mapToObj(i -> EnchantedBookItem.createForEnchantment(new EnchantmentInstance(this.ench, i))).toList();
        }

        @Override
        public JsonObject serialize() {
            throw new IllegalStateException("This value should not be serialized as such");
        }
    }
}