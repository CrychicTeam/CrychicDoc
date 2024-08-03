package software.bernie.geckolib.core.keyframe;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;

public record KeyframeStack<T extends Keyframe<?>>(List<T> xKeyframes, List<T> yKeyframes, List<T> zKeyframes) {

    public KeyframeStack() {
        this(new ObjectArrayList(), new ObjectArrayList(), new ObjectArrayList());
    }

    public static <F extends Keyframe<?>> KeyframeStack<F> from(KeyframeStack<F> otherStack) {
        return new KeyframeStack<>(otherStack.xKeyframes, otherStack.yKeyframes, otherStack.zKeyframes);
    }

    public double getLastKeyframeTime() {
        double xTime = 0.0;
        double yTime = 0.0;
        double zTime = 0.0;
        for (T frame : this.xKeyframes()) {
            xTime += frame.length();
        }
        for (T frame : this.yKeyframes()) {
            yTime += frame.length();
        }
        for (T frame : this.zKeyframes()) {
            zTime += frame.length();
        }
        return Math.max(xTime, Math.max(yTime, zTime));
    }
}