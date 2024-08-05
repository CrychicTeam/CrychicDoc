package snownee.lychee.crafting;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.Lychee;
import snownee.lychee.LycheeConfig;
import snownee.lychee.LycheeLootContextParamSets;
import snownee.lychee.RecipeSerializers;
import snownee.lychee.core.contextual.ContextualHolder;
import snownee.lychee.core.input.ItemHolderCollection;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.core.post.input.SetItem;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.fragment.Fragments;
import snownee.lychee.mixin.CraftingMenuAccess;
import snownee.lychee.mixin.InventoryMenuAccess;
import snownee.lychee.mixin.ShapedRecipeAccess;
import snownee.lychee.mixin.TransientCraftingContainerAccess;
import snownee.lychee.util.Pair;
import snownee.lychee.util.json.JsonPointer;

public class ShapedCraftingRecipe extends ShapedRecipe implements ILycheeRecipe<CraftingContext> {

    public static final Cache<Class<?>, Function<CraftingContainer, Pair<Vec3, Player>>> CONTAINER_WORLD_LOCATOR = CacheBuilder.newBuilder().build();

    public static final Cache<Class<?>, Function<AbstractContainerMenu, Pair<Vec3, Player>>> MENU_WORLD_LOCATOR = CacheBuilder.newBuilder().build();

    private static final Cache<CraftingContainer, CraftingContext> CONTEXT_CACHE = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.SECONDS).build();

    private final ContextualHolder conditions = new ContextualHolder();

    public boolean ghost;

    public boolean hideInRecipeViewer;

    @Nullable
    public String comment;

    @Nullable
    public String pattern;

    private List<PostAction> actions = List.of();

    private List<PostAction> assembling = List.of();

    public ShapedCraftingRecipe(ResourceLocation id, String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result, boolean showNotification) {
        super(id, group, category, width, height, ingredients, result, showNotification);
    }

    @Nullable
    private static Pair<Vec3, Player> getContainerContext(CraftingContainer container) {
        try {
            return (Pair<Vec3, Player>) ((Function) CONTAINER_WORLD_LOCATOR.get(container.getClass(), () -> {
                for (Class<?> clazz = container.getClass().getSuperclass(); clazz != CraftingContainer.class; clazz = clazz.getSuperclass()) {
                    Function<CraftingContainer, Pair<Vec3, Player>> locator = (Function<CraftingContainer, Pair<Vec3, Player>>) CONTAINER_WORLD_LOCATOR.getIfPresent(clazz);
                    if (locator != null) {
                        return locator;
                    }
                }
                return container1 -> null;
            })).apply(container);
        } catch (ExecutionException var2) {
            return null;
        }
    }

    public static CraftingContext makeContext(CraftingContainer container, Level level, int matchX, int matchY, boolean mirror) {
        Pair<Vec3, Player> pair = getContainerContext(container);
        CraftingContext.Builder builder = new CraftingContext.Builder(level, matchX, matchY, mirror);
        if (pair != null) {
            builder.withOptionalParameter(LootContextParams.ORIGIN, pair.getFirst());
            builder.withOptionalParameter(LootContextParams.THIS_ENTITY, pair.getSecond());
        }
        CraftingContext ctx = builder.create(LycheeLootContextParamSets.CRAFTING);
        CONTEXT_CACHE.put(container, ctx);
        return ctx;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        if (this.ghost) {
            return false;
        } else if (level.isClientSide) {
            return super.matches(container, level);
        } else {
            ShapedRecipeAccess access = (ShapedRecipeAccess) this;
            int i = 0;
            int j = 0;
            boolean mirror = false;
            boolean matched = false;
            label76: for (i = 0; i <= container.getWidth() - this.m_44220_(); i++) {
                for (j = 0; j <= container.getHeight() - this.m_44221_(); j++) {
                    if (access.callMatches(container, i, j, true)) {
                        matched = true;
                        break label76;
                    }
                    if (this.m_44220_() > 1 && access.callMatches(container, i, j, false)) {
                        matched = true;
                        mirror = true;
                        break label76;
                    }
                }
            }
            if (!matched) {
                return false;
            } else {
                CraftingContext ctx = makeContext(container, level, i, j, mirror);
                matched = this.conditions.checkConditions(this, ctx, 1) > 0;
                if (matched) {
                    ItemStack result = this.m_8043_(level.registryAccess()).copy();
                    List<Ingredient> ingredients = this.m_7527_();
                    ItemStack[] items = new ItemStack[ingredients.size() + 1];
                    int startIndex = container.getWidth() * ctx.matchY + ctx.matchX;
                    int k = 0;
                    for (int var15 = 0; var15 < this.m_44221_(); var15++) {
                        for (int var16 = 0; var16 < this.m_44220_(); var16++) {
                            items[k] = container.m_8020_(startIndex + container.getWidth() * var15 + (ctx.mirror ? this.m_44220_() - var16 : var16));
                            if (!items[k].isEmpty()) {
                                items[k] = items[k].copy();
                                items[k].setCount(1);
                            }
                            k++;
                        }
                    }
                    items[ingredients.size()] = result;
                    ctx.itemHolders = ItemHolderCollection.Inventory.of(ctx, items);
                }
                return matched;
            }
        }
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        CraftingContext ctx = (CraftingContext) CONTEXT_CACHE.getIfPresent(container);
        if (ctx == null) {
            return ItemStack.EMPTY;
        } else {
            ctx.enqueueActions(this.assembling.stream(), 1, true);
            ctx.runtime.run(this, ctx);
            return ctx.m_8020_(ctx.m_6643_() - 1);
        }
    }

    public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> items = super.m_7457_(container);
        CraftingContext ctx = (CraftingContext) CONTEXT_CACHE.getIfPresent(container);
        if (ctx == null) {
            return items;
        } else {
            this.applyPostActions(ctx, 1);
            int startIndex = container.getWidth() * ctx.matchY + ctx.matchX;
            int k = 0;
            for (int i = 0; i < this.m_44221_(); i++) {
                for (int j = 0; j < this.m_44220_(); j++) {
                    if (ctx.itemHolders.ignoreConsumptionFlags.get(k)) {
                        items.set(startIndex + container.getWidth() * i + (ctx.mirror ? this.m_44220_() - j : j), ctx.m_8020_(k));
                    }
                    k++;
                }
            }
            return items;
        }
    }

    @Override
    public JsonPointer defaultItemPointer() {
        return RESULT;
    }

    @Override
    public IntList getItemIndexes(JsonPointer pointer) {
        int size = this.m_7527_().size();
        if (pointer.size() == 1 && pointer.getString(0).equals("result")) {
            return IntList.of(size);
        } else if (pointer.size() == 2 && pointer.getString(0).equals("key")) {
            String key = pointer.getString(1);
            if (key.length() != 1) {
                return IntList.of();
            } else {
                IntList list = IntArrayList.of();
                int cp = key.codePointAt(0);
                int l = this.pattern.length();
                for (int i = 0; i < l; i++) {
                    if (cp == this.pattern.codePointAt(i)) {
                        list.add(i);
                    }
                }
                return list;
            }
        } else {
            return IntList.of(size);
        }
    }

    @Override
    public Stream<PostAction> getPostActions() {
        return this.actions.stream();
    }

    @Override
    public Stream<PostAction> getAllActions() {
        return Stream.concat(this.getPostActions(), this.assembling.stream());
    }

    @Override
    public ContextualHolder getContextualHolder() {
        return this.conditions;
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    public void addPostAction(PostAction action) {
        Objects.requireNonNull(action);
        if (action instanceof SetItem setItem && this.getItemIndexes(setItem.target).contains(this.m_7527_().size())) {
            throw new JsonSyntaxException("Can't set item to the result in \"post\", use \"assembling\".");
        }
        if (this.actions.isEmpty()) {
            this.actions = Lists.newArrayList();
        }
        this.actions.add(action);
    }

    public void addAssemblingAction(PostAction action) {
        Objects.requireNonNull(action);
        if (action instanceof SetItem setItem) {
            IntList intList = this.getItemIndexes(setItem.target);
            if (!intList.isEmpty() && !intList.contains(this.m_7527_().size())) {
                throw new JsonSyntaxException("Can't set item to the ingredients in \"assembling\", use \"post\".");
            }
        }
        if (this.assembling.isEmpty()) {
            this.assembling = Lists.newArrayList();
        }
        this.assembling.add(action);
    }

    @Override
    public boolean showInRecipeViewer() {
        return !this.hideInRecipeViewer;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.CRAFTING;
    }

    @Override
    public boolean isSpecial() {
        return !this.conditions.getConditions().isEmpty() || !this.assembling.isEmpty();
    }

    @Override
    public boolean isActionPath(JsonPointer pointer) {
        if (pointer.isRoot()) {
            return false;
        } else {
            String token = pointer.getString(0);
            return "assembling".equals(token) || "post".equals(token);
        }
    }

    @Override
    public Map<JsonPointer, List<PostAction>> getActionGroups() {
        return Map.of(POST, this.actions, new JsonPointer("/assembling"), this.assembling);
    }

    static {
        CONTAINER_WORLD_LOCATOR.put(TransientCraftingContainer.class, (Function) container -> {
            TransientCraftingContainerAccess access = (TransientCraftingContainerAccess) container;
            AbstractContainerMenu menu = access.getMenu();
            try {
                return (Pair) ((Function) MENU_WORLD_LOCATOR.get(menu.getClass(), () -> {
                    for (Class<?> clazz = menu.getClass().getSuperclass(); clazz != AbstractContainerMenu.class; clazz = clazz.getSuperclass()) {
                        Function<AbstractContainerMenu, Pair<Vec3, Player>> locator = (Function<AbstractContainerMenu, Pair<Vec3, Player>>) MENU_WORLD_LOCATOR.getIfPresent(clazz);
                        if (locator != null) {
                            return locator;
                        }
                    }
                    return menu1 -> null;
                })).apply(menu);
            } catch (ExecutionException var4) {
                return null;
            }
        });
        MENU_WORLD_LOCATOR.put(CraftingMenu.class, (Function) menu -> {
            CraftingMenuAccess access = (CraftingMenuAccess) menu;
            return Pair.of(access.getAccess().evaluate((level, pos) -> Vec3.atCenterOf(pos), access.getPlayer().m_20182_()), access.getPlayer());
        });
        MENU_WORLD_LOCATOR.put(InventoryMenu.class, (Function) menu -> {
            InventoryMenuAccess access = (InventoryMenuAccess) menu;
            return Pair.of(access.getOwner().m_20182_(), access.getOwner());
        });
    }

    public static class Serializer implements RecipeSerializer<ShapedCraftingRecipe> {

        private static ShapedCraftingRecipe fromNormal(ShapedRecipe recipe) {
            return new ShapedCraftingRecipe(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), ((ShapedRecipeAccess) recipe).getResult(), recipe.showNotification());
        }

        public ShapedCraftingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
            Fragments.INSTANCE.process(jsonObject);
            ShapedCraftingRecipe recipe = fromNormal(RecipeSerializer.SHAPED_RECIPE.fromJson(id, jsonObject));
            recipe.hideInRecipeViewer = GsonHelper.getAsBoolean(jsonObject, "hide_in_viewer", false);
            recipe.ghost = GsonHelper.getAsBoolean(jsonObject, "ghost", false);
            recipe.comment = GsonHelper.getAsString(jsonObject, "comment", null);
            StringBuilder sb = new StringBuilder();
            StreamSupport.stream(jsonObject.getAsJsonArray("pattern").spliterator(), false).map(JsonElement::getAsString).forEach(sb::append);
            recipe.pattern = sb.toString();
            recipe.conditions.parseConditions(jsonObject.get("contextual"));
            PostAction.parseActions(jsonObject.get("post"), recipe::addPostAction);
            PostAction.parseActions(jsonObject.get("assembling"), recipe::addAssemblingAction);
            ILycheeRecipe.processActions(recipe, jsonObject);
            return recipe;
        }

        public ShapedCraftingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            if (LycheeConfig.debug) {
                Lychee.LOGGER.debug("Read recipe: {}", id);
            }
            ShapedCraftingRecipe recipe = fromNormal(RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buf));
            recipe.hideInRecipeViewer = buf.readBoolean();
            if (recipe.hideInRecipeViewer) {
                return recipe;
            } else {
                recipe.conditions.conditionsFromNetwork(buf);
                LycheeRecipe.Serializer.actionsFromNetwork(buf, recipe::addPostAction);
                recipe.comment = buf.readUtf();
                recipe.pattern = buf.readUtf();
                return recipe;
            }
        }

        public void toNetwork(FriendlyByteBuf buf, ShapedCraftingRecipe recipe) {
            if (LycheeConfig.debug) {
                Lychee.LOGGER.debug("Write recipe: {}", recipe.m_6423_());
            }
            RecipeSerializer.SHAPED_RECIPE.toNetwork(buf, recipe);
            buf.writeBoolean(recipe.hideInRecipeViewer);
            if (!recipe.hideInRecipeViewer) {
                recipe.conditions.conditionsToNetwork(buf);
                LycheeRecipe.Serializer.actionsToNetwork(buf, recipe.actions);
                buf.writeUtf(Strings.nullToEmpty(recipe.comment));
                buf.writeUtf(Strings.nullToEmpty(recipe.pattern));
            }
        }
    }
}