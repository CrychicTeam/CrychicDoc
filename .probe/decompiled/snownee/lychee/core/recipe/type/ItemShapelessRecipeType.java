package snownee.lychee.core.recipe.type;

import com.google.common.collect.Sets;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.Lychee;
import snownee.lychee.core.ItemShapelessContext;
import snownee.lychee.core.recipe.LycheeRecipe;

public class ItemShapelessRecipeType<C extends ItemShapelessContext, T extends LycheeRecipe<C>> extends LycheeRecipeType<C, T> {

    private LycheeRecipeType.ValidItemCache validItems = new LycheeRecipeType.ValidItemCache();

    public ItemShapelessRecipeType(String name, Class<T> clazz, @Nullable LootContextParamSet contextParamSet) {
        super(name, clazz, contextParamSet);
        this.compactInputs = true;
    }

    @Override
    public void buildCache() {
        super.buildCache();
        this.validItems.buildCache(this.recipes);
    }

    public void process(Level level, Stream<ItemEntity> itemEntities, Consumer<ItemShapelessContext.Builder<C>> ctxBuilderConsumer) {
        if (!this.isEmpty()) {
            List<ItemEntity> list = (List<ItemEntity>) itemEntities.filter($ -> this.validItems.contains($.getItem())).collect(Collectors.toCollection(LinkedList::new));
            ItemShapelessContext.Builder<C> ctxBuilder = new ItemShapelessContext.Builder<>(level, list);
            ctxBuilderConsumer.accept(ctxBuilder);
            process(this, this.recipes, ctxBuilder.create(this.contextParamSet), null);
        }
    }

    public static <C extends ItemShapelessContext, T extends LycheeRecipe<C>> void process(LycheeRecipeType<C, T> recipeType, Iterable<T> recipes, C ctx, Predicate<T> test) {
        boolean matchedAny = false;
        int loop = 0;
        Set<T> excluded = Sets.newHashSet();
        boolean matched;
        label84: do {
            matched = false;
            for (T recipe : recipes) {
                if ((!recipe.m_7527_().isEmpty() || loop <= 0) && !excluded.contains(recipe)) {
                    try {
                        Optional<T> match = recipeType.tryMatch(recipe, ctx.getLevel(), ctx);
                        if (match.isPresent()) {
                            if (test != null && !test.test(recipe)) {
                                excluded.add(recipe);
                            } else {
                                matched = true;
                                matchedAny = true;
                                int times = 1;
                                if (ctx.getMatch() != null && ctx.getMatch().inputUsed.length > 0) {
                                    int[] inputUsed = ctx.getMatch().inputUsed;
                                    times = recipe.getRandomRepeats(Integer.MAX_VALUE, ctx);
                                    for (int i = 0; i < inputUsed.length; i++) {
                                        if (inputUsed[i] > 0) {
                                            ItemStack stack = ((ItemEntity) ctx.filteredItems.get(i)).getItem();
                                            times = Math.min(times, stack.getCount() / inputUsed[i]);
                                        }
                                    }
                                }
                                ((LycheeRecipe) match.get()).applyPostActions(ctx, times);
                                if (ctx.getMatch() != null) {
                                    ctx.totalItems = ctx.totalItems - ctx.itemHolders.postApply(ctx.runtime.doDefault, times);
                                }
                                if (!recipe.getMaxRepeats().m_55327_()) {
                                    break label84;
                                }
                                ctx.filteredItems = null;
                                ctx.setMatch(null);
                                ctx.itemEntities.removeIf($ -> $.getItem().isEmpty());
                            }
                        }
                    } catch (Exception var15) {
                        Lychee.LOGGER.error("", var15);
                        break label84;
                    }
                }
            }
        } while (++loop < 100 && matched);
        if (matchedAny) {
            ctx.itemEntities.forEach($ -> $.setItem($.getItem()));
        }
    }
}