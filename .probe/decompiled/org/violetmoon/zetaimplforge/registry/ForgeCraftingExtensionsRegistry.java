package org.violetmoon.zetaimplforge.registry;

import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import org.violetmoon.zeta.recipe.IZetaCondition;
import org.violetmoon.zeta.recipe.IZetaConditionSerializer;
import org.violetmoon.zeta.recipe.IZetaIngredientSerializer;
import org.violetmoon.zeta.registry.CraftingExtensionsRegistry;

public class ForgeCraftingExtensionsRegistry implements CraftingExtensionsRegistry {

    public final Map<IZetaIngredientSerializer<?>, IIngredientSerializer<?>> toForgeIngredientSerializers = new IdentityHashMap();

    @Override
    public <T extends Ingredient> IZetaIngredientSerializer<T> registerIngredientSerializer(ResourceLocation id, final IZetaIngredientSerializer<T> serializer) {
        IIngredientSerializer<T> forge = new IIngredientSerializer<T>() {

            @Override
            public T parse(FriendlyByteBuf buffer) {
                return serializer.parse(buffer);
            }

            @Override
            public T parse(JsonObject json) {
                return serializer.parse(json);
            }

            @Override
            public void write(FriendlyByteBuf buffer, T ingredient) {
                serializer.write(buffer, ingredient);
            }
        };
        CraftingHelper.register(id, forge);
        this.toForgeIngredientSerializers.put(serializer, forge);
        return serializer;
    }

    @Override
    public ResourceLocation getID(IZetaIngredientSerializer<?> serializer) {
        return CraftingHelper.getID((IIngredientSerializer<?>) this.toForgeIngredientSerializers.get(serializer));
    }

    @Override
    public <T extends IZetaCondition> IZetaConditionSerializer<T> registerConditionSerializer(final IZetaConditionSerializer<T> serializer) {
        CraftingHelper.register(new IConditionSerializer<ForgeCraftingExtensionsRegistry.Zeta2ForgeCondition<T>>() {

            public ForgeCraftingExtensionsRegistry.Zeta2ForgeCondition<T> read(JsonObject json) {
                return new ForgeCraftingExtensionsRegistry.Zeta2ForgeCondition<>(serializer.read(json));
            }

            public void write(JsonObject json, ForgeCraftingExtensionsRegistry.Zeta2ForgeCondition<T> value) {
                serializer.write(json, value.zeta);
            }

            @Override
            public ResourceLocation getID() {
                return serializer.getID();
            }
        });
        return serializer;
    }

    public static record Forge2ZetaContext(ICondition.IContext forge) implements IZetaCondition.IContext {

        @Override
        public <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(ResourceKey<? extends Registry<T>> registry) {
            return this.forge.getAllTags(registry);
        }
    }

    public static record Zeta2ForgeCondition<T extends IZetaCondition>(T zeta) implements ICondition {

        @Override
        public ResourceLocation getID() {
            return this.zeta.getID();
        }

        @Override
        public boolean test(ICondition.IContext context) {
            return this.zeta.test(new ForgeCraftingExtensionsRegistry.Forge2ZetaContext(context));
        }
    }
}