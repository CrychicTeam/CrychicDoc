package net.minecraft.client.gui.screens.advancements;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

public class AdvancementWidget {

    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/advancements/widgets.png");

    private static final int HEIGHT = 26;

    private static final int BOX_X = 0;

    private static final int BOX_WIDTH = 200;

    private static final int FRAME_WIDTH = 26;

    private static final int ICON_X = 8;

    private static final int ICON_Y = 5;

    private static final int ICON_WIDTH = 26;

    private static final int TITLE_PADDING_LEFT = 3;

    private static final int TITLE_PADDING_RIGHT = 5;

    private static final int TITLE_X = 32;

    private static final int TITLE_Y = 9;

    private static final int TITLE_MAX_WIDTH = 163;

    private static final int[] TEST_SPLIT_OFFSETS = new int[] { 0, 10, -10, 25, -25 };

    private final AdvancementTab tab;

    private final Advancement advancement;

    private final DisplayInfo display;

    private final FormattedCharSequence title;

    private final int width;

    private final List<FormattedCharSequence> description;

    private final Minecraft minecraft;

    @Nullable
    private AdvancementWidget parent;

    private final List<AdvancementWidget> children = Lists.newArrayList();

    @Nullable
    private AdvancementProgress progress;

    private final int x;

    private final int y;

    public AdvancementWidget(AdvancementTab advancementTab0, Minecraft minecraft1, Advancement advancement2, DisplayInfo displayInfo3) {
        this.tab = advancementTab0;
        this.advancement = advancement2;
        this.display = displayInfo3;
        this.minecraft = minecraft1;
        this.title = Language.getInstance().getVisualOrder(minecraft1.font.substrByWidth(displayInfo3.getTitle(), 163));
        this.x = Mth.floor(displayInfo3.getX() * 28.0F);
        this.y = Mth.floor(displayInfo3.getY() * 27.0F);
        int $$4 = advancement2.getMaxCriteraRequired();
        int $$5 = String.valueOf($$4).length();
        int $$6 = $$4 > 1 ? minecraft1.font.width("  ") + minecraft1.font.width("0") * $$5 * 2 + minecraft1.font.width("/") : 0;
        int $$7 = 29 + minecraft1.font.width(this.title) + $$6;
        this.description = Language.getInstance().getVisualOrder(this.findOptimalLines(ComponentUtils.mergeStyles(displayInfo3.getDescription().copy(), Style.EMPTY.withColor(displayInfo3.getFrame().getChatColor())), $$7));
        for (FormattedCharSequence $$8 : this.description) {
            $$7 = Math.max($$7, minecraft1.font.width($$8));
        }
        this.width = $$7 + 3 + 5;
    }

    private static float getMaxWidth(StringSplitter stringSplitter0, List<FormattedText> listFormattedText1) {
        return (float) listFormattedText1.stream().mapToDouble(stringSplitter0::m_92384_).max().orElse(0.0);
    }

    private List<FormattedText> findOptimalLines(Component component0, int int1) {
        StringSplitter $$2 = this.minecraft.font.getSplitter();
        List<FormattedText> $$3 = null;
        float $$4 = Float.MAX_VALUE;
        for (int $$5 : TEST_SPLIT_OFFSETS) {
            List<FormattedText> $$6 = $$2.splitLines(component0, int1 - $$5, Style.EMPTY);
            float $$7 = Math.abs(getMaxWidth($$2, $$6) - (float) int1);
            if ($$7 <= 10.0F) {
                return $$6;
            }
            if ($$7 < $$4) {
                $$4 = $$7;
                $$3 = $$6;
            }
        }
        return $$3;
    }

    @Nullable
    private AdvancementWidget getFirstVisibleParent(Advancement advancement0) {
        do {
            advancement0 = advancement0.getParent();
        } while (advancement0 != null && advancement0.getDisplay() == null);
        return advancement0 != null && advancement0.getDisplay() != null ? this.tab.getWidget(advancement0) : null;
    }

    public void drawConnectivity(GuiGraphics guiGraphics0, int int1, int int2, boolean boolean3) {
        if (this.parent != null) {
            int $$4 = int1 + this.parent.x + 13;
            int $$5 = int1 + this.parent.x + 26 + 4;
            int $$6 = int2 + this.parent.y + 13;
            int $$7 = int1 + this.x + 13;
            int $$8 = int2 + this.y + 13;
            int $$9 = boolean3 ? -16777216 : -1;
            if (boolean3) {
                guiGraphics0.hLine($$5, $$4, $$6 - 1, $$9);
                guiGraphics0.hLine($$5 + 1, $$4, $$6, $$9);
                guiGraphics0.hLine($$5, $$4, $$6 + 1, $$9);
                guiGraphics0.hLine($$7, $$5 - 1, $$8 - 1, $$9);
                guiGraphics0.hLine($$7, $$5 - 1, $$8, $$9);
                guiGraphics0.hLine($$7, $$5 - 1, $$8 + 1, $$9);
                guiGraphics0.vLine($$5 - 1, $$8, $$6, $$9);
                guiGraphics0.vLine($$5 + 1, $$8, $$6, $$9);
            } else {
                guiGraphics0.hLine($$5, $$4, $$6, $$9);
                guiGraphics0.hLine($$7, $$5, $$8, $$9);
                guiGraphics0.vLine($$5, $$8, $$6, $$9);
            }
        }
        for (AdvancementWidget $$10 : this.children) {
            $$10.drawConnectivity(guiGraphics0, int1, int2, boolean3);
        }
    }

    public void draw(GuiGraphics guiGraphics0, int int1, int int2) {
        if (!this.display.isHidden() || this.progress != null && this.progress.isDone()) {
            float $$3 = this.progress == null ? 0.0F : this.progress.getPercent();
            AdvancementWidgetType $$4;
            if ($$3 >= 1.0F) {
                $$4 = AdvancementWidgetType.OBTAINED;
            } else {
                $$4 = AdvancementWidgetType.UNOBTAINED;
            }
            guiGraphics0.blit(WIDGETS_LOCATION, int1 + this.x + 3, int2 + this.y, this.display.getFrame().getTexture(), 128 + $$4.getIndex() * 26, 26, 26);
            guiGraphics0.renderFakeItem(this.display.getIcon(), int1 + this.x + 8, int2 + this.y + 5);
        }
        for (AdvancementWidget $$6 : this.children) {
            $$6.draw(guiGraphics0, int1, int2);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public void setProgress(AdvancementProgress advancementProgress0) {
        this.progress = advancementProgress0;
    }

    public void addChild(AdvancementWidget advancementWidget0) {
        this.children.add(advancementWidget0);
    }

    public void drawHover(GuiGraphics guiGraphics0, int int1, int int2, float float3, int int4, int int5) {
        boolean $$6 = int4 + int1 + this.x + this.width + 26 >= this.tab.getScreen().f_96543_;
        String $$7 = this.progress == null ? null : this.progress.getProgressText();
        int $$8 = $$7 == null ? 0 : this.minecraft.font.width($$7);
        boolean $$9 = 113 - int2 - this.y - 26 <= 6 + this.description.size() * 9;
        float $$10 = this.progress == null ? 0.0F : this.progress.getPercent();
        int $$11 = Mth.floor($$10 * (float) this.width);
        AdvancementWidgetType $$12;
        AdvancementWidgetType $$13;
        AdvancementWidgetType $$14;
        if ($$10 >= 1.0F) {
            $$11 = this.width / 2;
            $$12 = AdvancementWidgetType.OBTAINED;
            $$13 = AdvancementWidgetType.OBTAINED;
            $$14 = AdvancementWidgetType.OBTAINED;
        } else if ($$11 < 2) {
            $$11 = this.width / 2;
            $$12 = AdvancementWidgetType.UNOBTAINED;
            $$13 = AdvancementWidgetType.UNOBTAINED;
            $$14 = AdvancementWidgetType.UNOBTAINED;
        } else if ($$11 > this.width - 2) {
            $$11 = this.width / 2;
            $$12 = AdvancementWidgetType.OBTAINED;
            $$13 = AdvancementWidgetType.OBTAINED;
            $$14 = AdvancementWidgetType.UNOBTAINED;
        } else {
            $$12 = AdvancementWidgetType.OBTAINED;
            $$13 = AdvancementWidgetType.UNOBTAINED;
            $$14 = AdvancementWidgetType.UNOBTAINED;
        }
        int $$24 = this.width - $$11;
        RenderSystem.enableBlend();
        int $$25 = int2 + this.y;
        int $$26;
        if ($$6) {
            $$26 = int1 + this.x - this.width + 26 + 6;
        } else {
            $$26 = int1 + this.x;
        }
        int $$28 = 32 + this.description.size() * 9;
        if (!this.description.isEmpty()) {
            if ($$9) {
                guiGraphics0.blitNineSliced(WIDGETS_LOCATION, $$26, $$25 + 26 - $$28, this.width, $$28, 10, 200, 26, 0, 52);
            } else {
                guiGraphics0.blitNineSliced(WIDGETS_LOCATION, $$26, $$25, this.width, $$28, 10, 200, 26, 0, 52);
            }
        }
        guiGraphics0.blit(WIDGETS_LOCATION, $$26, $$25, 0, $$12.getIndex() * 26, $$11, 26);
        guiGraphics0.blit(WIDGETS_LOCATION, $$26 + $$11, $$25, 200 - $$24, $$13.getIndex() * 26, $$24, 26);
        guiGraphics0.blit(WIDGETS_LOCATION, int1 + this.x + 3, int2 + this.y, this.display.getFrame().getTexture(), 128 + $$14.getIndex() * 26, 26, 26);
        if ($$6) {
            guiGraphics0.drawString(this.minecraft.font, this.title, $$26 + 5, int2 + this.y + 9, -1);
            if ($$7 != null) {
                guiGraphics0.drawString(this.minecraft.font, $$7, int1 + this.x - $$8, int2 + this.y + 9, -1);
            }
        } else {
            guiGraphics0.drawString(this.minecraft.font, this.title, int1 + this.x + 32, int2 + this.y + 9, -1);
            if ($$7 != null) {
                guiGraphics0.drawString(this.minecraft.font, $$7, int1 + this.x + this.width - $$8 - 5, int2 + this.y + 9, -1);
            }
        }
        if ($$9) {
            for (int $$29 = 0; $$29 < this.description.size(); $$29++) {
                guiGraphics0.drawString(this.minecraft.font, (FormattedCharSequence) this.description.get($$29), $$26 + 5, $$25 + 26 - $$28 + 7 + $$29 * 9, -5592406, false);
            }
        } else {
            for (int $$30 = 0; $$30 < this.description.size(); $$30++) {
                guiGraphics0.drawString(this.minecraft.font, (FormattedCharSequence) this.description.get($$30), $$26 + 5, int2 + this.y + 9 + 17 + $$30 * 9, -5592406, false);
            }
        }
        guiGraphics0.renderFakeItem(this.display.getIcon(), int1 + this.x + 8, int2 + this.y + 5);
    }

    public boolean isMouseOver(int int0, int int1, int int2, int int3) {
        if (!this.display.isHidden() || this.progress != null && this.progress.isDone()) {
            int $$4 = int0 + this.x;
            int $$5 = $$4 + 26;
            int $$6 = int1 + this.y;
            int $$7 = $$6 + 26;
            return int2 >= $$4 && int2 <= $$5 && int3 >= $$6 && int3 <= $$7;
        } else {
            return false;
        }
    }

    public void attachToParent() {
        if (this.parent == null && this.advancement.getParent() != null) {
            this.parent = this.getFirstVisibleParent(this.advancement);
            if (this.parent != null) {
                this.parent.addChild(this);
            }
        }
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }
}