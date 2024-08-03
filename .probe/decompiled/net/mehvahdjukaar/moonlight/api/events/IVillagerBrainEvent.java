package net.mehvahdjukaar.moonlight.api.events;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.core.misc.VillagerBrainEventInternal;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.ExpirableValue;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface IVillagerBrainEvent extends SimpleEvent {

    Villager getVillager();

    Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> getMemories();

    void addOrReplaceActivity(Activity var1, ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super Villager>>> var2);

    void scheduleActivity(Activity var1, int var2, int var3);

    void addSensor(SensorType<? extends Sensor<Villager>> var1);

    <P extends Pair<Integer, ? extends Behavior<Villager>>> boolean addTaskToActivity(Activity var1, P var2);

    @Internal
    VillagerBrainEventInternal getInternal();
}