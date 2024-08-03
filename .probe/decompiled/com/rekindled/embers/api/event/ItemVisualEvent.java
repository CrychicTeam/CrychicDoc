package com.rekindled.embers.api.event;

import java.awt.Color;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

public class ItemVisualEvent extends Event {

    private LivingEntity entity;

    private EquipmentSlot slot;

    private ItemStack item;

    private Color color;

    private SoundEvent sound;

    private float pitch;

    private float volume;

    private String state;

    public LivingEntity getEntity() {
        return this.entity;
    }

    public EquipmentSlot getSlot() {
        return this.slot;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public String getUseState() {
        return this.state;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public SoundEvent getSound() {
        return this.sound;
    }

    public void setSound(SoundEvent sound) {
        this.sound = sound;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setNoParticles() {
        this.color = new Color(0, 0, 0, 0);
    }

    public void setNoSound() {
        this.sound = null;
        this.volume = 0.0F;
        this.pitch = 0.0F;
    }

    public boolean hasSound() {
        return this.sound != null;
    }

    public boolean hasParticles() {
        return this.color.getAlpha() > 0;
    }

    public ItemVisualEvent(LivingEntity entity, EquipmentSlot slot, ItemStack item, Color color, SoundEvent sound, float pitch, float volume, String state) {
        this.entity = entity;
        this.slot = slot;
        this.item = item;
        this.color = color;
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
        this.state = state;
    }
}