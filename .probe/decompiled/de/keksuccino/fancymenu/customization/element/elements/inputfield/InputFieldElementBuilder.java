package de.keksuccino.fancymenu.customization.element.elements.inputfield;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.customization.variables.Variable;
import de.keksuccino.fancymenu.customization.variables.VariableHandler;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.ui.widget.editbox.ExtendedEditBox;
import de.keksuccino.konkrete.math.MathUtils;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputFieldElementBuilder extends ElementBuilder<InputFieldElement, InputFieldEditorElement> {

    public InputFieldElementBuilder() {
        super("fancymenu_customization_item_input_field");
    }

    @NotNull
    public InputFieldElement buildDefaultInstance() {
        InputFieldElement e = new InputFieldElement(this);
        e.baseWidth = 100;
        e.baseHeight = 20;
        e.editBox = new ExtendedEditBox(Minecraft.getInstance().font, e.getAbsoluteX(), e.getAbsoluteY(), e.getAbsoluteWidth(), e.getAbsoluteHeight(), Component.empty());
        e.editBox.setCharacterFilter(e.type.filter);
        return e;
    }

    @NotNull
    public InputFieldElement deserializeElement(@NotNull SerializedElement serialized) {
        InputFieldElement element = this.buildDefaultInstance();
        element.linkedVariable = serialized.getValue("linked_variable");
        String inputFieldTypeString = serialized.getValue("input_field_type");
        if (inputFieldTypeString != null) {
            InputFieldElement.InputFieldType t = InputFieldElement.InputFieldType.getByName(inputFieldTypeString);
            if (t != null) {
                element.type = t;
            }
        }
        String maxLengthString = serialized.getValue("max_text_length");
        if (maxLengthString != null && MathUtils.isInteger(maxLengthString)) {
            element.maxTextLength = Integer.parseInt(maxLengthString);
        }
        if (element.maxTextLength <= 0) {
            element.maxTextLength = 1;
        }
        element.editBox = new ExtendedEditBox(Minecraft.getInstance().font, element.getAbsoluteX(), element.getAbsoluteY(), element.getAbsoluteWidth(), element.getAbsoluteHeight(), Component.empty());
        element.editBox.setCharacterFilter(element.type.filter);
        element.editBox.m_94199_(element.maxTextLength);
        if (element.linkedVariable != null && VariableHandler.variableExists(element.linkedVariable)) {
            String var = ((Variable) Objects.requireNonNull(VariableHandler.getVariable(element.linkedVariable))).getValue();
            element.editBox.setValue(var);
        }
        element.navigatable = this.deserializeBoolean(element.navigatable, serialized.getValue("navigatable"));
        return element;
    }

    protected SerializedElement serializeElement(@NotNull InputFieldElement element, @NotNull SerializedElement serializeTo) {
        if (element.linkedVariable != null) {
            serializeTo.putProperty("linked_variable", element.linkedVariable);
        }
        serializeTo.putProperty("input_field_type", element.type.getName());
        serializeTo.putProperty("max_text_length", element.maxTextLength + "");
        serializeTo.putProperty("navigatable", element.navigatable + "");
        return serializeTo;
    }

    @NotNull
    public InputFieldEditorElement wrapIntoEditorElement(@NotNull InputFieldElement element, @NotNull LayoutEditorScreen editor) {
        return new InputFieldEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.customization.items.input_field");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return LocalizationUtils.splitLocalizedLines("fancymenu.customization.items.input_field.desc");
    }
}