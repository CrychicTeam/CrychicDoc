package net.minecraft.client.gui.components.spectator;

import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import net.minecraft.client.gui.spectator.SpectatorMenuListener;
import net.minecraft.client.gui.spectator.categories.SpectatorPage;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SpectatorGui implements SpectatorMenuListener {

    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    public static final ResourceLocation SPECTATOR_LOCATION = new ResourceLocation("textures/gui/spectator_widgets.png");

    private static final long FADE_OUT_DELAY = 5000L;

    private static final long FADE_OUT_TIME = 2000L;

    private final Minecraft minecraft;

    private long lastSelectionTime;

    @Nullable
    private SpectatorMenu menu;

    public SpectatorGui(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    public void onHotbarSelected(int int0) {
        this.lastSelectionTime = Util.getMillis();
        if (this.menu != null) {
            this.menu.selectSlot(int0);
        } else {
            this.menu = new SpectatorMenu(this);
        }
    }

    private float getHotbarAlpha() {
        long $$0 = this.lastSelectionTime - Util.getMillis() + 5000L;
        return Mth.clamp((float) $$0 / 2000.0F, 0.0F, 1.0F);
    }

    public void renderHotbar(GuiGraphics guiGraphics0) {
        if (this.menu != null) {
            float $$1 = this.getHotbarAlpha();
            if ($$1 <= 0.0F) {
                this.menu.exit();
            } else {
                int $$2 = guiGraphics0.guiWidth() / 2;
                guiGraphics0.pose().pushPose();
                guiGraphics0.pose().translate(0.0F, 0.0F, -90.0F);
                int $$3 = Mth.floor((float) guiGraphics0.guiHeight() - 22.0F * $$1);
                SpectatorPage $$4 = this.menu.getCurrentPage();
                this.renderPage(guiGraphics0, $$1, $$2, $$3, $$4);
                guiGraphics0.pose().popPose();
            }
        }
    }

    protected void renderPage(GuiGraphics guiGraphics0, float float1, int int2, int int3, SpectatorPage spectatorPage4) {
        RenderSystem.enableBlend();
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, float1);
        guiGraphics0.blit(WIDGETS_LOCATION, int2 - 91, int3, 0, 0, 182, 22);
        if (spectatorPage4.getSelectedSlot() >= 0) {
            guiGraphics0.blit(WIDGETS_LOCATION, int2 - 91 - 1 + spectatorPage4.getSelectedSlot() * 20, int3 - 1, 0, 22, 24, 22);
        }
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        for (int $$5 = 0; $$5 < 9; $$5++) {
            this.renderSlot(guiGraphics0, $$5, guiGraphics0.guiWidth() / 2 - 90 + $$5 * 20 + 2, (float) (int3 + 3), float1, spectatorPage4.getItem($$5));
        }
        RenderSystem.disableBlend();
    }

    private void renderSlot(GuiGraphics guiGraphics0, int int1, int int2, float float3, float float4, SpectatorMenuItem spectatorMenuItem5) {
        if (spectatorMenuItem5 != SpectatorMenu.EMPTY_SLOT) {
            int $$6 = (int) (float4 * 255.0F);
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate((float) int2, float3, 0.0F);
            float $$7 = spectatorMenuItem5.isEnabled() ? 1.0F : 0.25F;
            guiGraphics0.setColor($$7, $$7, $$7, float4);
            spectatorMenuItem5.renderIcon(guiGraphics0, $$7, $$6);
            guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            guiGraphics0.pose().popPose();
            if ($$6 > 3 && spectatorMenuItem5.isEnabled()) {
                Component $$8 = this.minecraft.options.keyHotbarSlots[int1].getTranslatedKeyMessage();
                guiGraphics0.drawString(this.minecraft.font, $$8, int2 + 19 - 2 - this.minecraft.font.width($$8), (int) float3 + 6 + 3, 16777215 + ($$6 << 24));
            }
        }
    }

    public void renderTooltip(GuiGraphics guiGraphics0) {
        int $$1 = (int) (this.getHotbarAlpha() * 255.0F);
        if ($$1 > 3 && this.menu != null) {
            SpectatorMenuItem $$2 = this.menu.getSelectedItem();
            Component $$3 = $$2 == SpectatorMenu.EMPTY_SLOT ? this.menu.getSelectedCategory().getPrompt() : $$2.getName();
            if ($$3 != null) {
                int $$4 = (guiGraphics0.guiWidth() - this.minecraft.font.width($$3)) / 2;
                int $$5 = guiGraphics0.guiHeight() - 35;
                guiGraphics0.drawString(this.minecraft.font, $$3, $$4, $$5, 16777215 + ($$1 << 24));
            }
        }
    }

    @Override
    public void onSpectatorMenuClosed(SpectatorMenu spectatorMenu0) {
        this.menu = null;
        this.lastSelectionTime = 0L;
    }

    public boolean isMenuActive() {
        return this.menu != null;
    }

    public void onMouseScrolled(int int0) {
        int $$1 = this.menu.getSelectedSlot() + int0;
        while ($$1 >= 0 && $$1 <= 8 && (this.menu.getItem($$1) == SpectatorMenu.EMPTY_SLOT || !this.menu.getItem($$1).isEnabled())) {
            $$1 += int0;
        }
        if ($$1 >= 0 && $$1 <= 8) {
            this.menu.selectSlot($$1);
            this.lastSelectionTime = Util.getMillis();
        }
    }

    public void onMouseMiddleClick() {
        this.lastSelectionTime = Util.getMillis();
        if (this.isMenuActive()) {
            int $$0 = this.menu.getSelectedSlot();
            if ($$0 != -1) {
                this.menu.selectSlot($$0);
            }
        } else {
            this.menu = new SpectatorMenu(this);
        }
    }
}