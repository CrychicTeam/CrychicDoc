package snownee.jade.overlay;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.util.SmoothChasingValue;

public class ProgressTracker {

    private final ListMultimap<ResourceLocation, ProgressTracker.TrackInfo> map = ArrayListMultimap.create();

    public ProgressTracker.TrackInfo createInfo(ResourceLocation tag, float progress, boolean canDecrease, float expectedWidth) {
        List<ProgressTracker.TrackInfo> infos = this.map.get(tag);
        ProgressTracker.TrackInfo info = null;
        for (ProgressTracker.TrackInfo o : infos) {
            if (!o.updatedThisTick) {
                info = o;
                break;
            }
        }
        if (info == null) {
            info = new ProgressTracker.TrackInfo();
            info.width = expectedWidth;
            info.progress.start(progress);
            this.map.put(tag, info);
        }
        info.updatedThisTick = true;
        if (progress == info.progress.getTarget()) {
            info.ticksSinceValueChanged++;
        } else {
            if (info.ticksSinceValueChanged > 10) {
                info.progress.withSpeed(0.4F);
            } else if (canDecrease || progress >= info.progress.getTarget()) {
                float spd = Math.abs(progress - info.progress.getTarget()) / (float) info.ticksSinceValueChanged;
                spd = Math.max(0.1F, 4.0F * spd);
                info.progress.withSpeed(spd);
            }
            info.ticksSinceValueChanged = 1;
        }
        if (canDecrease || !(progress < info.progress.getTarget())) {
            info.progress.target(progress);
        } else if (info.progress.isMoving()) {
            info.progress.withSpeed(Math.max(0.5F, info.progress.getSpeed()));
            if (info.progress.getTarget() > 0.9F) {
                info.progress.target(1.0F);
            }
        } else {
            info.progress.start(progress);
        }
        if (info.width != expectedWidth && (expectedWidth > info.width || ++info.ticksSinceWidthChanged > 10)) {
            info.width = expectedWidth;
            info.ticksSinceWidthChanged = 0;
        }
        return info;
    }

    public void tick() {
        this.map.values().removeIf(info -> {
            if (info.updatedThisTick) {
                info.updatedThisTick = false;
                return false;
            } else {
                return true;
            }
        });
    }

    public void clear() {
        this.map.clear();
    }

    public static class TrackInfo {

        private float width;

        private int ticksSinceWidthChanged;

        private int ticksSinceValueChanged;

        private boolean updatedThisTick;

        private SmoothChasingValue progress = new SmoothChasingValue();

        public float getWidth() {
            return this.width;
        }

        public float tick(float pTicks) {
            this.progress.tick(pTicks);
            return this.progress.value;
        }
    }
}