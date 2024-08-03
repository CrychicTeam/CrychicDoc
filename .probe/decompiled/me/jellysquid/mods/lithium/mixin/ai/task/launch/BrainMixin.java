package me.jellysquid.mods.lithium.mixin.ai.task.launch;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;
import me.jellysquid.mods.lithium.common.util.collections.MaskedList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ Brain.class })
public class BrainMixin<E extends LivingEntity> {

    @Shadow
    @Final
    private Map<Integer, Map<Activity, Set<BehaviorControl<? super E>>>> availableBehaviorsByPriority;

    @Shadow
    @Final
    private Set<Activity> activeActivities;

    private ArrayList<BehaviorControl<? super E>> possibleTasks;

    private MaskedList<BehaviorControl<? super E>> runningTasks;

    private void onTasksChanged() {
        this.runningTasks = null;
        this.onPossibleActivitiesChanged();
    }

    private void onPossibleActivitiesChanged() {
        this.possibleTasks = null;
    }

    private void initPossibleTasks() {
        this.possibleTasks = new ArrayList();
        for (Map<Activity, Set<BehaviorControl<? super E>>> map : this.availableBehaviorsByPriority.values()) {
            for (Entry<Activity, Set<BehaviorControl<? super E>>> entry : map.entrySet()) {
                Activity activity = (Activity) entry.getKey();
                if (this.activeActivities.contains(activity)) {
                    for (BehaviorControl<? super E> task : (Set) entry.getValue()) {
                        this.possibleTasks.add(task);
                    }
                }
            }
        }
    }

    private ArrayList<BehaviorControl<? super E>> getPossibleTasks() {
        if (this.possibleTasks == null) {
            this.initPossibleTasks();
        }
        return this.possibleTasks;
    }

    private MaskedList<BehaviorControl<? super E>> getCurrentlyRunningTasks() {
        if (this.runningTasks == null) {
            this.initCurrentlyRunningTasks();
        }
        return this.runningTasks;
    }

    private void initCurrentlyRunningTasks() {
        MaskedList<BehaviorControl<? super E>> list = new MaskedList<>(new ObjectArrayList(), false);
        for (Map<Activity, Set<BehaviorControl<? super E>>> map : this.availableBehaviorsByPriority.values()) {
            for (Set<BehaviorControl<? super E>> set : map.values()) {
                for (BehaviorControl<? super E> task : set) {
                    list.addOrSet(task, task.getStatus() == Behavior.Status.RUNNING);
                }
            }
        }
        this.runningTasks = list;
    }

    @Overwrite
    private void startEachNonRunningBehavior(ServerLevel world, E entity) {
        long startTime = world.m_46467_();
        for (BehaviorControl<? super E> task : this.getPossibleTasks()) {
            if (task.getStatus() == Behavior.Status.STOPPED) {
                task.tryStart(world, entity, startTime);
            }
        }
    }

    @Overwrite
    @Deprecated
    @VisibleForDebug
    public List<BehaviorControl<? super E>> getRunningBehaviors() {
        return this.getCurrentlyRunningTasks();
    }

    @Inject(method = { "<init>(Ljava/util/Collection;Ljava/util/Collection;Lcom/google/common/collect/ImmutableList;Ljava/util/function/Supplier;)V" }, at = { @At("RETURN") })
    private void reinitializeBrainCollections(Collection<?> memories, Collection<?> sensors, ImmutableList<?> memoryEntries, Supplier<?> codecSupplier, CallbackInfo ci) {
        this.onTasksChanged();
    }

    @Inject(method = { "setTaskList(Lnet/minecraft/entity/ai/brain/Activity;Lcom/google/common/collect/ImmutableList;Ljava/util/Set;Ljava/util/Set;)V" }, at = { @At("RETURN") })
    private void reinitializeTasksSorted(Activity activity, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<?>>> indexedTasks, Set<Pair<MemoryModuleType<?>, MemoryStatus>> requiredMemories, Set<MemoryModuleType<?>> forgettingMemories, CallbackInfo ci) {
        this.onTasksChanged();
    }

    @Inject(method = { "clear()V" }, at = { @At("RETURN") })
    private void reinitializeTasksSorted(CallbackInfo ci) {
        this.onTasksChanged();
    }

    @Inject(method = { "resetPossibleActivities(Lnet/minecraft/entity/ai/brain/Activity;)V" }, at = { @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z", shift = Shift.AFTER) })
    private void onPossibleActivitiesChanged(Activity except, CallbackInfo ci) {
        this.onPossibleActivitiesChanged();
    }

    @Inject(method = { "stopAllTasks" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/brain/task/Task;stop(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;J)V") }, locals = LocalCapture.CAPTURE_FAILHARD)
    private void removeStoppedTask(ServerLevel world, E entity, CallbackInfo ci, long l, Iterator<?> it, BehaviorControl<? super E> task) {
        if (this.runningTasks != null) {
            this.runningTasks.setVisible(task, false);
        }
    }

    @Inject(method = { "updateTasks" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/brain/task/Task;tick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;J)V", shift = Shift.AFTER) }, locals = LocalCapture.CAPTURE_FAILHARD)
    private void removeTaskIfStopped(ServerLevel world, E entity, CallbackInfo ci, long l, Iterator<?> it, BehaviorControl<? super E> task) {
        if (this.runningTasks != null && task.getStatus() != Behavior.Status.RUNNING) {
            this.runningTasks.setVisible(task, false);
        }
    }

    @ModifyVariable(method = { "startTasks" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/brain/task/Task;tryStarting(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;J)Z", shift = Shift.AFTER))
    private BehaviorControl<? super E> addStartedTasks(BehaviorControl<? super E> task) {
        if (this.runningTasks != null && task.getStatus() == Behavior.Status.RUNNING) {
            this.runningTasks.setVisible(task, true);
        }
        return task;
    }
}