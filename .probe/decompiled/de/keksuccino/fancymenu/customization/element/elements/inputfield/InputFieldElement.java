package de.keksuccino.fancymenu.customization.element.elements.inputfield;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.variables.Variable;
import de.keksuccino.fancymenu.customization.variables.VariableHandler;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinAbstractWidget;
import de.keksuccino.fancymenu.util.ListUtils;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.widget.editbox.ExtendedEditBox;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputFieldElement extends AbstractElement {

    public String linkedVariable;

    public InputFieldElement.InputFieldType type = InputFieldElement.InputFieldType.TEXT;

    public int maxTextLength = 10000;

    public ExtendedEditBox editBox;

    public String lastValue = "";

    public boolean navigatable = true;

    public InputFieldElement(ElementBuilder<InputFieldElement, InputFieldEditorElement> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.shouldRender()) {
            RenderSystem.enableBlend();
            if (isEditor()) {
                this.editBox.f_93623_ = false;
                this.editBox.m_94186_(false);
                if (this.linkedVariable != null && VariableHandler.variableExists(this.linkedVariable)) {
                    String var = ((Variable) Objects.requireNonNull(VariableHandler.getVariable(this.linkedVariable))).getValue();
                    this.editBox.setValue(var);
                }
            }
            this.editBox.setNavigatable(this.navigatable);
            this.editBox.m_252865_(this.getAbsoluteX());
            this.editBox.m_253211_(this.getAbsoluteY());
            this.editBox.m_93674_(this.getAbsoluteWidth());
            ((IMixinAbstractWidget) this.editBox).setHeightFancyMenu(this.getAbsoluteHeight());
            this.editBox.m_88315_(graphics, mouseX, mouseY, partial);
            if (!isEditor()) {
                if (this.linkedVariable != null) {
                    if (!this.lastValue.equals(this.editBox.m_94155_())) {
                        VariableHandler.setVariable(this.linkedVariable, this.editBox.m_94155_());
                    }
                    if (VariableHandler.variableExists(this.linkedVariable)) {
                        String val = ((Variable) Objects.requireNonNull(VariableHandler.getVariable(this.linkedVariable))).getValue();
                        if (!this.editBox.m_94155_().equals(val)) {
                            this.editBox.setValue(val);
                        }
                    } else {
                        this.editBox.setValue("");
                    }
                }
                this.lastValue = this.editBox.m_94155_();
            }
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    @Override
    public void tick() {
        this.editBox.m_94120_();
    }

    @Nullable
    @Override
    public List<GuiEventListener> getWidgetsToRegister() {
        return ListUtils.of(this.editBox);
    }

    public static enum InputFieldType {

        INTEGER_ONLY("integer", CharacterFilter.buildIntegerFiler()), DECIMAL_ONLY("decimal", CharacterFilter.buildDecimalFiler()), URL("url", CharacterFilter.buildUrlFilter()), TEXT("text", null);

        final String name;

        final CharacterFilter filter;

        private InputFieldType(String name, CharacterFilter filter) {
            this.name = name;
            this.filter = filter;
        }

        public String getName() {
            return this.name;
        }

        public CharacterFilter getFilter() {
            return this.filter;
        }

        public static InputFieldElement.InputFieldType getByName(String name) {
            for (InputFieldElement.InputFieldType i : values()) {
                if (i.getName().equals(name)) {
                    return i;
                }
            }
            return null;
        }
    }
}