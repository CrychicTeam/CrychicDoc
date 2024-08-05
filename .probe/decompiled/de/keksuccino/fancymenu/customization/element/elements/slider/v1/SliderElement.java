package de.keksuccino.fancymenu.customization.element.elements.slider.v1;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.variables.Variable;
import de.keksuccino.fancymenu.customization.variables.VariableHandler;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinAbstractWidget;
import de.keksuccino.fancymenu.util.rendering.ui.widget.slider.v1.ExtendedSliderButton;
import de.keksuccino.fancymenu.util.rendering.ui.widget.slider.v1.ListSliderButton;
import de.keksuccino.fancymenu.util.rendering.ui.widget.slider.v1.RangeSliderButton;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.math.MathUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class SliderElement extends AbstractElement {

    public String linkedVariable;

    public SliderElement.SliderType type = SliderElement.SliderType.RANGE;

    public List<String> listValues = new ArrayList();

    public int minRangeValue = 1;

    public int maxRangeValue = 10;

    public String labelPrefix;

    public String labelSuffix;

    public ExtendedSliderButton slider;

    public SliderElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    public void initializeSlider() {
        String valString = null;
        if (this.linkedVariable != null && VariableHandler.variableExists(this.linkedVariable)) {
            valString = ((Variable) Objects.requireNonNull(VariableHandler.getVariable(this.linkedVariable))).getValue();
        }
        if (this.type == SliderElement.SliderType.RANGE) {
            int selectedRangeValue = this.minRangeValue;
            if (valString != null && MathUtils.isInteger(valString)) {
                selectedRangeValue = Integer.parseInt(valString);
            }
            this.slider = new RangeSliderButton(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteWidth(), this.getAbsoluteHeight(), true, (double) this.minRangeValue, (double) this.maxRangeValue, (double) selectedRangeValue, apply -> {
                if (this.linkedVariable != null) {
                    VariableHandler.setVariable(this.linkedVariable, ((RangeSliderButton) apply).getSelectedRangeValue() + "");
                }
            });
        }
        if (this.type == SliderElement.SliderType.LIST) {
            int selectedIndex = 0;
            if (valString != null) {
                int i = 0;
                for (String s : this.listValues) {
                    if (s.equals(valString)) {
                        selectedIndex = i;
                        break;
                    }
                    i++;
                }
            }
            this.slider = new ListSliderButton(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteWidth(), this.getAbsoluteHeight(), true, this.listValues, (double) selectedIndex, apply -> {
                if (this.linkedVariable != null) {
                    VariableHandler.setVariable(this.linkedVariable, ((ListSliderButton) apply).getSelectedListValue());
                }
            });
        }
        if (this.slider != null) {
            this.slider.setLabelPrefix(this.labelPrefix);
            this.slider.setLabelSuffix(this.labelSuffix);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.shouldRender()) {
            RenderSystem.enableBlend();
            if (isEditor()) {
                this.slider.f_93623_ = false;
            }
            this.slider.m_252865_(this.getAbsoluteX());
            this.slider.m_253211_(this.getAbsoluteY());
            this.slider.m_93674_(this.getAbsoluteWidth());
            ((IMixinAbstractWidget) this.slider).setHeightFancyMenu(this.getAbsoluteHeight());
            this.slider.render(graphics, MouseInput.getMouseX(), MouseInput.getMouseY(), Minecraft.getInstance().getDeltaFrameTime());
            if (this.linkedVariable != null && VariableHandler.variableExists(this.linkedVariable)) {
                String valString = ((Variable) Objects.requireNonNull(VariableHandler.getVariable(this.linkedVariable))).getValue();
                if (this.type == SliderElement.SliderType.RANGE && MathUtils.isInteger(valString)) {
                    int val = Integer.parseInt(valString);
                    if (((RangeSliderButton) this.slider).getSelectedRangeValue() != val) {
                        ((RangeSliderButton) this.slider).setSelectedRangeValue((double) val);
                    }
                }
                if (this.type == SliderElement.SliderType.LIST && !((ListSliderButton) this.slider).getSelectedListValue().equals(valString)) {
                    int newIndex = 0;
                    int i = 0;
                    for (String s : this.listValues) {
                        if (s.equals(valString)) {
                            newIndex = i;
                            break;
                        }
                        i++;
                    }
                    ((ListSliderButton) this.slider).setSelectedIndex((double) newIndex);
                }
            }
            if (this.type == SliderElement.SliderType.RANGE) {
                ((RangeSliderButton) this.slider).maxValue = (double) this.maxRangeValue;
                ((RangeSliderButton) this.slider).minValue = (double) this.minRangeValue;
            }
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public static enum SliderType {

        LIST("list"), RANGE("range");

        final String name;

        private SliderType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static SliderElement.SliderType getByName(String name) {
            for (SliderElement.SliderType i : values()) {
                if (i.getName().equals(name)) {
                    return i;
                }
            }
            return null;
        }
    }
}