package com.mna.api.blocks.tile;

import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public interface IMultiblockDefinition extends Recipe<CraftingContainer> {

    boolean match(Level var1, BlockPos var2);

    boolean match(Level var1, BlockPos var2, Rotation var3, boolean var4);

    Pair<Integer, Integer> matchWithCount(Level var1, BlockPos var2, Rotation var3, boolean var4);

    HashMap<BlockPos, BlockState> getMissingBlocks(Level var1, BlockPos var2, Rotation var3, boolean var4);

    List<String> getMatchedVariations();

    boolean spawn(ServerLevel var1, BlockPos var2);

    boolean spawn(ServerLevel var1, BlockPos var2, Rotation var3, boolean var4);

    int getBlockCount();

    boolean isSymmetrical();

    ResourceLocation getStructurePath();

    void setStructurePath(ResourceLocation var1);

    Vec3i getSize();

    List<BlockPos> getPositions(ResourceLocation var1, @Nullable Predicate<BlockState> var2);

    List<BlockPos> getPositions(List<ResourceLocation> var1, @Nullable Predicate<BlockState> var2);

    HashMap<ResourceLocation, Integer> getSpecialBlockMatchersByBlock();

    HashMap<Long, Integer> getSpecialBlockMatchersByOffset();

    void setSpecialBlockMatchersByBlock(HashMap<ResourceLocation, Integer> var1);

    void setSpecialBlockMatchersByOffset(HashMap<Long, Integer> var1);

    @Override
    ResourceLocation getId();
}