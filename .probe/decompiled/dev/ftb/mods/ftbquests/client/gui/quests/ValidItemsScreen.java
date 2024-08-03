package dev.ftb.mods.ftbquests.client.gui.quests;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetType;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.CompactGridLayout;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import dev.ftb.mods.ftbquests.FTBQuests;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.FTBQuestsTheme;
import dev.ftb.mods.ftbquests.net.SubmitTaskMessage;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ValidItemsScreen extends BaseScreen {

    private final Component title;

    private final Panel itemPanel;

    private final Button backButton;

    private final Button submitButton;

    public ValidItemsScreen(ItemTask task, List<ItemStack> validItems, boolean canClick) {
        this.title = Component.translatable("ftbquests.task.ftbquests.item.valid_for", task.getTitle());
        this.itemPanel = new Panel(this) {

            @Override
            public void addWidgets() {
                for (ItemStack validItem : validItems) {
                    this.add(new ValidItemsScreen.ValidItemButton(this, validItem));
                }
            }

            @Override
            public void alignWidgets() {
                this.align(new CompactGridLayout(36));
                this.setHeight(Math.min(160, this.getContentHeight()));
                this.parent.setHeight(this.height + 53);
                int off = (this.width - this.getContentWidth()) / 2;
                for (Widget widget : this.widgets) {
                    widget.setX(widget.posX + off);
                }
                ValidItemsScreen.this.itemPanel.setX((this.parent.width - this.width) / 2);
                ValidItemsScreen.this.backButton.setPosAndSize(ValidItemsScreen.this.itemPanel.posX - 1, this.height + 28, 70, 20);
                ValidItemsScreen.this.submitButton.setPosAndSize(ValidItemsScreen.this.itemPanel.posX + 75, this.height + 28, 70, 20);
            }

            @Override
            public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                theme.drawButton(graphics, x - 1, y - 1, w + 2, h + 2, WidgetType.NORMAL);
            }
        };
        this.itemPanel.setPosAndSize(0, 22, 144, 0);
        this.backButton = new SimpleTextButton(this, Component.translatable("gui.back"), Color4I.empty()) {

            @Override
            public void onClicked(MouseButton button) {
                this.playClickSound();
                ValidItemsScreen.this.onBack();
            }

            @Override
            public boolean renderTitleInCenter() {
                return true;
            }
        };
        this.submitButton = new SimpleTextButton(this, Component.literal("Submit"), Color4I.empty()) {

            @Override
            public void onClicked(MouseButton button) {
                this.playClickSound();
                new SubmitTaskMessage(task.id).sendToServer();
                ValidItemsScreen.this.onBack();
            }

            @Override
            public void addMouseOverText(TooltipList list) {
                if (canClick && !task.consumesResources() && !task.isTaskScreenOnly()) {
                    list.translate("ftbquests.task.auto_detected");
                }
            }

            @Override
            public WidgetType getWidgetType() {
                return canClick && task.consumesResources() && !task.isTaskScreenOnly() ? super.getWidgetType() : WidgetType.DISABLED;
            }

            @Override
            public boolean renderTitleInCenter() {
                return true;
            }
        };
    }

    @Override
    public void addWidgets() {
        this.setWidth(Math.max(156, this.getTheme().getStringWidth(this.title) + 12));
        this.add(this.itemPanel);
        this.add(this.backButton);
        this.add(this.submitButton);
    }

    @Override
    public Theme getTheme() {
        return FTBQuestsTheme.INSTANCE;
    }

    @Override
    public void drawBackground(GuiGraphics matrixStack, Theme theme, int x, int y, int w, int h) {
        super.drawBackground(matrixStack, theme, x, y, w, h);
        theme.drawString(matrixStack, this.title, x + w / 2, y + 6, Color4I.WHITE, 4);
    }

    @Override
    public boolean keyPressed(Key key) {
        if (super.keyPressed(key)) {
            return true;
        } else if (key.esc()) {
            this.onBack();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return ClientQuestFile.exists() && ClientQuestFile.INSTANCE.isPauseGame();
    }

    @Override
    public boolean onClosedByKey(Key key) {
        if (super.onClosedByKey(key)) {
            this.onBack();
        }
        return false;
    }

    private static class ValidItemButton extends Button {

        private final ItemStack stack;

        ValidItemButton(Panel panel, ItemStack stack) {
            super(panel, Component.empty(), ItemIcon.getItemIcon(stack));
            this.stack = stack;
        }

        @Override
        public void onClicked(MouseButton button) {
            FTBQuests.getRecipeModHelper().showRecipes(this.stack);
        }

        @Override
        public Optional<PositionedIngredient> getIngredientUnderMouse() {
            return PositionedIngredient.of(this.stack, this, true);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            if (this.isMouseOver()) {
                Color4I.WHITE.withAlpha(33).draw(graphics, x, y, w, h);
            }
            graphics.pose().pushPose();
            graphics.pose().translate((double) x + (double) w / 2.0, (double) y + (double) h / 2.0, 10.0);
            graphics.pose().scale(2.0F, 2.0F, 2.0F);
            GuiHelper.drawItem(graphics, this.stack, 0, true, null);
            graphics.pose().popPose();
        }
    }
}