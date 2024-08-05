package software.bernie.geckolib.core.keyframe;

import com.eliotlash.mclib.math.IValue;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import software.bernie.geckolib.core.animation.EasingType;

public record Keyframe<T extends IValue>(double length, T startValue, T endValue, EasingType easingType, List<T> easingArgs) {

    public Keyframe(double length, T startValue, T endValue) {
        this(length, startValue, endValue, EasingType.LINEAR);
    }

    public Keyframe(double length, T startValue, T endValue, EasingType easingType) {
        this(length, startValue, endValue, easingType, new ObjectArrayList(0));
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.length, this.startValue, this.endValue, this.easingType, this.easingArgs });
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj != null && this.getClass() == obj.getClass() ? this.hashCode() == obj.hashCode() : false;
        }
    }
}