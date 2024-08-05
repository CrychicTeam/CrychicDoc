package dev.xkmc.l2library.serial.recipe;

import com.google.gson.JsonObject;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AbstractShapedRecipe<T extends AbstractShapedRecipe<T>> extends ShapedRecipe {

    public AbstractShapedRecipe(ResourceLocation rl, String group, int w, int h, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(rl, group, CraftingBookCategory.MISC, w, h, ingredients, result);
    }

    public abstract AbstractShapedRecipe.Serializer<T> getSerializer();

    @FunctionalInterface
    public interface RecipeFactory<T extends AbstractShapedRecipe<T>> {

        T create(ResourceLocation var1, String var2, int var3, int var4, NonNullList<Ingredient> var5, ItemStack var6);
    }

    public static class SerialSerializer<T extends AbstractShapedRecipe<T>> extends AbstractShapedRecipe.Serializer<T> {

        private final Class<T> cls;

        public SerialSerializer(Class<T> cls, AbstractShapedRecipe.RecipeFactory<T> factory) {
            super(factory);
            this.cls = cls;
        }

        @Override
        public T fromJson(ResourceLocation id, JsonObject obj) {
            return (T) Objects.requireNonNull((AbstractShapedRecipe) JsonCodec.from(obj, this.cls, super.fromJson(id, obj)));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShapedRecipe rec) {
            super.m_6178_(buf, rec);
            PacketCodec.to(buf, rec);
        }

        @Override
        public T fromNetwork(ResourceLocation id, FriendlyByteBuf obj) {
            return (T) PacketCodec.from(obj, this.cls, super.fromNetwork(id, obj));
        }

        @Override
        public void toJson(T recipe, JsonObject obj) {
            JsonCodec.toJsonObject(recipe, obj);
        }
    }

    public static class Serializer<T extends AbstractShapedRecipe<T>> extends ShapedRecipe.Serializer {

        private final AbstractShapedRecipe.RecipeFactory<T> factory;

        public Serializer(AbstractShapedRecipe.RecipeFactory<T> factory) {
            this.factory = factory;
        }

        public T fromJson(ResourceLocation id, JsonObject obj) {
            ShapedRecipe r = super.fromJson(id, obj);
            return this.factory.create(r.getId(), r.getGroup(), r.getRecipeWidth(), r.getRecipeHeight(), r.getIngredients(), r.result);
        }

        public T fromNetwork(ResourceLocation id, FriendlyByteBuf obj) {
            ShapedRecipe r = super.fromNetwork(id, obj);
            return r == null ? null : this.factory.create(r.getId(), r.getGroup(), r.getRecipeWidth(), r.getRecipeHeight(), r.getIngredients(), r.result);
        }

        public void toJson(T recipe, JsonObject obj) {
        }
    }
}