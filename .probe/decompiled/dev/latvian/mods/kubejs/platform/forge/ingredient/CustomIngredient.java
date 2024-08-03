package dev.latvian.mods.kubejs.platform.forge.ingredient;

import com.google.gson.JsonObject;
import java.util.function.Predicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.IIngredientSerializer;

public class CustomIngredient extends KubeJSIngredient {

    public static final KubeJSIngredientSerializer<CustomIngredient> SERIALIZER = new KubeJSIngredientSerializer<>(CustomIngredient::new, CustomIngredient::new);

    private final Predicate<ItemStack> predicate;

    public CustomIngredient(Predicate<ItemStack> predicate) {
        this.predicate = predicate;
    }

    private CustomIngredient(JsonObject json) {
        this.predicate = stack -> true;
    }

    private CustomIngredient(FriendlyByteBuf buf) {
        this.predicate = stack -> true;
    }

    @Override
    public IIngredientSerializer<CustomIngredient> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public boolean test(ItemStack stack) {
        return this.predicate.test(stack);
    }

    @Override
    public void toJson(JsonObject json) {
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }
}