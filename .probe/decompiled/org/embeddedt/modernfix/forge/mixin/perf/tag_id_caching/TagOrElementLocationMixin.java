package org.embeddedt.modernfix.forge.mixin.perf.tag_id_caching;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ ExtraCodecs.TagOrElementLocation.class })
public class TagOrElementLocationMixin {

    @Shadow
    @Final
    private boolean tag;

    @Shadow
    @Final
    private ResourceLocation id;

    private String cachedDecoratedId;

    @Overwrite
    private String decoratedId() {
        String id = this.cachedDecoratedId;
        if (id == null) {
            id = this.tag ? "#" + this.id : this.id.toString();
            this.cachedDecoratedId = id;
        }
        return id;
    }
}