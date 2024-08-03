package com.github.alexthe666.citadel.client.event;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;

@OnlyIn(Dist.CLIENT)
@HasResult
public class EventPosePlayerHand extends Event {

    private LivingEntity entityIn;

    private HumanoidModel model;

    private boolean left;

    public EventPosePlayerHand(LivingEntity entityIn, HumanoidModel model, boolean left) {
        this.entityIn = entityIn;
        this.model = model;
        this.left = left;
    }

    public Entity getEntityIn() {
        return this.entityIn;
    }

    public HumanoidModel getModel() {
        return this.model;
    }

    public boolean isLeftHand() {
        return this.left;
    }
}