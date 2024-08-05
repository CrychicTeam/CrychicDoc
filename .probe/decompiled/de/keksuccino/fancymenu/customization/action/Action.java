package de.keksuccino.fancymenu.customization.action;

import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorFormattingRule;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorScreen;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Action {

    public static final Action EMPTY = new Action("empty") {

        @Override
        public boolean hasValue() {
            return false;
        }

        @Override
        public void execute(@Nullable String value) {
        }

        @NotNull
        @Override
        public Component getActionDisplayName() {
            return Component.empty();
        }

        @NotNull
        @Override
        public Component[] getActionDescription() {
            return new Component[0];
        }

        @Nullable
        @Override
        public Component getValueDisplayName() {
            return null;
        }

        @Nullable
        @Override
        public String getValueExample() {
            return null;
        }
    };

    private final String identifier;

    public Action(@NotNull String uniqueIdentifier) {
        this.identifier = (String) Objects.requireNonNull(uniqueIdentifier);
    }

    public boolean isDeprecated() {
        return false;
    }

    public boolean shouldShowUpInEditorActionMenu(@NotNull LayoutEditorScreen editor) {
        return true;
    }

    public abstract boolean hasValue();

    public abstract void execute(@Nullable String var1);

    @NotNull
    public abstract Component getActionDisplayName();

    @NotNull
    public abstract Component[] getActionDescription();

    @Nullable
    public abstract Component getValueDisplayName();

    @Nullable
    public abstract String getValueExample();

    @NotNull
    public String getIdentifier() {
        return this.identifier;
    }

    @Nullable
    public List<TextEditorFormattingRule> getValueFormattingRules() {
        return null;
    }

    public void editValue(@NotNull Screen parentScreen, @NotNull ActionInstance instance) {
        if (this.hasValue()) {
            TextEditorScreen s = new TextEditorScreen(this.getValueDisplayName(), null, call -> {
                if (call != null) {
                    instance.value = call;
                }
                Minecraft.getInstance().setScreen(parentScreen);
            });
            List<TextEditorFormattingRule> formattingRules = this.getValueFormattingRules();
            if (formattingRules != null) {
                s.formattingRules.addAll(formattingRules);
            }
            s.setMultilineMode(false);
            if (instance.value != null) {
                s.setText(instance.value);
            } else {
                s.setText(this.getValueExample());
            }
            Minecraft.getInstance().setScreen(s);
        }
    }
}