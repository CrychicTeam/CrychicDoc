package dev.latvian.mods.kubejs.core;

import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.Nullable;

public interface GameRulesKJS {

    @Nullable
    GameRules.Value<?> kjs$get(String var1);

    void kjs$set(String var1, String var2);

    default String kjs$getString(String rule) {
        GameRules.Value<? extends GameRules.Value<?>> o = (GameRules.Value<? extends GameRules.Value<?>>) this.kjs$get(rule);
        return o == null ? "" : o.serialize();
    }

    default boolean kjs$getBoolean(String rule) {
        if (this.kjs$get(rule) instanceof GameRules.BooleanValue v && v.get()) {
            return true;
        }
        return false;
    }

    default int kjs$getInt(String rule) {
        return this.kjs$get(rule) instanceof GameRules.IntegerValue v ? v.get() : 0;
    }
}