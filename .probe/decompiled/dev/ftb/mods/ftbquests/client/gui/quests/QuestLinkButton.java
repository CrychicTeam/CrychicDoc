package dev.ftb.mods.ftbquests.client.gui.quests;

import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestLink;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import net.minecraft.client.gui.GuiGraphics;

public class QuestLinkButton extends QuestButton {

    private final QuestLink link;

    public QuestLinkButton(QuestPanel questPanel, QuestLink link, Quest quest) {
        super(questPanel, quest);
        this.link = link;
    }

    @Override
    public QuestPositionableButton.Position getPosition() {
        return new QuestPositionableButton.Position(this.link.getX(), this.link.getY(), this.link.getWidth(), this.link.getHeight());
    }

    @Override
    protected QuestObject theQuestObject() {
        return this.link;
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        super.draw(graphics, theme, x, y, w, h);
        if (this.questScreen.file.canEdit()) {
            float s = (float) w / 8.0F * 3.0F;
            graphics.pose().pushPose();
            graphics.pose().translate((float) x, (float) (y + h) - s, 200.0F);
            graphics.pose().scale(s, s, 1.0F);
            ThemeProperties.LINK_ICON.get().draw(graphics, 0, 0, 1, 1);
            graphics.pose().popPose();
        }
    }

    @Override
    protected String getShape() {
        return this.link.getShape();
    }
}