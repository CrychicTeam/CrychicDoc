package de.keksuccino.fancymenu.customization.placeholder.placeholders.client;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.platform.Services;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModVersionPlaceholder extends Placeholder {

    public ModVersionPlaceholder() {
        super("modversion");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        return !dps.values.containsKey("modid") ? null : this.getModVersion((String) dps.values.get("modid"));
    }

    private String getModVersion(String modid) {
        return Services.PLATFORM.getModVersion(modid);
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        List<String> l = new ArrayList();
        l.add("modid");
        return l;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.editor.dynamicvariabletextfield.variables.modversion");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.dynamicvariabletextfield.variables.modversion.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.client");
    }

    @NotNull
    @Override
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        Map<String, String> m = new HashMap();
        m.put("modid", "some_mod_id");
        return DeserializedPlaceholderString.build(this.getIdentifier(), m);
    }
}