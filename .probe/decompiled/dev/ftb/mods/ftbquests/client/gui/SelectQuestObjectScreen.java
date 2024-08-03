package dev.ftb.mods.ftbquests.client.gui;

import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.AbstractButtonListScreen;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestLink;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.QuestObjectType;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.util.ConfigQuestObject;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class SelectQuestObjectScreen<T extends QuestObjectBase> extends AbstractButtonListScreen {

    private final ConfigQuestObject<T> config;

    private final ConfigCallback callback;

    public SelectQuestObjectScreen(ConfigQuestObject<T> config, ConfigCallback callback) {
        this.setTitle(Component.translatable("ftbquests.gui.select_quest_object"));
        this.setHasSearchBox(true);
        this.showBottomPanel(false);
        this.showCloseButton(true);
        this.focus();
        this.setBorder(1, 1, 1);
        this.config = config;
        this.callback = callback;
    }

    @Override
    public boolean onClosedByKey(Key key) {
        if (super.onClosedByKey(key)) {
            this.callback.save(false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        super.drawBackground(graphics, theme, x, y, w, h);
        GuiHelper.drawHollowRect(graphics, this.mainPanel.getX() - 1, this.mainPanel.getY() - 1, this.mainPanel.width + 2, this.mainPanel.height + 2, Color4I.DARK_GRAY.withAlpha(40), false);
    }

    @Override
    public void addButtons(Panel panel) {
        List<T> list = new ArrayList();
        ClientQuestFile file = ClientQuestFile.INSTANCE;
        for (QuestObjectBase objectBase : file.getAllObjects()) {
            if (this.config.predicate.test(objectBase)) {
                if (!file.canEdit() && objectBase instanceof QuestObject) {
                    QuestObject qo = (QuestObject) objectBase;
                    if (!qo.isVisible(file.selfTeamData)) {
                        continue;
                    }
                }
                list.add(objectBase);
            }
        }
        list.sort((o1, o2) -> {
            int i = Integer.compare(o1.getObjectType().ordinal(), o2.getObjectType().ordinal());
            return i == 0 ? o1.getTitle().getString().compareToIgnoreCase(o2.getTitle().getString()) : i;
        });
        if (this.config.predicate.test(null)) {
            panel.add(new SelectQuestObjectScreen.QuestObjectButton(panel, null));
        }
        for (T objectBasex : list) {
            panel.add(new SelectQuestObjectScreen.QuestObjectButton(panel, objectBasex));
        }
    }

    @Override
    protected void doCancel() {
        this.callback.save(false);
    }

    @Override
    protected void doAccept() {
        this.callback.save(true);
    }

    private class QuestObjectButton extends SimpleTextButton {

        public final T object;

        public QuestObjectButton(Panel panel, @Nullable T o) {
            super(panel, o == null ? Component.translatable("ftbquests.null") : o.getMutableTitle().withStyle(o.getObjectType().getColor()), (Icon) (o == null ? Color4I.empty() : o.getIcon()));
            this.object = o;
            this.setSize(200, 14);
        }

        private void addObject(TooltipList list, QuestObjectBase o) {
            list.add(QuestObjectType.NAME_MAP.getDisplayName(o.getObjectType()).copy().withStyle(ChatFormatting.GRAY).append(": ").append(o.getMutableTitle().withStyle(o.getObjectType().getColor())));
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            if (this.object != null) {
                list.add(this.object.getTitle());
                list.add(Component.literal("ID: ").withStyle(ChatFormatting.GRAY).append(Component.literal(this.object.toString()).withStyle(ChatFormatting.DARK_GRAY)));
                list.add(Component.literal("Type: ").withStyle(ChatFormatting.GRAY).append(QuestObjectType.NAME_MAP.getDisplayName(this.object.getObjectType()).copy().withStyle(this.object.getObjectType().getColor())));
                if (this.object instanceof Quest quest) {
                    this.addObject(list, quest.getChapter());
                    this.addRewardTooltip(list, quest);
                } else if (this.object instanceof QuestLink link) {
                    link.getQuest().ifPresent(questx -> {
                        this.addObject(list, link.getChapter());
                        list.add(Component.translatable("ftbquests.gui.linked_quest_id", Component.literal(questx.getCodeString()).withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY));
                        this.addObject(list, questx.getChapter());
                    });
                } else if (this.object instanceof Task task) {
                    Quest quest = task.getQuest();
                    this.addObject(list, quest.getChapter());
                    this.addObject(list, quest);
                    this.addRewardTooltip(list, quest);
                } else if (this.object instanceof Reward reward) {
                    Quest quest = reward.getQuest();
                    this.addObject(list, quest.getChapter());
                    this.addObject(list, quest);
                } else if (this.object instanceof RewardTable rewardTable) {
                    rewardTable.addMouseOverText(list, true, true);
                }
            }
        }

        private void addRewardTooltip(TooltipList list, Quest quest) {
            if (quest.getRewards().size() == 1) {
                this.addObject(list, (QuestObjectBase) quest.getRewards().stream().findFirst().orElseThrow());
            } else if (!quest.getRewards().isEmpty()) {
                list.add(Component.translatable("ftbquests.rewards").withStyle(ChatFormatting.GRAY));
                for (Reward reward : quest.getRewards()) {
                    list.add(Component.literal("  ").append(reward.getMutableTitle().withStyle(QuestObjectType.REWARD.getColor())));
                }
            }
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            if (this.isMouseOver) {
                Color4I.WHITE.withAlpha(30).draw(graphics, x, y, w, h);
            }
            Color4I.GRAY.withAlpha(40).draw(graphics, x, y + h, w, 1);
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            SelectQuestObjectScreen.this.config.setCurrentValue(this.object);
            SelectQuestObjectScreen.this.callback.save(true);
        }
    }
}