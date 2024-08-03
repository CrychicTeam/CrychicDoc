package org.embeddedt.modernfix.common.mixin.perf.ticking_chunk_alloc;

import java.util.Collections;
import java.util.Map;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = { ChunkAccess.class }, priority = 800)
public class ChunkAccessMixin {

    @Shadow
    @Final
    private Map<?, ?> structuresRefences;

    private Map<?, ?> mfix$structureRefsView;

    @Overwrite
    public Map<?, ?> getAllReferences() {
        if (this.structuresRefences.isEmpty()) {
            return Collections.emptyMap();
        } else {
            Map<?, ?> view = this.mfix$structureRefsView;
            if (view == null) {
                this.mfix$structureRefsView = view = Collections.unmodifiableMap(this.structuresRefences);
            }
            return view;
        }
    }
}