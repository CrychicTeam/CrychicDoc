package snownee.lychee.block_crushing;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.Lychee;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.LycheeTags;
import snownee.lychee.core.input.ItemHolder;
import snownee.lychee.core.network.SCustomLevelEventPacket;
import snownee.lychee.core.recipe.type.BlockKeyRecipeType;
import snownee.lychee.core.recipe.type.LycheeRecipeType;
import snownee.lychee.util.CommonProxy;

public class BlockCrushingRecipeType extends BlockKeyRecipeType<BlockCrushingContext, BlockCrushingRecipe> {

    private LycheeRecipeType.ValidItemCache validItems = new LycheeRecipeType.ValidItemCache();

    public BlockCrushingRecipeType(String name, Class<BlockCrushingRecipe> clazz, @Nullable LootContextParamSet paramSet) {
        super(name, clazz, paramSet);
        this.compactInputs = true;
    }

    public void process(FallingBlockEntity entity) {
        if (!this.isEmpty()) {
            BlockPos pos = CommonProxy.getOnPos(entity);
            BlockState fallingBlock = entity.getBlockState();
            Collection<BlockCrushingRecipe> recipes = (Collection<BlockCrushingRecipe>) this.recipesByBlock.getOrDefault(fallingBlock.m_60734_(), Collections.EMPTY_LIST);
            if (!recipes.isEmpty()) {
                AABB box = entity.m_20191_();
                Level level = entity.m_9236_();
                BlockState landingBlock = level.getBlockState(pos);
                if (landingBlock.m_204336_(LycheeTags.EXTEND_BOX)) {
                    box = box.minmax(new AABB(pos));
                }
                List<ItemEntity> itemEntities = entity.m_9236_().m_6443_(ItemEntity.class, box, $ -> $.m_6084_() && this.validItems.contains($.getItem()));
                BlockCrushingContext.Builder ctxBuilder = new BlockCrushingContext.Builder(entity.m_9236_(), itemEntities, entity);
                ctxBuilder.withParameter(LootContextParams.ORIGIN, entity.m_20182_());
                ctxBuilder.withParameter(LootContextParams.THIS_ENTITY, entity);
                ctxBuilder.withParameter(LootContextParams.BLOCK_STATE, landingBlock);
                ctxBuilder.withParameter(LycheeLootContextParams.BLOCK_POS, pos);
                BlockCrushingContext ctx = ctxBuilder.create(this.contextParamSet);
                boolean matchedAny = false;
                int loop = 0;
                boolean matched;
                label107: do {
                    matched = false;
                    for (BlockCrushingRecipe recipe : recipes) {
                        if (!recipe.ingredients.isEmpty() || loop <= 0) {
                            try {
                                Optional<BlockCrushingRecipe> match = this.tryMatch(recipe, level, ctx);
                                if (match.isPresent()) {
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
                                    ((BlockCrushingRecipe) match.get()).applyPostActions(ctx, times);
                                    if (!ctx.runtime.doDefault) {
                                        ((LycheeFallingBlockEntity) entity).lychee$cancelDrop();
                                    }
                                    if (CommonProxy.hasKiwi) {
                                        Set<ItemHolder> alreadySentParticles = Sets.newHashSet();
                                        for (int ix = 0; ix < ctx.itemHolders.size(); ix++) {
                                            ItemHolder holder = ctx.itemHolders.get(ix);
                                            if (!ctx.itemHolders.ignoreConsumptionFlags.get(ix) && !holder.get().isEmpty() && holder instanceof ItemHolder.InWorld && !alreadySentParticles.contains(holder)) {
                                                alreadySentParticles.add(holder);
                                                SCustomLevelEventPacket.sendItemParticles(holder.get(), ctx.getServerLevel(), ((ItemHolder.InWorld) holder).getEntity().m_20182_());
                                            }
                                        }
                                    }
                                    ctx.totalItems = ctx.totalItems - ctx.itemHolders.postApply(true, times);
                                    if (!recipe.getMaxRepeats().m_55327_()) {
                                        break label107;
                                    }
                                    ctx.filteredItems = null;
                                    ctx.setMatch(null);
                                    ctx.itemEntities.removeIf($ -> $.getItem().isEmpty());
                                }
                            } catch (Exception var21) {
                                Lychee.LOGGER.error("", var21);
                                break label107;
                            }
                        }
                    }
                } while (++loop < 100 && matched);
                if (matchedAny) {
                    BlockState state = level.getBlockState(entity.m_20183_());
                    if (!FallingBlock.isFree(state)) {
                        entity.m_20343_(entity.m_20185_(), (double) (pos.m_123342_() + 1), entity.m_20189_());
                    }
                    ((LycheeFallingBlockEntity) entity).lychee$matched();
                    ctx.itemEntities.forEach($ -> $.setItem($.getItem().copy()));
                }
            }
        }
    }

    @Override
    public void buildCache() {
        super.buildCache();
        this.validItems.buildCache(this.recipes);
    }
}