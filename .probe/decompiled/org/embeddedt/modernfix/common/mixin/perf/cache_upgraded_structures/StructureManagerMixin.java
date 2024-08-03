package org.embeddedt.modernfix.common.mixin.perf.cache_upgraded_structures;

import com.mojang.datafixers.DataFixer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.structure.CachingStructureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ StructureTemplateManager.class })
public class StructureManagerMixin {

    @Shadow
    @Final
    private DataFixer fixerUpper;

    @Shadow
    private ResourceManager resourceManager;

    @Shadow
    @Final
    private HolderGetter<Block> blockLookup;

    @Overwrite
    private Optional<StructureTemplate> loadFromResource(ResourceLocation id) {
        ResourceLocation arg = new ResourceLocation(id.getNamespace(), "structures/" + id.getPath() + ".nbt");
        try {
            InputStream stream = this.resourceManager.m_215595_(arg);
            Optional var4;
            try {
                var4 = Optional.of(CachingStructureManager.readStructure(id, this.fixerUpper, stream, this.blockLookup));
            } catch (Throwable var7) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if (stream != null) {
                stream.close();
            }
            return var4;
        } catch (FileNotFoundException var8) {
            return Optional.empty();
        } catch (IOException var9) {
            ModernFix.LOGGER.error("Can't read structure", var9);
            return Optional.empty();
        }
    }
}