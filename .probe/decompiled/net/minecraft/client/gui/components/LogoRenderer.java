package net.minecraft.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public class LogoRenderer {

    public static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation("textures/gui/title/minecraft.png");

    public static final ResourceLocation EASTER_EGG_LOGO = new ResourceLocation("textures/gui/title/minceraft.png");

    public static final ResourceLocation MINECRAFT_EDITION = new ResourceLocation("textures/gui/title/edition.png");

    public static final int LOGO_WIDTH = 256;

    public static final int LOGO_HEIGHT = 44;

    private static final int LOGO_TEXTURE_WIDTH = 256;

    private static final int LOGO_TEXTURE_HEIGHT = 64;

    private static final int EDITION_WIDTH = 128;

    private static final int EDITION_HEIGHT = 14;

    private static final int EDITION_TEXTURE_WIDTH = 128;

    private static final int EDITION_TEXTURE_HEIGHT = 16;

    public static final int DEFAULT_HEIGHT_OFFSET = 30;

    private static final int EDITION_LOGO_OVERLAP = 7;

    private final boolean showEasterEgg = (double) RandomSource.create().nextFloat() < 1.0E-4;

    private final boolean keepLogoThroughFade;

    public LogoRenderer(boolean boolean0) {
        this.keepLogoThroughFade = boolean0;
    }

    public void renderLogo(GuiGraphics guiGraphics0, int int1, float float2) {
        this.renderLogo(guiGraphics0, int1, float2, 30);
    }

    public void renderLogo(GuiGraphics guiGraphics0, int int1, float float2, int int3) {
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, this.keepLogoThroughFade ? 1.0F : float2);
        int $$4 = int1 / 2 - 128;
        guiGraphics0.blit(this.showEasterEgg ? EASTER_EGG_LOGO : MINECRAFT_LOGO, $$4, int3, 0.0F, 0.0F, 256, 44, 256, 64);
        int $$5 = int1 / 2 - 64;
        int $$6 = int3 + 44 - 7;
        guiGraphics0.blit(MINECRAFT_EDITION, $$5, $$6, 0.0F, 0.0F, 128, 14, 128, 16);
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}