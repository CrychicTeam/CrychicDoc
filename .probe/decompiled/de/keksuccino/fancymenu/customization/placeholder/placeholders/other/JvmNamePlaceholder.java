package de.keksuccino.fancymenu.customization.placeholder.placeholders.other;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JvmNamePlaceholder extends Placeholder {

    public JvmNamePlaceholder() {
        super("jvmname");
    }

    @Nullable
    @Override
    public List<String> getAlternativeIdentifiers() {
        return List.of("drippy_java_vm");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        return System.getProperty("java.vm.name");
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        return null;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.placeholders.jvm_name");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.placeholders.jvm_name.desc"));
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