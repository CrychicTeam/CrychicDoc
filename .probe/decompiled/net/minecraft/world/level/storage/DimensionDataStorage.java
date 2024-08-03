package net.minecraft.world.level.storage;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;

public class DimensionDataStorage {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<String, SavedData> cache = Maps.newHashMap();

    private final DataFixer fixerUpper;

    private final File dataFolder;

    public DimensionDataStorage(File file0, DataFixer dataFixer1) {
        this.fixerUpper = dataFixer1;
        this.dataFolder = file0;
    }

    private File getDataFile(String string0) {
        return new File(this.dataFolder, string0 + ".dat");
    }

    public <T extends SavedData> T computeIfAbsent(Function<CompoundTag, T> functionCompoundTagT0, Supplier<T> supplierT1, String string2) {
        T $$3 = this.get(functionCompoundTagT0, string2);
        if ($$3 != null) {
            return $$3;
        } else {
            T $$4 = (T) supplierT1.get();
            this.set(string2, $$4);
            return $$4;
        }
    }

    @Nullable
    public <T extends SavedData> T get(Function<CompoundTag, T> functionCompoundTagT0, String string1) {
        SavedData $$2 = (SavedData) this.cache.get(string1);
        if ($$2 == null && !this.cache.containsKey(string1)) {
            $$2 = this.readSavedData(functionCompoundTagT0, string1);
            this.cache.put(string1, $$2);
        }
        return (T) $$2;
    }

    @Nullable
    private <T extends SavedData> T readSavedData(Function<CompoundTag, T> functionCompoundTagT0, String string1) {
        try {
            File $$2 = this.getDataFile(string1);
            if ($$2.exists()) {
                CompoundTag $$3 = this.readTagFromDisk(string1, SharedConstants.getCurrentVersion().getDataVersion().getVersion());
                return (T) functionCompoundTagT0.apply($$3.getCompound("data"));
            }
        } catch (Exception var5) {
            LOGGER.error("Error loading saved data: {}", string1, var5);
        }
        return null;
    }

    public void set(String string0, SavedData savedData1) {
        this.cache.put(string0, savedData1);
    }

    public CompoundTag readTagFromDisk(String string0, int int1) throws IOException {
        File $$2 = this.getDataFile(string0);
        FileInputStream $$3 = new FileInputStream($$2);
        CompoundTag var8;
        try {
            PushbackInputStream $$4 = new PushbackInputStream($$3, 2);
            try {
                CompoundTag $$5;
                if (this.isGzip($$4)) {
                    $$5 = NbtIo.readCompressed($$4);
                } else {
                    DataInputStream $$6 = new DataInputStream($$4);
                    try {
                        $$5 = NbtIo.read($$6);
                    } catch (Throwable var13) {
                        try {
                            $$6.close();
                        } catch (Throwable var12) {
                            var13.addSuppressed(var12);
                        }
                        throw var13;
                    }
                    $$6.close();
                }
                int $$9 = NbtUtils.getDataVersion($$5, 1343);
                var8 = DataFixTypes.SAVED_DATA.update(this.fixerUpper, $$5, $$9, int1);
            } catch (Throwable var14) {
                try {
                    $$4.close();
                } catch (Throwable var11) {
                    var14.addSuppressed(var11);
                }
                throw var14;
            }
            $$4.close();
        } catch (Throwable var15) {
            try {
                $$3.close();
            } catch (Throwable var10) {
                var15.addSuppressed(var10);
            }
            throw var15;
        }
        $$3.close();
        return var8;
    }

    private boolean isGzip(PushbackInputStream pushbackInputStream0) throws IOException {
        byte[] $$1 = new byte[2];
        boolean $$2 = false;
        int $$3 = pushbackInputStream0.read($$1, 0, 2);
        if ($$3 == 2) {
            int $$4 = ($$1[1] & 255) << 8 | $$1[0] & 255;
            if ($$4 == 35615) {
                $$2 = true;
            }
        }
        if ($$3 != 0) {
            pushbackInputStream0.unread($$1, 0, $$3);
        }
        return $$2;
    }

    public void save() {
        this.cache.forEach((p_164866_, p_164867_) -> {
            if (p_164867_ != null) {
                p_164867_.save(this.getDataFile(p_164866_));
            }
        });
    }
}