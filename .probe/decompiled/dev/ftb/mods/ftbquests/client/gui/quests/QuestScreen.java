package dev.ftb.mods.ftbquests.client.gui.quests;

import com.mojang.datafixers.util.Pair;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ConfigValue;
import dev.ftb.mods.ftblibrary.config.ConfigWithVariants;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.math.MathUtils;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.FTBQuestsClient;
import dev.ftb.mods.ftbquests.client.gui.CustomToast;
import dev.ftb.mods.ftbquests.client.gui.FTBQuestsTheme;
import dev.ftb.mods.ftbquests.client.gui.SelectQuestObjectScreen;
import dev.ftb.mods.ftbquests.net.ChangeProgressMessage;
import dev.ftb.mods.ftbquests.net.CopyChapterImageMessage;
import dev.ftb.mods.ftbquests.net.CopyQuestMessage;
import dev.ftb.mods.ftbquests.net.CreateObjectMessage;
import dev.ftb.mods.ftbquests.net.EditObjectMessage;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.ChapterImage;
import dev.ftb.mods.ftbquests.quest.Movable;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestLink;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.QuestObjectType;
import dev.ftb.mods.ftbquests.quest.reward.RandomReward;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.theme.QuestTheme;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import dev.ftb.mods.ftbquests.util.ConfigQuestObject;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class QuestScreen extends BaseScreen {

    final ClientQuestFile file;

    double scrollWidth;

    double scrollHeight;

    int prevMouseX;

    int prevMouseY;

    MouseButton grabbed = null;

    Chapter selectedChapter;

    final List<Movable> selectedObjects;

    final ExpandChaptersButton expandChaptersButton;

    final ChapterPanel chapterPanel;

    boolean movingObjects = false;

    int zoom = 16;

    static boolean grid = false;

    private QuestScreen.PersistedData pendingPersistedData;

    public final QuestPanel questPanel;

    public final OtherButtonsPanelBottom otherButtonsBottomPanel;

    public final OtherButtonsPanelTop otherButtonsTopPanel;

    public final ViewQuestPanel viewQuestPanel;

    public QuestScreen(ClientQuestFile clientQuestFile, @Nullable QuestScreen.PersistedData persistedData) {
        this.file = clientQuestFile;
        this.selectedObjects = new ArrayList();
        this.expandChaptersButton = new ExpandChaptersButton(this);
        this.chapterPanel = new ChapterPanel(this);
        this.selectedChapter = this.file.getFirstVisibleChapter(this.file.selfTeamData);
        this.questPanel = new QuestPanel(this);
        this.otherButtonsBottomPanel = new OtherButtonsPanelBottom(this);
        this.otherButtonsTopPanel = new OtherButtonsPanelTop(this);
        this.viewQuestPanel = new ViewQuestPanel(this);
        this.pendingPersistedData = persistedData;
        this.selectChapter(null);
    }

    @Nullable
    public Quest getViewedQuest() {
        return this.viewQuestPanel.getViewedQuest();
    }

    public boolean isViewingQuest() {
        return this.getViewedQuest() != null;
    }

    public void refreshChapterPanel() {
        this.chapterPanel.refreshWidgets();
    }

    public void refreshQuestPanel() {
        this.questPanel.refreshWidgets();
    }

    public void refreshViewQuestPanel() {
        this.viewQuestPanel.refreshWidgets();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return ClientQuestFile.INSTANCE.isPauseGame();
    }

    @Override
    public void addWidgets() {
        QuestTheme.currentObject = this.selectedChapter;
        this.add(this.questPanel);
        this.add(this.chapterPanel);
        this.add(this.expandChaptersButton);
        this.add(this.otherButtonsBottomPanel);
        this.add(this.otherButtonsTopPanel);
    }

    @Override
    public void alignWidgets() {
        QuestTheme.currentObject = this.selectedChapter;
        this.otherButtonsBottomPanel.alignWidgets();
        this.otherButtonsTopPanel.alignWidgets();
        this.chapterPanel.alignWidgets();
        this.expandChaptersButton.setPosAndSize(0, 0, 20, this.height);
    }

    @Override
    public boolean onInit() {
        return this.setFullscreen();
    }

    @Override
    public void onClosed() {
        this.file.setPersistedScreenInfo(this.getPersistedScreenData());
        super.onClosed();
    }

    public void selectChapter(@Nullable Chapter chapter) {
        if (this.selectedChapter != chapter) {
            this.closeQuest();
            this.selectedChapter = chapter;
            this.questPanel.refreshWidgets();
            this.questPanel.resetScroll();
        }
    }

    public void scrollTo(Movable movable) {
        this.questPanel.scrollTo(movable.getX(), movable.getY());
    }

    public void viewQuest(@Nullable Quest quest) {
        if (quest == null || this.file.canEdit() || !quest.hideDetailsUntilStartable() || this.file.selfTeamData.canStartTasks(quest)) {
            Quest current = this.viewQuestPanel.getViewedQuest();
            if (current != quest) {
                this.viewQuestPanel.setViewedQuest(quest);
                if (current == null) {
                    this.pushModalPanel(this.viewQuestPanel);
                } else if (quest == null) {
                    this.closeModalPanel(this.viewQuestPanel);
                }
                this.viewQuestPanel.updateMouseOver(this.getMouseX(), this.getMouseY());
            }
        }
    }

    @Override
    public void onBack() {
        if (this.isViewingQuest()) {
            this.closeQuest();
        } else {
            super.onBack();
        }
    }

    public void closeQuest() {
        this.viewQuest(null);
    }

    public void toggleSelected(Movable movable) {
        this.viewQuest(null);
        if (this.selectedObjects.contains(movable)) {
            this.selectedObjects.remove(movable);
        } else {
            this.selectedObjects.add(movable);
        }
    }

    public void addObjectMenuItems(List<ContextMenuItem> contextMenu, Runnable gui, QuestObjectBase object, Movable deletionFocus) {
        ConfigGroup group = new ConfigGroup("ftbquests");
        ConfigGroup subGroup = object.createSubGroup(group);
        object.fillConfigGroup(subGroup);
        contextMenu.add(new ContextMenuItem(Component.translatable("selectServer.edit"), ThemeProperties.EDIT_ICON.get(), b -> object.onEditButtonClicked(gui)));
        if (object instanceof QuestLink link) {
            link.getQuest().ifPresent(quest -> contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.edit_linked_quest"), ThemeProperties.EDIT_ICON.get(), b -> quest.onEditButtonClicked(gui))));
        }
        if (!subGroup.getValues().isEmpty()) {
            contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.copy_id.quick_properties"), Icons.SETTINGS, b -> this.openPropertiesSubMenu(object, subGroup)));
        }
        if (object instanceof RandomReward rr && !QuestObjectBase.isNull(rr.getTable())) {
            contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.reward_table.edit"), ThemeProperties.EDIT_ICON.get(), b -> rr.getTable().onEditButtonClicked(gui)));
        }
        long delId = deletionFocus == null ? object.id : deletionFocus.getMovableID();
        QuestObjectBase delObject = ClientQuestFile.INSTANCE.getBase(delId);
        if (delObject != null) {
            ContextMenuItem delete = new ContextMenuItem(Component.translatable("selectServer.delete"), ThemeProperties.DELETE_ICON.get(), b -> ClientQuestFile.INSTANCE.deleteObject(delId));
            if (!isShiftKeyDown()) {
                delete.setYesNoText(Component.translatable("delete_item", delObject.getTitle()));
            }
            contextMenu.add(delete);
        }
        contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.reset_progress"), ThemeProperties.RELOAD_ICON.get(), b -> ChangeProgressMessage.sendToServer(this.file.selfTeamData, object, progressChange -> progressChange.setReset(true))).setYesNoText(Component.translatable("ftbquests.gui.reset_progress_q")));
        contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.complete_instantly"), ThemeProperties.CHECK_ICON.get(), b -> ChangeProgressMessage.sendToServer(this.file.selfTeamData, object, progressChange -> progressChange.setReset(false))).setYesNoText(Component.translatable("ftbquests.gui.complete_instantly_q")));
        Component[] tooltip = object instanceof Quest ? new Component[] { Component.literal(QuestObjectBase.getCodeString(object)), Component.translatable("ftbquests.gui.copy_id.paste_hint").withStyle(ChatFormatting.GRAY) } : new Component[] { Component.literal(QuestObjectBase.getCodeString(object)) };
        if (this.selectedChapter != null) {
            if (this.selectedChapter.isAutofocus(object.id)) {
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquest.gui.clear_autofocused"), Icons.MARKER, b -> this.setAutofocusedId(0L)));
            } else if (object instanceof Quest || object instanceof QuestLink) {
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquest.gui.set_autofocused"), Icons.MARKER, b -> this.setAutofocusedId(object.id)));
            }
        }
        contextMenu.add(new TooltipContextMenuItem(Component.translatable("ftbquests.gui.copy_id"), ThemeProperties.WIKI_ICON.get(), b -> setClipboardString(object.getCodeString()), tooltip));
    }

    private void setAutofocusedId(long id) {
        this.selectedChapter.setAutofocus(id);
        new EditObjectMessage(this.selectedChapter).sendToServer();
    }

    private List<ContextMenuItem> scanForConfigEntries(List<ContextMenuItem> res, QuestObjectBase object, ConfigGroup g) {
        for (final ConfigValue<?> value : g.getValues()) {
            if (value instanceof ConfigWithVariants) {
                MutableComponent name = Component.translatable(value.getNameKey());
                if (!value.getCanEdit()) {
                    name = name.withStyle(ChatFormatting.GRAY);
                }
                res.add(new ContextMenuItem(name, Icons.SETTINGS, null) {

                    @Override
                    public void addMouseOverText(TooltipList list) {
                        list.add(value.getStringForGUI());
                    }

                    @Override
                    public void onClicked(Button button, Panel panel, MouseButton mouseButton) {
                        value.onClicked(button, mouseButton, accepted -> {
                            if (accepted) {
                                value.applyValue();
                                new EditObjectMessage(object).sendToServer();
                            }
                        });
                    }

                    @Override
                    public void drawIcon(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
                        value.getIcon().draw(graphics, x, y, w, h);
                    }
                });
            }
        }
        for (ConfigGroup sub : g.getSubgroups()) {
            this.scanForConfigEntries(res, object, sub);
        }
        return res;
    }

    private void openPropertiesSubMenu(QuestObjectBase object, ConfigGroup g) {
        List<ContextMenuItem> subMenu = new ArrayList();
        subMenu.add(new ContextMenuItem(object.getTitle(), Color4I.empty(), null).setCloseMenu(false));
        subMenu.add(ContextMenuItem.SEPARATOR);
        subMenu.addAll(this.scanForConfigEntries(new ArrayList(), object, g));
        this.getGui().openContextMenu(subMenu);
    }

    public static void displayError(Component error) {
        Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.TUTORIAL_HINT, Component.translatable("ftbquests.gui.error"), error));
    }

    private boolean moveSelectedQuests(double x, double y) {
        for (Movable movable : this.selectedObjects) {
            if (movable.getChapter() == this.selectedChapter) {
                movable.move(this.selectedChapter, movable.getX() + x, movable.getY() + y);
            }
        }
        return true;
    }

    private boolean copyObjectsToClipboard() {
        Movable toCopy = null;
        if (this.selectedObjects.size() > 1) {
            displayError(Component.translatable("ftbquests.quest.cannot_copy_many"));
        } else if (this.selectedObjects.isEmpty()) {
            toCopy = (Movable) this.questPanel.getWidgets().stream().filter(w -> w instanceof QuestPositionableButton && w.isMouseOver()).map(w -> ((QuestPositionableButton) w).moveAndDeleteFocus()).findFirst().orElse(null);
        } else {
            toCopy = (Movable) this.selectedObjects.get(0);
        }
        if (toCopy != null) {
            toCopy.copyToClipboard();
            Minecraft.getInstance().getToasts().addToast(new CustomToast(Component.translatable("ftbquests.quest.copied"), Icons.INFO, Component.literal(toCopy.getTitle().getString())));
            return true;
        } else {
            return false;
        }
    }

    private boolean pasteSelectedQuest(boolean withDeps) {
        return ChapterImage.isImageInClipboard() ? this.pasteSelectedImage() : (Boolean) QuestObjectBase.parseHexId(getClipboardString()).map(id -> {
            Quest quest = this.file.getQuest(id);
            if (quest == null) {
                return false;
            } else {
                Pair<Double, Double> qxy = this.getSnappedXY();
                new CopyQuestMessage(quest, this.selectedChapter, (Double) qxy.getFirst(), (Double) qxy.getSecond(), withDeps).sendToServer();
                return true;
            }
        }).orElse(false);
    }

    private boolean pasteSelectedImage() {
        return (Boolean) ChapterImageButton.getClipboardImage().map(clipImg -> {
            Pair<Double, Double> qxy = this.getSnappedXY();
            new CopyChapterImageMessage(clipImg, this.selectedChapter, (Double) qxy.getFirst(), (Double) qxy.getSecond()).sendToServer();
            return true;
        }).orElse(false);
    }

    private boolean pasteSelectedQuestLinks() {
        String clip = getClipboardString();
        return clip.isEmpty() ? false : (Boolean) QuestObjectBase.parseHexId(clip).map(id -> {
            if (this.file.getQuest(id) == null) {
                return false;
            } else {
                Pair<Double, Double> qxy = this.getSnappedXY();
                QuestLink link = new QuestLink(0L, this.selectedChapter, id);
                link.setPosition((Double) qxy.getFirst(), (Double) qxy.getSecond());
                new CreateObjectMessage(link, new CompoundTag(), false).sendToServer();
                return true;
            }
        }).orElse(false);
    }

    private Pair<Double, Double> getSnappedXY() {
        double snap = 1.0 / this.file.getGridScale();
        double qx = (double) Mth.floor(this.questPanel.questX * snap + 0.5) / snap;
        double qy = (double) Mth.floor(this.questPanel.questY * snap + 0.5) / snap;
        return Pair.of(qx, qy);
    }

    void deleteSelectedObjects() {
        this.selectedObjects.forEach(movable -> {
            if (movable instanceof Quest q) {
                this.file.deleteObject(q.id);
            } else if (movable instanceof QuestLink ql) {
                this.file.deleteObject(ql.id);
            } else if (movable instanceof ChapterImage img) {
                img.getChapter().removeImage(img);
                new EditObjectMessage(img.getChapter()).sendToServer();
            }
        });
        this.selectedObjects.clear();
    }

    @Override
    public boolean keyPressed(Key key) {
        if (super.keyPressed(key)) {
            return true;
        } else if (FTBQuestsClient.KEY_QUESTS.matches(key.keyCode, key.scanCode)) {
            this.closeGui(true);
            return true;
        } else {
            List<Chapter> visibleChapters = this.file.getVisibleChapters(this.file.selfTeamData);
            if (key.is(258)) {
                if (this.selectedChapter != null && this.file.getVisibleChapters(this.file.selfTeamData).size() > 1 && !visibleChapters.isEmpty()) {
                    this.selectChapter((Chapter) visibleChapters.get(MathUtils.mod(visibleChapters.indexOf(this.selectedChapter) + (isShiftKeyDown() ? -1 : 1), visibleChapters.size())));
                    this.selectedChapter.getAutofocus().ifPresent(this::scrollTo);
                }
                return true;
            } else if (key.is(32)) {
                this.questPanel.resetScroll();
                return true;
            } else if (key.is(82) && key.modifiers.onlyControl()) {
                grid = !grid;
                return true;
            } else if (key.is(70) && key.modifiers.onlyControl()) {
                this.openQuestSelectionGUI();
                return true;
            } else if (key.is(48)) {
                this.addZoom((double) (16 - this.zoom) / 4.0);
                return true;
            } else if (key.keyCode >= 49 && key.keyCode <= 57) {
                int i = key.keyCode - 49;
                if (i < visibleChapters.size()) {
                    this.selectChapter((Chapter) visibleChapters.get(i));
                    this.selectedChapter.getAutofocus().ifPresent(this::scrollTo);
                }
                return true;
            } else if (!this.file.canEdit()) {
                return false;
            } else {
                if (key.is(261) && !this.selectedObjects.isEmpty()) {
                    if (!isShiftKeyDown()) {
                        Component title = Component.translatable("delete_item", Component.translatable("ftbquests.objects", this.selectedObjects.size()));
                        this.getGui().openYesNo(title, Component.empty(), this::deleteSelectedObjects);
                    } else {
                        this.deleteSelectedObjects();
                    }
                } else if (key.modifiers.control()) {
                    double step = key.modifiers.shift() ? 0.1 : 0.5;
                    switch(key.keyCode) {
                        case 65:
                            if (this.selectedChapter != null) {
                                this.selectedObjects.addAll(this.selectedChapter.getQuests());
                                this.selectedObjects.addAll(this.selectedChapter.getQuestLinks());
                                this.selectedObjects.addAll(this.selectedChapter.getImages());
                            }
                            return true;
                        case 67:
                            return this.copyObjectsToClipboard();
                        case 68:
                            this.selectedObjects.clear();
                            return true;
                        case 86:
                            if (key.modifiers.alt()) {
                                return this.pasteSelectedQuestLinks();
                            }
                            return this.pasteSelectedQuest(!key.modifiers.shift());
                        case 262:
                            return this.moveSelectedQuests(step, 0.0);
                        case 263:
                            return this.moveSelectedQuests(-step, 0.0);
                        case 264:
                            return this.moveSelectedQuests(0.0, step);
                        case 265:
                            return this.moveSelectedQuests(0.0, -step);
                    }
                }
                return false;
            }
        }
    }

    private void openQuestSelectionGUI() {
        ConfigQuestObject<QuestObject> c = new ConfigQuestObject<>(QuestObjectType.CHAPTER.or(QuestObjectType.QUEST).or(QuestObjectType.QUEST_LINK));
        new SelectQuestObjectScreen<>(c, accepted -> {
            if (accepted) {
                if (c.getValue() instanceof Chapter chapter) {
                    this.selectChapter(chapter);
                } else if (c.getValue() instanceof Quest quest) {
                    this.zoom = 20;
                    this.selectChapter(quest.getChapter());
                    this.questPanel.scrollTo(quest.getX(), quest.getY());
                    this.viewQuest(quest);
                } else if (c.getValue() instanceof QuestLink link) {
                    this.zoom = 20;
                    this.selectChapter(link.getChapter());
                    this.questPanel.scrollTo(link.getX(), link.getY());
                    link.getQuest().ifPresent(this::viewQuest);
                }
            }
            this.openGui();
        }).openGui();
    }

    @Override
    public void tick() {
        if (this.pendingPersistedData != null) {
            this.restorePersistedScreenData(this.file, this.pendingPersistedData);
            this.pendingPersistedData = null;
        }
        if (this.selectedChapter != null && !this.selectedChapter.isValid()) {
            this.selectChapter(null);
        }
        if (this.selectedChapter == null) {
            this.selectChapter(this.file.getFirstVisibleChapter(this.file.selfTeamData));
            if (this.selectedChapter != null) {
                this.selectedChapter.getAutofocus().ifPresent(this::scrollTo);
            }
        }
        super.tick();
    }

    public int getZoom() {
        return this.zoom;
    }

    public double getQuestButtonSize() {
        return (double) this.getZoom() * 3.0 / 2.0;
    }

    public double getQuestButtonSpacing() {
        return (double) this.getZoom() * ThemeProperties.QUEST_SPACING.get(this.selectedChapter) / 4.0;
    }

    public void addZoom(double up) {
        int z = this.zoom;
        this.zoom = (int) Mth.clamp((double) this.zoom + up * 4.0, 4.0, 28.0);
        if (this.zoom != z) {
            this.grabbed = null;
            this.questPanel.withPreservedPos(QuestPanel::resetScroll);
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        QuestTheme.currentObject = this.selectedChapter;
        super.drawBackground(graphics, theme, x, y, w, h);
        int pw = 20;
        Color4I borderColor = ThemeProperties.WIDGET_BORDER.get(this.selectedChapter);
        Color4I backgroundColor = ThemeProperties.WIDGET_BACKGROUND.get(this.selectedChapter);
        borderColor.draw(graphics, x + pw - 1, y + 1, 1, h - 2);
        backgroundColor.draw(graphics, x + 1, y + 1, pw - 2, h - 2);
        borderColor.draw(graphics, x + w - pw, y + 1, 1, h - 2);
        backgroundColor.draw(graphics, x + w - pw + 1, y + 1, pw - 2, h - 2);
        if (this.grabbed != null) {
            int mx = this.getMouseX();
            int my = this.getMouseY();
            if (this.grabbed.isLeft()) {
                if (this.scrollWidth > (double) this.questPanel.width) {
                    this.questPanel.setScrollX(Math.max(Math.min(this.questPanel.getScrollX() + (double) (this.prevMouseX - mx), this.scrollWidth - (double) this.questPanel.width), 0.0));
                } else {
                    this.questPanel.setScrollX((this.scrollWidth - (double) this.questPanel.width) / 2.0);
                }
                if (this.scrollHeight > (double) this.questPanel.height) {
                    this.questPanel.setScrollY(Math.max(Math.min(this.questPanel.getScrollY() + (double) (this.prevMouseY - my), this.scrollHeight - (double) this.questPanel.height), 0.0));
                } else {
                    this.questPanel.setScrollY((this.scrollHeight - (double) this.questPanel.height) / 2.0);
                }
                this.prevMouseX = mx;
                this.prevMouseY = my;
            } else if (this.grabbed.isMiddle()) {
                int boxX = Math.min(this.prevMouseX, mx);
                int boxY = Math.min(this.prevMouseY, my);
                int boxW = Math.abs(mx - this.prevMouseX);
                int boxH = Math.abs(my - this.prevMouseY);
                GuiHelper.drawHollowRect(graphics, boxX, boxY, boxW, boxH, Color4I.DARK_GRAY, false);
                Color4I.DARK_GRAY.withAlpha(40).draw(graphics, boxX, boxY, boxW, boxH);
            }
        }
    }

    void selectAllQuestsInBox(int mouseX, int mouseY, double scrollX, double scrollY) {
        int x1 = Math.min(this.prevMouseX, mouseX);
        int x2 = Math.max(this.prevMouseX, mouseX);
        int y1 = Math.min(this.prevMouseY, mouseY);
        int y2 = Math.max(this.prevMouseY, mouseY);
        Rect2i rect = new Rect2i(x1, y1, x2 - x1, y2 - y1);
        if (!Screen.hasControlDown()) {
            this.selectedObjects.clear();
        }
        this.questPanel.getWidgets().forEach(w -> {
            if (w instanceof QuestPositionableButton qb && rect.contains((int) ((double) w.getX() - scrollX), (int) ((double) w.getY() - scrollY))) {
                this.toggleSelected(qb.moveAndDeleteFocus());
            }
        });
    }

    @Override
    public void drawForeground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        Color4I borderColor = ThemeProperties.WIDGET_BORDER.get(this.selectedChapter);
        GuiHelper.drawHollowRect(graphics, x, y, w, h, borderColor, false);
        super.drawForeground(graphics, theme, x, y, w, h);
    }

    @Override
    public Theme getTheme() {
        return FTBQuestsTheme.INSTANCE;
    }

    @Override
    public boolean drawDefaultBackground(GuiGraphics graphics) {
        return false;
    }

    public void open(@Nullable QuestObject object, boolean focus) {
        if (object instanceof Chapter chapter) {
            this.selectChapter(chapter);
        } else if (object instanceof Quest quest) {
            this.selectChapter(quest.getChapter());
            this.viewQuest(quest);
            if (focus) {
                this.questPanel.scrollTo(quest.getX() + 0.5, quest.getY() + 0.5);
            }
        } else if (object instanceof QuestLink link) {
            link.getQuest().ifPresent(questx -> {
                this.selectChapter(link.getChapter());
                this.viewQuest(questx);
                if (focus) {
                    this.questPanel.scrollTo(link.getX() + 0.5, link.getY() + 0.5);
                }
            });
        } else if (object instanceof Task task) {
            this.selectChapter(task.getQuest().getChapter());
            this.viewQuest(task.getQuest());
        }
        this.pendingPersistedData = null;
        if (ClientUtils.getCurrentGuiAs(QuestScreen.class) != this) {
            this.openGui();
        }
    }

    @Override
    public boolean handleClick(String scheme, String path) {
        if (scheme.isEmpty() && path.startsWith("#")) {
            this.open(this.file.get(this.file.getID(path)), true);
            return true;
        } else {
            return super.handleClick(scheme, path);
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        super.addMouseOverText(list);
    }

    public void addInfoTooltip(TooltipList list, QuestObjectBase object) {
        if (isKeyDown(290) || isShiftKeyDown() && isCtrlKeyDown()) {
            list.add(Component.literal(object.getCodeString()).withStyle(ChatFormatting.DARK_GRAY));
            if (object instanceof QuestObject) {
                this.file.selfTeamData.getStartedTime(object.id).ifPresent(date -> list.add(formatDate("Started", date)));
                this.file.selfTeamData.getCompletedTime(object.id).ifPresent(date -> list.add(formatDate("Completed", date)));
            } else if (object instanceof Reward r) {
                this.file.selfTeamData.getRewardClaimTime(FTBQuestsClient.getClientPlayer().m_20148_(), r).ifPresent(date -> list.add(formatDate("Claimed", date)));
            }
        }
    }

    private static Component formatDate(String prefix, Date date) {
        return Component.literal(prefix + ": ").append(Component.literal(DateFormat.getDateTimeInstance().format(date)).withStyle(ChatFormatting.DARK_GRAY));
    }

    public Collection<Quest> getSelectedQuests() {
        Map<Long, Quest> questMap = new HashMap();
        this.selectedObjects.forEach(movable -> {
            if (movable instanceof Quest q) {
                questMap.put(q.id, q);
            } else if (movable instanceof QuestLink ql) {
                ql.getQuest().ifPresent(qx -> questMap.put(qx.id, qx));
            }
        });
        return List.copyOf(questMap.values());
    }

    public QuestScreen.PersistedData getPersistedScreenData() {
        return this.pendingPersistedData != null ? this.pendingPersistedData : new QuestScreen.PersistedData(this);
    }

    private void restorePersistedScreenData(BaseQuestFile file, QuestScreen.PersistedData persistedData) {
        this.zoom = persistedData.zoom;
        this.selectChapter(file.getChapter(persistedData.selectedChapter));
        this.selectedObjects.clear();
        persistedData.selectedQuests.stream().mapToLong(id -> id).filter(id -> file.get(id) instanceof Movable).mapToObj(id -> (Movable) file.get(id)).forEach(this.selectedObjects::add);
        this.questPanel.scrollTo(persistedData.scrollX, persistedData.scrollY);
        this.questPanel.centerQuestX = persistedData.scrollX;
        this.questPanel.centerQuestY = persistedData.scrollY;
        this.chapterPanel.setExpanded(persistedData.chaptersExpanded);
    }

    public void initiateMoving(Movable movable) {
        this.movingObjects = true;
        this.selectedObjects.clear();
        this.toggleSelected(movable);
    }

    public static class PersistedData {

        private final int zoom;

        private final double scrollX;

        private final double scrollY;

        private final long selectedChapter;

        private final List<Long> selectedQuests;

        private final boolean chaptersExpanded;

        private PersistedData(QuestScreen questScreen) {
            this.zoom = questScreen.zoom;
            this.scrollX = questScreen.questPanel.centerQuestX;
            this.scrollY = questScreen.questPanel.centerQuestY;
            this.selectedChapter = questScreen.selectedChapter == null ? 0L : questScreen.selectedChapter.id;
            this.selectedQuests = questScreen.selectedObjects.stream().map(Movable::getMovableID).filter(id -> id != 0L).toList();
            this.chaptersExpanded = questScreen.chapterPanel.expanded;
        }
    }
}