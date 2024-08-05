package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.dto.Backup;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;

public class RealmsBackupInfoScreen extends RealmsScreen {

    private static final Component UNKNOWN = Component.translatable("mco.backup.unknown");

    private final Screen lastScreen;

    final Backup backup;

    private RealmsBackupInfoScreen.BackupInfoList backupInfoList;

    public RealmsBackupInfoScreen(Screen screen0, Backup backup1) {
        super(Component.translatable("mco.backup.info.title"));
        this.lastScreen = screen0;
        this.backup = backup1;
    }

    @Override
    public void tick() {
    }

    @Override
    public void init() {
        this.m_142416_(Button.builder(CommonComponents.GUI_BACK, p_280689_ -> this.f_96541_.setScreen(this.lastScreen)).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 120 + 24, 200, 20).build());
        this.backupInfoList = new RealmsBackupInfoScreen.BackupInfoList(this.f_96541_);
        this.m_7787_(this.backupInfoList);
        this.m_94725_(this.backupInfoList);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.f_96541_.setScreen(this.lastScreen);
            return true;
        } else {
            return super.m_7933_(int0, int1, int2);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.backupInfoList.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 10, 16777215);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }

    Component checkForSpecificMetadata(String string0, String string1) {
        String $$2 = string0.toLowerCase(Locale.ROOT);
        if ($$2.contains("game") && $$2.contains("mode")) {
            return this.gameModeMetadata(string1);
        } else {
            return (Component) ($$2.contains("game") && $$2.contains("difficulty") ? this.gameDifficultyMetadata(string1) : Component.literal(string1));
        }
    }

    private Component gameDifficultyMetadata(String string0) {
        try {
            return ((Difficulty) RealmsSlotOptionsScreen.DIFFICULTIES.get(Integer.parseInt(string0))).getDisplayName();
        } catch (Exception var3) {
            return UNKNOWN;
        }
    }

    private Component gameModeMetadata(String string0) {
        try {
            return ((GameType) RealmsSlotOptionsScreen.GAME_MODES.get(Integer.parseInt(string0))).getShortDisplayName();
        } catch (Exception var3) {
            return UNKNOWN;
        }
    }

    class BackupInfoList extends ObjectSelectionList<RealmsBackupInfoScreen.BackupInfoListEntry> {

        public BackupInfoList(Minecraft minecraft0) {
            super(minecraft0, RealmsBackupInfoScreen.this.f_96543_, RealmsBackupInfoScreen.this.f_96544_, 32, RealmsBackupInfoScreen.this.f_96544_ - 64, 36);
            this.m_93471_(false);
            if (RealmsBackupInfoScreen.this.backup.changeList != null) {
                RealmsBackupInfoScreen.this.backup.changeList.forEach((p_88084_, p_88085_) -> this.m_7085_(RealmsBackupInfoScreen.this.new BackupInfoListEntry(p_88084_, p_88085_)));
            }
        }
    }

    class BackupInfoListEntry extends ObjectSelectionList.Entry<RealmsBackupInfoScreen.BackupInfoListEntry> {

        private static final Component TEMPLATE_NAME = Component.translatable("mco.backup.entry.templateName");

        private static final Component GAME_DIFFICULTY = Component.translatable("mco.backup.entry.gameDifficulty");

        private static final Component NAME = Component.translatable("mco.backup.entry.name");

        private static final Component GAME_SERVER_VERSION = Component.translatable("mco.backup.entry.gameServerVersion");

        private static final Component UPLOADED = Component.translatable("mco.backup.entry.uploaded");

        private static final Component ENABLED_PACK = Component.translatable("mco.backup.entry.enabledPack");

        private static final Component DESCRIPTION = Component.translatable("mco.backup.entry.description");

        private static final Component GAME_MODE = Component.translatable("mco.backup.entry.gameMode");

        private static final Component SEED = Component.translatable("mco.backup.entry.seed");

        private static final Component WORLD_TYPE = Component.translatable("mco.backup.entry.worldType");

        private static final Component UNDEFINED = Component.translatable("mco.backup.entry.undefined");

        private final String key;

        private final String value;

        public BackupInfoListEntry(String string0, String string1) {
            this.key = string0;
            this.value = string1;
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            guiGraphics0.drawString(RealmsBackupInfoScreen.this.f_96547_, this.translateKey(this.key), int3, int2, 10526880);
            guiGraphics0.drawString(RealmsBackupInfoScreen.this.f_96547_, RealmsBackupInfoScreen.this.checkForSpecificMetadata(this.key, this.value), int3, int2 + 12, 16777215);
        }

        private Component translateKey(String string0) {
            return switch(string0) {
                case "template_name" ->
                    TEMPLATE_NAME;
                case "game_difficulty" ->
                    GAME_DIFFICULTY;
                case "name" ->
                    NAME;
                case "game_server_version" ->
                    GAME_SERVER_VERSION;
                case "uploaded" ->
                    UPLOADED;
                case "enabled_pack" ->
                    ENABLED_PACK;
                case "description" ->
                    DESCRIPTION;
                case "game_mode" ->
                    GAME_MODE;
                case "seed" ->
                    SEED;
                case "world_type" ->
                    WORLD_TYPE;
                default ->
                    UNDEFINED;
            };
        }

        @Override
        public Component getNarration() {
            return Component.translatable("narrator.select", this.key + " " + this.value);
        }
    }
}