package net.minecraft.world.entity.ai.behavior.declarative;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.K1;
import java.util.Optional;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public final class MemoryAccessor<F extends K1, Value> {

    private final Brain<?> brain;

    private final MemoryModuleType<Value> memoryType;

    private final App<F, Value> value;

    public MemoryAccessor(Brain<?> brain0, MemoryModuleType<Value> memoryModuleTypeValue1, App<F, Value> appFValue2) {
        this.brain = brain0;
        this.memoryType = memoryModuleTypeValue1;
        this.value = appFValue2;
    }

    public App<F, Value> value() {
        return this.value;
    }

    public void set(Value value0) {
        this.brain.setMemory(this.memoryType, Optional.of(value0));
    }

    public void setOrErase(Optional<Value> optionalValue0) {
        this.brain.setMemory(this.memoryType, optionalValue0);
    }

    public void setWithExpiry(Value value0, long long1) {
        this.brain.setMemoryWithExpiry(this.memoryType, value0, long1);
    }

    public void erase() {
        this.brain.eraseMemory(this.memoryType);
    }
}