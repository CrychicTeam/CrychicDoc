package com.simibubi.create.infrastructure.gui;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.config.ui.BaseConfigScreen;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.gui.element.BoxElement;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.ponder.ui.PonderTagIndexScreen;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CreateMainMenuScreen extends AbstractSimiScreen {

    public static final CubeMap PANORAMA_RESOURCES = new CubeMap(Create.asResource("textures/gui/title/background/panorama"));

    public static final ResourceLocation PANORAMA_OVERLAY_TEXTURES = new ResourceLocation("textures/gui/title/background/panorama_overlay.png");

    public static final PanoramaRenderer PANORAMA = new PanoramaRenderer(PANORAMA_RESOURCES);

    private static final Component CURSEFORGE_TOOLTIP = Components.literal("CurseForge").withStyle(s -> s.withColor(16545884).withBold(true));

    private static final Component MODRINTH_TOOLTIP = Components.literal("Modrinth").withStyle(s -> s.withColor(4182827).withBold(true));

    public static final String CURSEFORGE_LINK = "https://www.curseforge.com/minecraft/mc-mods/create";

    public static final String MODRINTH_LINK = "https://modrinth.com/mod/create";

    public static final String ISSUE_TRACKER_LINK = "https://github.com/Creators-of-Create/Create/issues";

    public static final String SUPPORT_LINK = "https://github.com/Creators-of-Create/Create/wiki/Supporting-the-Project";

    protected final Screen parent;

    protected boolean returnOnClose;

    private PanoramaRenderer vanillaPanorama;

    private long firstRenderTime;

    private Button gettingStarted;

    public CreateMainMenuScreen(Screen parent) {
        this.parent = parent;
        this.returnOnClose = true;
        if (parent instanceof TitleScreen titleScreen) {
            this.vanillaPanorama = titleScreen.panorama;
        } else {
            this.vanillaPanorama = new PanoramaRenderer(TitleScreen.CUBE_MAP);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.firstRenderTime == 0L) {
            this.firstRenderTime = Util.getMillis();
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        float f = (float) (Util.getMillis() - this.firstRenderTime) / 1000.0F;
        float alpha = Mth.clamp(f, 0.0F, 1.0F);
        float elapsedPartials = this.f_96541_.getDeltaFrameTime();
        if (this.parent instanceof TitleScreen) {
            if (alpha < 1.0F) {
                this.vanillaPanorama.render(elapsedPartials, 1.0F);
            }
            PANORAMA.render(elapsedPartials, alpha);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            graphics.blit(PANORAMA_OVERLAY_TEXTURES, 0, 0, this.f_96543_, this.f_96544_, 0.0F, 0.0F, 16, 128, 16, 128);
        }
        RenderSystem.enableDepthTest();
        PoseStack ms = graphics.pose();
        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            ms.translate((float) (this.f_96543_ / 2), 60.0F, 200.0F);
            ms.scale((float) (24 * side), (float) (24 * side), 32.0F);
            ms.translate(-1.75 * (double) (alpha * alpha / 2.0F + 0.5F), 0.25, 0.0);
            TransformStack.cast(ms).rotateX(45.0);
            GuiGameElement.of(AllBlocks.LARGE_COGWHEEL.getDefaultState()).rotateBlock(0.0, (double) ((float) Util.getMillis() / 32.0F * (float) side), 0.0).render(graphics);
            ms.translate(-1.0F, 0.0F, -1.0F);
            GuiGameElement.of(AllBlocks.COGWHEEL.getDefaultState()).rotateBlock(0.0, (double) ((float) Util.getMillis() / -16.0F * (float) side + 22.5F), 0.0).render(graphics);
            ms.popPose();
        }
        ms.pushPose();
        ms.translate((float) (this.f_96543_ / 2 - 32), 32.0F, -10.0F);
        ms.pushPose();
        ms.scale(0.25F, 0.25F, 0.25F);
        AllGuiTextures.LOGO.render(graphics, 0, 0);
        ms.popPose();
        new BoxElement().<BoxElement>withBackground(-2013265920).<BoxElement>flatBorder(new Color(16777216)).<RenderElement>at(-32.0F, 56.0F, 100.0F).<RenderElement>withBounds(128, 11).render(graphics);
        ms.popPose();
        ms.pushPose();
        ms.translate(0.0F, 0.0F, 200.0F);
        graphics.drawCenteredString(this.f_96547_, Components.literal("Create").withStyle(ChatFormatting.BOLD).append(Components.literal(" v0.5.1f").withStyle(ChatFormatting.BOLD, ChatFormatting.WHITE)), this.f_96543_ / 2, 89, -1787033);
        ms.popPose();
        RenderSystem.disableDepthTest();
    }

    @Override
    protected void init() {
        super.init();
        this.returnOnClose = true;
        this.addButtons();
    }

    private void addButtons() {
        int yStart = this.f_96544_ / 4 + 40;
        int center = this.f_96543_ / 2;
        int bHeight = 20;
        int bShortWidth = 98;
        int bLongWidth = 200;
        this.m_142416_(Button.builder(Lang.translateDirect("menu.return"), $ -> this.linkTo(this.parent)).bounds(center - 100, yStart + 92, bLongWidth, bHeight).build());
        this.m_142416_(Button.builder(Lang.translateDirect("menu.configure"), $ -> this.linkTo(BaseConfigScreen.forCreate(this))).bounds(center - 100, yStart + 24 + -16, bLongWidth, bHeight).build());
        this.gettingStarted = Button.builder(Lang.translateDirect("menu.ponder_index"), $ -> this.linkTo(new PonderTagIndexScreen())).bounds(center + 2, yStart + 48 + -16, bShortWidth, bHeight).build();
        this.gettingStarted.f_93623_ = !(this.parent instanceof TitleScreen);
        this.m_142416_(this.gettingStarted);
        this.m_142416_(new CreateMainMenuScreen.PlatformIconButton(center - 100, yStart + 48 + -16, bShortWidth / 2, bHeight, AllGuiTextures.CURSEFORGE_LOGO, 0.085F, b -> this.linkTo("https://www.curseforge.com/minecraft/mc-mods/create"), Tooltip.create(CURSEFORGE_TOOLTIP)));
        this.m_142416_(new CreateMainMenuScreen.PlatformIconButton(center - 50, yStart + 48 + -16, bShortWidth / 2, bHeight, AllGuiTextures.MODRINTH_LOGO, 0.0575F, b -> this.linkTo("https://modrinth.com/mod/create"), Tooltip.create(MODRINTH_TOOLTIP)));
        this.m_142416_(Button.builder(Lang.translateDirect("menu.report_bugs"), $ -> this.linkTo("https://github.com/Creators-of-Create/Create/issues")).bounds(center + 2, yStart + 68, bShortWidth, bHeight).build());
        this.m_142416_(Button.builder(Lang.translateDirect("menu.support"), $ -> this.linkTo("https://github.com/Creators-of-Create/Create/wiki/Supporting-the-Project")).bounds(center - 100, yStart + 68, bShortWidth, bHeight).build());
    }

    @Override
    protected void renderWindowForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWindowForeground(graphics, mouseX, mouseY, partialTicks);
        this.f_169369_.forEach(w -> w.render(graphics, mouseX, mouseY, partialTicks));
        if (this.parent instanceof TitleScreen) {
            if (mouseX < this.gettingStarted.m_252754_() || mouseX > this.gettingStarted.m_252754_() + 98) {
                return;
            }
            if (mouseY < this.gettingStarted.m_252907_() || mouseY > this.gettingStarted.m_252907_() + 20) {
                return;
            }
            graphics.renderComponentTooltip(this.f_96547_, TooltipHelper.cutTextComponent(Lang.translateDirect("menu.only_ingame"), TooltipHelper.Palette.ALL_GRAY), mouseX, mouseY);
        }
    }

    private void linkTo(Screen screen) {
        this.returnOnClose = false;
        ScreenOpener.open(screen);
    }

    private void linkTo(String url) {
        this.returnOnClose = false;
        ScreenOpener.open(new ConfirmLinkScreen(p_213069_2_ -> {
            if (p_213069_2_) {
                Util.getPlatform().openUri(url);
            }
            this.f_96541_.setScreen(this);
        }, url, true));
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    protected static class PlatformIconButton extends Button {

        protected final AllGuiTextures icon;

        protected final float scale;

        public PlatformIconButton(int pX, int pY, int pWidth, int pHeight, AllGuiTextures icon, float scale, Button.OnPress pOnPress, Tooltip tooltip) {
            super(pX, pY, pWidth, pHeight, Components.immutableEmpty(), pOnPress, f_252438_);
            this.icon = icon;
            this.scale = scale;
            this.m_257544_(tooltip);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pt) {
            super.m_87963_(graphics, pMouseX, pMouseY, pt);
            PoseStack pPoseStack = graphics.pose();
            pPoseStack.pushPose();
            pPoseStack.translate((float) (this.m_252754_() + this.f_93618_ / 2) - (float) this.icon.width * this.scale / 2.0F, (float) (this.m_252907_() + this.f_93619_ / 2) - (float) this.icon.height * this.scale / 2.0F, 0.0F);
            pPoseStack.scale(this.scale, this.scale, 1.0F);
            this.icon.render(graphics, 0, 0);
            pPoseStack.popPose();
        }
    }
}