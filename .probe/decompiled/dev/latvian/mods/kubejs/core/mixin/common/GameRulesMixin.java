package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.core.GameRulesKJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@RemapPrefixForJS("kjs$")
@Mixin({ GameRules.class })
public abstract class GameRulesMixin implements GameRulesKJS {

    private Map<String, GameRules.Key<?>> kjs$keyCache;

    @Shadow
    public abstract <T extends GameRules.Value<T>> T getRule(GameRules.Key<T> var1);

    @Nullable
    private GameRules.Key<?> getKey(String rule) {
        if (this.kjs$keyCache == null) {
            this.kjs$keyCache = new HashMap();
            GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {

                @Override
                public <T extends GameRules.Value<T>> void visit(GameRules.Key<T> key, GameRules.Type<T> type) {
                    GameRulesMixin.this.kjs$keyCache.put(key.toString(), key);
                }
            });
        }
        return (GameRules.Key<?>) this.kjs$keyCache.get(rule);
    }

    @Nullable
    @Override
    public GameRules.Value<?> kjs$get(String rule) {
        GameRules.Key<? extends GameRules.Value<?>> key = (GameRules.Key<? extends GameRules.Value<?>>) this.getKey(rule);
        return key == null ? null : this.getRule((GameRules.Key<GameRules.Value<?>>) key);
    }

    @Override
    public void kjs$set(String rule, String value) {
        GameRules.Key<? extends GameRules.Value<?>> key = (GameRules.Key<? extends GameRules.Value<?>>) this.getKey(rule);
        GameRules.Value<?> r = key == null ? null : this.getRule((GameRules.Key<GameRules.Value<?>>) key);
        if (r != null) {
            r.deserialize(value);
            if (UtilsJS.staticServer != null) {
                r.onChanged(UtilsJS.staticServer);
            }
        }
    }
}