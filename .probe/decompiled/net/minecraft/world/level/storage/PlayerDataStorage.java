package net.minecraft.world.level.storage;

import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import java.io.File;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public class PlayerDataStorage {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final File playerDir;

    protected final DataFixer fixerUpper;

    public PlayerDataStorage(LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess0, DataFixer dataFixer1) {
        this.fixerUpper = dataFixer1;
        this.playerDir = levelStorageSourceLevelStorageAccess0.getLevelPath(LevelResource.PLAYER_DATA_DIR).toFile();
        this.playerDir.mkdirs();
    }

    public void save(Player player0) {
        try {
            CompoundTag $$1 = player0.m_20240_(new CompoundTag());
            File $$2 = File.createTempFile(player0.m_20149_() + "-", ".dat", this.playerDir);
            NbtIo.writeCompressed($$1, $$2);
            File $$3 = new File(this.playerDir, player0.m_20149_() + ".dat");
            File $$4 = new File(this.playerDir, player0.m_20149_() + ".dat_old");
            Util.safeReplaceFile($$3, $$2, $$4);
        } catch (Exception var6) {
            LOGGER.warn("Failed to save player data for {}", player0.getName().getString());
        }
    }

    @Nullable
    public CompoundTag load(Player player0) {
        CompoundTag $$1 = null;
        try {
            File $$2 = new File(this.playerDir, player0.m_20149_() + ".dat");
            if ($$2.exists() && $$2.isFile()) {
                $$1 = NbtIo.readCompressed($$2);
            }
        } catch (Exception var4) {
            LOGGER.warn("Failed to load player data for {}", player0.getName().getString());
        }
        if ($$1 != null) {
            int $$4 = NbtUtils.getDataVersion($$1, -1);
            player0.m_20258_(DataFixTypes.PLAYER.updateToCurrentVersion(this.fixerUpper, $$1, $$4));
        }
        return $$1;
    }

    public String[] getSeenPlayers() {
        String[] $$0 = this.playerDir.list();
        if ($$0 == null) {
            $$0 = new String[0];
        }
        for (int $$1 = 0; $$1 < $$0.length; $$1++) {
            if ($$0[$$1].endsWith(".dat")) {
                $$0[$$1] = $$0[$$1].substring(0, $$0[$$1].length() - 4);
            }
        }
        return $$0;
    }
}