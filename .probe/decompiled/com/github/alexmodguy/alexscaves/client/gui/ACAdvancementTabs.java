package com.github.alexmodguy.alexscaves.client.gui;

import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import java.util.Locale;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

public class ACAdvancementTabs {

    private static final float MAX_TRANSITION_TIME = 25.0F;

    private static ACAdvancementTabs.Type hoverType = ACAdvancementTabs.Type.DEFAULT;

    private static ACAdvancementTabs.Type previousHoverType = ACAdvancementTabs.Type.DEFAULT;

    private static float hoverChangeProgress = 25.0F;

    private static float previousHoverChangeProgress = 25.0F;

    private static int windowWidth;

    private static int windowHeight;

    private static boolean[][] foregroundBlocks;

    public static boolean isAlexsCavesWidget(Advancement root) {
        return root.getId().getNamespace().equals("alexscaves");
    }

    public static void renderTabBackground(GuiGraphics guiGraphics, int topX, int topY, DisplayInfo displayInfo, double scrollX, double scrollY) {
        float partialTick = Minecraft.getInstance().getPartialTick();
        float hoverProgress = getHoverChangeAmount(partialTick);
        float priorHoverProgress = 1.0F - hoverProgress;
        int fastColor = FastColor.ARGB32.lerp(hoverProgress, previousHoverType.backgroundColor, hoverType.backgroundColor);
        guiGraphics.fill(0, 0, windowWidth + 100, windowHeight, fastColor | 0xFF000000);
        renderTabBackgroundForType(guiGraphics, topX, topY, partialTick, scrollX, scrollY, previousHoverType, priorHoverProgress);
        renderTabBackgroundForType(guiGraphics, topX, topY, partialTick, scrollX, scrollY, hoverType, hoverProgress);
    }

    private static void renderTabBackgroundForType(GuiGraphics guiGraphics, int topX, int topY, float partialTick, double scrollX, double scrollY, ACAdvancementTabs.Type type, float alpha) {
        guiGraphics.pose().pushPose();
        if (type != ACAdvancementTabs.Type.DEFAULT) {
            int i = (int) Math.round(scrollX);
            int j = (int) Math.round(scrollY);
            for (int parallaxX = -1; parallaxX <= (windowWidth + 128) / 128; parallaxX++) {
                for (int parallaxY = -1; parallaxY <= (windowWidth + 128) / 128; parallaxY++) {
                    ColorBlitHelper.blitWithColor(guiGraphics, type.background, parallaxX * 128 + i / 4, parallaxY * 128 + j / 4, 0.0F, 0.0F, 128, 128, 128, 128, 1.0F, 1.0F, 1.0F, alpha);
                    ColorBlitHelper.blitWithColor(guiGraphics, type.midground, parallaxX * 128 + i / 2 - 1, parallaxY * 128 + j / 2, 0.0F, 0.0F, 128, 128, 128, 128, 1.0F, 1.0F, 1.0F, alpha);
                }
            }
        }
        int i = Mth.floor(scrollX);
        int j = Mth.floor(scrollY);
        int scrollPixelOffsetX = i % 16;
        int scrollPixelOffsetY = j % 16;
        int blockCoordOffsetX = i / 16;
        int blockCoordOffsetY = j / 16;
        int screenWidthInBlocks = windowWidth / 16 + 6;
        int screenHeightInBlocks = windowHeight / 16 + 6;
        for (int relativeBlockX = -2; relativeBlockX <= screenWidthInBlocks; relativeBlockX++) {
            for (int relativeBlockY = -2; relativeBlockY <= screenHeightInBlocks; relativeBlockY++) {
                int blockX = relativeBlockX - blockCoordOffsetX;
                int blockY = relativeBlockY - blockCoordOffsetY;
                if (type == ACAdvancementTabs.Type.DEFAULT || !isBlockCarvedOut(blockX, blockY, type)) {
                    ColorBlitHelper.blitWithColor(guiGraphics, type.baseStone, 16 * relativeBlockX + scrollPixelOffsetX, 16 * relativeBlockY + scrollPixelOffsetY, 0.0F, 0.0F, 16, 16, 16, 16, 1.0F, 1.0F, 1.0F, alpha);
                }
            }
        }
        guiGraphics.pose().popPose();
    }

    private static boolean isBlockCarvedOut(int blockX, int blockY, ACAdvancementTabs.Type type) {
        int biomeTypeOffset = type.ordinal() * 120;
        float noise = ACMath.sampleNoise2D(blockX + biomeTypeOffset, blockY + biomeTypeOffset, 20.0F);
        return noise < -0.25F || noise > 0.25F;
    }

    public static void tick() {
        previousHoverChangeProgress = hoverChangeProgress;
        if (previousHoverType != hoverType) {
            if (hoverChangeProgress < 25.0F) {
                hoverChangeProgress++;
            } else if (hoverChangeProgress > 25.0F) {
                previousHoverType = hoverType;
            }
        } else {
            hoverChangeProgress = 25.0F;
        }
    }

    private static float getHoverChangeAmount(float partialTick) {
        return (previousHoverChangeProgress + (hoverChangeProgress - previousHoverChangeProgress) * partialTick) / 25.0F;
    }

    public static void setHoverType(ACAdvancementTabs.Type type) {
        if (hoverChangeProgress >= 25.0F && type != hoverType) {
            previousHoverChangeProgress = 0.0F;
            hoverChangeProgress = 0.0F;
            previousHoverType = hoverType;
            hoverType = type;
        }
    }

    public static void setDimensions(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    public static enum Type {

        DEFAULT(new ResourceLocation("alexscaves", "alexscaves/root"), 0, new ResourceLocation("textures/block/stone.png")),
        MAGNETIC(new ResourceLocation("alexscaves", "alexscaves/discover_magnetic_caves"), 394759, new ResourceLocation("alexscaves", "textures/block/galena.png")),
        PRIMORDIAL(new ResourceLocation("alexscaves", "alexscaves/discover_primordial_caves"), 15915104, new ResourceLocation("alexscaves", "textures/block/limestone.png")),
        TOXIC(new ResourceLocation("alexscaves", "alexscaves/discover_toxic_caves"), 8322816, new ResourceLocation("alexscaves", "textures/block/radrock.png")),
        ABYSSAL(new ResourceLocation("alexscaves", "alexscaves/discover_abyssal_chasm"), 70711, new ResourceLocation("alexscaves", "textures/block/abyssmarine.png")),
        FORLORN(new ResourceLocation("alexscaves", "alexscaves/discover_forlorn_hollows"), 1380622, new ResourceLocation("alexscaves", "textures/block/guanostone.png"));

        ResourceLocation root;

        private final int backgroundColor;

        private final ResourceLocation baseStone;

        private final ResourceLocation midground;

        private final ResourceLocation background;

        private Type(ResourceLocation root, int backgroundColor, ResourceLocation baseStone) {
            this.root = root;
            this.backgroundColor = backgroundColor;
            this.baseStone = baseStone;
            this.midground = this.generateTexture("midground");
            this.background = this.generateTexture("background");
        }

        private ResourceLocation generateTexture(String type) {
            return this == DEFAULT ? null : new ResourceLocation("alexscaves", "textures/misc/advancement/" + this.name().toLowerCase(Locale.ROOT) + "_" + type + ".png");
        }

        private static ACAdvancementTabs.Type getDirectType(Advancement advancement) {
            for (ACAdvancementTabs.Type type : values()) {
                if (type.root.equals(advancement.getId())) {
                    return type;
                }
            }
            return DEFAULT;
        }

        public static ACAdvancementTabs.Type forAdvancement(Advancement advancement) {
            ACAdvancementTabs.Type direct = getDirectType(advancement);
            for (Advancement next = advancement; direct == DEFAULT && next.getParent() != null; direct = getDirectType(next)) {
                next = next.getParent();
            }
            return direct;
        }

        public static boolean isTreeNodeUnlocked(AdvancementWidget advancementWidget) {
            if (advancementWidget.progress.isDone()) {
                return true;
            } else {
                ACAdvancementTabs.Type direct = getDirectType(advancementWidget.advancement);
                AdvancementWidget next;
                for (next = advancementWidget; direct == DEFAULT && next.advancement.getParent() != null; direct = getDirectType(next.advancement)) {
                    next = next.parent;
                }
                return direct == DEFAULT || next.progress != null && next.progress.isDone();
            }
        }
    }
}