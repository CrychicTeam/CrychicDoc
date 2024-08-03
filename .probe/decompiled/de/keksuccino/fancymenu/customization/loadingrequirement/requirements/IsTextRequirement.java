package de.keksuccino.fancymenu.customization.loadingrequirement.requirements;

import de.keksuccino.fancymenu.customization.loadingrequirement.LoadingRequirement;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementInstance;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.cycle.CommonCycles;
import de.keksuccino.fancymenu.util.cycle.ILocalizedValueCycle;
import de.keksuccino.fancymenu.util.rendering.ui.screen.CellScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.StringBuilderScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorFormattingRule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IsTextRequirement extends LoadingRequirement {

    private static final Logger LOGGER = LogManager.getLogger();

    public IsTextRequirement() {
        super("fancymenu_visibility_requirement_is_text");
    }

    @Override
    public boolean hasValue() {
        return true;
    }

    @Override
    public boolean isRequirementMet(@Nullable String value) {
        if (value != null) {
            List<String> secStrings = getSections(value);
            if (secStrings.isEmpty()) {
                return false;
            } else {
                boolean b = true;
                for (String s : secStrings) {
                    if (!isSectionMet(parseSection(s))) {
                        b = false;
                    }
                }
                return b;
            }
        } else {
            return false;
        }
    }

    private static boolean isSectionMet(List<String> section) {
        if (!section.isEmpty()) {
            String mode = (String) section.get(0);
            String text = (String) section.get(1);
            String compareWith = (String) section.get(2);
            if (mode.equals("equals")) {
                return text.equals(compareWith);
            }
            if (mode.equals("contains")) {
                return text.contains(compareWith);
            }
            if (mode.equals("starts-with")) {
                return text.startsWith(compareWith);
            }
            if (mode.equals("ends-with")) {
                return text.endsWith(compareWith);
            }
        }
        return false;
    }

    private static List<String> parseSection(String section) {
        List<String> l = new ArrayList();
        int currentIndex = 0;
        int currentStartIndex = 0;
        String mode = null;
        String text = null;
        String compareWith = null;
        for (char c : section.toCharArray()) {
            String s = String.valueOf(c);
            if (s.equals("\"")) {
                if (currentIndex >= 7 && section.substring(currentIndex - 7).startsWith("\"mode\":\"")) {
                    currentStartIndex = currentIndex + 1;
                }
                if (section.substring(currentIndex).startsWith("\",\"text\":\"")) {
                    mode = section.substring(currentStartIndex, currentIndex);
                }
                if (currentIndex >= 7 && section.substring(currentIndex - 7).startsWith("\"text\":\"")) {
                    currentStartIndex = currentIndex + 1;
                }
                if (section.substring(currentIndex).startsWith("\",\"compare_with\":\"")) {
                    text = section.substring(currentStartIndex, currentIndex);
                }
                if (currentIndex >= 15 && section.substring(currentIndex - 15).startsWith("\"compare_with\":\"")) {
                    currentStartIndex = currentIndex + 1;
                }
                if (section.substring(currentIndex).startsWith("\"]$")) {
                    compareWith = section.substring(currentStartIndex, currentIndex);
                }
            }
            currentIndex++;
        }
        if (mode != null && text != null && compareWith != null) {
            l.add(mode);
            l.add(text);
            l.add(compareWith);
        }
        return l;
    }

    private static List<String> getSections(String value) {
        List<String> l = new ArrayList();
        int currentIndex = 0;
        int currentStartIndex = 0;
        for (char c : value.toCharArray()) {
            String s = String.valueOf(c);
            if (s.equals("[") && value.substring(currentIndex).startsWith("[\"mode\":\"")) {
                currentStartIndex = currentIndex;
            }
            if (currentIndex >= 1 && s.equals("]") && value.substring(currentIndex - 1).startsWith("\"]$")) {
                l.add(value.substring(currentStartIndex, currentIndex + 2));
            }
            currentIndex++;
        }
        return l;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.helper.editor.items.visibilityrequirements.is_text");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.helper.editor.items.visibilityrequirements.is_text.desc"));
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
        return "[\"mode\":\"...\",\"text\":\"...\",\"compare_with\":\"...\"]$";
    }

    @Override
    public List<TextEditorFormattingRule> getValueFormattingRules() {
        return null;
    }

    @Override
    public void editValue(@NotNull Screen parentScreen, @NotNull LoadingRequirementInstance requirementInstance) {
        IsTextRequirement.IsTextValueConfigScreen s = new IsTextRequirement.IsTextValueConfigScreen((String) Objects.requireNonNullElse(requirementInstance.value, ""), callback -> {
            if (callback != null) {
                requirementInstance.value = callback;
            }
            Minecraft.getInstance().setScreen(parentScreen);
        });
        Minecraft.getInstance().setScreen(s);
    }

    public static class IsTextValueConfigScreen extends StringBuilderScreen {

        @NotNull
        protected IsTextRequirement.TextCompareMode mode = IsTextRequirement.TextCompareMode.EQUALS;

        @NotNull
        protected String firstText = "";

        @NotNull
        protected String secondText = "";

        protected CellScreen.TextInputCell firstTextCell;

        protected CellScreen.TextInputCell secondTextCell;

        protected IsTextValueConfigScreen(String value, @NotNull Consumer<String> callback) {
            super(Component.translatable("fancymenu.helper.editor.items.visibilityrequirements.is_text.valuename"), callback);
            if (value == null) {
                value = "";
            }
            List<String> sections = IsTextRequirement.getSections(value);
            if (!sections.isEmpty()) {
                List<String> deserialized = IsTextRequirement.parseSection((String) sections.get(0));
                if (!deserialized.isEmpty()) {
                    IsTextRequirement.TextCompareMode m = IsTextRequirement.TextCompareMode.getByKey((String) deserialized.get(0));
                    if (m != null) {
                        this.mode = m;
                    }
                    this.firstText = (String) deserialized.get(1);
                    this.secondText = (String) deserialized.get(2);
                }
            }
        }

        @Override
        protected void initCells() {
            this.addSpacerCell(20);
            ILocalizedValueCycle<IsTextRequirement.TextCompareMode> modeCycle = CommonCycles.cycleOrangeValue("fancymenu.loading_requirements.is_text.compare_mode", Arrays.asList(IsTextRequirement.TextCompareMode.values()), this.mode).setValueNameSupplier(mode -> {
                if (mode == IsTextRequirement.TextCompareMode.CONTAINS) {
                    return I18n.get("fancymenu.loading_requirements.is_text.compare_mode.contains");
                } else if (mode == IsTextRequirement.TextCompareMode.STARTS_WITH) {
                    return I18n.get("fancymenu.loading_requirements.is_text.compare_mode.starts_with");
                } else {
                    return mode == IsTextRequirement.TextCompareMode.ENDS_WITH ? I18n.get("fancymenu.loading_requirements.is_text.compare_mode.ends_with") : I18n.get("fancymenu.loading_requirements.is_text.compare_mode.equals");
                }
            });
            this.addCycleButtonCell(modeCycle, true, (value, button) -> this.mode = value);
            this.addCellGroupEndSpacerCell();
            String fText = this.getFirstTextString();
            this.addLabelCell(Component.translatable("fancymenu.loading_requirements.is_text.compare_mode.first_text"));
            this.firstTextCell = this.addTextInputCell(null, true, true).setText(fText);
            this.addCellGroupEndSpacerCell();
            String sText = this.getSecondTextString();
            this.addLabelCell(Component.translatable("fancymenu.loading_requirements.is_text.compare_mode.second_text"));
            this.secondTextCell = this.addTextInputCell(null, true, true).setText(sText);
            this.addSpacerCell(20);
        }

        @NotNull
        @Override
        public String buildString() {
            return "[\"mode\":\"" + this.mode.key + "\",\"text\":\"" + this.getFirstTextString() + "\",\"compare_with\":\"" + this.getSecondTextString() + "\"]$";
        }

        @NotNull
        protected String getFirstTextString() {
            return this.firstTextCell != null ? this.firstTextCell.getText() : this.firstText;
        }

        @NotNull
        protected String getSecondTextString() {
            return this.secondTextCell != null ? this.secondTextCell.getText() : this.secondText;
        }
    }

    public static enum TextCompareMode {

        EQUALS("equals"), CONTAINS("contains"), STARTS_WITH("starts-with"), ENDS_WITH("ends-with");

        public final String key;

        private TextCompareMode(String key) {
            this.key = key;
        }

        @Nullable
        public static IsTextRequirement.TextCompareMode getByKey(@NotNull String key) {
            for (IsTextRequirement.TextCompareMode mode : values()) {
                if (mode.key.equals(key)) {
                    return mode;
                }
            }
            return null;
        }
    }
}