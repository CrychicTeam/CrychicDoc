package com.illusivesoulworks.polymorph.common.util;

import com.illusivesoulworks.polymorph.api.common.capability.IBlockEntityRecipeData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityTicker {

    private static final Map<BlockEntity, IBlockEntityRecipeData> TICKABLE_BLOCKS = new ConcurrentHashMap();

    public static void tick() {
        List<BlockEntity> toRemove = new ArrayList();
        for (Entry<BlockEntity, IBlockEntityRecipeData> entry : TICKABLE_BLOCKS.entrySet()) {
            BlockEntity be = (BlockEntity) entry.getKey();
            if (!be.isRemoved() && be.getLevel() != null && !be.getLevel().isClientSide()) {
                ((IBlockEntityRecipeData) entry.getValue()).tick();
            } else {
                toRemove.add(be);
            }
        }
        for (BlockEntity be : toRemove) {
            TICKABLE_BLOCKS.remove(be);
        }
    }

    public static void add(ServerPlayer serverPlayer, IBlockEntityRecipeData recipeData) {
        IBlockEntityRecipeData data = (IBlockEntityRecipeData) TICKABLE_BLOCKS.get(recipeData.getOwner());
        if (data != null) {
            data.addListener(serverPlayer);
        } else {
            recipeData.addListener(serverPlayer);
            TICKABLE_BLOCKS.put(recipeData.getOwner(), recipeData);
        }
    }

    public static void remove(ServerPlayer serverPlayer) {
        List<BlockEntity> toRemove = new ArrayList();
        for (Entry<BlockEntity, IBlockEntityRecipeData> entry : TICKABLE_BLOCKS.entrySet()) {
            ((IBlockEntityRecipeData) entry.getValue()).removeListener(serverPlayer);
            if (((IBlockEntityRecipeData) entry.getValue()).getListeners().isEmpty()) {
                toRemove.add((BlockEntity) entry.getKey());
            }
        }
        for (BlockEntity blockEntity : toRemove) {
            TICKABLE_BLOCKS.remove(blockEntity);
        }
    }

    public static void clear() {
        TICKABLE_BLOCKS.clear();
    }
}