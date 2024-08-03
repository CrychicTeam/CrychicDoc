package com.rekindled.embers.api.event;

import java.awt.Color;
import net.minecraft.world.level.block.entity.BlockEntity;

public class HeatCoilVisualEvent extends UpgradeEvent {

    Color color;

    int particles;

    float verticalSpeed;

    public HeatCoilVisualEvent(BlockEntity tile, Color color, int particles, float verticalSpeed) {
        super(tile);
        this.color = color;
        this.particles = particles;
        this.verticalSpeed = verticalSpeed;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getParticles() {
        return this.particles;
    }

    public void setParticles(int particles) {
        this.particles = particles;
    }

    public float getVerticalSpeed() {
        return this.verticalSpeed;
    }

    public void setVerticalSpeed(float verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }
}