package com.craisinlord.integrated_api.mixins.structures;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ StructureTemplatePool.class })
public interface StructurePoolAccessor {

    @Accessor("CODEC_REFERENCE")
    static MutableObject<Codec<Holder<StructureTemplatePool>>> getCODEC_REFERENCE() {
        throw new UnsupportedOperationException();
    }

    @Accessor("rawTemplates")
    List<Pair<StructurePoolElement, Integer>> integratedapi_getRawTemplates();

    @Mutable
    @Accessor("rawTemplates")
    void integratedapi_setRawTemplates(List<Pair<StructurePoolElement, Integer>> var1);

    @Accessor("templates")
    ObjectArrayList<StructurePoolElement> integratedapi_getTemplates();

    @Mutable
    @Accessor("templates")
    void integratedapi_setTemplates(ObjectArrayList<StructurePoolElement> var1);
}