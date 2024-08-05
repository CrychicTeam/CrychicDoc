package me.shedaniel.autoconfig.gui.registry;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import org.apache.logging.log4j.LogManager;

public class DefaultGuiRegistryAccess implements GuiRegistryAccess {

    @Override
    public List<AbstractConfigListEntry> get(String i18n, Field field, Object config, Object defaults, GuiRegistryAccess registry) {
        LogManager.getLogger().error("No GUI provider registered for field '{}'!", field);
        return Collections.emptyList();
    }

    @Override
    public List<AbstractConfigListEntry> transform(List<AbstractConfigListEntry> guis, String i18n, Field field, Object config, Object defaults, GuiRegistryAccess registry) {
        return guis;
    }
}