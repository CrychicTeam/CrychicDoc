package software.bernie.geckolib.core.keyframe.event.data;

import java.util.Objects;

public abstract class KeyFrameData {

    private final double startTick;

    public KeyFrameData(double startTick) {
        this.startTick = startTick;
    }

    public double getStartTick() {
        return this.startTick;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj != null && this.getClass() == obj.getClass() ? this.hashCode() == obj.hashCode() : false;
        }
    }

    public int hashCode() {
        return Objects.hashCode(this.startTick);
    }
}