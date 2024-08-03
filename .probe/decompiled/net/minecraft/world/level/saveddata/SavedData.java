package net.minecraft.world.level.saveddata;

import com.mojang.logging.LogUtils;
import java.io.File;
import java.io.IOException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import org.slf4j.Logger;

public abstract class SavedData {

    private static final Logger LOGGER = LogUtils.getLogger();

    private boolean dirty;

    public abstract CompoundTag save(CompoundTag var1);

    public void setDirty() {
        this.setDirty(true);
    }

    public void setDirty(boolean boolean0) {
        this.dirty = boolean0;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void save(File file0) {
        if (this.isDirty()) {
            CompoundTag $$1 = new CompoundTag();
            $$1.put("data", this.save(new CompoundTag()));
            NbtUtils.addCurrentDataVersion($$1);
            try {
                NbtIo.writeCompressed($$1, file0);
            } catch (IOException var4) {
                LOGGER.error("Could not save data {}", this, var4);
            }
            this.setDirty(false);
        }
    }
}