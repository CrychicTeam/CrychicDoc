package com.simibubi.create.api.event;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import java.lang.reflect.Type;
import java.util.Map;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.GenericEvent;

public class BlockEntityBehaviourEvent<T extends SmartBlockEntity> extends GenericEvent<T> {

    private T smartBlockEntity;

    private Map<BehaviourType<?>, BlockEntityBehaviour> behaviours;

    public BlockEntityBehaviourEvent(T blockEntity, Map<BehaviourType<?>, BlockEntityBehaviour> behaviours) {
        this.smartBlockEntity = blockEntity;
        this.behaviours = behaviours;
    }

    public Type getGenericType() {
        return this.smartBlockEntity.getClass();
    }

    public void attach(BlockEntityBehaviour behaviour) {
        this.behaviours.put(behaviour.getType(), behaviour);
    }

    public BlockEntityBehaviour remove(BehaviourType<?> type) {
        return (BlockEntityBehaviour) this.behaviours.remove(type);
    }

    public T getBlockEntity() {
        return this.smartBlockEntity;
    }

    public BlockState getBlockState() {
        return this.smartBlockEntity.m_58900_();
    }
}