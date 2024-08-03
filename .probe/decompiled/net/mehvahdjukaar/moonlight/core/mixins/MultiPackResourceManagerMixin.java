package net.mehvahdjukaar.moonlight.core.mixins;

import java.util.List;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.events.EarlyPackReloadEvent;
import net.mehvahdjukaar.moonlight.api.events.MoonlightEventsHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicResourcePack;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.mehvahdjukaar.moonlight.core.misc.FilteredResManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ MultiPackResourceManager.class })
public abstract class MultiPackResourceManagerMixin implements CloseableResourceManager {

    @Shadow
    @Override
    public abstract Optional<Resource> getResource(ResourceLocation var1);

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void moonlight$dynamicPackEarlyReload(PackType type, List<PackResources> packs, CallbackInfo cir) {
        if (!(this instanceof FilteredResManager) && (Boolean) Moonlight.CAN_EARLY_RELOAD_HACK.get() && this.getResource(new ResourceLocation("moonlight:moonlight/token.json")).isPresent() && !PlatHelper.isInitializing()) {
            DynamicResourcePack.clearBeforeReload(type);
            MoonlightEventsHelper.postEvent(new EarlyPackReloadEvent(packs, this, type), EarlyPackReloadEvent.class);
        }
    }
}