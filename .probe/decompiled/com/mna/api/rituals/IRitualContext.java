package com.mna.api.rituals;

import com.mna.api.recipes.IRitualRecipe;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IRitualContext {

    Player getCaster();

    IRitualRecipe getRecipe();

    NonNullList<RitualBlockPos> getAllPositions();

    NonNullList<RitualBlockPos> getIndexedPositions();

    BlockPos getCenter();

    List<ItemStack> getCollectedReagents();

    List<ItemStack> getCollectedReagents(Predicate<ItemStack> var1);

    List<ResourceLocation> getCollectedPatterns();

    List<ResourceLocation> getCollectedPatterns(Predicate<ResourceLocation> var1);

    Level getLevel();

    void replaceReagents(ResourceLocation var1, NonNullList<ResourceLocation> var2);

    void replaceReagents(ResourceLocation var1, ResourceLocation var2);

    void replacePatterns(NonNullList<ResourceLocation> var1);

    void appendPatterns(NonNullList<ResourceLocation> var1);
}