package fuzs.puzzleslib.api.init.v2;

import fuzs.puzzleslib.impl.core.CommonFactories;
import java.util.function.BiConsumer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

public interface GameRulesFactory {

    GameRulesFactory INSTANCE = CommonFactories.INSTANCE.getGameRulesFactory();

    default GameRules.Key<GameRules.BooleanValue> registerBooleanRule(String name, GameRules.Category category, boolean defaultValue) {
        return this.register(name, category, this.createBooleanRule(defaultValue));
    }

    default GameRules.Key<GameRules.IntegerValue> registerIntRule(String name, GameRules.Category category, int defaultValue) {
        return this.register(name, category, this.createIntRule(defaultValue));
    }

    <T extends GameRules.Value<T>> GameRules.Key<T> register(String var1, GameRules.Category var2, GameRules.Type<T> var3);

    default GameRules.Type<GameRules.BooleanValue> createBooleanRule(boolean defaultValue) {
        return this.createBooleanRule(defaultValue, (server, booleanValue) -> {
        });
    }

    GameRules.Type<GameRules.BooleanValue> createBooleanRule(boolean var1, BiConsumer<MinecraftServer, GameRules.BooleanValue> var2);

    default GameRules.Type<GameRules.IntegerValue> createIntRule(int defaultValue) {
        return this.createIntRule(defaultValue, Integer.MIN_VALUE);
    }

    default GameRules.Type<GameRules.IntegerValue> createIntRule(int defaultValue, int minimumValue) {
        return this.createIntRule(defaultValue, minimumValue, Integer.MAX_VALUE);
    }

    default GameRules.Type<GameRules.IntegerValue> createIntRule(int defaultValue, int minimumValue, int maximumValue) {
        return this.createIntRule(defaultValue, minimumValue, maximumValue, (server, integerValue) -> {
        });
    }

    default GameRules.Type<GameRules.IntegerValue> createIntRule(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> callback) {
        return this.createIntRule(defaultValue, Integer.MIN_VALUE, callback);
    }

    default GameRules.Type<GameRules.IntegerValue> createIntRule(int defaultValue, int minimumValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> callback) {
        return this.createIntRule(defaultValue, minimumValue, Integer.MAX_VALUE, callback);
    }

    GameRules.Type<GameRules.IntegerValue> createIntRule(int var1, int var2, int var3, BiConsumer<MinecraftServer, GameRules.IntegerValue> var4);
}