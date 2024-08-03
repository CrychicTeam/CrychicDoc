package dev.ftb.mods.ftbquests.client.gui.quests;

import dev.architectury.platform.Platform;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.net.TogglePinnedMessage;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import net.minecraft.network.chat.Component;

public class OtherButtonsPanelTop extends OtherButtonsPanel {

    public OtherButtonsPanelTop(Panel panel) {
        super(panel);
    }

    @Override
    public void addWidgets() {
        this.add(new CollectRewardsButton(this));
        this.add(new OtherButtonsPanelTop.AutopinButton(this));
        this.add(new OtherButtonsPanelTop.KeyReferenceButton(this));
        if (Platform.isModLoaded("ftbguides")) {
            this.add(new OpenGuidesButton(this));
        }
        if (!this.questScreen.file.getEmergencyItems().isEmpty() && (this.questScreen.file.selfTeamData != null || this.questScreen.file.canEdit())) {
            this.add(new EmergencyItemsButton(this));
        }
        if (!ThemeProperties.WIKI_URL.get().equals("-")) {
            this.add(new OtherButtonsPanelTop.WikiButton(this));
        }
        if (Platform.isModLoaded("ftbmoney")) {
            this.add(new OpenShopButton(this));
        }
    }

    @Override
    public void alignWidgets() {
        this.setPosAndSize(this.questScreen.width - this.width, 1, this.width, this.align(WidgetLayout.VERTICAL));
    }

    public static class AutopinButton extends TabButton {

        public AutopinButton(Panel panel) {
            super(panel, Component.translatable(isAutoPin() ? "ftbquests.gui.autopin.on" : "ftbquests.gui.autopin.off"), isAutoPin() ? ThemeProperties.PIN_ICON_ON.get() : ThemeProperties.PIN_ICON_OFF.get());
        }

        private static boolean isAutoPin() {
            return ClientQuestFile.isQuestPinned(1L);
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            new TogglePinnedMessage(1L).sendToServer();
        }
    }

    private static class KeyReferenceButton extends TabButton {

        public KeyReferenceButton(Panel panel) {
            super(panel, Component.translatable("ftblibrary.gui.key_reference"), Icons.INFO_GRAY);
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            if (ClientQuestFile.INSTANCE.canEdit()) {
                new QuestKeyReferenceScreen("ftbquests.gui.key_reference.player", "ftbquests.gui.key_reference.editor").openGui();
            } else {
                new QuestKeyReferenceScreen("ftbquests.gui.key_reference.player").openGui();
            }
        }
    }

    public static class WikiButton extends TabButton {

        public WikiButton(Panel panel) {
            super(panel, Component.translatable("ftbquests.gui.wiki"), ThemeProperties.WIKI_ICON.get());
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            this.handleClick(ThemeProperties.WIKI_URL.get());
        }
    }
}