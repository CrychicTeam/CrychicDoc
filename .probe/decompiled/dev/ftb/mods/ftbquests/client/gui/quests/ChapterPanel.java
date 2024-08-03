package dev.ftb.mods.ftbquests.client.gui.quests;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.config.ui.EditStringConfigOverlay;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.PositionedIngredient;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.ChangeChapterGroupScreen;
import dev.ftb.mods.ftbquests.client.gui.ContextMenuBuilder;
import dev.ftb.mods.ftbquests.net.CreateObjectMessage;
import dev.ftb.mods.ftbquests.net.MoveChapterGroupMessage;
import dev.ftb.mods.ftbquests.net.MoveChapterMessage;
import dev.ftb.mods.ftbquests.net.ToggleChapterPinnedMessage;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.ChapterGroup;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import dev.ftb.mods.ftbquests.util.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class ChapterPanel extends Panel {

    public static final Icon ARROW_COLLAPSED = Icon.getIcon("ftbquests:textures/gui/arrow_collapsed.png");

    public static final Icon ARROW_EXPANDED = Icon.getIcon("ftbquests:textures/gui/arrow_expanded.png");

    public static final int Z_LEVEL = 300;

    private final QuestScreen questScreen;

    boolean expanded = this.isPinned();

    public ChapterPanel(Panel panel) {
        super(panel);
        this.questScreen = (QuestScreen) panel.getGui();
    }

    @Override
    public boolean checkMouseOver(int mouseX, int mouseY) {
        return this.questScreen.isViewingQuest() ? false : super.checkMouseOver(mouseX, mouseY);
    }

    @Override
    public void addWidgets() {
        this.add(new ChapterPanel.ModpackButton(this, this.questScreen.file));
        boolean canEdit = this.questScreen.file.canEdit();
        for (Chapter chapter : this.questScreen.file.getDefaultChapterGroup().getVisibleChapters(this.questScreen.file.selfTeamData)) {
            this.add(new ChapterPanel.ChapterButton(this, chapter));
        }
        if (canEdit) {
        }
        this.questScreen.file.forAllChapterGroups(group -> {
            if (!group.isDefaultGroup()) {
                ChapterPanel.ChapterGroupButton button = new ChapterPanel.ChapterGroupButton(this, group);
                if (canEdit || !button.visibleChapters.isEmpty()) {
                    this.add(button);
                    if (!group.isGuiCollapsed()) {
                        button.visibleChapters.forEach(chapterx -> this.add(new ChapterPanel.ChapterButton(this, chapterx)));
                    }
                }
            }
        });
    }

    @Override
    public void alignWidgets() {
        int wd = 100;
        for (Widget w : this.widgets) {
            wd = Math.min(Math.max(wd, ((ChapterPanel.ListButton) w).getActualWidth(this.questScreen)), 800);
        }
        this.setPosAndSize((this.expanded || this.isPinned()) && !this.questScreen.isViewingQuest() ? 0 : -wd, 0, wd, this.questScreen.height);
        for (Widget w : this.widgets) {
            w.setWidth(wd);
        }
        this.align(WidgetLayout.VERTICAL);
        if (this.getContentHeight() <= this.height) {
            this.setScrollY(0.0);
        }
    }

    @Override
    public void updateMouseOver(int mouseX, int mouseY) {
        super.updateMouseOver(mouseX, mouseY);
        if (this.expanded && !this.isPinned() && !this.isMouseOver()) {
            this.setExpanded(false);
        }
    }

    @Override
    public int getX() {
        return (this.expanded || this.isPinned()) && !this.questScreen.isViewingQuest() ? 0 : -this.width;
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawContextMenuBackground(graphics, x, y, w, h);
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 300.0F);
        RenderSystem.enableDepthTest();
        super.draw(graphics, theme, x, y, w, h);
        graphics.pose().popPose();
    }

    public void setExpanded(boolean b) {
        this.expanded = b;
    }

    boolean isPinned() {
        return ClientQuestFile.INSTANCE.selfTeamData.isChapterPinned(Minecraft.getInstance().player);
    }

    public static class ChapterButton extends ChapterPanel.ListButton {

        private final Chapter chapter;

        private final List<? extends Component> description;

        public ChapterButton(ChapterPanel panel, Chapter c) {
            super(panel, c.getTitle(), c.getIcon());
            this.chapter = c;
            this.description = this.chapter.getRawSubtitle().stream().map(line -> TextUtils.parseRawText(line).copy().withStyle(ChatFormatting.GRAY)).toList();
        }

        @Override
        public void onClicked(MouseButton button) {
            if (this.chapterPanel.questScreen.file.canEdit() || this.chapter.hasAnyVisibleChildren()) {
                this.playClickSound();
                if (this.chapterPanel.questScreen.selectedChapter != this.chapter) {
                    this.chapterPanel.questScreen.open(this.chapter, false);
                    this.chapter.getAutofocus().ifPresent(this.chapterPanel.questScreen::scrollTo);
                }
            }
            if (this.chapterPanel.questScreen.file.canEdit() && button.isRight()) {
                ContextMenuBuilder.create(this.chapter, this.chapterPanel.questScreen).insertAtTop(List.of(new ContextMenuItem(Component.translatable("gui.move"), ThemeProperties.MOVE_UP_ICON.get(), b -> new MoveChapterMessage(this.chapter.id, true).sendToServer()).setEnabled(this.chapter.getIndex() > 0).setCloseMenu(false), new ContextMenuItem(Component.translatable("gui.move"), ThemeProperties.MOVE_DOWN_ICON.get(), b -> new MoveChapterMessage(this.chapter.id, false).sendToServer()).setEnabled(this.chapter.getIndex() < this.chapter.getGroup().getChapters().size() - 1).setCloseMenu(false), new ContextMenuItem(Component.translatable("ftbquests.gui.change_group"), Icons.COLOR_RGB, b -> new ChangeChapterGroupScreen(this.chapter, this.chapterPanel.questScreen).openGui()))).openContextMenu(this.chapterPanel.questScreen);
            }
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            GuiHelper.setupDrawing();
            if (this.isMouseOver()) {
                Color4I.WHITE.withAlpha(40).draw(graphics, x + 1, y, w - 2, h);
            }
            Color4I c = this.chapter.getProgressColor(this.chapterPanel.questScreen.file.selfTeamData, !this.isMouseOver());
            int xOff = this.chapter.getGroup().isDefaultGroup() ? 0 : 7;
            this.icon.draw(graphics, x + 2 + xOff, y + 1, 12, 12);
            MutableComponent text = Component.literal("").append(this.title).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(c.rgb())));
            if (this.chapterPanel.questScreen.selectedChapter != null && this.chapter.id == this.chapterPanel.questScreen.selectedChapter.id) {
                text.append(Component.literal(" ◀").withStyle(ChatFormatting.GRAY));
            }
            theme.drawString(graphics, text, x + 16 + xOff, y + 3);
            GuiHelper.setupDrawing();
            if (!this.chapter.hasAnyVisibleChildren()) {
                ThemeProperties.CLOSE_ICON.get().draw(graphics, x + w - 12, y + 3, 8, 8);
            } else if (this.chapterPanel.questScreen.file.selfTeamData.hasUnclaimedRewards(Minecraft.getInstance().player.m_20148_(), this.chapter)) {
                ThemeProperties.ALERT_ICON.get().draw(graphics, x + w - 12, y + 3, 8, 8);
            }
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            this.chapterPanel.questScreen.addInfoTooltip(list, this.chapter);
            for (Component s : this.description) {
                list.add(s);
            }
        }

        @Override
        public int getActualWidth(QuestScreen screen) {
            int extra = this.chapter.getGroup().isDefaultGroup() ? 0 : 7;
            if (!this.chapter.hasAnyVisibleChildren() || this.chapterPanel.questScreen.file.selfTeamData.hasUnclaimedRewards(Minecraft.getInstance().player.m_20148_(), this.chapter)) {
                extra += 16;
            }
            return screen.getTheme().getStringWidth(this.title) + 20 + extra;
        }
    }

    public static class ChapterGroupButton extends ChapterPanel.ListButton {

        public final ChapterGroup group;

        public final List<Chapter> visibleChapters;

        public ChapterGroupButton(ChapterPanel panel, ChapterGroup g) {
            super(panel, g.getTitle(), g.getIcon());
            this.setSize(100, 18);
            this.group = g;
            this.visibleChapters = g.getVisibleChapters(panel.questScreen.file.selfTeamData);
        }

        @Override
        public void onClicked(MouseButton button) {
            if (this.chapterPanel.questScreen.file.canEdit() && this.getMouseX() > this.getX() + this.width - 15) {
                this.playClickSound();
                StringConfig c = new StringConfig(Pattern.compile("^.+$"));
                EditStringConfigOverlay<String> overlay = new EditStringConfigOverlay<>(this.parent, c, accepted -> {
                    this.chapterPanel.questScreen.openGui();
                    if (accepted && !c.getValue().isEmpty()) {
                        Chapter chapter = new Chapter(0L, this.chapterPanel.questScreen.file, this.chapterPanel.questScreen.file.getDefaultChapterGroup());
                        chapter.setRawTitle(c.getValue());
                        CompoundTag extra = new CompoundTag();
                        extra.putLong("group", this.group.id);
                        new CreateObjectMessage(chapter, extra).sendToServer();
                    }
                    this.run();
                }, Component.translatable("ftbquests.chapter")).atMousePosition();
                overlay.setWidth(150);
                overlay.setExtraZlevel(310);
                this.getGui().pushModalPanel(overlay);
            } else if (this.chapterPanel.questScreen.file.canEdit() && button.isRight() && !this.group.isDefaultGroup()) {
                ContextMenuBuilder.create(this.group, this.chapterPanel.questScreen).insertAtTop(List.of(new ContextMenuItem(Component.translatable("gui.move"), ThemeProperties.MOVE_UP_ICON.get(), b -> new MoveChapterGroupMessage(this.group.id, true).sendToServer()).setEnabled(!this.group.isFirstGroup()).setCloseMenu(false), new ContextMenuItem(Component.translatable("gui.move"), ThemeProperties.MOVE_DOWN_ICON.get(), b -> new MoveChapterGroupMessage(this.group.id, false).sendToServer()).setEnabled(!this.group.isLastGroup()).setCloseMenu(false))).openContextMenu(this.chapterPanel.questScreen);
            } else {
                this.group.toggleCollapsed();
                this.parent.refreshWidgets();
            }
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            GuiHelper.setupDrawing();
            if (this.isMouseOver()) {
                Color4I.WHITE.withAlpha(40).draw(graphics, x + 1, y, w - 2, h);
            }
            ChatFormatting f = this.isMouseOver() ? ChatFormatting.WHITE : ChatFormatting.GRAY;
            (this.group.isGuiCollapsed() ? ChapterPanel.ARROW_COLLAPSED : ChapterPanel.ARROW_EXPANDED).withColor(Color4I.getChatFormattingColor(f)).draw(graphics, x + 3, y + 5, 8, 8);
            theme.drawString(graphics, Component.literal("").append(this.title).withStyle(f), x + 15, y + 5);
            boolean canEdit = this.chapterPanel.questScreen.file.canEdit();
            if (canEdit) {
                ThemeProperties.ADD_ICON.get().draw(graphics, x + w - 14, y + 3, 12, 12);
            }
        }

        @Override
        public int getActualWidth(QuestScreen screen) {
            boolean canEdit = this.chapterPanel.questScreen.file.canEdit();
            return screen.getTheme().getStringWidth(this.title) + 20 + (canEdit ? 16 : 0);
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            this.chapterPanel.questScreen.addInfoTooltip(list, this.group);
        }
    }

    public abstract static class ListButton extends Button {

        public final ChapterPanel chapterPanel;

        public ListButton(ChapterPanel panel, Component t, Icon i) {
            super(panel, t, i);
            this.setSize(100, 14);
            this.chapterPanel = panel;
        }

        public int getActualWidth(QuestScreen screen) {
            return screen.getTheme().getStringWidth(this.title) + 20;
        }

        @Override
        public void addMouseOverText(TooltipList list) {
        }

        @Override
        public Optional<PositionedIngredient> getIngredientUnderMouse() {
            return PositionedIngredient.of(this.icon.getIngredient(), this);
        }
    }

    public static class ModpackButton extends ChapterPanel.ListButton {

        public ModpackButton(ChapterPanel panel, ClientQuestFile f) {
            super(panel, f.getTitle(), f.getIcon());
            this.setSize(100, 18);
        }

        @Override
        public void onClicked(MouseButton button) {
            if (this.getMouseX() > this.getX() + this.width - 18) {
                this.playClickSound();
                new ToggleChapterPinnedMessage().sendToServer();
            } else if (this.chapterPanel.questScreen.file.canEdit() && this.getMouseX() > this.getX() + this.width - 34) {
                this.playClickSound();
                List<ContextMenuItem> contextMenu = new ArrayList();
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.chapter"), ThemeProperties.ADD_ICON.get(), b -> {
                    StringConfig c = new StringConfig(Pattern.compile("^.+$"));
                    EditStringConfigOverlay<String> overlay = new EditStringConfigOverlay<>(this.parent, c, accepted -> {
                        this.chapterPanel.questScreen.openGui();
                        if (accepted && !c.getValue().isEmpty()) {
                            Chapter chapter = new Chapter(0L, this.chapterPanel.questScreen.file, this.chapterPanel.questScreen.file.getDefaultChapterGroup());
                            chapter.setRawTitle(c.getValue());
                            CompoundTag extra = new CompoundTag();
                            extra.putLong("group", 0L);
                            new CreateObjectMessage(chapter, extra).sendToServer();
                        }
                        this.run();
                    }, b.getTitle()).atMousePosition();
                    overlay.setWidth(150);
                    overlay.setExtraZlevel(310);
                    this.getGui().pushModalPanel(overlay);
                }));
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.chapter_group"), ThemeProperties.ADD_ICON.get(), b -> {
                    StringConfig c = new StringConfig(Pattern.compile("^.+$"));
                    EditStringConfigOverlay<String> overlay = new EditStringConfigOverlay<>(this.parent, c, accepted -> {
                        this.chapterPanel.questScreen.openGui();
                        if (accepted) {
                            ChapterGroup group = new ChapterGroup(0L, ClientQuestFile.INSTANCE);
                            group.setRawTitle(c.getValue());
                            new CreateObjectMessage(group, null).sendToServer();
                        }
                    }, b.getTitle()).atMousePosition();
                    overlay.setWidth(150);
                    overlay.setExtraZlevel(310);
                    this.getGui().pushModalPanel(overlay);
                }));
                this.chapterPanel.questScreen.openContextMenu(contextMenu);
            }
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            GuiHelper.setupDrawing();
            if (this.isMouseOver()) {
                Color4I.WHITE.withAlpha(40).draw(graphics, x + 1, y + 1, w - 2, h - 2);
            }
            ChatFormatting f = this.isMouseOver() ? ChatFormatting.WHITE : ChatFormatting.GRAY;
            this.icon.draw(graphics, x + 2, y + 3, 12, 12);
            theme.drawString(graphics, Component.literal("").append(this.title).withStyle(f), x + 16, y + 5);
            ThemeProperties.WIDGET_BORDER.get(ClientQuestFile.INSTANCE).draw(graphics, x, y + h - 1, w, 1);
            boolean canEdit = this.chapterPanel.questScreen.file.canEdit();
            (this.chapterPanel.isPinned() ? ThemeProperties.PIN_ICON_ON : ThemeProperties.PIN_ICON_OFF).get().draw(graphics, x + w - 16, y + 3, 12, 12);
            if (canEdit) {
                ThemeProperties.ADD_ICON.get().draw(graphics, x + w - 31, y + 3, 12, 12);
            }
        }

        @Override
        public int getActualWidth(QuestScreen screen) {
            boolean canEdit = this.chapterPanel.questScreen.file.canEdit();
            return screen.getTheme().getStringWidth(this.title) + 36 + (canEdit ? 16 : 0);
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            this.chapterPanel.questScreen.addInfoTooltip(list, this.chapterPanel.questScreen.file);
            if (this.getMouseX() > this.getX() + this.width - 18) {
                list.string(this.chapterPanel.isPinned() ? "Stays open" : "Doesn't stay open");
            } else if (this.chapterPanel.questScreen.file.canEdit() && this.getMouseX() > this.getX() + this.width - 34) {
                list.translate("gui.add");
            }
        }
    }
}