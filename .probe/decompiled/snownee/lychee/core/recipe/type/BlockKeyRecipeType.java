package snownee.lychee.core.recipe.type;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.contextual.Chance;
import snownee.lychee.core.contextual.ContextualCondition;
import snownee.lychee.core.def.BlockPredicateHelper;
import snownee.lychee.core.input.ItemHolderCollection;
import snownee.lychee.core.recipe.BlockKeyRecipe;
import snownee.lychee.core.recipe.ChanceRecipe;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.util.CommonProxy;
import snownee.lychee.util.Pair;

public class BlockKeyRecipeType<C extends LycheeContext, T extends LycheeRecipe<C> & BlockKeyRecipe<?>> extends LycheeRecipeType<C, T> {

    protected final Map<Block, List<T>> recipesByBlock = Maps.newHashMap();

    protected final List<T> anyBlockRecipes = Lists.newLinkedList();

    public boolean extractChance;

    public BlockKeyRecipeType(String name, Class<T> clazz, @Nullable LootContextParamSet paramSet) {
        super(name, clazz, paramSet);
    }

    @Override
    public void buildCache() {
        this.recipesByBlock.clear();
        this.anyBlockRecipes.clear();
        super.buildCache();
        Multimap<Block, T> multimap = HashMultimap.create();
        for (T recipe : this.recipes) {
            if (!recipe.getConditions().isEmpty()) {
                ContextualCondition condition = (ContextualCondition) recipe.getConditions().get(0);
                if (condition instanceof Chance chance) {
                    ((ChanceRecipe) recipe).setChance(chance.chance());
                }
            }
            if (recipe.getBlock() == BlockPredicate.ANY) {
                this.anyBlockRecipes.add(recipe);
            } else {
                for (Block block : BlockPredicateHelper.getMatchedBlocks(recipe.getBlock())) {
                    multimap.put(block, recipe);
                }
            }
        }
        for (Entry<Block, Collection<T>> e : multimap.asMap().entrySet()) {
            List<T> list = Lists.newArrayList((Iterable) e.getValue());
            list.sort(null);
            this.recipesByBlock.put((Block) e.getKey(), list);
        }
    }

    public List<ItemStack> blockKeysToItems() {
        return this.recipesByBlock.keySet().stream().map(Block::m_5456_).filter($ -> $ != Items.AIR).sorted((a, b) -> Integer.compare(Item.getId(a), Item.getId(b))).map(Item::m_7968_).toList();
    }

    public Optional<T> process(Player player, InteractionHand hand, BlockPos pos, Vec3 origin, LycheeContext.Builder<C> ctxBuilder) {
        if (this.isEmpty()) {
            return Optional.empty();
        } else {
            Level level = player.m_9236_();
            BlockState blockstate = level.getBlockState(pos);
            Collection<T> recipes = (Collection<T>) this.recipesByBlock.getOrDefault(blockstate.m_60734_(), List.of());
            if (recipes.isEmpty() && this.anyBlockRecipes.isEmpty()) {
                return Optional.empty();
            } else {
                ctxBuilder.withParameter(LootContextParams.ORIGIN, CommonProxy.clampPos(origin, pos));
                ctxBuilder.withParameter(LootContextParams.THIS_ENTITY, player);
                ctxBuilder.withParameter(LootContextParams.BLOCK_STATE, blockstate);
                ctxBuilder.withParameter(LycheeLootContextParams.BLOCK_POS, pos);
                C ctx = ctxBuilder.create(this.contextParamSet);
                ItemStack stack = player.m_21120_(hand);
                ItemStack otherStack = player.m_21120_(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
                ctx.itemHolders = ItemHolderCollection.Inventory.of(ctx, stack, otherStack);
                for (T recipe : Iterables.concat(recipes, this.anyBlockRecipes)) {
                    if (this.tryMatch(recipe, level, ctx).isPresent()) {
                        if (!level.isClientSide && recipe.tickOrApply(ctx)) {
                            int times = Math.min(ctx.getItem(0).getCount(), ctx.getItem(1).getCount());
                            times = recipe.getRandomRepeats(Math.max(1, times), ctx);
                            if (recipe.m_7527_().size() == 1) {
                                ctx.itemHolders.ignoreConsumptionFlags.set(1);
                            }
                            recipe.applyPostActions(ctx, times);
                            ctx.itemHolders.postApply(ctx.runtime.doDefault, times);
                            player.m_21008_(hand, ctx.getItem(0));
                            player.m_21008_(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, ctx.getItem(1));
                        }
                        return Optional.of(recipe);
                    }
                }
                return Optional.empty();
            }
        }
    }

    public boolean has(Block block) {
        return !this.anyBlockRecipes.isEmpty() || this.recipesByBlock.containsKey(block);
    }

    public boolean has(BlockState state) {
        return this.has(state.m_60734_());
    }

    @Nullable
    public Pair<C, T> process(Level level, BlockState state, Supplier<C> ctxSupplier) {
        Collection<T> recipes = (Collection<T>) this.recipesByBlock.getOrDefault(state.m_60734_(), List.of());
        Iterable<T> iterable = Iterables.concat(recipes, this.anyBlockRecipes);
        C ctx = null;
        for (T recipe : iterable) {
            if (this.extractChance) {
                ChanceRecipe $ = (ChanceRecipe) recipe;
                if ($.getChance() != 1.0F && $.getChance() <= level.random.nextFloat()) {
                    continue;
                }
            }
            if (ctx == null) {
                ctx = (C) ctxSupplier.get();
            }
            if (this.tryMatch(recipe, level, ctx).isPresent()) {
                recipe.applyPostActions(ctx, 1);
                return Pair.of(ctx, recipe);
            }
        }
        return null;
    }
}