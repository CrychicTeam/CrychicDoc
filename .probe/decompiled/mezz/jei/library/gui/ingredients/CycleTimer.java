package mezz.jei.library.gui.ingredients;

import java.util.List;
import java.util.Optional;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.gui.screens.Screen;

public class CycleTimer {

    private static final int cycleTime = 1000;

    private long startTime;

    private long drawTime;

    private long pausedDuration = 0L;

    public CycleTimer(int offset) {
        long time = System.currentTimeMillis();
        this.startTime = time - (long) offset * 1000L;
        this.drawTime = time;
    }

    public Optional<ITypedIngredient<?>> getCycledItem(List<Optional<ITypedIngredient<?>>> list) {
        if (list.isEmpty()) {
            return Optional.empty();
        } else {
            long index = (this.drawTime - this.startTime) / 1000L % (long) list.size();
            return (Optional<ITypedIngredient<?>>) list.get(Math.toIntExact(index));
        }
    }

    public void onDraw() {
        if (!Screen.hasShiftDown()) {
            if (this.pausedDuration > 0L) {
                this.startTime = this.startTime + this.pausedDuration;
                this.pausedDuration = 0L;
            }
            this.drawTime = System.currentTimeMillis();
        } else {
            this.pausedDuration = System.currentTimeMillis() - this.drawTime;
        }
    }
}