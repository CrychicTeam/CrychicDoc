package dev.architectury.mixin.forge;

import dev.architectury.event.forge.EventHandlerImplCommon;
import java.lang.ref.WeakReference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ChunkSerializer.class })
public class MixinChunkSerializer {

    @Unique
    private static ThreadLocal<WeakReference<ServerLevel>> level = new ThreadLocal();

    @Inject(method = { "read" }, at = { @At("HEAD") })
    private static void read(ServerLevel worldIn, PoiManager arg2, ChunkPos arg3, CompoundTag arg4, CallbackInfoReturnable<ProtoChunk> cir) {
        level.set(new WeakReference(worldIn));
    }

    @ModifyArg(method = { "read" }, at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraftforge/eventbus/api/IEventBus;post(Lnet/minecraftforge/eventbus/api/Event;)Z"), index = 0)
    private static Event modifyProtoChunkLevel(Event event) {
        WeakReference<ServerLevel> levelRef = (WeakReference<ServerLevel>) level.get();
        if (levelRef != null && event instanceof ChunkDataEvent.Load load) {
            ((EventHandlerImplCommon.LevelEventAttachment) load).architectury$attachLevel((LevelAccessor) levelRef.get());
        }
        level.remove();
        return event;
    }
}