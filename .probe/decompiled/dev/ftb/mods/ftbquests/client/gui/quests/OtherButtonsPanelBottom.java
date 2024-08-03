package dev.ftb.mods.ftbquests.client.gui.quests;

import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import dev.ftb.mods.ftbquests.client.FTBQuestsClientConfig;
import dev.ftb.mods.ftbquests.client.gui.CustomToast;
import dev.ftb.mods.ftbquests.client.gui.RewardTablesScreen;
import dev.ftb.mods.ftbquests.net.ChangeProgressMessage;
import dev.ftb.mods.ftbquests.net.ForceSaveMessage;
import dev.ftb.mods.ftbquests.net.ToggleEditingModeMessage;
import dev.ftb.mods.ftbquests.quest.task.StructureTask;
import dev.ftb.mods.ftbquests.quest.theme.ThemeLoader;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;

public class OtherButtonsPanelBottom extends OtherButtonsPanel {

    public OtherButtonsPanelBottom(Panel panel) {
        super(panel);
    }

    @Override
    public void addWidgets() {
        if (this.questScreen.file.canEdit()) {
            this.add(new OtherButtonsPanelBottom.EditSettingsButton(this));
        }
        if (FTBQuestsClient.getClientPlayer().m_20310_(2) || ClientQuestFile.INSTANCE.hasEditorPermission()) {
            this.add(new OtherButtonsPanelBottom.ToggleEditModeButton(this));
        }
        this.add(new OtherButtonsPanelBottom.EditPlayerPrefsButton(this));
    }

    @Override
    public void alignWidgets() {
        this.setHeight(this.align(WidgetLayout.VERTICAL));
        this.setPos(this.questScreen.width - this.width, this.questScreen.height - this.height - 1);
    }

    private static class EditPlayerPrefsButton extends TabButton {

        public EditPlayerPrefsButton(OtherButtonsPanelBottom panel) {
            super(panel, Component.translatable("ftbquests.gui.preferences"), ThemeProperties.PREFS_ICON.get());
        }

        @Override
        public void onClicked(MouseButton button) {
            FTBQuestsClientConfig.openSettings(new ScreenWrapper(this.questScreen));
        }
    }

    public static class EditSettingsButton extends TabButton {

        public EditSettingsButton(Panel panel) {
            super(panel, Component.translatable("gui.settings"), ThemeProperties.SETTINGS_ICON.get());
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            if (this.questScreen.getContextMenu().isPresent()) {
                this.questScreen.closeContextMenu();
            } else {
                List<ContextMenuItem> contextMenu = new ArrayList();
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.edit_file"), ThemeProperties.SETTINGS_ICON.get(), b -> this.questScreen.file.onEditButtonClicked(this)));
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.reset_progress"), ThemeProperties.RELOAD_ICON.get(), b -> ChangeProgressMessage.sendToServer(this.questScreen.file.selfTeamData, this.questScreen.file, progressChange -> progressChange.setReset(true))).setYesNoText(Component.translatable("ftbquests.gui.reset_progress_q")));
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.complete_instantly"), ThemeProperties.CHECK_ICON.get(), b -> ChangeProgressMessage.sendToServer(this.questScreen.file.selfTeamData, this.questScreen.file, progressChange -> progressChange.setReset(false))).setYesNoText(Component.translatable("ftbquests.gui.complete_instantly_q")));
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.reward_tables"), ThemeProperties.REWARD_TABLE_ICON.get(), b -> new RewardTablesScreen(this.questScreen).openGui()));
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.save_on_server"), ThemeProperties.SAVE_ICON.get(), b -> new ForceSaveMessage().sendToServer()));
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.save_as_file"), ThemeProperties.DOWNLOAD_ICON.get(), b -> this.saveLocally()));
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.reload_theme"), ThemeProperties.RELOAD_ICON.get(), b -> this.reload_theme()));
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.wiki"), Icons.INFO, b -> this.handleClick("https://help.ftb.team/mods")));
                this.questScreen.openContextMenu(contextMenu);
            }
        }

        private void reload_theme() {
            Minecraft mc = Minecraft.getInstance();
            ThemeLoader.loadTheme(mc.getResourceManager());
            ClientQuestFile.INSTANCE.refreshGui();
            Minecraft.getInstance().getToasts().addToast(new CustomToast(Component.translatable("ftbquests.gui.reload_theme"), Icons.ACCEPT, Component.translatable("gui.done")));
        }

        private void saveLocally() {
            try {
                Calendar time = Calendar.getInstance();
                StringBuilder fileName = new StringBuilder("local/ftbquests/saved/");
                this.appendNum(fileName, time.get(1), '-');
                this.appendNum(fileName, time.get(2) + 1, '-');
                this.appendNum(fileName, time.get(5), '-');
                this.appendNum(fileName, time.get(11), '-');
                this.appendNum(fileName, time.get(12), '-');
                this.appendNum(fileName, time.get(13), '\u0000');
                File file = new File(Minecraft.getInstance().gameDirectory, fileName.toString()).getCanonicalFile();
                ClientQuestFile.INSTANCE.writeDataFull(file.toPath());
                Component component = Component.translatable("ftbquests.gui.saved_as_file", "." + file.getPath().replace(Minecraft.getInstance().gameDirectory.getCanonicalFile().getAbsolutePath(), ""));
                component.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath()));
                Minecraft.getInstance().player.sendSystemMessage(component);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }

        private void appendNum(StringBuilder sb, int num, char c) {
            if (num < 10) {
                sb.append('0');
            }
            sb.append(num);
            if (c != 0) {
                sb.append(c);
            }
        }
    }

    public static class ToggleEditModeButton extends TabButton {

        public ToggleEditModeButton(Panel panel) {
            super(panel, makeTooltip(), ClientQuestFile.canClientPlayerEdit() ? ThemeProperties.EDITOR_ICON_ON.get() : ThemeProperties.EDITOR_ICON_OFF.get());
        }

        private static Component makeTooltip() {
            String key = ClientQuestFile.canClientPlayerEdit() ? "commands.ftbquests.editing_mode.enabled" : "commands.ftbquests.editing_mode.disabled";
            return Component.translatable(key, ClientQuestFile.INSTANCE.selfTeamData.getName());
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            if (!this.questScreen.file.selfTeamData.getCanEdit(Minecraft.getInstance().player)) {
                StructureTask.maybeRequestStructureSync();
            }
            new ToggleEditingModeMessage().sendToServer();
        }
    }
}