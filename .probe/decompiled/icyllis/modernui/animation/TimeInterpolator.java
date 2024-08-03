package icyllis.modernui.animation;

import icyllis.modernui.annotation.NonNull;

@FunctionalInterface
public interface TimeInterpolator {

    @NonNull
    TimeInterpolator LINEAR = t -> t;

    @NonNull
    TimeInterpolator ACCELERATE = t -> t * t;

    @NonNull
    TimeInterpolator DECELERATE = t -> 1.0F - (1.0F - t) * (1.0F - t);

    @NonNull
    TimeInterpolator DECELERATE_CUBIC = t -> t * t * --t + 1.0F;

    @NonNull
    TimeInterpolator DECELERATE_QUINTIC = t -> t * t * t * t * --t + 1.0F;

    @NonNull
    TimeInterpolator ACCELERATE_DECELERATE = t -> (float) Math.cos(((double) t + 1.0) * Math.PI) * 0.5F + 0.5F;

    @NonNull
    TimeInterpolator SINE = t -> (float) Math.sin((Math.PI / 2) * (double) t);

    @NonNull
    TimeInterpolator ANTICIPATE = t -> t * t * (3.0F * t - 2.0F);

    @NonNull
    TimeInterpolator OVERSHOOT = t -> t * --t * (3.0F * t + 2.0F) + 1.0F;

    @NonNull
    TimeInterpolator ANTICIPATE_OVERSHOOT = new AnticipateOvershootInterpolator();

    @NonNull
    TimeInterpolator BOUNCE = new BounceInterpolator();

    @NonNull
    TimeInterpolator VISCOUS_FLUID = new ViscousFluidInterpolator();

    float getInterpolation(float var1);

    @NonNull
    static TimeInterpolator linear() {
        return LINEAR;
    }

    @NonNull
    static TimeInterpolator accelerate() {
        return ACCELERATE;
    }

    @NonNull
    static TimeInterpolator accelerate(float factor) {
        if (factor == 1.0F) {
            return ACCELERATE;
        } else {
            double f = (double) factor * 2.0;
            return t -> (float) Math.pow((double) t, f);
        }
    }

    @NonNull
    static TimeInterpolator decelerate() {
        return DECELERATE;
    }

    @NonNull
    static TimeInterpolator decelerate(float factor) {
        if (factor == 1.0F) {
            return DECELERATE;
        } else if (factor == 1.5F) {
            return DECELERATE_CUBIC;
        } else if (factor == 2.5F) {
            return DECELERATE_QUINTIC;
        } else {
            double f = (double) factor * 2.0;
            return t -> (float) (1.0 - Math.pow(1.0 - (double) t, f));
        }
    }

    @NonNull
    static TimeInterpolator cycle(float cycle) {
        if (cycle == 0.25F) {
            return SINE;
        } else {
            double f = (Math.PI * 2) * (double) cycle;
            return t -> (float) Math.sin(f * (double) t);
        }
    }

    @NonNull
    static TimeInterpolator anticipate() {
        return ANTICIPATE;
    }

    @NonNull
    static TimeInterpolator anticipate(float tension) {
        return tension == 2.0F ? ANTICIPATE : t -> t * t * ((tension + 1.0F) * t - tension);
    }

    @NonNull
    static TimeInterpolator overshoot() {
        return OVERSHOOT;
    }

    @NonNull
    static TimeInterpolator overshoot(float tension) {
        return tension == 2.0F ? OVERSHOOT : t -> t * --t * ((tension + 1.0F) * t + tension) + 1.0F;
    }

    @NonNull
    static TimeInterpolator bounce() {
        return BOUNCE;
    }
}