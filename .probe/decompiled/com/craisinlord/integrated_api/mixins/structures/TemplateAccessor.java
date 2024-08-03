package com.craisinlord.integrated_api.mixins.structures;

import java.util.List;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ StructureTemplate.class })
public interface TemplateAccessor {

    @Accessor("palettes")
    List<StructureTemplate.Palette> integratedapi_getPalettes();
}