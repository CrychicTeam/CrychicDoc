package dev.ftb.mods.ftbquests.client;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.snbt.config.BooleanValue;
import dev.ftb.mods.ftblibrary.snbt.config.ConfigUtil;
import dev.ftb.mods.ftblibrary.snbt.config.EnumValue;
import dev.ftb.mods.ftblibrary.snbt.config.IntValue;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import dev.ftb.mods.ftblibrary.util.PanelPositioning;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public interface FTBQuestsClientConfig {

    SNBTConfig CONFIG = SNBTConfig.create("ftbquests-client");

    String CLIENT_CONFIG = "client-config.snbt";

    SNBTConfig UI = CONFIG.addGroup("ui", 0);

    BooleanValue OLD_SCROLL_WHEEL = UI.addBoolean("old_scroll_wheel", false);

    EnumValue<PanelPositioning> PINNED_QUESTS_POS = UI.addEnum("pinned_quests_pos", PanelPositioning.NAME_MAP, PanelPositioning.RIGHT);

    IntValue PINNED_QUESTS_INSET_X = UI.addInt("pinned_quests_inset_x", 2);

    IntValue PINNED_QUESTS_INSET_Y = UI.addInt("pinned_quests_inset_y", 2);

    static void openSettings(Screen screen) {
        ConfigGroup group = new ConfigGroup("ftbquests", accepted -> {
            if (accepted) {
                saveConfig();
            }
            Minecraft.getInstance().setScreen(screen);
        });
        CONFIG.createClientConfig(group);
        EditConfigScreen gui = new EditConfigScreen(group) {

            @Override
            public boolean doesGuiPauseGame() {
                return screen.isPauseScreen();
            }
        };
        gui.openGui();
    }

    static void init() {
        ConfigUtil.loadDefaulted(CONFIG, ConfigUtil.LOCAL_DIR.resolve("ftbquests"), "ftbquests", "client-config.snbt");
    }

    static void saveConfig() {
        CONFIG.save(ConfigUtil.LOCAL_DIR.resolve("ftbquests").resolve("client-config.snbt"));
    }
}