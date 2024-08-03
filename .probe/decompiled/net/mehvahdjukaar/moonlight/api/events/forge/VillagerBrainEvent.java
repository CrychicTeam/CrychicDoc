package net.mehvahdjukaar.moonlight.api.events.forge;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.events.IVillagerBrainEvent;
import net.mehvahdjukaar.moonlight.core.misc.VillagerBrainEventInternal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.ExpirableValue;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

public class VillagerBrainEvent extends Event implements IVillagerBrainEvent {

    private final VillagerBrainEventInternal internal;

    public VillagerBrainEvent(Brain<Villager> brain, Villager villager) {
        this.internal = new VillagerBrainEventInternal(brain, villager);
    }

    @Override
    public Villager getVillager() {
        return this.internal.getVillager();
    }

    @Override
    public Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> getMemories() {
        return this.internal.getMemories();
    }

    @Override
    public void addOrReplaceActivity(Activity activity, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super Villager>>> activityPackage) {
        this.internal.addOrReplaceActivity(activity, activityPackage);
    }

    @Override
    public void scheduleActivity(Activity activity, int startTime, int endTime) {
        this.internal.scheduleActivity(activity, startTime, endTime);
    }

    @Override
    public void addSensor(SensorType<? extends Sensor<Villager>> newSensor) {
        this.internal.addSensor(newSensor);
    }

    @Override
    public <P extends Pair<Integer, ? extends Behavior<Villager>>> boolean addTaskToActivity(Activity activity, P task) {
        return this.internal.addTaskToActivity(activity, task);
    }

    @Internal
    @Override
    public VillagerBrainEventInternal getInternal() {
        return this.internal;
    }
}