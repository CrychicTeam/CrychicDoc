package de.keksuccino.fancymenu.customization.placeholder.placeholders.advanced;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.konkrete.math.MathUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RandomNumberPlaceholder extends Placeholder {

    public RandomNumberPlaceholder() {
        super("random_number");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String min = (String) dps.values.get("min");
        String max = (String) dps.values.get("max");
        if (min != null && max != null && MathUtils.isInteger(min) && MathUtils.isInteger(max)) {
            int minInt = Integer.parseInt(min);
            int maxInt = Integer.parseInt(max);
            return minInt == maxInt ? minInt + "" : MathUtils.getRandomNumberInRange(Math.min(minInt, maxInt), Math.max(minInt, maxInt)) + "";
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        List<String> l = new ArrayList();
        l.add("min");
        l.add("max");
        return l;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.helper.placeholder.random_number");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.helper.placeholder.random_number.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.advanced");
    }

    @NotNull
    @Override
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        Map<String, String> m = new HashMap();
        m.put("min", "1");
        m.put("max", "30");
        return DeserializedPlaceholderString.build(this.getIdentifier(), m);
    }
}