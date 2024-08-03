package net.minecraft.world.entity.ai.sensing;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class Sensing {

    private final Mob mob;

    private final IntSet seen = new IntOpenHashSet();

    private final IntSet unseen = new IntOpenHashSet();

    public Sensing(Mob mob0) {
        this.mob = mob0;
    }

    public void tick() {
        this.seen.clear();
        this.unseen.clear();
    }

    public boolean hasLineOfSight(Entity entity0) {
        int $$1 = entity0.getId();
        if (this.seen.contains($$1)) {
            return true;
        } else if (this.unseen.contains($$1)) {
            return false;
        } else {
            this.mob.m_9236_().getProfiler().push("hasLineOfSight");
            boolean $$2 = this.mob.m_142582_(entity0);
            this.mob.m_9236_().getProfiler().pop();
            if ($$2) {
                this.seen.add($$1);
            } else {
                this.unseen.add($$1);
            }
            return $$2;
        }
    }
}