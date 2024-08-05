package com.illusivesoulworks.polymorph.api.common.capability;

import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public interface IRecipeData<E> {

    <T extends Recipe<C>, C extends Container> Optional<T> getRecipe(RecipeType<T> var1, C var2, Level var3, List<T> var4);

    void selectRecipe(@Nonnull Recipe<?> var1);

    Optional<? extends Recipe<?>> getSelectedRecipe();

    void setSelectedRecipe(@Nonnull Recipe<?> var1);

    @Nonnull
    SortedSet<IRecipePair> getRecipesList();

    void setRecipesList(@Nonnull SortedSet<IRecipePair> var1);

    boolean isEmpty(Container var1);

    Set<ServerPlayer> getListeners();

    void sendRecipesListToListeners(boolean var1);

    Pair<SortedSet<IRecipePair>, ResourceLocation> getPacketData();

    E getOwner();

    boolean isFailing();

    void setFailing(boolean var1);

    @Nonnull
    CompoundTag writeNBT();

    void readNBT(CompoundTag var1);
}