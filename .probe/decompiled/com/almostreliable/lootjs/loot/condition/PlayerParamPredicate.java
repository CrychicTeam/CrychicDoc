package com.almostreliable.lootjs.loot.condition;

import com.almostreliable.lootjs.util.LootContextUtils;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;

public class PlayerParamPredicate implements IExtendedLootCondition {

    private final Predicate<ServerPlayer> predicate;

    public PlayerParamPredicate(Predicate<ServerPlayer> predicate) {
        Objects.requireNonNull(predicate);
        this.predicate = predicate;
    }

    public boolean test(LootContext lootContext) {
        ServerPlayer player = LootContextUtils.getPlayerOrNull(lootContext);
        return player != null && this.predicate.test(player);
    }
}