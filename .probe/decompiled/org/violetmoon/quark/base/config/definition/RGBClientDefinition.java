package org.violetmoon.quark.base.config.definition;

import com.google.common.base.Preconditions;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.config.type.RGBAColorConfig;
import org.violetmoon.zeta.client.ZetaClient;
import org.violetmoon.zeta.client.config.definition.ClientDefinitionExt;
import org.violetmoon.zeta.client.config.screen.AbstractSectionInputScreen;
import org.violetmoon.zeta.client.config.widget.PencilButton;
import org.violetmoon.zeta.config.ChangeSet;
import org.violetmoon.zeta.config.SectionDefinition;
import org.violetmoon.zeta.config.ValueDefinition;

public class RGBClientDefinition implements ClientDefinitionExt<SectionDefinition> {

    protected final ValueDefinition<Double> r;

    protected final ValueDefinition<Double> g;

    protected final ValueDefinition<Double> b;

    @Nullable
    protected final ValueDefinition<Double> a;

    public RGBClientDefinition(SectionDefinition def) {
        this.r = def.getValue("R", Double.class);
        this.g = def.getValue("G", Double.class);
        this.b = def.getValue("B", Double.class);
        this.a = def.getValue("A", Double.class);
        Preconditions.checkNotNull(this.r, "need an 'R' value in this section");
        Preconditions.checkNotNull(this.g, "need an 'G' value in this section");
        Preconditions.checkNotNull(this.b, "need an 'B' value in this section");
    }

    public String getSubtitle(ChangeSet changes, SectionDefinition def) {
        double r = changes.get(this.r);
        double g = changes.get(this.g);
        double b = changes.get(this.b);
        if (this.a == null) {
            return String.format("[%.1f, %.1f, %.1f]", r, g, b);
        } else {
            double a = changes.get(this.a);
            return String.format("[%.1f, %.1f, %.1f, %.1f]", r, g, b, a);
        }
    }

    public void addWidgets(ZetaClient zc, Screen parent, ChangeSet changes, SectionDefinition def, Consumer<AbstractWidget> widgets) {
        Screen newScreen = new RGBClientDefinition.RGBInputScreen(zc, parent, changes, def);
        widgets.accept(new PencilButton(zc, 230, 3, b1 -> Minecraft.getInstance().setScreen(newScreen)));
    }

    class RGBInputScreen extends AbstractSectionInputScreen {

        protected ForgeSlider rslide;

        protected ForgeSlider gslide;

        protected ForgeSlider bslide;

        @Nullable
        protected ForgeSlider aslide;

        private static final Component EMPTY = Component.empty();

        public RGBInputScreen(ZetaClient zc, Screen parent, ChangeSet changes, SectionDefinition def) {
            super(zc, parent, changes, def);
        }

        @Override
        protected void init() {
            super.init();
            int w = 100;
            int p = 12;
            int x = this.f_96543_ / 2 - 110;
            int y = 55;
            this.rslide = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x, y, w - p, 20, RGBClientDefinition.this.r, "R =", 16711680));
            this.gslide = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x, y + 25, w - p, 20, RGBClientDefinition.this.g, "G = ", 65280));
            this.bslide = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x, y + 50, w - p, 20, RGBClientDefinition.this.b, "B = ", 30719));
            if (RGBClientDefinition.this.a != null) {
                this.aslide = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x, y + 75, w - p, 20, RGBClientDefinition.this.a, "A = ", 16777215));
            }
            this.forceUpdateWidgets();
        }

        @Override
        protected void forceUpdateWidgets() {
            this.rslide.setValue(this.changes.get(RGBClientDefinition.this.r));
            this.gslide.setValue(this.changes.get(RGBClientDefinition.this.g));
            this.bslide.setValue(this.changes.get(RGBClientDefinition.this.b));
            if (this.aslide != null) {
                this.aslide.setValue(this.changes.get(RGBClientDefinition.this.a));
            }
        }

        private ForgeSlider makeSliderPlease(final int x, final int y, int width, int height, final ValueDefinition<Double> binding, final String label, final int labelColor) {
            return new ForgeSlider(x, y + 50, width, height, EMPTY, EMPTY, 0.0, 1.0, 0.0, 0.0, 1, false) {

                @Override
                protected void applyValue() {
                    this.m_93611_(RGBInputScreen.this.snap(this));
                    RGBInputScreen.this.changes.set(binding, this.getValue());
                }

                @Override
                public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
                    super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
                    String displayVal = String.format("%.2f", this.getValue());
                    int valueColor = RGBInputScreen.this.changes.isDirty(binding) ? ChatFormatting.GOLD.getColor() : 16777215;
                    guiGraphics.drawString(RGBInputScreen.this.f_96547_, displayVal, (float) x + (float) (this.m_5711_() / 2 - RGBInputScreen.this.f_96547_.width(displayVal) / 2), (float) (y + 6), valueColor, true);
                    guiGraphics.drawString(RGBInputScreen.this.f_96547_, label, x - 20, y + 5, labelColor, true);
                }
            };
        }

        @Override
        public void tick() {
            this.updateButtonStatus(true);
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            this.m_280273_(guiGraphics);
            super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
            int titleLeft = this.f_96543_ / 2;
            guiGraphics.drawCenteredString(this.f_96547_, Component.literal(this.def.getTranslatedDisplayName(x$0 -> I18n.get(x$0))).withStyle(ChatFormatting.BOLD), titleLeft, 20, 16777215);
            int cx = this.f_96543_ / 2 + 20;
            int cy = 55;
            int size = 95;
            int color = RGBAColorConfig.forColor(this.rslide.getValue(), this.gslide.getValue(), this.bslide.getValue(), this.aslide == null ? 1.0 : this.aslide.getValue()).getColor();
            guiGraphics.fill(cx - 1, cy - 1, cx + size + 1, cy + size + 1, -16777216);
            guiGraphics.fill(cx, cy, cx + size, cy + size, -6710887);
            guiGraphics.fill(cx, cy, cx + size / 2, cy + size / 2, -10066330);
            guiGraphics.fill(cx + size / 2, cy + size / 2, cx + size, cy + size, -10066330);
            guiGraphics.fill(cx, cy, cx + size, cy + size, color);
        }

        private double snap(ForgeSlider s) {
            double val = s.getValue();
            val = this.snap(val, 0.0, s);
            val = this.snap(val, 0.25, s);
            val = this.snap(val, 0.5, s);
            val = this.snap(val, 0.75, s);
            return this.snap(val, 1.0, s);
        }

        private double snap(double val, double target, ForgeSlider s) {
            if (Math.abs(val - target) < 0.02) {
                s.setValue(target);
                return target;
            } else {
                return val;
            }
        }
    }
}