package com.mna.interop.lootr;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import noobanidus.mods.lootr.data.DataStorage;

public class LootrIntegration {

    public static <C extends RandomizableContainerBlockEntity & ILootrBridge> void openCache(C cache, ServerPlayer player) {
        LootBlockTileWrapper<C> wrapper = new LootBlockTileWrapper<>(cache);
        if (cache.getOpeners().add(player.m_20148_())) {
            cache.m_6596_();
        }
        MenuProvider provider = DataStorage.getInventory(cache.m_58904_(), wrapper.getTileId(), cache.m_58899_(), player, wrapper, wrapper::unpackLootTable);
        player.openMenu(provider);
    }
}