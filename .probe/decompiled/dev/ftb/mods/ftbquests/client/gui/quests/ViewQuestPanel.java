package dev.ftb.mods.ftbquests.client.gui.quests;

import com.mojang.datafixers.util.Pair;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ImageResourceConfig;
import dev.ftb.mods.ftblibrary.config.ListConfig;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.config.ui.EditStringConfigOverlay;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.BlankPanel;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.ColorWidget;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.CursorType;
import dev.ftb.mods.ftblibrary.ui.ModalPanel;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleButton;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.VerticalSpaceWidget;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.CompactGridLayout;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.ImageComponent;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.ImageComponentWidget;
import dev.ftb.mods.ftbquests.client.gui.MultilineTextEditorScreen;
import dev.ftb.mods.ftbquests.net.EditObjectMessage;
import dev.ftb.mods.ftbquests.net.TogglePinnedMessage;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestLink;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.reward.RewardAutoClaim;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.theme.QuestTheme;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ViewQuestPanel extends ModalPanel {

    public static final Icon PAGEBREAK_ICON = Icon.getIcon(new ResourceLocation("ftbquests", "textures/gui/pagebreak.png"));

    private final QuestScreen questScreen;

    private Quest quest = null;

    private Icon icon = Color4I.empty();

    private Button buttonOpenDependencies;

    private BlankPanel panelContent;

    private BlankPanel panelTasks;

    private BlankPanel panelText;

    private TextField titleField;

    private final List<Pair<Integer, Integer>> pageIndices = new ArrayList();

    private final Long2IntMap currentPages = new Long2IntOpenHashMap();

    private long lastScrollTime = 0L;

    public ViewQuestPanel(QuestScreen questScreen) {
        super(questScreen);
        this.questScreen = questScreen;
        this.setPosAndSize(-1, -1, 0, 0);
        this.setOnlyRenderWidgetsInside(true);
        this.setOnlyInteractWithWidgetsInside(true);
        this.setExtraZlevel(300);
    }

    @Override
    public void onClosed() {
        this.quest = null;
        this.updateMouseOver(this.getMouseX(), this.getMouseY());
    }

    @Override
    public boolean checkMouseOver(int mouseX, int mouseY) {
        return this.quest != null && super.checkMouseOver(mouseX, mouseY);
    }

    public Quest getViewedQuest() {
        return this.quest;
    }

    public void setViewedQuest(Quest newQuest) {
        if (this.quest != newQuest) {
            this.quest = newQuest;
            this.refreshWidgets();
        }
    }

    public boolean canEdit() {
        return this.quest.getQuestFile().canEdit();
    }

    private void buildPageIndices() {
        this.pageIndices.clear();
        if (this.quest != null) {
            this.pageIndices.addAll(this.quest.buildDescriptionIndex());
        }
    }

    private int getCurrentPage() {
        if (this.quest == null) {
            return 0;
        } else {
            int page = this.currentPages.getOrDefault(this.quest.id, 0);
            if (page < 0 || page >= this.pageIndices.size()) {
                page = 0;
                this.currentPages.put(this.quest.id, 0);
            }
            return page;
        }
    }

    private void setCurrentPage(int page) {
        if (this.quest != null) {
            this.currentPages.put(this.quest.id, page);
        }
    }

    @Override
    public void addWidgets() {
        this.setPosAndSize(-1, -1, 1, 1);
        if (this.quest != null) {
            QuestObjectBase prev = QuestTheme.currentObject;
            QuestTheme.currentObject = this.quest;
            this.setScrollX(0.0);
            this.setScrollY(0.0);
            this.icon = this.quest.getIcon();
            boolean canEdit = this.questScreen.file.canEdit();
            this.titleField = new ViewQuestPanel.QuestDescriptionField(this, canEdit, (b, clickedW) -> this.editTitle()).addFlags(4).setMinWidth(150).setMaxWidth(500).setSpacing(9).setText(this.quest.getTitle().copy().withStyle(Style.EMPTY.withColor(TextColor.fromRgb(ThemeProperties.QUEST_VIEW_TITLE.get().rgb()))));
            int w = Math.max(200, this.titleField.width + 54);
            if (this.quest.getMinWidth() > 0) {
                w = Math.max(this.quest.getMinWidth(), w);
            } else if (this.questScreen.selectedChapter.getDefaultMinWidth() > 0) {
                w = Math.max(this.questScreen.selectedChapter.getDefaultMinWidth(), w);
            }
            this.titleField.setPosAndSize(27, 4, w - 54, this.titleField.height);
            this.add(this.titleField);
            this.add(this.panelContent = new BlankPanel(this, "ContentPanel"));
            this.panelContent.add(this.panelTasks = new BlankPanel(this.panelContent, "TasksPanel"));
            BlankPanel panelRewards;
            this.panelContent.add(panelRewards = new BlankPanel(this.panelContent, "RewardsPanel"));
            this.panelContent.add(this.panelText = new BlankPanel(this.panelContent, "TextPanel"));
            int bsize = 18;
            boolean seq = this.quest.getRequireSequentialTasks();
            for (Task task : this.quest.getTasks()) {
                TaskButton taskButton = new TaskButton(this.panelTasks, task);
                this.panelTasks.add(taskButton);
                taskButton.setSize(bsize, bsize);
                if (!canEdit && seq && !this.questScreen.file.selfTeamData.isCompleted(task)) {
                    break;
                }
            }
            if (!canEdit && this.panelTasks.getWidgets().isEmpty()) {
                ViewQuestPanel.DisabledButtonTextField noTasks = new ViewQuestPanel.DisabledButtonTextField(this.panelTasks, Component.translatable("ftbquests.gui.no_tasks"));
                noTasks.setSize(noTasks.width + 8, bsize);
                noTasks.setColor(ThemeProperties.DISABLED_TEXT_COLOR.get(this.quest));
                this.panelTasks.add(noTasks);
            }
            for (Reward reward : this.quest.getRewards()) {
                if (canEdit || !this.questScreen.file.selfTeamData.isRewardBlocked(reward) && reward.getAutoClaimType() != RewardAutoClaim.INVISIBLE) {
                    RewardButton b = new RewardButton(panelRewards, reward);
                    panelRewards.add(b);
                    b.setSize(bsize, bsize);
                }
            }
            if (!canEdit && panelRewards.getWidgets().isEmpty()) {
                ViewQuestPanel.DisabledButtonTextField noRewards = new ViewQuestPanel.DisabledButtonTextField(panelRewards, Component.translatable("ftbquests.gui.no_rewards"));
                noRewards.setSize(noRewards.width + 8, bsize);
                noRewards.setColor(ThemeProperties.DISABLED_TEXT_COLOR.get(this.quest));
                panelRewards.add(noRewards);
            }
            if (this.questScreen.file.canEdit()) {
                this.panelTasks.add(new AddTaskButton(this.panelTasks, this.quest));
                panelRewards.add(new AddRewardButton(panelRewards, this.quest));
            }
            int ww = 0;
            for (Widget widget : this.panelTasks.getWidgets()) {
                ww = Math.max(ww, widget.width);
            }
            for (Widget widget : panelRewards.getWidgets()) {
                ww = Math.max(ww, widget.width);
            }
            Color4I borderColor = ThemeProperties.WIDGET_BORDER.get(this.questScreen.selectedChapter);
            ww = Mth.clamp(ww, 70, 140);
            w = Math.max(w, ww * 2 + 10);
            if (ThemeProperties.FULL_SCREEN_QUEST.get(this.quest) == 1) {
                w = this.questScreen.width - 1;
            }
            if (w % 2 == 0) {
                w++;
            }
            this.setWidth(w);
            this.panelContent.setPosAndSize(0, Math.max(16, this.titleField.height + 8), w, 0);
            int iconSize = Math.min(16, this.titleField.height + 2);
            Button buttonClose;
            this.add(buttonClose = new ViewQuestPanel.CloseViewQuestButton());
            buttonClose.setPosAndSize(w - iconSize - 2, 4, iconSize, iconSize);
            Button buttonPin;
            this.add(buttonPin = new ViewQuestPanel.PinViewQuestButton());
            buttonPin.setPosAndSize(w - iconSize * 2 - 4, 4, iconSize, iconSize);
            if (this.questScreen.selectedChapter.id != this.quest.getChapter().id) {
                ViewQuestPanel.GotoLinkedQuestButton b = new ViewQuestPanel.GotoLinkedQuestButton();
                this.add(b);
                b.setPosAndSize(iconSize + 4, 0, iconSize, iconSize);
            }
            List<QuestLink> links = new ArrayList();
            this.questScreen.file.forAllChapters(chapter -> chapter.getQuestLinks().stream().filter(link -> chapter != this.questScreen.selectedChapter && link.linksTo(this.quest)).forEach(links::add));
            ViewQuestPanel.ViewQuestLinksButton linksButton = new ViewQuestPanel.ViewQuestLinksButton(links);
            this.add(linksButton);
            linksButton.setPosAndSize(w - iconSize * 3 - 4, 0, iconSize, iconSize);
            if (!this.quest.hasDependencies()) {
                this.add(this.buttonOpenDependencies = new SimpleButton(this, Component.translatable("ftbquests.gui.no_dependencies"), Icon.getIcon("ftbquests:textures/gui/arrow_left.png").withTint(borderColor), (widget, button) -> {
                }));
            } else {
                this.add(this.buttonOpenDependencies = new SimpleButton(this, Component.translatable("ftbquests.gui.view_dependencies"), Icon.getIcon("ftbquests:textures/gui/arrow_left.png").withTint(ThemeProperties.QUEST_VIEW_TITLE.get()), (widget, button) -> this.showList(this.quest.streamDependencies().toList(), true)));
            }
            Button buttonOpenDependants;
            if (this.quest.getDependants().isEmpty()) {
                this.add(buttonOpenDependants = new SimpleButton(this, Component.translatable("ftbquests.gui.no_dependants"), Icon.getIcon("ftbquests:textures/gui/arrow_right.png").withTint(borderColor), (widget, button) -> {
                }));
            } else {
                this.add(buttonOpenDependants = new SimpleButton(this, Component.translatable("ftbquests.gui.view_dependants"), Icon.getIcon("ftbquests:textures/gui/arrow_right.png").withTint(ThemeProperties.QUEST_VIEW_TITLE.get()), (widget, button) -> this.showList(this.quest.getDependants(), false)));
            }
            this.buttonOpenDependencies.setPosAndSize(0, this.panelContent.posY + 2, 13, 13);
            buttonOpenDependants.setPosAndSize(w - 13, this.panelContent.posY + 2, 13, 13);
            TextField textFieldTasks = new TextField(this.panelContent) {

                @Override
                public TextField resize(Theme theme) {
                    return this;
                }
            };
            int w2 = w / 2;
            textFieldTasks.setPosAndSize(2, 2, w2 - 3, 13);
            textFieldTasks.setMaxWidth(w);
            textFieldTasks.addFlags(36);
            textFieldTasks.setText(Component.translatable("ftbquests.tasks"));
            textFieldTasks.setColor(ThemeProperties.TASKS_TEXT_COLOR.get(this.quest));
            this.panelContent.add(textFieldTasks);
            TextField textFieldRewards = new TextField(this.panelContent) {

                @Override
                public TextField resize(Theme theme) {
                    return this;
                }
            };
            textFieldRewards.setPosAndSize(w2 + 2, 2, w2 - 3, 13);
            textFieldRewards.setMaxWidth(w);
            textFieldRewards.addFlags(36);
            textFieldRewards.setText(Component.translatable("ftbquests.rewards"));
            textFieldRewards.setColor(ThemeProperties.REWARDS_TEXT_COLOR.get(this.quest));
            this.panelContent.add(textFieldRewards);
            this.panelTasks.setPosAndSize(2, 16, w2 - 3, 0);
            panelRewards.setPosAndSize(w2 + 2, 16, w2 - 3, 0);
            int at = this.panelTasks.align(new CompactGridLayout(bsize + 2));
            int ar = panelRewards.align(new CompactGridLayout(bsize + 2));
            int h = Math.max(at, ar);
            this.panelTasks.setHeight(h);
            panelRewards.setHeight(h);
            int tox = (this.panelTasks.width - this.panelTasks.getContentWidth()) / 2;
            int rox = (panelRewards.width - panelRewards.getContentWidth()) / 2;
            int toy = (this.panelTasks.height - this.panelTasks.getContentHeight()) / 2;
            int roy = (panelRewards.height - panelRewards.getContentHeight()) / 2;
            for (Widget widget : this.panelTasks.getWidgets()) {
                widget.setX(widget.posX + tox);
                widget.setY(widget.posY + toy);
            }
            for (Widget widget : panelRewards.getWidgets()) {
                widget.setX(widget.posX + rox);
                widget.setY(widget.posY + roy);
            }
            this.panelText.setPosAndSize(3, 16 + h + 12, w - 6, 0);
            Component subtitle = this.quest.getSubtitle();
            if (subtitle.getContents() == ComponentContents.EMPTY && canEdit) {
                subtitle = Component.literal("[No Subtitle]");
            }
            if (!subtitle.equals(Component.empty())) {
                this.panelText.add(new ViewQuestPanel.QuestDescriptionField(this.panelText, canEdit, (b, clickedW) -> this.editSubtitle()).addFlags(4).setMinWidth(this.panelText.width).setMaxWidth(this.panelText.width).setSpacing(9).setText(Component.literal("").append(subtitle).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)));
            }
            boolean showText = !this.quest.getHideTextUntilComplete().get(false) || this.questScreen.file.selfTeamData != null && this.questScreen.file.selfTeamData.isCompleted(this.quest);
            this.buildPageIndices();
            if (showText) {
                if (!this.pageIndices.isEmpty()) {
                    this.addDescriptionText(canEdit, subtitle);
                }
                if (!this.quest.getGuidePage().isEmpty()) {
                    if (subtitle.getContents() != ComponentContents.EMPTY) {
                        this.panelText.add(new VerticalSpaceWidget(this.panelText, 7));
                    }
                    this.panelText.add(new ViewQuestPanel.OpenInGuideButton(this.panelText, this.quest));
                }
            }
            if (this.pageIndices.size() > 1 || canEdit) {
                this.addButtonBar(canEdit);
            }
            if (this.panelText.getWidgets().isEmpty()) {
                this.panelContent.add(new ColorWidget(this.panelContent, borderColor, null).setPosAndSize(w2, 0, 1, h + 40));
                this.panelText.setHeight(0);
                this.setHeight(Math.min(this.panelContent.getContentHeight(), this.parent.height - 10));
            } else {
                this.panelContent.add(new ColorWidget(this.panelContent, borderColor, null).setPosAndSize(w2, 0, 1, 16 + h + 6));
                this.panelContent.add(new ColorWidget(this.panelContent, borderColor, null).setPosAndSize(1, 16 + h + 6, w - 2, 1));
                this.panelText.setHeight(this.panelText.align(new WidgetLayout.Vertical(0, 1, 2)));
                this.setHeight(Math.min(this.panelContent.getContentHeight() + this.titleField.height + 12, this.parent.height - 10));
            }
            if (ThemeProperties.FULL_SCREEN_QUEST.get(this.quest) == 1) {
                this.height = this.questScreen.height;
            }
            this.setPos((this.parent.width - this.width) / 2, (this.parent.height - this.height) / 2);
            this.panelContent.setHeight(this.height - 17);
            QuestTheme.currentObject = prev;
        }
    }

    private void addDescriptionText(boolean canEdit, Component subtitle) {
        Pair<Integer, Integer> pageSpan = (Pair<Integer, Integer>) this.pageIndices.get(this.getCurrentPage());
        if (subtitle.getContents() != ComponentContents.EMPTY) {
            this.panelText.add(new VerticalSpaceWidget(this.panelText, 7));
        }
        for (int i = (Integer) pageSpan.getFirst(); i <= pageSpan.getSecond() && i < this.quest.getDescription().size(); i++) {
            Component component = (Component) this.quest.getDescription().get(i);
            ImageComponent img = this.findImageComponent(component);
            if (img != null) {
                ImageComponentWidget cw = new ImageComponentWidget(this, this.panelText, img, i);
                if (cw.getComponent().fit) {
                    double scale = (double) this.panelText.width / (double) cw.width;
                    cw.setSize((int) ((double) cw.width * scale), (int) ((double) cw.height * scale));
                } else if (cw.getComponent().align == 1) {
                    cw.setX((this.panelText.width - cw.width) / 2);
                } else if (cw.getComponent().align == 2) {
                    cw.setX(this.panelText.width - cw.width);
                } else {
                    cw.setX(0);
                }
                this.panelText.add(cw);
            } else {
                int line = i;
                TextField field = new ViewQuestPanel.QuestDescriptionField(this.panelText, canEdit, (context, clickedW) -> this.editDescLine(clickedW, line, context, null)).setMaxWidth(this.panelText.width).setSpacing(9).setText(component);
                field.setWidth(this.panelText.width);
                this.panelText.add(field);
            }
        }
    }

    private void addButtonBar(boolean canEdit) {
        this.panelText.add(new VerticalSpaceWidget(this.panelText, 3));
        Panel buttonPanel = new BlankPanel(this.panelText);
        buttonPanel.setSize(this.panelText.width, 15);
        this.panelText.add(buttonPanel);
        int currentPage = this.getCurrentPage();
        Component page = Component.literal(currentPage + 1 + "/" + this.pageIndices.size()).withStyle(ChatFormatting.GRAY);
        int labelWidth = this.questScreen.getTheme().getStringWidth(page);
        if (currentPage > 0) {
            buttonPanel.add(this.makePrevPageButton(buttonPanel, currentPage, labelWidth));
        }
        if (this.pageIndices.size() > 1) {
            TextField pageLabel = new TextField(buttonPanel);
            pageLabel.setText(page);
            pageLabel.setPosAndSize(this.panelText.width - 24 - labelWidth, 3, 20, 14);
            buttonPanel.add(pageLabel);
        }
        if (currentPage < this.pageIndices.size() - 1) {
            buttonPanel.add(this.makeNextPageButton(buttonPanel, currentPage));
        }
        if (canEdit) {
            SimpleTextButton edit = new SimpleTextButton(buttonPanel, Component.translatable("ftbquests.gui.edit").append(" ▼"), ThemeProperties.EDIT_ICON.get()) {

                @Override
                public void onClicked(MouseButton mouseButton) {
                    ViewQuestPanel.this.openEditButtonContextMenu();
                }
            };
            edit.setX((this.panelText.width - edit.width) / 2);
            edit.setHeight(14);
            buttonPanel.add(edit);
        }
    }

    @NotNull
    private SimpleTextButton makeNextPageButton(Panel buttonPanel, int currentPage) {
        SimpleTextButton nextPage = new SimpleTextButton(buttonPanel, Component.empty(), ThemeProperties.RIGHT_ARROW.get()) {

            @Override
            public void onClicked(MouseButton mouseButton) {
                ViewQuestPanel.this.setCurrentPage(Math.min(ViewQuestPanel.this.pageIndices.size() + 1, currentPage + 1));
                ViewQuestPanel.this.refreshWidgets();
            }

            @Override
            public void addMouseOverText(TooltipList list) {
                list.add(Component.literal("[Page Down]").withStyle(ChatFormatting.DARK_GRAY));
                list.add(Component.literal("[Mousewheel Down]").withStyle(ChatFormatting.DARK_GRAY));
            }
        };
        nextPage.setPosAndSize(this.panelText.width - 21, nextPage.getPosY(), 16, 14);
        return nextPage;
    }

    @NotNull
    private SimpleTextButton makePrevPageButton(Panel buttonPanel, int currentPage, int labelWidth) {
        SimpleTextButton prevPage = new SimpleTextButton(buttonPanel, Component.empty(), ThemeProperties.LEFT_ARROW.get()) {

            @Override
            public void onClicked(MouseButton mouseButton) {
                ViewQuestPanel.this.setCurrentPage(Math.max(0, currentPage - 1));
                ViewQuestPanel.this.refreshWidgets();
            }

            @Override
            public void addMouseOverText(TooltipList list) {
                list.add(Component.literal("[Page Up]").withStyle(ChatFormatting.DARK_GRAY));
                list.add(Component.literal("[Mousewheel Up]").withStyle(ChatFormatting.DARK_GRAY));
            }
        };
        prevPage.setPosAndSize(this.panelText.width - 43 - labelWidth, prevPage.getPosY(), 16, 14);
        return prevPage;
    }

    private ImageComponent findImageComponent(Component c) {
        Iterator var2 = c.getSiblings().iterator();
        if (var2.hasNext()) {
            Component c1 = (Component) var2.next();
            ComponentContents var5 = c1.getContents();
            return var5 instanceof ImageComponent ? (ImageComponent) var5 : this.findImageComponent(c1);
        } else {
            return null;
        }
    }

    @Override
    public void alignWidgets() {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.quest != null && this.quest.hasDependencies() && !this.questScreen.file.selfTeamData.canStartTasks(this.quest) && this.buttonOpenDependencies != null) {
            float red = Mth.sin((float) (System.currentTimeMillis() % 1200L) * 0.0026179939F);
            Color4I col = Color4I.rgb((int) (red * 127.0F + 63.0F), 0, 0);
            this.buttonOpenDependencies.setIcon(Icon.getIcon("ftbquests:textures/gui/arrow_left.png").withTint(col));
        }
    }

    private void showList(Collection<QuestObject> c, boolean dependencies) {
        int hidden = 0;
        List<ContextMenuItem> contextMenu = new ArrayList();
        if (dependencies && this.quest.getMinRequiredDependencies() > 0) {
            contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.quest.min_required_header", this.quest.getMinRequiredDependencies()).withStyle(ChatFormatting.UNDERLINE), Color4I.empty(), null).setEnabled(false));
        }
        for (QuestObject object : c) {
            if (!this.questScreen.file.canEdit() && !object.isVisible(this.questScreen.file.selfTeamData)) {
                hidden++;
            } else {
                MutableComponent title = object.getMutableTitle();
                if (object.getQuestChapter() != null && object.getQuestChapter() != this.quest.getQuestChapter()) {
                    Component suffix = Component.literal(" [").append(object.getQuestChapter().getTitle()).append("]").withStyle(ChatFormatting.GRAY);
                    title.append(suffix);
                }
                contextMenu.add(new ContextMenuItem(title, Color4I.empty(), button -> this.questScreen.open(object, true)));
            }
        }
        if (hidden > 0) {
            MutableComponent prefix = hidden == c.size() ? Component.empty() : Component.literal("+ ");
            contextMenu.add(new ContextMenuItem(prefix.append(Component.translatable("ftbquests.quest.hidden_quests_footer", hidden)), Color4I.empty(), null).setEnabled(false));
        }
        this.getGui().openContextMenu(contextMenu);
    }

    @Override
    public void keyReleased(Key key) {
        if (this.quest != null) {
            if (this.questScreen.file.canEdit()) {
                if (key.is(83)) {
                    this.editSubtitle();
                } else if (key.is(84)) {
                    this.editTitle();
                } else if (key.is(68)) {
                    this.editDescription();
                } else if (key.is(80)) {
                    this.addPageBreak();
                } else if (key.is(76)) {
                    this.editDescLine0(this, -1, null);
                } else if (key.is(73)) {
                    this.editDescLine0(this, -1, new ImageComponent());
                } else if (key.is(81)) {
                    this.quest.onEditButtonClicked(this.questScreen);
                }
            }
            if (key.is(266) || key.is(263)) {
                this.setCurrentPage(Math.max(0, this.getCurrentPage() - 1));
                this.refreshWidgets();
            } else if (key.is(267) || key.is(262)) {
                this.setCurrentPage(Math.min(this.pageIndices.size() - 1, this.getCurrentPage() + 1));
                this.refreshWidgets();
            }
        }
    }

    private void editTitle() {
        StringConfig c = new StringConfig(null);
        QuestObject qo = this.quest;
        String titleKey = "ftbquests.title";
        for (Widget w : this.panelTasks.getWidgets()) {
            if (w instanceof TaskButton b && b.isMouseOver()) {
                qo = b.task;
                titleKey = "ftbquests.task_title";
                break;
            }
        }
        QuestObject qo1 = qo;
        c.setValue(qo1.getRawTitle());
        EditStringConfigOverlay<String> overlay = new EditStringConfigOverlay<>(this.getGui(), c, accepted -> {
            if (accepted) {
                qo1.setRawTitle(c.getValue());
                new EditObjectMessage(qo1).sendToServer();
            }
        }, Component.translatable("ftbquests.title.tooltip")).atPosition(this.titleField.getX(), this.titleField.getY() - 14);
        overlay.setWidth(Math.max(150, overlay.getWidth()));
        this.getGui().pushModalPanel(overlay);
    }

    private void editSubtitle() {
        StringConfig c = new StringConfig(null);
        c.setValue(this.quest.getRawSubtitle());
        EditStringConfigOverlay<String> overlay = new EditStringConfigOverlay<>(this.getGui(), c, accepted -> {
            if (accepted) {
                this.quest.setRawSubtitle(c.getValue());
                new EditObjectMessage(this.quest).sendToServer();
            }
        }, Component.translatable("ftbquests.chapter.subtitle"));
        overlay.setWidth(Mth.clamp(overlay.getWidth(), 150, this.getScreen().getGuiScaledWidth() - 20));
        overlay.setPos(this.panelText.getX() + (this.panelText.width - overlay.width) / 2, this.panelText.getY() - 14);
        this.getGui().pushModalPanel(overlay);
    }

    private void editDescription() {
        ListConfig<String, StringConfig> lc = new ListConfig<>(new StringConfig());
        lc.setValue(this.quest.getRawDescription());
        new MultilineTextEditorScreen(Component.translatable("ftbquests.gui.edit_description"), lc, accepted -> {
            if (accepted) {
                new EditObjectMessage(this.quest).sendToServer();
                this.refreshWidgets();
            }
            this.openGui();
        }).openGui();
    }

    private void openEditButtonContextMenu() {
        List<ContextMenuItem> contextMenu = new ArrayList();
        contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.title").append(hotkey("T")), Icons.NOTES, b -> this.editTitle()));
        contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.quest.subtitle").append(hotkey("S")), Icons.NOTES, b -> this.editSubtitle()));
        contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.quest.description").append(hotkey("D")), Icons.NOTES, b -> this.editDescription()));
        contextMenu.add(ContextMenuItem.SEPARATOR);
        contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.line").append(hotkey("L")), Icons.NOTES, b -> this.editDescLine0(this, -1, null)));
        contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.page_break").append(hotkey("P")), PAGEBREAK_ICON, b -> this.addPageBreak()));
        contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.image").append(hotkey("I")), Icons.ART, b -> this.editDescLine0(this, -1, new ImageComponent())));
        contextMenu.add(ContextMenuItem.SEPARATOR);
        contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.edit_quest_props").append(hotkey("Q")), Icons.SETTINGS, b -> this.quest.onEditButtonClicked(this.questScreen)));
        this.getGui().openContextMenu(contextMenu);
    }

    private static Component hotkey(String key) {
        return Component.literal(" [" + key + "]").withStyle(ChatFormatting.DARK_GRAY);
    }

    private void addPageBreak() {
        this.appendToPage(this.quest.getRawDescription(), List.of("{@pagebreak}", "(new page placeholder text)"), this.getCurrentPage());
        new EditObjectMessage(this.quest).sendToServer();
        this.setCurrentPage(Math.min(this.pageIndices.size() - 1, this.getCurrentPage() + 1));
        this.refreshWidgets();
    }

    private void editDescLine0(Widget clickedWidget, int line, @Nullable Object type) {
        if (type instanceof ImageComponent img) {
            this.editImage(line, img);
        } else {
            List<String> rawDesc = this.quest.getRawDescription();
            StringConfig c = new StringConfig(null);
            c.setValue(line == -1 ? "" : (String) rawDesc.get(line));
            EditStringConfigOverlay<String> overlay = new EditStringConfigOverlay<>(this.getGui(), c, accepted -> {
                if (accepted) {
                    if (line == -1) {
                        this.appendToPage(rawDesc, List.of(c.getValue()), this.getCurrentPage());
                    } else {
                        rawDesc.set(line, c.getValue());
                    }
                    new EditObjectMessage(this.quest).sendToServer();
                    this.refreshWidgets();
                }
            }).atPosition(clickedWidget.getX(), clickedWidget.getY());
            overlay.setWidth(Mth.clamp(overlay.getWidth(), 150, this.getScreen().getGuiScaledWidth() - clickedWidget.getX() - 20));
            this.getGui().pushModalPanel(overlay);
        }
    }

    private void editImage(int line, ImageComponent component) {
        ConfigGroup group = new ConfigGroup("ftbquests.chapter.image", accepted -> {
            this.openGui();
            if (accepted) {
                if (line == -1) {
                    this.appendToPage(this.quest.getRawDescription(), List.of(component.toString()), this.getCurrentPage());
                } else {
                    this.quest.getRawDescription().set(line, component.toString());
                }
                new EditObjectMessage(this.quest).sendToServer();
                this.refreshWidgets();
            }
        });
        group.add("image", new ImageResourceConfig(), ImageResourceConfig.getResourceLocation(component.image), v -> component.image = Icon.getIcon(v), ImageResourceConfig.NONE);
        group.addInt("width", component.width, v -> component.width = v, 0, 1, 1000);
        group.addInt("height", component.height, v -> component.height = v, 0, 1, 1000);
        group.addInt("align", component.align, v -> component.align = v, 0, 0, 2);
        group.addBool("fit", component.fit, v -> component.fit = v, false);
        new EditConfigScreen(group).openGui();
    }

    private void appendToPage(List<String> list, List<String> toAdd, int pageNumber) {
        if (this.pageIndices.isEmpty()) {
            list.addAll(toAdd);
            this.buildPageIndices();
        } else {
            int idx = (Integer) ((Pair) this.pageIndices.get(pageNumber)).getSecond() + 1;
            for (String line : toAdd) {
                list.add(idx, line);
                idx++;
            }
        }
    }

    public void editDescLine(Widget clickedWidget, int line, boolean context, @Nullable Object type) {
        if (context) {
            List<ContextMenuItem> contextMenu = new ArrayList();
            contextMenu.add(new ContextMenuItem(Component.translatable("selectServer.edit"), ThemeProperties.EDIT_ICON.get(), b -> this.editDescLine0(clickedWidget, line, type)));
            contextMenu.add(new ContextMenuItem(Component.translatable("selectServer.delete"), ThemeProperties.DELETE_ICON.get(), b -> {
                this.quest.getRawDescription().remove(line);
                new EditObjectMessage(this.quest).sendToServer();
                this.refreshWidgets();
            }));
            this.getGui().openContextMenu(contextMenu);
        } else {
            this.editDescLine0(clickedWidget, line, type);
        }
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        if (this.quest != null) {
            QuestObjectBase prev = QuestTheme.currentObject;
            QuestTheme.currentObject = this.quest;
            super.draw(graphics, theme, x, y, w, h);
            QuestTheme.currentObject = prev;
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        ThemeProperties.QUEST_VIEW_BACKGROUND.get().draw(graphics, x, y, w, h);
        if (this.titleField != null && this.panelContent != null) {
            int iconSize = Math.min(16, this.titleField.height + 2);
            this.icon.draw(graphics, x + 4, y + 4, iconSize, iconSize);
            ThemeProperties.QUEST_VIEW_BORDER.get().draw(graphics, x + 1, this.panelContent.getY(), w - 2, 1);
        }
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        return super.mousePressed(button) || this.isMouseOver();
    }

    @Override
    public boolean mouseScrolled(double scroll) {
        long now = System.currentTimeMillis();
        if (super.mouseScrolled(scroll)) {
            this.lastScrollTime = now;
            return true;
        } else {
            if (now - this.lastScrollTime > 500L) {
                if (scroll < 0.0 && this.getCurrentPage() < this.pageIndices.size() - 1) {
                    this.setCurrentPage(this.getCurrentPage() + 1);
                    this.refreshWidgets();
                    this.lastScrollTime = now;
                    return true;
                }
                if (scroll > 0.0 && this.getCurrentPage() > 0) {
                    this.setCurrentPage(this.getCurrentPage() - 1);
                    this.refreshWidgets();
                    this.lastScrollTime = now;
                    return true;
                }
            }
            return false;
        }
    }

    private abstract class AbstractPanelButton extends SimpleTextButton {

        public AbstractPanelButton(Component txt, Icon icon) {
            super(ViewQuestPanel.this, txt, icon);
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            this.drawIcon(graphics, theme, x + 1, y + 1, w - 2, h - 2);
        }
    }

    private class CloseViewQuestButton extends ViewQuestPanel.AbstractPanelButton {

        private CloseViewQuestButton() {
            super(Component.translatable("gui.close"), ThemeProperties.CLOSE_ICON.get(ViewQuestPanel.this.quest));
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            ViewQuestPanel.this.questScreen.closeQuest();
        }
    }

    public static class DisabledButtonTextField extends TextField {

        public DisabledButtonTextField(Panel panel, Component text) {
            super(panel);
            this.addFlags(36);
            this.setText(text);
        }
    }

    private class GotoLinkedQuestButton extends ViewQuestPanel.AbstractPanelButton {

        public GotoLinkedQuestButton() {
            super(Component.translatable("ftbquests.gui.goto_linked_quest", ViewQuestPanel.this.quest.getChapter().getMutableTitle().withStyle(ChatFormatting.YELLOW)), ThemeProperties.LINK_ICON.get());
        }

        @Override
        public void onClicked(MouseButton button) {
            double qx = ViewQuestPanel.this.quest.getX() + 0.5;
            double qy = ViewQuestPanel.this.quest.getY() + 0.5;
            ViewQuestPanel.this.questScreen.selectChapter(ViewQuestPanel.this.quest.getChapter());
            ViewQuestPanel.this.questScreen.questPanel.scrollTo(qx, qy);
        }
    }

    public static class OpenInGuideButton extends SimpleTextButton {

        private final Quest quest;

        public OpenInGuideButton(Panel panel, Quest q) {
            super(panel, Component.translatable("ftbquests.gui.open_in_guide"), ItemIcon.getItemIcon(Items.BOOK));
            this.setHeight(13);
            this.setX((panel.width - this.width) / 2);
            this.quest = q;
        }

        @Override
        public void onClicked(MouseButton button) {
            this.handleClick("guide", this.quest.getGuidePage());
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        }
    }

    private class PinViewQuestButton extends ViewQuestPanel.AbstractPanelButton {

        private PinViewQuestButton() {
            super(Component.translatable(ClientQuestFile.isQuestPinned(ViewQuestPanel.this.quest.id) ? "ftbquests.gui.unpin" : "ftbquests.gui.pin"), ClientQuestFile.isQuestPinned(ViewQuestPanel.this.quest.id) ? ThemeProperties.PIN_ICON_ON.get() : ThemeProperties.PIN_ICON_OFF.get());
        }

        @Override
        public void onClicked(MouseButton button) {
            this.playClickSound();
            new TogglePinnedMessage(ViewQuestPanel.this.quest.id).sendToServer();
        }
    }

    private class QuestDescriptionField extends TextField {

        private final boolean canEdit;

        private final BiConsumer<Boolean, Widget> editCallback;

        QuestDescriptionField(Panel panel, boolean canEdit, BiConsumer<Boolean, Widget> editCallback) {
            super(panel);
            this.canEdit = canEdit;
            this.editCallback = editCallback;
        }

        @Override
        public boolean mousePressed(MouseButton button) {
            if (this.isMouseOver()) {
                if (this.canEdit && button.isRight()) {
                    this.editCallback.accept(true, this);
                    return true;
                }
                if (button.isLeft() && Minecraft.getInstance().screen != null) {
                    Optional<Style> style = this.getComponentStyleAt(ViewQuestPanel.this.questScreen.getTheme(), this.getMouseX(), this.getMouseY());
                    if (style.isPresent()) {
                        return this.handleCustomClickEvent((Style) style.get()) || Minecraft.getInstance().screen.handleComponentClicked((Style) style.get());
                    }
                }
            }
            return super.mousePressed(button);
        }

        private boolean handleCustomClickEvent(Style style) {
            if (style == null) {
                return false;
            } else {
                ClickEvent clickEvent = style.getClickEvent();
                if (clickEvent == null) {
                    return false;
                } else if (clickEvent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
                    QuestObjectBase.parseHexId(clickEvent.getValue()).ifPresentOrElse(questId -> {
                        QuestObject qo = ViewQuestPanel.this.quest.getQuestFile().get(questId);
                        if (qo != null) {
                            ViewQuestPanel.this.questScreen.open(qo, false);
                        } else {
                            this.errorToPlayer("Unknown quest object id: %s", clickEvent.getValue());
                        }
                    }, () -> this.errorToPlayer("Invalid quest object id: %s", clickEvent.getValue()));
                    return true;
                } else if (clickEvent.getAction() == ClickEvent.Action.OPEN_URL) {
                    try {
                        URI uri = new URI(clickEvent.getValue());
                        String scheme = uri.getScheme();
                        if (scheme == null) {
                            throw new URISyntaxException(clickEvent.getValue(), "Missing protocol");
                        } else if (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https")) {
                            throw new URISyntaxException(clickEvent.getValue(), "Unsupported protocol: " + scheme.toLowerCase(Locale.ROOT));
                        } else {
                            Screen curScreen = Minecraft.getInstance().screen;
                            Minecraft.getInstance().setScreen(new ConfirmLinkScreen(accepted -> {
                                if (accepted) {
                                    Util.getPlatform().openUri(uri);
                                }
                                Minecraft.getInstance().setScreen(curScreen);
                            }, clickEvent.getValue(), false));
                            return true;
                        }
                    } catch (URISyntaxException var6) {
                        this.errorToPlayer("Can't open url for %s (%s)", clickEvent.getValue(), var6.getMessage());
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        private void errorToPlayer(String msg, Object... args) {
            QuestScreen.displayError(Component.literal(String.format(msg, args)).withStyle(ChatFormatting.RED));
        }

        @Override
        public boolean mouseDoubleClicked(MouseButton button) {
            if (this.isMouseOver() && this.canEdit) {
                this.editCallback.accept(false, this);
                return true;
            } else {
                return false;
            }
        }

        @Nullable
        @Override
        public CursorType getCursor() {
            return this.canEdit ? CursorType.IBEAM : null;
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            if (this.isMouseOver()) {
                super.addMouseOverText(list);
                this.getComponentStyleAt(ViewQuestPanel.this.questScreen.getTheme(), this.getMouseX(), this.getMouseY()).ifPresent(style -> {
                    if (style.getHoverEvent() != null) {
                        HoverEvent hoverevent = style.getHoverEvent();
                        HoverEvent.ItemStackInfo stackInfo = hoverevent.getValue(HoverEvent.Action.SHOW_ITEM);
                        Minecraft mc = Minecraft.getInstance();
                        TooltipFlag flag = mc.options.advancedItemTooltips ? TooltipFlag.Default.f_256730_ : TooltipFlag.Default.f_256752_;
                        if (stackInfo != null) {
                            stackInfo.getItemStack().getTooltipLines(mc.player, flag).forEach(list::add);
                        } else {
                            HoverEvent.EntityTooltipInfo entityInfo = hoverevent.getValue(HoverEvent.Action.SHOW_ENTITY);
                            if (entityInfo != null) {
                                if (flag.isAdvanced()) {
                                    entityInfo.getTooltipLines().forEach(list::add);
                                }
                            } else {
                                Component component = hoverevent.getValue(HoverEvent.Action.SHOW_TEXT);
                                if (component != null) {
                                    list.add(component);
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    private class ViewQuestLinksButton extends ViewQuestPanel.AbstractPanelButton {

        private final List<QuestLink> links;

        public ViewQuestLinksButton(Collection<QuestLink> links) {
            super(Component.translatable("ftbquests.gui.view_quest_links"), ThemeProperties.LINK_ICON.get());
            this.links = List.copyOf(links);
        }

        @Override
        public void onClicked(MouseButton button) {
            List<ContextMenuItem> items = new ArrayList();
            for (QuestLink link : this.links) {
                link.getQuest().ifPresent(quest -> {
                    Component title = quest.getTitle().copy().append(": ").append(link.getChapter().getTitle().copy().withStyle(ChatFormatting.YELLOW));
                    items.add(new ContextMenuItem(title, quest.getIcon(), b -> this.gotoLink(link)));
                });
            }
            if (!items.isEmpty()) {
                ViewQuestPanel.this.questScreen.openContextMenu(items);
            }
        }

        private void gotoLink(QuestLink link) {
            ViewQuestPanel.this.questScreen.closeQuest();
            ViewQuestPanel.this.questScreen.selectChapter(link.getChapter());
            ViewQuestPanel.this.questScreen.questPanel.scrollTo(link.getX() + 0.5, link.getX() + 0.5);
        }

        @Override
        public boolean isEnabled() {
            return !this.links.isEmpty();
        }

        @Override
        public boolean shouldDraw() {
            return !this.links.isEmpty();
        }
    }
}