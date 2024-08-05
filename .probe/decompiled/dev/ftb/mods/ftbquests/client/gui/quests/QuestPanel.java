package dev.ftb.mods.ftbquests.client.gui.quests;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import dev.ftb.mods.ftblibrary.config.ImageResourceConfig;
import dev.ftb.mods.ftblibrary.config.ui.SelectImageResourceScreen;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ImageIcon;
import dev.ftb.mods.ftblibrary.math.MathUtils;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.client.FTBQuestsClientConfig;
import dev.ftb.mods.ftbquests.net.CopyChapterImageMessage;
import dev.ftb.mods.ftbquests.net.CopyQuestMessage;
import dev.ftb.mods.ftbquests.net.CreateObjectMessage;
import dev.ftb.mods.ftbquests.net.CreateTaskAtMessage;
import dev.ftb.mods.ftbquests.net.EditObjectMessage;
import dev.ftb.mods.ftbquests.quest.ChapterImage;
import dev.ftb.mods.ftbquests.quest.Movable;
import dev.ftb.mods.ftbquests.quest.ProgressionMode;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestLink;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.QuestShape;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class QuestPanel extends Panel {

    private static final ImageIcon DEFAULT_DEPENDENCY_LINE_TEXTURE = (ImageIcon) Icon.getIcon("ftbquests:textures/gui/dependency.png");

    private final QuestScreen questScreen;

    protected double questX = 0.0;

    protected double questY = 0.0;

    double centerQuestX = 0.0;

    double centerQuestY = 0.0;

    QuestButton mouseOverQuest = null;

    private double questMinX;

    private double questMinY;

    private double questMaxX;

    private double questMaxY;

    public QuestPanel(Panel panel) {
        super(panel);
        this.questScreen = (QuestScreen) panel.getGui();
    }

    public void updateMinMax() {
        this.questMinX = Double.POSITIVE_INFINITY;
        this.questMinY = Double.POSITIVE_INFINITY;
        this.questMaxX = Double.NEGATIVE_INFINITY;
        this.questMaxY = Double.NEGATIVE_INFINITY;
        for (Widget w : this.widgets) {
            if (w instanceof QuestPositionableButton qb) {
                double qx = qb.getPosition().x();
                double qy = qb.getPosition().y();
                double qw = qb.getPosition().w();
                double qh = qb.getPosition().h();
                this.questMinX = Math.min(this.questMinX, qx - qw / 2.0);
                this.questMinY = Math.min(this.questMinY, qy - qh / 2.0);
                this.questMaxX = Math.max(this.questMaxX, qx + qw / 2.0);
                this.questMaxY = Math.max(this.questMaxY, qy + qh / 2.0);
            }
        }
        if (this.questMinX == Double.POSITIVE_INFINITY) {
            this.questMinX = this.questMinY = this.questMaxX = this.questMaxY = 0.0;
        }
        this.questMinX -= 40.0;
        this.questMinY -= 30.0;
        this.questMaxX += 40.0;
        this.questMaxY += 30.0;
    }

    public void scrollTo(double x, double y) {
        this.updateMinMax();
        double dx = this.questMaxX - this.questMinX;
        double dy = this.questMaxY - this.questMinY;
        this.setScrollX((x - this.questMinX) / dx * this.questScreen.scrollWidth - (double) this.width / 2.0);
        this.setScrollY((y - this.questMinY) / dy * this.questScreen.scrollHeight - (double) this.height / 2.0);
    }

    public void resetScroll() {
        this.alignWidgets();
        this.setScrollX((this.questScreen.scrollWidth - (double) this.width) / 2.0);
        this.setScrollY((this.questScreen.scrollHeight - (double) this.height) / 2.0);
    }

    public void withPreservedPos(Consumer<QuestPanel> r) {
        double sx = this.centerQuestX;
        double sy = this.centerQuestY;
        r.accept(this);
        this.scrollTo(sx, sy);
    }

    @Override
    public void addWidgets() {
        if (this.questScreen.selectedChapter != null) {
            this.questScreen.selectedChapter.getImages().stream().filter(image -> this.questScreen.file.canEdit() || image.shouldShowImage(this.questScreen.file.selfTeamData)).sorted(Comparator.comparingInt(ChapterImage::getOrder)).forEach(image -> this.add(new ChapterImageButton(this, image)));
            this.questScreen.selectedChapter.getQuests().forEach(quest -> this.add(new QuestButton(this, quest)));
            this.questScreen.selectedChapter.getQuestLinks().forEach(link -> link.getQuest().ifPresent(quest -> this.add(new QuestLinkButton(this, link, quest))));
            this.alignWidgets();
        }
    }

    @Override
    public void alignWidgets() {
        if (this.questScreen.selectedChapter != null) {
            this.questScreen.scrollWidth = 0.0;
            this.questScreen.scrollHeight = 0.0;
            this.updateMinMax();
            double bs = this.questScreen.getQuestButtonSize();
            double bp = this.questScreen.getQuestButtonSpacing();
            this.questScreen.scrollWidth = (this.questMaxX - this.questMinX) * (bs + bp);
            this.questScreen.scrollHeight = (this.questMaxY - this.questMinY) * (bs + bp);
            for (Widget w : this.widgets) {
                if (w instanceof QuestPositionableButton pos) {
                    double qx = pos.getPosition().x();
                    double qy = pos.getPosition().y();
                    double qw = pos.getPosition().w();
                    double qh = pos.getPosition().h();
                    double x = (qx - this.questMinX - qw / 2.0) * (bs + bp) + bp / 2.0 + bp * (qw - 1.0) / 2.0;
                    double y = (qy - this.questMinY - qh / 2.0) * (bs + bp) + bp / 2.0 + bp * (qh - 1.0) / 2.0;
                    w.setPosAndSize((int) x, (int) y, (int) (bs * qw), (int) (bs * qh));
                }
            }
            this.setPosAndSize(20, 1, this.questScreen.width - 40, this.questScreen.height - 2);
        }
    }

    @Override
    public void drawOffsetBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        if (this.questScreen.selectedChapter != null && this.questScreen.file.selfTeamData != null) {
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.getBuilder();
            Icon icon = ThemeProperties.DEPENDENCY_LINE_TEXTURE.get(this.questScreen.selectedChapter);
            if (icon instanceof ImageIcon img) {
                img.bindTexture();
            } else {
                DEFAULT_DEPENDENCY_LINE_TEXTURE.bindTexture();
            }
            Quest selectedQuest = this.questScreen.getViewedQuest();
            if (selectedQuest == null) {
                Collection<Quest> sel = this.questScreen.getSelectedQuests();
                if (sel.size() == 1) {
                    selectedQuest = (Quest) this.questScreen.getSelectedQuests().stream().findFirst().orElse(null);
                }
            }
            double mt = -((double) System.currentTimeMillis() * 0.001);
            float lineWidth = (float) ((double) this.questScreen.getZoom() * ThemeProperties.DEPENDENCY_LINE_THICKNESS.get(this.questScreen.selectedChapter) / 4.0 * 3.0);
            RenderSystem.setShader(GameRenderer::m_172814_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            float mu = (float) (mt * ThemeProperties.DEPENDENCY_LINE_UNSELECTED_SPEED.get(this.questScreen.selectedChapter) % 1.0);
            for (Widget widget : this.widgets) {
                if (widget.shouldDraw() && widget instanceof QuestButton) {
                    QuestButton qb = (QuestButton) widget;
                    if (!qb.quest.shouldHideDependencyLines()) {
                        boolean unavailable = !this.questScreen.file.selfTeamData.canStartTasks(qb.quest);
                        boolean complete = !unavailable && this.questScreen.file.selfTeamData.isCompleted(qb.quest);
                        Color4I c = complete ? ThemeProperties.DEPENDENCY_LINE_COMPLETED_COLOR.get(this.questScreen.selectedChapter) : ThemeProperties.DEPENDENCY_LINE_UNCOMPLETED_COLOR.get(this.questScreen.selectedChapter);
                        if (unavailable || qb.quest.getProgressionMode() == ProgressionMode.FLEXIBLE && !this.questScreen.file.selfTeamData.areDependenciesComplete(qb.quest)) {
                            c = c.withAlpha(Math.max(30, c.alphai() / 2));
                        }
                        for (QuestButton button : qb.getDependencies()) {
                            if (button.shouldDraw() && button.quest != selectedQuest && qb.quest != selectedQuest && !button.quest.shouldHideDependentLines()) {
                                this.renderConnection(widget, button, graphics.pose(), buffer, lineWidth, c.redi(), c.greeni(), c.bluei(), c.alphai(), c.alphai(), mu, tesselator);
                            }
                        }
                    }
                }
            }
            float ms = (float) (mt * ThemeProperties.DEPENDENCY_LINE_SELECTED_SPEED.get(this.questScreen.selectedChapter) % 1.0);
            List<QuestButton> toOutline = new ArrayList();
            for (Widget widgetx : this.widgets) {
                if (widgetx.shouldDraw() && widgetx instanceof QuestButton) {
                    QuestButton qb = (QuestButton) widgetx;
                    if (!qb.quest.shouldHideDependencyLines()) {
                        for (QuestButton buttonx : qb.getDependencies()) {
                            if (buttonx.shouldDraw()) {
                                if (buttonx.quest == selectedQuest || buttonx.isMouseOver()) {
                                    Color4I c = ThemeProperties.DEPENDENCY_LINE_REQUIRED_FOR_COLOR.get(this.questScreen.selectedChapter);
                                    int a;
                                    int a2;
                                    if (qb.shouldDraw()) {
                                        a = a2 = c.alphai();
                                    } else {
                                        a = c.alphai() / 4 * 3;
                                        a2 = 30;
                                        toOutline.add(qb);
                                    }
                                    this.renderConnection(widgetx, buttonx, graphics.pose(), buffer, lineWidth, c.redi(), c.greeni(), c.bluei(), a2, a, ms, tesselator);
                                } else if (qb.quest == selectedQuest || qb.isMouseOver()) {
                                    Color4I c = ThemeProperties.DEPENDENCY_LINE_REQUIRES_COLOR.get(this.questScreen.selectedChapter);
                                    this.renderConnection(widgetx, buttonx, graphics.pose(), buffer, lineWidth, c.redi(), c.greeni(), c.bluei(), c.alphai(), c.alphai(), ms, tesselator);
                                }
                            }
                        }
                    }
                }
            }
            toOutline.forEach(qbx -> {
                QuestShape.get(qbx.quest.getShape()).getShape().withColor(Color4I.BLACK.withAlpha(30)).draw(graphics, qbx.getX(), qbx.getY(), qbx.width, qbx.height);
                QuestShape.get(qbx.quest.getShape()).getOutline().withColor(Color4I.BLACK.withAlpha(90)).draw(graphics, qbx.getX(), qbx.getY(), qbx.width, qbx.height);
            });
        }
    }

    private void renderConnection(Widget widget, QuestButton button, PoseStack poseStack, BufferBuilder buffer, float s, int r, int g, int b, int a, int a1, float mu, Tesselator tesselator) {
        int sx = widget.getX() + widget.width / 2;
        int sy = widget.getY() + widget.height / 2;
        int ex = button.getX() + button.width / 2;
        int ey = button.getY() + button.height / 2;
        float len = (float) MathUtils.dist((double) sx, (double) sy, (double) ex, (double) ey);
        poseStack.pushPose();
        poseStack.translate((float) sx, (float) sy, 0.0F);
        poseStack.mulPose(Axis.ZP.rotation((float) Math.atan2((double) (ey - sy), (double) (ex - sx))));
        Matrix4f m = poseStack.last().pose();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        buffer.m_252986_(m, 0.0F, -s, 0.0F).color(r, g, b, a).uv(len / s / 2.0F + mu, 0.0F).endVertex();
        buffer.m_252986_(m, 0.0F, s, 0.0F).color(r, g, b, a).uv(len / s / 2.0F + mu, 1.0F).endVertex();
        buffer.m_252986_(m, len, s, 0.0F).color(r * 3 / 4, g * 3 / 4, b * 3 / 4, a1).uv(mu, 1.0F).endVertex();
        buffer.m_252986_(m, len, -s, 0.0F).color(r * 3 / 4, g * 3 / 4, b * 3 / 4, a1).uv(mu, 0.0F).endVertex();
        tesselator.end();
        poseStack.popPose();
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        super.draw(graphics, theme, x, y, w, h);
        if (this.questScreen.selectedChapter != null && this.isMouseOver()) {
            double dx = this.questMaxX - this.questMinX;
            double dy = this.questMaxY - this.questMinY;
            double px = (double) this.getX() - this.getScrollX();
            double py = (double) this.getY() - this.getScrollY();
            double qx = ((double) this.questScreen.getMouseX() - px) / this.questScreen.scrollWidth * dx + this.questMinX;
            double qy = ((double) this.questScreen.getMouseY() - py) / this.questScreen.scrollHeight * dy + this.questMinY;
            this.centerQuestX = ((double) this.questScreen.width / 2.0 - px) / this.questScreen.scrollWidth * dx + this.questMinX;
            this.centerQuestY = ((double) this.questScreen.height / 2.0 - py) / this.questScreen.scrollHeight * dy + this.questMinY;
            if (isShiftKeyDown()) {
                this.questX = qx;
                this.questY = qy;
            } else {
                double minSize = (Double) this.questScreen.selectedObjects.stream().map(mx -> mx instanceof ChapterImage ? 1.0 : mx.getWidth()).min(Double::compare).orElse(1.0);
                double snap = 1.0 / (this.questScreen.file.getGridScale() * minSize);
                this.questX = (double) Mth.floor(qx * snap + 0.5) / snap;
                this.questY = (double) Mth.floor(qy * snap + 0.5) / snap;
            }
            if (this.questScreen.file.canEdit()) {
                PoseStack poseStack = graphics.pose();
                this.drawStatusBar(graphics, theme, poseStack);
                double bs = this.questScreen.getQuestButtonSize();
                if (this.questScreen.movingObjects && !this.questScreen.selectedObjects.isEmpty()) {
                    double ominX = Double.POSITIVE_INFINITY;
                    double ominY = Double.POSITIVE_INFINITY;
                    double omaxX = Double.NEGATIVE_INFINITY;
                    double omaxY = Double.NEGATIVE_INFINITY;
                    for (Movable q : this.questScreen.selectedObjects) {
                        ominX = Math.min(ominX, q.getX());
                        ominY = Math.min(ominY, q.getY());
                        omaxX = Math.max(omaxX, q.getX());
                        omaxY = Math.max(omaxY, q.getY());
                    }
                    for (Movable m : this.questScreen.selectedObjects) {
                        double ox = m.getX() - ominX;
                        double oy = m.getY() - ominY;
                        double sx = (this.questX + ox - this.questMinX) / dx * this.questScreen.scrollWidth + px;
                        double sy = (this.questY + oy - this.questMinY) / dy * this.questScreen.scrollHeight + py;
                        poseStack.pushPose();
                        poseStack.translate(sx - bs * m.getWidth() / 2.0, sy - bs * m.getHeight() / 2.0, 0.0);
                        poseStack.scale((float) (bs * m.getWidth()), (float) (bs * m.getHeight()), 1.0F);
                        GuiHelper.setupDrawing();
                        RenderSystem.enableDepthTest();
                        m.drawMoved(graphics);
                        poseStack.popPose();
                    }
                    if (QuestScreen.grid && !this.questScreen.isViewingQuest()) {
                        double boxX = ominX / dx * this.questScreen.scrollWidth + px;
                        double boxY = ominY / dy * this.questScreen.scrollHeight + py;
                        double boxW = omaxX / dx * this.questScreen.scrollWidth + px - boxX;
                        double boxH = omaxY / dy * this.questScreen.scrollHeight + py - boxY;
                        poseStack.pushPose();
                        poseStack.translate(0.0F, 0.0F, 200.0F);
                        GuiHelper.drawHollowRect(graphics, (int) boxX, (int) boxY, (int) boxW, (int) boxH, Color4I.WHITE.withAlpha(30), false);
                        poseStack.popPose();
                    }
                } else if (!this.questScreen.isViewingQuest() || !this.questScreen.viewQuestPanel.isMouseOver()) {
                    double sx = (this.questX - this.questMinX) / dx * this.questScreen.scrollWidth + px;
                    double sy = (this.questY - this.questMinY) / dy * this.questScreen.scrollHeight + py;
                    poseStack.pushPose();
                    poseStack.translate(sx - bs / 2.0, sy - bs / 2.0, 0.0);
                    poseStack.scale((float) bs, (float) bs, 1.0F);
                    GuiHelper.setupDrawing();
                    RenderSystem.enableDepthTest();
                    QuestShape.get(this.questScreen.selectedChapter.getDefaultQuestShape()).getOutline().withColor(Color4I.WHITE.withAlpha(30)).draw(graphics, 0, 0, 1, 1);
                    poseStack.popPose();
                    if (QuestScreen.grid && !this.questScreen.isViewingQuest()) {
                        poseStack.pushPose();
                        poseStack.translate(0.0F, 0.0F, 1000.0F);
                        Color4I.WHITE.draw(graphics, (int) sx, (int) sy, 1, 1);
                        Color4I.WHITE.withAlpha(30).draw(graphics, this.getX(), (int) sy, this.width, 1);
                        Color4I.WHITE.withAlpha(30).draw(graphics, (int) sx, this.getY(), 1, this.height);
                        poseStack.popPose();
                    }
                }
            }
        }
    }

    private void drawStatusBar(GuiGraphics graphics, Theme theme, PoseStack poseStack) {
        poseStack.pushPose();
        int statusX = this.questScreen.chapterPanel.expanded ? this.questScreen.chapterPanel.width : this.questScreen.expandChaptersButton.width;
        int statusWidth = this.questScreen.chapterPanel.expanded ? this.width - statusX + this.questScreen.expandChaptersButton.width : this.width;
        Color4I statPanelBg = ThemeProperties.WIDGET_BACKGROUND.get();
        Color4I.DARK_GRAY.draw(graphics, statusX, this.height - 9, statusWidth, 1);
        statPanelBg.draw(graphics, statusX, this.height - 9, statusWidth, 10);
        poseStack.translate((float) statusX, (float) (this.height - 6), 250.0F);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        String curStr = String.format("Cursor: [%+.2f, %+.2f]", this.questX, this.questY);
        theme.drawString(graphics, curStr, 6, 0, 2);
        String cStr = String.format("Center: [%.2f, %.2f]", this.centerQuestX, this.centerQuestY);
        theme.drawString(graphics, cStr, statusWidth * 2 - theme.getStringWidth(cStr) - 6, 0, 2);
        int total = this.questScreen.selectedChapter.getQuests().size() + this.questScreen.selectedChapter.getQuestLinks().size() + this.questScreen.selectedChapter.getImages().size();
        String sStr = String.format("Selected: %d / %d", this.questScreen.selectedObjects.size(), total);
        theme.drawString(graphics, sStr, statusWidth / 2, 0, 2);
        if (this.questScreen.movingObjects) {
            theme.drawString(graphics, "Moving", statusWidth, 0, 2);
        }
        poseStack.popPose();
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if (this.questScreen.selectedChapter == null || this.questScreen.chapterPanel.isMouseOver()) {
            return false;
        } else if (this.questScreen.movingObjects && this.questScreen.file.canEdit()) {
            if (this.questScreen.selectedChapter != null && !button.isRight() && !this.questScreen.selectedObjects.isEmpty()) {
                this.playClickSound();
                double minX = Double.POSITIVE_INFINITY;
                double minY = Double.POSITIVE_INFINITY;
                for (Movable q : this.questScreen.selectedObjects) {
                    minX = Math.min(minX, q.getX());
                    minY = Math.min(minY, q.getY());
                }
                for (Movable q : this.questScreen.selectedObjects) {
                    q.move(this.questScreen.selectedChapter, this.questX + (q.getX() - minX), this.questY + (q.getY() - minY));
                }
            }
            this.questScreen.movingObjects = false;
            this.questScreen.selectedObjects.clear();
            return true;
        } else if (super.mousePressed(button)) {
            return true;
        } else if (this.questScreen.isViewingQuest()) {
            this.questScreen.closeQuest();
            return true;
        } else if ((button.isLeft() || button.isMiddle() && this.questScreen.file.canEdit()) && this.isMouseOver() && !this.questScreen.isViewingQuest()) {
            this.questScreen.prevMouseX = this.getMouseX();
            this.questScreen.prevMouseY = this.getMouseY();
            this.questScreen.grabbed = button;
            return true;
        } else if (button.isRight() && this.questScreen.file.canEdit()) {
            this.playClickSound();
            List<ContextMenuItem> contextMenu = new ArrayList();
            double qx = this.questX;
            double qy = this.questY;
            for (TaskType type : TaskTypes.TYPES.values()) {
                contextMenu.add(new ContextMenuItem(type.getDisplayName(), type.getIconSupplier(), b -> {
                    this.playClickSound();
                    type.getGuiProvider().openCreationGui(this, new Quest(0L, this.questScreen.selectedChapter), task -> new CreateTaskAtMessage(this.questScreen.selectedChapter, qx, qy, task).sendToServer());
                }));
            }
            contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.chapter.image"), Icons.ART, b -> this.showImageCreationScreen(qx, qy)));
            String clip = getClipboardString();
            if (!ChapterImage.isImageInClipboard()) {
                QuestObjectBase.parseHexId(clip).ifPresent(questId -> {
                    QuestObject qo = this.questScreen.file.get(questId);
                    contextMenu.add(ContextMenuItem.SEPARATOR);
                    if (qo instanceof Quest quest) {
                        contextMenu.add(new QuestPanel.PasteQuestMenuItem(quest, Component.translatable("ftbquests.gui.paste"), Icons.ADD, b -> new CopyQuestMessage(quest, this.questScreen.selectedChapter, qx, qy, true).sendToServer()));
                        if (quest.hasDependencies()) {
                            contextMenu.add(new QuestPanel.PasteQuestMenuItem(quest, Component.translatable("ftbquests.gui.paste_no_deps"), Icons.ADD_GRAY.withTint(Color4I.rgb(32768)), b -> new CopyQuestMessage(quest, this.questScreen.selectedChapter, qx, qy, false).sendToServer()));
                        }
                        contextMenu.add(new QuestPanel.PasteQuestMenuItem(quest, Component.translatable("ftbquests.gui.paste_link"), Icons.ADD_GRAY.withTint(Color4I.rgb(8421568)), b -> {
                            QuestLink link = new QuestLink(0L, this.questScreen.selectedChapter, quest.id);
                            link.setPosition(qx, qy);
                            new CreateObjectMessage(link, new CompoundTag()).sendToServer();
                        }));
                    } else if (qo instanceof Task task) {
                        contextMenu.add(new AddTaskButton.PasteTaskMenuItem(task, b -> this.copyAndCreateTask(task, qx, qy)));
                    }
                });
            } else {
                ChapterImageButton.getClipboardImage().ifPresent(clipImg -> {
                    contextMenu.add(ContextMenuItem.SEPARATOR);
                    contextMenu.add(new TooltipContextMenuItem(Component.translatable("ftbquests.gui.paste_image"), Icons.ADD, b -> new CopyChapterImageMessage(clipImg, this.questScreen.selectedChapter, qx, qy).sendToServer(), Component.literal(clipImg.getImage().toString()).withStyle(ChatFormatting.GRAY)));
                });
            }
            this.questScreen.openContextMenu(contextMenu);
            return true;
        } else {
            return false;
        }
    }

    private void showImageCreationScreen(double qx, double qy) {
        ImageResourceConfig imageConfig = new ImageResourceConfig();
        new SelectImageResourceScreen(imageConfig, accepted -> {
            if (accepted) {
                this.playClickSound();
                ChapterImage image = new ChapterImage(this.questScreen.selectedChapter).setImage(Icon.getIcon(imageConfig.getValue())).setPosition(qx, qy);
                image.fixupAspectRatio(true);
                this.questScreen.selectedChapter.addImage(image);
                new EditObjectMessage(this.questScreen.selectedChapter).sendToServer();
            }
            this.questScreen.openGui();
        }).openGui();
    }

    private void copyAndCreateTask(Task task, double qx, double qy) {
        Task newTask = QuestObjectBase.copy(task, () -> TaskType.createTask(0L, new Quest(0L, this.questScreen.selectedChapter), task.getType().getTypeId().toString()));
        if (newTask != null) {
            new CreateTaskAtMessage(this.questScreen.selectedChapter, qx, qy, newTask).sendToServer();
        }
    }

    @Override
    public void mouseReleased(MouseButton button) {
        super.mouseReleased(button);
        if (this.questScreen.grabbed != null && this.questScreen.grabbed.isMiddle() && this.questScreen.file.canEdit()) {
            this.questScreen.selectAllQuestsInBox(this.getMouseX(), this.getMouseY(), this.getScrollX(), this.getScrollY());
        }
        this.questScreen.grabbed = null;
    }

    @Override
    public boolean checkMouseOver(int mouseX, int mouseY) {
        return this.questScreen.chapterPanel.isMouseOver() ? false : super.checkMouseOver(mouseX, mouseY);
    }

    @Override
    public void updateMouseOver(int mouseX, int mouseY) {
        this.mouseOverQuest = null;
        super.updateMouseOver(mouseX, mouseY);
        for (Widget widget : this.widgets) {
            if (widget.isMouseOver() && widget instanceof QuestButton) {
                this.mouseOverQuest = (QuestButton) widget;
                break;
            }
        }
    }

    @Override
    public boolean keyPressed(Key key) {
        if (this.questScreen.selectedChapter != null && !this.questScreen.isViewingQuest() && (key.is(45) || key.is(61))) {
            this.questScreen.addZoom(key.is(45) ? -1.0 : 1.0);
            return true;
        } else {
            return super.keyPressed(key);
        }
    }

    @Override
    public boolean scrollPanel(double scroll) {
        if (this.questScreen.selectedChapter != null && !this.questScreen.isViewingQuest() && this.isMouseOver()) {
            if (FTBQuestsClientConfig.OLD_SCROLL_WHEEL.get()) {
                this.questScreen.addZoom(scroll);
            } else if (isShiftKeyDown()) {
                this.setScrollX(this.getScrollX() - scroll * 15.0);
            } else if (isCtrlKeyDown()) {
                this.questScreen.addZoom(scroll);
            } else {
                this.setScrollY(this.getScrollY() - scroll * 15.0);
            }
            return true;
        } else {
            return false;
        }
    }

    private static class PasteQuestMenuItem extends TooltipContextMenuItem {

        public PasteQuestMenuItem(Quest quest, Component title, Icon icon, @Nullable Consumer<Button> callback) {
            super(title, icon, callback, Component.literal("\"").append(quest.getTitle()).append("\""), Component.literal(QuestObjectBase.getCodeString(quest.id)).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}