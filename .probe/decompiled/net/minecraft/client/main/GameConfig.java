package net.minecraft.client.main;

import com.mojang.authlib.properties.PropertyMap;
import com.mojang.blaze3d.platform.DisplayData;
import java.io.File;
import java.net.Proxy;
import java.nio.file.Path;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.User;
import net.minecraft.client.resources.IndexedAssetSource;

public class GameConfig {

    public final GameConfig.UserData user;

    public final DisplayData display;

    public final GameConfig.FolderData location;

    public final GameConfig.GameData game;

    public final GameConfig.QuickPlayData quickPlay;

    public GameConfig(GameConfig.UserData gameConfigUserData0, DisplayData displayData1, GameConfig.FolderData gameConfigFolderData2, GameConfig.GameData gameConfigGameData3, GameConfig.QuickPlayData gameConfigQuickPlayData4) {
        this.user = gameConfigUserData0;
        this.display = displayData1;
        this.location = gameConfigFolderData2;
        this.game = gameConfigGameData3;
        this.quickPlay = gameConfigQuickPlayData4;
    }

    public static class FolderData {

        public final File gameDirectory;

        public final File resourcePackDirectory;

        public final File assetDirectory;

        @Nullable
        public final String assetIndex;

        public FolderData(File file0, File file1, File file2, @Nullable String string3) {
            this.gameDirectory = file0;
            this.resourcePackDirectory = file1;
            this.assetDirectory = file2;
            this.assetIndex = string3;
        }

        public Path getExternalAssetSource() {
            return this.assetIndex == null ? this.assetDirectory.toPath() : IndexedAssetSource.createIndexFs(this.assetDirectory.toPath(), this.assetIndex);
        }
    }

    public static class GameData {

        public final boolean demo;

        public final String launchVersion;

        public final String versionType;

        public final boolean disableMultiplayer;

        public final boolean disableChat;

        public GameData(boolean boolean0, String string1, String string2, boolean boolean3, boolean boolean4) {
            this.demo = boolean0;
            this.launchVersion = string1;
            this.versionType = string2;
            this.disableMultiplayer = boolean3;
            this.disableChat = boolean4;
        }
    }

    public static record QuickPlayData(@Nullable String f_278493_, @Nullable String f_278449_, @Nullable String f_278424_, @Nullable String f_278402_) {

        @Nullable
        private final String path;

        @Nullable
        private final String singleplayer;

        @Nullable
        private final String multiplayer;

        @Nullable
        private final String realms;

        public QuickPlayData(@Nullable String f_278493_, @Nullable String f_278449_, @Nullable String f_278424_, @Nullable String f_278402_) {
            this.path = f_278493_;
            this.singleplayer = f_278449_;
            this.multiplayer = f_278424_;
            this.realms = f_278402_;
        }

        public boolean isEnabled() {
            return !Util.isBlank(this.singleplayer) || !Util.isBlank(this.multiplayer) || !Util.isBlank(this.realms);
        }
    }

    public static class UserData {

        public final User user;

        public final PropertyMap userProperties;

        public final PropertyMap profileProperties;

        public final Proxy proxy;

        public UserData(User user0, PropertyMap propertyMap1, PropertyMap propertyMap2, Proxy proxy3) {
            this.user = user0;
            this.userProperties = propertyMap1;
            this.profileProperties = propertyMap2;
            this.proxy = proxy3;
        }
    }
}