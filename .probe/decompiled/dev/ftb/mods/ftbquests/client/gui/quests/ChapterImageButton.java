package dev.ftb.mods.ftbquests.client.gui.quests;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.net.EditObjectMessage;
import dev.ftb.mods.ftbquests.quest.ChapterImage;
import dev.ftb.mods.ftbquests.quest.Movable;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ChapterImageButton extends Button implements QuestPositionableButton {

    private final QuestScreen questScreen;

    private final ChapterImage chapterImage;

    public ChapterImageButton(Panel panel, ChapterImage i) {
        super(panel, Component.empty(), i.getImage());
        this.questScreen = (QuestScreen) panel.getGui();
        this.setSize(20, 20);
        this.chapterImage = i;
        this.setDrawLayer(Widget.DrawLayer.BACKGROUND);
    }

    public static Optional<ChapterImage> getClipboardImage() {
        ChapterImage img = (ChapterImage) ChapterImage.clipboard.get();
        if (img != null) {
            if (img.getChapter().isValid()) {
                return Optional.of(img);
            }
            ChapterImage.clipboard = new WeakReference(null);
        }
        return Optional.empty();
    }

    @Override
    public boolean checkMouseOver(int mouseX, int mouseY) {
        return this.questScreen.questPanel.mouseOverQuest == null && !this.questScreen.movingObjects && !this.questScreen.viewQuestPanel.isMouseOver() && !this.questScreen.chapterPanel.isMouseOver() && (!this.chapterImage.getClick().isEmpty() || this.questScreen.file.canEdit()) ? super.checkMouseOver(mouseX, mouseY) : false;
    }

    @Override
    public void onClicked(MouseButton button) {
        if (this.questScreen.file.canEdit() && button.isRight()) {
            List<ContextMenuItem> contextMenu = new ArrayList();
            contextMenu.add(new ContextMenuItem(Component.translatable("selectServer.edit"), ThemeProperties.EDIT_ICON.get(), b -> {
                String name = this.chapterImage.getImage() instanceof Color4I ? this.chapterImage.getColor().toString() : this.chapterImage.getImage().toString();
                ConfigGroup group = new ConfigGroup("ftbquests", accepted -> {
                    if (accepted) {
                        new EditObjectMessage(this.chapterImage.getChapter()).sendToServer();
                    }
                    this.run();
                }).setNameKey("Img: " + name);
                this.chapterImage.fillConfigGroup(group.getOrCreateSubgroup("chapter").getOrCreateSubgroup("image"));
                new EditConfigScreen(group).openGui();
            }));
            contextMenu.add(new ContextMenuItem(Component.translatable("gui.move"), ThemeProperties.MOVE_UP_ICON.get(this.chapterImage.getChapter()), b -> this.questScreen.initiateMoving(this.chapterImage)) {

                @Override
                public void addMouseOverText(TooltipList list) {
                    list.add(Component.translatable("ftbquests.gui.move_tooltip").withStyle(ChatFormatting.DARK_GRAY));
                }
            });
            contextMenu.add(new ContextMenuItem(Component.translatable("gui.copy"), Icons.INFO, b -> this.chapterImage.copyToClipboard()) {

                @Override
                public void addMouseOverText(TooltipList list) {
                    list.add(Component.literal(ChapterImageButton.this.chapterImage.getImage().toString()).withStyle(ChatFormatting.DARK_GRAY));
                }
            });
            if (this.chapterImage.isAspectRatioOff()) {
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.fix_aspect_ratio_w"), Icons.ART, b -> this.chapterImage.fixupAspectRatio(true)));
                contextMenu.add(new ContextMenuItem(Component.translatable("ftbquests.gui.fix_aspect_ratio_h"), Icons.ART, b -> this.chapterImage.fixupAspectRatio(false)));
            }
            contextMenu.add(new ContextMenuItem(Component.translatable("selectServer.delete"), ThemeProperties.DELETE_ICON.get(), b -> {
                this.chapterImage.getChapter().removeImage(this.chapterImage);
                new EditObjectMessage(this.chapterImage.getChapter()).sendToServer();
            }).setYesNoText(Component.translatable("delete_item", this.chapterImage.getImage().toString())));
            this.getGui().openContextMenu(contextMenu);
        } else if (button.isLeft()) {
            if (Screen.hasControlDown() && this.questScreen.file.canEdit()) {
                this.questScreen.toggleSelected(this.chapterImage);
            } else if (!this.chapterImage.getClick().isEmpty()) {
                this.playClickSound();
                this.handleClick(this.chapterImage.getClick());
            }
        } else if (this.questScreen.file.canEdit() && button.isMiddle()) {
            if (!this.questScreen.selectedObjects.contains(this.chapterImage)) {
                this.questScreen.toggleSelected(this.chapterImage);
            }
            this.questScreen.movingObjects = true;
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        this.chapterImage.addHoverText(list);
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        Icon image = this.chapterImage.getImage();
        boolean transparent = !this.chapterImage.shouldShowImage(this.questScreen.file.selfTeamData);
        if (transparent) {
            image = image.withColor(Color4I.WHITE.withAlpha(100));
        } else if (!this.chapterImage.getColor().equals(Color4I.WHITE) || this.chapterImage.getAlpha() < 255) {
            image = image.withColor(this.chapterImage.getColor().withAlpha(this.chapterImage.getAlpha()));
        }
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        if (this.chapterImage.isAlignToCorner()) {
            poseStack.translate((float) x, (float) y, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) this.chapterImage.getRotation()));
            poseStack.scale((float) w, (float) h, 1.0F);
            image.draw(graphics, 0, 0, 1, 1);
            if (this.questScreen.selectedObjects.contains(this.moveAndDeleteFocus())) {
                Color4I col = Color4I.WHITE.withAlpha((int) (128.0 + Math.sin((double) System.currentTimeMillis() * 0.003) * 50.0));
                col.draw(graphics, 0, 0, 1, 1);
            }
        } else {
            poseStack.translate((float) ((int) ((double) x + (double) w / 2.0)), (float) ((int) ((double) y + (double) h / 2.0)), 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees((float) this.chapterImage.getRotation()));
            poseStack.scale((float) w / 2.0F, (float) h / 2.0F, 1.0F);
            image.draw(graphics, -1, -1, 2, 2);
            if (this.questScreen.selectedObjects.contains(this.moveAndDeleteFocus())) {
                Color4I col = Color4I.WHITE.withAlpha((int) (128.0 + Math.sin((double) System.currentTimeMillis() * 0.003) * 50.0));
                col.draw(graphics, -1, -1, 2, 2);
            }
        }
        poseStack.popPose();
    }

    @Override
    public QuestPositionableButton.Position getPosition() {
        return new QuestPositionableButton.Position(this.chapterImage.getX(), this.chapterImage.getY(), this.chapterImage.getWidth(), this.chapterImage.getHeight());
    }

    @Override
    public int compareTo(@NotNull Widget o) {
        return o instanceof ChapterImageButton cb2 ? Integer.compare(this.chapterImage.getOrder(), cb2.chapterImage.getOrder()) : 0;
    }

    @Override
    public Movable moveAndDeleteFocus() {
        return this.chapterImage;
    }
}