package de.keksuccino.fancymenu.customization.widget.identification.identificationcontext;

import de.keksuccino.fancymenu.customization.widget.WidgetMeta;
import de.keksuccino.fancymenu.util.ConsumingSupplier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated
public abstract class WidgetIdentificationContext {

    protected final List<ConsumingSupplier<WidgetMeta, String>> identifierProviders = new ArrayList();

    @NotNull
    public abstract Class<? extends Screen> getTargetScreen();

    public void addUniversalIdentifierProvider(@NotNull ConsumingSupplier<WidgetMeta, String> provider) {
        this.identifierProviders.add((ConsumingSupplier) Objects.requireNonNull(provider));
    }

    @Nullable
    public String getUniversalIdentifierForWidget(@NotNull WidgetMeta meta) {
        for (ConsumingSupplier<WidgetMeta, String> provider : this.identifierProviders) {
            String universal = provider.get(meta);
            if (universal != null) {
                return universal;
            }
        }
        return null;
    }
}