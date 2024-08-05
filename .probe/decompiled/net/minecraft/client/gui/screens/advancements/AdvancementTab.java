package net.minecraft.client.gui.screens.advancements;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class AdvancementTab {

    private final Minecraft minecraft;

    private final AdvancementsScreen screen;

    private final AdvancementTabType type;

    private final int index;

    private final Advancement advancement;

    private final DisplayInfo display;

    private final ItemStack icon;

    private final Component title;

    private final AdvancementWidget root;

    private final Map<Advancement, AdvancementWidget> widgets = Maps.newLinkedHashMap();

    private double scrollX;

    private double scrollY;

    private int minX = Integer.MAX_VALUE;

    private int minY = Integer.MAX_VALUE;

    private int maxX = Integer.MIN_VALUE;

    private int maxY = Integer.MIN_VALUE;

    private float fade;

    private boolean centered;

    public AdvancementTab(Minecraft minecraft0, AdvancementsScreen advancementsScreen1, AdvancementTabType advancementTabType2, int int3, Advancement advancement4, DisplayInfo displayInfo5) {
        this.minecraft = minecraft0;
        this.screen = advancementsScreen1;
        this.type = advancementTabType2;
        this.index = int3;
        this.advancement = advancement4;
        this.display = displayInfo5;
        this.icon = displayInfo5.getIcon();
        this.title = displayInfo5.getTitle();
        this.root = new AdvancementWidget(this, minecraft0, advancement4, displayInfo5);
        this.addWidget(this.root, advancement4);
    }

    public AdvancementTabType getType() {
        return this.type;
    }

    public int getIndex() {
        return this.index;
    }

    public Advancement getAdvancement() {
        return this.advancement;
    }

    public Component getTitle() {
        return this.title;
    }

    public DisplayInfo getDisplay() {
        return this.display;
    }

    public void drawTab(GuiGraphics guiGraphics0, int int1, int int2, boolean boolean3) {
        this.type.draw(guiGraphics0, int1, int2, boolean3, this.index);
    }

    public void drawIcon(GuiGraphics guiGraphics0, int int1, int int2) {
        this.type.drawIcon(guiGraphics0, int1, int2, this.index, this.icon);
    }

    public void drawContents(GuiGraphics guiGraphics0, int int1, int int2) {
        if (!this.centered) {
            this.scrollX = (double) (117 - (this.maxX + this.minX) / 2);
            this.scrollY = (double) (56 - (this.maxY + this.minY) / 2);
            this.centered = true;
        }
        guiGraphics0.enableScissor(int1, int2, int1 + 234, int2 + 113);
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate((float) int1, (float) int2, 0.0F);
        ResourceLocation $$3 = (ResourceLocation) Objects.requireNonNullElse(this.display.getBackground(), TextureManager.INTENTIONAL_MISSING_TEXTURE);
        int $$4 = Mth.floor(this.scrollX);
        int $$5 = Mth.floor(this.scrollY);
        int $$6 = $$4 % 16;
        int $$7 = $$5 % 16;
        for (int $$8 = -1; $$8 <= 15; $$8++) {
            for (int $$9 = -1; $$9 <= 8; $$9++) {
                guiGraphics0.blit($$3, $$6 + 16 * $$8, $$7 + 16 * $$9, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }
        this.root.drawConnectivity(guiGraphics0, $$4, $$5, true);
        this.root.drawConnectivity(guiGraphics0, $$4, $$5, false);
        this.root.draw(guiGraphics0, $$4, $$5);
        guiGraphics0.pose().popPose();
        guiGraphics0.disableScissor();
    }

    public void drawTooltips(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4) {
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate(0.0F, 0.0F, -200.0F);
        guiGraphics0.fill(0, 0, 234, 113, Mth.floor(this.fade * 255.0F) << 24);
        boolean $$5 = false;
        int $$6 = Mth.floor(this.scrollX);
        int $$7 = Mth.floor(this.scrollY);
        if (int1 > 0 && int1 < 234 && int2 > 0 && int2 < 113) {
            for (AdvancementWidget $$8 : this.widgets.values()) {
                if ($$8.isMouseOver($$6, $$7, int1, int2)) {
                    $$5 = true;
                    $$8.drawHover(guiGraphics0, $$6, $$7, this.fade, int3, int4);
                    break;
                }
            }
        }
        guiGraphics0.pose().popPose();
        if ($$5) {
            this.fade = Mth.clamp(this.fade + 0.02F, 0.0F, 0.3F);
        } else {
            this.fade = Mth.clamp(this.fade - 0.04F, 0.0F, 1.0F);
        }
    }

    public boolean isMouseOver(int int0, int int1, double double2, double double3) {
        return this.type.isMouseOver(int0, int1, this.index, double2, double3);
    }

    @Nullable
    public static AdvancementTab create(Minecraft minecraft0, AdvancementsScreen advancementsScreen1, int int2, Advancement advancement3) {
        if (advancement3.getDisplay() == null) {
            return null;
        } else {
            for (AdvancementTabType $$4 : AdvancementTabType.values()) {
                if (int2 < $$4.getMax()) {
                    return new AdvancementTab(minecraft0, advancementsScreen1, $$4, int2, advancement3, advancement3.getDisplay());
                }
                int2 -= $$4.getMax();
            }
            return null;
        }
    }

    public void scroll(double double0, double double1) {
        if (this.maxX - this.minX > 234) {
            this.scrollX = Mth.clamp(this.scrollX + double0, (double) (-(this.maxX - 234)), 0.0);
        }
        if (this.maxY - this.minY > 113) {
            this.scrollY = Mth.clamp(this.scrollY + double1, (double) (-(this.maxY - 113)), 0.0);
        }
    }

    public void addAdvancement(Advancement advancement0) {
        if (advancement0.getDisplay() != null) {
            AdvancementWidget $$1 = new AdvancementWidget(this, this.minecraft, advancement0, advancement0.getDisplay());
            this.addWidget($$1, advancement0);
        }
    }

    private void addWidget(AdvancementWidget advancementWidget0, Advancement advancement1) {
        this.widgets.put(advancement1, advancementWidget0);
        int $$2 = advancementWidget0.getX();
        int $$3 = $$2 + 28;
        int $$4 = advancementWidget0.getY();
        int $$5 = $$4 + 27;
        this.minX = Math.min(this.minX, $$2);
        this.maxX = Math.max(this.maxX, $$3);
        this.minY = Math.min(this.minY, $$4);
        this.maxY = Math.max(this.maxY, $$5);
        for (AdvancementWidget $$6 : this.widgets.values()) {
            $$6.attachToParent();
        }
    }

    @Nullable
    public AdvancementWidget getWidget(Advancement advancement0) {
        return (AdvancementWidget) this.widgets.get(advancement0);
    }

    public AdvancementsScreen getScreen() {
        return this.screen;
    }
}