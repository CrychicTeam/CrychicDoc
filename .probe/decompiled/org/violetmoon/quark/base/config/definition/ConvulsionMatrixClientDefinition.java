package org.violetmoon.quark.base.config.definition;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.config.type.ConvulsionMatrixConfig;
import org.violetmoon.zeta.client.ZetaClient;
import org.violetmoon.zeta.client.config.definition.ClientDefinitionExt;
import org.violetmoon.zeta.client.config.screen.AbstractSectionInputScreen;
import org.violetmoon.zeta.client.config.widget.PencilButton;
import org.violetmoon.zeta.config.ChangeSet;
import org.violetmoon.zeta.config.SectionDefinition;
import org.violetmoon.zeta.config.ValueDefinition;

public class ConvulsionMatrixClientDefinition implements ClientDefinitionExt<SectionDefinition> {

    private final ConvulsionMatrixConfig.Params params;

    private final ValueDefinition<List<Double>> r;

    private final ValueDefinition<List<Double>> g;

    private final ValueDefinition<List<Double>> b;

    public ConvulsionMatrixClientDefinition(ConvulsionMatrixConfig cfg, SectionDefinition def) {
        this.params = cfg.params;
        this.r = def.getValueErased("R", List.class);
        this.g = def.getValueErased("G", List.class);
        this.b = def.getValueErased("B", List.class);
        Preconditions.checkNotNull(this.r, "need an 'R' value in this section");
        Preconditions.checkNotNull(this.g, "need an 'G' value in this section");
        Preconditions.checkNotNull(this.b, "need an 'B' value in this section");
    }

    public String getSubtitle(ChangeSet changes, SectionDefinition def) {
        List<Double> r_ = changes.getExactSizeCopy(this.r, 3, 0.0);
        List<Double> g_ = changes.getExactSizeCopy(this.g, 3, 0.0);
        List<Double> b_ = changes.getExactSizeCopy(this.b, 3, 0.0);
        return (String) Stream.of(r_, g_, b_).flatMap(Collection::stream).map(d -> String.format("%.1f", d)).collect(Collectors.joining(", ", "[", "]"));
    }

    public void addWidgets(ZetaClient zc, Screen parent, ChangeSet changes, SectionDefinition def, Consumer<AbstractWidget> widgets) {
        Screen newScreen = new ConvulsionMatrixClientDefinition.ConvulsionMatrixInputScreen(zc, parent, changes, def);
        widgets.accept(new PencilButton(zc, 230, 3, b1 -> Minecraft.getInstance().setScreen(newScreen)));
    }

    class ConvulsionMatrixInputScreen extends AbstractSectionInputScreen {

        ForgeSlider[] sliders = new ForgeSlider[9];

        private static final Component EMPTY = Component.empty();

        public ConvulsionMatrixInputScreen(ZetaClient zc, Screen parent, ChangeSet changes, SectionDefinition def) {
            super(zc, parent, changes, def);
        }

        protected List<Double> with(ValueDefinition<List<Double>> def, int index, double value) {
            List<Double> copy = this.changes.getExactSizeCopy(def, 3, 0.0);
            copy.set(index, value);
            return copy;
        }

        @Override
        protected void init() {
            super.init();
            int w = 70;
            int p = 12;
            int x = this.f_96543_ / 2 - 33;
            int y = 55;
            this.sliders[0] = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x + w * 0, y + 0, w - p, 20, ConvulsionMatrixClientDefinition.this.r, 0));
            this.sliders[1] = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x + w * 1, y + 0, w - p, 20, ConvulsionMatrixClientDefinition.this.r, 1));
            this.sliders[2] = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x + w * 2, y + 0, w - p, 20, ConvulsionMatrixClientDefinition.this.r, 2));
            this.sliders[3] = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x + w * 0, y + 25, w - p, 20, ConvulsionMatrixClientDefinition.this.g, 0));
            this.sliders[4] = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x + w * 1, y + 25, w - p, 20, ConvulsionMatrixClientDefinition.this.g, 1));
            this.sliders[5] = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x + w * 2, y + 25, w - p, 20, ConvulsionMatrixClientDefinition.this.g, 2));
            this.sliders[6] = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x + w * 0, y + 50, w - p, 20, ConvulsionMatrixClientDefinition.this.b, 0));
            this.sliders[7] = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x + w * 1, y + 50, w - p, 20, ConvulsionMatrixClientDefinition.this.b, 1));
            this.sliders[8] = (ForgeSlider) this.m_142416_(this.makeSliderPlease(x + w * 2, y + 50, w - p, 20, ConvulsionMatrixClientDefinition.this.b, 2));
            int i = 0;
            for (Entry<String, double[]> entry : ConvulsionMatrixClientDefinition.this.params.presetMap.entrySet()) {
                String name = (String) entry.getKey();
                double[] preset = (double[]) entry.getValue();
                this.m_142416_(new Button.Builder(Component.literal(name), __ -> this.setFromArray(preset)).size(w - p, 20).pos(x + w * i, y + 115).build());
                i++;
            }
            this.forceUpdateWidgets();
        }

        @Override
        protected void forceUpdateWidgets() {
            this.setFromList(Stream.of(this.changes.getExactSizeCopy(ConvulsionMatrixClientDefinition.this.r, 3, 0.0), this.changes.getExactSizeCopy(ConvulsionMatrixClientDefinition.this.g, 3, 0.0), this.changes.getExactSizeCopy(ConvulsionMatrixClientDefinition.this.b, 3, 0.0)).flatMap(Collection::stream).toList());
        }

        private ForgeSlider makeSliderPlease(final int x, final int y, int width, int height, final ValueDefinition<List<Double>> binding, final int bindingIndex) {
            return new ForgeSlider(x, y, width, height, EMPTY, EMPTY, 0.0, 2.0, 0.0, 0.0, 1, false) {

                @Override
                public void setValue(double value) {
                    super.setValue(value);
                    ConvulsionMatrixInputScreen.this.changes.set(binding, ConvulsionMatrixInputScreen.this.with(binding, bindingIndex, value));
                }

                @Override
                protected void applyValue() {
                    this.setValue(ConvulsionMatrixInputScreen.this.snap(this));
                }

                @Override
                public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
                    Minecraft mc = Minecraft.getInstance();
                    super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
                    String displayVal = String.format("%.2f", this.getValue());
                    int valueColor = ConvulsionMatrixInputScreen.this.changes.isDirty(binding) ? ChatFormatting.GOLD.getColor() : 16777215;
                    guiGraphics.drawString(mc.font, displayVal, x + (this.m_5711_() / 2 - ConvulsionMatrixInputScreen.this.f_96547_.width(displayVal) / 2), y + 6, valueColor);
                }
            };
        }

        @Override
        public void tick() {
            this.updateButtonStatus(true);
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            PoseStack mstack = guiGraphics.pose();
            this.m_280273_(guiGraphics);
            super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
            int x = this.f_96543_ / 2 - 203;
            int y = 10;
            int size = 60;
            int titleLeft = this.f_96543_ / 2 + 66;
            guiGraphics.drawCenteredString(this.f_96547_, Component.literal(ConvulsionMatrixClientDefinition.this.params.name).withStyle(ChatFormatting.BOLD), titleLeft, 20, 16777215);
            guiGraphics.drawCenteredString(this.f_96547_, Component.literal("Presets"), titleLeft, 155, 16777215);
            int sliders = 0;
            for (Renderable renderable : this.f_169369_) {
                if (renderable instanceof ForgeSlider) {
                    ForgeSlider s = (ForgeSlider) renderable;
                    switch(sliders) {
                        case 0:
                            guiGraphics.drawString(this.f_96547_, "R =", s.m_252754_() - 20, s.m_252907_() + 5, 16711680);
                            guiGraphics.drawString(this.f_96547_, "R", s.m_252754_() + (s.m_5711_() / 2 - 2), s.m_252907_() - 12, 16711680);
                            break;
                        case 1:
                            guiGraphics.drawString(this.f_96547_, "G", s.m_252754_() + (s.m_5711_() / 2 - 2), s.m_252907_() - 12, 65280);
                            break;
                        case 2:
                            guiGraphics.drawString(this.f_96547_, "B", s.m_252754_() + (s.m_5711_() / 2 - 2), s.m_252907_() - 12, 30719);
                            break;
                        case 3:
                            guiGraphics.drawString(this.f_96547_, "G =", s.m_252754_() - 20, s.m_252907_() + 5, 65280);
                        case 4:
                        case 5:
                        default:
                            break;
                        case 6:
                            guiGraphics.drawString(this.f_96547_, "B =", s.m_252754_() - 20, s.m_252907_() + 5, 30719);
                    }
                    if (sliders % 3 != 0) {
                        guiGraphics.drawString(this.f_96547_, "+", s.m_252754_() - 9, s.m_252907_() + 5, 16777215);
                    }
                    sliders++;
                }
            }
            String[] biomes = ConvulsionMatrixClientDefinition.this.params.biomeNames;
            int[] colors = ConvulsionMatrixClientDefinition.this.params.testColors;
            int[] folliageColors = ConvulsionMatrixClientDefinition.this.params.folliageTestColors;
            boolean renderFolliage = ConvulsionMatrixClientDefinition.this.params.shouldDisplayFolliage();
            double[] matrix = this.getToDoubleArray();
            for (int i = 0; i < biomes.length; i++) {
                String name = biomes[i];
                int color = colors[i];
                int convolved = ConvulsionMatrixConfig.convolve(matrix, color);
                int convolvedFolliage = 0;
                if (renderFolliage) {
                    int folliage = folliageColors[i];
                    convolvedFolliage = ConvulsionMatrixConfig.convolve(matrix, folliage);
                }
                int cx = x + i % 2 * (size + 5);
                int cy = y + i / 2 * (size + 5);
                guiGraphics.fill(cx - 1, cy - 1, cx + size + 1, cy + size + 1, -16777216);
                guiGraphics.fill(cx, cy, cx + size, cy + size, convolved);
                guiGraphics.fill(cx + size / 2 - 1, cy + size / 2 - 1, cx + size, cy + size, 570425344);
                if (renderFolliage) {
                    guiGraphics.fill(cx + size / 2, cy + size / 2, cx + size, cy + size, convolvedFolliage);
                }
                guiGraphics.drawString(this.f_96547_, name, cx + 2, cy + 2, 1426063360);
                if (renderFolliage) {
                    guiGraphics.renderItem(new ItemStack(Items.OAK_SAPLING), cx + size - 18, cy + size - 16);
                    mstack.pushPose();
                    mstack.translate(0.0F, 0.0F, 999.0F);
                    guiGraphics.fill(cx + size / 2, cy + size / 2, cx + size, cy + size, convolvedFolliage & 1442840575);
                    mstack.popPose();
                }
            }
        }

        protected void setFromArray(double[] values) {
            for (int i = 0; i < 9; i++) {
                this.sliders[i].setValue(values[i]);
            }
        }

        protected void setFromList(List<Double> values) {
            for (int i = 0; i < 9; i++) {
                this.sliders[i].setValue((Double) values.get(i));
            }
        }

        protected double[] getToDoubleArray() {
            double[] values = new double[9];
            for (int i = 0; i < 9; i++) {
                values[i] = this.sliders[i].getValue();
            }
            return values;
        }

        private double snap(ForgeSlider s) {
            double val = s.getValue();
            val = this.snap(val, 0.5, s);
            val = this.snap(val, 1.0, s);
            return this.snap(val, 1.5, s);
        }

        private double snap(double val, double correct, ForgeSlider s) {
            if (Math.abs(val - correct) < 0.02) {
                s.setValue(correct);
                return correct;
            } else {
                return val;
            }
        }
    }
}