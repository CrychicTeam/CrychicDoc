package de.keksuccino.fancymenu.customization.loadingrequirement.requirements;

import de.keksuccino.fancymenu.customization.loadingrequirement.LoadingRequirement;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementInstance;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.screen.CellScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.StringBuilderScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorFormattingRule;
import de.keksuccino.fancymenu.util.rendering.ui.widget.editbox.EditBoxSuggestions;
import java.util.ArrayList;
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

public class IsLanguageRequirement extends LoadingRequirement {

    public IsLanguageRequirement() {
        super("fancymenu_loading_requirement_is_language");
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public boolean isRequirementMet(@Nullable String value) {
        return value != null ? Minecraft.getInstance().options.languageCode.equalsIgnoreCase(value) : false;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.helper.editor.items.visibilityrequirements.language");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.helper.editor.items.visibilityrequirements.language.desc"));
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
        return "en_us";
    }

    @Override
    public List<TextEditorFormattingRule> getValueFormattingRules() {
        return null;
    }

    @Override
    public void editValue(@NotNull Screen parentScreen, @NotNull LoadingRequirementInstance requirementInstance) {
        IsLanguageRequirement.IsLanguageValueConfigScreen s = new IsLanguageRequirement.IsLanguageValueConfigScreen((String) Objects.requireNonNullElse(requirementInstance.value, ""), callback -> {
            if (callback != null) {
                requirementInstance.value = callback;
            }
            Minecraft.getInstance().setScreen(parentScreen);
        });
        Minecraft.getInstance().setScreen(s);
    }

    public static class IsLanguageValueConfigScreen extends StringBuilderScreen {

        @NotNull
        protected String langId;

        protected CellScreen.TextInputCell langIdCell;

        protected EditBoxSuggestions langIdSuggestions;

        protected IsLanguageValueConfigScreen(@NotNull String value, @NotNull Consumer<String> callback) {
            super(Component.translatable("fancymenu.editor.elements.visibilityrequirements.edit_value"), callback);
            this.langId = value;
        }

        @Override
        protected void initCells() {
            this.addSpacerCell(20);
            String id = this.getLangIdString();
            this.addLabelCell(Component.translatable("fancymenu.loading_requirements.is_language.lang_id"));
            this.langIdCell = this.addTextInputCell(null, true, true).setText(id);
            this.addCellGroupEndSpacerCell();
            this.langIdSuggestions = EditBoxSuggestions.createWithCustomSuggestions(this, this.langIdCell.editBox, EditBoxSuggestions.SuggestionsRenderPosition.ABOVE_EDIT_BOX, new ArrayList(Minecraft.getInstance().getLanguageManager().getLanguages().keySet()));
            UIBase.applyDefaultWidgetSkinTo(this.langIdSuggestions);
            this.langIdCell.editBox.m_94151_(s -> this.langIdSuggestions.updateCommandInfo());
            this.addSpacerCell(20);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            super.m_88315_(graphics, mouseX, mouseY, partial);
            this.langIdSuggestions.render(graphics, mouseX, mouseY);
        }

        @Override
        public boolean keyPressed(int $$0, int $$1, int $$2) {
            return this.langIdSuggestions.keyPressed($$0, $$1, $$2) ? true : super.m_7933_($$0, $$1, $$2);
        }

        @Override
        public boolean mouseScrolled(double $$0, double $$1, double $$2) {
            return this.langIdSuggestions.mouseScrolled($$2) ? true : super.m_6050_($$0, $$1, $$2);
        }

        @Override
        public boolean mouseClicked(double $$0, double $$1, int $$2) {
            return this.langIdSuggestions.mouseClicked($$0, $$1, $$2) ? true : super.m_6375_($$0, $$1, $$2);
        }

        @NotNull
        @Override
        public String buildString() {
            return this.getLangIdString();
        }

        @NotNull
        protected String getLangIdString() {
            return this.langIdCell != null ? this.langIdCell.getText() : this.langId;
        }
    }
}