package de.keksuccino.fancymenu.customization.placeholder.placeholders.gui;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.customization.screen.identifier.ScreenIdentifierHandler;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScreenIdentifierPlaceholder extends Placeholder {

    public ScreenIdentifierPlaceholder() {
        super("screenid");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        return Minecraft.getInstance().screen != null ? ScreenIdentifierHandler.getIdentifierOfScreen(Minecraft.getInstance().screen) : "";
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        return null;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.placeholders.screen_identifier");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.placeholders.screen_identifier.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.gui");
    }

    @NotNull
    @Override
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        return new DeserializedPlaceholderString(this.getIdentifier(), null, "");
    }
}