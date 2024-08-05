package de.keksuccino.fancymenu.customization.placeholder.placeholders.other.cpu;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.MathUtils;
import de.keksuccino.fancymenu.util.PerformanceUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OsCpuUsagePlaceholder extends Placeholder {

    public OsCpuUsagePlaceholder() {
        super("oscpu");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        double d = PerformanceUtils.getOsCpuUsage();
        if (d < 0.0) {
            d = 0.0;
        }
        d *= 100.0;
        d = MathUtils.round(d, 1);
        return d + "";
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        return null;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.placeholders.os_cpu_usage");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.placeholders.os_cpu_usage.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.other");
    }

    @NotNull
    @Override
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        return new DeserializedPlaceholderString(this.getIdentifier(), null, "");
    }
}