package de.keksuccino.fancymenu.customization.placeholder.placeholders.other.cpu;

import com.mojang.blaze3d.platform.GlUtil;
import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CpuInfoPlaceholder extends Placeholder {

    public CpuInfoPlaceholder() {
        super("cpuinfo");
    }

    @Nullable
    @Override
    public List<String> getAlternativeIdentifiers() {
        return List.of("drippy_cpu_info");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        return GlUtil.getCpuInfo();
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        return null;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.placeholders.cpu_info");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.placeholders.cpu_info.desc"));
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