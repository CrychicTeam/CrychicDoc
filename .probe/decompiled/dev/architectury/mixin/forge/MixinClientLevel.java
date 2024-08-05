package dev.architectury.mixin.forge;

import dev.architectury.event.events.client.ClientTickEvent;
import java.util.function.Supplier;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ClientLevel.class })
public abstract class MixinClientLevel extends Level {

    protected MixinClientLevel(WritableLevelData arg, ResourceKey<Level> arg2, RegistryAccess arg3, Holder<DimensionType> arg4, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
        super(arg, arg2, arg3, arg4, supplier, bl, bl2, l, i);
    }

    @Inject(method = { "tickEntities" }, at = { @At("HEAD") })
    private void tickEntities(CallbackInfo ci) {
        ProfilerFiller profiler = this.m_46473_();
        profiler.push("architecturyClientLevelPreTick");
        ClientTickEvent.CLIENT_LEVEL_PRE.invoker().tick((ClientLevel) this);
        profiler.pop();
    }

    @Inject(method = { "tickEntities" }, at = { @At("RETURN") })
    private void tickEntitiesPost(CallbackInfo ci) {
        ProfilerFiller profiler = this.m_46473_();
        profiler.push("architecturyClientLevelPostTick");
        ClientTickEvent.CLIENT_LEVEL_POST.invoker().tick((ClientLevel) this);
        profiler.pop();
    }
}