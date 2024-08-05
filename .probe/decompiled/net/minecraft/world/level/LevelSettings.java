package net.minecraft.world.level;

import com.mojang.serialization.Dynamic;
import net.minecraft.world.Difficulty;

public final class LevelSettings {

    private final String levelName;

    private final GameType gameType;

    private final boolean hardcore;

    private final Difficulty difficulty;

    private final boolean allowCommands;

    private final GameRules gameRules;

    private final WorldDataConfiguration dataConfiguration;

    public LevelSettings(String string0, GameType gameType1, boolean boolean2, Difficulty difficulty3, boolean boolean4, GameRules gameRules5, WorldDataConfiguration worldDataConfiguration6) {
        this.levelName = string0;
        this.gameType = gameType1;
        this.hardcore = boolean2;
        this.difficulty = difficulty3;
        this.allowCommands = boolean4;
        this.gameRules = gameRules5;
        this.dataConfiguration = worldDataConfiguration6;
    }

    public static LevelSettings parse(Dynamic<?> dynamic0, WorldDataConfiguration worldDataConfiguration1) {
        GameType $$2 = GameType.byId(dynamic0.get("GameType").asInt(0));
        return new LevelSettings(dynamic0.get("LevelName").asString(""), $$2, dynamic0.get("hardcore").asBoolean(false), (Difficulty) dynamic0.get("Difficulty").asNumber().map(p_46928_ -> Difficulty.byId(p_46928_.byteValue())).result().orElse(Difficulty.NORMAL), dynamic0.get("allowCommands").asBoolean($$2 == GameType.CREATIVE), new GameRules(dynamic0.get("GameRules")), worldDataConfiguration1);
    }

    public String levelName() {
        return this.levelName;
    }

    public GameType gameType() {
        return this.gameType;
    }

    public boolean hardcore() {
        return this.hardcore;
    }

    public Difficulty difficulty() {
        return this.difficulty;
    }

    public boolean allowCommands() {
        return this.allowCommands;
    }

    public GameRules gameRules() {
        return this.gameRules;
    }

    public WorldDataConfiguration getDataConfiguration() {
        return this.dataConfiguration;
    }

    public LevelSettings withGameType(GameType gameType0) {
        return new LevelSettings(this.levelName, gameType0, this.hardcore, this.difficulty, this.allowCommands, this.gameRules, this.dataConfiguration);
    }

    public LevelSettings withDifficulty(Difficulty difficulty0) {
        return new LevelSettings(this.levelName, this.gameType, this.hardcore, difficulty0, this.allowCommands, this.gameRules, this.dataConfiguration);
    }

    public LevelSettings withDataConfiguration(WorldDataConfiguration worldDataConfiguration0) {
        return new LevelSettings(this.levelName, this.gameType, this.hardcore, this.difficulty, this.allowCommands, this.gameRules, worldDataConfiguration0);
    }

    public LevelSettings copy() {
        return new LevelSettings(this.levelName, this.gameType, this.hardcore, this.difficulty, this.allowCommands, this.gameRules.copy(), this.dataConfiguration);
    }
}