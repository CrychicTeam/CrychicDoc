package net.minecraft.client.gui.screens.inventory.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.ItemStack;

public class ClientBundleTooltip implements ClientTooltipComponent {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/gui/container/bundle.png");

    private static final int MARGIN_Y = 4;

    private static final int BORDER_WIDTH = 1;

    private static final int TEX_SIZE = 128;

    private static final int SLOT_SIZE_X = 18;

    private static final int SLOT_SIZE_Y = 20;

    private final NonNullList<ItemStack> items;

    private final int weight;

    public ClientBundleTooltip(BundleTooltip bundleTooltip0) {
        this.items = bundleTooltip0.getItems();
        this.weight = bundleTooltip0.getWeight();
    }

    @Override
    public int getHeight() {
        return this.gridSizeY() * 20 + 2 + 4;
    }

    @Override
    public int getWidth(Font font0) {
        return this.gridSizeX() * 18 + 2;
    }

    @Override
    public void renderImage(Font font0, int int1, int int2, GuiGraphics guiGraphics3) {
        int $$4 = this.gridSizeX();
        int $$5 = this.gridSizeY();
        boolean $$6 = this.weight >= 64;
        int $$7 = 0;
        for (int $$8 = 0; $$8 < $$5; $$8++) {
            for (int $$9 = 0; $$9 < $$4; $$9++) {
                int $$10 = int1 + $$9 * 18 + 1;
                int $$11 = int2 + $$8 * 20 + 1;
                this.renderSlot($$10, $$11, $$7++, $$6, guiGraphics3, font0);
            }
        }
        this.drawBorder(int1, int2, $$4, $$5, guiGraphics3);
    }

    private void renderSlot(int int0, int int1, int int2, boolean boolean3, GuiGraphics guiGraphics4, Font font5) {
        if (int2 >= this.items.size()) {
            this.blit(guiGraphics4, int0, int1, boolean3 ? ClientBundleTooltip.Texture.BLOCKED_SLOT : ClientBundleTooltip.Texture.SLOT);
        } else {
            ItemStack $$6 = this.items.get(int2);
            this.blit(guiGraphics4, int0, int1, ClientBundleTooltip.Texture.SLOT);
            guiGraphics4.renderItem($$6, int0 + 1, int1 + 1, int2);
            guiGraphics4.renderItemDecorations(font5, $$6, int0 + 1, int1 + 1);
            if (int2 == 0) {
                AbstractContainerScreen.renderSlotHighlight(guiGraphics4, int0 + 1, int1 + 1, 0);
            }
        }
    }

    private void drawBorder(int int0, int int1, int int2, int int3, GuiGraphics guiGraphics4) {
        this.blit(guiGraphics4, int0, int1, ClientBundleTooltip.Texture.BORDER_CORNER_TOP);
        this.blit(guiGraphics4, int0 + int2 * 18 + 1, int1, ClientBundleTooltip.Texture.BORDER_CORNER_TOP);
        for (int $$5 = 0; $$5 < int2; $$5++) {
            this.blit(guiGraphics4, int0 + 1 + $$5 * 18, int1, ClientBundleTooltip.Texture.BORDER_HORIZONTAL_TOP);
            this.blit(guiGraphics4, int0 + 1 + $$5 * 18, int1 + int3 * 20, ClientBundleTooltip.Texture.BORDER_HORIZONTAL_BOTTOM);
        }
        for (int $$6 = 0; $$6 < int3; $$6++) {
            this.blit(guiGraphics4, int0, int1 + $$6 * 20 + 1, ClientBundleTooltip.Texture.BORDER_VERTICAL);
            this.blit(guiGraphics4, int0 + int2 * 18 + 1, int1 + $$6 * 20 + 1, ClientBundleTooltip.Texture.BORDER_VERTICAL);
        }
        this.blit(guiGraphics4, int0, int1 + int3 * 20, ClientBundleTooltip.Texture.BORDER_CORNER_BOTTOM);
        this.blit(guiGraphics4, int0 + int2 * 18 + 1, int1 + int3 * 20, ClientBundleTooltip.Texture.BORDER_CORNER_BOTTOM);
    }

    private void blit(GuiGraphics guiGraphics0, int int1, int int2, ClientBundleTooltip.Texture clientBundleTooltipTexture3) {
        guiGraphics0.blit(TEXTURE_LOCATION, int1, int2, 0, (float) clientBundleTooltipTexture3.x, (float) clientBundleTooltipTexture3.y, clientBundleTooltipTexture3.w, clientBundleTooltipTexture3.h, 128, 128);
    }

    private int gridSizeX() {
        return Math.max(2, (int) Math.ceil(Math.sqrt((double) this.items.size() + 1.0)));
    }

    private int gridSizeY() {
        return (int) Math.ceil(((double) this.items.size() + 1.0) / (double) this.gridSizeX());
    }

    static enum Texture {

        SLOT(0, 0, 18, 20),
        BLOCKED_SLOT(0, 40, 18, 20),
        BORDER_VERTICAL(0, 18, 1, 20),
        BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
        BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
        BORDER_CORNER_TOP(0, 20, 1, 1),
        BORDER_CORNER_BOTTOM(0, 60, 1, 1);

        public final int x;

        public final int y;

        public final int w;

        public final int h;

        private Texture(int p_169928_, int p_169929_, int p_169930_, int p_169931_) {
            this.x = p_169928_;
            this.y = p_169929_;
            this.w = p_169930_;
            this.h = p_169931_;
        }
    }
}