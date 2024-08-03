package dev.ftb.mods.ftbquests.client.gui.quests;

import dev.ftb.mods.ftblibrary.ui.Panel;

public abstract class OtherButtonsPanel extends Panel {

    protected final QuestScreen questScreen;

    public OtherButtonsPanel(Panel panel) {
        super(panel);
        this.questScreen = (QuestScreen) panel.getGui();
        this.setWidth(20);
    }
}