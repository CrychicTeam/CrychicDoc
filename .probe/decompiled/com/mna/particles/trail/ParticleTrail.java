package com.mna.particles.trail;

import com.mna.particles.bolt.Segment;
import com.mna.tools.math.Vector3;
import java.util.LinkedList;
import java.util.List;

public class ParticleTrail {

    private LinkedList<Segment> segments = new LinkedList();

    private int maxSegments = 60;

    public ParticleTrail(long seed) {
    }

    public void tick(Vector3 prevPos, Vector3 pos) {
        this.segments.add(new Segment(prevPos, pos));
        while (this.segments.size() > this.maxSegments) {
            this.segments.pop();
        }
    }

    public List<Segment> getSegments() {
        return this.segments;
    }
}