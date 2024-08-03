package io.redspace.ironsspellbooks.data;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import java.io.File;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.jetbrains.annotations.NotNull;

public class DataFixerStorage extends SavedData {

    public static DataFixerStorage INSTANCE;

    private DimensionDataStorage overworldDataStorage;

    private int dataVersion;

    public static void init(LevelStorageSource.LevelStorageAccess levelStorageAccess) {
        DataFixer dataFixer = new DataFixerBuilder(1).buildUnoptimized();
        File file = levelStorageAccess.getDimensionPath(Level.OVERWORLD).resolve("data").toFile();
        try {
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception var4) {
        }
        DimensionDataStorage overworldDataStorage = new DimensionDataStorage(file, dataFixer);
        INSTANCE = overworldDataStorage.computeIfAbsent(DataFixerStorage::load, DataFixerStorage::new, "irons_spellbooks");
        INSTANCE.overworldDataStorage = overworldDataStorage;
    }

    public DataFixerStorage() {
        this.dataVersion = 0;
    }

    public DataFixerStorage(int dataVersion) {
        this.dataVersion = dataVersion;
    }

    public int getDataVersion() {
        return this.dataVersion;
    }

    public void setDataVersion(int dataVersion) {
        this.dataVersion = dataVersion;
        this.m_77762_();
        this.overworldDataStorage.save();
    }

    @NotNull
    @Override
    public CompoundTag save(@NotNull CompoundTag pCompoundTag) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("dataVersion", this.dataVersion);
        return tag;
    }

    public static DataFixerStorage load(CompoundTag tag) {
        int dataVersion = tag.getInt("dataVersion");
        return new DataFixerStorage(dataVersion);
    }
}