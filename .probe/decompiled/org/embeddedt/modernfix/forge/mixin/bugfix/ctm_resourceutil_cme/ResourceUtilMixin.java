package org.embeddedt.modernfix.forge.mixin.bugfix.ctm_resourceutil_cme;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.embeddedt.modernfix.annotation.RequiresMod;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.chisel.ctm.client.util.ResourceUtil;

@Mixin({ ResourceUtil.class })
@RequiresMod("ctm")
@ClientOnlyMixin
public class ResourceUtilMixin {

    @Shadow(remap = false)
    @Final
    @Mutable
    private static Map metadataCache;

    @Inject(method = { "<clinit>" }, at = { @At("RETURN") })
    private static void synchronizeMetadataCache(CallbackInfo ci) {
        if (!(metadataCache instanceof ConcurrentMap)) {
            metadataCache = Collections.synchronizedMap(metadataCache);
        }
    }
}