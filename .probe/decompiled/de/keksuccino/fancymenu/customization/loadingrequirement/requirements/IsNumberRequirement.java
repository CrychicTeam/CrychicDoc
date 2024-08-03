package de.keksuccino.fancymenu.customization.loadingrequirement.requirements;

import de.keksuccino.fancymenu.customization.loadingrequirement.LoadingRequirement;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementInstance;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.cycle.CommonCycles;
import de.keksuccino.fancymenu.util.cycle.ILocalizedValueCycle;
import de.keksuccino.fancymenu.util.rendering.ui.screen.CellScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.StringBuilderScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorFormattingRule;
import de.keksuccino.konkrete.math.MathUtils;
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

public class IsNumberRequirement extends LoadingRequirement {

    private static final Logger LOGGER = LogManager.getLogger();

    public IsNumberRequirement() {
        super("fancymenu_visibility_requirement_is_number");
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
            String number = (String) section.get(1);
            String compareWith = (String) section.get(2);
            if (MathUtils.isDouble(number) && MathUtils.isDouble(compareWith)) {
                double num = Double.parseDouble(number);
                double comp = Double.parseDouble(compareWith);
                if (mode.equals("equals")) {
                    return num == comp;
                }
                if (mode.equals("bigger-than")) {
                    return num > comp;
                }
                if (mode.equals("smaller-than")) {
                    return num < comp;
                }
                if (mode.equals("bigger-than-or-equals")) {
                    return num >= comp;
                }
                if (mode.equals("smaller-than-or-equals")) {
                    return num <= comp;
                }
            }
        }
        return false;
    }

    private static List<String> parseSection(String section) {
        List<String> l = new ArrayList();
        int currentIndex = 0;
        int currentStartIndex = 0;
        String mode = null;
        String number = null;
        String compareWith = null;
        for (char c : section.toCharArray()) {
            String s = String.valueOf(c);
            if (s.equals("\"")) {
                if (currentIndex >= 7 && section.substring(currentIndex - 7).startsWith("\"mode\":\"")) {
                    currentStartIndex = currentIndex + 1;
                }
                if (section.substring(currentIndex).startsWith("\",\"number\":\"")) {
                    mode = section.substring(currentStartIndex, currentIndex);
                }
                if (currentIndex >= 9 && section.substring(currentIndex - 9).startsWith("\"number\":\"")) {
                    currentStartIndex = currentIndex + 1;
                }
                if (section.substring(currentIndex).startsWith("\",\"compare_with\":\"")) {
                    number = section.substring(currentStartIndex, currentIndex);
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
        if (mode != null && number != null && compareWith != null) {
            l.add(mode);
            l.add(number);
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
        return I18n.get("fancymenu.helper.editor.items.visibilityrequirements.is_number");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.helper.editor.items.visibilityrequirements.is_number.desc"));
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
        return "[\"mode\":\"...\",\"number\":\"...\",\"compare_with\":\"...\"]$";
    }

    @Override
    public List<TextEditorFormattingRule> getValueFormattingRules() {
        return null;
    }

    @Override
    public void editValue(@NotNull Screen parentScreen, @NotNull LoadingRequirementInstance requirementInstance) {
        IsNumberRequirement.IsNumberValueConfigScreen s = new IsNumberRequirement.IsNumberValueConfigScreen((String) Objects.requireNonNullElse(requirementInstance.value, ""), callback -> {
            if (callback != null) {
                requirementInstance.value = callback;
            }
            Minecraft.getInstance().setScreen(parentScreen);
        });
        Minecraft.getInstance().setScreen(s);
    }

    public static class IsNumberValueConfigScreen extends StringBuilderScreen {

        @NotNull
        protected IsNumberRequirement.NumberCompareMode mode = IsNumberRequirement.NumberCompareMode.EQUALS;

        @NotNull
        protected String firstNumber = "";

        @NotNull
        protected String secondNumber = "";

        protected CellScreen.TextInputCell firstNumberCell;

        protected CellScreen.TextInputCell secondNumberCell;

        protected IsNumberValueConfigScreen(String value, @NotNull Consumer<String> callback) {
            super(Component.translatable("fancymenu.helper.editor.items.visibilityrequirements.is_number.valuename"), callback);
            if (value == null) {
                value = "";
            }
            List<String> sections = IsNumberRequirement.getSections(value);
            if (!sections.isEmpty()) {
                List<String> deserialized = IsNumberRequirement.parseSection((String) sections.get(0));
                if (!deserialized.isEmpty()) {
                    IsNumberRequirement.NumberCompareMode m = IsNumberRequirement.NumberCompareMode.getByKey((String) deserialized.get(0));
                    if (m != null) {
                        this.mode = m;
                    }
                    this.firstNumber = (String) deserialized.get(1);
                    this.secondNumber = (String) deserialized.get(2);
                }
            }
        }

        @Override
        protected void initCells() {
            this.addSpacerCell(20);
            ILocalizedValueCycle<IsNumberRequirement.NumberCompareMode> modeCycle = CommonCycles.cycleOrangeValue("fancymenu.loading_requirements.is_number.compare_mode", Arrays.asList(IsNumberRequirement.NumberCompareMode.values()), this.mode).setValueNameSupplier(mode -> {
                if (mode == IsNumberRequirement.NumberCompareMode.BIGGER_THAN) {
                    return I18n.get("fancymenu.loading_requirements.is_number.compare_mode.bigger_than");
                } else if (mode == IsNumberRequirement.NumberCompareMode.SMALLER_THAN) {
                    return I18n.get("fancymenu.loading_requirements.is_number.compare_mode.smaller_than");
                } else if (mode == IsNumberRequirement.NumberCompareMode.BIGGER_THAN_OR_EQUALS) {
                    return I18n.get("fancymenu.loading_requirements.is_number.compare_mode.bigger_than_or_equals");
                } else {
                    return mode == IsNumberRequirement.NumberCompareMode.SMALLER_THAN_OR_EQUALS ? I18n.get("fancymenu.loading_requirements.is_number.compare_mode.smaller_than_or_equals") : I18n.get("fancymenu.loading_requirements.is_number.compare_mode.equals");
                }
            });
            this.addCycleButtonCell(modeCycle, true, (value, button) -> this.mode = value);
            this.addCellGroupEndSpacerCell();
            String fNumber = this.getFirstNumberString();
            this.addLabelCell(Component.translatable("fancymenu.loading_requirements.is_number.compare_mode.first_number"));
            this.firstNumberCell = this.addTextInputCell(null, true, true).setText(fNumber);
            this.addCellGroupEndSpacerCell();
            String sNumber = this.getSecondNumberString();
            this.addLabelCell(Component.translatable("fancymenu.loading_requirements.is_number.compare_mode.second_number"));
            this.secondNumberCell = this.addTextInputCell(null, true, true).setText(sNumber);
            this.addSpacerCell(20);
        }

        @NotNull
        @Override
        public String buildString() {
            return "[\"mode\":\"" + this.mode.key + "\",\"number\":\"" + this.getFirstNumberString() + "\",\"compare_with\":\"" + this.getSecondNumberString() + "\"]$";
        }

        @NotNull
        protected String getFirstNumberString() {
            return this.firstNumberCell != null ? this.firstNumberCell.getText() : this.firstNumber;
        }

        @NotNull
        protected String getSecondNumberString() {
            return this.secondNumberCell != null ? this.secondNumberCell.getText() : this.secondNumber;
        }
    }

    public static enum NumberCompareMode {

        EQUALS("equals"), BIGGER_THAN("bigger-than"), SMALLER_THAN("smaller-than-or-equals"), BIGGER_THAN_OR_EQUALS("bigger-than-or-equals"), SMALLER_THAN_OR_EQUALS("smaller-than-or-equals");

        public final String key;

        private NumberCompareMode(String key) {
            this.key = key;
        }

        @Nullable
        public static IsNumberRequirement.NumberCompareMode getByKey(@NotNull String key) {
            for (IsNumberRequirement.NumberCompareMode mode : values()) {
                if (mode.key.equals(key)) {
                    return mode;
                }
            }
            return null;
        }
    }
}