package com.illusivesoulworks.polymorph.api.common.base;

import java.util.SortedSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public interface IPolymorphPacketDistributor {

    void sendPlayerRecipeSelectionC2S(ResourceLocation var1);

    void sendPersistentRecipeSelectionC2S(ResourceLocation var1);

    void sendStackRecipeSelectionC2S(ResourceLocation var1);

    void sendRecipesListS2C(ServerPlayer var1);

    void sendRecipesListS2C(ServerPlayer var1, SortedSet<IRecipePair> var2);

    void sendRecipesListS2C(ServerPlayer var1, SortedSet<IRecipePair> var2, ResourceLocation var3);

    void sendHighlightRecipeS2C(ServerPlayer var1, ResourceLocation var2);

    void sendPlayerSyncS2C(ServerPlayer var1, SortedSet<IRecipePair> var2, ResourceLocation var3);

    void sendBlockEntitySyncS2C(BlockPos var1, ResourceLocation var2);

    void sendBlockEntityListenerC2S(boolean var1);
}