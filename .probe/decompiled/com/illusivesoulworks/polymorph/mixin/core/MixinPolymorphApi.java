package com.illusivesoulworks.polymorph.mixin.core;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.client.base.IPolymorphClient;
import com.illusivesoulworks.polymorph.api.common.base.IPolymorphCommon;
import com.illusivesoulworks.polymorph.client.impl.PolymorphClient;
import com.illusivesoulworks.polymorph.common.impl.PolymorphCommon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ PolymorphApi.class })
public class MixinPolymorphApi {

    @Inject(at = { @At("HEAD") }, method = { "common" }, remap = false, cancellable = true)
    private static void polymorph$common(CallbackInfoReturnable<IPolymorphCommon> cir) {
        cir.setReturnValue(PolymorphCommon.get());
    }

    @Inject(at = { @At("HEAD") }, method = { "client" }, remap = false, cancellable = true)
    private static void polymorph$client(CallbackInfoReturnable<IPolymorphClient> cir) {
        cir.setReturnValue(PolymorphClient.get());
    }
}