package de.keksuccino.fancymenu.customization.loadingrequirement;

import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementInstance;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorFormattingRule;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorScreen;
import de.keksuccino.konkrete.input.CharacterFilter;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class LoadingRequirement {

    protected final String identifier;

    public LoadingRequirement(@NotNull String uniqueRequirementIdentifier) {
        if (!CharacterFilter.getBasicFilenameCharacterFilter().isAllowed(uniqueRequirementIdentifier)) {
            throw new UnsupportedCharsetException("[FANCYMENU] Illegal characters in LoadingRequirement name: " + uniqueRequirementIdentifier);
        } else {
            this.identifier = (String) Objects.requireNonNull(uniqueRequirementIdentifier);
        }
    }

    public abstract boolean hasValue();

    public abstract boolean isRequirementMet(@Nullable String var1);

    @NotNull
    public abstract String getDisplayName();

    @Nullable
    public abstract List<String> getDescription();

    @Nullable
    public abstract String getCategory();

    @Nullable
    public abstract String getValueDisplayName();

    @Nullable
    public abstract String getValuePreset();

    @Nullable
    public abstract List<TextEditorFormattingRule> getValueFormattingRules();

    public void editValue(@NotNull Screen parentScreen, @NotNull LoadingRequirementInstance requirementInstance) {
        if (this.hasValue()) {
            String displayName = this.getValueDisplayName();
            TextEditorScreen s = new TextEditorScreen(displayName != null ? Component.literal(displayName) : Component.translatable("fancymenu.editor.elements.visibilityrequirements.edit_value"), null, call -> {
                if (call != null) {
                    requirementInstance.value = call;
                }
                Minecraft.getInstance().setScreen(parentScreen);
            });
            if (this.getValueFormattingRules() != null) {
                s.formattingRules.addAll(this.getValueFormattingRules());
            }
            s.setMultilineMode(false);
            if (requirementInstance.value != null) {
                s.setText(requirementInstance.value);
            } else {
                s.setText(this.getValuePreset());
            }
            Minecraft.getInstance().setScreen(s);
        }
    }

    @NotNull
    public String getIdentifier() {
        return this.identifier;
    }

    public boolean shouldShowUpInEditorRequirementMenu(@NotNull LayoutEditorScreen editor) {
        return true;
    }
}