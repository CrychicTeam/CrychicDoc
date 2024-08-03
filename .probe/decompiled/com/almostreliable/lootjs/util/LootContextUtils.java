package com.almostreliable.lootjs.util;

import com.almostreliable.lootjs.core.ILootContextData;
import com.almostreliable.lootjs.core.LootJSParamSets;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class LootContextUtils {

    @Nullable
    public static ServerPlayer getPlayerOrNull(LootContext context) {
        ILootContextData data = context.getParamOrNull(LootJSParamSets.DATA);
        if (data == null) {
            return null;
        } else {
            switch(data.getLootContextType()) {
                case BLOCK:
                case CHEST:
                    return tryGetPlayer(context.getParamOrNull(LootContextParams.THIS_ENTITY));
                case ENTITY:
                    ServerPlayer player = tryGetPlayer(context.getParamOrNull(LootContextParams.KILLER_ENTITY));
                    if (player != null) {
                        return player;
                    }
                    return tryGetPlayer(context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER));
                case FISHING:
                    return tryGetPlayer(context.getParamOrNull(LootContextParams.KILLER_ENTITY));
                default:
                    return null;
            }
        }
    }

    @Nullable
    private static ServerPlayer tryGetPlayer(@Nullable Entity entity) {
        return entity instanceof ServerPlayer ? (ServerPlayer) entity : null;
    }
}