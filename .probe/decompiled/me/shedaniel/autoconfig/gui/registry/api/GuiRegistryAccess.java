package me.shedaniel.autoconfig.gui.registry.api;

import java.lang.reflect.Field;
import java.util.List;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface GuiRegistryAccess extends GuiProvider, GuiTransformer {

    default List<AbstractConfigListEntry> getAndTransform(String i18n, Field field, Object config, Object defaults, GuiRegistryAccess registry) {
        return this.transform(this.get(i18n, field, config, defaults, registry), i18n, field, config, defaults, registry);
    }
}