package icyllis.modernui.transition;

import icyllis.modernui.view.View;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;

public class TransitionValues {

    @Nonnull
    public final View view;

    public final Map<String, Object> values = new Object2ObjectOpenHashMap();

    final ArrayList<Transition> mTargetedTransitions = new ArrayList();

    public TransitionValues(@Nonnull View view) {
        this.view = view;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            TransitionValues that = (TransitionValues) o;
            return this.view == that.view && this.values.equals(that.values);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.view.hashCode();
        return 31 * result + this.values.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("TransitionValues@" + Integer.toHexString(this.hashCode()) + ":\n");
        sb.append("    view = ").append(this.view).append("\n");
        sb.append("    values:");
        for (Entry<String, Object> e : this.values.entrySet()) {
            sb.append("    ").append((String) e.getKey()).append(": ").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }
}