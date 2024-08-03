package snownee.lychee.item_inside;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.core.ItemShapelessContext;
import snownee.lychee.core.recipe.LycheeCounter;
import snownee.lychee.core.recipe.type.ItemShapelessRecipeType;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.util.CommonProxy;

public class ItemInsideRecipeType extends LycheeRecipeType<ItemShapelessContext, ItemInsideRecipe> {

    private List<ItemInsideRecipe> specialRecipes = Lists.newArrayList();

    private Multimap<Item, ItemInsideRecipe> recipesByItem = ArrayListMultimap.create();

    public ItemInsideRecipeType(String name, Class<ItemInsideRecipe> clazz, @Nullable LootContextParamSet paramSet) {
        super(name, clazz, paramSet);
        this.compactInputs = true;
    }

    @Override
    public void buildCache() {
        this.specialRecipes.clear();
        this.recipesByItem.clear();
        super.buildCache();
        Object2FloatMap<Item> itemCount = new Object2FloatOpenHashMap();
        List<ItemInsideRecipeType.Cache> caches = (List<ItemInsideRecipeType.Cache>) this.recipes.stream().map($ -> $.buildCache(itemCount, this.specialRecipes)).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
        for (Item item : itemCount.object2FloatEntrySet().stream().sorted((a, b) -> Float.compare(b.getFloatValue(), a.getFloatValue())).map(Entry::getKey).toList()) {
            caches.removeIf(cache -> {
                if (cache.ingredients.stream().anyMatch($ -> $.contains(item))) {
                    this.recipesByItem.put(item, cache.recipe);
                    return cache.ingredients.stream().peek($ -> $.remove(item)).anyMatch(Set::isEmpty);
                } else {
                    return false;
                }
            });
        }
    }

    public void process(Entity entity, ItemStack stack, BlockPos pos, Vec3 origin) {
        if (!this.isEmpty()) {
            MutableObject<ResourceLocation> prevRecipeId = new MutableObject();
            if (entity instanceof LycheeCounter) {
                prevRecipeId.setValue(((LycheeCounter) entity).lychee$getRecipeId());
                ((LycheeCounter) entity).lychee$setRecipeId(null);
            }
            Collection<ItemInsideRecipe> recipes = this.recipesByItem.get(stack.getItem());
            if (!recipes.isEmpty() || !this.specialRecipes.isEmpty()) {
                Level level = entity.level();
                BlockState blockstate = level.getBlockState(pos);
                Block block = blockstate.m_60734_();
                List<ItemEntity> items = level.m_6443_(ItemEntity.class, AABB.ofSize(origin, 3.0, 3.0, 3.0), $ -> $.m_213877_() ? false : pos.equals($.m_20183_()) || level.getBlockState($.m_20183_()).m_60713_(block));
                ItemShapelessContext.Builder<ItemShapelessContext> ctxBuilder = new ItemShapelessContext.Builder<>(level, items);
                ctxBuilder.withParameter(LootContextParams.ORIGIN, CommonProxy.clampPos(origin, pos));
                ctxBuilder.withParameter(LootContextParams.THIS_ENTITY, entity);
                ctxBuilder.withParameter(LootContextParams.BLOCK_STATE, blockstate);
                ctxBuilder.withParameter(LycheeLootContextParams.BLOCK_POS, pos);
                ItemShapelessContext ctx = ctxBuilder.create(this.contextParamSet);
                ItemInsideRecipe prevRecipe = (ItemInsideRecipe) Optional.ofNullable((ResourceLocation) prevRecipeId.getValue()).map(CommonProxy::recipe).filter($ -> $.getType() == this).orElse(null);
                Iterable<ItemInsideRecipe> iterable = Iterables.concat(recipes, this.specialRecipes);
                if (prevRecipe != null) {
                    iterable = Iterables.concat(List.of(prevRecipe), Iterables.filter(iterable, $ -> $ != prevRecipe));
                }
                ItemShapelessRecipeType.process(this, iterable, ctx, recipe -> {
                    ((LycheeCounter) entity).lychee$update((ResourceLocation) prevRecipeId.getValue(), recipe);
                    return recipe.tickOrApply(ctx);
                });
            }
        }
    }

    static record Cache(ItemInsideRecipe recipe, List<Set<Item>> ingredients) {
    }
}