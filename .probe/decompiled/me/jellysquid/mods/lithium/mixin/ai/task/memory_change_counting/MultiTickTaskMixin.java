package me.jellysquid.mods.lithium.mixin.ai.task.memory_change_counting;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap.Entry;
import java.util.Map;
import me.jellysquid.mods.lithium.common.ai.MemoryModificationCounter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Behavior.class })
public class MultiTickTaskMixin<E extends LivingEntity> {

    @Mutable
    @Shadow
    @Final
    protected Map<MemoryModuleType<?>, MemoryStatus> entryCondition;

    @Unique
    private long cachedMemoryModCount = -1L;

    @Unique
    private boolean cachedHasRequiredMemoryState;

    @Inject(method = { "<init>(Ljava/util/Map;II)V" }, at = { @At("RETURN") })
    private void init(Map<MemoryModuleType<?>, MemoryStatus> map, int int_1, int int_2, CallbackInfo ci) {
        this.entryCondition = new Reference2ObjectOpenHashMap(map);
    }

    @Overwrite
    public boolean hasRequiredMemories(E entity) {
        Brain<?> brain = entity.getBrain();
        long modCount = ((MemoryModificationCounter) brain).getModCount();
        if (this.cachedMemoryModCount == modCount) {
            return this.cachedHasRequiredMemoryState;
        } else {
            this.cachedMemoryModCount = modCount;
            ObjectIterator<Entry<MemoryModuleType<?>, MemoryStatus>> fastIterator = ((Reference2ObjectOpenHashMap) this.entryCondition).reference2ObjectEntrySet().fastIterator();
            while (fastIterator.hasNext()) {
                Entry<MemoryModuleType<?>, MemoryStatus> entry = (Entry<MemoryModuleType<?>, MemoryStatus>) fastIterator.next();
                if (!brain.checkMemory((MemoryModuleType<?>) entry.getKey(), (MemoryStatus) entry.getValue())) {
                    return this.cachedHasRequiredMemoryState = false;
                }
            }
            return this.cachedHasRequiredMemoryState = true;
        }
    }
}