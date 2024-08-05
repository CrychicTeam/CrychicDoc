package net.minecraft.world.item;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

public class KnowledgeBookItem extends Item {

    private static final String RECIPE_TAG = "Recipes";

    private static final Logger LOGGER = LogUtils.getLogger();

    public KnowledgeBookItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        CompoundTag $$4 = $$3.getTag();
        if (!player1.getAbilities().instabuild) {
            player1.m_21008_(interactionHand2, ItemStack.EMPTY);
        }
        if ($$4 != null && $$4.contains("Recipes", 9)) {
            if (!level0.isClientSide) {
                ListTag $$5 = $$4.getList("Recipes", 8);
                List<Recipe<?>> $$6 = Lists.newArrayList();
                RecipeManager $$7 = level0.getServer().getRecipeManager();
                for (int $$8 = 0; $$8 < $$5.size(); $$8++) {
                    String $$9 = $$5.getString($$8);
                    Optional<? extends Recipe<?>> $$10 = $$7.byKey(new ResourceLocation($$9));
                    if (!$$10.isPresent()) {
                        LOGGER.error("Invalid recipe: {}", $$9);
                        return InteractionResultHolder.fail($$3);
                    }
                    $$6.add((Recipe) $$10.get());
                }
                player1.awardRecipes($$6);
                player1.awardStat(Stats.ITEM_USED.get(this));
            }
            return InteractionResultHolder.sidedSuccess($$3, level0.isClientSide());
        } else {
            LOGGER.error("Tag not valid: {}", $$4);
            return InteractionResultHolder.fail($$3);
        }
    }
}