package dev.ftb.mods.ftbquests.client.gui;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.StringUtils;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import dev.ftb.mods.ftbquests.quest.QuestShape;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

public class RewardNotificationsScreen extends BaseScreen implements IRewardListenerScreen {

    private final Object2IntOpenHashMap<RewardKey> rewards = new Object2IntOpenHashMap();

    private final SimpleTextButton closeButton = new SimpleTextButton(this, Component.translatable("gui.close"), Color4I.empty()) {

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            this.getGui().closeGui();
        }
    };

    private final Panel itemPanel = new Panel(this) {

        @Override
        public void addWidgets() {
            List<RewardKey> keys = new ArrayList(RewardNotificationsScreen.this.rewards.keySet());
            keys.sort((o1, o2) -> Integer.compare(RewardNotificationsScreen.this.rewards.getInt(o2), RewardNotificationsScreen.this.rewards.getInt(o1)));
            for (RewardKey key : keys) {
                this.add(RewardNotificationsScreen.this.new RewardNotification(this, key));
            }
        }

        @Override
        public void alignWidgets() {
            if (this.widgets.size() < 9) {
                this.setWidth(this.align(new WidgetLayout.Horizontal(0, 1, 0)));
                this.setHeight(22);
            } else {
                this.setWidth(207);
                this.setHeight(23 * Mth.ceil((float) this.widgets.size() / 9.0F));
                for (int i = 0; i < this.widgets.size(); i++) {
                    ((Widget) this.widgets.get(i)).setPos(i % 9 * 23, i / 9 * 23);
                }
            }
            this.setPos((RewardNotificationsScreen.this.width - RewardNotificationsScreen.this.itemPanel.width) / 2, (RewardNotificationsScreen.this.height - RewardNotificationsScreen.this.itemPanel.height) / 2);
        }
    };

    public RewardNotificationsScreen() {
        this.itemPanel.setOnlyRenderWidgetsInside(false);
    }

    @Override
    public void addWidgets() {
        this.add(this.itemPanel);
        this.add(this.closeButton);
        this.closeButton.setPos((this.width - this.closeButton.width) / 2, this.height * 2 / 3 + 16);
    }

    @Override
    public boolean onInit() {
        return this.setFullscreen();
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        graphics.pose().pushPose();
        graphics.pose().translate((float) ((int) ((double) w / 2.0)), (float) ((int) ((double) h / 5.0)), 0.0F);
        graphics.pose().scale(2.0F, 2.0F, 1.0F);
        MutableComponent s = Component.translatable("ftbquests.rewards");
        theme.drawString(graphics, s, -theme.getStringWidth(s) / 2, 0, Color4I.WHITE, 0);
        graphics.pose().popPose();
    }

    @Override
    public Theme getTheme() {
        return FTBQuestsTheme.INSTANCE;
    }

    @Override
    public void rewardReceived(RewardKey key, int count) {
        this.rewards.put(key, this.rewards.getInt(key) + count);
        this.itemPanel.refreshWidgets();
    }

    private class RewardNotification extends Widget {

        private final RewardKey key;

        public RewardNotification(Panel panel, RewardKey key) {
            super(panel);
            this.setSize(22, 22);
            this.key = key;
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            if (!this.key.getTitle().isEmpty()) {
                list.string(this.key.getTitle());
            }
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            GuiHelper.setupDrawing();
            QuestShape.get("rsquare").getOutline().draw(graphics, x, y, w, h);
            this.key.getIcon().draw(graphics, x + 3, y + 3, 16, 16);
            int count = RewardNotificationsScreen.this.rewards.getInt(this.key);
            if (count > 1) {
                graphics.pose().pushPose();
                graphics.pose().translate(0.0F, 0.0F, 600.0F);
                Component s = Component.literal(StringUtils.formatDouble((double) count, true)).withStyle(ChatFormatting.YELLOW);
                theme.drawString(graphics, s, x + 22 - theme.getStringWidth(s), y + 12, 2);
                graphics.pose().popPose();
            }
        }

        @Override
        public Optional<PositionedIngredient> getIngredientUnderMouse() {
            return PositionedIngredient.of(this.key.getIcon().getIngredient(), this, true);
        }
    }
}