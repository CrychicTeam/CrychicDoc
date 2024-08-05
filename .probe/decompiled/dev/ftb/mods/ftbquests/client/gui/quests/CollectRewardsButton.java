package dev.ftb.mods.ftbquests.client.gui.quests;

import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.client.gui.RewardNotificationsScreen;
import dev.ftb.mods.ftbquests.net.ClaimAllRewardsMessage;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class CollectRewardsButton extends TabButton {

    public CollectRewardsButton(Panel panel) {
        super(panel, Component.empty(), ThemeProperties.COLLECT_REWARDS_ICON.get());
        this.title = this.questScreen.file.getTitle();
    }

    @Override
    public void onClicked(MouseButton button) {
        if (this.questScreen.file.selfTeamData.hasUnclaimedRewards(Minecraft.getInstance().player.m_20148_(), this.questScreen.file)) {
            this.playClickSound();
            new RewardNotificationsScreen().openGui();
            new ClaimAllRewardsMessage().sendToServer();
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        list.translate("ftbquests.gui.collect_rewards");
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        super.draw(graphics, theme, x, y, w, h);
        if (this.questScreen.file.selfTeamData.hasUnclaimedRewards(Minecraft.getInstance().player.m_20148_(), this.questScreen.file)) {
            GuiHelper.setupDrawing();
            int s = w / 2;
            graphics.pose().pushPose();
            graphics.pose().translate((float) (x + w - s), (float) y, 200.0F);
            ThemeProperties.ALERT_ICON.get(this.questScreen.file).draw(graphics, 0, 0, s, s);
            graphics.pose().popPose();
        }
    }
}