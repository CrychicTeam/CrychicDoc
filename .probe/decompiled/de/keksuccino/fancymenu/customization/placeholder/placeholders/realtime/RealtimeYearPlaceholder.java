package de.keksuccino.fancymenu.customization.placeholder.placeholders.realtime;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import java.util.Calendar;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RealtimeYearPlaceholder extends Placeholder {

    public RealtimeYearPlaceholder() {
        super("realtimeyear");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        Calendar c = Calendar.getInstance();
        return c.get(1) + "";
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        return null;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.variables.realtimeyear");
    }

    @Override
    public List<String> getDescription() {
        return null;
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.realtime");
    }

    @NotNull
    @Override
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        DeserializedPlaceholderString dps = new DeserializedPlaceholderString();
        dps.placeholderIdentifier = this.getIdentifier();
        return dps;
    }
}