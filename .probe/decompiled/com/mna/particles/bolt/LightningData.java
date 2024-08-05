package com.mna.particles.bolt;

import com.mna.tools.math.Vector3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class LightningData {

    private ArrayList<Segment> segments;

    private Vector3 start;

    private Vector3 end;

    private float length;

    private int speed;

    private boolean finalized;

    private Random rand;

    private int age;

    private int maxAge;

    private float maxOffset = 0.5F;

    public LightningData(Vector3 start, Vector3 end, long seed) {
        this.segments = new ArrayList();
        this.start = start;
        this.end = end;
        this.rand = new Random(seed);
        this.speed = 4;
        this.length = this.end.sub(this.start).length();
        this.maxAge = 10;
        this.age = 0;
        this.segments.add(new Segment(this.start, this.end));
    }

    public LightningData(Vector3 start, Vector3 end, long seed, int maxAge) {
        this.segments = new ArrayList();
        this.start = start;
        this.end = end;
        this.rand = new Random(seed);
        this.speed = 4;
        this.length = this.end.sub(this.start).length();
        this.maxAge = maxAge;
        this.age = 0;
        this.segments.add(new Segment(this.start, this.end));
    }

    public void setMaxOffset(float offset) {
        this.maxOffset = offset;
    }

    public void split() {
        if (!this.finalized) {
            ArrayList<Segment> oldSegments = this.segments;
            this.segments = new ArrayList();
            for (Segment segment : oldSegments) {
                Vector3[] newPoints = new Vector3[] { segment.getStart(), null, segment.getEnd() };
                Vector3 offset = Vector3.getPerpendicular(segment.getDiff()).rotate(this.rand.nextFloat() * 360.0F, segment.getDiff()).normalize();
                float offsetScale = -this.maxOffset + this.rand.nextFloat() * this.maxOffset * 2.0F;
                offset = offset.scale(offsetScale * segment.getDiff().length());
                offset = offset.add(segment.getDiff().scale(0.5F)).add(segment.getStart());
                newPoints[1] = offset;
                for (int i = 0; i < newPoints.length - 1; i++) {
                    Segment seg = new Segment(newPoints[i], newPoints[i + 1], segment.light);
                    this.segments.add(seg);
                }
            }
        }
    }

    public void fractalize() {
        this.fractalize(5);
    }

    public void fractalize(int count) {
        for (int i = 0; i < count; i++) {
            this.split();
        }
    }

    public void finalize() {
        if (!this.finalized) {
            this.finalized = true;
            Collections.sort(this.segments, new LightningData.SegmentSorterLightValue());
        }
    }

    public void onUpdate() {
        this.age = this.age + this.speed;
        if (this.age > this.maxAge) {
            this.age = this.maxAge;
        }
    }

    public int getAge() {
        return this.age;
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getLength() {
        return this.length;
    }

    public int numSegments() {
        return this.segments.size();
    }

    public ArrayList<Segment> getSegments() {
        return this.segments;
    }

    public boolean isFinal() {
        return this.finalized;
    }

    class SegmentSorterLightValue implements Comparator<Segment> {

        public int compare(Segment a, Segment b) {
            return Float.compare(b.light, a.light);
        }
    }
}