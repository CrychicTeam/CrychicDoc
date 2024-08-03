package me.shedaniel.autoconfig.gui.registry;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ComposedGuiRegistryAccess implements GuiRegistryAccess {

    private List<GuiRegistryAccess> children;

    public ComposedGuiRegistryAccess(GuiRegistryAccess... children) {
        this.children = Arrays.asList(children);
    }

    @Override
    public List<AbstractConfigListEntry> get(String i18n, Field field, Object config, Object defaults, GuiRegistryAccess registry) {
        return (List<AbstractConfigListEntry>) this.children.stream().map(child -> child.get(i18n, field, config, defaults, registry)).filter(Objects::nonNull).findFirst().orElseThrow(() -> new RuntimeException("No ConfigGuiProvider match!"));
    }

    @Override
    public List<AbstractConfigListEntry> transform(List<AbstractConfigListEntry> guis, String i18n, Field field, Object config, Object defaults, GuiRegistryAccess registry) {
        for (GuiRegistryAccess child : this.children) {
            guis = child.transform(guis, i18n, field, config, defaults, registry);
        }
        return guis;
    }
}