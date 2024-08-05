package dev.xkmc.l2library.serial.recipe;

import com.google.gson.JsonObject;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class BaseRecipeBuilder<T extends BaseRecipeBuilder<T, Rec, SRec, Inv>, Rec extends SRec, SRec extends BaseRecipe<?, SRec, Inv>, Inv extends Container> {

    protected final BaseRecipe.RecType<Rec, SRec, Inv> type;

    protected final Rec recipe;

    protected final Advancement.Builder advancement = Advancement.Builder.advancement();

    public BaseRecipeBuilder(BaseRecipe.RecType<Rec, SRec, Inv> type) {
        this.type = type;
        this.recipe = (Rec) ((BaseRecipe) type.blank());
    }

    public T getThis() {
        return (T) this;
    }

    public T unlockedBy(String name, CriterionTriggerInstance trigger) {
        this.advancement.addCriterion(name, trigger);
        return this.getThis();
    }

    public void save(Consumer<FinishedRecipe> pvd, ResourceLocation id) {
        this.ensureValid(id);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        pvd.accept(new BaseRecipeBuilder.Result<>(this.type, this.recipe, id, this.advancement, new ResourceLocation(id.getNamespace(), "recipes/" + ForgeRegistries.RECIPE_SERIALIZERS.getKey(this.type).getPath() + "/" + id.getPath())));
    }

    protected void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static record Result<Rec extends SRec, SRec extends BaseRecipe<?, SRec, Inv>, Inv extends Container>(BaseRecipe.RecType<Rec, SRec, Inv> type, Rec recipe, ResourceLocation id, Advancement.Builder advancement, ResourceLocation advancementId) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(@Nullable JsonObject json) {
        }

        @Override
        public JsonObject serializeRecipe() {
            JsonObject jsonobject = JsonCodec.toJson(this.recipe).getAsJsonObject();
            jsonobject.addProperty("type", ForgeRegistries.RECIPE_SERIALIZERS.getKey(this.getType()).toString());
            return jsonobject;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.type;
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}