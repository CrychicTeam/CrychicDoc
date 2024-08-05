package net.liopyu.entityjs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

@Info("This event is fired during entity creation and is responsible\nfor adding the `MemoryModuleType` and `SensorType`s the used\nby the entity.\n\nThis is only posted for entities made through a builder\n")
public class BuildBrainProviderEventJS<T extends LivingEntity> extends EventJS {

    private final List<MemoryModuleType<?>> memories = new ArrayList();

    private final List<SensorType<? extends Sensor<? super LivingEntity>>> sensors = new ArrayList();

    @Info("Adds the provided `MemoryModuleType` to the entity type's memories")
    public void addMemory(MemoryModuleType<?> memory) {
        this.memories.add(memory);
    }

    @Info("Adds the provided `SensorType` to the entity type's sensors")
    public void addSensor(SensorType<? extends Sensor<? super LivingEntity>> sensor) {
        this.sensors.add(sensor);
    }

    public Brain.Provider<T> provide() {
        return Brain.provider(this.memories, this.sensors);
    }
}