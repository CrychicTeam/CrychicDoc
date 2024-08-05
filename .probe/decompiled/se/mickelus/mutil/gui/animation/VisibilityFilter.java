package se.mickelus.mutil.gui.animation;

public class VisibilityFilter {

    private final float OPACITY_STEP = 0.1F;

    private final int DECREASE_DELAY = 100;

    private final float min;

    private final float max;

    private float input;

    private float output;

    private int delay = 100;

    public VisibilityFilter(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public float apply(float value) {
        this.input = value;
        this.updateOutput();
        return this.output;
    }

    public float get() {
        return this.output;
    }

    private void updateOutput() {
        if (this.min < this.input && this.input < this.max) {
            if (this.output + 0.1F < 1.0F) {
                this.output += 0.1F;
            } else {
                this.output = 1.0F;
            }
            this.delay = 100;
        } else if (this.delay == 0) {
            if (this.output - 0.1F > 0.0F) {
                this.output -= 0.1F;
            } else {
                this.output = 0.0F;
            }
        } else {
            this.delay--;
        }
    }
}