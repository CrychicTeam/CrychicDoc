package com.simibubi.create.foundation.utility.animation;

public interface Force {

    float get(float var1, float var2, float var3);

    boolean finished();

    public static class Drag implements Force {

        final float dragFactor;

        public Drag(float dragFactor) {
            this.dragFactor = dragFactor;
        }

        @Override
        public float get(float mass, float value, float speed) {
            return -speed * this.dragFactor;
        }

        @Override
        public boolean finished() {
            return false;
        }
    }

    public static class Impulse implements Force {

        float force;

        public Impulse(float force) {
            this.force = force;
        }

        @Override
        public float get(float mass, float value, float speed) {
            return this.force;
        }

        @Override
        public boolean finished() {
            return true;
        }
    }

    public static class OverTime implements Force {

        int timeRemaining;

        float f;

        public OverTime(int time, float totalAcceleration) {
            this.timeRemaining = time;
            this.f = totalAcceleration / (float) time;
        }

        @Override
        public float get(float mass, float value, float speed) {
            this.timeRemaining--;
            return this.f;
        }

        @Override
        public boolean finished() {
            return this.timeRemaining <= 0;
        }
    }

    public static class Static implements Force {

        float force;

        public Static(float force) {
            this.force = force;
        }

        @Override
        public float get(float mass, float value, float speed) {
            return this.force;
        }

        @Override
        public boolean finished() {
            return false;
        }
    }

    public static class Zeroing implements Force {

        final float g;

        public Zeroing(float g) {
            this.g = g / 20.0F;
        }

        @Override
        public float get(float mass, float value, float speed) {
            return -Math.signum(value) * this.g * mass;
        }

        @Override
        public boolean finished() {
            return false;
        }
    }
}