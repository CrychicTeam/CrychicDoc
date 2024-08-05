package com.mna.entities.state;

import java.util.HashMap;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;

public class EntityStateMachine<T extends Entity> {

    private HashMap<String, StateSequence<T>> sequences = new HashMap();

    private String curSequence = "";

    private EntityDataAccessor<String> CURRENT_SEQUENCE;

    private T entityRef;

    public EntityStateMachine(T entityRef, EntityDataAccessor<String> currentSequence) {
        this.CURRENT_SEQUENCE = currentSequence;
        this.entityRef = entityRef;
    }

    public SequenceEntry<T> addSequenceEntry(String name, int delay) {
        if (!this.sequences.containsKey(name)) {
            this.sequences.put(name, new StateSequence());
        }
        StateSequence<T> sequence = (StateSequence<T>) this.sequences.get(name);
        return sequence.AddEntry(delay);
    }

    public void runSequence(String name) {
        if (this.sequences.containsKey(name) && !this.curSequence.equals(name)) {
            this.entityRef.getEntityData().set(this.CURRENT_SEQUENCE, name);
            this.curSequence = name;
            StateSequence<T> current = (StateSequence<T>) this.sequences.get(this.curSequence);
            current.reset();
        }
    }

    public void tick() {
        String seq = this.entityRef.getEntityData().get(this.CURRENT_SEQUENCE);
        if (!this.curSequence.equals(seq)) {
            this.runSequence(seq);
        }
        StateSequence<T> current = (StateSequence<T>) this.sequences.get(this.curSequence);
        if (current != null && !current.isComplete()) {
            current.tick();
        }
    }

    public boolean isComplete() {
        if (!this.sequences.containsKey(this.curSequence)) {
            return true;
        } else {
            StateSequence<T> current = (StateSequence<T>) this.sequences.get(this.curSequence);
            return current.isComplete();
        }
    }
}