package dev.xkmc.l2archery.content.stats;

import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import dev.xkmc.l2library.base.NamedEntry;

public class BowArrowStatType extends NamedEntry<BowArrowStatType> {

    public final StatType type;

    public final double defaultValue;

    public BowArrowStatType(StatType type, double defaultValue) {
        super(ArcheryRegister.STAT_TYPE);
        this.type = type;
        this.defaultValue = defaultValue;
    }

    public double getDefault() {
        return this.defaultValue;
    }
}