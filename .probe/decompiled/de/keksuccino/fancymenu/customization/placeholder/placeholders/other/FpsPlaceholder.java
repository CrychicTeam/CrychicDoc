package de.keksuccino.fancymenu.customization.placeholder.placeholders.other;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FpsPlaceholder extends Placeholder {

    public FpsPlaceholder() {
        super("fps");
    }

    @Nullable
    @Override
    public List<String> getAlternativeIdentifiers() {
        return List.of("drippy_fps");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String s = "0";
        if (Minecraft.getInstance().fpsString.contains(" ")) {
            s = Minecraft.getInstance().fpsString.split(" ", 2)[0];
        }
        return s;
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        return null;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.placeholders.fps");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.placeholders.fps.desc"));
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