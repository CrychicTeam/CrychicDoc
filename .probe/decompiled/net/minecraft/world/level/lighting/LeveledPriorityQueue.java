package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;

public class LeveledPriorityQueue {

    private final int levelCount;

    private final LongLinkedOpenHashSet[] queues;

    private int firstQueuedLevel;

    public LeveledPriorityQueue(int int0, final int int1) {
        this.levelCount = int0;
        this.queues = new LongLinkedOpenHashSet[int0];
        for (int $$2 = 0; $$2 < int0; $$2++) {
            this.queues[$$2] = new LongLinkedOpenHashSet(int1, 0.5F) {

                protected void rehash(int p_278313_) {
                    if (p_278313_ > int1) {
                        super.rehash(p_278313_);
                    }
                }
            };
        }
        this.firstQueuedLevel = int0;
    }

    public long removeFirstLong() {
        LongLinkedOpenHashSet $$0 = this.queues[this.firstQueuedLevel];
        long $$1 = $$0.removeFirstLong();
        if ($$0.isEmpty()) {
            this.checkFirstQueuedLevel(this.levelCount);
        }
        return $$1;
    }

    public boolean isEmpty() {
        return this.firstQueuedLevel >= this.levelCount;
    }

    public void dequeue(long long0, int int1, int int2) {
        LongLinkedOpenHashSet $$3 = this.queues[int1];
        $$3.remove(long0);
        if ($$3.isEmpty() && this.firstQueuedLevel == int1) {
            this.checkFirstQueuedLevel(int2);
        }
    }

    public void enqueue(long long0, int int1) {
        this.queues[int1].add(long0);
        if (this.firstQueuedLevel > int1) {
            this.firstQueuedLevel = int1;
        }
    }

    private void checkFirstQueuedLevel(int int0) {
        int $$1 = this.firstQueuedLevel;
        this.firstQueuedLevel = int0;
        for (int $$2 = $$1 + 1; $$2 < int0; $$2++) {
            if (!this.queues[$$2].isEmpty()) {
                this.firstQueuedLevel = $$2;
                break;
            }
        }
    }
}