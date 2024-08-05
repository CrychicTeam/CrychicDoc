package dev.shadowsoffire.placebo.codec;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import dev.shadowsoffire.placebo.json.ItemAdapter;
import java.util.Arrays;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;

public class IngredientCodec implements Codec<Ingredient> {

    public static IngredientCodec INSTANCE = new IngredientCodec();

    private static Codec<List<ItemStack>> ITEM_LIST_CODEC = ItemAdapter.CODEC.listOf();

    public <T> DataResult<T> encode(Ingredient input, DynamicOps<T> ops, T prefix) {
        return ITEM_LIST_CODEC.encode(Arrays.asList(input.getItems()), ops, prefix);
    }

    public <T> DataResult<Pair<Ingredient, T>> decode(DynamicOps<T> ops, T input) {
        JsonElement json = input instanceof JsonElement j ? j : (JsonElement) ops.convertTo(JsonOps.INSTANCE, input);
        try {
            return DataResult.success(Pair.of(CraftingHelper.getIngredient(json, true), input));
        } catch (JsonSyntaxException var5) {
            return DataResult.error(var5::getMessage);
        }
    }
}