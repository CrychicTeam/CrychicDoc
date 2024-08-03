package org.embeddedt.modernfix.forge.mixin.perf.forge_registry_alloc;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.embeddedt.modernfix.forge.registry.DelegateHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { ForgeRegistry.class }, remap = false)
public abstract class ForgeRegistryMixin<V> {

    @Shadow
    @Final
    private Map<ResourceLocation, Holder.Reference<V>> delegatesByName = new Object2ObjectOpenHashMap();

    @Shadow
    @Final
    private Map<V, Holder.Reference<V>> delegatesByValue = new Object2ObjectOpenHashMap(16, 0.5F);

    @Shadow
    @Final
    private RegistryManager stage;

    @Shadow
    public abstract ResourceKey<Registry<V>> getRegistryKey();

    @Overwrite
    public Holder.Reference<V> getDelegateOrThrow(ResourceLocation location) {
        Holder.Reference<V> holder = (Holder.Reference<V>) this.delegatesByName.get(location);
        if (holder == null) {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "No delegate exists for location %s", location));
        } else {
            return holder;
        }
    }

    @Overwrite
    public Holder.Reference<V> getDelegateOrThrow(ResourceKey<V> rkey) {
        Holder.Reference<V> holder = (Holder.Reference<V>) this.delegatesByName.get(rkey.location());
        if (holder == null) {
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "No delegate exists for key %s", rkey));
        } else {
            return holder;
        }
    }

    @Inject(method = { "bindDelegate" }, at = { @At("RETURN") })
    private void attachDelegate(ResourceKey<V> rkey, V value, CallbackInfoReturnable<Holder.Reference<V>> cir) {
        if (this.stage == RegistryManager.ACTIVE && value instanceof DelegateHolder) {
            ((DelegateHolder) value).mfix$setDelegate(this.getRegistryKey(), (Holder.Reference<V>) cir.getReturnValue());
        }
    }

    @Overwrite
    public Holder.Reference<V> getDelegateOrThrow(V value) {
        Holder.Reference<V> holder = null;
        if (this.stage == RegistryManager.ACTIVE && value instanceof DelegateHolder) {
            holder = ((DelegateHolder) value).mfix$getDelegate(this.getRegistryKey());
        }
        if (holder == null) {
            holder = (Holder.Reference<V>) this.delegatesByValue.get(value);
            if (holder == null) {
                throw new IllegalArgumentException(String.format(Locale.ENGLISH, "No delegate exists for value %s", value));
            }
        }
        return holder;
    }
}