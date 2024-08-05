package dev.architectury.mixin.forge;

import dev.architectury.event.forge.EventHandlerImplCommon;
import java.lang.ref.WeakReference;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.loot.LootDataManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LootDataManager.class })
public class MixinLootDataManager {

    @Inject(method = { "reload" }, at = { @At("HEAD") })
    private void reload(PreparableReloadListener.PreparationBarrier arg, ResourceManager arg2, ProfilerFiller arg3, ProfilerFiller arg4, Executor executor, Executor executor2, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        EventHandlerImplCommon.lootDataManagerRef = new WeakReference((LootDataManager) this);
    }
}