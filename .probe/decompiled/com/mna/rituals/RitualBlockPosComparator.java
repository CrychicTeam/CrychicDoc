package com.mna.rituals;

import com.mna.api.rituals.RitualBlockPos;
import java.util.Comparator;

public class RitualBlockPosComparator implements Comparator<RitualBlockPos> {

    private boolean compareDisplay = false;

    public int compare(RitualBlockPos o1, RitualBlockPos o2) {
        if (this.compareDisplay) {
            if (o1.getDisplayIndex() < o2.getDisplayIndex()) {
                return -1;
            } else {
                return o1.getDisplayIndex() == o2.getDisplayIndex() ? 0 : 1;
            }
        } else if (o1.getIndex() < o2.getIndex()) {
            return -1;
        } else {
            return o1.getIndex() == o2.getIndex() ? 0 : 1;
        }
    }

    public void setCompareDisplay() {
        this.compareDisplay = true;
    }

    public void setCompareIndex() {
        this.compareDisplay = false;
    }
}