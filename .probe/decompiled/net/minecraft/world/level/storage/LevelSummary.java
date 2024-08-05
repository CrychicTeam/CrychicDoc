package net.minecraft.world.level.storage;

import java.nio.file.Path;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.WorldVersion;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import org.apache.commons.lang3.StringUtils;

public class LevelSummary implements Comparable<LevelSummary> {

    private final LevelSettings settings;

    private final LevelVersion levelVersion;

    private final String levelId;

    private final boolean requiresManualConversion;

    private final boolean locked;

    private final boolean experimental;

    private final Path icon;

    @Nullable
    private Component info;

    public LevelSummary(LevelSettings levelSettings0, LevelVersion levelVersion1, String string2, boolean boolean3, boolean boolean4, boolean boolean5, Path path6) {
        this.settings = levelSettings0;
        this.levelVersion = levelVersion1;
        this.levelId = string2;
        this.locked = boolean4;
        this.experimental = boolean5;
        this.icon = path6;
        this.requiresManualConversion = boolean3;
    }

    public String getLevelId() {
        return this.levelId;
    }

    public String getLevelName() {
        return StringUtils.isEmpty(this.settings.levelName()) ? this.levelId : this.settings.levelName();
    }

    public Path getIcon() {
        return this.icon;
    }

    public boolean requiresManualConversion() {
        return this.requiresManualConversion;
    }

    public boolean isExperimental() {
        return this.experimental;
    }

    public long getLastPlayed() {
        return this.levelVersion.lastPlayed();
    }

    public int compareTo(LevelSummary levelSummary0) {
        if (this.getLastPlayed() < levelSummary0.getLastPlayed()) {
            return 1;
        } else {
            return this.getLastPlayed() > levelSummary0.getLastPlayed() ? -1 : this.levelId.compareTo(levelSummary0.levelId);
        }
    }

    public LevelSettings getSettings() {
        return this.settings;
    }

    public GameType getGameMode() {
        return this.settings.gameType();
    }

    public boolean isHardcore() {
        return this.settings.hardcore();
    }

    public boolean hasCheats() {
        return this.settings.allowCommands();
    }

    public MutableComponent getWorldVersionName() {
        return StringUtil.isNullOrEmpty(this.levelVersion.minecraftVersionName()) ? Component.translatable("selectWorld.versionUnknown") : Component.literal(this.levelVersion.minecraftVersionName());
    }

    public LevelVersion levelVersion() {
        return this.levelVersion;
    }

    public boolean markVersionInList() {
        return this.askToOpenWorld() || !SharedConstants.getCurrentVersion().isStable() && !this.levelVersion.snapshot() || this.backupStatus().shouldBackup();
    }

    public boolean askToOpenWorld() {
        return this.levelVersion.minecraftVersion().getVersion() > SharedConstants.getCurrentVersion().getDataVersion().getVersion();
    }

    public LevelSummary.BackupStatus backupStatus() {
        WorldVersion $$0 = SharedConstants.getCurrentVersion();
        int $$1 = $$0.getDataVersion().getVersion();
        int $$2 = this.levelVersion.minecraftVersion().getVersion();
        if (!$$0.isStable() && $$2 < $$1) {
            return LevelSummary.BackupStatus.UPGRADE_TO_SNAPSHOT;
        } else {
            return $$2 > $$1 ? LevelSummary.BackupStatus.DOWNGRADE : LevelSummary.BackupStatus.NONE;
        }
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean isDisabled() {
        return !this.isLocked() && !this.requiresManualConversion() ? !this.isCompatible() : true;
    }

    public boolean isCompatible() {
        return SharedConstants.getCurrentVersion().getDataVersion().isCompatible(this.levelVersion.minecraftVersion());
    }

    public Component getInfo() {
        if (this.info == null) {
            this.info = this.createInfo();
        }
        return this.info;
    }

    private Component createInfo() {
        if (this.isLocked()) {
            return Component.translatable("selectWorld.locked").withStyle(ChatFormatting.RED);
        } else if (this.requiresManualConversion()) {
            return Component.translatable("selectWorld.conversion").withStyle(ChatFormatting.RED);
        } else if (!this.isCompatible()) {
            return Component.translatable("selectWorld.incompatible_series").withStyle(ChatFormatting.RED);
        } else {
            MutableComponent $$0 = this.isHardcore() ? Component.empty().append(Component.translatable("gameMode.hardcore").withStyle(p_265611_ -> p_265611_.withColor(-65536))) : Component.translatable("gameMode." + this.getGameMode().getName());
            if (this.hasCheats()) {
                $$0.append(", ").append(Component.translatable("selectWorld.cheats"));
            }
            if (this.isExperimental()) {
                $$0.append(", ").append(Component.translatable("selectWorld.experimental").withStyle(ChatFormatting.YELLOW));
            }
            MutableComponent $$1 = this.getWorldVersionName();
            MutableComponent $$2 = Component.literal(", ").append(Component.translatable("selectWorld.version")).append(CommonComponents.SPACE);
            if (this.markVersionInList()) {
                $$2.append($$1.withStyle(this.askToOpenWorld() ? ChatFormatting.RED : ChatFormatting.ITALIC));
            } else {
                $$2.append($$1);
            }
            $$0.append($$2);
            return $$0;
        }
    }

    public static enum BackupStatus {

        NONE(false, false, ""), DOWNGRADE(true, true, "downgrade"), UPGRADE_TO_SNAPSHOT(true, false, "snapshot");

        private final boolean shouldBackup;

        private final boolean severe;

        private final String translationKey;

        private BackupStatus(boolean p_164928_, boolean p_164929_, String p_164930_) {
            this.shouldBackup = p_164928_;
            this.severe = p_164929_;
            this.translationKey = p_164930_;
        }

        public boolean shouldBackup() {
            return this.shouldBackup;
        }

        public boolean isSevere() {
            return this.severe;
        }

        public String getTranslationKey() {
            return this.translationKey;
        }
    }

    public static class SymlinkLevelSummary extends LevelSummary {

        public SymlinkLevelSummary(String string0, Path path1) {
            super(null, null, string0, false, false, false, path1);
        }

        @Override
        public String getLevelName() {
            return this.m_78358_();
        }

        @Override
        public Component getInfo() {
            return Component.translatable("symlink_warning.title").withStyle(p_289961_ -> p_289961_.withColor(-65536));
        }

        @Override
        public long getLastPlayed() {
            return -1L;
        }

        @Override
        public boolean isDisabled() {
            return false;
        }
    }
}