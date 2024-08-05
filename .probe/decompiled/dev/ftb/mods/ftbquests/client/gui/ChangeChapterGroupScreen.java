package dev.ftb.mods.ftbquests.client.gui;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractButtonListScreen;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.net.ChangeChapterGroupMessage;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.ChapterGroup;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ChangeChapterGroupScreen extends AbstractButtonListScreen {

    private final Chapter chapter;

    private final QuestScreen questScreen;

    private ChapterGroup newGroup;

    public ChangeChapterGroupScreen(Chapter chapter, QuestScreen questScreen) {
        this.chapter = chapter;
        this.questScreen = questScreen;
        this.setTitle(Component.translatable("ftbquests.gui.change_group"));
        this.setHasSearchBox(true);
        this.showCloseButton(true);
        this.showBottomPanel(false);
        this.setBorder(1, 1, 1);
    }

    @Override
    public void addButtons(Panel panel) {
        ClientQuestFile.INSTANCE.getChapterGroups().stream().sorted().forEach(group -> panel.add(new ChangeChapterGroupScreen.ChapterGroupButton(panel, group)));
    }

    @Override
    protected void doCancel() {
        this.questScreen.open(this.chapter, false);
    }

    @Override
    protected void doAccept() {
        if (this.newGroup != null) {
            new ChangeChapterGroupMessage(this.chapter.id, this.newGroup.id).sendToServer();
        }
        this.questScreen.open(this.chapter, false);
    }

    private class ChapterGroupButton extends SimpleTextButton {

        private final ChapterGroup chapterGroup;

        public ChapterGroupButton(Panel panel, ChapterGroup chapterGroup) {
            super(panel, chapterGroup.getTitle(), Color4I.empty());
            this.chapterGroup = chapterGroup;
            this.setHeight(16);
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            ChangeChapterGroupScreen.this.newGroup = this.chapterGroup;
            ChangeChapterGroupScreen.this.doAccept();
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            if (this.isMouseOver) {
                Color4I.WHITE.withAlpha(30).draw(graphics, x, y, w, h);
            }
            Color4I.GRAY.withAlpha(40).draw(graphics, x, y + h, w, 1);
        }
    }
}