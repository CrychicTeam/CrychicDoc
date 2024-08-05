package net.minecraft.world.level.storage;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import net.minecraft.SharedConstants;

public class LevelVersion {

    private final int levelDataVersion;

    private final long lastPlayed;

    private final String minecraftVersionName;

    private final DataVersion minecraftVersion;

    private final boolean snapshot;

    private LevelVersion(int int0, long long1, String string2, int int3, String string4, boolean boolean5) {
        this.levelDataVersion = int0;
        this.lastPlayed = long1;
        this.minecraftVersionName = string2;
        this.minecraftVersion = new DataVersion(int3, string4);
        this.snapshot = boolean5;
    }

    public static LevelVersion parse(Dynamic<?> dynamic0) {
        int $$1 = dynamic0.get("version").asInt(0);
        long $$2 = dynamic0.get("LastPlayed").asLong(0L);
        OptionalDynamic<?> $$3 = dynamic0.get("Version");
        return $$3.result().isPresent() ? new LevelVersion($$1, $$2, $$3.get("Name").asString(SharedConstants.getCurrentVersion().getName()), $$3.get("Id").asInt(SharedConstants.getCurrentVersion().getDataVersion().getVersion()), $$3.get("Series").asString(DataVersion.MAIN_SERIES), $$3.get("Snapshot").asBoolean(!SharedConstants.getCurrentVersion().isStable())) : new LevelVersion($$1, $$2, "", 0, DataVersion.MAIN_SERIES, false);
    }

    public int levelDataVersion() {
        return this.levelDataVersion;
    }

    public long lastPlayed() {
        return this.lastPlayed;
    }

    public String minecraftVersionName() {
        return this.minecraftVersionName;
    }

    public DataVersion minecraftVersion() {
        return this.minecraftVersion;
    }

    public boolean snapshot() {
        return this.snapshot;
    }
}