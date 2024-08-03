package harmonised.pmmo.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import harmonised.pmmo.client.events.ClientTickHandler;
import harmonised.pmmo.client.utils.DP;
import harmonised.pmmo.client.utils.DataMirror;
import harmonised.pmmo.client.utils.VeinTracker;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.SkillsConfig;
import harmonised.pmmo.config.codecs.SkillData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.CoreUtils;
import harmonised.pmmo.features.veinmining.VeinMiningLogic;
import harmonised.pmmo.setup.datagen.LangProvider;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.LogicalSide;

public class XPOverlayGUI implements IGuiOverlay {

    private Core core = Core.get(LogicalSide.CLIENT);

    private int skillGap = 0;

    private Minecraft mc;

    private Font fontRenderer;

    private Map<String, Double> modifiers = new HashMap();

    private List<String> skillsKeys = new ArrayList();

    private LinkedHashMap<String, XPOverlayGUI.SkillLine> lineRenderers = new LinkedHashMap();

    private int maxCharge = 0;

    private int currentCharge = 0;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (this.mc == null) {
            this.mc = Minecraft.getInstance();
        }
        if (this.fontRenderer == null) {
            this.fontRenderer = this.mc.font;
        }
        if (!this.mc.options.renderDebug) {
            guiGraphics.pose().pushPose();
            RenderSystem.enableBlend();
            if (Config.SKILL_LIST_DISPLAY.get()) {
                this.renderSkillList(guiGraphics, Config.SKILL_LIST_OFFSET_X.get(), Config.SKILL_LIST_OFFSET_Y.get());
            }
            if (Config.VEIN_ENABLED.get() && Config.VEIN_GAUGE_DISPLAY.get()) {
                this.renderVeinGauge(guiGraphics, Config.VEIN_GAUGE_OFFSET_X.get(), Config.VEIN_GAUGE_OFFSET_Y.get());
            }
            if (Config.GAIN_LIST_DISPLAY.get()) {
                if (ClientTickHandler.xpGains.size() >= 1 && ((ClientTickHandler.GainEntry) ClientTickHandler.xpGains.get(0)).duration <= 0) {
                    ClientTickHandler.xpGains.remove(0);
                }
                this.renderGains(guiGraphics, Config.GAIN_LIST_OFFSET_X.get(), Config.GAIN_LIST_OFFSET_Y.get());
            }
            guiGraphics.pose().popPose();
        }
        if (ClientTickHandler.isRefreshTick()) {
            ClientTickHandler.resetTicks();
        }
    }

    private void renderSkillList(GuiGraphics graphics, double skillListX, double skillListY) {
        int renderX = (int) ((double) this.mc.getWindow().getGuiScaledWidth() * skillListX);
        int renderY = (int) ((double) this.mc.getWindow().getGuiScaledHeight() * skillListY);
        if (ClientTickHandler.isRefreshTick()) {
            this.modifiers = this.core.getConsolidatedModifierMap(this.mc.player);
            this.skillsKeys = this.core.getData().getXpMap(null).keySet().stream().sorted(Comparator.comparingLong(a -> this.core.getData().getXpRaw(null, a)).reversed()).toList();
            LinkedHashMap<String, XPOverlayGUI.SkillLine> holderMap = this.lineRenderers;
            this.lineRenderers.clear();
            AtomicInteger yOffset = new AtomicInteger(0);
            this.skillGap = (Integer) this.skillsKeys.stream().map(skill -> this.fontRenderer.width(Component.translatable("pmmo." + skill).getString())).max(Comparator.comparingInt(t -> t)).orElse(0);
            this.skillsKeys.forEach(skillKey -> {
                long xpRaw = this.core.getData().getXpRaw(null, skillKey);
                this.lineRenderers.put(skillKey, xpRaw != ((XPOverlayGUI.SkillLine) holderMap.getOrDefault(skillKey, XPOverlayGUI.SkillLine.DEFAULT)).xpValue() ? new XPOverlayGUI.SkillLine(skillKey, (Double) this.modifiers.getOrDefault(skillKey, 1.0), xpRaw, yOffset.get(), this.skillGap) : new XPOverlayGUI.SkillLine((XPOverlayGUI.SkillLine) holderMap.get(skillKey), yOffset.get()));
                yOffset.getAndIncrement();
            });
        }
        this.lineRenderers.forEach((skill, line) -> line.render(graphics, renderX, renderY, this.fontRenderer));
    }

    private void renderVeinGauge(GuiGraphics graphics, double gaugeX, double gaugeY) {
        int renderX = (int) ((double) this.mc.getWindow().getGuiScaledWidth() * gaugeX);
        int renderY = (int) ((double) this.mc.getWindow().getGuiScaledHeight() * gaugeY);
        if (ClientTickHandler.isRefreshTick()) {
            this.maxCharge = VeinMiningLogic.getMaxChargeFromAllItems(this.mc.player);
            if (this.maxCharge > 0) {
                this.currentCharge = VeinTracker.getCurrentCharge();
            }
        }
        if (this.currentCharge > 0) {
            graphics.drawString(this.fontRenderer, LangProvider.VEIN_LIMIT.asComponent(Config.VEIN_LIMIT.get()), renderX, renderY - 11, 16777215);
            graphics.drawString(this.fontRenderer, LangProvider.VEIN_CHARGE.asComponent(this.currentCharge, this.maxCharge), renderX, renderY, 16777215);
        }
    }

    private void renderGains(GuiGraphics graphics, double listX, double listY) {
        int renderX = (int) ((double) this.mc.getWindow().getGuiScaledWidth() * listX);
        int renderY = (int) ((double) this.mc.getWindow().getGuiScaledHeight() * listY);
        for (int i = 0; i < ClientTickHandler.xpGains.size(); i++) {
            ClientTickHandler.GainEntry entry = (ClientTickHandler.GainEntry) ClientTickHandler.xpGains.get(i);
            graphics.drawString(this.fontRenderer, entry.display(), renderX, 3 + renderY + i * 9, entry.display().getStyle().getColor().getValue());
        }
    }

    private static record SkillLine(String xpRaw, MutableComponent skillName, String bonusLine, long xpValue, int color, int yOffset, int skillGap) {

        public static XPOverlayGUI.SkillLine DEFAULT = new XPOverlayGUI.SkillLine("", Component.literal(""), "", -1L, 16777215, 0, 0);

        public SkillLine(String skillName, double bonus, long xpValue, int yOffset, int skillGap) {
            this(rawXpLine(xpValue, skillName), Component.translatable("pmmo." + skillName), bonusLine(bonus), xpValue, CoreUtils.getSkillColor(skillName), yOffset * 9, skillGap);
        }

        public SkillLine(XPOverlayGUI.SkillLine src, int yOffset) {
            this(src.xpRaw(), src.skillName(), src.bonusLine(), src.xpValue(), src.color, yOffset * 9, src.skillGap());
        }

        private static String rawXpLine(long xpValue, String skillKey) {
            double level = ((DataMirror) Core.get(LogicalSide.CLIENT).getData()).getXpWithPercentToNextLevel(xpValue);
            int skillMaxLevel = ((SkillData) SkillsConfig.SKILLS.get().getOrDefault(skillKey, SkillData.Builder.getDefault())).getMaxLevel();
            level = level > (double) skillMaxLevel ? (double) skillMaxLevel : level;
            return level >= (double) Config.MAX_LEVEL.get().intValue() ? Config.MAX_LEVEL.get() + "" : DP.dp(Math.floor(level * 100.0) / 100.0);
        }

        private static String bonusLine(double bonus) {
            if (bonus != 1.0) {
                bonus = (Math.max(0.0, bonus) - 1.0) * 100.0;
                return (bonus >= 0.0 ? "+" : "-") + DP.dp(bonus) + "%";
            } else {
                return "";
            }
        }

        public void render(GuiGraphics graphics, int skillListX, int skillListY, Font fontRenderer) {
            int levelGap = fontRenderer.width(this.xpRaw());
            graphics.drawString(fontRenderer, this.xpRaw(), skillListX, skillListY + 3 + this.yOffset(), this.color());
            graphics.drawString(fontRenderer, " | " + this.skillName.getString(), skillListX + levelGap, skillListY + 3 + this.yOffset(), this.color());
            graphics.drawString(fontRenderer, " | " + DP.dprefix(this.xpValue()), skillListX + levelGap + this.skillGap() + 9, skillListY + 3 + this.yOffset(), this.color());
            graphics.drawString(fontRenderer, this.bonusLine, skillListX + levelGap + this.skillGap() + 46, skillListY + 3 + this.yOffset(), this.color());
        }
    }
}