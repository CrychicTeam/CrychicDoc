package dev.xkmc.l2library.serial.recipe;

import com.google.gson.JsonObject;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AbstractSmithingRecipe<T extends AbstractSmithingRecipe<T>> extends SmithingTransformRecipe {

    public static final Ingredient TEMPLATE_PLACEHOLDER = Ingredient.EMPTY;

    public AbstractSmithingRecipe(ResourceLocation rl, Ingredient left, Ingredient right, ItemStack result) {
        super(rl, TEMPLATE_PLACEHOLDER, left, right, result);
    }

    public abstract AbstractSmithingRecipe.Serializer<T> getSerializer();

    @FunctionalInterface
    public interface RecipeFactory<T extends AbstractSmithingRecipe<T>> {

        T create(ResourceLocation var1, Ingredient var2, Ingredient var3, ItemStack var4);
    }

    public static class SerialSerializer<T extends AbstractSmithingRecipe<T>> extends AbstractSmithingRecipe.Serializer<T> {

        private final Class<T> cls;

        public SerialSerializer(Class<T> cls, AbstractSmithingRecipe.RecipeFactory<T> factory) {
            super(factory);
            this.cls = cls;
        }

        @Override
        public T fromJson(ResourceLocation id, JsonObject obj) {
            return (T) Objects.requireNonNull((AbstractSmithingRecipe) JsonCodec.from(obj, this.cls, super.fromJson(id, obj)));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SmithingTransformRecipe rec) {
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

    public static class Serializer<T extends AbstractSmithingRecipe<T>> extends SmithingTransformRecipe.Serializer {

        private final AbstractSmithingRecipe.RecipeFactory<T> factory;

        public Serializer(AbstractSmithingRecipe.RecipeFactory<T> factory) {
            this.factory = factory;
        }

        public T fromJson(ResourceLocation id, JsonObject obj) {
            SmithingTransformRecipe r = super.fromJson(id, obj);
            return this.factory.create(r.getId(), r.base, r.addition, r.result);
        }

        public T fromNetwork(ResourceLocation id, FriendlyByteBuf obj) {
            SmithingTransformRecipe r = super.fromNetwork(id, obj);
            return r == null ? null : this.factory.create(r.getId(), r.base, r.addition, r.result);
        }

        public void toJson(T recipe, JsonObject obj) {
        }
    }
}