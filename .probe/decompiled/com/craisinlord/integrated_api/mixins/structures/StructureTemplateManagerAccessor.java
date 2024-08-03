package com.craisinlord.integrated_api.mixins.structures;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ StructureTemplateManager.class })
public interface StructureTemplateManagerAccessor {

    @Accessor("resourceManager")
    ResourceManager integratedapi_getResourceManager();
}