package me.jellysquid.mods.lithium.mixin.ai.task.replace_streams;

import java.util.Set;
import me.jellysquid.mods.lithium.common.ai.WeightedListIterable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.GateBehavior;
import net.minecraft.world.entity.ai.behavior.ShufflingList;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ GateBehavior.class })
public abstract class CompositeTaskMixin<E extends LivingEntity> implements BehaviorControl<E> {

    @Shadow
    @Final
    private ShufflingList<BehaviorControl<? super E>> behaviors;

    @Shadow
    @Final
    private Set<MemoryModuleType<?>> exitErasedMemories;

    @Shadow
    private Behavior.Status status;

    @Overwrite
    @Override
    public final void tickOrStop(ServerLevel world, E entity, long time) {
        boolean hasOneTaskRunning = false;
        for (BehaviorControl<? super E> task : WeightedListIterable.cast(this.behaviors)) {
            if (task.getStatus() == Behavior.Status.RUNNING) {
                task.tickOrStop(world, entity, time);
                hasOneTaskRunning |= task.getStatus() == Behavior.Status.RUNNING;
            }
        }
        if (!hasOneTaskRunning) {
            this.doStop(world, entity, time);
        }
    }

    @Overwrite
    @Override
    public final void doStop(ServerLevel world, E entity, long time) {
        this.status = Behavior.Status.STOPPED;
        for (BehaviorControl<? super E> task : WeightedListIterable.cast(this.behaviors)) {
            if (task.getStatus() == Behavior.Status.RUNNING) {
                task.doStop(world, entity, time);
            }
        }
        Brain<?> brain = entity.getBrain();
        for (MemoryModuleType<?> module : this.exitErasedMemories) {
            brain.eraseMemory(module);
        }
    }
}