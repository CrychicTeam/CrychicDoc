package com.illusivesoulworks.polymorph.common.capability;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.mojang.datafixers.util.Pair;
import java.util.SortedSet;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractHighlightedRecipeData<E extends BlockEntity> extends AbstractBlockEntityRecipeData<E> {

    public AbstractHighlightedRecipeData(E owner) {
        super(owner);
    }

    @Override
    public void selectRecipe(@Nonnull Recipe<?> recipe) {
        super.selectRecipe(recipe);
        for (ServerPlayer listeningPlayer : this.getListeners()) {
            PolymorphApi.common().getPacketDistributor().sendHighlightRecipeS2C(listeningPlayer, recipe.getId());
        }
    }

    @Override
    public Pair<SortedSet<IRecipePair>, ResourceLocation> getPacketData() {
        SortedSet<IRecipePair> recipesList = this.getRecipesList();
        ResourceLocation selected = null;
        if (!recipesList.isEmpty()) {
            selected = (ResourceLocation) this.getSelectedRecipe().map(Recipe::m_6423_).orElse(((IRecipePair) recipesList.first()).getResourceLocation());
        }
        return new Pair(recipesList, selected);
    }
}