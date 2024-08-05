package de.keksuccino.fancymenu.customization.placeholder.placeholders.client;

import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MinecraftVersionPlaceholder extends Placeholder {

    public MinecraftVersionPlaceholder() {
        super("mcversion");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        return FancyMenu.getMinecraftVersion();
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        return null;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.editor.dynamicvariabletextfield.variables.mcversion");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.dynamicvariabletextfield.variables.mcversion.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.client");
    }

    @NotNull
    @Override
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        DeserializedPlaceholderString dps = new DeserializedPlaceholderString();
        dps.placeholderIdentifier = this.getIdentifier();
        return dps;
    }
}