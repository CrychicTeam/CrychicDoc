package com.illusivesoulworks.polymorph.common.capability;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.api.common.capability.IPlayerRecipeData;
import com.illusivesoulworks.polymorph.client.recipe.RecipesWidget;
import com.mojang.datafixers.util.Pair;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class PlayerRecipeData extends AbstractRecipeData<Player> implements IPlayerRecipeData {

    private AbstractContainerMenu containerMenu;

    private Recipe<?> cachedSelection;

    private int lastAccessTick;

    public PlayerRecipeData(Player owner) {
        super(owner);
    }

    @Override
    public <T extends Recipe<C>, C extends Container> Optional<T> getRecipe(RecipeType<T> type, C inventory, Level level, List<T> recipesList) {
        if (this.getOwner().f_19797_ == this.lastAccessTick) {
            if (this.cachedSelection != null) {
                this.setSelectedRecipe(this.cachedSelection);
            }
        } else {
            this.cachedSelection = null;
        }
        Optional<T> maybeRecipe = super.getRecipe(type, inventory, level, recipesList);
        if (this.getContainerMenu() == this.getOwner().containerMenu) {
            this.syncPlayerRecipeData();
        }
        this.setContainerMenu(null);
        if (this.getOwner().f_19797_ != this.lastAccessTick) {
            this.lastAccessTick = this.getOwner().f_19797_;
            this.cachedSelection = (Recipe<?>) maybeRecipe.orElse(null);
        }
        return maybeRecipe;
    }

    @Override
    public void selectRecipe(@Nonnull Recipe<?> recipe) {
        super.selectRecipe(recipe);
        this.syncPlayerRecipeData();
    }

    private void syncPlayerRecipeData() {
        if (this.getOwner() instanceof ServerPlayer) {
            PolymorphApi.common().getPacketDistributor().sendPlayerSyncS2C((ServerPlayer) this.getOwner(), this.getRecipesList(), (ResourceLocation) this.getSelectedRecipe().map(Recipe::m_6423_).orElse(null));
        }
    }

    @Override
    public void sendRecipesListToListeners(boolean isEmpty) {
        if (this.getContainerMenu() == this.getOwner().containerMenu) {
            Pair<SortedSet<IRecipePair>, ResourceLocation> packetData = isEmpty ? new Pair(new TreeSet(), null) : this.getPacketData();
            Player player = this.getOwner();
            if (player.m_9236_().isClientSide()) {
                RecipesWidget.get().ifPresent(widget -> widget.setRecipesList((Set<IRecipePair>) packetData.getFirst(), (ResourceLocation) packetData.getSecond()));
            } else if (player instanceof ServerPlayer) {
                PolymorphApi.common().getPacketDistributor().sendRecipesListS2C((ServerPlayer) player, (SortedSet<IRecipePair>) packetData.getFirst(), (ResourceLocation) packetData.getSecond());
            }
        }
    }

    @Override
    public Set<ServerPlayer> getListeners() {
        Player player = this.getOwner();
        return (Set<ServerPlayer>) (player instanceof ServerPlayer ? Collections.singleton((ServerPlayer) player) : new HashSet());
    }

    @Override
    public void setContainerMenu(AbstractContainerMenu containerMenu) {
        this.containerMenu = containerMenu;
    }

    @Override
    public AbstractContainerMenu getContainerMenu() {
        return this.containerMenu;
    }
}