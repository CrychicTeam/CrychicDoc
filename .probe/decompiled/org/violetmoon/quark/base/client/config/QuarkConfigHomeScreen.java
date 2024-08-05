package org.violetmoon.quark.base.client.config;

import java.util.Iterator;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.config.QuarkGeneralConfig;
import org.violetmoon.quark.base.handler.ContributorRewardHandler;
import org.violetmoon.zeta.client.config.screen.ZetaConfigHomeScreen;

public class QuarkConfigHomeScreen extends ZetaConfigHomeScreen {

    private static final CubeMap CUBE_MAP = new CubeMap(new ResourceLocation("quark", "textures/misc/panorama/panorama"));

    private static final PanoramaRenderer PANORAMA = new PanoramaRenderer(CUBE_MAP);

    private float time;

    protected float partialTicks;

    public QuarkConfigHomeScreen(Screen parent) {
        super(QuarkClient.ZETA_CLIENT, parent);
    }

    @Override
    protected void init() {
        super.init();
        List<Integer> socialButtonPlacements = this.centeredRow(this.f_96543_ / 2, 20, 5, 5);
        Iterator<Integer> iter = socialButtonPlacements.iterator();
        this.m_142416_(new SocialButton((Integer) iter.next(), this.f_96544_ - 55, Component.translatable("quark.gui.config.social.website"), 4775356, 0, "https://quarkmod.net"));
        this.m_142416_(new SocialButton((Integer) iter.next(), this.f_96544_ - 55, Component.translatable("quark.gui.config.social.discord"), 7506394, 1, "https://discord.gg/vm"));
        this.m_142416_(new SocialButton((Integer) iter.next(), this.f_96544_ - 55, Component.translatable("quark.gui.config.social.patreon"), 16345172, 2, "https://patreon.com/vazkii"));
        this.m_142416_(new SocialButton((Integer) iter.next(), this.f_96544_ - 55, Component.translatable("quark.gui.config.social.forum"), 11948248, 3, "https://forum.violetmoon.org"));
        this.m_142416_(new SocialButton((Integer) iter.next(), this.f_96544_ - 55, Component.translatable("quark.gui.config.social.twitter"), 1942002, 4, "https://twitter.com/VazkiiMods"));
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        this.time = this.time + this.partialTicks;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            float spin = this.partialTicks * 2.0F;
            float blur = 0.85F;
            if (this.time < 20.0F && !QuarkGeneralConfig.disableQMenuEffects) {
                spin += 20.0F - this.time;
                blur = this.time / 20.0F * 0.75F + 0.1F;
            }
            PANORAMA.render(spin, blur);
        } else {
            super.m_280273_(guiGraphics);
        }
        int boxWidth = 400;
        guiGraphics.fill(this.f_96543_ / 2 - boxWidth / 2, 0, this.f_96543_ / 2 + boxWidth / 2, this.f_96544_, 1711276032);
        guiGraphics.fill(this.f_96543_ / 2 - boxWidth / 2 - 1, 0, this.f_96543_ / 2 - boxWidth / 2, this.f_96544_, 1721342361);
        guiGraphics.fill(this.f_96543_ / 2 + boxWidth / 2, 0, this.f_96543_ / 2 + boxWidth / 2 + 1, this.f_96544_, 1721342361);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.partialTicks = partialTicks;
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.drawCenteredString(this.f_96547_, I18n.get("quark.gui.config.subheader1", ChatFormatting.LIGHT_PURPLE, ContributorRewardHandler.featuredPatron, ChatFormatting.RESET), this.f_96543_ / 2, 28, 10420222);
        guiGraphics.drawCenteredString(this.f_96547_, I18n.get("quark.gui.config.subheader2"), this.f_96543_ / 2, 38, 10420222);
    }
}