package fr.frinn.custommachinery.common.util;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class CycleTimer {

    private final Supplier<Integer> cycleTime;

    private long startTime;

    private long drawTime;

    private long pausedDuration = 0L;

    public CycleTimer(Supplier<Integer> cycleTime) {
        this.cycleTime = cycleTime;
        long time = System.currentTimeMillis();
        this.startTime = time - (long) ((Integer) cycleTime.get()).intValue();
        this.drawTime = time;
    }

    @Nullable
    public <T> T get(List<T> list) {
        if (list.isEmpty()) {
            return null;
        } else {
            long index = (this.drawTime - this.startTime) / (long) ((Integer) this.cycleTime.get()).intValue() % (long) list.size();
            return (T) list.get(Math.toIntExact(index));
        }
    }

    public <T> T getOrDefault(List<T> list, T defaultObject) {
        return (T) Optional.ofNullable(this.get(list)).orElse(defaultObject);
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