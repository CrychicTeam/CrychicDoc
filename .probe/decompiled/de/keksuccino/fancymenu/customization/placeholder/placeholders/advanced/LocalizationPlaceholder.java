package de.keksuccino.fancymenu.customization.placeholder.placeholders.advanced;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.konkrete.localization.Locals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocalizationPlaceholder extends Placeholder {

    public LocalizationPlaceholder() {
        super("local");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String key = (String) dps.values.get("key");
        if (key != null) {
            String localized = I18n.get(key);
            if (localized.equals(key)) {
                localized = Locals.localize(key);
            }
            return localized;
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        List<String> l = new ArrayList();
        l.add("key");
        return l;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.variables.local");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.fancymenu.editor.dynamicvariabletextfield.variables.local.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.advanced");
    }

    @NotNull
    @Override
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        DeserializedPlaceholderString dps = new DeserializedPlaceholderString();
        dps.placeholderIdentifier = this.getIdentifier();
        dps.values.put("key", "localization.key");
        return dps;
    }
}