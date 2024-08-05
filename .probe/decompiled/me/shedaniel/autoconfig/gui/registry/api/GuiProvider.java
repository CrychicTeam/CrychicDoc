package me.shedaniel.autoconfig.gui.registry.api;

import java.lang.reflect.Field;
import java.util.List;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@FunctionalInterface
@OnlyIn(Dist.CLIENT)
public interface GuiProvider {

    List<AbstractConfigListEntry> get(String var1, Field var2, Object var3, Object var4, GuiRegistryAccess var5);
}