package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import java.util.Set;
import net.minecraft.core.Direction;

public class VisibilitySet {

    private static final int FACINGS = Direction.values().length;

    private final BitSet data = new BitSet(FACINGS * FACINGS);

    public void add(Set<Direction> setDirection0) {
        for (Direction $$1 : setDirection0) {
            for (Direction $$2 : setDirection0) {
                this.set($$1, $$2, true);
            }
        }
    }

    public void set(Direction direction0, Direction direction1, boolean boolean2) {
        this.data.set(direction0.ordinal() + direction1.ordinal() * FACINGS, boolean2);
        this.data.set(direction1.ordinal() + direction0.ordinal() * FACINGS, boolean2);
    }

    public void setAll(boolean boolean0) {
        this.data.set(0, this.data.size(), boolean0);
    }

    public boolean visibilityBetween(Direction direction0, Direction direction1) {
        return this.data.get(direction0.ordinal() + direction1.ordinal() * FACINGS);
    }

    public String toString() {
        StringBuilder $$0 = new StringBuilder();
        $$0.append(' ');
        for (Direction $$1 : Direction.values()) {
            $$0.append(' ').append($$1.toString().toUpperCase().charAt(0));
        }
        $$0.append('\n');
        for (Direction $$2 : Direction.values()) {
            $$0.append($$2.toString().toUpperCase().charAt(0));
            for (Direction $$3 : Direction.values()) {
                if ($$2 == $$3) {
                    $$0.append("  ");
                } else {
                    boolean $$4 = this.visibilityBetween($$2, $$3);
                    $$0.append(' ').append((char) ($$4 ? 'Y' : 'n'));
                }
            }
            $$0.append('\n');
        }
        return $$0.toString();
    }
}