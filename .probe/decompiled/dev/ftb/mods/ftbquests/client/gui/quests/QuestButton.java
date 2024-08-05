package dev.ftb.mods.ftbquests.client.gui.quests;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftblibrary.config.DoubleConfig;
import dev.ftb.mods.ftblibrary.config.ui.EditStringConfigOverlay;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.math.PixelBuffer;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import dev.ftb.mods.ftbquests.client.gui.ContextMenuBuilder;
import dev.ftb.mods.ftbquests.net.CreateObjectMessage;
import dev.ftb.mods.ftbquests.net.DeleteObjectMessage;
import dev.ftb.mods.ftbquests.net.EditObjectMessage;
import dev.ftb.mods.ftbquests.quest.Movable;
import dev.ftb.mods.ftbquests.quest.ProgressionMode;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.QuestShape;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import dev.ftb.mods.ftbquests.quest.reward.RewardTypes;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.world.entity.player.Player;

public class QuestButton extends Button implements QuestPositionableButton {

    protected final QuestScreen questScreen;

    final Quest quest;

    private Collection<QuestButton> dependencies = null;

    public QuestButton(Panel panel, Quest quest) {
        super(panel, quest.getTitle(), quest.getIcon());
        this.questScreen = (QuestScreen) panel.getGui();
        this.setSize(20, 20);
        this.quest = quest;
    }

    @Override
    public boolean isEnabled() {
        return this.questScreen.file.canEdit() || this.quest.isVisible(this.questScreen.file.selfTeamData);
    }

    @Override
    public boolean shouldDraw() {
        return this.questScreen.file.canEdit() || this.quest.isVisible(this.questScreen.file.selfTeamData);
    }

    @Override
    public boolean checkMouseOver(int mouseX, int mouseY) {
        return this.shouldDraw() && !this.questScreen.movingObjects && !this.questScreen.viewQuestPanel.isMouseOver() && !this.questScreen.chapterPanel.isMouseOver() ? super.checkMouseOver(mouseX, mouseY) : false;
    }

    @Override
    public void updateMouseOver(int mouseX, int mouseY) {
        super.updateMouseOver(mouseX, mouseY);
        if (this.questScreen.questPanel.mouseOverQuest != null && this.questScreen.questPanel.mouseOverQuest != this) {
            this.isMouseOver = false;
        }
        if (this.isMouseOver) {
            QuestShape shape = QuestShape.get(this.quest.getShape());
            int ax = this.getX();
            int ay = this.getY();
            double relX = (double) (mouseX - ax) / (double) this.width;
            double relY = (double) (mouseY - ay) / (double) this.height;
            PixelBuffer pixelBuffer = shape.getShapePixels();
            int rx = (int) (relX * (double) pixelBuffer.getWidth());
            int ry = (int) (relY * (double) pixelBuffer.getHeight());
            if (rx >= 0 && ry >= 0 && rx < pixelBuffer.getWidth() && ry < pixelBuffer.getHeight()) {
                int a = pixelBuffer.getRGB(rx, ry) >> 24 & 0xFF;
                if (a < 5) {
                    this.isMouseOver = false;
                }
            } else {
                this.isMouseOver = false;
            }
        }
        if (this.isMouseOver && this.questScreen.questPanel.mouseOverQuest == null) {
            this.questScreen.questPanel.mouseOverQuest = this;
        }
    }

    public Collection<QuestButton> getDependencies() {
        if (this.dependencies == null) {
            List<QuestButton> list = new ArrayList();
            this.quest.streamDependencies().forEach(dependency -> {
                if (dependency.isValid() && dependency instanceof Quest) {
                    for (Widget widget : this.questScreen.questPanel.getWidgets()) {
                        if (widget instanceof QuestButton) {
                            QuestButton qb = (QuestButton) widget;
                            if (dependency == qb.quest) {
                                list.add(qb);
                            }
                        }
                    }
                }
            });
            this.dependencies = List.copyOf(list);
        }
        return this.dependencies;
    }

    @Override
    public void onClicked(MouseButton button) {
        this.playClickSound();
        if (this.questScreen.file.canEdit() && button.isRight()) {
            List<ContextMenuItem> contextMenu = new ArrayList();
            Collection<Quest> selected = this.questScreen.getSelectedQuests();
            if (!selected.isEmpty()) {
                if (!selected.contains(this.quest)) {
                    contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.add_dependencies"), ThemeProperties.ADD_ICON.get(), b -> selected.forEach(q -> this.editDependency(this.quest, q, true))));
                    contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.remove_dependencies"), ThemeProperties.DELETE_ICON.get(), b -> selected.forEach(q -> this.editDependency(this.quest, q, false))));
                    contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.add_dependencies_self"), ThemeProperties.ADD_ICON.get(), b -> selected.forEach(q -> this.editDependency(q, this.quest, true))));
                    contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.remove_dependencies_self"), ThemeProperties.DELETE_ICON.get(), b -> selected.forEach(q -> this.editDependency(q, this.quest, false))));
                } else {
                    contextMenu.add(new ContextMenuItem(Component.translatable("gui.move"), ThemeProperties.MOVE_UP_ICON.get(this.quest), b -> this.questScreen.movingObjects = true));
                    contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.add_reward_all"), ThemeProperties.ADD_ICON.get(this.quest), b -> this.openAddRewardContextMenu()));
                    contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.clear_reward_all"), ThemeProperties.CLOSE_ICON.get(this.quest), b -> selected.forEach(q -> q.getRewards().forEach(r -> new DeleteObjectMessage(r.id).sendToServer()))));
                    contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.bulk_change_size"), Icons.SETTINGS, b -> this.bulkChangeSize()));
                    contextMenu.add(new ContextMenuItem(Component.translatable("selectServer.delete"), ThemeProperties.DELETE_ICON.get(this.quest), b -> this.questScreen.deleteSelectedObjects()).setYesNoText(Component.translatable("delete_item", Component.translatable("ftbquests.quests").append(" [" + this.questScreen.selectedObjects.size() + "]"))));
                }
                contextMenu.add(ContextMenuItem.SEPARATOR);
                contextMenu.add(new ContextMenuItem(Component.literal("Ctrl+A to select all quests").withStyle(ChatFormatting.GRAY), Icons.INFO, null).setCloseMenu(false));
                contextMenu.add(new ContextMenuItem(Component.literal("Ctrl+D to deselect all quests").withStyle(ChatFormatting.GRAY), Icons.INFO, null).setCloseMenu(false));
                contextMenu.add(new ContextMenuItem(Component.literal("Ctrl+Arrow Key to move selected quests").withStyle(ChatFormatting.GRAY), Icons.INFO, null).setCloseMenu(false));
                this.getGui().openContextMenu(contextMenu);
            } else {
                ContextMenuBuilder.create(this.theQuestObject(), this.questScreen).withDeletionFocus(this.moveAndDeleteFocus()).insertAtTop(List.of(new TooltipContextMenuItem(Component.translatable("gui.move"), ThemeProperties.MOVE_UP_ICON.get(this.quest), b -> this.questScreen.initiateMoving(this.moveAndDeleteFocus()), Component.translatable("ftbquests.gui.move_tooltip").withStyle(ChatFormatting.DARK_GRAY)))).openContextMenu(this.getGui());
            }
        } else if (button.isLeft()) {
            if (isCtrlKeyDown() && this.questScreen.file.canEdit()) {
                if (this.questScreen.isViewingQuest()) {
                    this.questScreen.closeQuest();
                }
                this.questScreen.toggleSelected(this.moveAndDeleteFocus());
            } else if (!this.quest.getGuidePage().isEmpty() && this.quest.getTasks().isEmpty() && this.quest.getRewards().isEmpty() && this.quest.getDescription().isEmpty()) {
                this.handleClick("guide", this.quest.getGuidePage());
            } else {
                this.questScreen.open(this.theQuestObject(), false);
            }
        } else if (this.questScreen.file.canEdit() && button.isMiddle()) {
            if (!this.questScreen.selectedObjects.contains(this.moveAndDeleteFocus())) {
                this.questScreen.toggleSelected(this.moveAndDeleteFocus());
            }
            this.questScreen.movingObjects = true;
        } else if (button.isRight()) {
            this.questScreen.movingObjects = false;
            if (this.questScreen.getViewedQuest() != this.quest) {
                this.questScreen.viewQuest(this.quest);
            } else {
                this.questScreen.closeQuest();
            }
        }
    }

    private void bulkChangeSize() {
        Collection<Quest> quests = this.questScreen.getSelectedQuests();
        if (!quests.isEmpty()) {
            DoubleConfig c = new DoubleConfig(0.0625, 8.0);
            c.setValue((Double) quests.stream().findFirst().map(Quest::getSize).orElse(1.0));
            EditStringConfigOverlay<Double> overlay = new EditStringConfigOverlay<>(this.getGui(), c, accepted -> {
                if (accepted) {
                    quests.forEach(q -> {
                        q.setSize(c.getValue());
                        new EditObjectMessage(q).sendToServer();
                    });
                }
                this.run();
            }, Component.translatable("ftbquests.quest.appearance.size")).atMousePosition();
            overlay.setExtraZlevel(600);
            this.getGui().pushModalPanel(overlay);
        }
    }

    private void openAddRewardContextMenu() {
        List<ContextMenuItem> contextMenu2 = new ArrayList();
        for (RewardType type : RewardTypes.TYPES.values()) {
            contextMenu2.add(new ContextMenuItem(type.getDisplayName(), type.getIconSupplier(), b -> {
                this.playClickSound();
                type.getGuiProvider().openCreationGui(this.parent, this.quest, reward -> this.questScreen.getSelectedQuests().forEach(quest -> {
                    Reward newReward = QuestObjectBase.copy(reward, () -> type.createReward(0L, quest));
                    if (newReward != null) {
                        CompoundTag extra = new CompoundTag();
                        extra.putString("type", type.getTypeForNBT());
                        new CreateObjectMessage(newReward, extra).sendToServer();
                    }
                }));
            }));
        }
        this.getGui().openContextMenu(contextMenu2);
    }

    private void editDependency(Quest quest, QuestObject object, boolean add) {
        List<QuestObject> prevDeps = quest.streamDependencies().toList();
        if (add != quest.hasDependency(object)) {
            if (add) {
                quest.addDependency(object);
            } else {
                quest.removeDependency(object);
            }
        }
        quest.removeInvalidDependencies();
        if (quest.verifyDependencies(false)) {
            new EditObjectMessage(quest).sendToServer();
            this.questScreen.questPanel.refreshWidgets();
        } else {
            quest.clearDependencies();
            prevDeps.forEach(quest::addDependency);
            QuestScreen.displayError(Component.translatable("ftbquests.gui.looping_dependencies"));
        }
    }

    @Override
    public Optional<PositionedIngredient> getIngredientUnderMouse() {
        return this.quest.getTasks().size() == 1 ? ((Task) this.quest.getTasks().stream().findFirst().orElseThrow()).getIngredient(this) : Optional.empty();
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        this.questScreen.addInfoTooltip(list, this.quest);
        Component title = this.getTitle();
        if (this.questScreen.file.selfTeamData != null && this.questScreen.file.selfTeamData.isStarted(this.quest) && !this.questScreen.file.selfTeamData.isCompleted(this.quest)) {
            title = title.copy().append(Component.literal(" " + this.questScreen.file.selfTeamData.getRelativeProgress(this.quest) + "%").withStyle(ChatFormatting.DARK_GRAY));
        }
        if (title.getString().contains("\n")) {
            title.visit((style, txt) -> {
                for (String s : txt.split("\n")) {
                    if (!s.isEmpty()) {
                        list.add(Component.literal(s).withStyle(style));
                    }
                }
                return Optional.empty();
            }, title.getStyle());
        } else {
            list.add(title);
        }
        Component description = this.quest.getSubtitle();
        if (description.getContents() != ComponentContents.EMPTY) {
            list.add(Component.literal("").append(description).withStyle(ChatFormatting.GRAY));
        }
        if (this.quest.isOptional()) {
            list.add(Component.literal("[").withStyle(ChatFormatting.GRAY).append(Component.translatable("ftbquests.quest.misc.optional")).append("]"));
        }
        if (this.quest.canBeRepeated()) {
            list.add(Component.translatable("ftbquests.quest.misc.can_repeat").withStyle(ChatFormatting.GRAY));
        }
        if (!this.questScreen.file.selfTeamData.canStartTasks(this.quest)) {
            list.add(Component.literal("[").withStyle(ChatFormatting.DARK_GRAY).append(Component.translatable("ftbquests.quest.locked")).append("]"));
        }
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        Color4I outlineColor = ThemeProperties.QUEST_NOT_STARTED_COLOR.get(this.quest);
        Icon questIcon = Color4I.empty();
        Icon hiddenIcon = Color4I.empty();
        TeamData teamData = this.questScreen.file.selfTeamData;
        boolean isCompleted = teamData.isCompleted(this.quest);
        boolean isStarted = isCompleted || teamData.isStarted(this.quest);
        boolean canStart = isCompleted || isStarted || teamData.areDependenciesComplete(this.quest);
        Player player = Minecraft.getInstance().player;
        if (canStart) {
            if (isCompleted) {
                if (teamData.hasUnclaimedRewards(player.m_20148_(), this.quest)) {
                    questIcon = ThemeProperties.ALERT_ICON.get(this.quest);
                } else if (teamData.isQuestPinned(player, this.quest.id)) {
                    questIcon = ThemeProperties.PIN_ICON_ON.get();
                } else {
                    questIcon = ThemeProperties.CHECK_ICON.get(this.quest);
                }
                outlineColor = ThemeProperties.QUEST_COMPLETED_COLOR.get(this.quest);
            } else if (isStarted) {
                if (teamData.areDependenciesComplete(this.quest)) {
                    outlineColor = ThemeProperties.QUEST_STARTED_COLOR.get(this.quest);
                }
                if (this.quest.getProgressionMode() == ProgressionMode.FLEXIBLE && this.quest.allTasksCompleted(teamData)) {
                    questIcon = new ThemeProperties.CheckIcon(Color4I.rgb(6316128), Color4I.rgb(8421504));
                }
            }
        } else {
            outlineColor = ThemeProperties.QUEST_LOCKED_COLOR.get(this.quest);
        }
        if (questIcon == Color4I.empty() && teamData.isQuestPinned(player, this.quest.id)) {
            questIcon = ThemeProperties.PIN_ICON_ON.get();
        }
        if (this.questScreen.file.canEdit() && !this.quest.isVisible(teamData)) {
            hiddenIcon = ThemeProperties.HIDDEN_ICON.get();
        }
        QuestShape shape = QuestShape.get(this.getShape());
        shape.getShape().withColor(Color4I.DARK_GRAY).draw(graphics, x, y, w, h);
        shape.getBackground().withColor(Color4I.WHITE.withAlpha(150)).draw(graphics, x, y, w, h);
        shape.getOutline().withColor(outlineColor).draw(graphics, x, y, w, h);
        PoseStack poseStack = graphics.pose();
        if (!this.icon.isEmpty()) {
            float s = (float) w * 0.6666667F * (float) this.quest.getIconScale();
            poseStack.pushPose();
            poseStack.translate((double) x + (double) ((float) w - s) / 2.0, (double) y + (double) ((float) h - s) / 2.0, 0.0);
            poseStack.scale(s, s, 1.0F);
            this.icon.draw(graphics, 0, 0, 1, 1);
            poseStack.popPose();
        }
        GuiHelper.setupDrawing();
        if (this.questScreen.getViewedQuest() == this.quest || this.questScreen.selectedObjects.contains(this.moveAndDeleteFocus())) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 1.0F);
            Color4I col = Color4I.WHITE.withAlpha((int) (190.0 + Math.sin((double) System.currentTimeMillis() * 0.003) * 50.0));
            shape.getOutline().withColor(col).draw(graphics, x, y, w, h);
            shape.getBackground().withColor(col).draw(graphics, x, y, w, h);
            poseStack.popPose();
        }
        if (!canStart || !teamData.areDependenciesComplete(this.quest)) {
            shape.getShape().withColor(Color4I.BLACK.withAlpha(100)).draw(graphics, x, y, w, h);
        }
        if (this.isMouseOver()) {
            shape.getShape().withColor(Color4I.WHITE.withAlpha(100)).draw(graphics, x, y, w, h);
        }
        if (!questIcon.isEmpty()) {
            float s = (float) w / 8.0F * 3.0F;
            poseStack.pushPose();
            poseStack.translate((float) (x + w) - s, (float) y, 0.0F);
            poseStack.scale(s, s, 1.0F);
            questIcon.draw(graphics, 0, 0, 1, 1);
            poseStack.popPose();
        }
        if (!hiddenIcon.isEmpty()) {
            float s = (float) w / 8.0F * 3.0F;
            poseStack.pushPose();
            poseStack.translate((float) x, (float) y, 0.0F);
            poseStack.scale(s, s, 1.0F);
            hiddenIcon.draw(graphics, 0, 0, 1, 1);
            poseStack.popPose();
        }
    }

    protected String getShape() {
        return this.quest.getShape();
    }

    @Override
    public QuestPositionableButton.Position getPosition() {
        return new QuestPositionableButton.Position(this.quest.getX(), this.quest.getY(), this.quest.getWidth(), this.quest.getHeight());
    }

    protected QuestObject theQuestObject() {
        return this.quest;
    }

    @Override
    public Movable moveAndDeleteFocus() {
        return (Movable) this.theQuestObject();
    }
}