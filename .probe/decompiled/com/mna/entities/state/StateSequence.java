package com.mna.entities.state;

import java.util.ArrayList;
import net.minecraft.world.entity.Entity;

public class StateSequence<T extends Entity> {

    private ArrayList<SequenceEntry<T>> sequence;

    private int curIndex = 0;

    private int timer = 0;

    public StateSequence() {
        this.sequence = new ArrayList();
        this.curIndex = 0;
    }

    public SequenceEntry<T> AddEntry(int delay) {
        SequenceEntry<T> entry = new SequenceEntry<>(delay);
        this.sequence.add(entry);
        return entry;
    }

    public void tick() {
        if (this.curIndex < this.sequence.size()) {
            this.timer++;
            SequenceEntry<T> curEntry = (SequenceEntry<T>) this.sequence.get(this.curIndex);
            if (this.timer > curEntry.getDelay()) {
                curEntry.complete();
                this.timer = 0;
                this.curIndex++;
            } else {
                curEntry.tick(this.timer);
            }
        }
    }

    public void reset() {
        this.timer = 0;
        this.curIndex = 0;
    }

    public boolean isComplete() {
        return this.curIndex >= this.sequence.size();
    }
}