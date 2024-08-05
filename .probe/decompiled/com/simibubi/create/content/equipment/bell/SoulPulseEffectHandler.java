package com.simibubi.create.content.equipment.bell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class SoulPulseEffectHandler {

    private List<SoulPulseEffect> pulses = new ArrayList();

    private Set<BlockPos> occupied = new HashSet();

    public void tick(Level world) {
        for (SoulPulseEffect pulse : this.pulses) {
            List<BlockPos> spawns = pulse.tick(world);
            if (spawns != null) {
                if (pulse.canOverlap()) {
                    for (BlockPos pos : spawns) {
                        pulse.spawnParticles(world, pos);
                    }
                } else {
                    for (BlockPos pos : spawns) {
                        if (!this.occupied.contains(pos)) {
                            pulse.spawnParticles(world, pos);
                            pulse.added.add(pos);
                            this.occupied.add(pos);
                        }
                    }
                }
            }
        }
        for (SoulPulseEffect pulsex : this.pulses) {
            if (pulsex.finished() && !pulsex.canOverlap()) {
                this.occupied.removeAll(pulsex.added);
            }
        }
        this.pulses.removeIf(SoulPulseEffect::finished);
    }

    public void addPulse(SoulPulseEffect pulse) {
        this.pulses.add(pulse);
    }

    public void refresh() {
        this.pulses.clear();
        this.occupied.clear();
    }
}