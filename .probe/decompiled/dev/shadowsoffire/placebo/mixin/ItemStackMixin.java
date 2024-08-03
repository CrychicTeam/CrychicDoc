package dev.shadowsoffire.placebo.mixin;

import dev.shadowsoffire.placebo.events.PlaceboEventFactory;
import dev.shadowsoffire.placebo.util.CachedObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ItemStack.class })
public class ItemStackMixin implements CachedObject.CachedObjectSource {

    private volatile Map<ResourceLocation, CachedObject<?>> cachedObjects = null;

    @Inject(at = { @At("HEAD") }, method = { "useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;" }, cancellable = true, require = 1)
    public void placebo_itemUseHook(UseOnContext ctx, CallbackInfoReturnable<InteractionResult> cir) {
        InteractionResult itemUseEventRes = PlaceboEventFactory.onItemUse((ItemStack) this, ctx);
        if (itemUseEventRes != null) {
            cir.setReturnValue(itemUseEventRes);
        }
    }

    @Override
    public <T> T getOrCreate(ResourceLocation id, Function<ItemStack, T> deserializer, ToIntFunction<ItemStack> hasher) {
        CachedObject<?> cachedObj = (CachedObject<?>) this.getOrCreate().computeIfAbsent(id, key -> new CachedObject(key, deserializer, hasher));
        return (T) cachedObj.get((ItemStack) this);
    }

    private Map<ResourceLocation, CachedObject<?>> getOrCreate() {
        if (this.cachedObjects == null) {
            synchronized (this) {
                if (this.cachedObjects == null) {
                    this.cachedObjects = new ConcurrentHashMap();
                }
            }
        }
        return this.cachedObjects;
    }
}