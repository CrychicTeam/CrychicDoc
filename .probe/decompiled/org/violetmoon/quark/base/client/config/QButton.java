package org.violetmoon.quark.base.client.config;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.client.handler.ClientUtil;
import org.violetmoon.quark.base.config.QuarkGeneralConfig;
import org.violetmoon.quark.base.handler.ContributorRewardHandler;

public class QButton extends Button {

    private static final int ORANGE = 1;

    private static final int PURPLE = 2;

    private static final int RAINBOW = 3;

    private static final int QUARK = 4;

    private static final List<QButton.Celebration> CELEBRATIONS = new ArrayList();

    private final boolean gay;

    private QButton.Celebration celebrating;

    private boolean showBubble;

    private static void celebrate(String name, int day, Month month, int tier) {
        celebrate(name, day, day, month, tier);
    }

    private static void celebrate(String name, int day, int end, Month month, int tier) {
        CELEBRATIONS.add(new QButton.Celebration(day, month.getValue(), end - day, tier, name));
    }

    public QButton(int x, int y) {
        super(new Button.Builder(Component.literal("q"), QButton::click).size(20, 20).pos(x, y));
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        this.gay = month == 6;
        for (QButton.Celebration c : CELEBRATIONS) {
            if (c.running(day, month)) {
                this.celebrating = c;
                this.m_257544_(Tooltip.create(Component.translatable("quark.gui.celebration." + this.celebrating.name)));
                break;
            }
        }
        this.showBubble = !getQuarkMarkerFile().exists();
    }

    public int getFGColor() {
        return this.gay ? Color.HSBtoRGB(QuarkClient.ticker.total / 200.0F, 1.0F, 1.0F) : 4775356;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.m_87963_(guiGraphics, mouseX, mouseY, partialTicks);
        int iconIndex = Math.min(4, ContributorRewardHandler.localPatronTier);
        if (this.celebrating != null) {
            iconIndex = this.celebrating.tier;
        }
        if (iconIndex > 0) {
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
            int rx = this.m_252754_() - 2;
            int ry = this.m_252907_() - 2;
            int w = 9;
            int h = 9;
            int v = 26;
            if (this.celebrating != null) {
                rx -= 3;
                ry -= 2;
                w = 10;
                h = 10;
                v = 44;
            }
            int u = 256 - iconIndex * w;
            guiGraphics.blit(ClientUtil.GENERAL_ICONS, rx, ry, u, v, w, h);
        }
        if (this.showBubble && QuarkGeneralConfig.enableOnboarding) {
            Font font = Minecraft.getInstance().font;
            int cy = this.m_252907_() - 2;
            if (QuarkClient.ticker.total % 20.0F > 10.0F) {
                cy++;
            }
            ClientUtil.drawChatBubble(guiGraphics, this.m_252754_() + 16, cy, font, I18n.get("quark.misc.configure_quark_here"), this.f_93625_, true);
        }
    }

    private static File getQuarkMarkerFile() {
        return new File(Minecraft.getInstance().gameDirectory, ".qmenu_opened.marker");
    }

    public static void click(Button b) {
        if (b instanceof QButton qb && qb.showBubble) {
            try {
                getQuarkMarkerFile().createNewFile();
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }
        Minecraft.getInstance().setScreen(new QuarkConfigHomeScreen(Minecraft.getInstance().screen));
    }

    static {
        celebrate("quark", 21, Month.MARCH, 4);
        celebrate("vm", 29, Month.APRIL, 2);
        celebrate("minecraft", 18, Month.NOVEMBER, 1);
        celebrate("vns", 9, Month.APRIL, 1);
        celebrate("vazkii", 22, Month.NOVEMBER, 1);
        celebrate("wire", 23, Month.SEPTEMBER, 1);
        celebrate("anb", 6, Month.JUNE, 1);
        celebrate("kame", 5, Month.NOVEMBER, 1);
        celebrate("adrian", 4, Month.MAY, 1);
        celebrate("train", 16, Month.AUGUST, 1);
        celebrate("zemmy", 9, Month.JUNE, 1);
        celebrate("mat", 7, Month.FEBRUARY, 1);
        celebrate("iad", 6, Month.APRIL, 3);
        celebrate("iad2", 26, Month.OCTOBER, 3);
        celebrate("idr", 8, Month.NOVEMBER, 3);
        celebrate("ld", 8, Month.OCTOBER, 3);
        celebrate("lvd", 26, Month.APRIL, 3);
        celebrate("ncod", 11, Month.OCTOBER, 3);
        celebrate("nbpd", 14, Month.JULY, 3);
        celebrate("ppad", 24, Month.MAY, 3);
        celebrate("tdr", 20, Month.NOVEMBER, 3);
        celebrate("tdv", 31, Month.MARCH, 3);
        celebrate("zdd", 1, Month.MARCH, 3);
        celebrate("afd", 1, Month.APRIL, 4);
        celebrate("wwd", 3, Month.MARCH, 2);
        celebrate("hw", 31, Month.OCTOBER, 1);
        celebrate("xmas", 25, Month.DECEMBER, 2);
        celebrate("iwd", 8, Month.MARCH, 2);
        celebrate("wpld", 5, Month.MAY, 2);
        celebrate("iyd", 12, Month.AUGUST, 2);
        celebrate("hrd", 9, Month.DECEMBER, 2);
        celebrate("ny", 1, 3, Month.JANUARY, 2);
        celebrate("edballs", 28, Month.APRIL, 1);
        celebrate("doyouremember", 21, Month.SEPTEMBER, 1);
        celebrate("pm", 1, 30, Month.JUNE, 3);
        celebrate("baw", 16, 22, Month.SEPTEMBER, 3);
        celebrate("taw", 13, 19, Month.NOVEMBER, 3);
    }

    private static record Celebration(int day, int month, int len, int tier, String name) {

        public boolean running(int day, int month) {
            return this.month == month && this.day >= day && this.day <= day + this.len;
        }
    }
}