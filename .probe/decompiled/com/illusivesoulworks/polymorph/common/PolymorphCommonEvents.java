package com.illusivesoulworks.polymorph.common;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.common.base.IPolymorphCommon;
import com.illusivesoulworks.polymorph.api.common.base.IPolymorphPacketDistributor;
import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.common.integration.PolymorphIntegrations;
import com.illusivesoulworks.polymorph.common.util.BlockEntityTicker;
import com.mojang.datafixers.util.Pair;
import java.util.SortedSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;

public class PolymorphCommonEvents {

    public static void levelTick(Level level) {
        if (!level.isClientSide() && level.getGameTime() % 5L == 0L) {
            BlockEntityTicker.tick();
        }
    }

    public static void playerDisconnected(ServerPlayer serverPlayer) {
        BlockEntityTicker.remove(serverPlayer);
    }

    public static void openContainer(Player player, AbstractContainerMenu containerMenu) {
        if (!player.m_9236_().isClientSide() && player instanceof ServerPlayer serverPlayerEntity) {
            IPolymorphCommon commonApi = PolymorphApi.common();
            commonApi.getRecipeDataFromBlockEntity(containerMenu).ifPresent(recipeData -> {
                IPolymorphPacketDistributor packetDistributor = commonApi.getPacketDistributor();
                if (!recipeData.isFailing() && !recipeData.isEmpty(null)) {
                    Pair<SortedSet<IRecipePair>, ResourceLocation> data = recipeData.getPacketData();
                    packetDistributor.sendRecipesListS2C(serverPlayerEntity, (SortedSet<IRecipePair>) data.getFirst(), (ResourceLocation) data.getSecond());
                } else {
                    packetDistributor.sendRecipesListS2C(serverPlayerEntity);
                }
            });
            PolymorphIntegrations.openContainer(containerMenu, serverPlayerEntity);
        }
    }
}