package com.simibubi.create.content.kinetics;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KineticNetwork {

    public Long id;

    public boolean initialized;

    public Map<KineticBlockEntity, Float> sources = new HashMap();

    public Map<KineticBlockEntity, Float> members = new HashMap();

    private float currentCapacity;

    private float currentStress;

    private float unloadedCapacity;

    private float unloadedStress;

    private int unloadedMembers;

    public void initFromTE(float maxStress, float currentStress, int members) {
        this.unloadedCapacity = maxStress;
        this.unloadedStress = currentStress;
        this.unloadedMembers = members;
        this.initialized = true;
        this.updateStress();
        this.updateCapacity();
    }

    public void addSilently(KineticBlockEntity be, float lastCapacity, float lastStress) {
        if (!this.members.containsKey(be)) {
            if (be.isSource()) {
                this.unloadedCapacity = this.unloadedCapacity - lastCapacity * getStressMultiplierForSpeed(be.getGeneratedSpeed());
                float addedStressCapacity = be.calculateAddedStressCapacity();
                this.sources.put(be, addedStressCapacity);
            }
            this.unloadedStress = this.unloadedStress - lastStress * getStressMultiplierForSpeed(be.getTheoreticalSpeed());
            float stressApplied = be.calculateStressApplied();
            this.members.put(be, stressApplied);
            this.unloadedMembers--;
            if (this.unloadedMembers < 0) {
                this.unloadedMembers = 0;
            }
            if (this.unloadedCapacity < 0.0F) {
                this.unloadedCapacity = 0.0F;
            }
            if (this.unloadedStress < 0.0F) {
                this.unloadedStress = 0.0F;
            }
        }
    }

    public void add(KineticBlockEntity be) {
        if (!this.members.containsKey(be)) {
            if (be.isSource()) {
                this.sources.put(be, be.calculateAddedStressCapacity());
            }
            this.members.put(be, be.calculateStressApplied());
            this.updateFromNetwork(be);
            be.networkDirty = true;
        }
    }

    public void updateCapacityFor(KineticBlockEntity be, float capacity) {
        this.sources.put(be, capacity);
        this.updateCapacity();
    }

    public void updateStressFor(KineticBlockEntity be, float stress) {
        this.members.put(be, stress);
        this.updateStress();
    }

    public void remove(KineticBlockEntity be) {
        if (this.members.containsKey(be)) {
            if (be.isSource()) {
                this.sources.remove(be);
            }
            this.members.remove(be);
            be.updateFromNetwork(0.0F, 0.0F, 0);
            if (this.members.isEmpty()) {
                ((Map) TorquePropagator.networks.get(be.m_58904_())).remove(this.id);
            } else {
                this.members.keySet().stream().findFirst().map(member -> member.networkDirty = true);
            }
        }
    }

    public void sync() {
        for (KineticBlockEntity be : this.members.keySet()) {
            this.updateFromNetwork(be);
        }
    }

    private void updateFromNetwork(KineticBlockEntity be) {
        be.updateFromNetwork(this.currentCapacity, this.currentStress, this.getSize());
    }

    public void updateCapacity() {
        float newMaxStress = this.calculateCapacity();
        if (this.currentCapacity != newMaxStress) {
            this.currentCapacity = newMaxStress;
            this.sync();
        }
    }

    public void updateStress() {
        float newStress = this.calculateStress();
        if (this.currentStress != newStress) {
            this.currentStress = newStress;
            this.sync();
        }
    }

    public void updateNetwork() {
        float newStress = this.calculateStress();
        float newMaxStress = this.calculateCapacity();
        if (this.currentStress != newStress || this.currentCapacity != newMaxStress) {
            this.currentStress = newStress;
            this.currentCapacity = newMaxStress;
            this.sync();
        }
    }

    public float calculateCapacity() {
        float presentCapacity = 0.0F;
        Iterator<KineticBlockEntity> iterator = this.sources.keySet().iterator();
        while (iterator.hasNext()) {
            KineticBlockEntity be = (KineticBlockEntity) iterator.next();
            if (be.m_58904_().getBlockEntity(be.m_58899_()) != be) {
                iterator.remove();
            } else {
                presentCapacity += this.getActualCapacityOf(be);
            }
        }
        return presentCapacity + this.unloadedCapacity;
    }

    public float calculateStress() {
        float presentStress = 0.0F;
        Iterator<KineticBlockEntity> iterator = this.members.keySet().iterator();
        while (iterator.hasNext()) {
            KineticBlockEntity be = (KineticBlockEntity) iterator.next();
            if (be.m_58904_().getBlockEntity(be.m_58899_()) != be) {
                iterator.remove();
            } else {
                presentStress += this.getActualStressOf(be);
            }
        }
        return presentStress + this.unloadedStress;
    }

    public float getActualCapacityOf(KineticBlockEntity be) {
        return (Float) this.sources.get(be) * getStressMultiplierForSpeed(be.getGeneratedSpeed());
    }

    public float getActualStressOf(KineticBlockEntity be) {
        return (Float) this.members.get(be) * getStressMultiplierForSpeed(be.getTheoreticalSpeed());
    }

    private static float getStressMultiplierForSpeed(float speed) {
        return Math.abs(speed);
    }

    public int getSize() {
        return this.unloadedMembers + this.members.size();
    }
}