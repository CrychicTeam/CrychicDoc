package de.keksuccino.fancymenu.customization.placeholder.placeholders.client;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.minecraftoptions.MinecraftOption;
import de.keksuccino.fancymenu.util.minecraftoptions.MinecraftOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MinecraftOptionValuePlaceholder extends Placeholder {

    private static final Map<String, MinecraftOption> CACHED_OPTIONS = new HashMap();

    public MinecraftOptionValuePlaceholder() {
        super("minecraft_option_value");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        return !dps.values.containsKey("name") ? null : this.getOptionValue((String) dps.values.get("name"));
    }

    @NotNull
    private String getOptionValue(@NotNull String name) {
        MinecraftOption option = MinecraftOptions.getOption(name);
        if (option != null) {
            String value = option.get();
            if (value != null) {
                return value;
            }
        }
        return "";
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        List<String> l = new ArrayList();
        l.add("name");
        return l;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.placeholders.minecraft_option_value");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.placeholders.minecraft_option_value.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.client");
    }

    @NotNull
    @Override
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        Map<String, String> m = new HashMap();
        m.put("name", "option_name");
        return DeserializedPlaceholderString.build(this.getIdentifier(), m);
    }
}