package de.keksuccino.fancymenu.mixin.mixins.common.client;

import java.util.Map;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ ClientLanguage.class })
public interface IMixinClientLanguage {

    @Accessor("storage")
    Map<String, String> getStorageFancyMenu();
}