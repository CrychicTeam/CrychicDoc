package de.keksuccino.fancymenu.customization.loadingrequirement.requirements;

import de.keksuccino.fancymenu.customization.loadingrequirement.LoadingRequirement;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementInstance;
import de.keksuccino.fancymenu.customization.variables.Variable;
import de.keksuccino.fancymenu.customization.variables.VariableHandler;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.screen.CellScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.StringBuilderScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorFormattingRule;
import de.keksuccino.fancymenu.util.rendering.ui.widget.editbox.EditBoxSuggestions;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class IsVariableValueRequirement extends LoadingRequirement {

    public IsVariableValueRequirement() {
        super("fancymenu_visibility_requirement_is_variable_value");
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public boolean isRequirementMet(@Nullable String value) {
        if (value != null && value.contains(":")) {
            String name = value.split(":", 2)[0];
            String val = value.split(":", 2)[1];
            if (VariableHandler.variableExists(name)) {
                String storedVal = ((Variable) Objects.requireNonNull(VariableHandler.getVariable(name))).getValue();
                return val.equals(storedVal);
            }
        }
        return false;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.helper.visibilityrequirement.is_variable_value");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.helper.visibilityrequirement.is_variable_value.desc"));
    }

    @Override
    public String getCategory() {
        return null;
    }

    @Override
    public String getValueDisplayName() {
        return "";
    }

    @Override
    public String getValuePreset() {
        return "<variable_name>:<value_to_check_for>";
    }

    @Override
    public List<TextEditorFormattingRule> getValueFormattingRules() {
        return null;
    }

    @Override
    public void editValue(@NotNull Screen parentScreen, @NotNull LoadingRequirementInstance requirementInstance) {
        IsVariableValueRequirement.IsVariableValueConfigScreen s = new IsVariableValueRequirement.IsVariableValueConfigScreen((String) Objects.requireNonNullElse(requirementInstance.value, ""), callback -> {
            if (callback != null) {
                requirementInstance.value = callback;
            }
            Minecraft.getInstance().setScreen(parentScreen);
        });
        Minecraft.getInstance().setScreen(s);
    }

    public static class IsVariableValueConfigScreen extends StringBuilderScreen {

        @NotNull
        protected String variableName = "";

        @NotNull
        protected String variableValue = "";

        protected CellScreen.TextInputCell nameCell;

        protected CellScreen.TextInputCell valueCell;

        protected EditBoxSuggestions variableNameSuggestions;

        protected IsVariableValueConfigScreen(String value, @NotNull Consumer<String> callback) {
            super(Component.translatable("fancymenu.helper.visibilityrequirement.is_variable_value.value.desc"), callback);
            if (value == null) {
                value = "";
            }
            if (value.contains(":")) {
                this.variableName = value.split(":", 2)[0];
                this.variableValue = value.split(":", 2)[1];
            }
        }

        @Override
        protected void initCells() {
            this.addSpacerCell(20);
            String name = this.getVarNameString();
            this.addLabelCell(Component.translatable("fancymenu.loading_requirements.is_variable_value.var_name"));
            this.nameCell = this.addTextInputCell(null, true, true).setText(name);
            this.addCellGroupEndSpacerCell();
            this.variableNameSuggestions = EditBoxSuggestions.createWithCustomSuggestions(this, this.nameCell.editBox, EditBoxSuggestions.SuggestionsRenderPosition.ABOVE_EDIT_BOX, VariableHandler.getVariableNames());
            UIBase.applyDefaultWidgetSkinTo(this.variableNameSuggestions);
            this.nameCell.editBox.m_94151_(s -> this.variableNameSuggestions.updateCommandInfo());
            this.addCellGroupEndSpacerCell();
            String value = this.getVarValueString();
            this.addLabelCell(Component.translatable("fancymenu.loading_requirements.is_variable_value.var_value"));
            this.valueCell = this.addTextInputCell(null, true, true).setText(value);
            this.addSpacerCell(20);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            super.m_88315_(graphics, mouseX, mouseY, partial);
            this.variableNameSuggestions.render(graphics, mouseX, mouseY);
        }

        @Override
        public boolean keyPressed(int $$0, int $$1, int $$2) {
            return this.variableNameSuggestions.keyPressed($$0, $$1, $$2) ? true : super.m_7933_($$0, $$1, $$2);
        }

        @Override
        public boolean mouseScrolled(double $$0, double $$1, double $$2) {
            return this.variableNameSuggestions.mouseScrolled($$2) ? true : super.m_6050_($$0, $$1, $$2);
        }

        @Override
        public boolean mouseClicked(double $$0, double $$1, int $$2) {
            return this.variableNameSuggestions.mouseClicked($$0, $$1, $$2) ? true : super.m_6375_($$0, $$1, $$2);
        }

        @NotNull
        @Override
        public String buildString() {
            return this.getVarNameString() + ":" + this.getVarValueString();
        }

        @NotNull
        protected String getVarNameString() {
            return this.nameCell != null ? this.nameCell.getText() : this.variableName;
        }

        @NotNull
        protected String getVarValueString() {
            return this.valueCell != null ? this.valueCell.getText() : this.variableValue;
        }
    }
}