package net.minecraftforge.server.timings;

import java.lang.ref.WeakReference;

public class ForgeTimings<T> {

    private WeakReference<T> object;

    private int[] rawTimingData;

    public ForgeTimings(T object, int[] rawTimingData) {
        this.object = new WeakReference(object);
        this.rawTimingData = rawTimingData;
    }

    public WeakReference<T> getObject() {
        return this.object;
    }

    public double getAverageTimings() {
        double sum = 0.0;
        for (int data : this.rawTimingData) {
            sum += (double) data;
        }
        return sum / (double) this.rawTimingData.length;
    }
}