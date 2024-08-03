package dev.ftb.mods.ftbquests.client.gui;

import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.config.ui.EditStringConfigOverlay;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractButtonListScreen;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractThreePanelScreen;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import dev.ftb.mods.ftbquests.net.CreateObjectMessage;
import dev.ftb.mods.ftbquests.net.DeleteObjectMessage;
import dev.ftb.mods.ftbquests.net.EditObjectMessage;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.loot.LootCrate;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import dev.ftb.mods.ftbquests.quest.reward.RandomReward;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;

public class RewardTablesScreen extends AbstractButtonListScreen {

    private final QuestScreen questScreen;

    private final SimpleTextButton addButton;

    private final List<RewardTable> rewardTablesCopy;

    private boolean changed = false;

    private final Set<RewardTable> editedTables = new HashSet();

    public RewardTablesScreen(QuestScreen questScreen) {
        this.questScreen = questScreen;
        this.rewardTablesCopy = (List<RewardTable>) ClientQuestFile.INSTANCE.getRewardTables().stream().map(t -> QuestObjectBase.copy(t, () -> new RewardTable(t.id, ClientQuestFile.INSTANCE))).collect(Collectors.toCollection(ArrayList::new));
        this.setTitle(Component.translatable("ftbquests.reward_tables"));
        this.setHasSearchBox(true);
        this.setBorder(1, 1, 1);
        this.addButton = new SimpleTextButton(this.topPanel, Component.translatable("gui.add"), Icons.ADD) {

            @Override
            public void onClicked(MouseButton button) {
                this.playClickSound();
                StringConfig cfg = new StringConfig();
                EditStringConfigOverlay<String> panel = new EditStringConfigOverlay<>(this.getGui(), cfg, accepted -> {
                    if (accepted) {
                        RewardTable table = new RewardTable(0L, ClientQuestFile.INSTANCE);
                        table.setRawTitle(cfg.getValue());
                        RewardTablesScreen.this.rewardTablesCopy.add(table);
                        RewardTablesScreen.this.refreshWidgets();
                    }
                }).atPosition(this.posX, this.posY + this.height);
                panel.setExtraZlevel(300);
                this.getGui().pushModalPanel(panel);
            }
        };
    }

    @Override
    public void addButtons(Panel panel) {
        this.rewardTablesCopy.stream().sorted().forEach(table -> panel.add(new RewardTablesScreen.RewardTableButton(panel, table)));
    }

    @Override
    protected int getTopPanelHeight() {
        return 25;
    }

    @Override
    protected Panel createTopPanel() {
        return new RewardTablesScreen.CustomTopPanel();
    }

    @Override
    public boolean onInit() {
        int maxW = Math.max(this.getTheme().getStringWidth(this.getTitle()) + 100, (Integer) this.rewardTablesCopy.stream().map(t -> this.getTheme().getStringWidth(t.getTitle())).max(Comparator.naturalOrder()).orElse(0));
        this.setWidth(maxW);
        this.setHeight(this.getGui().getScreen().getGuiScaledHeight() * 4 / 5);
        return true;
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
            this.openYesNo(Component.translatable("ftblibrary.unsaved_changes"), Component.empty(), this.questScreen);
        } else {
            this.questScreen.run();
        }
    }

    @Override
    protected void doAccept() {
        Set<Long> toRemove = (Set<Long>) ClientQuestFile.INSTANCE.getRewardTables().stream().map(t -> t.id).collect(Collectors.toSet());
        this.rewardTablesCopy.forEach(table -> {
            if (table.id == 0L) {
                new CreateObjectMessage(table, null).sendToServer();
            }
            toRemove.remove(table.id);
        });
        toRemove.forEach(id -> new DeleteObjectMessage(id).sendToServer());
        this.editedTables.forEach(t -> new EditObjectMessage(t).sendToServer());
        this.questScreen.run();
    }

    private class CustomTopPanel extends AbstractThreePanelScreen<AbstractButtonListScreen.ButtonPanel>.TopPanel {

        @Override
        public void addWidgets() {
            this.add(RewardTablesScreen.this.addButton);
        }

        @Override
        public void alignWidgets() {
            RewardTablesScreen.this.addButton.setPosAndSize(this.width - RewardTablesScreen.this.addButton.width - 2, 1, RewardTablesScreen.this.addButton.width, 20);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            super.draw(graphics, theme, x, y, w, h);
            theme.drawString(graphics, this.getGui().getTitle(), x + 6, y + 6, 2);
        }
    }

    private class RewardTableButton extends SimpleTextButton {

        private final RewardTable table;

        public RewardTableButton(Panel panel, RewardTable table) {
            super(panel, table.getTitle(), table.getIcon());
            this.table = table;
            this.setHeight(16);
            if (this.table.getLootCrate() != null) {
                this.title = this.title.copy().withStyle(ChatFormatting.YELLOW);
            }
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            if (button.isLeft()) {
                if (this.getMouseX() > this.getX() + this.width - 13) {
                    this.deleteRewardTable();
                } else if (this.getMouseX() > this.getX() + this.width - 26) {
                    this.toggleLootCrate();
                } else {
                    this.editRewardTable();
                }
            } else {
                List<ContextMenuItem> menu = List.of(new ContextMenuItem(Component.translatable("ftbquests.gui.edit"), ItemIcon.getItemIcon(Items.FEATHER), b -> this.editRewardTable()), new ContextMenuItem(Component.translatable("gui.remove"), Icons.BIN, b -> this.deleteRewardTable()), new ContextMenuItem(this.getLootCrateText(), ItemIcon.getItemIcon((Item) FTBQuestsItems.LOOTCRATE.get()), b -> this.toggleLootCrate()));
                this.getGui().openContextMenu(menu);
            }
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            if (this.isMouseOver) {
                Color4I.WHITE.withAlpha(30).draw(graphics, x, y, w, h);
                ItemIcon.getItemIcon((Item) FTBQuestsItems.LOOTCRATE.get()).draw(graphics, x + w - 26, y + 2, 12, 12);
                Icons.BIN.draw(graphics, x + w - 13, y + 2, 12, 12);
            }
            Color4I.GRAY.withAlpha(40).draw(graphics, x, y + h, w, 1);
        }

        private void editRewardTable() {
            new EditRewardTableScreen(RewardTablesScreen.this, this.table, editedReward -> {
                RewardTablesScreen.this.rewardTablesCopy.replaceAll(t -> t.id == editedReward.id ? editedReward : t);
                RewardTablesScreen.this.changed = true;
                RewardTablesScreen.this.editedTables.add(editedReward);
                editedReward.clearCachedData();
                RewardTablesScreen.this.refreshWidgets();
            }).openGui();
        }

        private void deleteRewardTable() {
            RewardTablesScreen.this.openYesNo(Component.translatable("delete_item", this.table.getTitle()), Component.empty(), () -> {
                RewardTablesScreen.this.rewardTablesCopy.removeIf(t -> t == this.table);
                RewardTablesScreen.this.changed = true;
                RewardTablesScreen.this.refreshWidgets();
            });
        }

        private void toggleLootCrate() {
            LootCrate crate = this.table.toggleLootCrate();
            if (crate != null) {
                this.title = this.table.getMutableTitle().withStyle(ChatFormatting.YELLOW);
            } else {
                this.title = this.table.getTitle();
            }
            RewardTablesScreen.this.changed = true;
            RewardTablesScreen.this.refreshWidgets();
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            super.addMouseOverText(list);
            if (this.getMouseX() > this.getX() + this.width - 13) {
                list.add(Component.translatable("gui.remove"));
            } else if (this.getMouseX() > this.getX() + this.width - 26) {
                list.add(this.getLootCrateText());
            } else {
                MutableInt usedIn = new MutableInt(0);
                ClientQuestFile.INSTANCE.forAllQuests(quest -> quest.getRewards().stream().filter(reward -> {
                    if (reward instanceof RandomReward rr && rr.getTable() != null && rr.getTable().id == this.table.id) {
                        return true;
                    }
                    return false;
                }).forEach(reward -> usedIn.increment()));
                list.add(Component.translatable("ftbquests.reward_table.used_in", usedIn));
                this.table.addMouseOverText(list, true, true);
            }
        }

        @NotNull
        private Component getLootCrateText() {
            return Component.translatable("ftbquests.reward_table." + (this.table.getLootCrate() != null ? "disable_loot_crate" : "enable_loot_crate"));
        }
    }
}