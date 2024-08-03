package dev.xkmc.l2library.serial.recipe;

import com.google.gson.JsonObject;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import java.util.List;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AbstractShapelessRecipe<T extends AbstractShapelessRecipe<T>> extends ShapelessRecipe {

    public AbstractShapelessRecipe(ResourceLocation rl, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(rl, group, CraftingBookCategory.MISC, result, ingredients);
    }

    public List<ItemStack> getJEIResult() {
        return List.of(this.f_44243_);
    }

    public abstract AbstractShapelessRecipe.Serializer<T> getSerializer();

    @FunctionalInterface
    public interface RecipeFactory<T extends AbstractShapelessRecipe<T>> {

        T create(ResourceLocation var1, String var2, ItemStack var3, NonNullList<Ingredient> var4);
    }

    public static class SerialSerializer<T extends AbstractShapelessRecipe<T>> extends AbstractShapelessRecipe.Serializer<T> {

        private final Class<T> cls;

        public SerialSerializer(Class<T> cls, AbstractShapelessRecipe.RecipeFactory<T> factory) {
            super(factory);
            this.cls = cls;
        }

        @Override
        public T fromJson(ResourceLocation id, JsonObject obj) {
            return (T) Objects.requireNonNull((AbstractShapelessRecipe) JsonCodec.from(obj, this.cls, super.fromJson(id, obj)));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShapelessRecipe rec) {
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

    public static class Serializer<T extends AbstractShapelessRecipe<T>> extends ShapelessRecipe.Serializer {

        private final AbstractShapelessRecipe.RecipeFactory<T> factory;

        public Serializer(AbstractShapelessRecipe.RecipeFactory<T> factory) {
            this.factory = factory;
        }

        public T fromJson(ResourceLocation id, JsonObject obj) {
            ShapelessRecipe r = super.fromJson(id, obj);
            return this.factory.create(r.getId(), r.getGroup(), r.result, r.getIngredients());
        }

        public T fromNetwork(ResourceLocation id, FriendlyByteBuf obj) {
            ShapelessRecipe r = super.fromNetwork(id, obj);
            return r == null ? null : this.factory.create(r.getId(), r.getGroup(), r.result, r.getIngredients());
        }

        public void toJson(T recipe, JsonObject obj) {
        }
    }
}