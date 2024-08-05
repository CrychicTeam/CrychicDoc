package me.shedaniel.autoconfig.gui.registry.api;

import java.lang.reflect.Field;
import java.util.List;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@FunctionalInterface
@OnlyIn(Dist.CLIENT)
public interface GuiTransformer {

    List<AbstractConfigListEntry> transform(List<AbstractConfigListEntry> var1, String var2, Field var3, Object var4, Object var5, GuiRegistryAccess var6);
}