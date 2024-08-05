package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.resources.language.I18n;

public class RealmsWorldOptions extends ValueObject {

    public final boolean pvp;

    public final boolean spawnAnimals;

    public final boolean spawnMonsters;

    public final boolean spawnNPCs;

    public final int spawnProtection;

    public final boolean commandBlocks;

    public final boolean forceGameMode;

    public final int difficulty;

    public final int gameMode;

    @Nullable
    private final String slotName;

    public long templateId;

    @Nullable
    public String templateImage;

    public boolean empty;

    private static final boolean DEFAULT_FORCE_GAME_MODE = false;

    private static final boolean DEFAULT_PVP = true;

    private static final boolean DEFAULT_SPAWN_ANIMALS = true;

    private static final boolean DEFAULT_SPAWN_MONSTERS = true;

    private static final boolean DEFAULT_SPAWN_NPCS = true;

    private static final int DEFAULT_SPAWN_PROTECTION = 0;

    private static final boolean DEFAULT_COMMAND_BLOCKS = false;

    private static final int DEFAULT_DIFFICULTY = 2;

    private static final int DEFAULT_GAME_MODE = 0;

    private static final String DEFAULT_SLOT_NAME = "";

    private static final long DEFAULT_TEMPLATE_ID = -1L;

    private static final String DEFAULT_TEMPLATE_IMAGE = null;

    public RealmsWorldOptions(boolean boolean0, boolean boolean1, boolean boolean2, boolean boolean3, int int4, boolean boolean5, int int6, int int7, boolean boolean8, @Nullable String string9) {
        this.pvp = boolean0;
        this.spawnAnimals = boolean1;
        this.spawnMonsters = boolean2;
        this.spawnNPCs = boolean3;
        this.spawnProtection = int4;
        this.commandBlocks = boolean5;
        this.difficulty = int6;
        this.gameMode = int7;
        this.forceGameMode = boolean8;
        this.slotName = string9;
    }

    public static RealmsWorldOptions createDefaults() {
        return new RealmsWorldOptions(true, true, true, true, 0, false, 2, 0, false, "");
    }

    public static RealmsWorldOptions createEmptyDefaults() {
        RealmsWorldOptions $$0 = createDefaults();
        $$0.setEmpty(true);
        return $$0;
    }

    public void setEmpty(boolean boolean0) {
        this.empty = boolean0;
    }

    public static RealmsWorldOptions parse(JsonObject jsonObject0) {
        RealmsWorldOptions $$1 = new RealmsWorldOptions(JsonUtils.getBooleanOr("pvp", jsonObject0, true), JsonUtils.getBooleanOr("spawnAnimals", jsonObject0, true), JsonUtils.getBooleanOr("spawnMonsters", jsonObject0, true), JsonUtils.getBooleanOr("spawnNPCs", jsonObject0, true), JsonUtils.getIntOr("spawnProtection", jsonObject0, 0), JsonUtils.getBooleanOr("commandBlocks", jsonObject0, false), JsonUtils.getIntOr("difficulty", jsonObject0, 2), JsonUtils.getIntOr("gameMode", jsonObject0, 0), JsonUtils.getBooleanOr("forceGameMode", jsonObject0, false), JsonUtils.getStringOr("slotName", jsonObject0, ""));
        $$1.templateId = JsonUtils.getLongOr("worldTemplateId", jsonObject0, -1L);
        $$1.templateImage = JsonUtils.getStringOr("worldTemplateImage", jsonObject0, DEFAULT_TEMPLATE_IMAGE);
        return $$1;
    }

    public String getSlotName(int int0) {
        if (this.slotName != null && !this.slotName.isEmpty()) {
            return this.slotName;
        } else {
            return this.empty ? I18n.get("mco.configure.world.slot.empty") : this.getDefaultSlotName(int0);
        }
    }

    public String getDefaultSlotName(int int0) {
        return I18n.get("mco.configure.world.slot", int0);
    }

    public String toJson() {
        JsonObject $$0 = new JsonObject();
        if (!this.pvp) {
            $$0.addProperty("pvp", this.pvp);
        }
        if (!this.spawnAnimals) {
            $$0.addProperty("spawnAnimals", this.spawnAnimals);
        }
        if (!this.spawnMonsters) {
            $$0.addProperty("spawnMonsters", this.spawnMonsters);
        }
        if (!this.spawnNPCs) {
            $$0.addProperty("spawnNPCs", this.spawnNPCs);
        }
        if (this.spawnProtection != 0) {
            $$0.addProperty("spawnProtection", this.spawnProtection);
        }
        if (this.commandBlocks) {
            $$0.addProperty("commandBlocks", this.commandBlocks);
        }
        if (this.difficulty != 2) {
            $$0.addProperty("difficulty", this.difficulty);
        }
        if (this.gameMode != 0) {
            $$0.addProperty("gameMode", this.gameMode);
        }
        if (this.forceGameMode) {
            $$0.addProperty("forceGameMode", this.forceGameMode);
        }
        if (!Objects.equals(this.slotName, "")) {
            $$0.addProperty("slotName", this.slotName);
        }
        return $$0.toString();
    }

    public RealmsWorldOptions clone() {
        return new RealmsWorldOptions(this.pvp, this.spawnAnimals, this.spawnMonsters, this.spawnNPCs, this.spawnProtection, this.commandBlocks, this.difficulty, this.gameMode, this.forceGameMode, this.slotName);
    }
}