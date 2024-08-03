package snownee.lychee.core.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.Lychee;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.Reference;
import snownee.lychee.core.contextual.ContextualHolder;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.util.json.JsonPatch;
import snownee.lychee.util.json.JsonPointer;

public interface ILycheeRecipe<C extends LycheeContext> {

    JsonPointer ITEM_IN = new JsonPointer("/item_in");

    JsonPointer ITEM_OUT = new JsonPointer("/item_out");

    JsonPointer RESULT = new JsonPointer("/result");

    JsonPointer POST = new JsonPointer("/post");

    Map<ResourceLocation, ILycheeRecipe.NBTPatchContext> patchContexts = Maps.newHashMap();

    default ResourceLocation lychee$getId() {
        return ((Recipe) this).getId();
    }

    default IntList getItemIndexes(Reference reference) {
        JsonPointer pointer = null;
        if (reference == Reference.DEFAULT) {
            pointer = this.defaultItemPointer();
        } else if (reference.isPointer()) {
            pointer = reference.getPointer();
        }
        return pointer != null ? this.getItemIndexes(pointer) : IntList.of();
    }

    IntList getItemIndexes(JsonPointer var1);

    default JsonPointer defaultItemPointer() {
        return ITEM_IN;
    }

    Stream<PostAction> getPostActions();

    default Stream<PostAction> getAllActions() {
        return this.getPostActions();
    }

    Map<JsonPointer, List<PostAction>> getActionGroups();

    static Stream<PostAction> filterHidden(Stream<PostAction> stream) {
        return stream.filter(Predicate.not(PostAction::isHidden));
    }

    default int showingActionsCount() {
        return (int) filterHidden(this.getPostActions()).count();
    }

    ContextualHolder getContextualHolder();

    @Nullable
    String getComment();

    boolean showInRecipeViewer();

    default void applyPostActions(LycheeContext ctx, int times) {
        if (!ctx.getLevel().isClientSide) {
            ctx.enqueueActions(this.getPostActions(), times, true);
            ctx.runtime.run(this, ctx);
        }
    }

    default List<BlockPredicate> getBlockInputs() {
        return this instanceof BlockKeyRecipe<?> blockKeyRecipe ? List.of(blockKeyRecipe.getBlock()) : List.of();
    }

    default List<BlockPredicate> getBlockOutputs() {
        return filterHidden(this.getAllActions()).map(PostAction::getBlockOutputs).flatMap(Collection::stream).toList();
    }

    default boolean isActionPath(JsonPointer pointer) {
        return !pointer.isRoot() && "post".equals(pointer.getString(0));
    }

    static void processActions(ILycheeRecipe<?> recipe, JsonObject recipeObject) {
        MutableObject<ILycheeRecipe.NBTPatchContext> patchContext = new MutableObject();
        Set<JsonPointer> usedPointers = Sets.newHashSet();
        recipe.getAllActions().forEach(action -> action.getUsedPointers(recipe, usedPointers::add));
        if (!usedPointers.isEmpty()) {
            IntSet usedIndexes = new IntArraySet();
            Object2IntMap<JsonPointer> splits = new Object2IntArrayMap();
            usedPointers.forEach(pointerx -> {
                if (!recipe.isActionPath(pointerx)) {
                    for (List<String> tokens = Lists.newArrayList(pointerx.tokens); !tokens.isEmpty(); tokens.remove(tokens.size() - 1)) {
                        JsonPointer current = new JsonPointer(tokens);
                        if (current.find(recipeObject) != null) {
                            IntList indexes = recipe.getItemIndexes(current);
                            if (!indexes.isEmpty()) {
                                usedIndexes.addAll(indexes);
                                splits.put(pointerx, current.toString().length());
                                break;
                            }
                        }
                    }
                }
            });
            JsonObject jsonObject = new JsonObject();
            for (Entry<JsonPointer, List<PostAction>> entry : recipe.getActionGroups().entrySet()) {
                JsonPointer pointer = (JsonPointer) entry.getKey();
                List<PostAction> actions = (List<PostAction>) entry.getValue();
                JsonElement element = processActionGroup(recipe, pointer, actions, recipeObject);
                if (element != null) {
                    JsonPatch.add(jsonObject, pointer, element);
                }
            }
            patchContext.setValue(new ILycheeRecipe.NBTPatchContext(jsonObject, usedIndexes, splits));
            patchContexts.put(recipe.lychee$getId(), (ILycheeRecipe.NBTPatchContext) patchContext.getValue());
        } else {
            patchContexts.remove(recipe.lychee$getId());
        }
        recipe.getAllActions().forEach(action -> {
            try {
                action.validate(recipe, (ILycheeRecipe.NBTPatchContext) patchContext.getValue());
            } catch (Exception var4x) {
                Lychee.LOGGER.error("Error while validating action " + action, var4x);
            }
        });
    }

    static JsonElement processActionGroup(ILycheeRecipe<?> recipe, JsonPointer pointer, List<PostAction> actions, JsonObject recipeObject) {
        if (actions.isEmpty()) {
            return null;
        } else {
            JsonElement element = pointer.find(recipeObject);
            if (element == null) {
                return null;
            } else if (element.isJsonObject()) {
                element = ((PostAction) actions.get(0)).provideJsonInfo(recipe, pointer, recipeObject);
                return element.isJsonNull() ? null : element;
            } else {
                JsonArray array = new JsonArray();
                int size = element.getAsJsonArray().size();
                for (int i = 0; i < size; i++) {
                    array.add(((PostAction) actions.get(i)).provideJsonInfo(recipe, pointer.append(Integer.toString(i)), recipeObject));
                }
                return array;
            }
        }
    }

    public static record NBTPatchContext(JsonObject template, IntCollection usedIndexes, Object2IntMap<JsonPointer> splits) {

        public JsonPointer convertPath(JsonPointer path, BiFunction<String, String, String> composer) {
            int index = this.splits.getOrDefault(path, -1);
            if (index == -1) {
                return path;
            } else {
                String s = path.toString();
                String first = s.substring(0, index);
                String last = s.substring(index);
                return new JsonPointer((String) composer.apply(first, last));
            }
        }

        public int countTargets(ILycheeRecipe<?> recipe, Reference reference) {
            JsonPointer pointer = null;
            if (reference == Reference.DEFAULT) {
                pointer = recipe.defaultItemPointer();
            } else if (reference.isPointer()) {
                pointer = reference.getPointer();
            }
            return pointer == null ? 0 : this.countTargets(recipe, pointer);
        }

        public int countTargets(ILycheeRecipe<?> recipe, JsonPointer pointer) {
            if (recipe.isActionPath(pointer)) {
                return 1;
            } else {
                int index = this.splits.getOrDefault(pointer, -1);
                return index == -1 ? 0 : recipe.getItemIndexes(new JsonPointer(pointer.toString().substring(0, index))).size();
            }
        }
    }
}