package dev.kosmx.playerAnim.core.data.quarktool;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;

public class PartMap {

    public KeyframeAnimation.StateCollection part;

    public PartMap.PartValue x;

    public PartMap.PartValue y;

    public PartMap.PartValue z;

    public PartMap(KeyframeAnimation.StateCollection part) {
        this.part = part;
        this.x = new PartMap.PartValue(this.part.pitch);
        this.y = new PartMap.PartValue(this.part.yaw);
        this.z = new PartMap.PartValue(this.part.roll);
    }

    static class PartValue {

        private float value;

        private int lastTick;

        private final KeyframeAnimation.StateCollection.State timeline;

        private PartValue(KeyframeAnimation.StateCollection.State timeline) {
            this.timeline = timeline;
        }

        public void addValue(int tick, float value, Ease ease) {
            this.lastTick = tick;
            this.timeline.addKeyFrame(tick, value, ease);
        }

        public void addValue(int tickFrom, int tickTo, float value, Ease ease) throws QuarkParsingError {
            if (tickFrom < this.lastTick) {
                throw new QuarkParsingError();
            } else {
                if (tickFrom == this.lastTick && this.timeline.getKeyFrames().size() != 0) {
                    this.timeline.replaceEase(this.timeline.findAtTick(tickFrom), ease);
                } else {
                    this.timeline.addKeyFrame(tickFrom, this.value, ease);
                }
                this.value = value;
                this.lastTick = tickTo;
                this.timeline.addKeyFrame(tickTo, this.value, Ease.CONSTANT);
            }
        }

        public float getValue() {
            return this.value;
        }

        public void hold() {
            this.timeline.replaceEase(this.timeline.length() - 1, Ease.CONSTANT);
        }

        public void setValue(float valueAfter) {
            this.value = valueAfter;
        }
    }
}