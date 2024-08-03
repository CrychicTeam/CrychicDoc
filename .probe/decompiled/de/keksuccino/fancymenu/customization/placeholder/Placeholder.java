package de.keksuccino.fancymenu.customization.placeholder;

import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Placeholder {

    protected final String id;

    public Placeholder(String id) {
        this.id = id;
    }

    public abstract String getReplacementFor(DeserializedPlaceholderString var1);

    @Nullable
    public abstract List<String> getValueNames();

    @NotNull
    public abstract String getDisplayName();

    @Nullable
    public abstract List<String> getDescription();

    public abstract String getCategory();

    @NotNull
    public abstract DeserializedPlaceholderString getDefaultPlaceholderString();

    public String getIdentifier() {
        return this.id;
    }

    @Nullable
    public List<String> getAlternativeIdentifiers() {
        return null;
    }

    public boolean shouldShowUpInPlaceholderMenu(@Nullable LayoutEditorScreen editor) {
        return true;
    }
}