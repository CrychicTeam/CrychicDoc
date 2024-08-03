package dev.ftb.mods.ftbquests.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TimeUtils;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.net.GetEmergencyItemsMessage;
import dev.ftb.mods.ftbquests.quest.QuestShape;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class EmergencyItemsScreen extends BaseScreen {

    private final long endTime = System.currentTimeMillis() + (long) ClientQuestFile.INSTANCE.getEmergencyItemsCooldown() * 1000L;

    private boolean done = false;

    private final SimpleTextButton cancelButton = new SimpleTextButton(this, Component.translatable("gui.cancel"), Color4I.empty()) {

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            this.getGui().closeGui();
        }
    };

    private final Panel itemPanel = new Panel(this) {

        @Override
        public void addWidgets() {
            ClientQuestFile.INSTANCE.getEmergencyItems().forEach(stack -> this.add(new EmergencyItemsScreen.EmergencyItemWidget(this, stack)));
        }

        @Override
        public void alignWidgets() {
            this.setWidth(this.align(new WidgetLayout.Horizontal(3, 7, 3)));
            this.setHeight(22);
            this.setPos((EmergencyItemsScreen.this.width - EmergencyItemsScreen.this.itemPanel.width) / 2, EmergencyItemsScreen.this.height * 2 / 3 - 10);
        }
    };

    @Override
    public void addWidgets() {
        this.add(this.itemPanel);
        this.add(this.cancelButton);
        this.cancelButton.setPos((this.width - this.cancelButton.width) / 2, this.height * 2 / 3 + 16);
    }

    @Override
    public boolean onInit() {
        return this.setFullscreen();
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        long timeLeft = this.endTime - System.currentTimeMillis();
        if (timeLeft <= 0L) {
            if (!this.done) {
                this.done = true;
                this.cancelButton.setTitle(Component.translatable("gui.close"));
                new GetEmergencyItemsMessage().sendToServer();
            }
            timeLeft = 0L;
        }
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate((float) ((int) ((double) w / 2.0)), (float) ((int) ((double) h / 5.0)), 0.0F);
        poseStack.scale(2.0F, 2.0F, 1.0F);
        String s = I18n.get("ftbquests.file.emergency_items");
        theme.drawString(graphics, s, -theme.getStringWidth(s) / 2, 0, Color4I.WHITE, 0);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate((float) ((int) ((double) w / 2.0)), (float) ((int) ((double) h / 2.5)), 0.0F);
        poseStack.scale(4.0F, 4.0F, 1.0F);
        s = timeLeft <= 0L ? "00:00" : TimeUtils.getTimeString(timeLeft / 1000L * 1000L + 1000L);
        int x1 = -theme.getStringWidth(s) / 2;
        theme.drawString(graphics, s, x1 - 1, 0, Color4I.BLACK, 0);
        theme.drawString(graphics, s, x1 + 1, 0, Color4I.BLACK, 0);
        theme.drawString(graphics, s, x1, 1, Color4I.BLACK, 0);
        theme.drawString(graphics, s, x1, -1, Color4I.BLACK, 0);
        theme.drawString(graphics, s, x1, 0, Color4I.WHITE, 0);
        poseStack.popPose();
    }

    @Override
    public Theme getTheme() {
        return FTBQuestsTheme.INSTANCE;
    }

    private static class EmergencyItemWidget extends Widget {

        private final ItemStack stack;

        public EmergencyItemWidget(Panel panel, ItemStack stack) {
            super(panel);
            this.stack = stack;
            this.setY(3);
            this.setSize(16, 16);
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            List<Component> l = new ArrayList();
            GuiHelper.addStackTooltip(this.stack, l);
            l.forEach(list::add);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            GuiHelper.setupDrawing();
            QuestShape.get("rsquare").getOutline().draw(graphics, x - 3, y - 3, w + 6, h + 6);
            graphics.pose().pushPose();
            graphics.pose().translate((double) x + (double) w / 2.0, (double) y + (double) h / 2.0, 100.0);
            GuiHelper.drawItem(graphics, this.stack, 0, true, null);
            graphics.pose().popPose();
        }

        @Override
        public Optional<PositionedIngredient> getIngredientUnderMouse() {
            return PositionedIngredient.of(this.stack, this);
        }
    }
}