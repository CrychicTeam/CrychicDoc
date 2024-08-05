package org.violetmoon.zeta.world;

import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.world.generator.Generator;

public record WeightedGenerator(ZetaModule module, Generator generator, int weight) implements Comparable<WeightedGenerator> {

    public int compareTo(@NotNull WeightedGenerator o) {
        int diff = this.weight - o.weight;
        return diff != 0 ? diff : this.hashCode() - o.hashCode();
    }

    public int hashCode() {
        return this.generator.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            if (obj instanceof WeightedGenerator gen && gen.generator == this.generator) {
                return true;
            }
            return false;
        }
    }
}