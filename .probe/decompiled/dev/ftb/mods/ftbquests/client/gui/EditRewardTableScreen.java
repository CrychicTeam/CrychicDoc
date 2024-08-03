package dev.ftb.mods.ftbquests.client.gui;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.DoubleConfig;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.config.ui.EditStringConfigOverlay;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.NordTheme;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleButton;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractButtonListScreen;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractThreePanelScreen;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import dev.ftb.mods.ftbquests.quest.loot.WeightedReward;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import dev.ftb.mods.ftbquests.quest.reward.RewardTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

public class EditRewardTableScreen extends AbstractButtonListScreen {

    private final Runnable parentScreen;

    private final RewardTable editedTable;

    private final Consumer<RewardTable> callback;

    boolean changed = false;

    public EditRewardTableScreen(Runnable parentScreen, RewardTable originalTable, Consumer<RewardTable> callback) {
        this.parentScreen = parentScreen;
        this.callback = callback;
        this.editedTable = QuestObjectBase.copy(originalTable, () -> new RewardTable(originalTable.id, originalTable.getFile()));
        this.setBorder(1, 1, 1);
    }

    @Override
    protected Panel createTopPanel() {
        return new EditRewardTableScreen.CustomTopPanel();
    }

    @Override
    protected int getTopPanelHeight() {
        return 25;
    }

    @Override
    public void addButtons(Panel panel) {
        this.editedTable.getWeightedRewards().forEach(wr -> panel.add(new EditRewardTableScreen.WeightedRewardButton(panel, wr)));
    }

    @Override
    public boolean onInit() {
        this.setTitle(Component.literal(((RewardTable) Objects.requireNonNull(this.editedTable)).getRawTitle()));
        return super.onInit();
    }

    @Override
    public Theme getTheme() {
        return NordTheme.THEME;
    }

    @Override
    public boolean onClosedByKey(Key key) {
        if (super.onClosedByKey(key)) {
            this.doCancel();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void doCancel() {
        if (this.changed) {
            this.openYesNo(Component.translatable("ftblibrary.unsaved_changes"), Component.empty(), this.parentScreen);
        } else {
            this.parentScreen.run();
        }
    }

    @Override
    protected void doAccept() {
        this.callback.accept(this.editedTable);
        this.parentScreen.run();
    }

    private class AddWeightedRewardButton extends SimpleButton {

        private AddWeightedRewardButton(Panel panel) {
            super(panel, Component.translatable("gui.add"), Icons.ADD, (b, mb) -> {
            });
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            List<ContextMenuItem> contextMenu = new ArrayList();
            for (RewardType type : RewardTypes.TYPES.values()) {
                if (!type.getExcludeFromListRewards()) {
                    contextMenu.add(new ContextMenuItem(type.getDisplayName(), type.getIconSupplier(), b -> {
                        this.playClickSound();
                        type.getGuiProvider().openCreationGui(this.parent, EditRewardTableScreen.this.editedTable.getFakeQuest(), reward -> {
                            EditRewardTableScreen.this.editedTable.addReward(new WeightedReward(reward, 1.0F));
                            EditRewardTableScreen.this.changed = true;
                            this.openGui();
                        });
                    }));
                }
            }
            this.getGui().openContextMenu(contextMenu);
        }
    }

    private class CustomTopPanel extends AbstractThreePanelScreen<AbstractButtonListScreen.ButtonPanel>.TopPanel {

        private final EditRewardTableScreen.RewardTableSettingsButton settingsButton = EditRewardTableScreen.this.new RewardTableSettingsButton(this);

        private final EditRewardTableScreen.AddWeightedRewardButton addButton = EditRewardTableScreen.this.new AddWeightedRewardButton(this);

        public CustomTopPanel() {
        }

        @Override
        public void addWidgets() {
            this.add(this.settingsButton);
            this.add(this.addButton);
        }

        @Override
        public void alignWidgets() {
            this.settingsButton.setPosAndSize(this.width - 18, 2, 16, 16);
            this.addButton.setPosAndSize(this.width - 36, 2, 16, 16);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            super.draw(graphics, theme, x, y, w, h);
            EditRewardTableScreen.this.editedTable.getIcon().draw(graphics, x + 2, y + 2, 16, 16);
            theme.drawString(graphics, this.getGui().getTitle(), x + 20, y + 6, 2);
        }
    }

    private class RewardTableSettingsButton extends SimpleButton {

        private RewardTableSettingsButton(Panel panel) {
            super(panel, Component.translatable("gui.settings"), Icons.SETTINGS, (b, mb) -> {
            });
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            ConfigGroup group = new ConfigGroup("ftbquests", accepted -> {
                EditRewardTableScreen.this.editedTable.clearCachedData();
                this.run();
            }) {

                @Override
                public Component getName() {
                    return EditRewardTableScreen.this.editedTable.getTitle();
                }
            };
            EditRewardTableScreen.this.editedTable.fillConfigGroup(EditRewardTableScreen.this.editedTable.createSubGroup(group));
            new EditConfigScreen(group).openGui();
        }
    }

    private class WeightedRewardButton extends SimpleTextButton {

        private final WeightedReward wr;

        private WeightedRewardButton(Panel panel, WeightedReward wr) {
            super(panel, wr.getReward().getTitle(), wr.getReward().getIcon());
            this.wr = wr;
            this.setHeight(16);
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            super.addMouseOverText(list);
            if (this.getMouseX() > this.getX() + this.width - 13) {
                list.add(Component.translatable("gui.remove"));
            } else if (this.getMouseX() > this.getX() + this.width - 26) {
                list.add(Component.translatable("ftbquests.reward_table.set_weight"));
            } else {
                this.wr.getReward().addMouseOverText(list);
                String w = String.format("%.2f", this.wr.getWeight());
                String str = WeightedReward.chanceString(this.wr.getWeight(), EditRewardTableScreen.this.editedTable.getTotalWeight(true));
                list.add(Component.translatable("ftbquests.reward_table.weight").append(": " + w).append(Component.literal(" [" + str + "]").withStyle(ChatFormatting.DARK_GRAY)));
            }
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            if (button.isLeft()) {
                if (this.getMouseX() > this.getX() + this.width - 13) {
                    EditRewardTableScreen.this.openYesNo(Component.translatable("delete_item", this.wr.getReward().getTitle()), Component.empty(), this::doDeletion);
                } else if (this.getMouseX() > this.getX() + this.width - 26) {
                    this.setEntryWeight();
                } else {
                    this.editRewardTableEntry();
                }
            } else {
                List<ContextMenuItem> contextMenu = new ArrayList();
                contextMenu.add(new ContextMenuItem(Component.translatable("selectServer.edit"), ItemIcon.getItemIcon(Items.FEATHER), b -> this.editRewardTableEntry()));
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.reward_table.set_weight"), ItemIcon.getItemIcon(Items.ANVIL), b -> this.setEntryWeight()));
                contextMenu.add(new ContextMenuItem(Component.translatable("gui.remove"), Icons.BIN, b -> this.doDeletion()).setYesNoText(Component.translatable("delete_item", this.wr.getReward().getTitle())));
                EditRewardTableScreen.this.openContextMenu(contextMenu);
            }
        }

        private void doDeletion() {
            EditRewardTableScreen.this.editedTable.removeReward(this.wr);
            EditRewardTableScreen.this.refreshWidgets();
            EditRewardTableScreen.this.changed = true;
        }

        private void setEntryWeight() {
            DoubleConfig c = new DoubleConfig(0.0, Double.POSITIVE_INFINITY);
            c.setValue(Double.valueOf((double) this.wr.getWeight()));
            EditStringConfigOverlay<Double> overlay = new EditStringConfigOverlay<>(this.parent, c, accepted -> {
                if (accepted) {
                    this.wr.setWeight(c.getValue().floatValue());
                    EditRewardTableScreen.this.changed = true;
                }
            }).atPosition(this.parent.width - 80, this.getPosY());
            overlay.setExtraZlevel(300);
            this.getGui().pushModalPanel(overlay);
        }

        private void editRewardTableEntry() {
            ConfigGroup group = new ConfigGroup("ftbquests", accepted -> {
                if (accepted) {
                    this.wr.getReward().clearCachedData();
                    EditRewardTableScreen.this.changed = true;
                }
                this.run();
            }) {

                @Override
                public Component getName() {
                    return WeightedRewardButton.this.wr.getReward().getTitle();
                }
            };
            this.wr.getReward().fillConfigGroup(this.wr.getReward().createSubGroup(group));
            new EditConfigScreen(group).openGui();
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            if (this.isMouseOver) {
                Color4I.WHITE.withAlpha(30).draw(graphics, x, y, w, h);
                ItemIcon.getItemIcon(Items.ANVIL).draw(graphics, x + w - 26, y + 2, 12, 12);
                Icons.BIN.draw(graphics, x + w - 13, y + 2, 12, 12);
            }
            Color4I.GRAY.withAlpha(40).draw(graphics, x, y + h, w, 1);
        }

        @Override
        public Optional<PositionedIngredient> getIngredientUnderMouse() {
            return PositionedIngredient.of(this.wr.getReward().getIngredient(this), this);
        }
    }
}